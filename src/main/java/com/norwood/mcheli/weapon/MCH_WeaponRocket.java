package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Lib;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponRocket extends MCH_WeaponBase {
   public MCH_WeaponRocket(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.acceleration = 4.0F;
      this.explosionPower = 3;
      this.power = 22;
      this.interval = 5;
      if (w.field_72995_K) {
         this.interval += 2;
      }

   }

   public String getName() {
      return super.getName() + (this.getCurrentMode() == 0 ? "" : " [HEIAP]");
   }

   public boolean shot(MCH_WeaponParam prm) {
      if (!this.worldObj.field_72995_K) {
         this.playSound(prm.entity);
         Vec3d v = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
         MCH_EntityRocket e = new MCH_EntityRocket(this.worldObj, prm.posX, prm.posY, prm.posZ, v.field_72450_a, v.field_72448_b, v.field_72449_c, prm.rotYaw, prm.rotPitch, (double)this.acceleration);
         e.setName(this.name);
         e.setParameterFromWeapon(this, prm.entity, prm.user);
         if (prm.option1 == 0 && this.numMode > 1) {
            e.piercing = 0;
         }

         this.worldObj.func_72838_d(e);
      } else {
         this.optionParameter1 = this.getCurrentMode();
      }

      return true;
   }
}
