package com.norwood.mcheli.throwable;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.wrapper.W_Item;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class MCH_ItemThrowable extends W_Item {
   public MCH_ItemThrowable(int par1) {
      super(par1);
      this.func_77625_d(1);
   }

   public static void registerDispenseBehavior(Item item) {
      BlockDispenser.field_149943_a.func_82595_a(item, new MCH_ItemThrowableDispenseBehavior());
   }

   public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
      ItemStack itemstack = player.func_184586_b(handIn);
      player.func_184598_c(handIn);
      return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
   }

   public void func_77615_a(ItemStack itemStack, World world, EntityLivingBase entityLiving, int par4) {
      if (entityLiving instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entityLiving;
         if (!itemStack.func_190926_b() && itemStack.func_190916_E() > 0) {
            MCH_ThrowableInfo info = MCH_ThrowableInfoManager.get(itemStack.func_77973_b());
            if (info != null) {
               if (!player.field_71075_bZ.field_75098_d) {
                  itemStack.func_190918_g(1);
                  if (itemStack.func_190916_E() <= 0) {
                     player.field_71071_by.func_70299_a(player.field_71071_by.field_70461_c, ItemStack.field_190927_a);
                  }
               }

               world.func_184148_a((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187737_v, SoundCategory.PLAYERS, 0.5F, 0.4F / (field_77697_d.nextFloat() * 0.4F + 0.8F));
               if (!world.field_72995_K) {
                  float acceleration = 1.0F;
                  par4 = itemStack.func_77988_m() - par4;
                  if (par4 <= 35) {
                     if (par4 < 5) {
                        par4 = 5;
                     }

                     acceleration = (float)par4 / 25.0F;
                  }

                  MCH_Lib.DbgLog(world, "MCH_ItemThrowable.onPlayerStoppedUsing(%d)", par4);
                  MCH_EntityThrowable entity = new MCH_EntityThrowable(world, player, acceleration);
                  entity.func_184538_a(player, player.field_70125_A, player.field_70177_z, 0.0F, acceleration, 1.0F);
                  entity.setInfo(info);
                  world.func_72838_d(entity);
               }
            }
         }

      }
   }

   public int func_77626_a(ItemStack par1ItemStack) {
      return 72000;
   }

   public EnumAction func_77661_b(ItemStack par1ItemStack) {
      return EnumAction.BOW;
   }
}
