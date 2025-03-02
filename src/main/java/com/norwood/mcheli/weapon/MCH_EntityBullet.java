package com.norwood.mcheli.weapon;

import java.util.List;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.wrapper.W_MovingObjectPosition;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_EntityBullet extends MCH_EntityBaseBullet {
   public MCH_EntityBullet(World par1World) {
      super(par1World);
   }

   public MCH_EntityBullet(World par1World, double pX, double pY, double pZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
      super(par1World, pX, pY, pZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70128_L && !this.world.isRemote && this.getCountOnUpdate() > 1 && this.getInfo() != null && this.explosionPower > 0) {
         float pDist = this.getInfo().proximityFuseDist;
         if ((double)pDist > 0.1D) {
            ++pDist;
            float rng = pDist + MathHelper.func_76135_e(this.getInfo().acceleration);
            List<Entity> list = this.world.func_72839_b(this, this.func_174813_aQ().func_72314_b((double)rng, (double)rng, (double)rng));

            for(int i = 0; i < list.size(); ++i) {
               Entity entity1 = (Entity)list.get(i);
               if (this.canBeCollidedEntity(entity1) && entity1.func_70068_e(this) < (double)(pDist * pDist)) {
                  MCH_Lib.DbgLog(this.world, "MCH_EntityBullet.onUpdate:proximityFuse:" + entity1);
                  this.posX = (entity1.posX + this.posX) / 2.0D;
                  this.posY = (entity1.posY + this.posY) / 2.0D;
                  this.posZ = (entity1.posZ + this.posZ) / 2.0D;
                  RayTraceResult mop = W_MovingObjectPosition.newMOP((int)this.posX, (int)this.posY, (int)this.posZ, 0, W_WorldFunc.getWorldVec3EntityPos(this), false);
                  this.onImpact(mop, 1.0F);
                  break;
               }
            }
         }
      }

   }

   protected void onUpdateCollided() {
      double mx = this.field_70159_w * this.accelerationFactor;
      double my = this.field_70181_x * this.accelerationFactor;
      double mz = this.field_70179_y * this.accelerationFactor;
      float damageFactor = 1.0F;
      RayTraceResult m = null;

      Vec3d vec31;
      for(int i = 0; i < 5; ++i) {
         vec31 = W_WorldFunc.getWorldVec3(this.world, this.posX, this.posY, this.posZ);
         Vec3d vec31 = W_WorldFunc.getWorldVec3(this.world, this.posX + mx, this.posY + my, this.posZ + mz);
         m = W_WorldFunc.clip(this.world, vec31, vec31);
         boolean continueClip = false;
         if (this.shootingEntity != null && W_MovingObjectPosition.isHitTypeTile(m)) {
            Block block = W_WorldFunc.getBlock(this.world, m.func_178782_a());
            if (MCH_Config.bulletBreakableBlocks.contains(block)) {
               W_WorldFunc.destroyBlock(this.world, m.func_178782_a(), true);
               continueClip = true;
            }
         }

         if (!continueClip) {
            break;
         }
      }

      Vec3d vec3 = W_WorldFunc.getWorldVec3(this.world, this.posX, this.posY, this.posZ);
      vec31 = W_WorldFunc.getWorldVec3(this.world, this.posX + mx, this.posY + my, this.posZ + mz);
      if (this.getInfo().delayFuse > 0) {
         if (m != null) {
            this.boundBullet(m.field_178784_b);
            if (this.delayFuse == 0) {
               this.delayFuse = this.getInfo().delayFuse;
            }
         }

      } else {
         if (m != null) {
            vec31 = W_WorldFunc.getWorldVec3(this.world, m.field_72307_f.x, m.field_72307_f.y, m.field_72307_f.z);
         }

         Entity entity = null;
         List<Entity> list = this.world.func_72839_b(this, this.func_174813_aQ().func_72321_a(mx, my, mz).func_72314_b(21.0D, 21.0D, 21.0D));
         double d0 = 0.0D;

         for(int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity)list.get(j);
            if (this.canBeCollidedEntity(entity1)) {
               float f = 0.3F;
               AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_72314_b((double)f, (double)f, (double)f);
               RayTraceResult m1 = axisalignedbb.func_72327_a(vec3, vec31);
               if (m1 != null) {
                  double d1 = vec3.func_72438_d(m1.field_72307_f);
                  if (d1 < d0 || d0 == 0.0D) {
                     entity = entity1;
                     d0 = d1;
                  }
               }
            }
         }

         if (entity != null) {
            m = new RayTraceResult(entity);
         }

         if (m != null) {
            this.onImpact(m, damageFactor);
         }

      }
   }

   public MCH_BulletModel getDefaultBulletModel() {
      return MCH_DefaultBulletModels.Bullet;
   }
}
