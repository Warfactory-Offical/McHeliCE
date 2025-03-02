package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_Blocks;
import com.norwood.mcheli.wrapper.W_MovingObjectPosition;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class MCH_EntityMarkerRocket extends MCH_EntityBaseBullet {
   private static final DataParameter<Byte> MARKER_STATUS;
   public int countDown;

   public MCH_EntityMarkerRocket(World par1World) {
      super(par1World);
      this.setMarkerStatus(0);
      this.countDown = 0;
   }

   public MCH_EntityMarkerRocket(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
      super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
      this.setMarkerStatus(0);
      this.countDown = 0;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(MARKER_STATUS, (byte)0);
   }

   public void setMarkerStatus(int n) {
      if (!this.world.isRemote) {
         this.dataManager.func_187227_b(MARKER_STATUS, (byte)n);
      }

   }

   public int getMarkerStatus() {
      return (Byte)this.dataManager.func_187225_a(MARKER_STATUS);
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      return false;
   }

   public void func_70071_h_() {
      int status = this.getMarkerStatus();
      if (this.world.isRemote) {
         if (this.getInfo() == null) {
            super.func_70071_h_();
         }

         if (this.getInfo() != null && !this.getInfo().disableSmoke && status != 0) {
            if (status == 1) {
               super.func_70071_h_();
               this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 5.0F * this.getInfo().smokeSize * 0.5F);
            } else {
               float gb = this.field_70146_Z.nextFloat() * 0.3F;
               this.spawnParticle("explode", 5, (float)(10 + this.field_70146_Z.nextInt(4)), this.field_70146_Z.nextFloat() * 0.2F + 0.8F, gb, gb, (this.field_70146_Z.nextFloat() - 0.5F) * 0.7F, 0.3F + this.field_70146_Z.nextFloat() * 0.3F, (this.field_70146_Z.nextFloat() - 0.5F) * 0.7F);
            }
         }
      } else if (status != 0 && !this.func_70090_H()) {
         if (status == 1) {
            super.func_70071_h_();
         } else if (this.countDown > 0) {
            --this.countDown;
            if (this.countDown == 40) {
               int num = 6 + this.field_70146_Z.nextInt(2);

               for(int i = 0; i < num; ++i) {
                  MCH_EntityBomb e = new MCH_EntityBomb(this.world, this.posX + (double)((this.field_70146_Z.nextFloat() - 0.5F) * 15.0F), (double)(260.0F + this.field_70146_Z.nextFloat() * 10.0F + (float)(i * 30)), this.posZ + (double)((this.field_70146_Z.nextFloat() - 0.5F) * 15.0F), 0.0D, -0.5D, 0.0D, 0.0F, 90.0F, 4.0D);
                  e.setName(this.func_70005_c_());
                  e.explosionPower = 3 + this.field_70146_Z.nextInt(2);
                  e.explosionPowerInWater = 0;
                  e.setPower(30);
                  e.piercing = 0;
                  e.shootingAircraft = this.shootingAircraft;
                  e.shootingEntity = this.shootingEntity;
                  this.world.func_72838_d(e);
               }
            }
         } else {
            this.func_70106_y();
         }
      } else {
         this.func_70106_y();
      }

   }

   public void spawnParticle(String name, int num, float size, float r, float g, float b, float mx, float my, float mz) {
      if (this.world.isRemote) {
         if (name.isEmpty() || num < 1 || num > 50) {
            return;
         }

         double x = (this.posX - this.field_70169_q) / (double)num;
         double y = (this.posY - this.field_70167_r) / (double)num;
         double z = (this.posZ - this.field_70166_s) / (double)num;

         for(int i = 0; i < num; ++i) {
            MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", this.field_70169_q + x * (double)i, this.field_70167_r + y * (double)i, this.field_70166_s + z * (double)i);
            prm.motionX = (double)mx;
            prm.motionY = (double)my;
            prm.motionZ = (double)mz;
            prm.size = size + this.field_70146_Z.nextFloat();
            prm.setColor(1.0F, r, g, b);
            prm.isEffectWind = true;
            MCH_ParticlesUtil.spawnParticle(prm);
         }
      }

   }

   protected void onImpact(RayTraceResult m, float damageFactor) {
      if (!this.world.isRemote) {
         if (m.field_72308_g == null && !W_MovingObjectPosition.isHitTypeEntity(m)) {
            BlockPos blockpos = m.func_178782_a();
            blockpos = blockpos.func_177972_a(m.field_178784_b);
            if (this.world.func_175623_d(blockpos)) {
               if (MCH_Config.Explosion_FlamingBlock.prmBool) {
                  W_WorldFunc.setBlock(this.world, blockpos, W_Blocks.field_150480_ab);
               }

               int noAirBlockCount = 0;

               for(int i = 1; i < 256; ++i) {
                  if (!this.world.func_175623_d(blockpos.func_177981_b(i))) {
                     ++noAirBlockCount;
                     if (noAirBlockCount >= 5) {
                        break;
                     }
                  }
               }

               if (noAirBlockCount < 5) {
                  this.setMarkerStatus(2);
                  this.func_70107_b((double)blockpos.func_177958_n() + 0.5D, (double)blockpos.func_177956_o() + 1.1D, (double)blockpos.func_177952_p() + 0.5D);
                  this.field_70169_q = this.posX;
                  this.field_70167_r = this.posY;
                  this.field_70166_s = this.posZ;
                  this.countDown = 100;
               } else {
                  this.func_70106_y();
               }
            } else {
               this.func_70106_y();
            }

         }
      }
   }

   public MCH_BulletModel getDefaultBulletModel() {
      return MCH_DefaultBulletModels.Rocket;
   }

   static {
      MARKER_STATUS = EntityDataManager.createKey(MCH_EntityMarkerRocket.class, DataSerializers.field_187191_a);
   }
}
