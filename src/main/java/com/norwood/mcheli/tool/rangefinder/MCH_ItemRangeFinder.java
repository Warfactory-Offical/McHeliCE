package com.norwood.mcheli.tool.rangefinder;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.multiplay.MCH_PacketIndSpotEntity;
import com.norwood.mcheli.wrapper.W_Item;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.W_Reflection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_ItemRangeFinder extends W_Item {
   public static int rangeFinderUseCooldown = 0;
   public static boolean continueUsingItem = false;
   public static float zoom = 2.0F;
   public static int mode = 0;

   public MCH_ItemRangeFinder(int itemId) {
      super(itemId);
      this.field_77777_bU = 1;
      this.func_77656_e(10);
   }

   public static boolean canUse(EntityPlayer player) {
      if (player == null) {
         return false;
      } else if (player.world == null) {
         return false;
      } else if (player.func_184614_ca().func_190926_b()) {
         return false;
      } else if (!(player.func_184614_ca().func_77973_b() instanceof MCH_ItemRangeFinder)) {
         return false;
      } else if (player.func_184187_bx() instanceof MCH_EntityAircraft) {
         return false;
      } else {
         if (player.func_184187_bx() instanceof MCH_EntitySeat) {
            MCH_EntityAircraft ac = ((MCH_EntitySeat)player.func_184187_bx()).getParent();
            if (ac != null && (ac.getIsGunnerMode(player) || ac.getWeaponIDBySeatID(ac.getSeatIdByEntity(player)) >= 0)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isUsingScope(EntityPlayer player) {
      return player.func_184612_cw() > 8 || continueUsingItem;
   }

   public static void onStartUseItem() {
      zoom = 2.0F;
      W_Reflection.setCameraZoom(2.0F);
      continueUsingItem = true;
   }

   public static void onStopUseItem() {
      W_Reflection.restoreCameraZoom();
      continueUsingItem = false;
   }

   @SideOnly(Side.CLIENT)
   public void spotEntity(EntityPlayer player, ItemStack itemStack) {
      if (player != null && player.world.isRemote && rangeFinderUseCooldown == 0 && player.func_184612_cw() > 8) {
         if (mode == 2) {
            rangeFinderUseCooldown = 60;
            MCH_PacketIndSpotEntity.send(player, 0);
         } else if (itemStack.func_77960_j() < itemStack.func_77958_k()) {
            rangeFinderUseCooldown = 60;
            MCH_PacketIndSpotEntity.send(player, mode == 0 ? 60 : 3);
         } else {
            W_McClient.MOD_playSoundFX("ng", 1.0F, 1.0F);
         }
      }

   }

   public void func_77615_a(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
      if (worldIn.isRemote) {
         onStopUseItem();
      }

   }

   public ItemStack func_77654_b(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
      return stack;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77662_d() {
      return true;
   }

   public EnumAction func_77661_b(ItemStack itemStack) {
      return EnumAction.BOW;
   }

   public int func_77626_a(ItemStack itemStack) {
      return 72000;
   }

   public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
      ItemStack itemstack = player.func_184586_b(handIn);
      if (canUse(player)) {
         player.func_184598_c(handIn);
      }

      return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
   }
}
