package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Explosion;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.helicopter.MCH_EntityHeli;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponBomb extends MCH_WeaponBase {
   public MCH_WeaponBomb(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.acceleration = 0.5F;
      this.explosionPower = 9;
      this.power = 35;
      this.interval = -90;
      if (w.isRemote) {
         this.interval -= 10;
      }

   }

   public boolean shot(MCH_WeaponParam prm) {
      if (this.getInfo() != null && this.getInfo().destruct) {
         if (prm.entity instanceof MCH_EntityHeli) {
            MCH_EntityAircraft ac = (MCH_EntityAircraft)prm.entity;
            if (ac.isUAV() && ac.getSeatNum() == 0) {
               if (!this.worldObj.isRemote) {
                  MCH_Explosion.newExplosion(this.worldObj, (Entity)null, prm.user, ac.posX, ac.posY, ac.posZ, (float)this.getInfo().explosion, (float)this.getInfo().explosionBlock, true, true, this.getInfo().flaming, true, 0);
                  this.playSound(prm.entity);
               }

               ac.destruct();
            }
         }
      } else if (!this.worldObj.isRemote) {
         this.playSound(prm.entity);
         MCH_EntityBomb e = new MCH_EntityBomb(this.worldObj, prm.posX, prm.posY, prm.posZ, prm.entity.field_70159_w, prm.entity.field_70181_x, prm.entity.field_70179_y, prm.entity.field_70177_z, 0.0F, (double)this.acceleration);
         e.setName(this.name);
         e.setParameterFromWeapon(this, prm.entity, prm.user);
         e.field_70159_w = prm.entity.field_70159_w;
         e.field_70181_x = prm.entity.field_70181_x;
         e.field_70179_y = prm.entity.field_70179_y;
         this.worldObj.func_72838_d(e);
      }

      return true;
   }
}
