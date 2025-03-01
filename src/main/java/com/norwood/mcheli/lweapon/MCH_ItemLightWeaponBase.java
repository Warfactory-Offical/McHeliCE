package com.norwood.mcheli.lweapon;

import javax.annotation.Nullable;
import com.norwood.mcheli.wrapper.W_Item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MCH_ItemLightWeaponBase extends W_Item {
   public final MCH_ItemLightWeaponBullet bullet;

   public MCH_ItemLightWeaponBase(int par1, MCH_ItemLightWeaponBullet bullet) {
      super(par1);
      this.func_77656_e(10);
      this.func_77625_d(1);
      this.bullet = bullet;
   }

   public static String getName(ItemStack itemStack) {
      if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemLightWeaponBase) {
         String name = itemStack.func_77977_a();
         int li = name.lastIndexOf(":");
         if (li >= 0) {
            name = name.substring(li + 1);
         }

         return name;
      } else {
         return "";
      }
   }

   public static boolean isHeld(@Nullable EntityPlayer player) {
      ItemStack is = player != null ? player.func_184614_ca() : ItemStack.field_190927_a;
      if (!is.func_190926_b() && is.func_77973_b() instanceof MCH_ItemLightWeaponBase) {
         return player.func_184612_cw() > 10;
      } else {
         return false;
      }
   }

   public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
      PotionEffect pe = player.func_70660_b(MobEffects.field_76439_r);
      if (pe != null && pe.func_76459_b() < 220) {
         player.func_70690_d(new PotionEffect(MobEffects.field_76439_r, 250, 0, false, false));
      }

   }

   public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
      return true;
   }

   public EnumAction func_77661_b(ItemStack par1ItemStack) {
      return EnumAction.BOW;
   }

   public int func_77626_a(ItemStack par1ItemStack) {
      return 72000;
   }

   public ActionResult<ItemStack> func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
      ItemStack itemstack = playerIn.func_184586_b(handIn);
      if (!itemstack.func_190926_b()) {
         playerIn.func_184598_c(handIn);
      }

      return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
   }
}
