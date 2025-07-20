package com.norwood.mcheli.command;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_CommandPacketHandler {
    @HandleSide({Side.CLIENT})
    public static void onPacketTitle(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player != null && player.world.isRemote) {
            MCH_PacketTitle req = new MCH_PacketTitle();
            req.readData(data);
            scheduler.addScheduledTask(() -> MCH_MOD.proxy.printChatMessage(req.chatComponent, req.showTime, req.position));
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketSave(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player != null && !player.world.isRemote) {
            MCH_PacketCommandSave req = new MCH_PacketCommandSave();
            req.readData(data);
            scheduler.addScheduledTask(() -> {
                MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
                if (ac != null) {
                    ac.setCommand(req.str, player);
                }
            });
        }
    }
}
