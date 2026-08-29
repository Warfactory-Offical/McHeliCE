package com.norwood.mcheli.networking.packet;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ElegantPacket
@AllArgsConstructor
public class PacketChaffUse implements ServerToClientPacket {


    final public int acId;
    final public int time;

    @Override
    @SideOnly(Side.CLIENT)
    public void onReceive(Minecraft mc) {
        Entity e = mc.player.world.getEntityByID(acId);
        if(e instanceof MCH_EntityAircraft) {
            ((MCH_EntityAircraft) e).chaffUseTime = time;
        }
    }
}
