package com.norwood.mcheli;

import java.util.HashMap;
import net.minecraft.entity.Entity;

public class MCH_DamageFactor {
   private HashMap<Class<? extends Entity>, Float> map = new HashMap<>();

   public void clear() {
      this.map.clear();
   }

   public void add(Class<? extends Entity> c, float value) {
      this.map.put(c, value);
   }

   public float getDamageFactor(Class<? extends Entity> c) {
      return this.map.containsKey(c) ? this.map.get(c) : 1.0F;
   }

   public float getDamageFactor(Entity e) {
      return e != null ? this.getDamageFactor((Class<? extends Entity>)e.getClass()) : 1.0F;
   }
}
