package com.norwood.mcheli.aircraft;

import net.minecraft.util.math.Vec3d;

public class MCH_MobDropOption {
   public Vec3d pos;
   public int interval;

   public MCH_MobDropOption() {
      this.pos = Vec3d.ZERO;
      this.interval = 1;
   }
}
