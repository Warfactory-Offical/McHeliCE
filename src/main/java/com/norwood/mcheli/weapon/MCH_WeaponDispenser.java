package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Lib;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponDispenser extends MCH_WeaponBase {
   public MCH_WeaponDispenser(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.acceleration = 0.5F;
      this.explosionPower = 0;
      this.power = 0;
      this.interval = -90;
      if (w.isRemote) {
         this.interval -= 10;
      }

   }

   public boolean shot(MCH_WeaponParam prm) {
      if (!this.worldObj.isRemote) {
         this.playSound(prm.entity);
         Vec3d v = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
         MCH_EntityDispensedItem e = new MCH_EntityDispensedItem(this.worldObj, prm.posX, prm.posY, prm.posZ, v.x, v.y, v.z, prm.rotYaw, prm.rotPitch, (double)this.acceleration);
         e.setName(this.name);
         e.setParameterFromWeapon(this, prm.entity, prm.user);
         e.field_70159_w = prm.entity.field_70159_w + e.field_70159_w * 0.5D;
         e.field_70181_x = prm.entity.field_70181_x + e.field_70181_x * 0.5D;
         e.field_70179_y = prm.entity.field_70179_y + e.field_70179_y * 0.5D;
         e.posX += e.field_70159_w * 0.5D;
         e.posY += e.field_70181_x * 0.5D;
         e.posZ += e.field_70179_y * 0.5D;
         this.worldObj.func_72838_d(e);
      }

      return true;
   }
}
