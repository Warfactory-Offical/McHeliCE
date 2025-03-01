package com.norwood.mcheli.particles;

import com.norwood.mcheli.wrapper.W_EntityFX;
import net.minecraft.world.World;

public abstract class MCH_EntityParticleBase extends W_EntityFX {
   public boolean isEffectedWind;
   public boolean diffusible;
   public boolean toWhite;
   public float particleMaxScale;
   public float gravity;
   public float moutionYUpAge;

   public MCH_EntityParticleBase(World par1World, double x, double y, double z, double mx, double my, double mz) {
      super(par1World, x, y, z, mx, my, mz);
      this.field_187129_i = mx;
      this.field_187130_j = my;
      this.field_187131_k = mz;
      this.isEffectedWind = false;
      this.particleMaxScale = this.field_70544_f;
   }

   public MCH_EntityParticleBase setParticleScale(float scale) {
      this.field_70544_f = scale;
      return this;
   }

   public void setParticleMaxAge(int age) {
      this.field_70547_e = age;
   }

   public void func_70536_a(int par1) {
      this.field_94054_b = par1 % 8;
      this.field_94055_c = par1 / 8;
   }

   public int func_70537_b() {
      return 2;
   }
}
