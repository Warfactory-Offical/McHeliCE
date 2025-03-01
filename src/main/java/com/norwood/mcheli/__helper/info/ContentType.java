package com.norwood.mcheli.__helper.info;

public enum ContentType {
   HELICOPTER("helicopter", "helicopters"),
   PLANE("plane", "plane"),
   TANK("tank", "tanks"),
   VEHICLE("vehicle", "vehicles"),
   WEAPON("weapon", "weapons"),
   THROWABLE("throwable", "throwable"),
   HUD("hud", "hud");

   public final String type;
   public final String dirName;

   private ContentType(String typeName, String dirName) {
      this.type = typeName;
      this.dirName = dirName;
   }

   public static boolean validateDirName(String dirName) {
      ContentType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ContentType type = var1[var3];
         if (type.dirName.equals(dirName)) {
            return true;
         }
      }

      return false;
   }
}
