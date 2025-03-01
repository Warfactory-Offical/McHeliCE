package com.norwood.mcheli;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.addon.GeneratedAddonPack;

public class MCH_SoundsJson {
   public static void updateGenerated() {
      File soundsDir = new File(MCH_MOD.getSource().getPath() + "/assets/mcheli/sounds");
      File[] soundFiles = soundsDir.listFiles((fx) -> {
         String s = fx.getName().toLowerCase();
         return fx.isFile() && s.length() >= 5 && s.substring(s.length() - 4).compareTo(".ogg") == 0;
      });
      Multimap<String, String> multimap = Multimaps.newListMultimap(Maps.newLinkedHashMap(), Lists::newLinkedList);
      List<String> lines = Lists.newLinkedList();
      int cnt = 0;
      if (soundFiles != null) {
         File[] var5 = soundFiles;
         int var6 = soundFiles.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File f = var5[var7];
            String name = f.getName().toLowerCase();
            int ei = name.lastIndexOf(".");
            name = name.substring(0, ei);
            String key = name;
            char c = name.charAt(name.length() - 1);
            if (c >= '0' && c <= '9') {
               key = name.substring(0, name.length() - 1);
            }

            multimap.put(key, name);
         }

         lines.add("{");

         String line;
         for(Iterator var13 = multimap.keySet().iterator(); var13.hasNext(); lines.add(line)) {
            String key = (String)var13.next();
            ++cnt;
            String sounds = Joiner.on(",").join((Iterable)multimap.get(key).stream().map((namex) -> {
               return '"' + MCH_Utils.suffix(namex).toString() + '"';
            }).collect(Collectors.toList()));
            line = "\"" + key + "\": {\"category\": \"master\",\"sounds\": [" + sounds + "]}";
            if (cnt < multimap.keySet().size()) {
               line = line + ",";
            }
         }

         lines.add("}");
         lines.add("");
      }

      GeneratedAddonPack.instance().updateAssetFile("sounds.json", lines);
      MCH_Utils.logger().info("Update sounds.json, %d sounds.", cnt);
   }
}
