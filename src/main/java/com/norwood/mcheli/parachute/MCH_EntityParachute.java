package com.norwood.mcheli.parachute;

import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.entity.IEntitySinglePassenger;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_AxisAlignedBB;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityParachute extends W_Entity implements IEntitySinglePassenger {
   private static final DataParameter<Byte> TYPE;
   private double speedMultiplier;
   private int paraPosRotInc;
   private double paraX;
   private double paraY;
   private double paraZ;
   private double paraYaw;
   private double paraPitch;
   @SideOnly(Side.CLIENT)
   private double velocityX;
   @SideOnly(Side.CLIENT)
   private double velocityY;
   @SideOnly(Side.CLIENT)
   private double velocityZ;
   public Entity user;
   public int onGroundCount;

   public MCH_EntityParachute(World par1World) {
      super(par1World);
      this.speedMultiplier = 0.07D;
      this.field_70156_m = true;
      this.func_70105_a(1.5F, 0.6F);
      this.user = null;
      this.onGroundCount = 0;
   }

   public MCH_EntityParachute(World par1World, double par2, double par4, double par6) {
      this(par1World);
      this.func_70107_b(par2, par4, par6);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.field_70169_q = par2;
      this.field_70167_r = par4;
      this.field_70166_s = par6;
   }

   protected boolean func_70041_e_() {
      return false;
   }

   protected void entityInit() {
      this.dataManager.register(TYPE, (byte)0);
   }

   public void setType(int n) {
      this.dataManager.func_187227_b(TYPE, (byte)n);
   }

   public int getType() {
      return (Byte)this.dataManager.func_187225_a(TYPE);
   }

   public AxisAlignedBB func_70114_g(Entity par1Entity) {
      return par1Entity.func_174813_aQ();
   }

   public AxisAlignedBB func_70046_E() {
      return this.func_174813_aQ();
   }

   public boolean func_70104_M() {
      return true;
   }

   public double func_70042_X() {
      return (double)this.field_70131_O * 0.0D - 0.30000001192092896D;
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      return false;
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   @SideOnly(Side.CLIENT)
   public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
      this.paraPosRotInc = posRotationIncrements + 10;
      this.paraX = x;
      this.paraY = y;
      this.paraZ = z;
      this.paraYaw = (double)yaw;
      this.paraPitch = (double)pitch;
      this.field_70159_w = this.velocityX;
      this.field_70181_x = this.velocityY;
      this.field_70179_y = this.velocityZ;
   }

   @SideOnly(Side.CLIENT)
   public void func_70016_h(double par1, double par3, double par5) {
      this.velocityX = this.field_70159_w = par1;
      this.velocityY = this.field_70181_x = par3;
      this.velocityZ = this.field_70179_y = par5;
   }

   public void func_70106_y() {
      super.func_70106_y();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.world.isRemote && this.field_70173_aa % 10 == 0) {
         MCH_Lib.DbgLog(this.world, "MCH_EntityParachute.onUpdate %d, %.3f", this.field_70173_aa, this.field_70181_x);
      }

      if (this.isOpenParachute() && this.field_70181_x > -0.3D && this.field_70173_aa > 20) {
         this.field_70143_R = (float)((double)this.field_70143_R * 0.85D);
      }

      if (!this.world.isRemote && this.user != null && this.user.func_184187_bx() == null) {
         this.user.func_184220_m(this);
         this.field_70177_z = this.field_70126_B = this.user.field_70177_z;
         this.user = null;
      }

      this.field_70169_q = this.posX;
      this.field_70167_r = this.posY;
      this.field_70166_s = this.posZ;
      double d1 = this.func_174813_aQ().field_72338_b + (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) * 0.0D / 5.0D - 0.125D;
      double d2 = this.func_174813_aQ().field_72338_b + (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) * 1.0D / 5.0D - 0.125D;
      AxisAlignedBB axisalignedbb = W_AxisAlignedBB.getAABB(this.func_174813_aQ().field_72340_a, d1, this.func_174813_aQ().field_72339_c, this.func_174813_aQ().field_72336_d, d2, this.func_174813_aQ().field_72334_f);
      if (this.world.func_72875_a(axisalignedbb, Material.field_151586_h)) {
         this.onWaterSetBoat();
         this.func_70106_y();
      }

      if (this.world.isRemote) {
         this.onUpdateClient();
      } else {
         this.onUpdateServer();
      }

   }

   public void onUpdateClient() {
      if (this.paraPosRotInc > 0) {
         double x = this.posX + (this.paraX - this.posX) / (double)this.paraPosRotInc;
         double y = this.posY + (this.paraY - this.posY) / (double)this.paraPosRotInc;
         double z = this.posZ + (this.paraZ - this.posZ) / (double)this.paraPosRotInc;
         double yaw = MathHelper.func_76138_g(this.paraYaw - (double)this.field_70177_z);
         this.field_70177_z = (float)((double)this.field_70177_z + yaw / (double)this.paraPosRotInc);
         this.field_70125_A = (float)((double)this.field_70125_A + (this.paraPitch - (double)this.field_70125_A) / (double)this.paraPosRotInc);
         --this.paraPosRotInc;
         this.func_70107_b(x, y, z);
         this.func_70101_b(this.field_70177_z, this.field_70125_A);
         if (this.getRiddenByEntity() != null) {
            this.func_70101_b(this.getRiddenByEntity().field_70126_B, this.field_70125_A);
         }
      } else {
         this.func_70107_b(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);
         if (this.field_70122_E) {
         }

         this.field_70159_w *= 0.99D;
         this.field_70181_x *= 0.95D;
         this.field_70179_y *= 0.99D;
      }

      if (!this.isOpenParachute() && this.field_70181_x > 0.01D) {
         float color = 0.6F + this.field_70146_Z.nextFloat() * 0.2F;
         double dx = this.field_70169_q - this.posX;
         double dy = this.field_70167_r - this.posY;
         double dz = this.field_70166_s - this.posZ;
         int num = 1 + (int)((double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) * 2.0D);

         for(double i = 0.0D; i < (double)num; ++i) {
            MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", this.field_70169_q + (this.posX - this.field_70169_q) * (i / (double)num) * 0.8D, this.field_70167_r + (this.posY - this.field_70167_r) * (i / (double)num) * 0.8D, this.field_70166_s + (this.posZ - this.field_70166_s) * (i / (double)num) * 0.8D);
            prm.motionX = this.field_70159_w * 0.5D + (this.field_70146_Z.nextDouble() - 0.5D) * 0.5D;
            prm.motionX = this.field_70181_x * -0.5D + (this.field_70146_Z.nextDouble() - 0.5D) * 0.5D;
            prm.motionX = this.field_70179_y * 0.5D + (this.field_70146_Z.nextDouble() - 0.5D) * 0.5D;
            prm.size = 5.0F;
            prm.setColor(0.8F + this.field_70146_Z.nextFloat(), color, color, color);
            MCH_ParticlesUtil.spawnParticle(prm);
         }
      }

   }

   public void onUpdateServer() {
      double prevSpeed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      double gravity = this.field_70122_E ? 0.01D : 0.03D;
      if (this.getType() == 2 && this.field_70173_aa < 20) {
         gravity = 0.01D;
      }

      this.field_70181_x -= gravity;
      double yaw;
      double dx;
      double dz;
      if (this.isOpenParachute()) {
         if (W_Lib.isEntityLivingBase(this.getRiddenByEntity())) {
            yaw = W_Lib.getEntityMoveDist(this.getRiddenByEntity());
            if (!this.isOpenParachute()) {
               yaw = 0.0D;
            }

            if (yaw > 0.0D) {
               dx = -Math.sin((double)(this.getRiddenByEntity().field_70177_z * 3.1415927F / 180.0F));
               dz = Math.cos((double)(this.getRiddenByEntity().field_70177_z * 3.1415927F / 180.0F));
               this.field_70159_w += dx * this.speedMultiplier * 0.05D;
               this.field_70179_y += dz * this.speedMultiplier * 0.05D;
            }
         }

         yaw = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
         if (yaw > 0.35D) {
            this.field_70159_w *= 0.35D / yaw;
            this.field_70179_y *= 0.35D / yaw;
            yaw = 0.35D;
         }

         if (yaw > prevSpeed && this.speedMultiplier < 0.35D) {
            this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
            if (this.speedMultiplier > 0.35D) {
               this.speedMultiplier = 0.35D;
            }
         } else {
            this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
            if (this.speedMultiplier < 0.07D) {
               this.speedMultiplier = 0.07D;
            }
         }
      }

      if (this.field_70122_E) {
         ++this.onGroundCount;
         if (this.onGroundCount > 5) {
            this.onGroundAndDead();
            return;
         }
      }

      this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
      if (this.getType() == 2 && this.field_70173_aa < 20) {
         this.field_70181_x *= 0.95D;
      } else {
         this.field_70181_x *= 0.9D;
      }

      if (this.isOpenParachute()) {
         this.field_70159_w *= 0.95D;
         this.field_70179_y *= 0.95D;
      } else {
         this.field_70159_w *= 0.99D;
         this.field_70179_y *= 0.99D;
      }

      this.field_70125_A = 0.0F;
      yaw = (double)this.field_70177_z;
      dx = this.field_70169_q - this.posX;
      dz = this.field_70166_s - this.posZ;
      if (dx * dx + dz * dz > 0.001D) {
         yaw = (double)((float)(Math.atan2(dx, dz) * 180.0D / 3.141592653589793D));
      }

      double yawDiff = MathHelper.func_76138_g(yaw - (double)this.field_70177_z);
      if (yawDiff > 20.0D) {
         yawDiff = 20.0D;
      }

      if (yawDiff < -20.0D) {
         yawDiff = -20.0D;
      }

      if (this.getRiddenByEntity() != null) {
         this.func_70101_b(this.getRiddenByEntity().field_70177_z, this.field_70125_A);
      } else {
         this.field_70177_z = (float)((double)this.field_70177_z + yawDiff);
         this.func_70101_b(this.field_70177_z, this.field_70125_A);
      }

      List<Entity> list = this.world.func_72839_b(this, this.func_174813_aQ().func_72314_b(0.2D, 0.0D, 0.2D));
      if (list != null && !list.isEmpty()) {
         for(int l = 0; l < list.size(); ++l) {
            Entity entity = (Entity)list.get(l);
            if (entity != this.getRiddenByEntity() && entity.func_70104_M() && entity instanceof MCH_EntityParachute) {
               entity.func_70108_f(this);
            }
         }
      }

      if (this.getRiddenByEntity() != null && this.getRiddenByEntity().field_70128_L) {
         this.func_70106_y();
      }

   }

   public void onGroundAndDead() {
      ++this.posY;
      this.func_184232_k(this.getRiddenByEntity());
      this.func_70106_y();
   }

   public void onWaterSetBoat() {
      if (!this.world.isRemote) {
         if (this.getType() == 2) {
            if (this.getRiddenByEntity() != null) {
               int px = (int)(this.posX + 0.5D);
               int py = (int)(this.posY + 0.5D);
               int pz = (int)(this.posZ + 0.5D);
               boolean foundBlock = false;

               int countWater;
               for(countWater = 0; countWater < 5 && py + countWater >= 0 && py + countWater <= 255; ++countWater) {
                  Block block = W_WorldFunc.getBlock(this.world, px, py - countWater, pz);
                  if (block == W_Block.getWater()) {
                     py -= countWater;
                     foundBlock = true;
                     break;
                  }
               }

               if (foundBlock) {
                  countWater = 0;

                  for(int y = 0; y < 3 && py + y >= 0 && py + y <= 255; ++y) {
                     for(int x = -2; x <= 2; ++x) {
                        for(int z = -2; z <= 2; ++z) {
                           Block block = W_WorldFunc.getBlock(this.world, px + x, py - y, pz + z);
                           if (block == W_Block.getWater()) {
                              ++countWater;
                              if (countWater > 37) {
                                 break;
                              }
                           }
                        }
                     }
                  }

                  if (countWater > 37) {
                     EntityBoat entityboat = new EntityBoat(this.world, (double)px, (double)((float)py + 1.0F), (double)pz);
                     entityboat.field_70177_z = this.field_70177_z - 90.0F;
                     this.world.func_72838_d(entityboat);
                     this.getRiddenByEntity().func_184220_m(entityboat);
                  }

               }
            }
         }
      }
   }

   public boolean isOpenParachute() {
      return this.getType() != 2 || this.field_70181_x < -0.1D;
   }

   public void func_184232_k(Entity passenger) {
      if (this.func_184196_w(passenger)) {
         double x = -Math.sin((double)this.field_70177_z * 3.141592653589793D / 180.0D) * 0.1D;
         double z = Math.cos((double)this.field_70177_z * 3.141592653589793D / 180.0D) * 0.1D;
         passenger.func_70107_b(this.posX + x, this.posY + this.func_70042_X() + passenger.func_70033_W(), this.posZ + z);
      }

   }

   protected void func_70014_b(NBTTagCompound nbt) {
      nbt.func_74774_a("ParachuteModelType", (byte)this.getType());
   }

   protected void func_70037_a(NBTTagCompound nbt) {
      this.setType(nbt.func_74771_c("ParachuteModelType"));
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 4.0F;
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      return false;
   }

   @Nullable
   public Entity getRiddenByEntity() {
      List<Entity> passengers = this.func_184188_bt();
      return passengers.isEmpty() ? null : (Entity)passengers.get(0);
   }

   static {
      TYPE = EntityDataManager.createKey(MCH_EntityParachute.class, DataSerializers.field_187191_a);
   }
}
