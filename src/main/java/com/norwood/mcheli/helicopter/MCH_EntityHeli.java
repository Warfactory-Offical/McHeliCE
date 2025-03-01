package com.norwood.mcheli.helicopter;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_ServerSettings;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_PacketStatusRequest;
import com.norwood.mcheli.aircraft.MCH_Rotor;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_EntityHeli extends MCH_EntityAircraft {
   private static final DataParameter<Byte> FOLD_STAT;
   private MCH_HeliInfo heliInfo = null;
   public double prevRotationRotor = 0.0D;
   public double rotationRotor = 0.0D;
   public MCH_Rotor[] rotors;
   public byte lastFoldBladeStat;
   public int foldBladesCooldown;
   public float prevRollFactor = 0.0F;

   public MCH_EntityHeli(World world) {
      super(world);
      this.currentSpeed = 0.07D;
      this.field_70156_m = true;
      this.func_70105_a(2.0F, 0.7F);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.weapons = this.createWeapon(0);
      this.rotors = new MCH_Rotor[0];
      this.lastFoldBladeStat = -1;
      if (this.field_70170_p.field_72995_K) {
         this.foldBladesCooldown = 40;
      }

   }

   public String getKindName() {
      return "helicopters";
   }

   public String getEntityType() {
      return "Plane";
   }

   public MCH_HeliInfo getHeliInfo() {
      return this.heliInfo;
   }

   public void changeType(String type) {
      MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityHeli.changeType " + type + " : " + this.toString());
      if (!type.isEmpty()) {
         this.heliInfo = MCH_HeliInfoManager.get(type);
      }

      if (this.heliInfo == null) {
         MCH_Lib.Log((Entity)this, "##### MCH_EntityHeli changeHeliType() Heli info null %d, %s, %s", W_Entity.getEntityId(this), type, this.getEntityName());
         this.setDead(true);
      } else {
         this.setAcInfo(this.heliInfo);
         this.newSeats(this.getAcInfo().getNumSeatAndRack());
         this.createRotors();
         this.weapons = this.createWeapon(1 + this.getSeatNum());
         this.initPartRotation(this.getRotYaw(), this.getRotPitch());
      }

   }

   @Nullable
   public Item getItem() {
      return this.getHeliInfo() != null ? this.getHeliInfo().item : null;
   }

   public boolean canMountWithNearEmptyMinecart() {
      return MCH_Config.MountMinecartHeli.prmBool;
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.field_70180_af.func_187214_a(FOLD_STAT, (byte)2);
   }

   protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
      par1NBTTagCompound.func_74780_a("RotorSpeed", this.getCurrentThrottle());
      par1NBTTagCompound.func_74780_a("rotetionRotor", this.rotationRotor);
      par1NBTTagCompound.func_74757_a("FoldBlade", this.getFoldBladeStat() == 0);
   }

   protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
      boolean beforeFoldBlade = this.getFoldBladeStat() == 0;
      if (this.getCommonUniqueId().isEmpty()) {
         this.setCommonUniqueId(par1NBTTagCompound.func_74779_i("HeliUniqueId"));
         MCH_Lib.Log((Entity)this, "# MCH_EntityHeli readEntityFromNBT() " + W_Entity.getEntityId(this) + ", " + this.getEntityName() + ", AircraftUniqueId=null, HeliUniqueId=" + this.getCommonUniqueId());
      }

      if (this.getTypeName().isEmpty()) {
         this.setTypeName(par1NBTTagCompound.func_74779_i("HeliType"));
         MCH_Lib.Log((Entity)this, "# MCH_EntityHeli readEntityFromNBT() " + W_Entity.getEntityId(this) + ", " + this.getEntityName() + ", TypeName=null, HeliType=" + this.getTypeName());
      }

      this.setCurrentThrottle(par1NBTTagCompound.func_74769_h("RotorSpeed"));
      this.rotationRotor = par1NBTTagCompound.func_74769_h("rotetionRotor");
      this.setFoldBladeStat((byte)(par1NBTTagCompound.func_74767_n("FoldBlade") ? 0 : 2));
      if (this.heliInfo == null) {
         this.heliInfo = MCH_HeliInfoManager.get(this.getTypeName());
         if (this.heliInfo == null) {
            MCH_Lib.Log((Entity)this, "##### MCH_EntityHeli readEntityFromNBT() Heli info null %d, %s", W_Entity.getEntityId(this), this.getEntityName());
            this.setDead(true);
         } else {
            this.setAcInfo(this.heliInfo);
         }
      }

      if (!beforeFoldBlade && this.getFoldBladeStat() == 0) {
         this.forceFoldBlade();
      }

      this.prevRotationRotor = this.rotationRotor;
   }

   public float getSoundVolume() {
      return this.getAcInfo() != null && this.getAcInfo().throttleUpDown <= 0.0F ? 0.0F : (float)this.getCurrentThrottle() * 2.0F;
   }

   public float getSoundPitch() {
      return (float)(0.2D + this.getCurrentThrottle() * 0.2D);
   }

   public String getDefaultSoundName() {
      return "heli";
   }

   public float getUnfoldLandingGearThrottle() {
      double x = this.field_70165_t - this.field_70169_q;
      double y = this.field_70163_u - this.field_70167_r;
      double z = this.field_70161_v - this.field_70166_s;
      float s = this.getAcInfo().speed / 3.5F;
      return x * x + y * y + z * z <= (double)s ? 0.8F : 0.3F;
   }

   protected void createRotors() {
      if (this.heliInfo != null) {
         this.rotors = new MCH_Rotor[this.heliInfo.rotorList.size()];
         int i = 0;

         for(Iterator var2 = this.heliInfo.rotorList.iterator(); var2.hasNext(); ++i) {
            MCH_HeliInfo.Rotor r = (MCH_HeliInfo.Rotor)var2.next();
            this.rotors[i] = new MCH_Rotor(r.bladeNum, r.bladeRot, this.field_70170_p.field_72995_K ? 2 : 2, (float)r.pos.field_72450_a, (float)r.pos.field_72448_b, (float)r.pos.field_72449_c, (float)r.rot.field_72450_a, (float)r.rot.field_72448_b, (float)r.rot.field_72449_c, r.haveFoldFunc);
         }

      }
   }

   protected void forceFoldBlade() {
      if (this.heliInfo != null && this.rotors.length > 0 && this.heliInfo.isEnableFoldBlade) {
         MCH_Rotor[] var1 = this.rotors;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MCH_Rotor r = var1[var3];
            r.update((float)this.rotationRotor);
            this.foldBlades();
            r.forceFold();
         }
      }

   }

   public boolean isFoldBlades() {
      if (this.heliInfo != null && this.rotors.length > 0) {
         return this.getFoldBladeStat() == 0;
      } else {
         return false;
      }
   }

   protected boolean canSwitchFoldBlades() {
      if (this.heliInfo != null && this.rotors.length > 0) {
         return this.heliInfo.isEnableFoldBlade && this.getCurrentThrottle() <= 0.01D && this.foldBladesCooldown == 0 && (this.getFoldBladeStat() == 2 || this.getFoldBladeStat() == 0);
      } else {
         return false;
      }
   }

   protected boolean canUseBlades() {
      if (this.heliInfo == null) {
         return false;
      } else if (this.rotors.length <= 0) {
         return true;
      } else if (this.getFoldBladeStat() == 2) {
         MCH_Rotor[] var1 = this.rotors;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MCH_Rotor r = var1[var3];
            if (r.isFoldingOrUnfolding()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected void foldBlades() {
      if (this.heliInfo != null && this.rotors.length > 0) {
         this.setCurrentThrottle(0.0D);
         MCH_Rotor[] var1 = this.rotors;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MCH_Rotor r = var1[var3];
            r.startFold();
         }

      }
   }

   public void unfoldBlades() {
      if (this.heliInfo != null && this.rotors.length > 0) {
         MCH_Rotor[] var1 = this.rotors;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MCH_Rotor r = var1[var3];
            r.startUnfold();
         }

      }
   }

   public void onRideEntity(Entity ridingEntity) {
      if (ridingEntity instanceof MCH_EntitySeat) {
         if (this.heliInfo == null || this.rotors.length <= 0) {
            return;
         }

         if (this.heliInfo.isEnableFoldBlade) {
            this.forceFoldBlade();
            this.setFoldBladeStat((byte)0);
         }
      }

   }

   protected byte getFoldBladeStat() {
      return (Byte)this.field_70180_af.func_187225_a(FOLD_STAT);
   }

   public void setFoldBladeStat(byte b) {
      if (!this.field_70170_p.field_72995_K && b >= 0 && b <= 3) {
         this.field_70180_af.func_187227_b(FOLD_STAT, b);
      }

   }

   public boolean canSwitchGunnerMode() {
      if (super.canSwitchGunnerMode() && this.canUseBlades()) {
         float roll = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotRoll()));
         float pitch = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotPitch()));
         if (roll < 40.0F && pitch < 40.0F) {
            return true;
         }
      }

      return false;
   }

   public boolean canSwitchHoveringMode() {
      if (super.canSwitchHoveringMode() && this.canUseBlades()) {
         float roll = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotRoll()));
         float pitch = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotPitch()));
         if (roll < 40.0F && pitch < 40.0F) {
            return true;
         }
      }

      return false;
   }

   public void onUpdateAircraft() {
      if (this.heliInfo == null) {
         this.changeType(this.getTypeName());
         this.field_70169_q = this.field_70165_t;
         this.field_70167_r = this.field_70163_u;
         this.field_70166_s = this.field_70161_v;
      } else {
         if (!this.isRequestedSyncStatus) {
            this.isRequestedSyncStatus = true;
            if (this.field_70170_p.field_72995_K) {
               int stat = this.getFoldBladeStat();
               if (stat == 1 || stat == 0) {
                  this.forceFoldBlade();
               }

               MCH_PacketStatusRequest.requestStatus(this);
            }
         }

         if (this.lastRiddenByEntity == null && this.getRiddenByEntity() != null) {
            this.initCurrentWeapon(this.getRiddenByEntity());
         }

         this.updateWeapons();
         this.onUpdate_Seats();
         this.onUpdate_Control();
         this.onUpdate_Rotor();
         this.field_70169_q = this.field_70165_t;
         this.field_70167_r = this.field_70163_u;
         this.field_70166_s = this.field_70161_v;
         if (!this.isDestroyed() && this.isHovering() && MathHelper.func_76135_e(this.getRotPitch()) < 70.0F) {
            this.setRotPitch(this.getRotPitch() * 0.95F);
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
         if (this.field_70170_p.field_72995_K) {
            this.onUpdate_Client();
         } else {
            this.onUpdate_Server();
         }

      }
   }

   public boolean canMouseRot() {
      return super.canMouseRot();
   }

   public boolean canUpdatePitch(Entity player) {
      return super.canUpdatePitch(player) && !this.isHovering();
   }

   public boolean canUpdateRoll(Entity player) {
      return super.canUpdateRoll(player) && !this.isHovering();
   }

   public boolean isOverridePlayerPitch() {
      return super.isOverridePlayerPitch() && !this.isHovering();
   }

   public float getRollFactor() {
      float roll = super.getRollFactor();
      double d = this.func_70092_e(this.field_70169_q, this.field_70163_u, this.field_70166_s);
      double s = (double)this.getAcInfo().speed;
      double var10000;
      if (s > 0.1D) {
         var10000 = d / s;
      } else {
         var10000 = 0.0D;
      }

      float f = this.prevRollFactor;
      this.prevRollFactor = roll;
      return (roll + f) / 2.0F;
   }

   public float getControlRotYaw(float mouseX, float mouseY, float tick) {
      return mouseX;
   }

   public float getControlRotPitch(float mouseX, float mouseY, float tick) {
      return mouseY;
   }

   public float getControlRotRoll(float mouseX, float mouseY, float tick) {
      return mouseX;
   }

   public void onUpdateAngles(float partialTicks) {
      if (!this.isDestroyed()) {
         float rotRoll = !this.isHovering() ? 0.04F : 0.07F;
         rotRoll = 1.0F - rotRoll * partialTicks;
         if (MCH_ServerSettings.enableRotationLimit) {
            if (this.getRotPitch() > MCH_ServerSettings.pitchLimitMax) {
               this.setRotPitch(this.getRotPitch() - Math.abs((this.getRotPitch() - MCH_ServerSettings.pitchLimitMax) * 0.1F * partialTicks));
            }

            if (this.getRotPitch() < MCH_ServerSettings.pitchLimitMin) {
               this.setRotPitch(this.getRotPitch() + Math.abs((this.getRotPitch() - MCH_ServerSettings.pitchLimitMin) * 0.2F * partialTicks));
            }

            if (this.getRotRoll() > MCH_ServerSettings.rollLimit) {
               this.setRotRoll(this.getRotRoll() - Math.abs((this.getRotRoll() - MCH_ServerSettings.rollLimit) * 0.03F * partialTicks));
            }

            if (this.getRotRoll() < -MCH_ServerSettings.rollLimit) {
               this.setRotRoll(this.getRotRoll() + Math.abs((this.getRotRoll() + MCH_ServerSettings.rollLimit) * 0.03F * partialTicks));
            }
         }

         if ((double)this.getRotRoll() > 0.1D && this.getRotRoll() < 65.0F) {
            this.setRotRoll(this.getRotRoll() * rotRoll);
         }

         if ((double)this.getRotRoll() < -0.1D && this.getRotRoll() > -65.0F) {
            this.setRotRoll(this.getRotRoll() * rotRoll);
         }

         if (MCH_Lib.getBlockIdY(this, 3, -3) == 0) {
            if (this.moveLeft && !this.moveRight) {
               this.setRotRoll(this.getRotRoll() - 1.2F * partialTicks);
            }

            if (this.moveRight && !this.moveLeft) {
               this.setRotRoll(this.getRotRoll() + 1.2F * partialTicks);
            }
         } else {
            if (MathHelper.func_76135_e(this.getRotPitch()) < 40.0F) {
               this.applyOnGroundPitch(0.97F);
            }

            if (this.heliInfo.isEnableFoldBlade && this.rotors.length > 0 && this.getFoldBladeStat() == 0 && !this.isDestroyed()) {
               if (this.moveLeft && !this.moveRight) {
                  this.setRotYaw(this.getRotYaw() - 0.5F * partialTicks);
               }

               if (this.moveRight && !this.moveLeft) {
                  this.setRotYaw(this.getRotYaw() + 0.5F * partialTicks);
               }
            }
         }

      }
   }

   protected void onUpdate_Rotor() {
      byte stat = this.getFoldBladeStat();
      boolean isEndSwitch = true;
      if (stat != this.lastFoldBladeStat) {
         if (stat == 1) {
            this.foldBlades();
         } else if (stat == 3) {
            this.unfoldBlades();
         }

         if (this.field_70170_p.field_72995_K) {
            this.foldBladesCooldown = 40;
         }

         this.lastFoldBladeStat = stat;
      } else if (this.foldBladesCooldown > 0) {
         --this.foldBladesCooldown;
      }

      MCH_Rotor[] var3 = this.rotors;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         MCH_Rotor r = var3[var5];
         r.update((float)this.rotationRotor);
         if (r.isFoldingOrUnfolding()) {
            isEndSwitch = false;
         }
      }

      if (isEndSwitch) {
         if (stat == 1) {
            this.setFoldBladeStat((byte)0);
         } else if (stat == 3) {
            this.setFoldBladeStat((byte)2);
         }
      }

   }

   protected void onUpdate_Control() {
      if (this.isHoveringMode() && !this.canUseFuel(true)) {
         this.switchHoveringMode(false);
      }

      if (this.isGunnerMode && !this.canUseFuel()) {
         this.switchGunnerMode(false);
      }

      if (!this.isDestroyed() && (this.getRiddenByEntity() != null || this.isHoveringMode()) && this.canUseBlades() && this.isCanopyClose() && this.canUseFuel(true)) {
         if (!this.isHovering()) {
            this.onUpdate_ControlNotHovering();
         } else {
            this.onUpdate_ControlHovering();
         }
      } else {
         if (this.getCurrentThrottle() > 0.0D) {
            this.addCurrentThrottle(-0.00125D);
         } else {
            this.setCurrentThrottle(0.0D);
         }

         if (this.heliInfo.isEnableFoldBlade && this.rotors.length > 0 && this.getFoldBladeStat() == 0 && this.field_70122_E && !this.isDestroyed()) {
            this.onUpdate_ControlFoldBladeAndOnGround();
         }
      }

      if (this.field_70170_p.field_72995_K) {
         if (!W_Lib.isClientPlayer(this.getRiddenByEntity())) {
            double ct = this.getThrottle();
            if (this.getCurrentThrottle() >= ct - 0.02D) {
               this.addCurrentThrottle(-0.01D);
            } else if (this.getCurrentThrottle() < ct) {
               this.addCurrentThrottle(0.01D);
            }
         }
      } else {
         this.setThrottle(this.getCurrentThrottle());
      }

      if (this.getCurrentThrottle() < 0.0D) {
         this.setCurrentThrottle(0.0D);
      }

      this.prevRotationRotor = this.rotationRotor;
      this.rotationRotor += (1.0D - Math.pow(1.0D - this.getCurrentThrottle(), 5.0D)) * (double)this.getAcInfo().rotorSpeed;
      this.rotationRotor %= 360.0D;
   }

   protected void onUpdate_ControlNotHovering() {
      float throttleUpDown = this.getAcInfo().throttleUpDown;
      if (this.throttleUp) {
         if (this.getCurrentThrottle() < 1.0D) {
            this.addCurrentThrottle(0.02D * (double)throttleUpDown);
         } else {
            this.setCurrentThrottle(1.0D);
         }
      } else if (this.throttleDown) {
         if (this.getCurrentThrottle() > 0.0D) {
            this.addCurrentThrottle(-0.014285714285714285D * (double)throttleUpDown);
         } else {
            this.setCurrentThrottle(0.0D);
         }
      } else if ((!this.field_70170_p.field_72995_K || W_Lib.isClientPlayer(this.getRiddenByEntity())) && this.cs_heliAutoThrottleDown) {
         if (this.getCurrentThrottle() > 0.52D) {
            this.addCurrentThrottle(-0.01D * (double)throttleUpDown);
         } else if (this.getCurrentThrottle() < 0.48D) {
            this.addCurrentThrottle(0.01D * (double)throttleUpDown);
         }
      }

      if (!this.field_70170_p.field_72995_K) {
         boolean move = false;
         float yaw = this.getRotYaw();
         double x = 0.0D;
         double z = 0.0D;
         if (this.moveLeft && !this.moveRight) {
            yaw = this.getRotYaw() - 90.0F;
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (this.moveRight && !this.moveLeft) {
            yaw = this.getRotYaw() + 90.0F;
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (move) {
            double f = 1.0D;
            double d = Math.sqrt(x * x + z * z);
            this.field_70159_w -= x / d * 0.019999999552965164D * f * (double)this.getAcInfo().speed;
            this.field_70179_y += z / d * 0.019999999552965164D * f * (double)this.getAcInfo().speed;
         }
      }

   }

   protected void onUpdate_ControlHovering() {
      if (this.getCurrentThrottle() < 1.0D) {
         this.addCurrentThrottle(0.03333333333333333D);
      } else {
         this.setCurrentThrottle(1.0D);
      }

      if (!this.field_70170_p.field_72995_K) {
         boolean move = false;
         float yaw = this.getRotYaw();
         double x = 0.0D;
         double z = 0.0D;
         if (this.throttleUp) {
            yaw = this.getRotYaw();
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (this.throttleDown) {
            yaw = this.getRotYaw() - 180.0F;
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (this.moveLeft && !this.moveRight) {
            yaw = this.getRotYaw() - 90.0F;
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (this.moveRight && !this.moveLeft) {
            yaw = this.getRotYaw() + 90.0F;
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (move) {
            double d = Math.sqrt(x * x + z * z);
            this.field_70159_w -= x / d * 0.009999999776482582D * (double)this.getAcInfo().speed;
            this.field_70179_y += z / d * 0.009999999776482582D * (double)this.getAcInfo().speed;
         }
      }

   }

   protected void onUpdate_ControlFoldBladeAndOnGround() {
      if (!this.field_70170_p.field_72995_K) {
         boolean move = false;
         float yaw = this.getRotYaw();
         double x = 0.0D;
         double z = 0.0D;
         if (this.throttleUp) {
            yaw = this.getRotYaw();
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (this.throttleDown) {
            yaw = this.getRotYaw() - 180.0F;
            x += Math.sin((double)yaw * 3.141592653589793D / 180.0D);
            z += Math.cos((double)yaw * 3.141592653589793D / 180.0D);
            move = true;
         }

         if (move) {
            double d = Math.sqrt(x * x + z * z);
            this.field_70159_w -= x / d * 0.029999999329447746D;
            this.field_70179_y += z / d * 0.029999999329447746D;
         }
      }

   }

   protected void onUpdate_Particle2() {
      if (this.field_70170_p.field_72995_K) {
         if (!((double)this.getHP() > (double)this.getMaxHP() * 0.5D)) {
            if (this.getHeliInfo() != null) {
               int rotorNum = this.getHeliInfo().rotorList.size();
               if (rotorNum > 0) {
                  if (this.isFirstDamageSmoke) {
                     this.prevDamageSmokePos = new Vec3d[rotorNum];
                  }

                  for(int ri = 0; ri < rotorNum; ++ri) {
                     Vec3d rotor_pos = ((MCH_HeliInfo.Rotor)this.getHeliInfo().rotorList.get(ri)).pos;
                     float yaw = this.getRotYaw();
                     float pitch = this.getRotPitch();
                     Vec3d pos = MCH_Lib.RotVec3(rotor_pos, -yaw, -pitch, -this.getRotRoll());
                     double x = this.field_70165_t + pos.field_72450_a;
                     double y = this.field_70163_u + pos.field_72448_b;
                     double z = this.field_70161_v + pos.field_72449_c;
                     if (this.isFirstDamageSmoke) {
                        this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
                     }

                     Vec3d prev = this.prevDamageSmokePos[ri];
                     double dx = x - prev.field_72450_a;
                     double dy = y - prev.field_72448_b;
                     double dz = z - prev.field_72449_c;
                     int num = (int)(MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) * 2.0F) + 1;

                     for(double i = 0.0D; i < (double)num; ++i) {
                        double p = (double)(this.getHP() / this.getMaxHP());
                        if (p < (double)(this.field_70146_Z.nextFloat() / 2.0F)) {
                           float c = 0.2F + this.field_70146_Z.nextFloat() * 0.3F;
                           MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", prev.field_72450_a + (x - prev.field_72450_a) * (i / (double)num), prev.field_72448_b + (y - prev.field_72448_b) * (i / (double)num), prev.field_72449_c + (z - prev.field_72449_c) * (i / (double)num));
                           prm.motionX = (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D;
                           prm.motionY = this.field_70146_Z.nextDouble() * 0.1D;
                           prm.motionZ = (this.field_70146_Z.nextDouble() - 0.5D) * 0.3D;
                           prm.size = ((float)this.field_70146_Z.nextInt(5) + 5.0F) * 1.0F;
                           prm.setColor(0.7F + this.field_70146_Z.nextFloat() * 0.1F, c, c, c);
                           MCH_ParticlesUtil.spawnParticle(prm);
                           int ebi = this.field_70146_Z.nextInt(1 + this.extraBoundingBox.length);
                           if (p < 0.3D && ebi > 0) {
                              AxisAlignedBB bb = this.extraBoundingBox[ebi - 1].getBoundingBox();
                              double bx = (bb.field_72336_d + bb.field_72340_a) / 2.0D;
                              double by = (bb.field_72337_e + bb.field_72338_b) / 2.0D;
                              double bz = (bb.field_72334_f + bb.field_72339_c) / 2.0D;
                              prm.posX = bx;
                              prm.posY = by;
                              prm.posZ = bz;
                              MCH_ParticlesUtil.spawnParticle(prm);
                           }
                        }
                     }

                     this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
                  }

                  this.isFirstDamageSmoke = false;
               }
            }
         }
      }
   }

   protected void onUpdate_Client() {
      if (this.getRiddenByEntity() != null && W_Lib.isClientPlayer(this.getRiddenByEntity())) {
         this.getRiddenByEntity().field_70125_A = this.getRiddenByEntity().field_70127_C;
      }

      if (this.aircraftPosRotInc > 0) {
         this.applyServerPositionAndRotation();
      } else {
         this.func_70107_b(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
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
         if (this.rotDestroyedYaw < 15.0F) {
            this.rotDestroyedYaw += 0.3F;
         }

         this.setRotYaw(this.getRotYaw() + this.rotDestroyedYaw * (float)this.getCurrentThrottle());
         if (MCH_Lib.getBlockIdY(this, 3, -3) == 0) {
            if (MathHelper.func_76135_e(this.getRotPitch()) < 10.0F) {
               this.setRotPitch(this.getRotPitch() + this.rotDestroyedPitch);
            }

            this.setRotRoll(this.getRotRoll() + this.rotDestroyedRoll);
         }
      }

      if (this.getRiddenByEntity() != null) {
      }

      this.onUpdate_ParticleSandCloud(false);
      this.onUpdate_Particle2();
      this.updateCamera(this.field_70165_t, this.field_70163_u, this.field_70161_v);
   }

   private void onUpdate_Server() {
      double prevMotion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      float ogp = this.getAcInfo().onGroundPitch;
      double motion;
      float speedLimit;
      float pitch;
      if (!this.isHovering()) {
         motion = 0.0D;
         if (this.canFloatWater()) {
            motion = this.getWaterDepth();
         }

         if (motion == 0.0D) {
            this.field_70181_x += (double)(!this.func_70090_H() ? this.getAcInfo().gravity : this.getAcInfo().gravityInWater);
            speedLimit = this.getRotYaw() / 180.0F * 3.1415927F;
            pitch = this.getRotPitch();
            if (MCH_Lib.getBlockIdY(this, 3, -3) > 0) {
               pitch -= ogp;
            }

            this.field_70159_w += 0.1D * (double)MathHelper.func_76126_a(speedLimit) * this.currentSpeed * (double)(-(pitch * pitch * pitch / 20000.0F)) * this.getCurrentThrottle();
            this.field_70179_y += 0.1D * (double)MathHelper.func_76134_b(speedLimit) * this.currentSpeed * (double)(pitch * pitch * pitch / 20000.0F) * this.getCurrentThrottle();
            double y = 0.0D;
            if (MathHelper.func_76135_e(this.getRotPitch()) + MathHelper.func_76135_e(this.getRotRoll() * 0.9F) <= 40.0F) {
               y = 1.0D - y / 40.0D;
            }

            double throttle = this.getCurrentThrottle();
            if (this.isDestroyed()) {
               throttle *= -0.65D;
            }

            this.field_70181_x += (y * 0.025D + 0.03D) * throttle;
         } else {
            if (MathHelper.func_76135_e(this.getRotPitch()) < 40.0F) {
               speedLimit = this.getRotPitch();
               speedLimit -= ogp;
               speedLimit *= 0.9F;
               speedLimit += ogp;
               this.setRotPitch(speedLimit);
            }

            if (MathHelper.func_76135_e(this.getRotRoll()) < 40.0F) {
               this.setRotRoll(this.getRotRoll() * 0.9F);
            }

            if (motion < 1.0D) {
               this.field_70181_x -= 1.0E-4D;
               this.field_70181_x += 0.007D * this.getCurrentThrottle();
            } else {
               if (this.field_70181_x < 0.0D) {
                  this.field_70181_x *= 0.7D;
               }

               this.field_70181_x += 0.007D;
            }
         }
      } else {
         if (this.field_70146_Z.nextInt(50) == 0) {
            this.field_70159_w += (this.field_70146_Z.nextDouble() - 0.5D) / 30.0D;
         }

         if (this.field_70146_Z.nextInt(50) == 0) {
            this.field_70181_x += (this.field_70146_Z.nextDouble() - 0.5D) / 50.0D;
         }

         if (this.field_70146_Z.nextInt(50) == 0) {
            this.field_70179_y += (this.field_70146_Z.nextDouble() - 0.5D) / 30.0D;
         }
      }

      motion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      speedLimit = this.getAcInfo().speed;
      if (motion > (double)speedLimit) {
         this.field_70159_w *= (double)speedLimit / motion;
         this.field_70179_y *= (double)speedLimit / motion;
         motion = (double)speedLimit;
      }

      if (this.isDestroyed()) {
         this.field_70159_w *= 0.0D;
         this.field_70179_y *= 0.0D;
         this.currentSpeed = 0.0D;
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

      if (this.field_70122_E) {
         this.field_70159_w *= 0.5D;
         this.field_70179_y *= 0.5D;
         if (MathHelper.func_76135_e(this.getRotPitch()) < 40.0F) {
            pitch = this.getRotPitch();
            pitch -= ogp;
            pitch *= 0.9F;
            pitch += ogp;
            this.setRotPitch(pitch);
         }

         if (MathHelper.func_76135_e(this.getRotRoll()) < 40.0F) {
            this.setRotRoll(this.getRotRoll() * 0.9F);
         }
      }

      this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
      this.field_70181_x *= 0.95D;
      this.field_70159_w *= 0.99D;
      this.field_70179_y *= 0.99D;
      this.func_70101_b(this.getRotYaw(), this.getRotPitch());
      this.onUpdate_updateBlock();
      if (this.getRiddenByEntity() != null && this.getRiddenByEntity().field_70128_L) {
         this.unmountEntity();
      }

   }

   static {
      FOLD_STAT = EntityDataManager.func_187226_a(MCH_EntityHeli.class, DataSerializers.field_187191_a);
   }
}
