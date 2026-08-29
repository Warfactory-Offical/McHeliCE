package com.norwood.mcheli.wingman.handler;
//WINGMAN — file introduced for the McHeli Wingman feature merge

import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.uav.IUavStation;
import com.norwood.mcheli.uav.UAVTracker;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import com.norwood.mcheli.wingman.McHeliWingman;
import com.norwood.mcheli.wingman.config.WingmanConfig;
import com.norwood.mcheli.wingman.util.McheliReflect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Streams terrain chunks around a UAV to its operating/viewing player — specifically the chunks
 * outside the player's normal view radius — so the client renders the ground beneath a distant UAV
 * instead of void. Two sources are handled:
 *
 * <ul>
 *   <li><b>Controlled UAVs</b>: a UAV whose station has a player rider (actively flown).</li>
 *   <li><b>Previewed UAVs</b>: the UAV currently selected in a player's open UAV-station screen, so
 *       the live camera-feed viewport renders real terrain before the player connects. Preview
 *       targets are registered by {@code PacketUavPreviewSelect} and auto-expire when the client
 *       stops sending heartbeats (i.e. the screen closed).</li>
 * </ul>
 *
 * <p>Reconciliation is done per player over the union of their desired chunks, so a player who
 * controls one UAV and previews another never double-manages a shared chunk. Runs on Phase.END so
 * entity positions are final, and only manages out-of-view chunks (avoiding spurious
 * SPacketUnloadChunk for in-view chunks that would cause warps).
 *
 * <p>{@code PlayerChunkMap#playerViewRadius} and {@code #getOrCreateEntry} are widened to public via
 * mcheli_at.cfg (Access Transformer) rather than reflection.
 */
public class UavChunkStreamer {

    /**
     * Streaming radius (chunks) around a UAV that is only being PREVIEWED in a station screen, not
     * actively controlled — just enough terrain for the small camera-feed viewport. An
     * actively-controlled UAV instead streams the operator's full view radius (already clamped to the
     * server's view distance) so it loads the same area a player would.
     */
    private static final int PREVIEW_STREAM_RADIUS = 2;

    /**
     * While a player is actively controlling a UAV, keep only this radius of chunks around their
     * (station-riding) body — 0 = just the chunk they are in. The rest of their body view square is
     * wasted while their view is at the UAV, so we drop it to save bandwidth/server load.
     */
    private static final int BODY_KEEP_RADIUS = 0;

    /** A preview target expires this many ticks after its last heartbeat (screen closed). */
    private static final int PREVIEW_TIMEOUT_TICKS = 60;

    /** playerUUID -> UAV being previewed (set by PacketUavPreviewSelect). */
    private static final Map<UUID, PreviewReq> PREVIEW = new ConcurrentHashMap<>();

    /**
     * Snapshot of the out-of-view chunks streamed to each player this tick, keyed by packed chunk
     * coordinate. Lets the entity tracker make entities inside a streamed region visible to that
     * player even though their body is far away (camera-aware visibility — read by
     * {@code TrackerHook#isVisibleFromViewOrigin}). Replaced wholesale each tick on the server thread.
     */
    private static volatile Map<UUID, Set<Long>> STREAMED_SNAPSHOT = Collections.emptyMap();

    /** Out-of-view chunk subscriptions we have added, keyed by player UUID. */
    private final Map<UUID, Set<Long>> subscribed = new HashMap<>();

    /** Body-square chunks we have removed a controlling player from (to restore when control ends). */
    private final Map<UUID, Set<Long>> suppressed = new HashMap<>();

    /** Entity-visible chunk set published for each player last tick. */
    private final Map<UUID, Set<Long>> lastVisible = new HashMap<>();

    /** Last (body, UAV) chunk positions a controller's body-suppression was computed for. */
    private final Map<UUID, Ctl> lastCtl = new HashMap<>();

    /** Registers/refreshes the UAV a player is previewing in the station screen. */
    public static void setPreview(UUID playerId, UUID uavId, long worldTick) {
        if (playerId != null && uavId != null) {
            PREVIEW.put(playerId, new PreviewReq(uavId, worldTick));
        }
    }

    public static void clearPreview(UUID playerId) {
        if (playerId != null) {
            PREVIEW.remove(playerId);
        }
    }

    /**
     * @return true if the given chunk is currently being streamed to the player around a UAV they
     * control or preview. Backs camera-aware entity visibility: an entity in such a chunk is shown
     * to the player even though their body is out of range.
     */
    public static boolean isChunkStreamedTo(UUID playerId, int chunkX, int chunkZ) {
        if (playerId == null) {
            return false;
        }
        Set<Long> set = STREAMED_SNAPSHOT.get(playerId);
        return set != null && set.contains(ChunkPos.asLong(chunkX, chunkZ));
    }

    /**
     * @return true if <b>this specific player</b> is previewing the given UAV.
     */
    public static boolean isPreviewedBy(UUID uavId, UUID playerId) {
        if (uavId == null || playerId == null) {
            return false;
        }
        PreviewReq req = PREVIEW.get(playerId);
        return req != null && uavId.equals(req.uavId());
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote || event.phase != TickEvent.Phase.END) return;
        tickChunkSubscriptions((WorldServer) event.world);
    }

    private void tickChunkSubscriptions(WorldServer ws) {
        PlayerChunkMap pcm = ws.getPlayerChunkMap();
        int viewRadius = pcm.playerViewRadius;
        long nowTick = ws.getTotalWorldTime();
        MinecraftServer server = ws.getMinecraftServer();

        Map<UUID, EntityPlayerMP> players = new HashMap<>();
        Map<UUID, Set<Long>> desired = new HashMap<>();
        Map<UUID, Set<Long>> entityVisible = new HashMap<>();
        Map<UUID, Map<Long, Integer>> terrainCovered = new HashMap<>();
        Map<UUID, Map<Long, Integer>> visibleCovered = new HashMap<>();
        Map<UUID, Ctl> controllers = new HashMap<>();

        boolean streamTvMissiles = WingmanConfig.tvMissileChunkLoad;

        for (Entity entity : new ArrayList<>(ws.loadedEntityList)) {
            if (entity.isDead) continue;

            if (McheliReflect.isAircraft(entity)) {
                IUavStation station = McheliReflect.getUavStation(entity);
                if (station == null) continue;
                Entity rider = McheliReflect.getStationRider(station);
                if (!(rider instanceof EntityPlayerMP player) || player.isDead) continue;
                addDesired(desired, entityVisible, terrainCovered, visibleCovered, true, players, player,
                        entity.posX, entity.posZ, viewRadius, viewRadius);
                controllers.put(player.getUniqueID(), new Ctl(
                        (int) Math.floor(player.posX / 16.0), (int) Math.floor(player.posZ / 16.0),
                        (int) Math.floor(entity.posX / 16.0), (int) Math.floor(entity.posZ / 16.0)));
                continue;
            }

            if (streamTvMissiles && entity instanceof MCH_EntityTvMissile tv) {
                if (!(tv.shootingEntity instanceof EntityPlayerMP player) || player.isDead) continue;
                if (player.world != ws) continue;
                addDesired(desired, entityVisible, terrainCovered, visibleCovered,
                        MCH_MOD.DEBUG_RENDER_TRAJECTORY_ENTITIES, players, player,
                        tv.posX, tv.posZ, viewRadius, viewRadius);
            }
        }

        for (Iterator<Map.Entry<UUID, PreviewReq>> it = PREVIEW.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<UUID, PreviewReq> e = it.next();
            PreviewReq req = e.getValue();
            if (nowTick - req.heartbeatTick() > PREVIEW_TIMEOUT_TICKS) {
                it.remove();
                continue;
            }
            EntityPlayerMP player = server == null ? null : server.getPlayerList().getPlayerByUUID(e.getKey());
            if (player == null || player.world != ws) continue;
            double[] pos = locateUavXZ(ws, req.uavId());
            if (pos == null) continue;
            addDesired(desired, entityVisible, terrainCovered, visibleCovered, true, players, player,
                    pos[0], pos[1], PREVIEW_STREAM_RADIUS, viewRadius);
        }

        Set<UUID> all = new HashSet<>(this.subscribed.keySet());
        all.addAll(desired.keySet());

        Map<UUID, Set<Long>> snapshot = new HashMap<>();
        Set<UUID> visibilityDirty = new HashSet<>();

        for (UUID pid : all) {
            EntityPlayerMP player = players.get(pid);
            if (player == null) {
                // Not controlling/previewing this tick, but they may still be in the world — e.g. they
                // just dismounted the station (shift). Resolve them so we actually RELEASE the chunks we
                // streamed; otherwise no SPacketUnloadChunk is sent and the client keeps hundreds of far
                // chunks loaded, which lags hard. Only when fully offline do we drop bookkeeping and let
                // onPlayerLogout release the entries.
                player = server == null ? null : server.getPlayerList().getPlayerByUUID(pid);
                if (player == null) {
                    this.subscribed.remove(pid);
                    this.lastVisible.remove(pid);
                    continue;
                }
            }

            boolean inThisWorld = player.world == ws;
            Set<Long> want = inThisWorld ? desired.getOrDefault(pid, Collections.emptySet())
                                         : Collections.emptySet();
            Set<Long> have = this.subscribed.getOrDefault(pid, Collections.emptySet());

            if (!have.equals(want)) {
                int plCX = (int) Math.floor(player.posX / 16.0);
                int plCZ = (int) Math.floor(player.posZ / 16.0);

                for (long old : have) {
                    if (want.contains(old)) {
                        continue;
                    }
                    int cx = chunkX(old);
                    int cz = chunkZ(old);
                    if (inThisWorld && isInViewRange(cx, cz, plCX, plCZ, viewRadius)) {
                        continue;
                    }
                    removePlayerFromEntry(pcm, player, cx, cz);
                }
                for (long pos : want) {
                    if (!have.contains(pos)) {
                        addPlayerToEntry(pcm, player, chunkX(pos), chunkZ(pos));
                    }
                }

                if (want.isEmpty()) {
                    this.subscribed.remove(pid);
                } else {
                    this.subscribed.put(pid, new HashSet<>(want));
                }
            }

            Set<Long> ev = entityVisible.get(pid);
            Set<Long> vis;
            if (want.isEmpty() || ev == null || ev.isEmpty()) {
                vis = Collections.emptySet();
            } else {
                vis = new HashSet<>(want);
                vis.retainAll(ev);
            }

            Set<Long> prevVis = this.lastVisible.getOrDefault(pid, Collections.emptySet());
            if (!prevVis.equals(vis)) {
                visibilityDirty.add(pid);
                if (vis.isEmpty()) {
                    this.lastVisible.remove(pid);
                } else {
                    this.lastVisible.put(pid, vis);
                }
            }
            if (!vis.isEmpty()) {
                snapshot.put(pid, vis);
            }
        }

        STREAMED_SNAPSHOT = snapshot;

        for (UUID pid : visibilityDirty) {
            EntityPlayerMP p = server == null ? null : server.getPlayerList().getPlayerByUUID(pid);
            if (p != null && p.world == ws) {
                ws.getEntityTracker().updateVisibility(p);
            }
        }

        // (5) Cost-cut: a player actively controlling a UAV is looking through the UAV, so the chunk
        // square around their (station-riding) body is wasted. Keep only BODY_KEEP_RADIUS chunks around
        // the body and drop the rest — except any chunk the UAV camera can still see (so a near UAV
        // doesn't blank the operator's surroundings). Everything is restored when control ends.
        Set<UUID> suppressTouched = new HashSet<>(this.suppressed.keySet());
        suppressTouched.addAll(controllers.keySet());
        for (UUID pid : suppressTouched) {
            Ctl ctl = controllers.get(pid);
            if (ctl != null && ctl.equals(this.lastCtl.get(pid))) {
                continue; // controller hasn't moved (body & UAV chunk unchanged) — already applied
            }

            EntityPlayerMP player = server == null ? null : server.getPlayerList().getPlayerByUUID(pid);
            boolean here = player != null && player.world == ws;

            Set<Long> streamed = this.subscribed.getOrDefault(pid, Collections.emptySet());
            Set<Long> nowSuppress = new HashSet<>();
            if (ctl != null && here) {
                for (int dx = -viewRadius; dx <= viewRadius; dx++) {
                    for (int dz = -viewRadius; dz <= viewRadius; dz++) {
                        if (Math.abs(dx) <= BODY_KEEP_RADIUS && Math.abs(dz) <= BODY_KEEP_RADIUS) {
                            continue; // keep the body core (the chunk(s) the operator/station sit in)
                        }
                        int cx = ctl.bodyX() + dx;
                        int cz = ctl.bodyZ() + dz;
                        if (isInViewRange(cx, cz, ctl.uavX(), ctl.uavZ(), viewRadius)) {
                            continue; // the UAV camera can see this chunk — must stay loaded
                        }
                        long key = ChunkPos.asLong(cx, cz);
                        if (streamed.contains(key)) {
                            continue;
                        }
                        nowSuppress.add(key);
                    }
                }
            }

            Set<Long> wasSuppress = this.suppressed.getOrDefault(pid, Collections.emptySet());
            if (here) {
                for (long c : wasSuppress) {
                    if (!nowSuppress.contains(c)) {
                        addPlayerToEntry(pcm, player, chunkX(c), chunkZ(c));
                    }
                }
                for (long c : nowSuppress) {
                    if (!wasSuppress.contains(c)) {
                        removePlayerFromEntry(pcm, player, chunkX(c), chunkZ(c));
                    }
                }
            }
            // If the player left this world, vanilla's dimension/logout sweep handles their entries.

            if (nowSuppress.isEmpty()) {
                this.suppressed.remove(pid);
            } else {
                this.suppressed.put(pid, nowSuppress);
            }
            if (ctl != null) {
                this.lastCtl.put(pid, ctl);
            } else {
                this.lastCtl.remove(pid);
            }
        }
    }

    private void addDesired(Map<UUID, Set<Long>> desired, Map<UUID, Set<Long>> entityVisible,
                            Map<UUID, Map<Long, Integer>> terrainCovered,
                            Map<UUID, Map<Long, Integer>> visibleCovered,
                            boolean revealEntities, Map<UUID, EntityPlayerMP> players,
                            EntityPlayerMP player, double x, double z, int streamRadius, int viewRadius) {
        UUID pid = player.getUniqueID();
        players.put(pid, player);

        int acCX = (int) Math.floor(x / 16.0);
        int acCZ = (int) Math.floor(z / 16.0);
        long center = ChunkPos.asLong(acCX, acCZ);

        Map<Long, Integer> tCov = terrainCovered.computeIfAbsent(pid, k -> new HashMap<>());
        Integer tPrev = tCov.get(center);
        boolean needTerrain = tPrev == null || tPrev < streamRadius;

        Map<Long, Integer> vCov = null;
        boolean needVisible = false;
        if (revealEntities) {
            vCov = visibleCovered.computeIfAbsent(pid, k -> new HashMap<>());
            Integer vPrev = vCov.get(center);
            needVisible = vPrev == null || vPrev < streamRadius;
        }

        if (!needTerrain && !needVisible) {
            return;
        }

        int plCX = (int) Math.floor(player.posX / 16.0);
        int plCZ = (int) Math.floor(player.posZ / 16.0);
        Set<Long> set = desired.computeIfAbsent(pid, k -> new HashSet<>());
        Set<Long> evSet = needVisible ? entityVisible.computeIfAbsent(pid, k -> new HashSet<>()) : null;

        for (int dx = -streamRadius; dx <= streamRadius; dx++) {
            for (int dz = -streamRadius; dz <= streamRadius; dz++) {
                int cx = acCX + dx;
                int cz = acCZ + dz;
                if (isInViewRange(cx, cz, plCX, plCZ, viewRadius)) {
                    continue;
                }
                long key = ChunkPos.asLong(cx, cz);
                if (needTerrain) {
                    set.add(key);
                }
                if (evSet != null) {
                    evSet.add(key);
                }
            }
        }

        if (needTerrain) {
            tCov.put(center, streamRadius);
        }
        if (needVisible) {
            vCov.put(center, streamRadius);
        }
    }

    /** Chunk-center XZ of a UAV by UUID (loaded entity, else its last-known position), or null. */
    private static double[] locateUavXZ(WorldServer ws, UUID uavId) {
        ChunkPos cp = UAVTracker.getUAVPos(ws, uavId);
        return cp == null ? null : new double[]{cp.x * 16 + 8, cp.z * 16 + 8};
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP player) {
            disengage(player);
        }
    }

    /**
     * A player can die while piloting/previewing a UAV. Death keeps the {@link EntityPlayerMP} alive
     * for the death screen but respawn swaps in a brand-new player instance — so, exactly like logout,
     * the streamed chunk entries must be released NOW, against the dying instance they still reference.
     * Otherwise reconcile (which by then sees the new respawned object) calls {@code removePlayer} on
     * the wrong instance, a no-op, and the far chunks leak forever — ghost-loaded by a discarded player
     * (vanilla's death/respawn sweep only covers the body view square, never our far chunks). The
     * station severs the control link + resets the camera on its own tick (dead rider -> unmountEntity);
     * this handles the streaming side, so the full disengagement runs on death just as on a dismount.
     */
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayerMP player) {
            disengage(player);
        }
    }

    /**
     * Streaming-side disengagement for a player whose object is about to be discarded (logout, or
     * death -> respawn). Releases every out-of-view chunk we streamed to them — these are the entries
     * vanilla never sweeps — while this is still the instance the entries reference, then drops all
     * bookkeeping. The suppressed body-square chunks need no restore: vanilla re-adds the fresh view
     * square to the leaving/respawned player, and its own sweep ignores chunks the player is already
     * out of.
     */
    private void disengage(EntityPlayerMP player) {
        UUID id = player.getUniqueID();
        Set<Long> streamed = this.subscribed.remove(id);
        if (streamed != null && player.world instanceof WorldServer ws) {
            PlayerChunkMap pcm = ws.getPlayerChunkMap();
            for (long c : streamed) {
                removePlayerFromEntry(pcm, player, chunkX(c), chunkZ(c));
            }
        }
        this.suppressed.remove(id);
        this.lastVisible.remove(id);
        this.lastCtl.remove(id);
        clearPreview(id);
    }

    private void addPlayerToEntry(PlayerChunkMap pcm, EntityPlayerMP player, int chunkX, int chunkZ) {
        try {
            PlayerChunkMapEntry entry = pcm.getOrCreateEntry(chunkX, chunkZ);
            if (entry != null && !entry.containsPlayer(player)) {
                entry.addPlayer(player);
                McHeliWingman.logger.debug("[UavChunkStreamer] + {} → chunk ({},{})",
                        player.getName(), chunkX, chunkZ);
            }
        } catch (Exception e) {
            McHeliWingman.logger.warn("[UavChunkStreamer] addPlayer ({},{}) failed: {}",
                    chunkX, chunkZ, e.getMessage());
        }
    }

    private void removePlayerFromEntry(PlayerChunkMap pcm, EntityPlayerMP player, int chunkX, int chunkZ) {
        try {
            PlayerChunkMapEntry entry = pcm.getEntry(chunkX, chunkZ);
            if (entry != null && entry.containsPlayer(player)) {
                entry.removePlayer(player);
                McHeliWingman.logger.debug("[UavChunkStreamer] - {} ← chunk ({},{})",
                        player.getName(), chunkX, chunkZ);
            }
        } catch (Exception e) {
            McHeliWingman.logger.warn("[UavChunkStreamer] removePlayer ({},{}) failed: {}",
                    chunkX, chunkZ, e.getMessage());
        }
    }

    private static int chunkX(long packed) {
        return (int) packed;
    }

    private static int chunkZ(long packed) {
        return (int) (packed >> 32);
    }

    private static boolean isInViewRange(int chunkX, int chunkZ, int plCX, int plCZ, int viewRadius) {
        return Math.abs(chunkX - plCX) <= viewRadius && Math.abs(chunkZ - plCZ) <= viewRadius;
    }

    private record PreviewReq(UUID uavId, long heartbeatTick) {
    }

    /** A controlling player's body chunk and the chunk their controlled UAV is in, this tick. */
    private record Ctl(int bodyX, int bodyZ, int uavX, int uavZ) {
    }
}
