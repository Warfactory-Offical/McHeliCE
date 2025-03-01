package com.norwood.mcheli;

public class MCH_Color {
   public float a;
   public float r;
   public float g;
   public float b;

   public MCH_Color(float aa, float rr, float gg, float bb) {
      this.a = this.round(aa);
      this.r = this.round(rr);
      this.g = this.round(gg);
      this.b = this.round(bb);
   }

   public MCH_Color(float rr, float gg, float bb) {
      this(1.0F, rr, gg, bb);
   }

   public MCH_Color() {
      this(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public float round(float f) {
      return f > 1.0F ? 1.0F : (f < 0.0F ? 0.0F : f);
   }
}
