package com.norwood.mcheli.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import com.norwood.mcheli.__helper.addon.GeneratedAddonPack;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;

public class W_LanguageRegistry {
   private static HashMap<String, ArrayList<String>> map = new HashMap();

   public static void addName(Object objectToName, String name) {
      addNameForObject(objectToName, "en_us", name);
   }

   public static void addNameForObject(Object o, String lang, String name) {
      addNameForObject(o, lang, name, "", "");
   }

   public static void addNameForObject(Object o, String lang, String name, String key, String desc) {
      if (o != null) {
         lang = lang.toLowerCase(Locale.ROOT);
         if (!map.containsKey(lang)) {
            map.put(lang, new ArrayList());
         }

         if (o instanceof Item) {
            ((ArrayList)map.get(lang)).add(((Item)o).func_77658_a() + ".name=" + name);
         }

         if (o instanceof Block) {
            ((ArrayList)map.get(lang)).add(((Block)o).func_149739_a() + ".name=" + name);
         } else if (o instanceof Advancement) {
            ((ArrayList)map.get(lang)).add("advancement." + key + "=" + name);
            ((ArrayList)map.get(lang)).add("advancement." + key + ".desc=" + desc);
         }

      }
   }

   public static void clear() {
      map.clear();
      map = null;
   }

   public static void updateGeneratedLang() {
      GeneratedAddonPack.instance().checkMkdirsAssets("lang");
      Iterator var0 = map.keySet().iterator();

      while(var0.hasNext()) {
         String key = (String)var0.next();
         ArrayList<String> list = (ArrayList)map.get(key);
         GeneratedAddonPack.instance().updateAssetFile("lang/" + key + ".lang", list);
      }

      FMLClientHandler.instance().refreshResources((resourceType) -> {
         return resourceType == VanillaResourceType.LANGUAGES;
      });
      clear();
   }
}
