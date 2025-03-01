package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.parachute.MCH_ItemParachute;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MCH_AircraftGuiContainer extends Container {
   public final EntityPlayer player;
   public final MCH_EntityAircraft aircraft;

   public MCH_AircraftGuiContainer(EntityPlayer player, MCH_EntityAircraft ac) {
      this.player = player;
      this.aircraft = ac;
      MCH_AircraftInventory iv = this.aircraft.getGuiInventory();
      this.func_75146_a(new Slot(iv, 0, 10, 30));
      this.func_75146_a(new Slot(iv, 1, 10, 48));
      this.func_75146_a(new Slot(iv, 2, 10, 66));
      int num = this.aircraft.getNumEjectionSeat();

      int x;
      for(x = 0; x < num; ++x) {
         this.func_75146_a(new Slot(iv, 3 + x, 10 + 18 * x, 105));
      }

      for(x = 0; x < 3; ++x) {
         for(int x = 0; x < 9; ++x) {
            this.func_75146_a(new Slot(player.field_71071_by, 9 + x + x * 9, 25 + x * 18, 135 + x * 18));
         }
      }

      for(x = 0; x < 9; ++x) {
         this.func_75146_a(new Slot(player.field_71071_by, x, 25 + x * 18, 195));
      }

   }

   public int getInventoryStartIndex() {
      return this.aircraft == null ? 3 : 3 + this.aircraft.getNumEjectionSeat();
   }

   public void func_75142_b() {
      super.func_75142_b();
   }

   public boolean func_75145_c(EntityPlayer player) {
      if (this.aircraft.getGuiInventory().func_70300_a(player)) {
         return true;
      } else {
         if (this.aircraft.isUAV()) {
            MCH_EntityUavStation us = this.aircraft.getUavStation();
            if (us != null) {
               double x = us.field_70165_t + (double)us.posUavX;
               double z = us.field_70161_v + (double)us.posUavZ;
               if (this.aircraft.field_70165_t < x + 10.0D && this.aircraft.field_70165_t > x - 10.0D && this.aircraft.field_70161_v < z + 10.0D && this.aircraft.field_70161_v > z - 10.0D) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public ItemStack func_82846_b(EntityPlayer player, int slotIndex) {
      MCH_AircraftInventory iv = this.aircraft.getGuiInventory();
      Slot slot = (Slot)this.field_75151_b.get(slotIndex);
      if (slot == null) {
         return null;
      } else {
         ItemStack itemStack = slot.func_75211_c();
         MCH_Lib.DbgLog(player.field_70170_p, "transferStackInSlot : %d :" + itemStack, slotIndex);
         if (itemStack.func_190926_b()) {
            return ItemStack.field_190927_a;
         } else {
            int i;
            if (slotIndex < this.getInventoryStartIndex()) {
               for(i = this.getInventoryStartIndex(); i < this.field_75151_b.size(); ++i) {
                  Slot playerSlot = (Slot)this.field_75151_b.get(i);
                  if (playerSlot.func_75211_c().func_190926_b()) {
                     playerSlot.func_75215_d(itemStack);
                     slot.func_75215_d(ItemStack.field_190927_a);
                     return itemStack;
                  }
               }
            } else if (itemStack.func_77973_b() instanceof MCH_ItemFuel) {
               for(i = 0; i < 3; ++i) {
                  if (iv.getFuelSlotItemStack(i).func_190926_b()) {
                     iv.func_70299_a(0 + i, itemStack);
                     slot.func_75215_d(ItemStack.field_190927_a);
                     return itemStack;
                  }
               }
            } else if (itemStack.func_77973_b() instanceof MCH_ItemParachute) {
               i = this.aircraft.getNumEjectionSeat();

               for(int i = 0; i < i; ++i) {
                  if (iv.getParachuteSlotItemStack(i).func_190926_b()) {
                     iv.func_70299_a(3 + i, itemStack);
                     slot.func_75215_d(ItemStack.field_190927_a);
                     return itemStack;
                  }
               }
            }

            return ItemStack.field_190927_a;
         }
      }
   }

   public void func_75134_a(EntityPlayer player) {
      super.func_75134_a(player);
      if (!player.field_70170_p.field_72995_K) {
         MCH_AircraftInventory iv = this.aircraft.getGuiInventory();

         int i;
         ItemStack is;
         for(i = 0; i < 3; ++i) {
            is = iv.getFuelSlotItemStack(i);
            if (!is.func_190926_b() && !(is.func_77973_b() instanceof MCH_ItemFuel)) {
               this.dropPlayerItem(player, 0 + i);
            }
         }

         for(i = 0; i < 2; ++i) {
            is = iv.getParachuteSlotItemStack(i);
            if (!is.func_190926_b() && !(is.func_77973_b() instanceof MCH_ItemParachute)) {
               this.dropPlayerItem(player, 3 + i);
            }
         }
      }

   }

   public void dropPlayerItem(EntityPlayer player, int slotID) {
      if (!player.field_70170_p.field_72995_K) {
         ItemStack itemstack = this.aircraft.getGuiInventory().func_70304_b(slotID);
         if (!itemstack.func_190926_b()) {
            player.func_71019_a(itemstack, false);
         }
      }

   }
}
