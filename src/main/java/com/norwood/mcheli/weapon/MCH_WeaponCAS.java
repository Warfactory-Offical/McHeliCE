package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.wrapper.W_MovingObjectPosition;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponCAS extends MCH_WeaponBase {
   private double targetPosX;
   private double targetPosY;
   private double targetPosZ;
   public int direction;
   private int startTick;
   private int cntAtk;
   private Entity shooter;
   public Entity user;

   public MCH_WeaponCAS(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.acceleration = 4.0F;
      this.explosionPower = 2;
      this.power = 32;
      this.interval = 65236;
      if (w.isRemote) {
         this.interval -= 10;
      }

      this.targetPosX = 0.0D;
      this.targetPosY = 0.0D;
      this.targetPosZ = 0.0D;
      this.direction = 0;
      this.startTick = 0;
      this.cntAtk = 3;
      this.shooter = null;
      this.user = null;
   }

   public void update(int countWait) {
      super.update(countWait);
      if (!this.worldObj.isRemote && this.cntAtk < 3 && countWait != 0 && this.tick == this.startTick) {
         double x = 0.0D;
         double z = 0.0D;
         if (this.cntAtk >= 1) {
            double sign = this.cntAtk == 1 ? 1.0D : -1.0D;
            if (this.direction == 0 || this.direction == 2) {
               x = rand.nextDouble() * 10.0D * sign;
               z = (rand.nextDouble() - 0.5D) * 10.0D;
            }

            if (this.direction == 1 || this.direction == 3) {
               z = rand.nextDouble() * 10.0D * sign;
               x = (rand.nextDouble() - 0.5D) * 10.0D;
            }
         }

         this.spawnA10(this.targetPosX + x, this.targetPosY + 20.0D, this.targetPosZ + z);
         this.startTick = this.tick + 45;
         ++this.cntAtk;
      }

   }

   public void modifyParameters() {
      if (this.interval > 65286) {
         this.interval = 65286;
      }

   }

   public void setTargetPosition(double x, double y, double z) {
      this.targetPosX = x;
      this.targetPosY = y;
      this.targetPosZ = z;
   }

   public void spawnA10(double x, double y, double z) {
      double mX = 0.0D;
      double mY = 0.0D;
      double mZ = 0.0D;
      if (this.direction == 0) {
         mZ += 3.0D;
         z -= 90.0D;
      }

      if (this.direction == 1) {
         mX -= 3.0D;
         x += 90.0D;
      }

      if (this.direction == 2) {
         mZ -= 3.0D;
         z += 90.0D;
      }

      if (this.direction == 3) {
         mX += 3.0D;
         x -= 90.0D;
      }

      MCH_EntityA10 a10 = new MCH_EntityA10(this.worldObj, x, y, z);
      a10.setWeaponName(this.name);
      a10.field_70126_B = a10.field_70177_z = (float)(90 * this.direction);
      a10.field_70159_w = mX;
      a10.field_70181_x = mY;
      a10.field_70179_y = mZ;
      a10.direction = this.direction;
      a10.shootingEntity = this.user;
      a10.shootingAircraft = this.shooter;
      a10.explosionPower = this.explosionPower;
      a10.power = this.power;
      a10.acceleration = this.acceleration;
      this.worldObj.func_72838_d(a10);
      W_WorldFunc.MOD_playSoundEffect(this.worldObj, x, y, z, "a-10_snd", 150.0F, 1.0F);
   }

   public boolean shot(MCH_WeaponParam prm) {
      float yaw = prm.user.field_70177_z;
      float pitch = prm.user.field_70125_A;
      double tX = (double)(-MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tY = (double)(-MathHelper.func_76126_a(pitch / 180.0F * 3.1415927F));
      double dist = (double)MathHelper.func_76133_a(tX * tX + tY * tY + tZ * tZ);
      if (this.worldObj.isRemote) {
         tX = tX * 80.0D / dist;
         tY = tY * 80.0D / dist;
         tZ = tZ * 80.0D / dist;
      } else {
         tX = tX * 150.0D / dist;
         tY = tY * 150.0D / dist;
         tZ = tZ * 150.0D / dist;
      }

      Vec3d src = W_WorldFunc.getWorldVec3(this.worldObj, prm.entity.posX, prm.entity.posY + 2.0D, prm.entity.posZ);
      Vec3d dst = W_WorldFunc.getWorldVec3(this.worldObj, prm.entity.posX + tX, prm.entity.posY + tY + 2.0D, prm.entity.posZ + tZ);
      RayTraceResult m = W_WorldFunc.clip(this.worldObj, src, dst);
      if (m != null && W_MovingObjectPosition.isHitTypeTile(m)) {
         this.targetPosX = m.field_72307_f.x;
         this.targetPosY = m.field_72307_f.y;
         this.targetPosZ = m.field_72307_f.z;
         this.direction = (int)MCH_Lib.getRotate360((double)(yaw + 45.0F)) / 90;
         this.direction += rand.nextBoolean() ? -1 : 1;
         this.direction %= 4;
         if (this.direction < 0) {
            this.direction += 4;
         }

         this.user = prm.user;
         this.shooter = prm.entity;
         if (prm.entity != null) {
            this.playSoundClient(prm.entity, 1.0F, 1.0F);
         }

         this.startTick = 50;
         this.cntAtk = 0;
         return true;
      } else {
         return false;
      }
   }

   public boolean shot(Entity user, double px, double py, double pz, int option1, int option2) {
      float yaw = user.field_70177_z;
      float pitch = user.field_70125_A;
      double tX = (double)(-MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
      double tY = (double)(-MathHelper.func_76126_a(pitch / 180.0F * 3.1415927F));
      double dist = (double)MathHelper.func_76133_a(tX * tX + tY * tY + tZ * tZ);
      if (this.worldObj.isRemote) {
         tX = tX * 80.0D / dist;
         tY = tY * 80.0D / dist;
         tZ = tZ * 80.0D / dist;
      } else {
         tX = tX * 120.0D / dist;
         tY = tY * 120.0D / dist;
         tZ = tZ * 120.0D / dist;
      }

      Vec3d src = W_WorldFunc.getWorldVec3(this.worldObj, px, py, pz);
      Vec3d dst = W_WorldFunc.getWorldVec3(this.worldObj, px + tX, py + tY, pz + tZ);
      RayTraceResult m = W_WorldFunc.clip(this.worldObj, src, dst);
      if (W_MovingObjectPosition.isHitTypeTile(m)) {
         if (this.worldObj.isRemote) {
            double dx = m.field_72307_f.x - px;
            double dz = m.field_72307_f.z - pz;
            if (Math.sqrt(dx * dx + dz * dz) < 20.0D) {
               return false;
            }
         }

         this.targetPosX = m.field_72307_f.x;
         this.targetPosY = m.field_72307_f.y;
         this.targetPosZ = m.field_72307_f.z;
         this.direction = (int)MCH_Lib.getRotate360((double)(yaw + 45.0F)) / 90;
         this.direction += rand.nextBoolean() ? -1 : 1;
         this.direction %= 4;
         if (this.direction < 0) {
            this.direction += 4;
         }

         this.user = user;
         this.shooter = null;
         this.tick = 0;
         this.startTick = 50;
         this.cntAtk = 0;
         return true;
      } else {
         return false;
      }
   }
}
