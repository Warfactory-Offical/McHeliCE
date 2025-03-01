package com.norwood.mcheli.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class MCH_ConfigGuiContainer extends Container {
   public final EntityPlayer player;

   public MCH_ConfigGuiContainer(EntityPlayer player) {
      this.player = player;
   }

   public void func_75142_b() {
      super.func_75142_b();
   }

   public boolean func_75145_c(EntityPlayer player) {
      return true;
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
      return ItemStack.field_190927_a;
   }
}
