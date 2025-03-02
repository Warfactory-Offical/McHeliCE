package com.norwood.mcheli.plane;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_PacketStatusRequest;
import com.norwood.mcheli.aircraft.MCH_Parts;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Blocks;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCP_EntityPlane extends MCH_EntityAircraft {
   private MCP_PlaneInfo planeInfo = null;
   public float soundVolume;
   public MCH_Parts partNozzle;
   public MCH_Parts partWing;
   public float rotationRotor;
   public float prevRotationRotor;
   public float addkeyRotValue;

   public MCP_EntityPlane(World world) {
      super(world);
      this.currentSpeed = 0.07D;
      this.field_70156_m = true;
      this.func_70105_a(2.0F, 0.7F);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.weapons = this.createWeapon(0);
      this.soundVolume = 0.0F;
      this.partNozzle = null;
      this.partWing = null;
      this.field_70138_W = 0.6F;
      this.rotationRotor = 0.0F;
      this.prevRotationRotor = 0.0F;
   }

   public String getKindName() {
      return "planes";
   }

   public String getEntityType() {
      return "Plane";
   }

   public MCP_PlaneInfo getPlaneInfo() {
      return this.planeInfo;
   }

   public void changeType(String type) {
      MCH_Lib.DbgLog(this.world, "MCP_EntityPlane.changeType " + type + " : " + this.toString());
      if (!type.isEmpty()) {
         this.planeInfo = MCP_PlaneInfoManager.get(type);
      }

      if (this.planeInfo == null) {
         MCH_Lib.Log((Entity)this, "##### MCP_EntityPlane changePlaneType() Plane info null %d, %s, %s", W_Entity.getEntityId(this), type, this.getEntityName());
         this.func_70106_y();
      } else {
         this.setAcInfo(this.planeInfo);
         this.newSeats(this.getAcInfo().getNumSeatAndRack());
         this.partNozzle = this.createNozzle(this.planeInfo);
         this.partWing = this.createWing(this.planeInfo);
         this.weapons = this.createWeapon(1 + this.getSeatNum());
         this.initPartRotation(this.getRotYaw(), this.getRotPitch());
      }

   }

   @Nullable
   public Item getItem() {
      return this.getPlaneInfo() != null ? this.getPlaneInfo().item : null;
   }

   public boolean canMountWithNearEmptyMinecart() {
      return MCH_Config.MountMinecartPlane.prmBool;
   }

   protected void entityInit() {
      super.entityInit();
   }

   protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
   }

   protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
      if (this.planeInfo == null) {
         this.planeInfo = MCP_PlaneInfoManager.get(this.getTypeName());
         if (this.planeInfo == null) {
            MCH_Lib.Log((Entity)this, "##### MCP_EntityPlane readEntityFromNBT() Plane info null %d, %s", W_Entity.getEntityId(this), this.getEntityName());
            this.func_70106_y();
         } else {
            this.setAcInfo(this.planeInfo);
         }
      }

   }

   public void func_70106_y() {
      super.func_70106_y();
   }

   public int getNumEjectionSeat() {
      if (this.getAcInfo() != null && this.getAcInfo().isEnableEjectionSeat) {
         int n = this.getSeatNum() + 1;
         return n <= 2 ? n : 0;
      } else {
         return 0;
      }
   }

   public void onInteractFirst(EntityPlayer player) {
      this.addkeyRotValue = 0.0F;
   }

   public boolean canSwitchGunnerMode() {
      if (!super.canSwitchGunnerMode()) {
         return false;
      } else {
         float roll = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotRoll()));
         float pitch = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotPitch()));
         if (!(roll > 40.0F) && !(pitch > 40.0F)) {
            return this.getCurrentThrottle() > 0.6000000238418579D && MCH_Lib.getBlockIdY(this, 3, -5) == 0;
         } else {
            return false;
         }
      }
   }

   public void onUpdateAircraft() {
      if (this.planeInfo == null) {
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

         if (this.field_70122_E && this.getVtolMode() == 0 && this.planeInfo.isDefaultVtol) {
            this.swithVtolMode(true);
         }

         this.field_70169_q = this.posX;
         this.field_70167_r = this.posY;
         this.field_70166_s = this.posZ;
         if (!this.isDestroyed() && this.isHovering() && MathHelper.func_76135_e(this.getRotPitch()) < 70.0F) {
            this.setRotPitch(this.getRotPitch() * 0.95F, "isHovering()");
         }

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

   public boolean canUpdateYaw(Entity player) {
      return super.canUpdateYaw(player) && !this.isHovering();
   }

   public boolean canUpdatePitch(Entity player) {
      return super.canUpdatePitch(player) && !this.isHovering();
   }

   public boolean canUpdateRoll(Entity player) {
      return super.canUpdateRoll(player) && !this.isHovering();
   }

   public float getYawFactor() {
      float yaw = this.getVtolMode() > 0 ? this.getPlaneInfo().vtolYaw : super.getYawFactor();
      return yaw * 0.8F;
   }

   public float getPitchFactor() {
      float pitch = this.getVtolMode() > 0 ? this.getPlaneInfo().vtolPitch : super.getPitchFactor();
      return pitch * 0.8F;
   }

   public float getRollFactor() {
      float roll = this.getVtolMode() > 0 ? this.getPlaneInfo().vtolYaw : super.getRollFactor();
      return roll * 0.8F;
   }

   public boolean isOverridePlayerPitch() {
      return super.isOverridePlayerPitch() && !this.isHovering();
   }

   public boolean isOverridePlayerYaw() {
      return super.isOverridePlayerYaw() && !this.isHovering();
   }

   public float getControlRotYaw(float mouseX, float mouseY, float tick) {
      if (MCH_Config.MouseControlFlightSimMode.prmBool) {
         this.rotationByKey(tick);
         return this.addkeyRotValue * 20.0F;
      } else {
         return mouseX;
      }
   }

   public float getControlRotPitch(float mouseX, float mouseY, float tick) {
      return mouseY;
   }

   public float getControlRotRoll(float mouseX, float mouseY, float tick) {
      if (MCH_Config.MouseControlFlightSimMode.prmBool) {
         return mouseX * 2.0F;
      } else {
         return this.getVtolMode() == 0 ? mouseX * 0.5F : mouseX;
      }
   }

   private void rotationByKey(float partialTicks) {
      float rot = 0.2F;
      if (!MCH_Config.MouseControlFlightSimMode.prmBool && this.getVtolMode() != 0) {
         rot *= 0.0F;
      }

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

         boolean isFly = MCH_Lib.getBlockIdY(this, 3, -3) == 0;
         float gmy;
         if (isFly && !this.isFreeLookMode() && !this.isGunnerMode && (!this.getAcInfo().isFloat || !(this.getWaterDepth() > 0.0D))) {
            if (isFly && !MCH_Config.MouseControlFlightSimMode.prmBool) {
               this.rotationByKey(partialTicks);
               this.setRotRoll(this.getRotRoll() + this.addkeyRotValue * 0.5F * this.getAcInfo().mobilityRoll * partialTicks * 3.3F);
            }
         } else {
            gmy = 1.0F;
            if (!isFly) {
               gmy = this.getAcInfo().mobilityYawOnGround;
               if (!this.getAcInfo().canRotOnGround) {
                  Block block = MCH_Lib.getBlockY(this, 3, -2, false);
                  if (!W_Block.isEqual(block, W_Block.getWater()) && !W_Block.isEqual(block, W_Blocks.field_150350_a)) {
                     gmy = 0.0F;
                  }
               }
            }

            if (this.moveLeft && !this.moveRight) {
               this.setRotYaw(this.getRotYaw() - 0.6F * gmy * partialTicks);
            }

            if (this.moveRight && !this.moveLeft) {
               this.setRotYaw(this.getRotYaw() + 0.6F * gmy * partialTicks);
            }
         }

         this.addkeyRotValue = (float)((double)this.addkeyRotValue * (1.0D - (double)(0.1F * partialTicks)));
         if (!isFly && MathHelper.func_76135_e(this.getRotPitch()) < 40.0F) {
            this.applyOnGroundPitch(0.97F);
         }

         if (this.getNozzleRotation() > 0.001F) {
            gmy = 1.0F - 0.03F * partialTicks;
            this.setRotPitch(this.getRotPitch() * gmy);
            gmy = 1.0F - 0.1F * partialTicks;
            this.setRotRoll(this.getRotRoll() * gmy);
         }

      }
   }

   protected void onUpdate_Control() {
      if (this.isGunnerMode && !this.canUseFuel()) {
         this.switchGunnerMode(false);
      }

      this.throttleBack = (float)((double)this.throttleBack * 0.8D);
      if (this.getRiddenByEntity() != null && !this.getRiddenByEntity().field_70128_L && this.isCanopyClose() && this.canUseWing() && this.canUseFuel() && !this.isDestroyed()) {
         this.onUpdate_ControlNotHovering();
      } else if (this.isTargetDrone() && this.canUseFuel() && !this.isDestroyed()) {
         this.throttleUp = true;
         this.onUpdate_ControlNotHovering();
      } else if (this.getCurrentThrottle() > 0.0D) {
         this.addCurrentThrottle(-0.0025D * (double)this.getAcInfo().throttleUpDown);
      } else {
         this.setCurrentThrottle(0.0D);
      }

      if (this.getCurrentThrottle() < 0.0D) {
         this.setCurrentThrottle(0.0D);
      }

      if (this.world.isRemote) {
         if (!W_Lib.isClientPlayer(this.getRiddenByEntity())) {
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

   protected void onUpdate_ControlNotHovering() {
      if (!this.isGunnerMode) {
         float throttleUpDown = this.getAcInfo().throttleUpDown;
         boolean turn = this.moveLeft && !this.moveRight || !this.moveLeft && this.moveRight;
         boolean localThrottleUp = this.throttleUp;
         if (turn && this.getCurrentThrottle() < (double)this.getAcInfo().pivotTurnThrottle && !localThrottleUp && !this.throttleDown) {
            localThrottleUp = true;
            throttleUpDown *= 2.0F;
         }

         if (localThrottleUp) {
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
         } else if (this.cs_planeAutoThrottleDown && this.getCurrentThrottle() > 0.0D) {
            this.addCurrentThrottle(-0.005D * (double)throttleUpDown);
            if (this.getCurrentThrottle() <= 0.0D) {
               this.setCurrentThrottle(0.0D);
            }
         }
      }

   }

   protected void onUpdate_Particle() {
      if (this.world.isRemote) {
         this.onUpdate_ParticleLandingGear();
         this.onUpdate_ParticleNozzle();
      }

   }

   protected void onUpdate_Particle2() {
      if (this.world.isRemote) {
         if (!((double)this.getHP() >= (double)this.getMaxHP() * 0.5D)) {
            if (this.getPlaneInfo() != null) {
               int rotorNum = this.getPlaneInfo().rotorList.size();
               if (rotorNum < 0) {
                  rotorNum = 0;
               }

               if (this.isFirstDamageSmoke) {
                  this.prevDamageSmokePos = new Vec3d[rotorNum + 1];
               }

               float yaw = this.getRotYaw();
               float pitch = this.getRotPitch();
               float roll = this.getRotRoll();
               boolean spawnSmoke = true;

               int d;
               for(d = 0; d < rotorNum; ++d) {
                  if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2D && this.getMaxHP() > 0) {
                     int d = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2D) / 0.3D * 15.0D);
                     if (d > 0 && this.field_70146_Z.nextInt(d) > 0) {
                        spawnSmoke = false;
                     }
                  }

                  Vec3d rotor_pos = ((MCP_PlaneInfo.Rotor)this.getPlaneInfo().rotorList.get(d)).pos;
                  Vec3d pos = MCH_Lib.RotVec3(rotor_pos, -yaw, -pitch, -roll);
                  double x = this.posX + pos.x;
                  double y = this.posY + pos.y;
                  double z = this.posZ + pos.z;
                  this.onUpdate_Particle2SpawnSmoke(d, x, y, z, 1.0F, spawnSmoke);
               }

               spawnSmoke = true;
               if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2D && this.getMaxHP() > 0) {
                  d = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2D) / 0.3D * 15.0D);
                  if (d > 0 && this.field_70146_Z.nextInt(d) > 0) {
                     spawnSmoke = false;
                  }
               }

               double px = this.posX;
               double py = this.posY;
               double pz = this.posZ;
               if (this.getSeatInfo(0) != null && this.getSeatInfo(0).pos != null) {
                  Vec3d pos = MCH_Lib.RotVec3(0.0D, this.getSeatInfo(0).pos.y, -2.0D, -yaw, -pitch, -roll);
                  px += pos.x;
                  py += pos.y;
                  pz += pos.z;
               }

               this.onUpdate_Particle2SpawnSmoke(rotorNum, px, py, pz, rotorNum == 0 ? 2.0F : 1.0F, spawnSmoke);
               this.isFirstDamageSmoke = false;
            }
         }
      }
   }

   public void onUpdate_Particle2SpawnSmoke(int ri, double x, double y, double z, float size, boolean spawnSmoke) {
      if (this.isFirstDamageSmoke || this.prevDamageSmokePos[ri] == null) {
         this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
      }

      Vec3d prev = this.prevDamageSmokePos[ri];
      double dx = x - prev.x;
      double dy = y - prev.y;
      double dz = z - prev.z;
      int num = (int)((double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) / 0.3D) + 1;

      for(int i = 0; i < num; ++i) {
         float c = 0.2F + this.field_70146_Z.nextFloat() * 0.3F;
         MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", prev.x + (x - prev.x) * (double)i / 3.0D, prev.y + (y - prev.y) * (double)i / 3.0D, prev.z + (z - prev.z) * (double)i / 3.0D);
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
      double d = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;
      if (d > 0.01D) {
         int x = MathHelper.func_76128_c(this.posX + 0.5D);
         int y = MathHelper.func_76128_c(this.posY - 0.5D);
         int z = MathHelper.func_76128_c(this.posZ + 0.5D);
         MCH_ParticlesUtil.spawnParticleTileCrack(this.world, x, y, z, this.posX + ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)this.field_70130_N, this.func_174813_aQ().field_72338_b + 0.1D, this.posZ + ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)this.field_70130_N, -this.field_70159_w * 4.0D, 1.5D, -this.field_70179_y * 4.0D);
      }

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

   public void onUpdate_ParticleNozzle() {
      if (this.planeInfo != null && this.planeInfo.haveNozzle()) {
         if (!(this.getCurrentThrottle() <= 0.10000000149011612D)) {
            float yaw = this.getRotYaw();
            float pitch = this.getRotPitch();
            float roll = this.getRotRoll();
            Vec3d nozzleRot = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -yaw - 180.0F, pitch - this.getNozzleRotation(), roll);
            Iterator var5 = this.planeInfo.nozzles.iterator();

            while(var5.hasNext()) {
               MCH_AircraftInfo.DrawnPart nozzle = (MCH_AircraftInfo.DrawnPart)var5.next();
               if ((double)this.field_70146_Z.nextFloat() <= this.getCurrentThrottle() * 1.5D) {
                  Vec3d nozzlePos = MCH_Lib.RotVec3(nozzle.pos, -yaw, -pitch, -roll);
                  double x = this.posX + nozzlePos.x + nozzleRot.x;
                  double y = this.posY + nozzlePos.y + nozzleRot.y;
                  double z = this.posZ + nozzlePos.z + nozzleRot.z;
                  float a = 0.7F;
                  if (W_WorldFunc.getBlockId(this.world, (int)(x + nozzleRot.x * 3.0D), (int)(y + nozzleRot.y * 3.0D), (int)(z + nozzleRot.z * 3.0D)) != 0) {
                     a = 2.0F;
                  }

                  MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", x, y, z, nozzleRot.x + (double)((this.field_70146_Z.nextFloat() - 0.5F) * a), nozzleRot.y, nozzleRot.z + (double)((this.field_70146_Z.nextFloat() - 0.5F) * a), 5.0F * this.getAcInfo().particlesScale);
                  MCH_ParticlesUtil.spawnParticle(prm);
               }
            }

         }
      }
   }

   public void destroyAircraft() {
      super.destroyAircraft();
      int inv = 1;
      if (this.getRotRoll() >= 0.0F) {
         if (this.getRotRoll() > 90.0F) {
            inv = -1;
         }
      } else if (this.getRotRoll() > -90.0F) {
         inv = -1;
      }

      this.rotDestroyedRoll = (0.5F + this.field_70146_Z.nextFloat()) * (float)inv;
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

      if (this.isDestroyed()) {
         if (MCH_Lib.getBlockIdY(this, 3, -3) == 0) {
            if (MathHelper.func_76135_e(this.getRotPitch()) < 10.0F) {
               this.setRotPitch(this.getRotPitch() + this.rotDestroyedPitch);
            }

            float roll = MathHelper.func_76135_e(this.getRotRoll());
            if (roll < 45.0F || roll > 135.0F) {
               this.setRotRoll(this.getRotRoll() + this.rotDestroyedRoll);
            }
         } else if (MathHelper.func_76135_e(this.getRotPitch()) > 20.0F) {
            this.setRotPitch(this.getRotPitch() * 0.99F);
         }
      }

      if (this.getRiddenByEntity() != null) {
      }

      this.updateSound();
      this.onUpdate_Particle();
      this.onUpdate_Particle2();
      this.onUpdate_ParticleSplash();
      this.onUpdate_ParticleSandCloud(true);
      this.updateCamera(this.posX, this.posY, this.posZ);
   }

   private void onUpdate_Server() {
      double prevMotion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      double dp = 0.0D;
      if (this.canFloatWater()) {
         dp = this.getWaterDepth();
      }

      boolean levelOff = this.isGunnerMode;
      if (dp == 0.0D) {
         if (this.isTargetDrone() && this.canUseFuel() && !this.isDestroyed()) {
            Block block = MCH_Lib.getBlockY(this, 3, -40, true);
            if (block != null && !W_Block.isEqual(block, W_Blocks.field_150350_a)) {
               block = MCH_Lib.getBlockY(this, 3, -5, true);
               if (block == null || W_Block.isEqual(block, W_Blocks.field_150350_a)) {
                  this.setRotYaw(this.getRotYaw() + this.getAcInfo().autoPilotRot * 2.0F);
                  if (this.getRotPitch() > -20.0F) {
                     this.setRotPitch(this.getRotPitch() - 0.5F);
                  }
               }
            } else {
               this.setRotYaw(this.getRotYaw() + this.getAcInfo().autoPilotRot * 1.0F);
               this.setRotPitch(this.getRotPitch() * 0.95F);
               if (this.canFoldLandingGear()) {
                  this.foldLandingGear();
               }

               levelOff = true;
            }
         }

         if (!levelOff) {
            this.field_70181_x += 0.04D + (double)(!this.func_70090_H() ? this.getAcInfo().gravity : this.getAcInfo().gravityInWater);
            this.field_70181_x += -0.047D * (1.0D - this.getCurrentThrottle());
         } else {
            this.field_70181_x *= 0.8D;
         }
      } else {
         this.setRotPitch(this.getRotPitch() * 0.8F, "getWaterDepth != 0");
         if (MathHelper.func_76135_e(this.getRotRoll()) < 40.0F) {
            this.setRotRoll(this.getRotRoll() * 0.9F);
         }

         if (dp < 1.0D) {
            this.field_70181_x -= 1.0E-4D;
            this.field_70181_x += 0.007D * this.getCurrentThrottle();
         } else {
            if (this.field_70181_x < 0.0D) {
               this.field_70181_x /= 2.0D;
            }

            this.field_70181_x += 0.007D;
         }
      }

      float throttle = (float)(this.getCurrentThrottle() / 10.0D);
      Vec3d v;
      if (this.getNozzleRotation() > 0.001F) {
         this.setRotPitch(this.getRotPitch() * 0.95F);
         v = MCH_Lib.Rot2Vec3(this.getRotYaw(), this.getRotPitch() - this.getNozzleRotation());
         if (this.getNozzleRotation() >= 90.0F) {
            v = new Vec3d(v.x * 0.800000011920929D, v.y, v.z * 0.800000011920929D);
         }
      } else {
         v = MCH_Lib.Rot2Vec3(this.getRotYaw(), this.getRotPitch() - 10.0F);
      }

      if (!levelOff) {
         if (this.getNozzleRotation() <= 0.01F) {
            this.field_70181_x += v.y * (double)throttle / 2.0D;
         } else {
            this.field_70181_x += v.y * (double)throttle / 8.0D;
         }
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

      this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
      this.field_70181_x *= 0.95D;
      this.field_70159_w *= (double)this.getAcInfo().motionFactor;
      this.field_70179_y *= (double)this.getAcInfo().motionFactor;
      this.func_70101_b(this.getRotYaw(), this.getRotPitch());
      this.onUpdate_updateBlock();
      if (this.getRiddenByEntity() != null && this.getRiddenByEntity().field_70128_L) {
         this.unmountEntity();
      }

   }

   public float getMaxSpeed() {
      float f = 0.0F;
      if (this.partWing != null && this.getPlaneInfo().isVariableSweepWing) {
         f = (this.getPlaneInfo().sweepWingSpeed - this.getPlaneInfo().speed) * this.partWing.getFactor();
      } else if (this.partHatch != null && this.getPlaneInfo().isVariableSweepWing) {
         f = (this.getPlaneInfo().sweepWingSpeed - this.getPlaneInfo().speed) * this.partHatch.getFactor();
      }

      return this.getPlaneInfo().speed + f;
   }

   public float getSoundVolume() {
      return this.getAcInfo() != null && this.getAcInfo().throttleUpDown <= 0.0F ? 0.0F : this.soundVolume * 0.7F;
   }

   public void updateSound() {
      float target = (float)this.getCurrentThrottle();
      if (this.getRiddenByEntity() != null && (this.partCanopy == null || this.getCanopyRotation() < 1.0F)) {
         target += 0.1F;
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
      return (float)(0.6D + this.getCurrentThrottle() * 0.4D);
   }

   public String getDefaultSoundName() {
      return "plane";
   }

   public void updateParts(int stat) {
      super.updateParts(stat);
      if (!this.isDestroyed()) {
         MCH_Parts[] parts = new MCH_Parts[]{this.partNozzle, this.partWing};
         MCH_Parts[] var3 = parts;
         int var4 = parts.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MCH_Parts p = var3[var5];
            if (p != null) {
               p.updateStatusClient(stat);
               p.update();
            }
         }

         if (!this.world.isRemote && this.partWing != null && this.getPlaneInfo().isVariableSweepWing && this.partWing.isON() && this.getCurrentThrottle() >= 0.20000000298023224D && (this.getCurrentThrottle() < 0.5D || MCH_Lib.getBlockIdY(this, 1, -10) != 0)) {
            this.partWing.setStatusServer(false);
         }

      }
   }

   public float getUnfoldLandingGearThrottle() {
      return 0.7F;
   }

   public boolean canSwitchVtol() {
      if (this.planeInfo != null && this.planeInfo.isEnableVtol) {
         if (this.getModeSwitchCooldown() > 0) {
            return false;
         } else if (this.getVtolMode() == 1) {
            return false;
         } else if (MathHelper.func_76135_e(this.getRotRoll()) > 30.0F) {
            return false;
         } else if (this.field_70122_E && this.planeInfo.isDefaultVtol) {
            return false;
         } else {
            this.setModeSwitchCooldown(20);
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean getNozzleStat() {
      return this.partNozzle != null ? this.partNozzle.getStatus() : false;
   }

   public int getVtolMode() {
      if (!this.getNozzleStat()) {
         return this.getNozzleRotation() <= 0.005F ? 0 : 1;
      } else {
         return this.getNozzleRotation() >= 89.995F ? 2 : 1;
      }
   }

   public float getFuleConsumptionFactor() {
      return super.getFuelConsumptionFactor() * (float)(this.getVtolMode() == 2 ? 1 : 1);
   }

   public float getNozzleRotation() {
      return this.partNozzle != null ? this.partNozzle.rotation : 0.0F;
   }

   public float getPrevNozzleRotation() {
      return this.partNozzle != null ? this.partNozzle.prevRotation : 0.0F;
   }

   public void swithVtolMode(boolean mode) {
      if (this.partNozzle != null) {
         if (this.planeInfo.isDefaultVtol && this.field_70122_E && !mode) {
            return;
         }

         if (!this.world.isRemote) {
            this.partNozzle.setStatusServer(mode);
         }

         if (this.getRiddenByEntity() != null && !this.getRiddenByEntity().field_70128_L) {
            this.getRiddenByEntity().field_70125_A = this.getRiddenByEntity().field_70127_C = 0.0F;
         }
      }

   }

   protected MCH_Parts createNozzle(MCP_PlaneInfo info) {
      MCH_Parts nozzle = null;
      if (info.haveNozzle() || info.haveRotor() || info.isEnableVtol) {
         nozzle = new MCH_Parts(this, 1, PART_STAT, "Nozzle");
         nozzle.rotationMax = 90.0F;
         nozzle.rotationInv = 1.5F;
         nozzle.soundStartSwichOn.setPrm("plane_cc", 1.0F, 0.5F);
         nozzle.soundEndSwichOn.setPrm("plane_cc", 1.0F, 0.5F);
         nozzle.soundStartSwichOff.setPrm("plane_cc", 1.0F, 0.5F);
         nozzle.soundEndSwichOff.setPrm("plane_cc", 1.0F, 0.5F);
         nozzle.soundSwitching.setPrm("plane_cv", 1.0F, 0.5F);
         if (info.isDefaultVtol) {
            nozzle.forceSwitch(true);
         }
      }

      return nozzle;
   }

   protected MCH_Parts createWing(MCP_PlaneInfo info) {
      MCH_Parts wing = null;
      if (this.planeInfo.haveWing()) {
         wing = new MCH_Parts(this, 3, PART_STAT, "Wing");
         wing.rotationMax = 90.0F;
         wing.rotationInv = 2.5F;
         wing.soundStartSwichOn.setPrm("plane_cc", 1.0F, 0.5F);
         wing.soundEndSwichOn.setPrm("plane_cc", 1.0F, 0.5F);
         wing.soundStartSwichOff.setPrm("plane_cc", 1.0F, 0.5F);
         wing.soundEndSwichOff.setPrm("plane_cc", 1.0F, 0.5F);
      }

      return wing;
   }

   public boolean canUseWing() {
      if (this.partWing == null) {
         return true;
      } else if (this.getPlaneInfo().isVariableSweepWing) {
         return this.getCurrentThrottle() < 0.2D ? this.partWing.isOFF() : true;
      } else {
         return this.partWing.isOFF();
      }
   }

   public boolean canFoldWing() {
      if (this.partWing != null && this.getModeSwitchCooldown() <= 0) {
         if (this.getPlaneInfo().isVariableSweepWing) {
            if (!this.field_70122_E && MCH_Lib.getBlockIdY(this, 3, -20) == 0) {
               if (this.getCurrentThrottle() < 0.699999988079071D) {
                  return false;
               }
            } else if (this.getCurrentThrottle() > 0.10000000149011612D) {
               return false;
            }
         } else {
            if (!this.field_70122_E && MCH_Lib.getBlockIdY(this, 3, -3) == 0) {
               return false;
            }

            if (this.getCurrentThrottle() > 0.009999999776482582D) {
               return false;
            }
         }

         return this.partWing.isOFF();
      } else {
         return false;
      }
   }

   public boolean canUnfoldWing() {
      return this.partWing != null && this.getModeSwitchCooldown() <= 0 ? this.partWing.isON() : false;
   }

   public void foldWing(boolean fold) {
      if (this.partWing != null && this.getModeSwitchCooldown() <= 0) {
         this.partWing.setStatusServer(fold);
         this.setModeSwitchCooldown(20);
      }
   }

   public float getWingRotation() {
      return this.partWing != null ? this.partWing.rotation : 0.0F;
   }

   public float getPrevWingRotation() {
      return this.partWing != null ? this.partWing.prevRotation : 0.0F;
   }
}
