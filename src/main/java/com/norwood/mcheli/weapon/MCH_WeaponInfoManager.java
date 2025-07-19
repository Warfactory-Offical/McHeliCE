package com.norwood.mcheli.weapon;

import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.wrapper.W_Item;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MCH_WeaponInfoManager {
   private MCH_WeaponInfoManager() {
   }

   public static void setRoundItems() {
      for (MCH_WeaponInfo w : ContentRegistries.weapon().values()) {
         for (MCH_WeaponInfo.RoundItem r : w.roundItems) {
            Item item = W_Item.getItemByName(r.itemName);
            r.itemStack = new ItemStack(item, 1, r.damage);
         }
      }
   }

   @Nullable
   public static MCH_WeaponInfo get(String name) {
      return ContentRegistries.weapon().get(name);
   }

   public static boolean contains(String name) {
      return ContentRegistries.weapon().contains(name);
   }
}
