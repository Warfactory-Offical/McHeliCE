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
public class PacketIronCurtainUse implements ServerToClientPacket {

    public final int acId;
    public final int time;

    @Override
    @SideOnly(Side.CLIENT)
    public void onReceive(Minecraft mc) {
        Entity e = mc.world.getEntityByID(acId);
        if (e instanceof MCH_EntityAircraft aircraft) {
            aircraft.ironCurtainRunningTick = time;
        }
    }
}
