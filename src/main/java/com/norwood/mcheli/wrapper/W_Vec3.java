package com.norwood.mcheli.wrapper;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class W_Vec3 {
   public static Vec3d rotateRoll(float par1, Vec3d vOut) {
      float f1 = MathHelper.func_76134_b(par1);
      float f2 = MathHelper.func_76126_a(par1);
      double d0 = vOut.x * (double)f1 + vOut.y * (double)f2;
      double d1 = vOut.y * (double)f1 - vOut.x * (double)f2;
      double d2 = vOut.z;
      return new Vec3d(d0, d1, d2);
   }
}
