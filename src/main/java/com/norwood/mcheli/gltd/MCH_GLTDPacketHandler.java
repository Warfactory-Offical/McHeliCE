package com.norwood.mcheli.gltd;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.networking.packet.MCH_PacketGLTDPlayerControl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_GLTDPacketHandler {
    @HandleSide({Side.SERVER})
    public static void onPacket_GLTDPlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.getRidingEntity() instanceof MCH_EntityGLTD gltd) {
            if (!player.world.isRemote) {
                MCH_PacketGLTDPlayerControl pc = new MCH_PacketGLTDPlayerControl();
                pc.readData(data);
                scheduler.addScheduledTask(() -> {
                    if (pc.unmount) {
                        Entity riddenByEntity = gltd.getRiddenByEntity();
                        if (riddenByEntity != null) {
                            riddenByEntity.dismountRidingEntity();
                        }
                    } else {
                        if (pc.switchCameraMode >= 0) {
                            gltd.camera.setMode(0, pc.switchCameraMode);
                        }

                        if (pc.switchWeapon >= 0) {
                            gltd.switchWeapon(pc.switchWeapon);
                        }

                        if (pc.useWeapon) {
                            gltd.useCurrentWeapon(pc.useWeaponOption1, pc.useWeaponOption2);
                        }
                    }
                });
            }
        }
    }
}
