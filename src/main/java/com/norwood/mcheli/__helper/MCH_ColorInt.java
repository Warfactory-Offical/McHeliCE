package com.norwood.mcheli.__helper;

public class MCH_ColorInt {
   public final int r;
   public final int g;
   public final int b;
   public final int a;

   public MCH_ColorInt(int color) {
      this(color >> 16 & 255, color >> 8 & 255, color & 255, color >> 24 & 255);
   }

   public MCH_ColorInt(int r, int g, int b, int a) {
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = a;
   }
}
