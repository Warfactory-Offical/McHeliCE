package com.norwood.mcheli.vehicle;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.__helper.network.HandleSide;
import com.norwood.mcheli.chain.MCH_EntityChain;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_VehiclePacketHandler {
   @HandleSide({Side.SERVER})
   public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.getRidingEntity() instanceof MCH_EntityVehicle) {
         if (!player.world.isRemote) {
            MCH_PacketVehiclePlayerControl pc = new MCH_PacketVehiclePlayerControl();
            pc.readData(data);
            scheduler.addScheduledTask(() -> {
               MCH_EntityVehicle vehicle = (MCH_EntityVehicle)player.getRidingEntity();
               if (pc.isUnmount == 1) {
                  vehicle.unmountEntity();
               } else if (pc.isUnmount == 2) {
                  vehicle.unmountCrew();
               } else {
                  if (pc.switchSearchLight) {
                     vehicle.setSearchLight(!vehicle.isSearchLightON());
                  }

                  if (pc.switchCameraMode > 0) {
                     vehicle.switchCameraMode(player, pc.switchCameraMode - 1);
                  }

                  if (pc.switchWeapon >= 0) {
                     vehicle.switchWeapon(player, pc.switchWeapon);
                  }

                  if (pc.useWeapon) {
                     MCH_WeaponParam prm = new MCH_WeaponParam();
                     prm.entity = vehicle;
                     prm.user = player;
                     prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, 0.0F, 0.0F);
                     prm.option1 = pc.useWeaponOption1;
                     prm.option2 = pc.useWeaponOption2;
                     vehicle.useCurrentWeapon(prm);
                  }

                  if (vehicle.isPilot(player)) {
                     vehicle.throttleUp = pc.throttleUp;
                     vehicle.throttleDown = pc.throttleDown;
                     vehicle.moveLeft = pc.moveLeft;
                     vehicle.moveRight = pc.moveRight;
                  }

                  if (pc.useFlareType > 0) {
                     vehicle.useFlare(pc.useFlareType);
                  }

                  if (pc.unhitchChainId >= 0) {
                     Entity e = player.world.getEntityByID(pc.unhitchChainId);
                     if (e instanceof MCH_EntityChain) {
                        e.setDead();
                     }
                  }

                  if (pc.openGui) {
                     vehicle.openGui(player);
                  }

                  if (pc.switchHatch > 0) {
                     vehicle.foldHatch(pc.switchHatch == 2);
                  }

                  if (pc.isUnmount == 3) {
                     vehicle.unmountAircraft();
                  }

                  if (pc.switchGunnerStatus) {
                     vehicle.setGunnerStatus(!vehicle.getGunnerStatus());
                  }
               }
            });
         }
      }
   }
}
