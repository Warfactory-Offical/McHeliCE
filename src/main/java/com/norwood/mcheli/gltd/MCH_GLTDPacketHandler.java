package com.norwood.mcheli.gltd;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.__helper.network.HandleSide;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_GLTDPacketHandler {
   @HandleSide({Side.SERVER})
   public static void onPacket_GLTDPlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.func_184187_bx() instanceof MCH_EntityGLTD) {
         if (!player.field_70170_p.field_72995_K) {
            MCH_PacketGLTDPlayerControl pc = new MCH_PacketGLTDPlayerControl();
            pc.readData(data);
            scheduler.func_152344_a(() -> {
               MCH_EntityGLTD gltd = (MCH_EntityGLTD)player.func_184187_bx();
               if (pc.unmount) {
                  Entity riddenByEntity = gltd.getRiddenByEntity();
                  if (riddenByEntity != null) {
                     riddenByEntity.func_184210_p();
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
