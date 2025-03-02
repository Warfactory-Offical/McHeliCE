package com.norwood.mcheli.lweapon;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.network.HandleSide;
import com.norwood.mcheli.weapon.MCH_IEntityLockChecker;
import com.norwood.mcheli.weapon.MCH_WeaponBase;
import com.norwood.mcheli.weapon.MCH_WeaponCreator;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import com.norwood.mcheli.wrapper.W_EntityPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_LightWeaponPacketHandler {
   @HandleSide({Side.SERVER})
   public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.world.isRemote) {
         MCH_PacketLightWeaponPlayerControl pc = new MCH_PacketLightWeaponPlayerControl();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            if (pc.camMode == 1) {
               player.func_184589_d(MobEffects.field_76439_r);
            }

            ItemStack is = player.func_184614_ca();
            if (!is.func_190926_b()) {
               if (is.func_77973_b() instanceof MCH_ItemLightWeaponBase) {
                  MCH_ItemLightWeaponBase lweapon = (MCH_ItemLightWeaponBase)is.func_77973_b();
                  if (pc.camMode == 2 && MCH_ItemLightWeaponBase.isHeld(player)) {
                     player.func_70690_d(new PotionEffect(MobEffects.field_76439_r, 255, 0, false, false));
                  }

                  if (pc.camMode > 0) {
                     MCH_Lib.DbgLog(false, "MCH_LightWeaponPacketHandler NV=%s", pc.camMode == 2 ? "ON" : "OFF");
                  }

                  if (pc.useWeapon && is.func_77960_j() < is.func_77958_k()) {
                     String name = MCH_ItemLightWeaponBase.getName(player.func_184614_ca());
                     MCH_WeaponBase w = MCH_WeaponCreator.createWeapon(player.world, name, Vec3d.ZERO, 0.0F, 0.0F, (MCH_IEntityLockChecker)null, false);
                     MCH_WeaponParam prm = new MCH_WeaponParam();
                     prm.entity = player;
                     prm.user = player;
                     prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, player.field_70177_z, player.field_70125_A);
                     prm.option1 = pc.useWeaponOption1;
                     prm.option2 = pc.useWeaponOption2;
                     w.shot(prm);
                     if (!player.field_71075_bZ.field_75098_d && is.func_77958_k() == 1) {
                        is.func_190918_g(1);
                     }

                     if (is.func_77958_k() > 1) {
                        is.func_77964_b(is.func_77958_k());
                     }
                  } else if (pc.cmpReload > 0 && is.func_77960_j() > 1 && W_EntityPlayer.hasItem(player, lweapon.bullet)) {
                     if (!player.field_71075_bZ.field_75098_d) {
                        W_EntityPlayer.consumeInventoryItem(player, lweapon.bullet);
                     }

                     is.func_77964_b(0);
                  }

               }
            }
         });
      }
   }
}
