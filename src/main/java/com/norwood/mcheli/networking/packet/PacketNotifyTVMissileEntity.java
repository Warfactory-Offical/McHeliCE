package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

@ElegantPacket
@RequiredArgsConstructor
public class PacketNotifyTVMissileEntity extends PacketBase implements ServerToClientPacket {

    final public int entityID_Ac;
    final public int entityID_TVMissile;

    @Override
    public void onReceive(Minecraft mc) {
            if (this.entityID_Ac <= 0 || this.entityID_TVMissile <= 0) {
                return;
            }
            getScheduler().addScheduledTask(() -> {
                Entity e = mc.player.world.getEntityByID(this.entityID_Ac);
                if (e instanceof MCH_EntityAircraft ac) {
                    e = mc.player.world.getEntityByID(this.entityID_TVMissile);
                    if (e instanceof MCH_EntityTvMissile) {
                        ((MCH_EntityTvMissile) e).shootingEntity = mc.player;
                        ac.setTVMissile((MCH_EntityTvMissile) e);
                    }
                }
            });
    }
}
