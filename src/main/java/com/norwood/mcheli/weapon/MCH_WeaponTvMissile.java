package com.norwood.mcheli.weapon;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_PacketNotifyTVMissileEntity;
import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponTvMissile extends MCH_WeaponBase {
   protected MCH_EntityTvMissile lastShotTvMissile = null;
   protected Entity lastShotEntity = null;
   protected boolean isTVGuided = false;

   public MCH_WeaponTvMissile(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.power = 32;
      this.acceleration = 2.0F;
      this.explosionPower = 4;
      this.interval = -100;
      if (w.isRemote) {
         this.interval -= 10;
      }

      this.numMode = 2;
      this.lastShotEntity = null;
      this.lastShotTvMissile = null;
      this.isTVGuided = false;
   }

   public String getName() {
      String opt = "";
      if (this.getCurrentMode() == 0) {
         opt = " [TV]";
      }

      if (this.getCurrentMode() == 2) {
         opt = " [TA]";
      }

      return super.getName() + opt;
   }

   public void update(int countWait) {
      super.update(countWait);
      if (!this.worldObj.isRemote) {
         if (this.isTVGuided && this.tick <= 9) {
            if (this.tick % 3 == 0 && this.lastShotTvMissile != null && !this.lastShotTvMissile.field_70128_L && this.lastShotEntity != null && !this.lastShotEntity.field_70128_L) {
               MCH_PacketNotifyTVMissileEntity.send(W_Entity.getEntityId(this.lastShotEntity), W_Entity.getEntityId(this.lastShotTvMissile));
            }

            if (this.tick == 9) {
               this.lastShotEntity = null;
               this.lastShotTvMissile = null;
            }
         }

         if (this.tick <= 2 && this.lastShotEntity instanceof MCH_EntityAircraft) {
            ((MCH_EntityAircraft)this.lastShotEntity).setTVMissile(this.lastShotTvMissile);
         }
      }

   }

   public boolean shot(MCH_WeaponParam prm) {
      return this.worldObj.isRemote ? this.shotClient(prm.entity, prm.user) : this.shotServer(prm);
   }

   protected boolean shotClient(Entity entity, Entity user) {
      this.optionParameter2 = 0;
      this.optionParameter1 = this.getCurrentMode();
      return true;
   }

   protected boolean shotServer(MCH_WeaponParam prm) {
      float yaw = prm.user.field_70177_z + this.fixRotationYaw;
      float pitch = prm.user.field_70125_A + this.fixRotationPitch;
      double tX = (double)(-MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tY = (double)(-MathHelper.func_76126_a(pitch / 180.0F * 3.1415927F));
      this.isTVGuided = prm.option1 == 0;
      float acr = this.acceleration;
      if (!this.isTVGuided) {
         acr = (float)((double)acr * 1.5D);
      }

      MCH_EntityTvMissile e = new MCH_EntityTvMissile(this.worldObj, prm.posX, prm.posY, prm.posZ, tX, tY, tZ, yaw, pitch, (double)acr);
      e.setName(this.name);
      e.setParameterFromWeapon(this, prm.entity, prm.user);
      this.lastShotEntity = prm.entity;
      this.lastShotTvMissile = e;
      this.worldObj.func_72838_d(e);
      this.playSound(prm.entity);
      return true;
   }
}
