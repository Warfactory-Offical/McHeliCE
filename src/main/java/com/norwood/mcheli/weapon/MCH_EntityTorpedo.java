package com.norwood.mcheli.weapon;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MCH_EntityTorpedo extends MCH_EntityBaseBullet {
   public double targetPosX;
   public double targetPosY;
   public double targetPosZ;
   public double accelerationInWater = 2.0D;

   public MCH_EntityTorpedo(World par1World) {
      super(par1World);
      this.targetPosX = 0.0D;
      this.targetPosY = 0.0D;
      this.targetPosZ = 0.0D;
   }

   public MCH_EntityTorpedo(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
      super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.getInfo() != null && this.getInfo().isGuidedTorpedo) {
         this.onUpdateGuided();
      } else {
         this.onUpdateNoGuided();
      }

      if (this.func_70090_H() && this.getInfo() != null && !this.getInfo().disableSmoke) {
         this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 5.0F * this.getInfo().smokeSize * 0.5F);
      }

   }

   private void onUpdateNoGuided() {
      double x;
      if (!this.world.isRemote && this.func_70090_H()) {
         this.field_70181_x *= 0.800000011920929D;
         if (this.acceleration < this.accelerationInWater) {
            this.acceleration += 0.1D;
         } else if (this.acceleration > this.accelerationInWater + 0.20000000298023224D) {
            this.acceleration -= 0.1D;
         }

         x = this.field_70159_w;
         double y = this.field_70181_x;
         double z = this.field_70179_y;
         double d = (double)MathHelper.func_76133_a(x * x + y * y + z * z);
         this.field_70159_w = x * this.acceleration / d;
         this.field_70181_x = y * this.acceleration / d;
         this.field_70179_y = z * this.acceleration / d;
      }

      if (this.func_70090_H()) {
         x = (double)((float)Math.atan2(this.field_70179_y, this.field_70159_w));
         this.field_70177_z = (float)(x * 180.0D / 3.141592653589793D) - 90.0F;
      }

   }

   private void onUpdateGuided() {
      double x;
      double y;
      if (!this.world.isRemote && this.func_70090_H()) {
         if (this.acceleration < this.accelerationInWater) {
            this.acceleration += 0.1D;
         } else if (this.acceleration > this.accelerationInWater + 0.20000000298023224D) {
            this.acceleration -= 0.1D;
         }

         x = this.targetPosX - this.posX;
         y = this.targetPosY - this.posY;
         double z = this.targetPosZ - this.posZ;
         double d = (double)MathHelper.func_76133_a(x * x + y * y + z * z);
         this.field_70159_w = x * this.acceleration / d;
         this.field_70181_x = y * this.acceleration / d;
         this.field_70179_y = z * this.acceleration / d;
      }

      if (this.func_70090_H()) {
         x = (double)((float)Math.atan2(this.field_70179_y, this.field_70159_w));
         this.field_70177_z = (float)(x * 180.0D / 3.141592653589793D) - 90.0F;
         y = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
         this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, y) * 180.0D / 3.141592653589793D));
      }

   }

   public MCH_BulletModel getDefaultBulletModel() {
      return MCH_DefaultBulletModels.Torpedo;
   }
}
