package com.norwood.mcheli.weapon;

import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponAAMissile extends MCH_WeaponEntitySeeker {
   public MCH_WeaponAAMissile(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.power = 12;
      this.acceleration = 2.5F;
      this.explosionPower = 4;
      this.interval = 5;
      if (w.field_72995_K) {
         this.interval += 5;
      }

      this.guidanceSystem.canLockInAir = true;
      this.guidanceSystem.ridableOnly = wi.ridableOnly;
   }

   public boolean isCooldownCountReloadTime() {
      return true;
   }

   public void update(int countWait) {
      super.update(countWait);
   }

   public boolean shot(MCH_WeaponParam prm) {
      boolean result = false;
      if (!this.worldObj.field_72995_K) {
         Entity tgtEnt = prm.user.field_70170_p.func_73045_a(prm.option1);
         if (tgtEnt != null && !tgtEnt.field_70128_L) {
            this.playSound(prm.entity);
            float yaw = prm.entity.field_70177_z + this.fixRotationYaw;
            float pitch = prm.entity.field_70125_A + this.fixRotationPitch;
            double tX = (double)(-MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
            double tZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
            double tY = (double)(-MathHelper.func_76126_a(pitch / 180.0F * 3.1415927F));
            MCH_EntityAAMissile e = new MCH_EntityAAMissile(this.worldObj, prm.posX, prm.posY, prm.posZ, tX, tY, tZ, yaw, pitch, (double)this.acceleration);
            e.setName(this.name);
            e.setParameterFromWeapon(this, prm.entity, prm.user);
            e.setTargetEntity(tgtEnt);
            this.worldObj.func_72838_d(e);
            result = true;
         }
      } else if (this.guidanceSystem.lock(prm.user) && this.guidanceSystem.lastLockEntity != null) {
         result = true;
         this.optionParameter1 = W_Entity.getEntityId(this.guidanceSystem.lastLockEntity);
      }

      return result;
   }
}
