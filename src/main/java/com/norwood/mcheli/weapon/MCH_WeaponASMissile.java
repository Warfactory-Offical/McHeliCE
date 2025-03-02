package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.wrapper.W_MovingObjectPosition;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponASMissile extends MCH_WeaponBase {
   public MCH_WeaponASMissile(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.acceleration = 3.0F;
      this.explosionPower = 9;
      this.power = 40;
      this.interval = 65186;
      if (w.isRemote) {
         this.interval -= 10;
      }

   }

   public boolean isCooldownCountReloadTime() {
      return true;
   }

   public void update(int countWait) {
      super.update(countWait);
   }

   public boolean shot(MCH_WeaponParam prm) {
      float yaw = prm.user.field_70177_z;
      float pitch = prm.user.field_70125_A;
      double tX = (double)(-MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tY = (double)(-MathHelper.func_76126_a(pitch / 180.0F * 3.1415927F));
      double dist = (double)MathHelper.func_76133_a(tX * tX + tY * tY + tZ * tZ);
      if (this.worldObj.isRemote) {
         tX = tX * 200.0D / dist;
         tY = tY * 200.0D / dist;
         tZ = tZ * 200.0D / dist;
      } else {
         tX = tX * 250.0D / dist;
         tY = tY * 250.0D / dist;
         tZ = tZ * 250.0D / dist;
      }

      Vec3d src = W_WorldFunc.getWorldVec3(this.worldObj, prm.entity.posX, prm.entity.posY + 1.62D, prm.entity.posZ);
      Vec3d dst = W_WorldFunc.getWorldVec3(this.worldObj, prm.entity.posX + tX, prm.entity.posY + 1.62D + tY, prm.entity.posZ + tZ);
      RayTraceResult m = W_WorldFunc.clip(this.worldObj, src, dst);
      if (m != null && W_MovingObjectPosition.isHitTypeTile(m) && !MCH_Lib.isBlockInWater(this.worldObj, m.func_178782_a().func_177958_n(), m.func_178782_a().func_177956_o(), m.func_178782_a().func_177952_p())) {
         if (!this.worldObj.isRemote) {
            MCH_EntityASMissile e = new MCH_EntityASMissile(this.worldObj, prm.posX, prm.posY, prm.posZ, tX, tY, tZ, yaw, pitch, (double)this.acceleration);
            e.setName(this.name);
            e.setParameterFromWeapon(this, prm.entity, prm.user);
            e.targetPosX = m.field_72307_f.x;
            e.targetPosY = m.field_72307_f.y;
            e.targetPosZ = m.field_72307_f.z;
            this.worldObj.func_72838_d(e);
            this.playSound(prm.entity);
         }

         return true;
      } else {
         return false;
      }
   }
}
