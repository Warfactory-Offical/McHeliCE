package com.norwood.mcheli.weapon;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class MCH_EntityAAMissile extends MCH_EntityBaseBullet {
   public MCH_EntityAAMissile(World par1World) {
      super(par1World);
      this.targetEntity = null;
   }

   public MCH_EntityAAMissile(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
      super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.getCountOnUpdate() > 4 && this.getInfo() != null && !this.getInfo().disableSmoke) {
         this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 7.0F * this.getInfo().smokeSize * 0.5F);
      }

      if (!this.field_70170_p.field_72995_K && this.getInfo() != null) {
         if (this.shootingEntity != null && this.targetEntity != null && !this.targetEntity.field_70128_L) {
            double x = this.field_70165_t - this.targetEntity.field_70165_t;
            double y = this.field_70163_u - this.targetEntity.field_70163_u;
            double z = this.field_70161_v - this.targetEntity.field_70161_v;
            double d = x * x + y * y + z * z;
            if (d > 3422500.0D) {
               this.func_70106_y();
            } else if (this.getCountOnUpdate() > this.getInfo().rigidityTime) {
               if (this.usingFlareOfTarget(this.targetEntity)) {
                  this.func_70106_y();
                  return;
               }

               if (this.getInfo().proximityFuseDist >= 0.1F && d < (double)this.getInfo().proximityFuseDist) {
                  RayTraceResult mop = new RayTraceResult(this.targetEntity);
                  this.field_70165_t = (this.targetEntity.field_70165_t + this.field_70165_t) / 2.0D;
                  this.field_70163_u = (this.targetEntity.field_70163_u + this.field_70163_u) / 2.0D;
                  this.field_70161_v = (this.targetEntity.field_70161_v + this.field_70161_v) / 2.0D;
                  this.onImpact(mop, 1.0F);
               } else {
                  this.guidanceToTarget(this.targetEntity.field_70165_t, this.targetEntity.field_70163_u, this.targetEntity.field_70161_v);
               }
            }
         } else {
            this.func_70106_y();
         }
      }

   }

   public MCH_BulletModel getDefaultBulletModel() {
      return MCH_DefaultBulletModels.AAMissile;
   }
}
