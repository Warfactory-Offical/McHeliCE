package com.norwood.mcheli.weapon;

import java.util.List;
import com.norwood.mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_EntityBomb extends MCH_EntityBaseBullet {
   public MCH_EntityBomb(World par1World) {
      super(par1World);
   }

   public MCH_EntityBomb(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
      super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.world.isRemote && this.getInfo() != null) {
         this.field_70159_w *= 0.999D;
         this.field_70179_y *= 0.999D;
         if (this.func_70090_H()) {
            this.field_70159_w *= (double)this.getInfo().velocityInWater;
            this.field_70181_x *= (double)this.getInfo().velocityInWater;
            this.field_70179_y *= (double)this.getInfo().velocityInWater;
         }

         float dist = this.getInfo().proximityFuseDist;
         if (dist > 0.1F && this.getCountOnUpdate() % 10 == 0) {
            List<Entity> list = this.world.func_72839_b(this, this.func_174813_aQ().func_72314_b((double)dist, (double)dist, (double)dist));
            if (list != null) {
               for(int i = 0; i < list.size(); ++i) {
                  Entity entity = (Entity)list.get(i);
                  if (W_Lib.isEntityLivingBase(entity) && this.canBeCollidedEntity(entity)) {
                     RayTraceResult m = new RayTraceResult(new Vec3d(this.posX, this.posY, this.posZ), EnumFacing.DOWN, new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D));
                     this.onImpact(m, 1.0F);
                     break;
                  }
               }
            }
         }
      }

      this.onUpdateBomblet();
   }

   public void sprinkleBomblet() {
      if (!this.world.isRemote) {
         MCH_EntityBomb e = new MCH_EntityBomb(this.world, this.posX, this.posY, this.posZ, this.field_70159_w, this.field_70181_x, this.field_70179_y, (float)this.field_70146_Z.nextInt(360), 0.0F, this.acceleration);
         e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
         e.setName(this.func_70005_c_());
         float RANDOM = this.getInfo().bombletDiff;
         e.field_70159_w = this.field_70159_w * 1.0D + (double)((this.field_70146_Z.nextFloat() - 0.5F) * RANDOM);
         e.field_70181_x = this.field_70181_x * 1.0D / 2.0D + (double)((this.field_70146_Z.nextFloat() - 0.5F) * RANDOM / 2.0F);
         e.field_70179_y = this.field_70179_y * 1.0D + (double)((this.field_70146_Z.nextFloat() - 0.5F) * RANDOM);
         e.setBomblet();
         this.world.func_72838_d(e);
      }

   }

   public MCH_BulletModel getDefaultBulletModel() {
      return MCH_DefaultBulletModels.Bomb;
   }
}
