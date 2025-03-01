package com.norwood.mcheli;

import com.norwood.mcheli.aircraft.MCH_ItemFuel;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class MCH_RecipeFuel extends Impl<IRecipe> implements IRecipe {
   public boolean func_77569_a(InventoryCrafting inv, World var2) {
      int jcnt = 0;
      int ccnt = 0;

      for(int i = 0; i < inv.func_70302_i_(); ++i) {
         ItemStack is = inv.func_70301_a(i);
         if (!is.func_190926_b()) {
            if (is.func_77973_b() instanceof MCH_ItemFuel) {
               if (is.func_77960_j() == 0) {
                  return false;
               }

               ++jcnt;
               if (jcnt > 1) {
                  return false;
               }
            } else {
               if (!(is.func_77973_b() instanceof ItemCoal) || is.func_190916_E() <= 0) {
                  return false;
               }

               ++ccnt;
            }
         }
      }

      return jcnt == 1 && ccnt > 0;
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      ItemStack output = new ItemStack(MCH_MOD.itemFuel);

      int i;
      ItemStack is;
      for(i = 0; i < inv.func_70302_i_(); ++i) {
         is = inv.func_70301_a(i);
         if (!is.func_190926_b() && is.func_77973_b() instanceof MCH_ItemFuel) {
            output.func_77964_b(is.func_77960_j());
            break;
         }
      }

      for(i = 0; i < inv.func_70302_i_(); ++i) {
         is = inv.func_70301_a(i);
         if (!is.func_190926_b() && is.func_77973_b() instanceof ItemCoal) {
            int sp = 100;
            if (is.func_77960_j() == 1) {
               sp = 75;
            }

            if (output.func_77960_j() > sp) {
               output.func_77964_b(output.func_77960_j() - sp);
            } else {
               output.func_77964_b(0);
            }
         }
      }

      return output;
   }

   public boolean func_194133_a(int width, int height) {
      return width >= 3 && height >= 3;
   }

   public ItemStack func_77571_b() {
      return ItemStack.field_190927_a;
   }
}
