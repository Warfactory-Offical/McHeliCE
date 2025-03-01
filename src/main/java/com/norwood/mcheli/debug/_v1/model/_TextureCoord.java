package com.norwood.mcheli.debug._v1.model;

import com.norwood.mcheli.__helper.debug.DebugInfoObject;
import com.norwood.mcheli.debug._v1.PrintStreamWrapper;

class _TextureCoord implements DebugInfoObject {
   public final float u;
   public final float v;
   public final float w;

   public _TextureCoord(float u, float v) {
      this(u, v, 0.0F);
   }

   public _TextureCoord(float u, float v, float w) {
      this.u = u;
      this.v = v;
      this.w = w;
   }

   public void printInfo(PrintStreamWrapper stream) {
      stream.println(String.format("T: [%.6f, %.6f, %.6f]", this.u, this.v, this.w));
   }
}
