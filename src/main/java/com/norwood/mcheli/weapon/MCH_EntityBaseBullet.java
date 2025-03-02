package com.norwood.mcheli.weapon;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Explosion;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.MCH_CriteriaTriggers;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntityHitBox;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_PacketNotifyHitBullet;
import com.norwood.mcheli.chain.MCH_EntityChain;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_EntityPlayer;
import com.norwood.mcheli.wrapper.W_MovingObjectPosition;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MCH_EntityBaseBullet extends W_Entity {
   private static final DataParameter<Integer> TARGET_ID;
   private static final DataParameter<String> INFO_NAME;
   private static final DataParameter<String> BULLET_MODEL;
   private static final DataParameter<Byte> BOMBLET_FLAG;
   public Entity shootingEntity;
   public Entity shootingAircraft;
   private int countOnUpdate;
   public int explosionPower;
   public int explosionPowerInWater;
   private int power;
   public double acceleration;
   public double accelerationFactor;
   public Entity targetEntity;
   public int piercing;
   public int delayFuse;
   public int sprinkleTime;
   public byte isBomblet;
   private MCH_WeaponInfo weaponInfo;
   private MCH_BulletModel model;
   public double prevPosX2;
   public double prevPosY2;
   public double prevPosZ2;
   public double prevMotionX;
   public double prevMotionY;
   public double prevMotionZ;

   public MCH_EntityBaseBullet(World par1World) {
      super(par1World);
      this.countOnUpdate = 0;
      this.func_70105_a(1.0F, 1.0F);
      this.field_70126_B = this.field_70177_z;
      this.field_70127_C = this.field_70125_A;
      this.targetEntity = null;
      this.setPower(1);
      this.acceleration = 1.0D;
      this.accelerationFactor = 1.0D;
      this.piercing = 0;
      this.explosionPower = 0;
      this.explosionPowerInWater = 0;
      this.delayFuse = 0;
      this.sprinkleTime = 0;
      this.isBomblet = -1;
      this.weaponInfo = null;
      this.noClip = true;
      if (par1World.isRemote) {
         this.model = null;
      }

   }

   public MCH_EntityBaseBullet(World par1World, double px, double py, double pz, double mx, double my, double mz, float yaw, float pitch, double acceleration) {
      this(par1World);
      this.func_70105_a(1.0F, 1.0F);
      this.func_70012_b(px, py, pz, yaw, pitch);
      this.func_70107_b(px, py, pz);
      this.field_70126_B = yaw;
      this.field_70127_C = pitch;
      if (acceleration > 3.9D) {
         acceleration = 3.9D;
      }

      double d = (double)MathHelper.func_76133_a(mx * mx + my * my + mz * mz);
      this.field_70159_w = mx * acceleration / d;
      this.field_70181_x = my * acceleration / d;
      this.field_70179_y = mz * acceleration / d;
      this.prevMotionX = this.field_70159_w;
      this.prevMotionY = this.field_70181_x;
      this.prevMotionZ = this.field_70179_y;
      this.acceleration = acceleration;
   }

   public void func_70012_b(double par1, double par3, double par5, float par7, float par8) {
      super.func_70012_b(par1, par3, par5, par7, par8);
      this.prevPosX2 = par1;
      this.prevPosY2 = par3;
      this.prevPosZ2 = par5;
   }

   public void func_70080_a(double x, double y, double z, float yaw, float pitch) {
      super.func_70080_a(x, y, z, yaw, pitch);
   }

   protected void func_70101_b(float yaw, float pitch) {
      super.func_70101_b(yaw, this.field_70125_A);
   }

   @SideOnly(Side.CLIENT)
   public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
      this.func_70107_b(x, (y + this.posY * 2.0D) / 3.0D, z);
      this.func_70101_b(yaw, pitch);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(TARGET_ID, 0);
      this.dataManager.register(INFO_NAME, "");
      this.dataManager.register(BULLET_MODEL, "");
      this.dataManager.register(BOMBLET_FLAG, (byte)0);
   }

   public void setName(String s) {
      if (s != null && !s.isEmpty()) {
         this.weaponInfo = MCH_WeaponInfoManager.get(s);
         if (this.weaponInfo != null) {
            if (!this.world.isRemote) {
               this.dataManager.func_187227_b(INFO_NAME, s);
            }

            this.onSetWeasponInfo();
         }
      }

   }

   public String func_70005_c_() {
      return (String)this.dataManager.func_187225_a(INFO_NAME);
   }

   @Nullable
   public MCH_WeaponInfo getInfo() {
      return this.weaponInfo;
   }

   public void onSetWeasponInfo() {
      if (!this.world.isRemote) {
         this.isBomblet = 0;
      }

      if (this.getInfo().bomblet > 0) {
         this.sprinkleTime = this.getInfo().bombletSTime;
      }

      this.piercing = this.getInfo().piercing;
      if (this instanceof MCH_EntityBullet) {
         if (this.getInfo().acceleration > 4.0F) {
            this.accelerationFactor = (double)(this.getInfo().acceleration / 4.0F);
         }
      } else if (this instanceof MCH_EntityRocket && this.isBomblet == 0 && this.getInfo().acceleration > 4.0F) {
         this.accelerationFactor = (double)(this.getInfo().acceleration / 4.0F);
      }

   }

   public void func_70106_y() {
      super.func_70106_y();
   }

   public void setBomblet() {
      this.isBomblet = 1;
      this.sprinkleTime = 0;
      this.dataManager.func_187227_b(BOMBLET_FLAG, (byte)1);
   }

   public byte getBomblet() {
      return (Byte)this.dataManager.func_187225_a(BOMBLET_FLAG);
   }

   public void setTargetEntity(@Nullable Entity entity) {
      this.targetEntity = entity;
      if (!this.world.isRemote) {
         if (this.targetEntity instanceof EntityPlayerMP) {
            MCH_Lib.DbgLog(this.world, "MCH_EntityBaseBullet.setTargetEntity alert" + this.targetEntity + " / " + this.targetEntity.func_184187_bx());
            if (this.targetEntity.func_184187_bx() != null && !(this.targetEntity.func_184187_bx() instanceof MCH_EntityAircraft) && !(this.targetEntity.func_184187_bx() instanceof MCH_EntitySeat)) {
               W_WorldFunc.MOD_playSoundAtEntity(this.targetEntity, "alert", 2.0F, 1.0F);
            }
         }

         if (entity != null) {
            this.dataManager.func_187227_b(TARGET_ID, W_Entity.getEntityId(entity));
         } else {
            this.dataManager.func_187227_b(TARGET_ID, 0);
         }
      }

   }

   public int getTargetEntityID() {
      return this.targetEntity != null ? W_Entity.getEntityId(this.targetEntity) : (Integer)this.dataManager.func_187225_a(TARGET_ID);
   }

   public MCH_BulletModel getBulletModel() {
      if (this.getInfo() == null) {
         return null;
      } else if (this.isBomblet < 0) {
         return null;
      } else {
         if (this.model == null) {
            if (this.isBomblet == 1) {
               this.model = this.getInfo().bombletModel;
            } else {
               this.model = this.getInfo().bulletModel;
            }

            if (this.model == null) {
               this.model = this.getDefaultBulletModel();
            }
         }

         return this.model;
      }
   }

   public abstract MCH_BulletModel getDefaultBulletModel();

   public void sprinkleBomblet() {
   }

   public void spawnParticle(String name, int num, float size) {
      if (this.world.isRemote) {
         if (name.isEmpty() || num < 1 || num > 50) {
            return;
         }

         double x = (this.posX - this.field_70169_q) / (double)num;
         double y = (this.posY - this.field_70167_r) / (double)num;
         double z = (this.posZ - this.field_70166_s) / (double)num;
         double x2 = (this.field_70169_q - this.prevPosX2) / (double)num;
         double y2 = (this.field_70167_r - this.prevPosY2) / (double)num;
         double z2 = (this.field_70166_s - this.prevPosZ2) / (double)num;
         int i;
         if (name.equals("explode")) {
            for(i = 0; i < num; ++i) {
               MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", (this.field_70169_q + x * (double)i + this.prevPosX2 + x2 * (double)i) / 2.0D, (this.field_70167_r + y * (double)i + this.prevPosY2 + y2 * (double)i) / 2.0D, (this.field_70166_s + z * (double)i + this.prevPosZ2 + z2 * (double)i) / 2.0D);
               prm.size = size + this.field_70146_Z.nextFloat();
               MCH_ParticlesUtil.spawnParticle(prm);
            }
         } else {
            for(i = 0; i < num; ++i) {
               MCH_ParticlesUtil.DEF_spawnParticle(name, (this.field_70169_q + x * (double)i + this.prevPosX2 + x2 * (double)i) / 2.0D, (this.field_70167_r + y * (double)i + this.prevPosY2 + y2 * (double)i) / 2.0D, (this.field_70166_s + z * (double)i + this.prevPosZ2 + z2 * (double)i) / 2.0D, 0.0D, 0.0D, 0.0D, 50.0F);
            }
         }
      }

   }

   public void DEF_spawnParticle(String name, int num, float size) {
      if (this.world.isRemote) {
         if (name.isEmpty() || num < 1 || num > 50) {
            return;
         }

         double x = (this.posX - this.field_70169_q) / (double)num;
         double y = (this.posY - this.field_70167_r) / (double)num;
         double z = (this.posZ - this.field_70166_s) / (double)num;
         double x2 = (this.field_70169_q - this.prevPosX2) / (double)num;
         double y2 = (this.field_70167_r - this.prevPosY2) / (double)num;
         double z2 = (this.field_70166_s - this.prevPosZ2) / (double)num;

         for(int i = 0; i < num; ++i) {
            MCH_ParticlesUtil.DEF_spawnParticle(name, (this.field_70169_q + x * (double)i + this.prevPosX2 + x2 * (double)i) / 2.0D, (this.field_70167_r + y * (double)i + this.prevPosY2 + y2 * (double)i) / 2.0D, (this.field_70166_s + z * (double)i + this.prevPosZ2 + z2 * (double)i) / 2.0D, 0.0D, 0.0D, 0.0D, 150.0F);
         }
      }

   }

   public int getCountOnUpdate() {
      return this.countOnUpdate;
   }

   public void clearCountOnUpdate() {
      this.countOnUpdate = 0;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_70112_a(double par1) {
      double d1 = this.func_174813_aQ().func_72320_b() * 4.0D;
      d1 *= 64.0D;
      return par1 < d1 * d1;
   }

   public void setParameterFromWeapon(MCH_WeaponBase w, Entity entity, Entity user) {
      this.explosionPower = w.explosionPower;
      this.explosionPowerInWater = w.explosionPowerInWater;
      this.setPower(w.power);
      this.piercing = w.piercing;
      this.shootingAircraft = entity;
      this.shootingEntity = user;
   }

   public void setParameterFromWeapon(MCH_EntityBaseBullet b, Entity entity, Entity user) {
      this.explosionPower = b.explosionPower;
      this.explosionPowerInWater = b.explosionPowerInWater;
      this.setPower(b.getPower());
      this.piercing = b.piercing;
      this.shootingAircraft = entity;
      this.shootingEntity = user;
   }

   public void setMotion(double targetX, double targetY, double targetZ) {
      double d6 = (double)MathHelper.func_76133_a(targetX * targetX + targetY * targetY + targetZ * targetZ);
      this.field_70159_w = targetX * this.acceleration / d6;
      this.field_70181_x = targetY * this.acceleration / d6;
      this.field_70179_y = targetZ * this.acceleration / d6;
   }

   public boolean usingFlareOfTarget(Entity entity) {
      if (this.getCountOnUpdate() % 3 == 0) {
         List<Entity> list = this.world.func_72839_b(this, entity.func_174813_aQ().func_72314_b(15.0D, 15.0D, 15.0D));

         for(int i = 0; i < list.size(); ++i) {
            if (((Entity)list.get(i)).getEntityData().getBoolean("FlareUsing")) {
               return true;
            }
         }
      }

      return false;
   }

   public void guidanceToTarget(double targetPosX, double targetPosY, double targetPosZ) {
      this.guidanceToTarget(targetPosX, targetPosY, targetPosZ, 1.0F);
   }

   public void guidanceToTarget(double targetPosX, double targetPosY, double targetPosZ, float accelerationFactor) {
      double tx = targetPosX - this.posX;
      double ty = targetPosY - this.posY;
      double tz = targetPosZ - this.posZ;
      double d = (double)MathHelper.func_76133_a(tx * tx + ty * ty + tz * tz);
      double mx = tx * this.acceleration / d;
      double my = ty * this.acceleration / d;
      double mz = tz * this.acceleration / d;
      this.field_70159_w = (this.field_70159_w * 6.0D + mx) / 7.0D;
      this.field_70181_x = (this.field_70181_x * 6.0D + my) / 7.0D;
      this.field_70179_y = (this.field_70179_y * 6.0D + mz) / 7.0D;
      double a = (double)((float)Math.atan2(this.field_70179_y, this.field_70159_w));
      this.field_70177_z = (float)(a * 180.0D / 3.141592653589793D) - 90.0F;
      double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0D / 3.141592653589793D));
   }

   public boolean checkValid() {
      if (this.shootingEntity == null && this.shootingAircraft == null) {
         return false;
      } else {
         Entity shooter = this.shootingAircraft != null && this.shootingAircraft.field_70128_L && this.shootingEntity == null ? this.shootingAircraft : this.shootingEntity;
         double x = this.posX - shooter.posX;
         double z = this.posZ - shooter.posZ;
         return x * x + z * z < 3.38724E7D && this.posY > -10.0D;
      }
   }

   public float getGravity() {
      return this.getInfo() != null ? this.getInfo().gravity : 0.0F;
   }

   public float getGravityInWater() {
      return this.getInfo() != null ? this.getInfo().gravityInWater : 0.0F;
   }

   public void func_70071_h_() {
      if (this.world.isRemote && this.countOnUpdate == 0) {
         int tgtEttId = this.getTargetEntityID();
         if (tgtEttId > 0) {
            this.setTargetEntity(this.world.func_73045_a(tgtEttId));
         }
      }

      if (!this.world.isRemote && this.getCountOnUpdate() % 20 == 19 && this.targetEntity instanceof EntityPlayerMP) {
         MCH_Lib.DbgLog(this.world, "MCH_EntityBaseBullet.onUpdate alert" + this.targetEntity + " / " + this.targetEntity.func_184187_bx());
         if (this.targetEntity.func_184187_bx() != null && !(this.targetEntity.func_184187_bx() instanceof MCH_EntityAircraft) && !(this.targetEntity.func_184187_bx() instanceof MCH_EntitySeat)) {
            W_WorldFunc.MOD_playSoundAtEntity(this.targetEntity, "alert", 2.0F, 1.0F);
         }
      }

      this.prevMotionX = this.field_70159_w;
      this.prevMotionY = this.field_70181_x;
      this.prevMotionZ = this.field_70179_y;
      ++this.countOnUpdate;
      if (this.countOnUpdate > 10000000) {
         this.clearCountOnUpdate();
      }

      this.prevPosX2 = this.field_70169_q;
      this.prevPosY2 = this.field_70167_r;
      this.prevPosZ2 = this.field_70166_s;
      super.func_70071_h_();
      if ((this.prevMotionX != this.field_70159_w || this.prevMotionY != this.field_70181_x || this.prevMotionZ != this.field_70179_y) && this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y > 0.1D) {
         double a = (double)((float)Math.atan2(this.field_70179_y, this.field_70159_w));
         this.field_70177_z = (float)(a * 180.0D / 3.141592653589793D) - 90.0F;
         double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
         this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0D / 3.141592653589793D));
      }

      if (this.getInfo() == null) {
         if (this.countOnUpdate >= 2) {
            MCH_Lib.Log((Entity)this, "##### MCH_EntityBaseBullet onUpdate() Weapon info null %d, %s, Name=%s", W_Entity.getEntityId(this), this.getEntityName(), this.func_70005_c_());
            this.func_70106_y();
            return;
         }

         this.setName(this.func_70005_c_());
         if (this.getInfo() == null) {
            return;
         }
      }

      if (this.getInfo().bound <= 0.0F && this.field_70122_E) {
         this.field_70159_w *= 0.9D;
         this.field_70179_y *= 0.9D;
      }

      if (this.world.isRemote && this.isBomblet < 0) {
         this.isBomblet = this.getBomblet();
      }

      if (!this.world.isRemote) {
         BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
         if ((int)this.posY <= 255 && !this.world.func_175667_e(blockpos)) {
            if (this.getInfo().delayFuse <= 0) {
               this.func_70106_y();
               return;
            }

            if (this.delayFuse == 0) {
               this.delayFuse = this.getInfo().delayFuse;
            }
         }

         if (this.delayFuse > 0) {
            --this.delayFuse;
            if (this.delayFuse == 0) {
               this.onUpdateTimeout();
               this.func_70106_y();
               return;
            }
         }

         if (!this.checkValid()) {
            this.func_70106_y();
            return;
         }

         if (this.getInfo().timeFuse > 0 && this.getCountOnUpdate() > this.getInfo().timeFuse) {
            this.onUpdateTimeout();
            this.func_70106_y();
            return;
         }

         if (this.getInfo().explosionAltitude > 0 && MCH_Lib.getBlockIdY(this, 3, -this.getInfo().explosionAltitude) != 0) {
            RayTraceResult mop = new RayTraceResult(new Vec3d(this.posX, this.posY, this.posZ), EnumFacing.DOWN, new BlockPos(this.posX, this.posY, this.posZ));
            this.onImpact(mop, 1.0F);
         }
      }

      if (!this.func_70090_H()) {
         this.field_70181_x += (double)this.getGravity();
      } else {
         this.field_70181_x += (double)this.getGravityInWater();
      }

      if (!this.field_70128_L) {
         this.onUpdateCollided();
      }

      this.posX += this.field_70159_w * this.accelerationFactor;
      this.posY += this.field_70181_x * this.accelerationFactor;
      this.posZ += this.field_70179_y * this.accelerationFactor;
      if (this.world.isRemote) {
         this.updateSplash();
      }

      if (this.func_70090_H()) {
         float f3 = 0.25F;
         this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX - this.field_70159_w * (double)f3, this.posY - this.field_70181_x * (double)f3, this.posZ - this.field_70179_y * (double)f3, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
      }

      this.func_70107_b(this.posX, this.posY, this.posZ);
   }

   public void updateSplash() {
      if (this.getInfo() != null) {
         if (this.getInfo().power > 0) {
            if (!W_WorldFunc.isBlockWater(this.world, (int)(this.field_70169_q + 0.5D), (int)(this.field_70167_r + 0.5D), (int)(this.field_70166_s + 0.5D)) && W_WorldFunc.isBlockWater(this.world, (int)(this.posX + 0.5D), (int)(this.posY + 0.5D), (int)(this.posZ + 0.5D))) {
               double x = this.posX - this.field_70169_q;
               double y = this.posY - this.field_70167_r;
               double z = this.posZ - this.field_70166_s;
               double d = Math.sqrt(x * x + y * y + z * z);
               if (d <= 0.15D) {
                  return;
               }

               x /= d;
               y /= d;
               z /= d;
               double px = this.field_70169_q;
               double py = this.field_70167_r;
               double pz = this.field_70166_s;

               for(int i = 0; (double)i <= d; ++i) {
                  px += x;
                  py += y;
                  pz += z;
                  if (W_WorldFunc.isBlockWater(this.world, (int)(px + 0.5D), (int)(py + 0.5D), (int)(pz + 0.5D))) {
                     float pwr = this.getInfo().power < 20 ? (float)this.getInfo().power : 20.0F;
                     int n = this.field_70146_Z.nextInt(1 + (int)pwr / 3) + (int)pwr / 2 + 1;
                     pwr *= 0.03F;

                     for(int j = 0; j < n; ++j) {
                        MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "splash", px, py + 0.5D, pz, (double)pwr * (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D, (double)pwr * (this.field_70146_Z.nextDouble() * 0.5D + 0.5D) * 1.8D, (double)pwr * (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D, pwr * 5.0F);
                        MCH_ParticlesUtil.spawnParticle(prm);
                     }

                     return;
                  }
               }
            }

         }
      }
   }

   public void onUpdateTimeout() {
      if (this.func_70090_H()) {
         if (this.explosionPowerInWater > 0) {
            this.newExplosion(this.posX, this.posY, this.posZ, (float)this.explosionPowerInWater, (float)this.explosionPowerInWater, true);
         }
      } else if (this.explosionPower > 0) {
         this.newExplosion(this.posX, this.posY, this.posZ, (float)this.explosionPower, (float)this.getInfo().explosionBlock, false);
      } else if (this.explosionPower < 0) {
         this.playExplosionSound();
      }

   }

   public void onUpdateBomblet() {
      if (!this.world.isRemote && this.sprinkleTime > 0 && !this.field_70128_L) {
         --this.sprinkleTime;
         if (this.sprinkleTime == 0) {
            for(int i = 0; i < this.getInfo().bomblet; ++i) {
               this.sprinkleBomblet();
            }

            this.func_70106_y();
         }
      }

   }

   public void boundBullet(EnumFacing sideHit) {
      switch(sideHit) {
      case DOWN:
         if (this.field_70181_x > 0.0D) {
            this.field_70181_x = -this.field_70181_x * (double)this.getInfo().bound;
         }
         break;
      case UP:
         if (this.field_70181_x < 0.0D) {
            this.field_70181_x = -this.field_70181_x * (double)this.getInfo().bound;
         }
         break;
      case NORTH:
         if (this.field_70179_y > 0.0D) {
            this.field_70179_y = -this.field_70179_y * (double)this.getInfo().bound;
         } else {
            this.posZ += this.field_70179_y;
         }
         break;
      case SOUTH:
         if (this.field_70179_y < 0.0D) {
            this.field_70179_y = -this.field_70179_y * (double)this.getInfo().bound;
         } else {
            this.posZ += this.field_70179_y;
         }
         break;
      case WEST:
         if (this.field_70159_w > 0.0D) {
            this.field_70159_w = -this.field_70159_w * (double)this.getInfo().bound;
         } else {
            this.posX += this.field_70159_w;
         }
         break;
      case EAST:
         if (this.field_70159_w < 0.0D) {
            this.field_70159_w = -this.field_70159_w * (double)this.getInfo().bound;
         } else {
            this.posX += this.field_70159_w;
         }
      }

      if (this.getInfo().bound <= 0.0F) {
         this.field_70159_w *= 0.25D;
         this.field_70181_x *= 0.25D;
         this.field_70179_y *= 0.25D;
      }

   }

   protected void onUpdateCollided() {
      float damageFator = 1.0F;
      double mx = this.field_70159_w * this.accelerationFactor;
      double my = this.field_70181_x * this.accelerationFactor;
      double mz = this.field_70179_y * this.accelerationFactor;
      RayTraceResult m = null;

      Vec3d vec31;
      for(int i = 0; i < 5; ++i) {
         vec31 = W_WorldFunc.getWorldVec3(this.world, this.posX, this.posY, this.posZ);
         Vec3d vec31 = W_WorldFunc.getWorldVec3(this.world, this.posX + mx, this.posY + my, this.posZ + mz);
         m = W_WorldFunc.clip(this.world, vec31, vec31);
         boolean continueClip = false;
         if (this.shootingEntity != null && W_MovingObjectPosition.isHitTypeTile(m)) {
            Block block = W_WorldFunc.getBlock(this.world, m.func_178782_a());
            if (MCH_Config.bulletBreakableBlocks.contains(block)) {
               W_WorldFunc.destroyBlock(this.world, m.func_178782_a(), true);
               continueClip = true;
            }
         }

         if (!continueClip) {
            break;
         }
      }

      Vec3d vec3 = W_WorldFunc.getWorldVec3(this.world, this.posX, this.posY, this.posZ);
      vec31 = W_WorldFunc.getWorldVec3(this.world, this.posX + mx, this.posY + my, this.posZ + mz);
      if (this.getInfo().delayFuse > 0) {
         if (m != null) {
            this.boundBullet(m.field_178784_b);
            if (this.delayFuse == 0) {
               this.delayFuse = this.getInfo().delayFuse;
            }
         }

      } else {
         if (m != null) {
            vec31 = W_WorldFunc.getWorldVec3(this.world, m.field_72307_f.x, m.field_72307_f.y, m.field_72307_f.z);
         }

         Entity entity = null;
         List<Entity> list = this.world.func_72839_b(this, this.func_174813_aQ().func_72321_a(mx, my, mz).func_72314_b(21.0D, 21.0D, 21.0D));
         double d0 = 0.0D;

         for(int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity)list.get(j);
            if (this.canBeCollidedEntity(entity1)) {
               float f = 0.3F;
               AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_72314_b((double)f, (double)f, (double)f);
               RayTraceResult m1 = axisalignedbb.func_72327_a(vec3, vec31);
               if (m1 != null) {
                  double d1 = vec3.func_72438_d(m1.field_72307_f);
                  if (d1 < d0 || d0 == 0.0D) {
                     entity = entity1;
                     d0 = d1;
                  }
               }
            }
         }

         if (entity != null) {
            m = new RayTraceResult(entity);
         }

         if (m != null) {
            this.onImpact(m, damageFator);
         }

      }
   }

   public boolean canBeCollidedEntity(Entity entity) {
      if (entity instanceof MCH_EntityChain) {
         return false;
      } else if (!entity.func_70067_L()) {
         return false;
      } else {
         if (entity instanceof MCH_EntityBaseBullet) {
            if (this.world.isRemote) {
               return false;
            }

            MCH_EntityBaseBullet blt = (MCH_EntityBaseBullet)entity;
            if (W_Entity.isEqual(blt.shootingAircraft, this.shootingAircraft)) {
               return false;
            }

            if (W_Entity.isEqual(blt.shootingEntity, this.shootingEntity)) {
               return false;
            }
         }

         if (entity instanceof MCH_EntitySeat) {
            return false;
         } else if (entity instanceof MCH_EntityHitBox) {
            return false;
         } else if (W_Entity.isEqual(entity, this.shootingEntity)) {
            return false;
         } else {
            if (this.shootingAircraft instanceof MCH_EntityAircraft) {
               if (W_Entity.isEqual(entity, this.shootingAircraft)) {
                  return false;
               }

               if (((MCH_EntityAircraft)this.shootingAircraft).isMountedEntity(entity)) {
                  return false;
               }
            }

            Iterator var4 = MCH_Config.IgnoreBulletHitList.iterator();

            String s;
            do {
               if (!var4.hasNext()) {
                  return true;
               }

               s = (String)var4.next();
            } while(entity.getClass().getName().toLowerCase().indexOf(s.toLowerCase()) < 0);

            return false;
         }
      }
   }

   public void notifyHitBullet() {
      if (this.shootingAircraft instanceof MCH_EntityAircraft && W_EntityPlayer.isPlayer(this.shootingEntity)) {
         MCH_PacketNotifyHitBullet.send((MCH_EntityAircraft)this.shootingAircraft, (EntityPlayer)this.shootingEntity);
      }

      if (W_EntityPlayer.isPlayer(this.shootingEntity)) {
         MCH_PacketNotifyHitBullet.send((MCH_EntityAircraft)null, (EntityPlayer)this.shootingEntity);
      }

   }

   protected void onImpact(RayTraceResult m, float damageFactor) {
      float expPower;
      if (!this.world.isRemote) {
         if (m.field_72308_g != null) {
            this.onImpactEntity(m.field_72308_g, damageFactor);
            this.piercing = 0;
         }

         expPower = (float)this.explosionPower * damageFactor;
         float expPowerInWater = (float)this.explosionPowerInWater * damageFactor;
         double dx = 0.0D;
         double dy = 0.0D;
         double dz = 0.0D;
         if (this.piercing > 0) {
            --this.piercing;
            if (expPower > 0.0F) {
               this.newExplosion(m.field_72307_f.x + dx, m.field_72307_f.y + dy, m.field_72307_f.z + dz, 1.0F, 1.0F, false);
            }
         } else {
            if (expPowerInWater == 0.0F) {
               if (this.getInfo().isFAE) {
                  this.newFAExplosion(this.posX, this.posY, this.posZ, expPower, (float)this.getInfo().explosionBlock);
               } else if (expPower > 0.0F) {
                  this.newExplosion(m.field_72307_f.x + dx, m.field_72307_f.y + dy, m.field_72307_f.z + dz, expPower, (float)this.getInfo().explosionBlock, false);
               } else if (expPower < 0.0F) {
                  this.playExplosionSound();
               }
            } else if (m.field_72308_g != null) {
               if (this.func_70090_H()) {
                  this.newExplosion(m.field_72307_f.x + dx, m.field_72307_f.y + dy, m.field_72307_f.z + dz, expPowerInWater, expPowerInWater, true);
               } else {
                  this.newExplosion(m.field_72307_f.x + dx, m.field_72307_f.y + dy, m.field_72307_f.z + dz, expPower, (float)this.getInfo().explosionBlock, false);
               }
            } else if (!this.func_70090_H() && !MCH_Lib.isBlockInWater(this.world, m.func_178782_a().func_177958_n(), m.func_178782_a().func_177956_o(), m.func_178782_a().func_177952_p())) {
               if (expPower > 0.0F) {
                  this.newExplosion(m.field_72307_f.x + dx, m.field_72307_f.y + dy, m.field_72307_f.z + dz, expPower, (float)this.getInfo().explosionBlock, false);
               } else if (expPower < 0.0F) {
                  this.playExplosionSound();
               }
            } else {
               this.newExplosion((double)m.func_178782_a().func_177958_n(), (double)m.func_178782_a().func_177956_o(), (double)m.func_178782_a().func_177952_p(), expPowerInWater, expPowerInWater, true);
            }

            this.func_70106_y();
         }
      } else if (this.getInfo() != null && (this.getInfo().explosion == 0 || this.getInfo().modeNum >= 2) && W_MovingObjectPosition.isHitTypeTile(m)) {
         expPower = (float)this.getInfo().power;

         for(int i = 0; (float)i < expPower / 3.0F; ++i) {
            MCH_ParticlesUtil.spawnParticleTileCrack(this.world, m.func_178782_a().func_177958_n(), m.func_178782_a().func_177956_o(), m.func_178782_a().func_177952_p(), m.field_72307_f.x + ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)expPower / 10.0D, m.field_72307_f.y + 0.1D, m.field_72307_f.z + ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)expPower / 10.0D, -this.field_70159_w * (double)expPower / 2.0D, (double)(expPower / 2.0F), -this.field_70179_y * (double)expPower / 2.0D);
         }
      }

   }

   public void onImpactEntity(Entity entity, float damageFactor) {
      if (!entity.field_70128_L) {
         MCH_Lib.DbgLog(this.world, "MCH_EntityBaseBullet.onImpactEntity:Damage=%d:" + entity.getClass(), this.getPower());
         MCH_Lib.applyEntityHurtResistantTimeConfig(entity);
         DamageSource ds = DamageSource.func_76356_a(this, this.shootingEntity);
         float damage = MCH_Config.applyDamageVsEntity(entity, ds, (float)this.getPower() * damageFactor);
         damage *= this.getInfo() != null ? this.getInfo().getDamageFactor(entity) : 1.0F;
         entity.func_70097_a(ds, damage);
         if (this instanceof MCH_EntityBullet && entity instanceof EntityVillager && this.shootingEntity != null && this.shootingEntity instanceof EntityPlayerMP && this.shootingEntity.func_184187_bx() instanceof MCH_EntitySeat) {
            MCH_CriteriaTriggers.VILLAGER_HURT_BULLET.trigger((EntityPlayerMP)this.shootingEntity);
         }

         if (!entity.field_70128_L) {
         }
      }

      this.notifyHitBullet();
   }

   public void newFAExplosion(double x, double y, double z, float exp, float expBlock) {
      MCH_Explosion.ExplosionResult result = MCH_Explosion.newExplosion(this.world, this, this.shootingEntity, x, y, z, exp, expBlock, true, true, this.getInfo().flaming, false, 15);
      if (result != null && result.hitEntity) {
         this.notifyHitBullet();
      }

   }

   public void newExplosion(double x, double y, double z, float exp, float expBlock, boolean inWater) {
      MCH_Explosion.ExplosionResult result;
      if (!inWater) {
         result = MCH_Explosion.newExplosion(this.world, this, this.shootingEntity, x, y, z, exp, expBlock, this.field_70146_Z.nextInt(3) == 0, true, this.getInfo().flaming, true, 0, this.getInfo() != null ? this.getInfo().damageFactor : null);
      } else {
         result = MCH_Explosion.newExplosionInWater(this.world, this, this.shootingEntity, x, y, z, exp, expBlock, this.field_70146_Z.nextInt(3) == 0, true, this.getInfo().flaming, true, 0, this.getInfo() != null ? this.getInfo().damageFactor : null);
      }

      if (result != null && result.hitEntity) {
         this.notifyHitBullet();
      }

   }

   public void playExplosionSound() {
      MCH_Explosion.playExplosionSound(this.world, this.posX, this.posY, this.posZ);
   }

   public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.func_74782_a("direction", this.func_70087_a(new double[]{this.field_70159_w, this.field_70181_x, this.field_70179_y}));
      par1NBTTagCompound.setString("WeaponName", this.func_70005_c_());
   }

   public void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      this.func_70106_y();
   }

   public boolean func_70067_L() {
      return true;
   }

   public float func_70111_Y() {
      return 1.0F;
   }

   public boolean func_70097_a(DamageSource ds, float par2) {
      if (this.func_180431_b(ds)) {
         return false;
      } else if (!this.world.isRemote && par2 > 0.0F && ds.func_76355_l().equalsIgnoreCase("thrown")) {
         this.func_70018_K();
         Vec3d pos = new Vec3d(this.posX, this.posY, this.posZ);
         RayTraceResult m = new RayTraceResult(pos, EnumFacing.DOWN, new BlockPos(pos));
         this.onImpact(m, 1.0F);
         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   public int getPower() {
      return this.power;
   }

   public void setPower(int power) {
      this.power = power;
   }

   static {
      TARGET_ID = EntityDataManager.createKey(MCH_EntityBaseBullet.class, DataSerializers.VARINT);
      INFO_NAME = EntityDataManager.createKey(MCH_EntityBaseBullet.class, DataSerializers.STRING);
      BULLET_MODEL = EntityDataManager.createKey(MCH_EntityBaseBullet.class, DataSerializers.STRING);
      BOMBLET_FLAG = EntityDataManager.createKey(MCH_EntityBaseBullet.class, DataSerializers.field_187191_a);
   }
}
