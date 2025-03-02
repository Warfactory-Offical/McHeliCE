package com.norwood.mcheli.weapon;

import net.minecraft.world.World;

public class MCH_EntityRocket extends MCH_EntityBaseBullet {
   public MCH_EntityRocket(World par1World) {
      super(par1World);
   }

   public MCH_EntityRocket(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
      super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      this.onUpdateBomblet();
      if (this.isBomblet <= 0 && this.getInfo() != null && !this.getInfo().disableSmoke) {
         this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 5.0F * this.getInfo().smokeSize * 0.5F);
      }

   }

   public void sprinkleBomblet() {
      if (!this.world.isRemote) {
         MCH_EntityRocket e = new MCH_EntityRocket(this.world, this.posX, this.posY, this.posZ, this.field_70159_w, this.field_70181_x, this.field_70179_y, this.field_70177_z, this.field_70125_A, this.acceleration);
         e.setName(this.func_70005_c_());
         e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
         float MOTION = this.getInfo().bombletDiff;
         e.field_70159_w += ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)MOTION;
         e.field_70181_x += ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)MOTION;
         e.field_70179_y += ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)MOTION;
         e.setBomblet();
         this.world.func_72838_d(e);
      }

   }

   public MCH_BulletModel getDefaultBulletModel() {
      return MCH_DefaultBulletModels.Rocket;
   }
}
