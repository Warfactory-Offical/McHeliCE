package com.norwood.mcheli.particles;

import net.minecraft.world.World;

public class MCH_ParticleParam {
   public final World world;
   public final String name;
   public double posX;
   public double posY;
   public double posZ;
   public double motionX;
   public double motionY;
   public double motionZ;
   public float size;
   public float a;
   public float r;
   public float g;
   public float b;
   public boolean isEffectWind;
   public int age;
   public boolean diffusible;
   public boolean toWhite;
   public float gravity;
   public float motionYUpAge;

   public MCH_ParticleParam(World w, String name, double x, double y, double z) {
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      this.size = 1.0F;
      this.a = 1.0F;
      this.r = 1.0F;
      this.g = 1.0F;
      this.b = 1.0F;
      this.isEffectWind = false;
      this.age = 0;
      this.diffusible = false;
      this.toWhite = false;
      this.gravity = 0.0F;
      this.motionYUpAge = 2.0F;
      this.world = w;
      this.name = name;
      this.posX = x;
      this.posY = y;
      this.posZ = z;
   }

   public MCH_ParticleParam(World w, String name, double x, double y, double z, double mx, double my, double mz, float size) {
      this(w, name, x, y, z);
      this.motionX = mx;
      this.motionY = my;
      this.motionZ = mz;
      this.size = size;
   }

   public void setColor(float a, float r, float g, float b) {
      this.a = a;
      this.r = r;
      this.g = g;
      this.b = b;
   }

   public void setMotion(double x, double y, double z) {
      this.motionX = x;
      this.motionY = y;
      this.motionZ = z;
   }
}
