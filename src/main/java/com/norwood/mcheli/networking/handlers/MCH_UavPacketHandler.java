package com.norwood.mcheli.networking.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.networking.packet.MCH_UavPacketStatus;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_UavPacketHandler {
    @HandleSide({Side.SERVER})
    public static void onPacketUavStatus(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_UavPacketStatus status = new MCH_UavPacketStatus();
            status.readData(data);
            scheduler.addScheduledTask(() -> {
                if (player.getRidingEntity() instanceof MCH_EntityUavStation) {
                    ((MCH_EntityUavStation) player.getRidingEntity()).setUavPosition(status.posUavX, status.posUavY, status.posUavZ);
                    if (status.continueControl) {
                        ((MCH_EntityUavStation) player.getRidingEntity()).controlLastAircraft(player);
                    }
                }
            });
        }
    }
}
