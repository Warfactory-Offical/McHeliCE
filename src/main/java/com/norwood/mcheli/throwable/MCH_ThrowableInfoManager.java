package com.norwood.mcheli.throwable;

import com.norwood.mcheli.__helper.info.ContentRegistries;
import net.minecraft.item.Item;

public class MCH_ThrowableInfoManager {
   public static MCH_ThrowableInfo get(String name) {
      return (MCH_ThrowableInfo)ContentRegistries.throwable().get(name);
   }

   public static MCH_ThrowableInfo get(Item item) {
      return (MCH_ThrowableInfo)ContentRegistries.throwable().findFirst((info) -> {
         return info.item == item;
      });
   }
}
