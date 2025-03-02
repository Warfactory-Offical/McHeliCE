package com.norwood.mcheli.throwable;

import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Explosion;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

public class MCH_EntityThrowable extends EntityThrowable implements IThrowableEntity {
   private static final DataParameter<String> INFO_NAME;
   private int countOnUpdate;
   private MCH_ThrowableInfo throwableInfo;
   public double boundPosX;
   public double boundPosY;
   public double boundPosZ;
   public RayTraceResult lastOnImpact;
   public int noInfoCount;

   public MCH_EntityThrowable(World par1World) {
      super(par1World);
      this.init();
   }

   public MCH_EntityThrowable(World par1World, EntityLivingBase par2EntityLivingBase, float acceleration) {
      super(par1World, par2EntityLivingBase);
      this.field_70159_w *= (double)acceleration;
      this.field_70181_x *= (double)acceleration;
      this.field_70179_y *= (double)acceleration;
      this.init();
   }

   public MCH_EntityThrowable(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
      this.init();
   }

   public MCH_EntityThrowable(World worldIn, double x, double y, double z, float yaw, float pitch) {
      this(worldIn);
      this.func_70105_a(0.25F, 0.25F);
      this.func_70012_b(x, y, z, yaw, pitch);
      this.posX -= (double)(MathHelper.func_76134_b(this.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
      this.posY -= 0.10000000149011612D;
      this.posZ -= (double)(MathHelper.func_76126_a(this.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
      this.func_70107_b(this.posX, this.posY, this.posZ);
      this.func_184538_a(null, pitch, yaw, 0.0F, 1.5F, 1.0F);
   }

   public void init() {
      this.lastOnImpact = null;
      this.countOnUpdate = 0;
      this.setInfo(null);
      this.noInfoCount = 0;
      this.dataManager.register(INFO_NAME, "");
   }

   public void func_184538_a(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
      float f = 0.4F;
      this.field_70159_w = (double)(-MathHelper.func_76126_a(rotationYawIn / 180.0F * 3.1415927F) * MathHelper.func_76134_b(rotationPitchIn / 180.0F * 3.1415927F) * f);
      this.field_70179_y = (double)(MathHelper.func_76134_b(rotationYawIn / 180.0F * 3.1415927F) * MathHelper.func_76134_b(rotationPitchIn / 180.0F * 3.1415927F) * f);
      this.field_70181_x = (double)(-MathHelper.func_76126_a((rotationPitchIn + pitchOffset) / 180.0F * 3.1415927F) * f);
      this.func_70186_c(this.field_70159_w, this.field_70181_x, this.field_70179_y, velocity, 1.0F);
   }

   public void func_70106_y() {
      String s = this.getInfo() != null ? this.getInfo().name : "null";
      MCH_Lib.DbgLog(this.world, "MCH_EntityThrowable.setDead(%s)", s);
      super.func_70106_y();
   }

   public void func_70071_h_() {
      this.boundPosX = this.posX;
      this.boundPosY = this.posY;
      this.boundPosZ = this.posZ;
      if (this.getInfo() != null) {
         Block block = W_WorldFunc.getBlock(this.world, (int)(this.posX + 0.5D), (int)this.posY, (int)(this.posZ + 0.5D));
         Material mat = W_WorldFunc.getBlockMaterial(this.world, (int)(this.posX + 0.5D), (int)this.posY, (int)(this.posZ + 0.5D));
         if (block != null && mat == Material.field_151586_h) {
            this.field_70181_x += (double)this.getInfo().gravityInWater;
         } else {
            this.field_70181_x += (double)this.getInfo().gravity;
         }
      }

      super.func_70071_h_();
      if (this.lastOnImpact != null) {
         this.boundBullet(this.lastOnImpact);
         this.func_70107_b(this.boundPosX + this.field_70159_w, this.boundPosY + this.field_70181_x, this.boundPosZ + this.field_70179_y);
         this.lastOnImpact = null;
      }

      ++this.countOnUpdate;
      if (this.countOnUpdate >= 2147483632) {
         this.func_70106_y();
      } else {
         if (this.getInfo() == null) {
            String s = (String)this.dataManager.func_187225_a(INFO_NAME);
            if (!s.isEmpty()) {
               this.setInfo(MCH_ThrowableInfoManager.get(s));
            }

            if (this.getInfo() == null) {
               ++this.noInfoCount;
               if (this.noInfoCount > 10) {
                  this.func_70106_y();
               }

               return;
            }
         }

         if (!this.field_70128_L) {
            if (!this.world.isRemote) {
               if (this.countOnUpdate == this.getInfo().timeFuse && this.getInfo().explosion > 0) {
                  MCH_Explosion.newExplosion(this.world, (Entity)null, (Entity)null, this.posX, this.posY, this.posZ, (float)this.getInfo().explosion, (float)this.getInfo().explosion, true, true, false, true, 0);
                  this.func_70106_y();
                  return;
               }

               if (this.countOnUpdate >= this.getInfo().aliveTime) {
                  this.func_70106_y();
               }
            } else if (this.countOnUpdate >= this.getInfo().timeFuse && this.getInfo().explosion <= 0) {
               for(int i = 0; i < this.getInfo().smokeNum; ++i) {
                  float r = this.getInfo().smokeColor.r * 0.9F + this.field_70146_Z.nextFloat() * 0.1F;
                  float g = this.getInfo().smokeColor.g * 0.9F + this.field_70146_Z.nextFloat() * 0.1F;
                  float b = this.getInfo().smokeColor.b * 0.9F + this.field_70146_Z.nextFloat() * 0.1F;
                  if (this.getInfo().smokeColor.r == this.getInfo().smokeColor.g) {
                     g = r;
                  }

                  if (this.getInfo().smokeColor.r == this.getInfo().smokeColor.b) {
                     b = r;
                  }

                  if (this.getInfo().smokeColor.g == this.getInfo().smokeColor.b) {
                     b = g;
                  }

                  this.spawnParticle("explode", 4, this.getInfo().smokeSize + this.field_70146_Z.nextFloat() * this.getInfo().smokeSize / 3.0F, r, g, b, this.getInfo().smokeVelocityHorizontal * (this.field_70146_Z.nextFloat() - 0.5F), this.getInfo().smokeVelocityVertical * this.field_70146_Z.nextFloat(), this.getInfo().smokeVelocityHorizontal * (this.field_70146_Z.nextFloat() - 0.5F));
               }
            }

         }
      }
   }

   public void spawnParticle(String name, int num, float size, float r, float g, float b, float mx, float my, float mz) {
      if (this.world.isRemote) {
         if (name.isEmpty() || num < 1) {
            return;
         }

         double x = (this.posX - this.field_70169_q) / (double)num;
         double y = (this.posY - this.field_70167_r) / (double)num;
         double z = (this.posZ - this.field_70166_s) / (double)num;

         for(int i = 0; i < num; ++i) {
            MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", this.field_70169_q + x * (double)i, 1.0D + this.field_70167_r + y * (double)i, this.field_70166_s + z * (double)i);
            prm.setMotion(mx, my, mz);
            prm.size = size;
            prm.setColor(1.0F, r, g, b);
            prm.isEffectWind = true;
            prm.toWhite = true;
            MCH_ParticlesUtil.spawnParticle(prm);
         }
      }

   }

   protected float func_70185_h() {
      return 0.0F;
   }

   public void boundBullet(RayTraceResult m) {
      if (m.field_178784_b != null) {
         float bound = this.getInfo().bound;
         switch(m.field_178784_b) {
         case DOWN:
         case UP:
            this.field_70159_w *= 0.8999999761581421D;
            this.field_70179_y *= 0.8999999761581421D;
            this.boundPosY = m.field_72307_f.y;
            if ((m.field_178784_b != EnumFacing.DOWN || !(this.field_70181_x > 0.0D)) && (m.field_178784_b != EnumFacing.UP || !(this.field_70181_x < 0.0D))) {
               this.field_70181_x = 0.0D;
            } else {
               this.field_70181_x = -this.field_70181_x * (double)bound;
            }
            break;
         case NORTH:
            if (this.field_70179_y > 0.0D) {
               this.field_70179_y = -this.field_70179_y * (double)bound;
            }
            break;
         case SOUTH:
            if (this.field_70179_y < 0.0D) {
               this.field_70179_y = -this.field_70179_y * (double)bound;
            }
            break;
         case WEST:
            if (this.field_70159_w > 0.0D) {
               this.field_70159_w = -this.field_70159_w * (double)bound;
            }
            break;
         case EAST:
            if (this.field_70159_w < 0.0D) {
               this.field_70159_w = -this.field_70159_w * (double)bound;
            }
         }

      }
   }

   protected void func_70184_a(RayTraceResult m) {
      if (this.getInfo() != null) {
         this.lastOnImpact = m;
      }

   }

   @Nullable
   public MCH_ThrowableInfo getInfo() {
      return this.throwableInfo;
   }

   public void setInfo(MCH_ThrowableInfo info) {
      this.throwableInfo = info;
      if (info != null && !this.world.isRemote) {
         this.dataManager.func_187227_b(INFO_NAME, info.name);
      }

   }

   public void setThrower(Entity entity) {
      if (entity instanceof EntityLivingBase) {
         this.field_70192_c = (EntityLivingBase)entity;
      }

   }

   static {
      INFO_NAME = EntityDataManager.createKey(MCH_EntityThrowable.class, DataSerializers.STRING);
   }
}
