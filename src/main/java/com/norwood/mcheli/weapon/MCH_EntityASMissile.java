package com.norwood.mcheli.weapon;

import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class MCH_EntityASMissile extends MCH_EntityBaseBullet {
   public double targetPosX;
   public double targetPosY;
   public double targetPosZ;

   public MCH_EntityASMissile(World par1World) {
      super(par1World);
      this.targetPosX = 0.0D;
      this.targetPosY = 0.0D;
      this.targetPosZ = 0.0D;
   }

   public MCH_EntityASMissile(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
      super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
   }

   public float getGravity() {
      return this.getBomblet() == 1 ? -0.03F : super.getGravity();
   }

   public float getGravityInWater() {
      return this.getBomblet() == 1 ? -0.03F : super.getGravityInWater();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.getInfo() != null && !this.getInfo().disableSmoke && this.getBomblet() == 0) {
         this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 10.0F * this.getInfo().smokeSize * 0.5F);
      }

      if (this.getInfo() != null && !this.world.isRemote && this.isBomblet != 1) {
         Block block = W_WorldFunc.getBlock(this.world, (int)this.targetPosX, (int)this.targetPosY, (int)this.targetPosZ);
         if (block != null && block.func_149703_v()) {
            double dist = this.func_70011_f(this.targetPosX, this.targetPosY, this.targetPosZ);
            if (dist < (double)this.getInfo().proximityFuseDist) {
               if (this.getInfo().bomblet > 0) {
                  for(int i = 0; i < this.getInfo().bomblet; ++i) {
                     this.sprinkleBomblet();
                  }
               } else {
                  RayTraceResult mop = new RayTraceResult(this);
                  this.onImpact(mop, 1.0F);
               }

               this.func_70106_y();
            } else {
               double up;
               double x;
               double y;
               double z;
               if ((double)this.getGravity() == 0.0D) {
                  up = 0.0D;
                  if (this.getCountOnUpdate() < 10) {
                     up = 20.0D;
                  }

                  x = this.targetPosX - this.posX;
                  y = this.targetPosY + up - this.posY;
                  z = this.targetPosZ - this.posZ;
                  double d = (double)MathHelper.func_76133_a(x * x + y * y + z * z);
                  this.field_70159_w = x * this.acceleration / d;
                  this.field_70181_x = y * this.acceleration / d;
                  this.field_70179_y = z * this.acceleration / d;
               } else {
                  up = this.targetPosX - this.posX;
                  x = this.targetPosY - this.posY;
                  x *= 0.3D;
                  y = this.targetPosZ - this.posZ;
                  z = (double)MathHelper.func_76133_a(up * up + x * x + y * y);
                  this.field_70159_w = up * this.acceleration / z;
                  this.field_70179_y = y * this.acceleration / z;
               }
            }
         }
      }

      double a = (double)((float)Math.atan2(this.field_70179_y, this.field_70159_w));
      this.field_70177_z = (float)(a * 180.0D / 3.141592653589793D) - 90.0F;
      double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0D / 3.141592653589793D));
      this.onUpdateBomblet();
   }

   public void sprinkleBomblet() {
      if (!this.world.isRemote) {
         MCH_EntityASMissile e = new MCH_EntityASMissile(this.world, this.posX, this.posY, this.posZ, this.field_70159_w, this.field_70181_x, this.field_70179_y, (float)this.field_70146_Z.nextInt(360), 0.0F, this.acceleration);
         e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
         e.setName(this.func_70005_c_());
         float RANDOM = this.getInfo().bombletDiff;
         e.field_70159_w = this.field_70159_w * 0.5D + (double)((this.field_70146_Z.nextFloat() - 0.5F) * RANDOM);
         e.field_70181_x = this.field_70181_x * 0.5D / 2.0D + (double)((this.field_70146_Z.nextFloat() - 0.5F) * RANDOM / 2.0F);
         e.field_70179_y = this.field_70179_y * 0.5D + (double)((this.field_70146_Z.nextFloat() - 0.5F) * RANDOM);
         e.setBomblet();
         this.world.func_72838_d(e);
      }

   }

   public MCH_BulletModel getDefaultBulletModel() {
      return MCH_DefaultBulletModels.ASMissile;
   }
}
