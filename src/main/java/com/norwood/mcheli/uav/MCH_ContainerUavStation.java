package com.norwood.mcheli.uav;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MCH_ContainerUavStation extends Container {
   protected MCH_EntityUavStation uavStation;

   public MCH_ContainerUavStation(InventoryPlayer inventoryPlayer, MCH_EntityUavStation te) {
      this.uavStation = te;
      this.func_75146_a(new Slot(this.uavStation, 0, 20, 20));
      this.bindPlayerInventory(inventoryPlayer);
   }

   public boolean func_75145_c(EntityPlayer player) {
      return this.uavStation.func_70300_a(player);
   }

   public void func_75130_a(IInventory par1IInventory) {
      super.func_75130_a(par1IInventory);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, 9 + j + i * 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
      }

   }

   public ItemStack func_82846_b(EntityPlayer player, int slot) {
      return ItemStack.field_190927_a;
   }
}
