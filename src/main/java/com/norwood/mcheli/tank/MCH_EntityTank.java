package com.norwood.mcheli.tank;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.MCH_Math;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_BoundingBox;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntityHitBox;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_PacketStatusRequest;
import com.norwood.mcheli.aircraft.MCH_Parts;
import com.norwood.mcheli.chain.MCH_EntityChain;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.weapon.MCH_EntityBaseBullet;
import com.norwood.mcheli.weapon.MCH_WeaponSet;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Blocks;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityTank extends MCH_EntityAircraft {
   private MCH_TankInfo tankInfo = null;
   public float soundVolume;
   public float soundVolumeTarget;
   public float rotationRotor;
   public float prevRotationRotor;
   public float addkeyRotValue;
   public final MCH_WheelManager WheelMng;

   public MCH_EntityTank(World world) {
      super(world);
      this.currentSpeed = 0.07D;
      this.field_70156_m = true;
      this.func_70105_a(2.0F, 0.7F);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.weapons = this.createWeapon(0);
      this.soundVolume = 0.0F;
      this.field_70138_W = 0.6F;
      this.rotationRotor = 0.0F;
      this.prevRotationRotor = 0.0F;
      this.WheelMng = new MCH_WheelManager(this);
   }

   public String getKindName() {
      return "tanks";
   }

   public String getEntityType() {
      return "Vehicle";
   }

   @Nullable
   public MCH_TankInfo getTankInfo() {
      return this.tankInfo;
   }

   public void changeType(String type) {
      MCH_Lib.DbgLog(this.world, "MCH_EntityTank.changeType " + type + " : " + this.toString());
      if (!type.isEmpty()) {
         this.tankInfo = MCH_TankInfoManager.get(type);
      }

      if (this.tankInfo == null) {
         MCH_Lib.Log((Entity)this, "##### MCH_EntityTank changeTankType() Tank info null %d, %s, %s", W_Entity.getEntityId(this), type, this.getEntityName());
         this.func_70106_y();
      } else {
         this.setAcInfo(this.tankInfo);
         this.newSeats(this.getAcInfo().getNumSeatAndRack());
         this.switchFreeLookModeClient(this.getAcInfo().defaultFreelook);
         this.weapons = this.createWeapon(1 + this.getSeatNum());
         this.initPartRotation(this.getRotYaw(), this.getRotPitch());
         this.WheelMng.createWheels(this.world, this.getAcInfo().wheels, new Vec3d(0.0D, -0.35D, (double)this.getTankInfo().weightedCenterZ));
      }

   }

   @Nullable
   public Item getItem() {
      return this.getTankInfo() != null ? this.getTankInfo().item : null;
   }

   public boolean canMountWithNearEmptyMinecart() {
      return MCH_Config.MountMinecartTank.prmBool;
   }

   protected void entityInit() {
      super.entityInit();
   }

   public float getGiveDamageRot() {
      return 91.0F;
   }

   protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
   }

   protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
      if (this.tankInfo == null) {
         this.tankInfo = MCH_TankInfoManager.get(this.getTypeName());
         if (this.tankInfo == null) {
            MCH_Lib.Log((Entity)this, "##### MCH_EntityTank readEntityFromNBT() Tank info null %d, %s", W_Entity.getEntityId(this), this.getEntityName());
            this.func_70106_y();
         } else {
            this.setAcInfo(this.tankInfo);
         }
      }

   }

   public void func_70106_y() {
      super.func_70106_y();
   }

   public void onInteractFirst(EntityPlayer player) {
      this.addkeyRotValue = 0.0F;
      player.field_70759_as = player.field_70758_at = this.getLastRiderYaw();
      player.field_70126_B = player.field_70177_z = this.getLastRiderYaw();
      player.field_70125_A = this.getLastRiderPitch();
   }

   public boolean canSwitchGunnerMode() {
      return !super.canSwitchGunnerMode() ? false : false;
   }

   public void onUpdateAircraft() {
      if (this.tankInfo == null) {
         this.changeType(this.getTypeName());
         this.field_70169_q = this.posX;
         this.field_70167_r = this.posY;
         this.field_70166_s = this.posZ;
      } else {
         if (!this.isRequestedSyncStatus) {
            this.isRequestedSyncStatus = true;
            if (this.world.isRemote) {
               MCH_PacketStatusRequest.requestStatus(this);
            }
         }

         if (this.lastRiddenByEntity == null && this.getRiddenByEntity() != null) {
            this.initCurrentWeapon(this.getRiddenByEntity());
         }

         this.updateWeapons();
         this.onUpdate_Seats();
         this.onUpdate_Control();
         this.prevRotationRotor = this.rotationRotor;
         this.rotationRotor = (float)((double)this.rotationRotor + this.getCurrentThrottle() * (double)this.getAcInfo().rotorSpeed);
         if (this.rotationRotor > 360.0F) {
            this.rotationRotor -= 360.0F;
            this.prevRotationRotor -= 360.0F;
         }

         if (this.rotationRotor < 0.0F) {
            this.rotationRotor += 360.0F;
            this.prevRotationRotor += 360.0F;
         }

         this.field_70169_q = this.posX;
         this.field_70167_r = this.posY;
         this.field_70166_s = this.posZ;
         if (this.isDestroyed() && this.getCurrentThrottle() > 0.0D) {
            if (MCH_Lib.getBlockIdY(this, 3, -2) > 0) {
               this.setCurrentThrottle(this.getCurrentThrottle() * 0.8D);
            }

            if (this.isExploded()) {
               this.setCurrentThrottle(this.getCurrentThrottle() * 0.98D);
            }
         }

         this.updateCameraViewers();
         if (this.world.isRemote) {
            this.onUpdate_Client();
         } else {
            this.onUpdate_Server();
         }

      }
   }

   @SideOnly(Side.CLIENT)
   public boolean func_90999_ad() {
      return this.isDestroyed() || super.func_90999_ad();
   }

   public void updateExtraBoundingBox() {
      if (this.world.isRemote) {
         super.updateExtraBoundingBox();
      } else if (this.getCountOnUpdate() <= 1) {
         super.updateExtraBoundingBox();
         super.updateExtraBoundingBox();
      }

   }

   public MCH_EntityTank.ClacAxisBB calculateXOffset(List<AxisAlignedBB> list, AxisAlignedBB bb, double x) {
      for(int j5 = 0; j5 < list.size(); ++j5) {
         x = ((AxisAlignedBB)list.get(j5)).func_72316_a(bb, x);
      }

      return new MCH_EntityTank.ClacAxisBB(x, bb.func_72317_d(x, 0.0D, 0.0D));
   }

   public MCH_EntityTank.ClacAxisBB calculateYOffset(List<AxisAlignedBB> list, AxisAlignedBB bb, double y) {
      return this.calculateYOffset(list, bb, bb, y);
   }

   public MCH_EntityTank.ClacAxisBB calculateYOffset(List<AxisAlignedBB> list, AxisAlignedBB calcBB, AxisAlignedBB offsetBB, double y) {
      for(int k = 0; k < list.size(); ++k) {
         y = ((AxisAlignedBB)list.get(k)).func_72323_b(calcBB, y);
      }

      return new MCH_EntityTank.ClacAxisBB(y, offsetBB.func_72317_d(0.0D, y, 0.0D));
   }

   public MCH_EntityTank.ClacAxisBB calculateZOffset(List<AxisAlignedBB> list, AxisAlignedBB bb, double z) {
      for(int k5 = 0; k5 < list.size(); ++k5) {
         z = ((AxisAlignedBB)list.get(k5)).func_72322_c(bb, z);
      }

      return new MCH_EntityTank.ClacAxisBB(z, bb.func_72317_d(0.0D, 0.0D, z));
   }

   public void func_70091_d(MoverType type, double x, double y, double z) {
      this.world.field_72984_F.func_76320_a("move");
      double d2 = x;
      double d3 = y;
      double d4 = z;
      List<AxisAlignedBB> list1 = getCollisionBoxes(this, this.func_174813_aQ().func_72321_a(x, y, z));
      AxisAlignedBB axisalignedbb = this.func_174813_aQ();
      MCH_EntityTank.ClacAxisBB v;
      if (y != 0.0D) {
         v = this.calculateYOffset(list1, this.func_174813_aQ(), y);
         y = v.value;
         this.func_174826_a(v.bb);
      }

      boolean flag = this.field_70122_E || y != y && y < 0.0D;
      MCH_BoundingBox[] var18 = this.extraBoundingBox;
      int i1 = var18.length;

      int k6;
      for(k6 = 0; k6 < i1; ++k6) {
         MCH_BoundingBox ebb = var18[k6];
         ebb.updatePosition(this.posX, this.posY, this.posZ, this.getRotYaw(), this.getRotPitch(), this.getRotRoll());
      }

      if (x != 0.0D) {
         v = this.calculateXOffset(list1, this.func_174813_aQ(), x);
         x = v.value;
         if (x != 0.0D) {
            this.func_174826_a(v.bb);
         }
      }

      if (z != 0.0D) {
         v = this.calculateZOffset(list1, this.func_174813_aQ(), z);
         z = v.value;
         if (z != 0.0D) {
            this.func_174826_a(v.bb);
         }
      }

      if (this.field_70138_W > 0.0F && flag && (x != x || z != z)) {
         double d14 = x;
         double d6 = y;
         double d7 = z;
         AxisAlignedBB axisalignedbb1 = this.func_174813_aQ();
         this.func_174826_a(axisalignedbb);
         y = (double)this.field_70138_W;
         List<AxisAlignedBB> list = getCollisionBoxes(this, this.func_174813_aQ().func_72321_a(d2, y, d4));
         AxisAlignedBB axisalignedbb2 = this.func_174813_aQ();
         AxisAlignedBB axisalignedbb3 = axisalignedbb2.func_72321_a(d2, 0.0D, d4);
         v = this.calculateYOffset(list, axisalignedbb3, axisalignedbb2, y);
         double d8 = v.value;
         axisalignedbb2 = v.bb;
         v = this.calculateXOffset(list, axisalignedbb2, d2);
         double d18 = v.value;
         axisalignedbb2 = v.bb;
         v = this.calculateZOffset(list, axisalignedbb2, d4);
         double d19 = v.value;
         axisalignedbb2 = v.bb;
         AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
         v = this.calculateYOffset(list, axisalignedbb4, y);
         double d20 = v.value;
         axisalignedbb4 = v.bb;
         v = this.calculateXOffset(list, axisalignedbb4, d2);
         double d21 = v.value;
         axisalignedbb4 = v.bb;
         v = this.calculateZOffset(list, axisalignedbb4, d4);
         double d22 = v.value;
         axisalignedbb4 = v.bb;
         double d23 = d18 * d18 + d19 * d19;
         double d9 = d21 * d21 + d22 * d22;
         if (d23 > d9) {
            x = d18;
            z = d19;
            y = -d8;
            this.func_174826_a(axisalignedbb2);
         } else {
            x = d21;
            z = d22;
            y = -d20;
            this.func_174826_a(axisalignedbb4);
         }

         v = this.calculateYOffset(list, this.func_174813_aQ(), y);
         y = v.value;
         this.func_174826_a(v.bb);
         if (d14 * d14 + d7 * d7 >= x * x + z * z) {
            x = d14;
            y = d6;
            z = d7;
            this.func_174826_a(axisalignedbb1);
         }
      }

      this.world.field_72984_F.func_76319_b();
      this.world.field_72984_F.func_76320_a("rest");
      this.func_174829_m();
      this.field_70123_F = x != x || z != z;
      this.field_70124_G = y != y;
      this.field_70122_E = this.field_70124_G && d3 < 0.0D;
      this.field_70132_H = this.field_70123_F || this.field_70124_G;
      int j6 = MathHelper.func_76128_c(this.posX);
      i1 = MathHelper.func_76128_c(this.posY - 0.20000000298023224D);
      k6 = MathHelper.func_76128_c(this.posZ);
      BlockPos blockpos = new BlockPos(j6, i1, k6);
      IBlockState iblockstate = this.world.func_180495_p(blockpos);
      if (iblockstate.func_185904_a() == Material.field_151579_a) {
         BlockPos blockpos1 = blockpos.func_177977_b();
         IBlockState iblockstate1 = this.world.func_180495_p(blockpos1);
         Block block1 = iblockstate1.func_177230_c();
         if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate) {
            iblockstate = iblockstate1;
            blockpos = blockpos1;
         }
      }

      this.func_184231_a(y, this.field_70122_E, iblockstate, blockpos);
      if (d2 != x) {
         this.field_70159_w = 0.0D;
      }

      if (z != z) {
         this.field_70179_y = 0.0D;
      }

      Block block = iblockstate.func_177230_c();
      if (d3 != y) {
         block.func_176216_a(this.world, this);
      }

      try {
         this.func_145775_I();
      } catch (Throwable var45) {
         CrashReport crashreport = CrashReport.func_85055_a(var45, "Checking entity block collision");
         CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
         this.func_85029_a(crashreportcategory);
         throw new ReportedException(crashreport);
      }

      this.world.field_72984_F.func_76319_b();
   }

   private void rotationByKey(float partialTicks) {
      float rot = 0.2F;
      if (this.moveLeft && !this.moveRight) {
         this.addkeyRotValue -= rot * partialTicks;
      }

      if (this.moveRight && !this.moveLeft) {
         this.addkeyRotValue += rot * partialTicks;
      }

   }

   public void onUpdateAngles(float partialTicks) {
      if (!this.isDestroyed()) {
         if (this.isGunnerMode) {
            this.setRotPitch(this.getRotPitch() * 0.95F);
            this.setRotYaw(this.getRotYaw() + this.getAcInfo().autoPilotRot * 0.2F);
            if (MathHelper.func_76135_e(this.getRotRoll()) > 20.0F) {
               this.setRotRoll(this.getRotRoll() * 0.95F);
            }
         }

         this.updateRecoil(partialTicks);
         this.setRotPitch(this.getRotPitch() + (this.WheelMng.targetPitch - this.getRotPitch()) * partialTicks);
         this.setRotRoll(this.getRotRoll() + (this.WheelMng.targetRoll - this.getRotRoll()) * partialTicks);
         boolean isFly = MCH_Lib.getBlockIdY(this, 3, -3) == 0;
         if (!isFly || this.getAcInfo().isFloat && this.getWaterDepth() > 0.0D) {
            float gmy = 1.0F;
            if (!isFly) {
               gmy = this.getAcInfo().mobilityYawOnGround;
               if (!this.getAcInfo().canRotOnGround) {
                  Block block = MCH_Lib.getBlockY(this, 3, -2, false);
                  if (!W_Block.isEqual(block, W_Block.getWater()) && !W_Block.isEqual(block, W_Blocks.field_150350_a)) {
                     gmy = 0.0F;
                  }
               }
            }

            float pivotTurnThrottle = this.getAcInfo().pivotTurnThrottle;
            double dx = this.posX - this.field_70169_q;
            double dz = this.posZ - this.field_70166_s;
            double dist = dx * dx + dz * dz;
            if (pivotTurnThrottle <= 0.0F || this.getCurrentThrottle() >= (double)pivotTurnThrottle || this.throttleBack >= pivotTurnThrottle / 10.0F || dist > (double)this.throttleBack * 0.01D) {
               float sf = (float)Math.sqrt(dist <= 1.0D ? dist : 1.0D);
               if (pivotTurnThrottle <= 0.0F) {
                  sf = 1.0F;
               }

               float flag = !this.throttleUp && this.throttleDown && this.getCurrentThrottle() < (double)pivotTurnThrottle + 0.05D ? -1.0F : 1.0F;
               if (this.moveLeft && !this.moveRight) {
                  this.setRotYaw(this.getRotYaw() - 0.6F * gmy * partialTicks * flag * sf);
               }

               if (this.moveRight && !this.moveLeft) {
                  this.setRotYaw(this.getRotYaw() + 0.6F * gmy * partialTicks * flag * sf);
               }
            }
         }

         this.addkeyRotValue = (float)((double)this.addkeyRotValue * (1.0D - (double)(0.1F * partialTicks)));
      }
   }

   protected void onUpdate_Control() {
      if (this.isGunnerMode && !this.canUseFuel()) {
         this.switchGunnerMode(false);
      }

      this.throttleBack = (float)((double)this.throttleBack * 0.8D);
      if (this.getBrake()) {
         this.throttleBack = (float)((double)this.throttleBack * 0.5D);
         if (this.getCurrentThrottle() > 0.0D) {
            this.addCurrentThrottle(-0.02D * (double)this.getAcInfo().throttleUpDown);
         } else {
            this.setCurrentThrottle(0.0D);
         }
      }

      if (this.getRiddenByEntity() != null && !this.getRiddenByEntity().field_70128_L && this.isCanopyClose() && this.canUseFuel() && !this.isDestroyed()) {
         this.onUpdate_ControlSub();
      } else if (this.isTargetDrone() && this.canUseFuel() && !this.isDestroyed()) {
         this.throttleUp = true;
         this.onUpdate_ControlSub();
      } else if (this.getCurrentThrottle() > 0.0D) {
         this.addCurrentThrottle(-0.0025D * (double)this.getAcInfo().throttleUpDown);
      } else {
         this.setCurrentThrottle(0.0D);
      }

      if (this.getCurrentThrottle() < 0.0D) {
         this.setCurrentThrottle(0.0D);
      }

      if (this.world.isRemote) {
         if (!W_Lib.isClientPlayer(this.getRiddenByEntity()) || this.getCountOnUpdate() % 200 == 0) {
            double ct = this.getThrottle();
            if (this.getCurrentThrottle() > ct) {
               this.addCurrentThrottle(-0.005D);
            }

            if (this.getCurrentThrottle() < ct) {
               this.addCurrentThrottle(0.005D);
            }
         }
      } else {
         this.setThrottle(this.getCurrentThrottle());
      }

   }

   protected void onUpdate_ControlSub() {
      if (!this.isGunnerMode) {
         float throttleUpDown = this.getAcInfo().throttleUpDown;
         if (this.throttleUp) {
            float f = throttleUpDown;
            if (this.func_184187_bx() != null) {
               double mx = this.func_184187_bx().field_70159_w;
               double mz = this.func_184187_bx().field_70179_y;
               f = throttleUpDown * MathHelper.func_76133_a(mx * mx + mz * mz) * this.getAcInfo().throttleUpDownOnEntity;
            }

            if (this.getAcInfo().enableBack && this.throttleBack > 0.0F) {
               this.throttleBack = (float)((double)this.throttleBack - 0.01D * (double)f);
            } else {
               this.throttleBack = 0.0F;
               if (this.getCurrentThrottle() < 1.0D) {
                  this.addCurrentThrottle(0.01D * (double)f);
               } else {
                  this.setCurrentThrottle(1.0D);
               }
            }
         } else if (this.throttleDown) {
            if (this.getCurrentThrottle() > 0.0D) {
               this.addCurrentThrottle(-0.01D * (double)throttleUpDown);
            } else {
               this.setCurrentThrottle(0.0D);
               if (this.getAcInfo().enableBack) {
                  this.throttleBack = (float)((double)this.throttleBack + 0.0025D * (double)throttleUpDown);
                  if (this.throttleBack > 0.6F) {
                     this.throttleBack = 0.6F;
                  }
               }
            }
         } else if (this.cs_tankAutoThrottleDown && this.getCurrentThrottle() > 0.0D) {
            this.addCurrentThrottle(-0.005D * (double)throttleUpDown);
            if (this.getCurrentThrottle() <= 0.0D) {
               this.setCurrentThrottle(0.0D);
            }
         }
      }

   }

   protected void onUpdate_Particle2() {
      if (this.world.isRemote) {
         if (!((double)this.getHP() >= (double)this.getMaxHP() * 0.5D)) {
            if (this.getTankInfo() != null) {
               int bbNum = this.getTankInfo().extraBoundingBox.size();
               if (bbNum < 0) {
                  bbNum = 0;
               }

               if (this.isFirstDamageSmoke || this.prevDamageSmokePos.length != bbNum + 1) {
                  this.prevDamageSmokePos = new Vec3d[bbNum + 1];
               }

               float yaw = this.getRotYaw();
               float pitch = this.getRotPitch();
               float roll = this.getRotRoll();

               double py;
               double pz;
               int d;
               for(int ri = 0; ri < bbNum; ++ri) {
                  if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2D && this.getMaxHP() > 0) {
                     d = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2D) / 0.3D * 15.0D);
                     if (d > 0 && this.field_70146_Z.nextInt(d) > 0) {
                     }
                  } else {
                     MCH_BoundingBox bb = (MCH_BoundingBox)this.getTankInfo().extraBoundingBox.get(ri);
                     Vec3d pos = this.getTransformedPosition(bb.offsetX, bb.offsetY, bb.offsetZ);
                     py = pos.x;
                     pz = pos.y;
                     double z = pos.z;
                     this.onUpdate_Particle2SpawnSmoke(ri, py, pz, z, 1.0F);
                  }
               }

               boolean b = true;
               if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2D && this.getMaxHP() > 0) {
                  d = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2D) / 0.3D * 15.0D);
                  if (d > 0 && this.field_70146_Z.nextInt(d) > 0) {
                     b = false;
                  }
               }

               if (b) {
                  double px = this.posX;
                  py = this.posY;
                  pz = this.posZ;
                  if (this.getSeatInfo(0) != null && this.getSeatInfo(0).pos != null) {
                     Vec3d pos = MCH_Lib.RotVec3(0.0D, this.getSeatInfo(0).pos.y, -2.0D, -yaw, -pitch, -roll);
                     px += pos.x;
                     py += pos.y;
                     pz += pos.z;
                  }

                  this.onUpdate_Particle2SpawnSmoke(bbNum, px, py, pz, bbNum == 0 ? 2.0F : 1.0F);
               }

               this.isFirstDamageSmoke = false;
            }
         }
      }
   }

   public void onUpdate_Particle2SpawnSmoke(int ri, double x, double y, double z, float size) {
      if (this.isFirstDamageSmoke || this.prevDamageSmokePos[ri] == null) {
         this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
      }

      int num = 1;

      for(int i = 0; i < num; ++i) {
         float c = 0.2F + this.field_70146_Z.nextFloat() * 0.3F;
         MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", x, y, z);
         prm.motionX = (double)size * (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D;
         prm.motionY = (double)size * this.field_70146_Z.nextDouble() * 0.1D;
         prm.motionZ = (double)size * (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D;
         prm.size = size * ((float)this.field_70146_Z.nextInt(5) + 5.0F) * 1.0F;
         prm.setColor(0.7F + this.field_70146_Z.nextFloat() * 0.1F, c, c, c);
         MCH_ParticlesUtil.spawnParticle(prm);
      }

      this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
   }

   public void onUpdate_Particle2SpawnSmode(int ri, double x, double y, double z, float size) {
      if (this.isFirstDamageSmoke) {
         this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
      }

      Vec3d prev = this.prevDamageSmokePos[ri];
      double dx = x - prev.x;
      double dy = y - prev.y;
      double dz = z - prev.z;
      int num = (int)((double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) / 0.3D) + 1;

      for(int i = 0; i < num; ++i) {
         float c = 0.2F + this.field_70146_Z.nextFloat() * 0.3F;
         MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", x, y, z);
         prm.motionX = (double)size * (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D;
         prm.motionY = (double)size * this.field_70146_Z.nextDouble() * 0.1D;
         prm.motionZ = (double)size * (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D;
         prm.size = size * ((float)this.field_70146_Z.nextInt(5) + 5.0F) * 1.0F;
         prm.setColor(0.7F + this.field_70146_Z.nextFloat() * 0.1F, c, c, c);
         MCH_ParticlesUtil.spawnParticle(prm);
      }

      this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
   }

   public void onUpdate_ParticleLandingGear() {
      this.WheelMng.particleLandingGear();
   }

   private void onUpdate_ParticleSplash() {
      if (this.getAcInfo() != null) {
         if (this.world.isRemote) {
            double mx = this.posX - this.field_70169_q;
            double mz = this.posZ - this.field_70166_s;
            double dist = mx * mx + mz * mz;
            if (dist > 1.0D) {
               dist = 1.0D;
            }

            Iterator var7 = this.getAcInfo().particleSplashs.iterator();

            while(var7.hasNext()) {
               MCH_AircraftInfo.ParticleSplash p = (MCH_AircraftInfo.ParticleSplash)var7.next();

               for(int i = 0; i < p.num; ++i) {
                  if (dist > 0.03D + (double)this.field_70146_Z.nextFloat() * 0.1D) {
                     this.setParticleSplash(p.pos, -mx * (double)p.acceleration, (double)p.motionY, -mz * (double)p.acceleration, p.gravity, (double)p.size * (0.5D + dist * 0.5D), p.age);
                  }
               }
            }

         }
      }
   }

   private void setParticleSplash(Vec3d pos, double mx, double my, double mz, float gravity, double size, int age) {
      Vec3d v = this.getTransformedPosition(pos);
      v = v.func_72441_c(this.field_70146_Z.nextDouble() - 0.5D, (this.field_70146_Z.nextDouble() - 0.5D) * 0.5D, this.field_70146_Z.nextDouble() - 0.5D);
      int x = (int)(v.x + 0.5D);
      int y = (int)(v.y + 0.0D);
      int z = (int)(v.z + 0.5D);
      if (W_WorldFunc.isBlockWater(this.world, x, y, z)) {
         float c = this.field_70146_Z.nextFloat() * 0.3F + 0.7F;
         MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", v.x, v.y, v.z);
         prm.motionX = mx + ((double)this.field_70146_Z.nextFloat() - 0.5D) * 0.7D;
         prm.motionY = my;
         prm.motionZ = mz + ((double)this.field_70146_Z.nextFloat() - 0.5D) * 0.7D;
         prm.size = (float)size * (this.field_70146_Z.nextFloat() * 0.2F + 0.8F);
         prm.setColor(0.9F, c, c, c);
         prm.age = age + (int)((double)this.field_70146_Z.nextFloat() * 0.5D * (double)age);
         prm.gravity = gravity;
         MCH_ParticlesUtil.spawnParticle(prm);
      }

   }

   public void destroyAircraft() {
      super.destroyAircraft();
      this.rotDestroyedPitch = 0.0F;
      this.rotDestroyedRoll = 0.0F;
      this.rotDestroyedYaw = 0.0F;
   }

   public int getClientPositionDelayCorrection() {
      return this.getTankInfo().weightType == 1 ? 2 : (this.getTankInfo() == null ? 7 : 7);
   }

   protected void onUpdate_Client() {
      if (this.getRiddenByEntity() != null && W_Lib.isClientPlayer(this.getRiddenByEntity())) {
         this.getRiddenByEntity().field_70125_A = this.getRiddenByEntity().field_70127_C;
      }

      if (this.aircraftPosRotInc > 0) {
         this.applyServerPositionAndRotation();
      } else {
         this.func_70107_b(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);
         if (!this.isDestroyed() && (this.field_70122_E || MCH_Lib.getBlockIdY(this, 1, -2) > 0)) {
            this.field_70159_w *= 0.95D;
            this.field_70179_y *= 0.95D;
            this.applyOnGroundPitch(0.95F);
         }

         if (this.func_70090_H()) {
            this.field_70159_w *= 0.99D;
            this.field_70179_y *= 0.99D;
         }
      }

      this.updateWheels();
      this.onUpdate_Particle2();
      this.updateSound();
      if (this.world.isRemote) {
         this.onUpdate_ParticleLandingGear();
         this.onUpdate_ParticleSplash();
         this.onUpdate_ParticleSandCloud(true);
      }

      this.updateCamera(this.posX, this.posY, this.posZ);
   }

   public void applyOnGroundPitch(float factor) {
   }

   private void onUpdate_Server() {
      double prevMotion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      double dp = 0.0D;
      if (this.canFloatWater()) {
         dp = this.getWaterDepth();
      }

      boolean levelOff = this.isGunnerMode;
      if (dp == 0.0D) {
         if (!levelOff) {
            this.field_70181_x += 0.04D + (double)(!this.func_70090_H() ? this.getAcInfo().gravity : this.getAcInfo().gravityInWater);
            this.field_70181_x += -0.047D * (1.0D - this.getCurrentThrottle());
         } else {
            this.field_70181_x *= 0.8D;
         }
      } else if (!(MathHelper.func_76135_e(this.getRotRoll()) >= 40.0F) && !(dp < 1.0D)) {
         if (this.field_70181_x < 0.0D) {
            this.field_70181_x /= 2.0D;
         }

         this.field_70181_x += 0.007D;
      } else {
         this.field_70181_x -= 1.0E-4D;
         this.field_70181_x += 0.007D * this.getCurrentThrottle();
      }

      float throttle = (float)(this.getCurrentThrottle() / 10.0D);
      Vec3d v = MCH_Lib.Rot2Vec3(this.getRotYaw(), this.getRotPitch() - 10.0F);
      if (!levelOff) {
         this.field_70181_x += v.y * (double)throttle / 8.0D;
      }

      boolean canMove = true;
      if (!this.getAcInfo().canMoveOnGround) {
         Block block = MCH_Lib.getBlockY(this, 3, -2, false);
         if (!W_Block.isEqual(block, W_Block.getWater()) && !W_Block.isEqual(block, W_Blocks.field_150350_a)) {
            canMove = false;
         }
      }

      if (canMove) {
         if (this.getAcInfo().enableBack && this.throttleBack > 0.0F) {
            this.field_70159_w -= v.x * (double)this.throttleBack;
            this.field_70179_y -= v.z * (double)this.throttleBack;
         } else {
            this.field_70159_w += v.x * (double)throttle;
            this.field_70179_y += v.z * (double)throttle;
         }
      }

      double motion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      float speedLimit = this.getMaxSpeed();
      if (motion > (double)speedLimit) {
         this.field_70159_w *= (double)speedLimit / motion;
         this.field_70179_y *= (double)speedLimit / motion;
         motion = (double)speedLimit;
      }

      if (motion > prevMotion && this.currentSpeed < (double)speedLimit) {
         this.currentSpeed += ((double)speedLimit - this.currentSpeed) / 35.0D;
         if (this.currentSpeed > (double)speedLimit) {
            this.currentSpeed = (double)speedLimit;
         }
      } else {
         this.currentSpeed -= (this.currentSpeed - 0.07D) / 35.0D;
         if (this.currentSpeed < 0.07D) {
            this.currentSpeed = 0.07D;
         }
      }

      if (this.field_70122_E || MCH_Lib.getBlockIdY(this, 1, -2) > 0) {
         this.field_70159_w *= (double)this.getAcInfo().motionFactor;
         this.field_70179_y *= (double)this.getAcInfo().motionFactor;
         if (MathHelper.func_76135_e(this.getRotPitch()) < 40.0F) {
            this.applyOnGroundPitch(0.8F);
         }
      }

      this.updateWheels();
      this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
      this.field_70181_x *= 0.95D;
      this.field_70159_w *= (double)this.getAcInfo().motionFactor;
      this.field_70179_y *= (double)this.getAcInfo().motionFactor;
      this.func_70101_b(this.getRotYaw(), this.getRotPitch());
      this.onUpdate_updateBlock();
      this.updateCollisionBox();
      if (this.getRiddenByEntity() != null && this.getRiddenByEntity().field_70128_L) {
         this.unmountEntity();
      }

   }

   private void collisionEntity(AxisAlignedBB bb) {
      if (bb != null) {
         double speed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
         if (!(speed <= 0.05D)) {
            Entity rider = this.getRiddenByEntity();
            float damage = (float)(speed * 15.0D);
            MCH_EntityAircraft rideAc = this.func_184187_bx() instanceof MCH_EntitySeat ? ((MCH_EntitySeat)this.func_184187_bx()).getParent() : (this.func_184187_bx() instanceof MCH_EntityAircraft ? (MCH_EntityAircraft)this.func_184187_bx() : null);
            List<Entity> list = this.world.func_175674_a(this, bb.func_72314_b(0.3D, 0.3D, 0.3D), (ex) -> {
               if (ex != rideAc && !(ex instanceof EntityItem) && !(ex instanceof EntityXPOrb) && !(ex instanceof MCH_EntityBaseBullet) && !(ex instanceof MCH_EntityChain) && !(ex instanceof MCH_EntitySeat)) {
                  if (ex instanceof MCH_EntityTank) {
                     MCH_EntityTank tank = (MCH_EntityTank)ex;
                     if (tank.getTankInfo() != null && tank.getTankInfo().weightType == 2) {
                        return MCH_Config.Collision_EntityTankDamage.prmBool;
                     }
                  }

                  return MCH_Config.Collision_EntityDamage.prmBool;
               } else {
                  return false;
               }
            });

            for(int i = 0; i < list.size(); ++i) {
               Entity e = (Entity)list.get(i);
               if (this.shouldCollisionDamage(e)) {
                  double dx = e.posX - this.posX;
                  double dz = e.posZ - this.posZ;
                  double dist = Math.sqrt(dx * dx + dz * dz);
                  if (dist > 5.0D) {
                     dist = 5.0D;
                  }

                  damage = (float)((double)damage + (5.0D - dist));
                  DamageSource ds;
                  if (rider instanceof EntityLivingBase) {
                     ds = DamageSource.func_76358_a((EntityLivingBase)rider);
                  } else {
                     ds = DamageSource.field_76377_j;
                  }

                  MCH_Lib.applyEntityHurtResistantTimeConfig(e);
                  e.func_70097_a(ds, damage);
                  if (e instanceof MCH_EntityAircraft) {
                     e.field_70159_w += this.field_70159_w * 0.05D;
                     e.field_70179_y += this.field_70179_y * 0.05D;
                  } else if (e instanceof EntityArrow) {
                     e.func_70106_y();
                  } else {
                     e.field_70159_w += this.field_70159_w * 1.5D;
                     e.field_70179_y += this.field_70179_y * 1.5D;
                  }

                  if (this.getTankInfo().weightType != 2 && (e.field_70130_N >= 1.0F || (double)e.field_70131_O >= 1.5D)) {
                     if (e instanceof EntityLivingBase) {
                        ds = DamageSource.func_76358_a((EntityLivingBase)e);
                     } else {
                        ds = DamageSource.field_76377_j;
                     }

                     this.func_70097_a(ds, damage / 3.0F);
                  }

                  MCH_Lib.DbgLog(this.world, "MCH_EntityTank.collisionEntity damage=%.1f %s", damage, e.toString());
               }
            }

         }
      }
   }

   private boolean shouldCollisionDamage(Entity e) {
      if (this.getSeatIdByEntity(e) >= 0) {
         return false;
      } else if (this.noCollisionEntities.containsKey(e)) {
         return false;
      } else {
         if (e instanceof MCH_EntityHitBox && ((MCH_EntityHitBox)e).parent != null) {
            MCH_EntityAircraft ac = ((MCH_EntityHitBox)e).parent;
            if (this.noCollisionEntities.containsKey(ac)) {
               return false;
            }
         }

         if (e.func_184187_bx() instanceof MCH_EntityAircraft && this.noCollisionEntities.containsKey(e.func_184187_bx())) {
            return false;
         } else {
            return !(e.func_184187_bx() instanceof MCH_EntitySeat) || ((MCH_EntitySeat)e.func_184187_bx()).getParent() == null || !this.noCollisionEntities.containsKey(((MCH_EntitySeat)e.func_184187_bx()).getParent());
         }
      }
   }

   public void updateCollisionBox() {
      if (this.getAcInfo() != null) {
         this.WheelMng.updateBlock();
         MCH_BoundingBox[] var1 = this.extraBoundingBox;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MCH_BoundingBox bb = var1[var3];
            if (this.field_70146_Z.nextInt(3) == 0) {
               if (MCH_Config.Collision_DestroyBlock.prmBool) {
                  Vec3d v = this.getTransformedPosition(bb.offsetX, bb.offsetY, bb.offsetZ);
                  this.destoryBlockRange(v, (double)bb.width, (double)bb.height);
               }

               this.collisionEntity(bb.getBoundingBox());
            }
         }

         if (MCH_Config.Collision_DestroyBlock.prmBool) {
            this.destoryBlockRange(this.getTransformedPosition(0.0D, 0.0D, 0.0D), (double)this.field_70130_N * 1.5D, (double)(this.field_70131_O * 2.0F));
         }

         this.collisionEntity(this.func_70046_E());
      }
   }

   public void destoryBlockRange(Vec3d v, double w, double h) {
      if (this.getAcInfo() != null) {
         List<Block> destroyBlocks = MCH_Config.getBreakableBlockListFromType(this.getTankInfo().weightType);
         List<Block> noDestroyBlocks = MCH_Config.getNoBreakableBlockListFromType(this.getTankInfo().weightType);
         List<Material> destroyMaterials = MCH_Config.getBreakableMaterialListFromType(this.getTankInfo().weightType);
         int ws = (int)(w + 2.0D) / 2;
         int hs = (int)(h + 2.0D) / 2;

         for(int x = -ws; x <= ws; ++x) {
            for(int z = -ws; z <= ws; ++z) {
               for(int y = -hs; y <= hs + 1; ++y) {
                  int bx = (int)(v.x + (double)x - 0.5D);
                  int by = (int)(v.y + (double)y - 1.0D);
                  int bz = (int)(v.z + (double)z - 0.5D);
                  BlockPos blockpos = new BlockPos(bx, by, bz);
                  IBlockState iblockstate = this.world.func_180495_p(blockpos);
                  Block block = by >= 0 && by < 256 ? iblockstate.func_177230_c() : Blocks.field_150350_a;
                  Material mat = iblockstate.func_185904_a();
                  if (!Block.func_149680_a(block, Blocks.field_150350_a)) {
                     Iterator var21 = noDestroyBlocks.iterator();

                     Block c;
                     while(var21.hasNext()) {
                        c = (Block)var21.next();
                        if (Block.func_149680_a(block, c)) {
                           block = null;
                           break;
                        }
                     }

                     if (block == null) {
                        break;
                     }

                     var21 = destroyBlocks.iterator();

                     while(var21.hasNext()) {
                        c = (Block)var21.next();
                        if (Block.func_149680_a(block, c)) {
                           this.destroyBlock(blockpos);
                           mat = null;
                           break;
                        }
                     }

                     if (mat == null) {
                        break;
                     }

                     var21 = destroyMaterials.iterator();

                     while(var21.hasNext()) {
                        Material m = (Material)var21.next();
                        if (iblockstate.func_185904_a() == m) {
                           this.destroyBlock(blockpos);
                           break;
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public void destroyBlock(BlockPos blockpos) {
      if (this.field_70146_Z.nextInt(8) == 0) {
         W_WorldFunc.destroyBlock(this.world, blockpos, true);
      } else {
         this.world.func_175698_g(blockpos);
      }

   }

   private void updateWheels() {
      this.WheelMng.move(this.field_70159_w, this.field_70181_x, this.field_70179_y);
   }

   public float getMaxSpeed() {
      return this.getTankInfo().speed + 0.0F;
   }

   public void setAngles(Entity player, boolean fixRot, float fixYaw, float fixPitch, float deltaX, float deltaY, float x, float y, float partialTicks) {
      if (partialTicks < 0.03F) {
         partialTicks = 0.4F;
      }

      if (partialTicks > 0.9F) {
         partialTicks = 0.6F;
      }

      this.lowPassPartialTicks.put(partialTicks);
      partialTicks = this.lowPassPartialTicks.getAvg();
      float ac_pitch = this.getRotPitch();
      float ac_yaw = this.getRotYaw();
      float ac_roll = this.getRotRoll();
      if (this.isFreeLookMode()) {
         y = 0.0F;
         x = 0.0F;
      }

      float yaw = 0.0F;
      float pitch = 0.0F;
      float roll = 0.0F;
      MCH_Math.FMatrix m_add = MCH_Math.newMatrix();
      MCH_Math.MatTurnZ(m_add, roll / 180.0F * 3.1415927F);
      MCH_Math.MatTurnX(m_add, pitch / 180.0F * 3.1415927F);
      MCH_Math.MatTurnY(m_add, yaw / 180.0F * 3.1415927F);
      MCH_Math.MatTurnZ(m_add, (float)((double)(this.getRotRoll() / 180.0F) * 3.141592653589793D));
      MCH_Math.MatTurnX(m_add, (float)((double)(this.getRotPitch() / 180.0F) * 3.141592653589793D));
      MCH_Math.MatTurnY(m_add, (float)((double)(this.getRotYaw() / 180.0F) * 3.141592653589793D));
      MCH_Math.FVector3D v = MCH_Math.MatrixToEuler(m_add);
      v.x = MCH_Lib.RNG(v.x, -90.0F, 90.0F);
      v.z = MCH_Lib.RNG(v.z, -90.0F, 90.0F);
      if (v.z > 180.0F) {
         v.z -= 360.0F;
      }

      if (v.z < -180.0F) {
         v.z += 360.0F;
      }

      this.setRotYaw(v.y);
      this.setRotPitch(v.x);
      this.setRotRoll(v.z);
      this.onUpdateAngles(partialTicks);
      if (this.getAcInfo().limitRotation) {
         v.x = MCH_Lib.RNG(this.getRotPitch(), -90.0F, 90.0F);
         v.z = MCH_Lib.RNG(this.getRotRoll(), -90.0F, 90.0F);
         this.setRotPitch(v.x);
         this.setRotRoll(v.z);
      }

      if (MathHelper.func_76135_e(this.getRotPitch()) > 90.0F) {
         MCH_Lib.DbgLog(true, "MCH_EntityAircraft.setAngles Error:Pitch=%.1f", this.getRotPitch());
         this.setRotPitch(0.0F);
      }

      if (this.getRotRoll() > 180.0F) {
         this.setRotRoll(this.getRotRoll() - 360.0F);
      }

      if (this.getRotRoll() < -180.0F) {
         this.setRotRoll(this.getRotRoll() + 360.0F);
      }

      this.prevRotationRoll = this.getRotRoll();
      this.field_70127_C = this.getRotPitch();
      if (this.func_184187_bx() == null) {
         this.field_70126_B = this.getRotYaw();
      }

      float deltaLimit = this.getAcInfo().cameraRotationSpeed * partialTicks;
      MCH_WeaponSet ws = this.getCurrentWeapon(player);
      deltaLimit *= ws != null && ws.getInfo() != null ? ws.getInfo().cameraRotationSpeedPitch : 1.0F;
      if (deltaX > deltaLimit) {
         deltaX = deltaLimit;
      }

      if (deltaX < -deltaLimit) {
         deltaX = -deltaLimit;
      }

      if (deltaY > deltaLimit) {
         deltaY = deltaLimit;
      }

      if (deltaY < -deltaLimit) {
         deltaY = -deltaLimit;
      }

      if (!this.isOverridePlayerYaw() && !fixRot) {
         player.func_70082_c(deltaX, 0.0F);
      } else {
         if (this.func_184187_bx() == null) {
            player.field_70126_B = this.getRotYaw() + fixYaw;
         } else {
            if (this.getRotYaw() - player.field_70177_z > 180.0F) {
               player.field_70126_B += 360.0F;
            }

            if (this.getRotYaw() - player.field_70177_z < -180.0F) {
               player.field_70126_B -= 360.0F;
            }
         }

         player.field_70177_z = this.getRotYaw() + fixYaw;
      }

      if (!this.isOverridePlayerPitch() && !fixRot) {
         player.func_70082_c(0.0F, deltaY);
      } else {
         player.field_70127_C = this.getRotPitch() + fixPitch;
         player.field_70125_A = this.getRotPitch() + fixPitch;
      }

      float playerYaw = MathHelper.func_76142_g(this.getRotYaw() - player.field_70177_z);
      float playerPitch = this.getRotPitch() * MathHelper.func_76134_b((float)((double)playerYaw * 3.141592653589793D / 180.0D)) + -this.getRotRoll() * MathHelper.func_76126_a((float)((double)playerYaw * 3.141592653589793D / 180.0D));
      if (MCH_MOD.proxy.isFirstPerson()) {
         player.field_70125_A = MCH_Lib.RNG(player.field_70125_A, playerPitch + this.getAcInfo().minRotationPitch, playerPitch + this.getAcInfo().maxRotationPitch);
         player.field_70125_A = MCH_Lib.RNG(player.field_70125_A, -90.0F, 90.0F);
      }

      player.field_70127_C = player.field_70125_A;
      if (this.func_184187_bx() == null && ac_yaw != this.getRotYaw() || ac_pitch != this.getRotPitch() || ac_roll != this.getRotRoll()) {
         this.aircraftRotChanged = true;
      }

   }

   public float getSoundVolume() {
      return this.getAcInfo() != null && this.getAcInfo().throttleUpDown <= 0.0F ? 0.0F : this.soundVolume * 0.7F;
   }

   public void updateSound() {
      float target = (float)this.getCurrentThrottle();
      if (this.getRiddenByEntity() != null && (this.partCanopy == null || this.getCanopyRotation() < 1.0F)) {
         target += 0.1F;
      }

      if (!this.moveLeft && !this.moveRight && !this.throttleDown) {
         this.soundVolumeTarget *= 0.8F;
      } else {
         this.soundVolumeTarget += 0.1F;
         if (this.soundVolumeTarget > 0.75F) {
            this.soundVolumeTarget = 0.75F;
         }
      }

      if (target < this.soundVolumeTarget) {
         target = this.soundVolumeTarget;
      }

      if (this.soundVolume < target) {
         this.soundVolume += 0.02F;
         if (this.soundVolume >= target) {
            this.soundVolume = target;
         }
      } else if (this.soundVolume > target) {
         this.soundVolume -= 0.02F;
         if (this.soundVolume <= target) {
            this.soundVolume = target;
         }
      }

   }

   public float getSoundPitch() {
      float target1 = (float)(0.5D + this.getCurrentThrottle() * 0.5D);
      float target2 = (float)(0.5D + (double)this.soundVolumeTarget * 0.5D);
      return target1 > target2 ? target1 : target2;
   }

   public String getDefaultSoundName() {
      return "prop";
   }

   public boolean hasBrake() {
      return true;
   }

   public void updateParts(int stat) {
      super.updateParts(stat);
      if (!this.isDestroyed()) {
         MCH_Parts[] parts = new MCH_Parts[0];
         MCH_Parts[] var3 = parts;
         int var4 = parts.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MCH_Parts p = var3[var5];
            if (p != null) {
               p.updateStatusClient(stat);
               p.update();
            }
         }

      }
   }

   public float getUnfoldLandingGearThrottle() {
      return 0.7F;
   }

   private static class ClacAxisBB {
      public final double value;
      public final AxisAlignedBB bb;

      public ClacAxisBB(double value, AxisAlignedBB bb) {
         this.value = value;
         this.bb = bb;
      }
   }
}
