package com.norwood.mcheli.flare;

import io.netty.buffer.ByteBuf;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityFlare extends W_Entity implements IEntityAdditionalSpawnData {
   public double gravity;
   public double airResistance;
   public float size;
   public int fuseCount;

   public MCH_EntityFlare(World par1World) {
      super(par1World);
      this.gravity = -0.013D;
      this.airResistance = 0.992D;
      this.func_70105_a(1.0F, 1.0F);
      this.field_70126_B = this.field_70177_z;
      this.field_70127_C = this.field_70125_A;
      this.size = 6.0F;
      this.fuseCount = 0;
   }

   public MCH_EntityFlare(World par1World, double pX, double pY, double pZ, double mX, double mY, double mZ, float size, int fuseCount) {
      this(par1World);
      this.func_70012_b(pX, pY, pZ, 0.0F, 0.0F);
      this.field_70159_w = mX;
      this.field_70181_x = mY;
      this.field_70179_y = mZ;
      this.size = size;
      this.fuseCount = fuseCount;
   }

   public boolean func_180431_b(DamageSource source) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_70112_a(double par1) {
      double d1 = this.func_174813_aQ().func_72320_b() * 4.0D;
      d1 *= 64.0D;
      return par1 < d1 * d1;
   }

   public void func_70106_y() {
      super.func_70106_y();
      if (this.fuseCount > 0 && this.field_70170_p.field_72995_K) {
         this.fuseCount = 0;

         for(int i = 0; i < 20; ++i) {
            double x = (this.field_70146_Z.nextDouble() - 0.5D) * 10.0D;
            double y = (this.field_70146_Z.nextDouble() - 0.5D) * 10.0D;
            double z = (this.field_70146_Z.nextDouble() - 0.5D) * 10.0D;
            MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", this.field_70165_t + x, this.field_70163_u + y, this.field_70161_v + z);
            prm.age = 200 + this.field_70146_Z.nextInt(100);
            prm.size = (float)(20 + this.field_70146_Z.nextInt(25));
            prm.motionX = (this.field_70146_Z.nextDouble() - 0.5D) * 0.45D;
            prm.motionY = (this.field_70146_Z.nextDouble() - 0.5D) * 0.01D;
            prm.motionZ = (this.field_70146_Z.nextDouble() - 0.5D) * 0.45D;
            prm.a = this.field_70146_Z.nextFloat() * 0.1F + 0.85F;
            prm.b = this.field_70146_Z.nextFloat() * 0.2F + 0.5F;
            prm.g = prm.b + 0.05F;
            prm.r = prm.b + 0.1F;
            MCH_ParticlesUtil.spawnParticle(prm);
         }
      }

   }

   public void writeSpawnData(ByteBuf buffer) {
      try {
         buffer.writeByte(this.fuseCount);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void readSpawnData(ByteBuf additionalData) {
      try {
         this.fuseCount = additionalData.readByte();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void func_70071_h_() {
      if (this.fuseCount > 0 && this.field_70173_aa >= this.fuseCount) {
         this.func_70106_y();
      } else if (!this.field_70170_p.field_72995_K && !this.field_70170_p.func_175667_e(new BlockPos(this.field_70165_t, this.field_70163_u, this.field_70161_v))) {
         this.func_70106_y();
      } else if (this.field_70173_aa > 300 && !this.field_70170_p.field_72995_K) {
         this.func_70106_y();
      } else {
         super.func_70071_h_();
         if (!this.field_70170_p.field_72995_K) {
            this.onUpdateCollided();
         }

         this.field_70165_t += this.field_70159_w;
         this.field_70163_u += this.field_70181_x;
         this.field_70161_v += this.field_70179_y;
         if (this.field_70170_p.field_72995_K) {
            double x = (this.field_70165_t - this.field_70169_q) / 2.0D;
            double y = (this.field_70163_u - this.field_70167_r) / 2.0D;
            double z = (this.field_70161_v - this.field_70166_s) / 2.0D;

            for(int i = 0; i < 2; ++i) {
               MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", this.field_70169_q + x * (double)i, this.field_70167_r + y * (double)i, this.field_70166_s + z * (double)i);
               prm.size = 6.0F + this.field_70146_Z.nextFloat();
               if (this.size < 5.0F) {
                  prm.a = (float)((double)prm.a * 0.75D);
                  if (this.field_70146_Z.nextInt(2) == 0) {
                  }
               }

               if (this.fuseCount > 0) {
                  prm.a = this.field_70146_Z.nextFloat() * 0.1F + 0.85F;
                  prm.b = this.field_70146_Z.nextFloat() * 0.1F + 0.5F;
                  prm.g = prm.b + 0.05F;
                  prm.r = prm.b + 0.1F;
               }

               MCH_ParticlesUtil.spawnParticle(prm);
            }
         }

         this.field_70181_x += this.gravity;
         this.field_70159_w *= this.airResistance;
         this.field_70179_y *= this.airResistance;
         if (this.func_70090_H() && !this.field_70170_p.field_72995_K) {
            this.func_70106_y();
         }

         if (this.field_70122_E && !this.field_70170_p.field_72995_K) {
            this.func_70106_y();
         }

         this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
      }

   }

   protected void onUpdateCollided() {
      Vec3d vec3 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);
      Vec3d vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
      RayTraceResult mop = W_WorldFunc.clip(this.field_70170_p, vec3, vec31);
      vec3 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);
      vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
      if (mop != null) {
         W_WorldFunc.getWorldVec3(this.field_70170_p, mop.field_72307_f.field_72450_a, mop.field_72307_f.field_72448_b, mop.field_72307_f.field_72449_c);
         this.onImpact(mop);
      }

   }

   protected void onImpact(RayTraceResult par1MovingObjectPosition) {
      if (!this.field_70170_p.field_72995_K) {
         this.func_70106_y();
      }

   }

   public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.func_74782_a("direction", this.func_70087_a(new double[]{this.field_70159_w, this.field_70181_x, this.field_70179_y}));
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

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }
}
