package com.norwood.mcheli.weapon;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.wrapper.W_Item;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MCH_WeaponInfoManager {
   private MCH_WeaponInfoManager() {
   }

   public static void setRoundItems() {
      Iterator var0 = ContentRegistries.weapon().values().iterator();

      while(var0.hasNext()) {
         MCH_WeaponInfo w = (MCH_WeaponInfo)var0.next();

         MCH_WeaponInfo.RoundItem r;
         Item item;
         for(Iterator var2 = w.roundItems.iterator(); var2.hasNext(); r.itemStack = new ItemStack(item, 1, r.damage)) {
            r = (MCH_WeaponInfo.RoundItem)var2.next();
            item = W_Item.getItemByName(r.itemName);
         }
      }

   }

   @Nullable
   public static MCH_WeaponInfo get(String name) {
      return (MCH_WeaponInfo)ContentRegistries.weapon().get(name);
   }

   public static boolean contains(String name) {
      return ContentRegistries.weapon().contains(name);
   }
}
