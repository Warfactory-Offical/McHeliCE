package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class PacketNotifyTVMissileEntity extends PacketBase implements ServerToClientPacket {

    public int entityID_Ac = -1;
    public int entityID_TVMissile = -1;

    public static void send(int heliEntityID, int tvMissileEntityID) {
        var packet = new PacketNotifyTVMissileEntity();
        packet.entityID_Ac = heliEntityID;
        packet.entityID_TVMissile = tvMissileEntityID;
        packet.sendToClients();
    }

    @Override
    public void onReceive(Minecraft mc) {
        if (mc.player.world.isRemote) {
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
}
