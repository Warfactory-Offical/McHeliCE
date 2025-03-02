package com.norwood.mcheli.weapon;

import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponATMissile extends MCH_WeaponEntitySeeker {
   public MCH_WeaponATMissile(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.power = 32;
      this.acceleration = 2.0F;
      this.explosionPower = 4;
      this.interval = -100;
      if (w.isRemote) {
         this.interval -= 10;
      }

      this.numMode = 2;
      this.guidanceSystem.canLockOnGround = true;
      this.guidanceSystem.ridableOnly = wi.ridableOnly;
   }

   public boolean isCooldownCountReloadTime() {
      return true;
   }

   public String getName() {
      String opt = "";
      if (this.getCurrentMode() == 1) {
         opt = " [TA]";
      }

      return super.getName() + opt;
   }

   public void update(int countWait) {
      super.update(countWait);
   }

   public boolean shot(MCH_WeaponParam prm) {
      return this.worldObj.isRemote ? this.shotClient(prm.entity, prm.user) : this.shotServer(prm);
   }

   protected boolean shotClient(Entity entity, Entity user) {
      boolean result = false;
      if (this.guidanceSystem.lock(user) && this.guidanceSystem.lastLockEntity != null) {
         result = true;
         this.optionParameter1 = W_Entity.getEntityId(this.guidanceSystem.lastLockEntity);
      }

      this.optionParameter2 = this.getCurrentMode();
      return result;
   }

   protected boolean shotServer(MCH_WeaponParam prm) {
      Entity tgtEnt = null;
      tgtEnt = prm.user.world.func_73045_a(prm.option1);
      if (tgtEnt != null && !tgtEnt.field_70128_L) {
         float yaw = prm.user.field_70177_z + this.fixRotationYaw;
         float pitch = prm.entity.field_70125_A + this.fixRotationPitch;
         double tX = (double)(-MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
         double tZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
         double tY = (double)(-MathHelper.func_76126_a(pitch / 180.0F * 3.1415927F));
         MCH_EntityATMissile e = new MCH_EntityATMissile(this.worldObj, prm.posX, prm.posY, prm.posZ, tX, tY, tZ, yaw, pitch, (double)this.acceleration);
         e.setName(this.name);
         e.setParameterFromWeapon(this, prm.entity, prm.user);
         e.setTargetEntity(tgtEnt);
         e.guidanceType = prm.option2;
         this.worldObj.func_72838_d(e);
         this.playSound(prm.entity);
         return true;
      } else {
         return false;
      }
   }
}
