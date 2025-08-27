package com.norwood.mcheli;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.helper.world.MCH_ExplosionV2;
import com.norwood.mcheli.lweapon.MCH_ClientLightWeaponTickHandler;
import com.norwood.mcheli.wrapper.W_Reflection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class MCH_CommonPacketHandler {
    @HandleSide({Side.CLIENT})
    public static void onPacketEffectExplosion(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketEffectExplosion pkt = new MCH_PacketEffectExplosion();
            pkt.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        Entity exploder = null;
                        if (player.getDistanceSq(pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ) <= 40000.0) {
                            if (!pkt.prm.inWater) {
                                if (!MCH_Config.DefaultExplosionParticle.prmBool) {
                                    List<BlockPos> affectedPositions = pkt.prm.getAffectedBlockPositions();
                                    MCH_ExplosionV2.effectMODExplosion(player.world, pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ, pkt.prm.size, affectedPositions);
                                } else {
                                    List<BlockPos> affectedPositions = pkt.prm.getAffectedBlockPositions();
                                    MCH_ExplosionV2.effectVanillaExplosion(player.world, pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ, pkt.prm.size, affectedPositions);
                                }
                            } else {
                                MCH_ExplosionV2.effectExplosionInWater(player.world, pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ, pkt.prm.size);
                            }
                        }
                    }
            );
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketIndOpenScreen(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketIndOpenScreen pkt = new MCH_PacketIndOpenScreen();
            pkt.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        if (pkt.guiID == 3) {
                            MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
                            if (ac != null) {
                                ac.displayInventory(player);
                            }
                        } else {
                            player.openGui(
                                    MCH_MOD.instance, pkt.guiID, player.world, (int) player.posX, (int) player.posY, (int) player.posZ
                            );
                        }
                    }
            );
        }
    }

    @HandleSide({Side.CLIENT})
    public static void onPacketNotifyServerSettings(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketNotifyServerSettings pkt = new MCH_PacketNotifyServerSettings();
            pkt.readData(data);
            scheduler.addScheduledTask(() -> {
                MCH_Lib.DbgLog(false, "onPacketNotifyServerSettings:" + player);
                if (!pkt.enableCamDistChange) {
                    W_Reflection.setThirdPersonDistance(4.0F);
                }

                MCH_ServerSettings.enableCamDistChange = pkt.enableCamDistChange;
                MCH_ServerSettings.enableEntityMarker = pkt.enableEntityMarker;
                MCH_ServerSettings.enablePVP = pkt.enablePVP;
                MCH_ServerSettings.stingerLockRange = pkt.stingerLockRange;
                MCH_ServerSettings.enableDebugBoundingBox = pkt.enableDebugBoundingBox;
                MCH_ServerSettings.enableRotationLimit = pkt.enableRotationLimit;
                MCH_ServerSettings.pitchLimitMax = pkt.pitchLimitMax;
                MCH_ServerSettings.pitchLimitMin = pkt.pitchLimitMin;
                MCH_ServerSettings.rollLimit = pkt.rollLimit;
                MCH_ClientLightWeaponTickHandler.lockRange = MCH_ServerSettings.stingerLockRange;
            });
        }
    }

    @HandleSide({Side.CLIENT, Side.SERVER})
    public static void onPacketNotifyLock(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        MCH_PacketNotifyLock pkt = new MCH_PacketNotifyLock();
        pkt.readData(data);
        if (!player.world.isRemote) {
            if (pkt.entityID >= 0) {
                scheduler.addScheduledTask(() -> {
                    Entity target = player.world.getEntityByID(pkt.entityID);
                    if (target != null) {
                        MCH_EntityAircraft ac;
                        if (target instanceof MCH_EntityAircraft) {
                            ac = (MCH_EntityAircraft) target;
                        } else if (target instanceof MCH_EntitySeat) {
                            ac = ((MCH_EntitySeat) target).getParent();
                        } else {
                            ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(target);
                        }

                        if (ac != null && ac.haveFlare() && !ac.isDestroyed()) {
                            for (int i = 0; i < 2; i++) {
                                Entity entity = ac.getEntityBySeatId(i);
                                if (entity instanceof EntityPlayerMP) {
                                    MCH_PacketNotifyLock.sendToPlayer((EntityPlayerMP) entity);
                                }
                            }
                        } else if (target.getRidingEntity() != null && target instanceof EntityPlayerMP) {
                            MCH_PacketNotifyLock.sendToPlayer((EntityPlayerMP) target);
                        }
                    }
                });
            }
        } else {
            scheduler.addScheduledTask(() -> MCH_MOD.proxy.clientLocked());
        }
    }
}
