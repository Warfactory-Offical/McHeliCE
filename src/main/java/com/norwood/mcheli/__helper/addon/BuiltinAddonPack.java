package com.norwood.mcheli.__helper.addon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import com.norwood.mcheli.__helper.MCH_Utils;

public class BuiltinAddonPack extends AddonPack {
   private static BuiltinAddonPack instance = null;

   public static BuiltinAddonPack instance() {
      if (instance == null) {
         instance = new BuiltinAddonPack();
      }

      return instance;
   }

   private BuiltinAddonPack() {
      super("@builtin", "MCHeli-Builtin", "1.0", (File)null, "EMB4-MCHeli", ImmutableList.of("EMB4", "Murachiki27"), "Builtin addon", "1", ImmutableMap.of());
   }

   public File getFile() {
      return MCH_Utils.getSource();
   }
}
