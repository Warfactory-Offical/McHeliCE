package com.norwood.mcheli;

import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.__helper.info.ContentParseException;
import com.norwood.mcheli.__helper.info.IContentData;
import net.minecraft.util.math.Vec3d;

public abstract class MCH_BaseInfo implements IContentData {
   public final String filePath;
   public final AddonResourceLocation location;

   public MCH_BaseInfo(AddonResourceLocation location, String filePath) {
      this.location = location;
      this.filePath = filePath;
   }

   public boolean toBool(String s) {
      return s.equalsIgnoreCase("true");
   }

   public boolean toBool(String s, boolean defaultValue) {
      if (s.equalsIgnoreCase("true")) {
         return true;
      } else {
         return s.equalsIgnoreCase("false") ? false : defaultValue;
      }
   }

   public float toFloat(String s) {
      return Float.parseFloat(s);
   }

   public float toFloat(String s, float min, float max) {
      float f = Float.parseFloat(s);
      return f > max ? max : (f < min ? min : f);
   }

   public double toDouble(String s) {
      return Double.parseDouble(s);
   }

   public Vec3d toVec3(String x, String y, String z) {
      return new Vec3d(this.toDouble(x), this.toDouble(y), this.toDouble(z));
   }

   public int toInt(String s) {
      return Integer.parseInt(s);
   }

   public int toInt(String s, int min, int max) {
      int f = Integer.parseInt(s);
      return f > max ? max : (f < min ? min : f);
   }

   public int hex2dec(String s) {
      return !s.startsWith("0x") && !s.startsWith("0X") && s.indexOf(0) != 35 ? (int)(Long.decode("0x" + s) & -1L) : (int)(Long.decode(s) & -1L);
   }

   public String[] splitParam(String data) {
      return data.split("\\s*,\\s*");
   }

   public String[] splitParamSlash(String data) {
      return data.split("\\s*/\\s*");
   }

   public boolean validate() throws Exception {
      return true;
   }

   protected void loadItemData(String item, String data) {
   }

   public void onPostReload() {
   }

   public boolean canReloadItem(String item) {
      return false;
   }

   public void parse(List<String> lines, String fileExtension, boolean reload) throws Exception {
      if ("txt".equals(fileExtension)) {
         int line = 0;

         try {
            Iterator var5 = lines.iterator();

            while(var5.hasNext()) {
               String str = (String)var5.next();
               ++line;
               str = str.trim();
               int eqIdx = str.indexOf(61);
               if (eqIdx >= 0 && str.length() > eqIdx + 1) {
                  this.loadItemData(str.substring(0, eqIdx).trim().toLowerCase(), str.substring(eqIdx + 1).trim());
               }
            }
         } catch (Exception var8) {
            throw new ContentParseException(var8, line);
         }
      }

   }

   public AddonResourceLocation getLoation() {
      return this.location;
   }

   public String getContentPath() {
      return this.filePath;
   }
}
