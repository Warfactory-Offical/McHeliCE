package com.norwood.mcheli.aircraft;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Camera;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Explosion;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_LowPassFilterFloat;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.MCH_Math;
import com.norwood.mcheli.MCH_Queue;
import com.norwood.mcheli.MCH_Vector2;
import com.norwood.mcheli.MCH_ViewEntityDummy;
import com.norwood.mcheli.__helper.MCH_CriteriaTriggers;
import com.norwood.mcheli.__helper.entity.IEntityItemStackPickable;
import com.norwood.mcheli.__helper.entity.IEntitySinglePassenger;
import com.norwood.mcheli.__helper.entity.ITargetMarkerObject;
import com.norwood.mcheli.chain.MCH_EntityChain;
import com.norwood.mcheli.command.MCH_Command;
import com.norwood.mcheli.flare.MCH_Flare;
import com.norwood.mcheli.mob.MCH_EntityGunner;
import com.norwood.mcheli.mob.MCH_ItemSpawnGunner;
import com.norwood.mcheli.multiplay.MCH_Multiplay;
import com.norwood.mcheli.parachute.MCH_EntityParachute;
import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.tool.MCH_ItemWrench;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import com.norwood.mcheli.weapon.MCH_IEntityLockChecker;
import com.norwood.mcheli.weapon.MCH_WeaponBase;
import com.norwood.mcheli.weapon.MCH_WeaponCreator;
import com.norwood.mcheli.weapon.MCH_WeaponDummy;
import com.norwood.mcheli.weapon.MCH_WeaponInfo;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import com.norwood.mcheli.weapon.MCH_WeaponSet;
import com.norwood.mcheli.weapon.MCH_WeaponSmoke;
import com.norwood.mcheli.wrapper.W_AxisAlignedBB;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Blocks;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_EntityContainer;
import com.norwood.mcheli.wrapper.W_EntityPlayer;
import com.norwood.mcheli.wrapper.W_EntityRenderer;
import com.norwood.mcheli.wrapper.W_Item;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_NBTTag;
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
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MCH_EntityAircraft extends W_EntityContainer implements MCH_IEntityLockChecker, MCH_IEntityCanRideAircraft, IEntityAdditionalSpawnData, IEntitySinglePassenger, ITargetMarkerObject, IEntityItemStackPickable {
   public static final float Y_OFFSET = 0.35F;
   private static final DataParameter<Integer> DAMAGE;
   private static final DataParameter<String> ID_TYPE;
   private static final DataParameter<String> TEXTURE_NAME;
   private static final DataParameter<Integer> UAV_STATION;
   private static final DataParameter<Integer> STATUS;
   private static final DataParameter<Integer> USE_WEAPON;
   private static final DataParameter<Integer> FUEL;
   private static final DataParameter<Integer> ROT_ROLL;
   private static final DataParameter<String> COMMAND;
   private static final DataParameter<Integer> THROTTLE;
   protected static final DataParameter<Integer> PART_STAT;
   protected static final int PART_ID_CANOPY = 0;
   protected static final int PART_ID_NOZZLE = 1;
   protected static final int PART_ID_LANDINGGEAR = 2;
   protected static final int PART_ID_WING = 3;
   protected static final int PART_ID_HATCH = 4;
   public static final byte LIMIT_GROUND_PITCH = 40;
   public static final byte LIMIT_GROUND_ROLL = 40;
   public boolean isRequestedSyncStatus;
   private MCH_AircraftInfo acInfo;
   private int commonStatus;
   private Entity[] partEntities;
   private MCH_EntityHitBox pilotSeat;
   private MCH_EntitySeat[] seats;
   private MCH_SeatInfo[] seatsInfo;
   private String commonUniqueId;
   private int seatSearchCount;
   protected double velocityX;
   protected double velocityY;
   protected double velocityZ;
   public boolean keepOnRideRotation;
   protected int aircraftPosRotInc;
   protected double aircraftX;
   protected double aircraftY;
   protected double aircraftZ;
   protected double aircraftYaw;
   protected double aircraftPitch;
   public boolean aircraftRollRev;
   public boolean aircraftRotChanged;
   public float rotationRoll;
   public float prevRotationRoll;
   private double currentThrottle;
   private double prevCurrentThrottle;
   public double currentSpeed;
   public int currentFuel;
   public float throttleBack = 0.0F;
   public double beforeHoverThrottle;
   public int waitMountEntity = 0;
   public boolean throttleUp = false;
   public boolean throttleDown = false;
   public boolean moveLeft = false;
   public boolean moveRight = false;
   public MCH_LowPassFilterFloat lowPassPartialTicks;
   private MCH_Radar entityRadar;
   private int radarRotate;
   private MCH_Flare flareDv;
   private int currentFlareIndex;
   protected MCH_WeaponSet[] weapons;
   protected int[] currentWeaponID;
   public float lastRiderYaw;
   public float prevLastRiderYaw;
   public float lastRiderPitch;
   public float prevLastRiderPitch;
   protected MCH_WeaponSet dummyWeapon;
   protected int useWeaponStat;
   protected int hitStatus;
   protected final MCH_SoundUpdater soundUpdater;
   protected Entity lastRiddenByEntity;
   protected Entity lastRidingEntity;
   public List<MCH_EntityAircraft.UnmountReserve> listUnmountReserve = new ArrayList();
   private int countOnUpdate;
   private MCH_EntityChain towChainEntity;
   private MCH_EntityChain towedChainEntity;
   public MCH_Camera camera;
   private int cameraId;
   protected boolean isGunnerMode = false;
   protected boolean isGunnerModeOtherSeat = false;
   private boolean isHoveringMode = false;
   public static final int CAMERA_PITCH_MIN = -30;
   public static final int CAMERA_PITCH_MAX = 70;
   private MCH_EntityTvMissile TVmissile;
   protected boolean isGunnerFreeLookMode = false;
   public final MCH_MissileDetector missileDetector;
   public int serverNoMoveCount = 0;
   public int repairCount;
   public int beforeDamageTaken;
   public int timeSinceHit;
   private int despawnCount;
   public float rotDestroyedYaw;
   public float rotDestroyedPitch;
   public float rotDestroyedRoll;
   public int damageSinceDestroyed;
   public boolean isFirstDamageSmoke = true;
   public Vec3d[] prevDamageSmokePos = new Vec3d[0];
   private MCH_EntityUavStation uavStation;
   public boolean cs_dismountAll;
   public boolean cs_heliAutoThrottleDown;
   public boolean cs_planeAutoThrottleDown;
   public boolean cs_tankAutoThrottleDown;
   public MCH_Parts partHatch;
   public MCH_Parts partCanopy;
   public MCH_Parts partLandingGear;
   public double prevRidingEntityPosX;
   public double prevRidingEntityPosY;
   public double prevRidingEntityPosZ;
   public boolean canRideRackStatus;
   private int modeSwitchCooldown;
   public MCH_BoundingBox[] extraBoundingBox;
   public float lastBBDamageFactor;
   private final MCH_AircraftInventory inventory;
   private double fuelConsumption;
   private int fuelSuppliedCount;
   private int supplyAmmoWait;
   private boolean beforeSupplyAmmo;
   public MCH_EntityAircraft.WeaponBay[] weaponBays;
   public float[] rotPartRotation;
   public float[] prevRotPartRotation;
   public float[] rotCrawlerTrack = new float[2];
   public float[] prevRotCrawlerTrack = new float[2];
   public float[] throttleCrawlerTrack = new float[2];
   public float[] rotTrackRoller = new float[2];
   public float[] prevRotTrackRoller = new float[2];
   public float rotWheel = 0.0F;
   public float prevRotWheel = 0.0F;
   public float rotYawWheel = 0.0F;
   public float prevRotYawWheel = 0.0F;
   private boolean isParachuting;
   public float ropesLength = 0.0F;
   private MCH_Queue<Vec3d> prevPosition;
   private int tickRepelling;
   private int lastUsedRopeIndex;
   private boolean dismountedUserCtrl;
   public float lastSearchLightYaw;
   public float lastSearchLightPitch;
   public float rotLightHatch = 0.0F;
   public float prevRotLightHatch = 0.0F;
   public int recoilCount = 0;
   public float recoilYaw = 0.0F;
   public float recoilValue = 0.0F;
   public int brightnessHigh = 240;
   public int brightnessLow = 240;
   public final HashMap<Entity, Integer> noCollisionEntities = new HashMap();
   private double lastCalcLandInDistanceCount;
   private double lastLandInDistance;
   public float thirdPersonDist = 4.0F;
   public Entity lastAttackedEntity = null;
   private static final MCH_EntitySeat[] seatsDummy;
   private boolean switchSeat = false;

   public MCH_EntityAircraft(World world) {
      super(world);
      MCH_Lib.DbgLog(world, "MCH_EntityAircraft : " + this.toString());
      this.isRequestedSyncStatus = false;
      this.setAcInfo((MCH_AircraftInfo)null);
      this.dropContentsWhenDead = false;
      this.noClip = true;
      this.flareDv = new MCH_Flare(world, this);
      this.currentFlareIndex = 0;
      this.entityRadar = new MCH_Radar(world);
      this.radarRotate = 0;
      this.currentWeaponID = new int[0];
      this.aircraftPosRotInc = 0;
      this.aircraftX = 0.0D;
      this.aircraftY = 0.0D;
      this.aircraftZ = 0.0D;
      this.aircraftYaw = 0.0D;
      this.aircraftPitch = 0.0D;
      this.currentSpeed = 0.0D;
      this.setCurrentThrottle(0.0D);
      this.currentFuel = 0;
      this.cs_dismountAll = false;
      this.cs_heliAutoThrottleDown = true;
      this.cs_planeAutoThrottleDown = false;
      this._renderDistanceWeight = 2.0D * MCH_Config.RenderDistanceWeight.prmDouble;
      this.setCommonUniqueId("");
      this.seatSearchCount = 0;
      this.seatsInfo = null;
      this.seats = new MCH_EntitySeat[0];
      this.pilotSeat = new MCH_EntityHitBox(world, this, 1.0F, 1.0F);
      this.pilotSeat.parent = this;
      this.partEntities = new Entity[]{this.pilotSeat};
      this.setTextureName("");
      this.camera = new MCH_Camera(world, this, this.posX, this.posY, this.posZ);
      this.setCameraId(0);
      this.lastRiddenByEntity = null;
      this.lastRidingEntity = null;
      this.soundUpdater = MCH_MOD.proxy.CreateSoundUpdater(this);
      this.countOnUpdate = 0;
      this.setTowChainEntity((MCH_EntityChain)null);
      this.dummyWeapon = new MCH_WeaponSet(new MCH_WeaponDummy(this.world, Vec3d.ZERO, 0.0F, 0.0F, "", (MCH_WeaponInfo)null));
      this.useWeaponStat = 0;
      this.hitStatus = 0;
      this.repairCount = 0;
      this.beforeDamageTaken = 0;
      this.timeSinceHit = 0;
      this.setDespawnCount(0);
      this.missileDetector = new MCH_MissileDetector(this, world);
      this.uavStation = null;
      this.modeSwitchCooldown = 0;
      this.partHatch = null;
      this.partCanopy = null;
      this.partLandingGear = null;
      this.weaponBays = new MCH_EntityAircraft.WeaponBay[0];
      this.rotPartRotation = new float[0];
      this.prevRotPartRotation = new float[0];
      this.lastRiderYaw = 0.0F;
      this.prevLastRiderYaw = 0.0F;
      this.lastRiderPitch = 0.0F;
      this.prevLastRiderPitch = 0.0F;
      this.rotationRoll = 0.0F;
      this.prevRotationRoll = 0.0F;
      this.lowPassPartialTicks = new MCH_LowPassFilterFloat(10);
      this.extraBoundingBox = new MCH_BoundingBox[0];
      this.func_174826_a(new MCH_AircraftBoundingBox(this));
      this.lastBBDamageFactor = 1.0F;
      this.inventory = new MCH_AircraftInventory(this);
      this.fuelConsumption = 0.0D;
      this.fuelSuppliedCount = 0;
      this.canRideRackStatus = false;
      this.isParachuting = false;
      this.prevPosition = new MCH_Queue(10, Vec3d.ZERO);
      this.lastSearchLightYaw = this.lastSearchLightPitch = 0.0F;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(ID_TYPE, "");
      this.dataManager.register(DAMAGE, 0);
      this.dataManager.register(STATUS, 0);
      this.dataManager.register(USE_WEAPON, 0);
      this.dataManager.register(FUEL, 0);
      this.dataManager.register(TEXTURE_NAME, "");
      this.dataManager.register(UAV_STATION, 0);
      this.dataManager.register(ROT_ROLL, 0);
      this.dataManager.register(COMMAND, "");
      this.dataManager.register(THROTTLE, 0);
      this.dataManager.register(PART_STAT, 0);
      if (!this.world.isRemote) {
         this.setCommonStatus(3, MCH_Config.InfinityAmmo.prmBool);
         this.setCommonStatus(4, MCH_Config.InfinityFuel.prmBool);
         this.setGunnerStatus(true);
      }

      this.getEntityData().setString("EntityType", this.getEntityType());
   }

   public float getServerRoll() {
      return (float)((Integer)this.dataManager.func_187225_a(ROT_ROLL)).shortValue();
   }

   public float getRotYaw() {
      return this.field_70177_z;
   }

   public float getRotPitch() {
      return this.field_70125_A;
   }

   public float getRotRoll() {
      return this.rotationRoll;
   }

   public void setRotYaw(float f) {
      this.field_70177_z = f;
   }

   public void setRotPitch(float f) {
      this.field_70125_A = f;
   }

   public void setRotPitch(float f, String msg) {
      this.setRotPitch(f);
   }

   public void setRotRoll(float f) {
      this.rotationRoll = f;
   }

   public void applyOnGroundPitch(float factor) {
      if (this.getAcInfo() != null) {
         float ogp = this.getAcInfo().onGroundPitch;
         float pitch = this.getRotPitch();
         pitch -= ogp;
         pitch *= factor;
         pitch += ogp;
         this.setRotPitch(pitch, "applyOnGroundPitch");
      }

      this.setRotRoll(this.getRotRoll() * factor);
   }

   public float calcRotYaw(float partialTicks) {
      return this.field_70126_B + (this.getRotYaw() - this.field_70126_B) * partialTicks;
   }

   public float calcRotPitch(float partialTicks) {
      return this.field_70127_C + (this.getRotPitch() - this.field_70127_C) * partialTicks;
   }

   public float calcRotRoll(float partialTicks) {
      return this.prevRotationRoll + (this.getRotRoll() - this.prevRotationRoll) * partialTicks;
   }

   protected void func_70101_b(float y, float p) {
      this.setRotYaw(y % 360.0F);
      this.setRotPitch(p % 360.0F);
   }

   public boolean isInfinityAmmo(Entity player) {
      return this.isCreative(player) || this.getCommonStatus(3);
   }

   public boolean isInfinityFuel(Entity player, boolean checkOtherSeet) {
      if (!this.isCreative(player) && !this.getCommonStatus(4)) {
         if (checkOtherSeet) {
            MCH_EntitySeat[] var3 = this.getSeats();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               MCH_EntitySeat seat = var3[var5];
               if (seat != null && this.isCreative(seat.getRiddenByEntity())) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void setCommand(String s, EntityPlayer player) {
      if (!this.world.isRemote && MCH_Command.canUseCommand(player)) {
         this.setCommandForce(s);
      }

   }

   public void setCommandForce(String s) {
      if (!this.world.isRemote) {
         this.dataManager.func_187227_b(COMMAND, s);
      }

   }

   public String getCommand() {
      return (String)this.dataManager.func_187225_a(COMMAND);
   }

   public String getKindName() {
      return "";
   }

   public String getEntityType() {
      return "";
   }

   public void setTypeName(String s) {
      String beforeType = this.getTypeName();
      if (s != null && !s.isEmpty() && s.compareTo(beforeType) != 0) {
         this.dataManager.func_187227_b(ID_TYPE, s);
         this.changeType(s);
         this.initRotationYaw(this.getRotYaw());
      }

   }

   public String getTypeName() {
      return (String)this.dataManager.func_187225_a(ID_TYPE);
   }

   public abstract void changeType(String var1);

   public boolean isTargetDrone() {
      return this.getAcInfo() != null && this.getAcInfo().isTargetDrone;
   }

   public boolean isUAV() {
      return this.getAcInfo() != null && this.getAcInfo().isUAV;
   }

   public boolean isSmallUAV() {
      return this.getAcInfo() != null && this.getAcInfo().isSmallUAV;
   }

   public boolean isAlwaysCameraView() {
      return this.getAcInfo() != null && this.getAcInfo().alwaysCameraView;
   }

   public void setUavStation(MCH_EntityUavStation uavSt) {
      this.uavStation = uavSt;
      if (!this.world.isRemote) {
         if (uavSt != null) {
            this.dataManager.func_187227_b(UAV_STATION, W_Entity.getEntityId(uavSt));
         } else {
            this.dataManager.func_187227_b(UAV_STATION, 0);
         }
      }

   }

   public float getStealth() {
      return this.getAcInfo() != null ? this.getAcInfo().stealth : 0.0F;
   }

   public MCH_AircraftInventory getGuiInventory() {
      return this.inventory;
   }

   public void openGui(EntityPlayer player) {
      if (!this.world.isRemote) {
         player.openGui(MCH_MOD.instance, 1, this.world, (int)this.posX, (int)this.posY, (int)this.posZ);
      }

   }

   @Nullable
   public MCH_EntityUavStation getUavStation() {
      return this.isUAV() ? this.uavStation : null;
   }

   @Nullable
   public static MCH_EntityAircraft getAircraft_RiddenOrControl(@Nullable Entity rider) {
      if (rider != null) {
         if (rider.func_184187_bx() instanceof MCH_EntityAircraft) {
            return (MCH_EntityAircraft)rider.func_184187_bx();
         }

         if (rider.func_184187_bx() instanceof MCH_EntitySeat) {
            return ((MCH_EntitySeat)rider.func_184187_bx()).getParent();
         }

         if (rider.func_184187_bx() instanceof MCH_EntityUavStation) {
            MCH_EntityUavStation uavStation = (MCH_EntityUavStation)rider.func_184187_bx();
            return uavStation.getControlAircract();
         }
      }

      return null;
   }

   public static boolean isSeatPassenger(@Nullable Entity rider) {
      return rider != null && rider.func_184187_bx() instanceof MCH_EntitySeat;
   }

   public boolean isCreative(@Nullable Entity entity) {
      if (entity instanceof EntityPlayer && ((EntityPlayer)entity).field_71075_bZ.field_75098_d) {
         return true;
      } else {
         return entity instanceof MCH_EntityGunner && ((MCH_EntityGunner)entity).isCreative;
      }
   }

   @Nullable
   public Entity getRiddenByEntity() {
      if (this.isUAV() && this.uavStation != null) {
         return this.uavStation.getRiddenByEntity();
      } else {
         List<Entity> passengers = this.func_184188_bt();
         return passengers.isEmpty() ? null : (Entity)passengers.get(0);
      }
   }

   public boolean getCommonStatus(int bit) {
      return (this.commonStatus >> bit & 1) != 0;
   }

   public void setCommonStatus(int bit, boolean b) {
      this.setCommonStatus(bit, b, false);
   }

   public void setCommonStatus(int bit, boolean b, boolean writeClient) {
      if (!this.world.isRemote || writeClient) {
         int bofore = this.commonStatus;
         int mask = 1 << bit;
         if (b) {
            this.commonStatus |= mask;
         } else {
            this.commonStatus &= ~mask;
         }

         if (bofore != this.commonStatus) {
            MCH_Lib.DbgLog(this.world, "setCommonStatus : %08X -> %08X ", this.dataManager.func_187225_a(STATUS), this.commonStatus);
            this.dataManager.func_187227_b(STATUS, this.commonStatus);
         }
      }

   }

   public double getThrottle() {
      return 0.05D * (double)(Integer)this.dataManager.func_187225_a(THROTTLE);
   }

   public void setThrottle(double t) {
      int n = (int)(t * 20.0D);
      if (n == 0 && t > 0.0D) {
         n = 1;
      }

      this.dataManager.func_187227_b(THROTTLE, n);
   }

   public int getMaxHP() {
      return this.getAcInfo() != null ? this.getAcInfo().maxHp : 100;
   }

   public int getHP() {
      return this.getMaxHP() - this.getDamageTaken() >= 0 ? this.getMaxHP() - this.getDamageTaken() : 0;
   }

   public void setDamageTaken(int par1) {
      if (par1 < 0) {
         par1 = 0;
      }

      if (par1 > this.getMaxHP()) {
         par1 = this.getMaxHP();
      }

      this.dataManager.func_187227_b(DAMAGE, par1);
   }

   public int getDamageTaken() {
      return (Integer)this.dataManager.func_187225_a(DAMAGE);
   }

   public void destroyAircraft() {
      this.setSearchLight(false);
      this.switchHoveringMode(false);
      this.switchGunnerMode(false);

      for(int i = 0; i < this.getSeatNum() + 1; ++i) {
         Entity e = this.getEntityBySeatId(i);
         if (e instanceof EntityPlayer) {
            this.switchCameraMode((EntityPlayer)e, 0);
         }
      }

      if (this.isTargetDrone()) {
         this.setDespawnCount(20 * MCH_Config.DespawnCount.prmInt / 10);
      } else {
         this.setDespawnCount(20 * MCH_Config.DespawnCount.prmInt);
      }

      this.rotDestroyedPitch = this.field_70146_Z.nextFloat() - 0.5F;
      this.rotDestroyedRoll = (this.field_70146_Z.nextFloat() - 0.5F) * 0.5F;
      this.rotDestroyedYaw = 0.0F;
      if (this.isUAV() && this.getRiddenByEntity() != null) {
         this.getRiddenByEntity().func_184210_p();
      }

      if (!this.world.isRemote) {
         this.ejectSeat(this.getRiddenByEntity());
         Entity entity = this.getEntityBySeatId(1);
         if (entity != null) {
            this.ejectSeat(entity);
         }

         float dmg = MCH_Config.KillPassengersWhenDestroyed.prmBool ? 100000.0F : 0.001F;
         DamageSource dse = DamageSource.field_76377_j;
         if (this.world.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            if (this.lastAttackedEntity instanceof EntityPlayer) {
               dse = DamageSource.func_76365_a((EntityPlayer)this.lastAttackedEntity);
            }
         } else {
            dse = DamageSource.func_94539_a(new Explosion(this.world, this.lastAttackedEntity, this.posX, this.posY, this.posZ, 1.0F, false, true));
         }

         Entity riddenByEntity = this.getRiddenByEntity();
         if (riddenByEntity != null) {
            riddenByEntity.func_70097_a(dse, dmg);
         }

         MCH_EntitySeat[] var5 = this.getSeats();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            MCH_EntitySeat seat = var5[var7];
            if (seat != null && seat.getRiddenByEntity() != null) {
               seat.getRiddenByEntity().func_70097_a(dse, dmg);
            }
         }
      }

   }

   public boolean isDestroyed() {
      return this.getDespawnCount() > 0;
   }

   public int getDespawnCount() {
      return this.despawnCount;
   }

   public void setDespawnCount(int despawnCount) {
      this.despawnCount = despawnCount;
   }

   public boolean isEntityRadarMounted() {
      return this.getAcInfo() != null ? this.getAcInfo().isEnableEntityRadar : false;
   }

   public boolean canFloatWater() {
      return this.getAcInfo() != null && this.getAcInfo().isFloat && !this.isDestroyed();
   }

   @SideOnly(Side.CLIENT)
   public int func_70070_b() {
      if (this.haveSearchLight() && this.isSearchLightON()) {
         return 15728880;
      } else {
         int i = MathHelper.func_76128_c(this.posX);
         int j = MathHelper.func_76128_c(this.posZ);
         if (this.world.func_175667_e(new BlockPos(i, 0, j))) {
            double d0 = (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) * 0.66D;
            float fo = this.getAcInfo() != null ? this.getAcInfo().submergedDamageHeight : 0.0F;
            if (this.canFloatWater()) {
               fo = this.getAcInfo().floatOffset;
               if (fo < 0.0F) {
                  fo = -fo;
               }

               ++fo;
            }

            int k = MathHelper.func_76128_c(this.posY + (double)fo + d0);
            int val = this.world.func_175626_b(new BlockPos(i, k, j), 0);
            int low = val & '\uffff';
            int high = val >> 16 & '\uffff';
            if (high < this.brightnessHigh) {
               if (this.brightnessHigh > 0 && this.getCountOnUpdate() % 2 == 0) {
                  --this.brightnessHigh;
               }
            } else if (high > this.brightnessHigh) {
               this.brightnessHigh += 4;
               if (this.brightnessHigh > 240) {
                  this.brightnessHigh = 240;
               }
            }

            return this.brightnessHigh << 16 | low;
         } else {
            return 0;
         }
      }
   }

   @Nullable
   public MCH_AircraftInfo.CameraPosition getCameraPosInfo() {
      if (this.getAcInfo() == null) {
         return null;
      } else {
         Entity player = MCH_Lib.getClientPlayer();
         int sid = this.getSeatIdByEntity(player);
         if (sid == 0 && this.canSwitchCameraPos() && this.getCameraId() > 0 && this.getCameraId() < this.getAcInfo().cameraPosition.size()) {
            return (MCH_AircraftInfo.CameraPosition)this.getAcInfo().cameraPosition.get(this.getCameraId());
         } else {
            return sid > 0 && sid < this.getSeatsInfo().length && this.getSeatsInfo()[sid].invCamPos ? this.getSeatsInfo()[sid].getCamPos() : (MCH_AircraftInfo.CameraPosition)this.getAcInfo().cameraPosition.get(0);
         }
      }
   }

   public int getCameraId() {
      return this.cameraId;
   }

   public void setCameraId(int cameraId) {
      MCH_Lib.DbgLog(true, "MCH_EntityAircraft.setCameraId %d -> %d", this.cameraId, cameraId);
      this.cameraId = cameraId;
   }

   public boolean canSwitchCameraPos() {
      return this.getCameraPosNum() >= 2;
   }

   public int getCameraPosNum() {
      return this.getAcInfo() != null ? this.getAcInfo().cameraPosition.size() : 1;
   }

   public void onAcInfoReloaded() {
      if (this.getAcInfo() != null) {
         this.func_70105_a(this.getAcInfo().bodyWidth, this.getAcInfo().bodyHeight);
      }
   }

   public void writeSpawnData(ByteBuf buffer) {
      if (this.getAcInfo() != null) {
         buffer.writeFloat(this.getAcInfo().bodyHeight);
         buffer.writeFloat(this.getAcInfo().bodyWidth);
         buffer.writeFloat(this.getAcInfo().thirdPersonDist);
         byte[] name = this.getTypeName().getBytes();
         buffer.writeShort(name.length);
         buffer.writeBytes(name);
      } else {
         buffer.writeFloat(this.field_70131_O);
         buffer.writeFloat(this.field_70130_N);
         buffer.writeFloat(4.0F);
         buffer.writeShort(0);
      }

   }

   public void readSpawnData(ByteBuf data) {
      try {
         float height = data.readFloat();
         float width = data.readFloat();
         this.func_70105_a(width, height);
         this.thirdPersonDist = data.readFloat();
         int len = data.readShort();
         if (len > 0) {
            byte[] dst = new byte[len];
            data.readBytes(dst);
            this.changeType(new String(dst));
         }
      } catch (Exception var6) {
         MCH_Lib.Log((Entity)this, "readSpawnData error!");
         var6.printStackTrace();
      }

   }

   protected void func_70037_a(NBTTagCompound nbt) {
      this.setDespawnCount(nbt.func_74762_e("AcDespawnCount"));
      this.setTextureName(nbt.func_74779_i("TextureName"));
      this.setCommonUniqueId(nbt.func_74779_i("AircraftUniqueId"));
      this.setRotRoll(nbt.func_74760_g("AcRoll"));
      this.prevRotationRoll = this.getRotRoll();
      this.prevLastRiderYaw = this.lastRiderYaw = nbt.func_74760_g("AcLastRYaw");
      this.prevLastRiderPitch = this.lastRiderPitch = nbt.func_74760_g("AcLastRPitch");
      this.setPartStatus(nbt.func_74762_e("PartStatus"));
      this.setTypeName(nbt.func_74779_i("TypeName"));
      super.func_70037_a(nbt);
      this.getGuiInventory().readEntityFromNBT(nbt);
      this.setCommandForce(nbt.func_74779_i("AcCommand"));
      this.setGunnerStatus(nbt.getBoolean("AcGunnerStatus"));
      this.setFuel(nbt.func_74762_e("AcFuel"));
      int[] wa_list = nbt.func_74759_k("AcWeaponsAmmo");

      for(int i = 0; i < wa_list.length; ++i) {
         this.getWeapon(i).setRestAllAmmoNum(wa_list[i]);
         this.getWeapon(i).reloadMag();
      }

      if (this.getDespawnCount() > 0) {
         this.setDamageTaken(this.getMaxHP());
      } else if (nbt.hasKey("AcDamage")) {
         this.setDamageTaken(nbt.func_74762_e("AcDamage"));
      }

      if (this.haveSearchLight() && nbt.hasKey("SearchLight")) {
         this.setSearchLight(nbt.getBoolean("SearchLight"));
      }

      this.dismountedUserCtrl = nbt.getBoolean("AcDismounted");
   }

   protected void func_70014_b(NBTTagCompound nbt) {
      nbt.setString("TextureName", this.getTextureName());
      nbt.setString("AircraftUniqueId", this.getCommonUniqueId());
      nbt.setString("TypeName", this.getTypeName());
      nbt.setInteger("PartStatus", this.getPartStatus() & this.getLastPartStatusMask());
      nbt.setInteger("AcFuel", this.getFuel());
      nbt.setInteger("AcDespawnCount", this.getDespawnCount());
      nbt.setFloat("AcRoll", this.getRotRoll());
      nbt.setBoolean("SearchLight", this.isSearchLightON());
      nbt.setFloat("AcLastRYaw", this.getLastRiderYaw());
      nbt.setFloat("AcLastRPitch", this.getLastRiderPitch());
      nbt.setString("AcCommand", this.getCommand());
      if (!nbt.hasKey("AcGunnerStatus")) {
         this.setGunnerStatus(true);
      }

      nbt.setBoolean("AcGunnerStatus", this.getGunnerStatus());
      super.func_70014_b(nbt);
      this.getGuiInventory().writeEntityToNBT(nbt);
      int[] wa_list = new int[this.getWeaponNum()];

      for(int i = 0; i < wa_list.length; ++i) {
         wa_list[i] = this.getWeapon(i).getRestAllAmmoNum() + this.getWeapon(i).getAmmoNum();
      }

      nbt.func_74782_a("AcWeaponsAmmo", W_NBTTag.newTagIntArray("AcWeaponsAmmo", wa_list));
      nbt.setInteger("AcDamage", this.getDamageTaken());
      nbt.setBoolean("AcDismounted", this.dismountedUserCtrl);
   }

   public boolean func_70097_a(DamageSource damageSource, float org_damage) {
      float damageFactor = this.lastBBDamageFactor;
      this.lastBBDamageFactor = 1.0F;
      if (this.func_180431_b(damageSource)) {
         return false;
      } else if (this.field_70128_L) {
         return false;
      } else if (this.timeSinceHit > 0) {
         return false;
      } else {
         String dmt = damageSource.func_76355_l();
         if (dmt.equalsIgnoreCase("inFire")) {
            return false;
         } else if (dmt.equalsIgnoreCase("cactus")) {
            return false;
         } else if (this.world.isRemote) {
            return true;
         } else {
            float damage = MCH_Config.applyDamageByExternal(this, damageSource, org_damage);
            if (this.getAcInfo() != null && this.getAcInfo().invulnerable) {
               damage = 0.0F;
            }

            if (damageSource == DamageSource.field_76380_i) {
               this.func_70106_y();
            }

            if (!MCH_Multiplay.canAttackEntity((DamageSource)damageSource, this)) {
               return false;
            } else {
               if (dmt.equalsIgnoreCase("lava")) {
                  damage *= (float)(this.field_70146_Z.nextInt(8) + 2);
                  this.timeSinceHit = 2;
               }

               if (dmt.startsWith("explosion")) {
                  this.timeSinceHit = 1;
               } else if (this.isMountedEntity(damageSource.func_76346_g())) {
                  return false;
               }

               if (dmt.equalsIgnoreCase("onFire")) {
                  this.timeSinceHit = 10;
               }

               boolean isCreative = false;
               boolean isSneaking = false;
               Entity entity = damageSource.func_76346_g();
               if (entity instanceof EntityLivingBase) {
                  this.lastAttackedEntity = entity;
               }

               boolean isDamegeSourcePlayer = false;
               boolean playDamageSound = false;
               if (entity instanceof EntityPlayer) {
                  EntityPlayer player = (EntityPlayer)entity;
                  isCreative = player.field_71075_bZ.field_75098_d;
                  isSneaking = player.func_70093_af();
                  if (dmt.equalsIgnoreCase("player")) {
                     if (isCreative) {
                        isDamegeSourcePlayer = true;
                     } else if (this.getAcInfo() != null && !this.getAcInfo().creativeOnly && !MCH_Config.PreventingBroken.prmBool) {
                        if (MCH_Config.BreakableOnlyPickaxe.prmBool) {
                           if (!player.func_184614_ca().func_190926_b() && player.func_184614_ca().func_77973_b() instanceof ItemPickaxe) {
                              isDamegeSourcePlayer = true;
                           }
                        } else {
                           isDamegeSourcePlayer = !this.isRidePlayer();
                        }
                     }
                  }

                  W_WorldFunc.MOD_playSoundAtEntity(this, "hit", damage > 0.0F ? 1.0F : 0.5F, 1.0F);
               } else {
                  playDamageSound = true;
               }

               if (!this.isDestroyed()) {
                  if (!isDamegeSourcePlayer) {
                     MCH_AircraftInfo acInfo = this.getAcInfo();
                     if (acInfo != null && !dmt.equalsIgnoreCase("lava") && !dmt.equalsIgnoreCase("onFire")) {
                        if (damage > acInfo.armorMaxDamage) {
                           damage = acInfo.armorMaxDamage;
                        }

                        if (damageFactor <= 1.0F) {
                           damage *= damageFactor;
                        }

                        damage *= acInfo.armorDamageFactor;
                        damage -= acInfo.armorMinDamage;
                        if (damage <= 0.0F) {
                           MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.attackEntityFrom:no damage=%.1f -> %.1f(factor=%.2f):%s", org_damage, damage, damageFactor, dmt);
                           return false;
                        }

                        if (damageFactor > 1.0F) {
                           damage *= damageFactor;
                        }
                     }

                     MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.attackEntityFrom:damage=%.1f(factor=%.2f):%s", damage, damageFactor, dmt);
                     this.setDamageTaken(this.getDamageTaken() + (int)damage);
                  }

                  this.func_70018_K();
                  if (this.getDamageTaken() >= this.getMaxHP() || isDamegeSourcePlayer) {
                     if (!isDamegeSourcePlayer) {
                        this.setDamageTaken(this.getMaxHP());
                        this.destroyAircraft();
                        this.timeSinceHit = 20;
                        String cmd = this.getCommand().trim();
                        if (cmd.startsWith("/")) {
                           cmd = cmd.substring(1);
                        }

                        if (!cmd.isEmpty()) {
                           MCH_DummyCommandSender.execCommand(cmd);
                        }

                        if (dmt.equalsIgnoreCase("inWall")) {
                           this.explosionByCrash(0.0D);
                           this.damageSinceDestroyed = this.getMaxHP();
                        } else {
                           MCH_Explosion.newExplosion(this.world, (Entity)null, entity, this.posX, this.posY, this.posZ, 2.0F, 2.0F, true, true, true, true, 5);
                        }
                     } else {
                        if (this.getAcInfo() != null && this.getAcInfo().getItem() != null) {
                           if (isCreative) {
                              if (MCH_Config.DropItemInCreativeMode.prmBool && !isSneaking) {
                                 this.func_145778_a(this.getAcInfo().getItem(), 1, 0.0F);
                              }

                              if (!MCH_Config.DropItemInCreativeMode.prmBool && isSneaking) {
                                 this.func_145778_a(this.getAcInfo().getItem(), 1, 0.0F);
                              }
                           } else {
                              this.func_145778_a(this.getAcInfo().getItem(), 1, 0.0F);
                           }
                        }

                        this.setDead(true);
                     }
                  }
               } else if (isDamegeSourcePlayer && isCreative) {
                  this.setDead(true);
               }

               if (playDamageSound) {
                  W_WorldFunc.MOD_playSoundAtEntity(this, "helidmg", 1.0F, 0.9F + this.field_70146_Z.nextFloat() * 0.1F);
               }

               return true;
            }
         }
      }
   }

   public boolean isExploded() {
      return this.isDestroyed() && this.damageSinceDestroyed > this.getMaxHP() / 10 + 1;
   }

   public void destruct() {
      if (this.getRiddenByEntity() != null) {
         this.getRiddenByEntity().func_184210_p();
      }

      this.setDead(true);
   }

   @Nullable
   public EntityItem func_70099_a(ItemStack is, float par2) {
      if (is.func_190916_E() == 0) {
         return null;
      } else {
         this.setAcDataToItem(is);
         return super.func_70099_a(is, par2);
      }
   }

   public void setAcDataToItem(ItemStack is) {
      if (!is.func_77942_o()) {
         is.func_77982_d(new NBTTagCompound());
      }

      NBTTagCompound nbt = is.func_77978_p();
      nbt.setString("MCH_Command", this.getCommand());
      if (MCH_Config.ItemFuel.prmBool) {
         nbt.setInteger("MCH_Fuel", this.getFuel());
      }

      if (MCH_Config.ItemDamage.prmBool) {
         is.func_77964_b(this.getDamageTaken());
      }

   }

   public void getAcDataFromItem(ItemStack is) {
      if (is.func_77942_o()) {
         NBTTagCompound nbt = is.func_77978_p();
         this.setCommandForce(nbt.func_74779_i("MCH_Command"));
         if (MCH_Config.ItemFuel.prmBool) {
            this.setFuel(nbt.func_74762_e("MCH_Fuel"));
         }

         if (MCH_Config.ItemDamage.prmBool) {
            this.setDamageTaken(is.func_77960_j());
         }

      }
   }

   public boolean func_70300_a(EntityPlayer player) {
      if (this.isUAV()) {
         return super.func_70300_a(player);
      } else if (!this.field_70128_L) {
         if (this.getSeatIdByEntity(player) >= 0) {
            return player.func_70068_e(this) <= 4096.0D;
         } else {
            return player.func_70068_e(this) <= 64.0D;
         }
      } else {
         return false;
      }
   }

   public void func_70108_f(Entity par1Entity) {
   }

   public void func_70024_g(double par1, double par3, double par5) {
   }

   @SideOnly(Side.CLIENT)
   public void func_70016_h(double par1, double par3, double par5) {
      this.velocityX = this.field_70159_w = par1;
      this.velocityY = this.field_70181_x = par3;
      this.velocityZ = this.field_70179_y = par5;
   }

   public void onFirstUpdate() {
      if (!this.world.isRemote) {
         this.setCommonStatus(3, MCH_Config.InfinityAmmo.prmBool);
         this.setCommonStatus(4, MCH_Config.InfinityFuel.prmBool);
      }

   }

   public void onRidePilotFirstUpdate() {
      if (this.world.isRemote && W_Lib.isClientPlayer(this.getRiddenByEntity())) {
         this.updateClientSettings(0);
      }

      Entity pilot = this.getRiddenByEntity();
      if (pilot != null) {
         pilot.field_70177_z = this.getLastRiderYaw();
         pilot.field_70125_A = this.getLastRiderPitch();
      }

      this.keepOnRideRotation = false;
      if (this.getAcInfo() != null) {
         this.switchFreeLookModeClient(this.getAcInfo().defaultFreelook);
      }

   }

   public double getCurrentThrottle() {
      return this.currentThrottle;
   }

   public void setCurrentThrottle(double throttle) {
      this.currentThrottle = throttle;
   }

   public void addCurrentThrottle(double throttle) {
      this.setCurrentThrottle(this.getCurrentThrottle() + throttle);
   }

   public double getPrevCurrentThrottle() {
      return this.prevCurrentThrottle;
   }

   public boolean canMouseRot() {
      return !this.field_70128_L && this.getRiddenByEntity() != null && !this.isDestroyed();
   }

   public boolean canUpdateYaw(Entity player) {
      if (this.func_184187_bx() != null) {
         return false;
      } else if (this.getCountOnUpdate() < 30) {
         return false;
      } else {
         return MCH_Lib.getBlockIdY(this, 3, -2) == 0;
      }
   }

   public boolean canUpdatePitch(Entity player) {
      if (this.getCountOnUpdate() < 30) {
         return false;
      } else {
         return MCH_Lib.getBlockIdY(this, 3, -2) == 0;
      }
   }

   public boolean canUpdateRoll(Entity player) {
      if (this.func_184187_bx() != null) {
         return false;
      } else if (this.getCountOnUpdate() < 30) {
         return false;
      } else {
         return MCH_Lib.getBlockIdY(this, 3, -2) == 0;
      }
   }

   public boolean isOverridePlayerYaw() {
      return !this.isFreeLookMode();
   }

   public boolean isOverridePlayerPitch() {
      return !this.isFreeLookMode();
   }

   public double getAddRotationYawLimit() {
      return this.getAcInfo() != null ? 40.0D * (double)this.getAcInfo().mobilityYaw : 40.0D;
   }

   public double getAddRotationPitchLimit() {
      return this.getAcInfo() != null ? 40.0D * (double)this.getAcInfo().mobilityPitch : 40.0D;
   }

   public double getAddRotationRollLimit() {
      return this.getAcInfo() != null ? 40.0D * (double)this.getAcInfo().mobilityRoll : 40.0D;
   }

   public float getYawFactor() {
      return 1.0F;
   }

   public float getPitchFactor() {
      return 1.0F;
   }

   public float getRollFactor() {
      return 1.0F;
   }

   public abstract void onUpdateAngles(float var1);

   public float getControlRotYaw(float mouseX, float mouseY, float tick) {
      return 0.0F;
   }

   public float getControlRotPitch(float mouseX, float mouseY, float tick) {
      return 0.0F;
   }

   public float getControlRotRoll(float mouseX, float mouseY, float tick) {
      return 0.0F;
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
      double limit;
      if (this.canUpdateYaw(player)) {
         limit = this.getAddRotationYawLimit();
         yaw = this.getControlRotYaw(x, y, partialTicks);
         if ((double)yaw < -limit) {
            yaw = (float)(-limit);
         }

         if ((double)yaw > limit) {
            yaw = (float)limit;
         }

         yaw = (float)((double)(yaw * this.getYawFactor()) * 0.06D * (double)partialTicks);
      }

      if (this.canUpdatePitch(player)) {
         limit = this.getAddRotationPitchLimit();
         pitch = this.getControlRotPitch(x, y, partialTicks);
         if ((double)pitch < -limit) {
            pitch = (float)(-limit);
         }

         if ((double)pitch > limit) {
            pitch = (float)limit;
         }

         pitch = (float)((double)(-pitch * this.getPitchFactor()) * 0.06D * (double)partialTicks);
      }

      if (this.canUpdateRoll(player)) {
         limit = this.getAddRotationRollLimit();
         roll = this.getControlRotRoll(x, y, partialTicks);
         if ((double)roll < -limit) {
            roll = (float)(-limit);
         }

         if ((double)roll > limit) {
            roll = (float)limit;
         }

         roll = roll * this.getRollFactor() * 0.06F * partialTicks;
      }

      MCH_Math.FMatrix m_add = MCH_Math.newMatrix();
      MCH_Math.MatTurnZ(m_add, roll / 180.0F * 3.1415927F);
      MCH_Math.MatTurnX(m_add, pitch / 180.0F * 3.1415927F);
      MCH_Math.MatTurnY(m_add, yaw / 180.0F * 3.1415927F);
      MCH_Math.MatTurnZ(m_add, (float)((double)(this.getRotRoll() / 180.0F) * 3.141592653589793D));
      MCH_Math.MatTurnX(m_add, (float)((double)(this.getRotPitch() / 180.0F) * 3.141592653589793D));
      MCH_Math.MatTurnY(m_add, (float)((double)(this.getRotYaw() / 180.0F) * 3.141592653589793D));
      MCH_Math.FVector3D v = MCH_Math.MatrixToEuler(m_add);
      if (this.getAcInfo().limitRotation) {
         v.x = MCH_Lib.RNG(v.x, this.getAcInfo().minRotationPitch, this.getAcInfo().maxRotationPitch);
         v.z = MCH_Lib.RNG(v.z, this.getAcInfo().minRotationRoll, this.getAcInfo().maxRotationRoll);
      }

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
         v.x = MCH_Lib.RNG(this.getRotPitch(), this.getAcInfo().minRotationPitch, this.getAcInfo().maxRotationPitch);
         v.z = MCH_Lib.RNG(this.getRotRoll(), this.getAcInfo().minRotationRoll, this.getAcInfo().maxRotationRoll);
         this.setRotPitch(v.x);
         this.setRotRoll(v.z);
      }

      if (MathHelper.func_76135_e(this.getRotPitch()) > 90.0F) {
         MCH_Lib.DbgLog(true, "MCH_EntityAircraft.setAngles Error:Pitch=%.1f", this.getRotPitch());
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

      if (!this.isOverridePlayerYaw() && !fixRot) {
         player.func_70082_c(deltaX, 0.0F);
      } else {
         if (this.func_184187_bx() == null) {
            player.field_70126_B = this.getRotYaw() + (fixRot ? fixYaw : 0.0F);
         } else {
            if (this.getRotYaw() - player.field_70177_z > 180.0F) {
               player.field_70126_B += 360.0F;
            }

            if (this.getRotYaw() - player.field_70177_z < -180.0F) {
               player.field_70126_B -= 360.0F;
            }
         }

         player.field_70177_z = this.getRotYaw() + (fixRot ? fixYaw : 0.0F);
      }

      if (!this.isOverridePlayerPitch() && !fixRot) {
         player.func_70082_c(0.0F, deltaY);
      } else {
         player.field_70127_C = this.getRotPitch() + (fixRot ? fixPitch : 0.0F);
         player.field_70125_A = this.getRotPitch() + (fixRot ? fixPitch : 0.0F);
      }

      if (this.func_184187_bx() == null && ac_yaw != this.getRotYaw() || ac_pitch != this.getRotPitch() || ac_roll != this.getRotRoll()) {
         this.aircraftRotChanged = true;
      }

   }

   public boolean canSwitchSearchLight(Entity entity) {
      return this.haveSearchLight() && this.getSeatIdByEntity(entity) <= 1;
   }

   public boolean isSearchLightON() {
      return this.getCommonStatus(6);
   }

   public void setSearchLight(boolean onoff) {
      this.setCommonStatus(6, onoff);
   }

   public boolean haveSearchLight() {
      return this.getAcInfo() != null && this.getAcInfo().searchLights.size() > 0;
   }

   public float getSearchLightValue(Entity entity) {
      if (this.haveSearchLight() && this.isSearchLightON()) {
         Iterator var2 = this.getAcInfo().searchLights.iterator();

         while(var2.hasNext()) {
            MCH_AircraftInfo.SearchLight sl = (MCH_AircraftInfo.SearchLight)var2.next();
            Vec3d pos = this.getTransformedPosition(sl.pos);
            double dist = entity.func_70092_e(pos.x, pos.y, pos.z);
            if (dist > 2.0D && dist < (double)(sl.height * sl.height + 20.0F)) {
               double cx = entity.posX - pos.x;
               double cy = entity.posY - pos.y;
               double cz = entity.posZ - pos.z;
               double h = 0.0D;
               double v = 0.0D;
               float angle;
               if (!sl.fixDir) {
                  Vec3d vv = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -this.lastSearchLightYaw + sl.yaw, -this.lastSearchLightPitch + sl.pitch, -this.getRotRoll());
                  h = (double)MCH_Lib.getPosAngle(vv.x, vv.z, cx, cz);
                  v = Math.atan2(cy, Math.sqrt(cx * cx + cz * cz)) * 180.0D / 3.141592653589793D;
                  v = Math.abs(v + (double)this.lastSearchLightPitch + (double)sl.pitch);
               } else {
                  angle = 0.0F;
                  if (sl.steering) {
                     angle = this.rotYawWheel * sl.stRot;
                  }

                  Vec3d vv = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -this.getRotYaw() + sl.yaw + angle, -this.getRotPitch() + sl.pitch, -this.getRotRoll());
                  h = (double)MCH_Lib.getPosAngle(vv.x, vv.z, cx, cz);
                  v = Math.atan2(cy, Math.sqrt(cx * cx + cz * cz)) * 180.0D / 3.141592653589793D;
                  v = Math.abs(v + (double)this.getRotPitch() + (double)sl.pitch);
               }

               angle = sl.angle * 3.0F;
               if (h < (double)angle && v < (double)angle) {
                  float value = 0.0F;
                  if (h + v < (double)angle) {
                     value = (float)(1440.0D * (1.0D - (h + v) / (double)angle));
                  }

                  return value <= 240.0F ? value : 240.0F;
               }
            }
         }
      }

      return 0.0F;
   }

   public abstract void onUpdateAircraft();

   public void func_70071_h_() {
      if (this.getCountOnUpdate() < 2) {
         this.prevPosition.clear(new Vec3d(this.posX, this.posY, this.posZ));
      }

      this.prevCurrentThrottle = this.getCurrentThrottle();
      this.lastBBDamageFactor = 1.0F;
      this.updateControl();
      this.checkServerNoMove();
      this.onUpdate_RidingEntity();
      Iterator itr = this.listUnmountReserve.iterator();

      while(itr.hasNext()) {
         MCH_EntityAircraft.UnmountReserve ur = (MCH_EntityAircraft.UnmountReserve)itr.next();
         if (ur.entity != null && !ur.entity.field_70128_L) {
            ur.entity.func_70107_b(ur.posX, ur.posY, ur.posZ);
            ur.entity.field_70143_R = this.field_70143_R;
         }

         if (ur.cnt > 0) {
            --ur.cnt;
         }

         if (ur.cnt == 0) {
            itr.remove();
         }
      }

      if (this.isDestroyed() && this.getCountOnUpdate() % 20 == 0) {
         for(int sid = 0; sid < this.getSeatNum() + 1; ++sid) {
            Entity entity = this.getEntityBySeatId(sid);
            if (entity != null && (sid != 0 || !this.isUAV()) && MCH_Config.applyDamageVsEntity(entity, DamageSource.field_76372_a, 1.0F) > 0.0F) {
               entity.func_70015_d(5);
            }
         }
      }

      if ((this.aircraftRotChanged || this.aircraftRollRev) && this.world.isRemote && this.getRiddenByEntity() != null) {
         MCH_PacketIndRotation.send(this);
         this.aircraftRotChanged = false;
         this.aircraftRollRev = false;
      }

      if (!this.world.isRemote && (int)this.prevRotationRoll != (int)this.getRotRoll()) {
         float roll = MathHelper.func_76142_g(this.getRotRoll());
         this.dataManager.func_187227_b(ROT_ROLL, (int)roll);
      }

      this.prevRotationRoll = this.getRotRoll();
      if (!this.world.isRemote && this.isTargetDrone() && !this.isDestroyed() && this.getCountOnUpdate() > 20 && !this.canUseFuel()) {
         this.setDamageTaken(this.getMaxHP());
         this.destroyAircraft();
         MCH_Explosion.newExplosion(this.world, (Entity)null, (Entity)null, this.posX, this.posY, this.posZ, 2.0F, 2.0F, true, true, true, true, 5);
      }

      if (this.world.isRemote && this.getAcInfo() != null && this.getHP() <= 0 && this.getDespawnCount() <= 0) {
         this.destroyAircraft();
      }

      if (!this.world.isRemote && this.getDespawnCount() > 0) {
         this.setDespawnCount(this.getDespawnCount() - 1);
         if (this.getDespawnCount() <= 1) {
            this.setDead(true);
         }
      }

      super.func_70071_h_();
      int ft;
      if (this.func_70021_al() != null) {
         Entity[] var10 = this.func_70021_al();
         ft = var10.length;

         for(int var4 = 0; var4 < ft; ++var4) {
            Entity e = var10[var4];
            if (e != null) {
               e.func_70071_h_();
            }
         }
      }

      this.updateNoCollisionEntities();
      this.updateUAV();
      this.supplyFuel();
      this.supplyAmmoToOtherAircraft();
      this.updateFuel();
      this.repairOtherAircraft();
      if (this.modeSwitchCooldown > 0) {
         --this.modeSwitchCooldown;
      }

      if (this.lastRiddenByEntity == null && this.getRiddenByEntity() != null) {
         this.onRidePilotFirstUpdate();
      }

      if (this.countOnUpdate == 0) {
         this.onFirstUpdate();
      }

      ++this.countOnUpdate;
      if (this.countOnUpdate >= 1000000) {
         this.countOnUpdate = 1;
      }

      if (this.world.isRemote) {
         this.commonStatus = (Integer)this.dataManager.func_187225_a(STATUS);
      }

      this.field_70143_R = 0.0F;
      Entity riddenByEntity = this.getRiddenByEntity();
      if (riddenByEntity != null) {
         riddenByEntity.field_70143_R = 0.0F;
      }

      if (this.missileDetector != null) {
         this.missileDetector.update();
      }

      if (this.soundUpdater != null) {
         this.soundUpdater.update();
      }

      if (this.getTowChainEntity() != null && this.getTowChainEntity().field_70128_L) {
         this.setTowChainEntity((MCH_EntityChain)null);
      }

      this.updateSupplyAmmo();
      this.autoRepair();
      ft = this.getFlareTick();
      this.flareDv.update();
      if (!this.world.isRemote && this.getFlareTick() == 0 && ft != 0) {
         this.setCommonStatus(0, false);
      }

      Entity e = this.getRiddenByEntity();
      if (e != null && !e.field_70128_L && !this.isDestroyed()) {
         this.lastRiderYaw = e.field_70177_z;
         this.prevLastRiderYaw = e.field_70126_B;
         this.lastRiderPitch = e.field_70125_A;
         this.prevLastRiderPitch = e.field_70127_C;
      } else if (this.getTowedChainEntity() != null || this.func_184187_bx() != null) {
         this.lastRiderYaw = this.field_70177_z;
         this.prevLastRiderYaw = this.field_70126_B;
         this.lastRiderPitch = this.field_70125_A;
         this.prevLastRiderPitch = this.field_70127_C;
      }

      this.updatePartCameraRotate();
      this.updatePartWheel();
      this.updatePartCrawlerTrack();
      this.updatePartLightHatch();
      this.regenerationMob();
      if (this.getRiddenByEntity() == null && this.lastRiddenByEntity != null) {
         this.unmountEntity();
      }

      this.updateExtraBoundingBox();
      boolean prevOnGround = this.field_70122_E;
      double prevMotionY = this.field_70181_x;
      this.onUpdateAircraft();
      if (this.getAcInfo() != null) {
         this.updateParts(this.getPartStatus());
      }

      if (this.recoilCount > 0) {
         --this.recoilCount;
      }

      if (!W_Entity.isEqual(MCH_MOD.proxy.getClientPlayer(), this.getRiddenByEntity())) {
         this.updateRecoil(1.0F);
      }

      if (!this.world.isRemote && this.isDestroyed() && !this.isExploded() && !prevOnGround && this.field_70122_E && prevMotionY < -0.2D) {
         this.explosionByCrash(prevMotionY);
         this.damageSinceDestroyed = this.getMaxHP();
      }

      this.onUpdate_PartRotation();
      this.onUpdate_ParticleSmoke();
      this.updateSeatsPosition(this.posX, this.posY, this.posZ, false);
      this.updateHitBoxPosition();
      this.onUpdate_CollisionGroundDamage();
      this.onUpdate_UnmountCrew();
      this.onUpdate_Repelling();
      this.checkRideRack();
      if (this.lastRidingEntity == null && this.func_184187_bx() != null) {
         this.onRideEntity(this.func_184187_bx());
      }

      this.lastRiddenByEntity = this.getRiddenByEntity();
      this.lastRidingEntity = this.func_184187_bx();
      this.prevPosition.put(new Vec3d(this.posX, this.posY, this.posZ));
   }

   private void updateNoCollisionEntities() {
      if (!this.world.isRemote) {
         if (this.getCountOnUpdate() % 10 == 0) {
            Entity key;
            for(int i = 0; i < 1 + this.getSeatNum(); ++i) {
               key = this.getEntityBySeatId(i);
               if (key != null) {
                  this.noCollisionEntities.put(key, 8);
               }
            }

            if (this.getTowChainEntity() != null && this.getTowChainEntity().towedEntity != null) {
               this.noCollisionEntities.put(this.getTowChainEntity().towedEntity, 60);
            }

            if (this.getTowedChainEntity() != null && this.getTowedChainEntity().towEntity != null) {
               this.noCollisionEntities.put(this.getTowedChainEntity().towEntity, 60);
            }

            if (this.func_184187_bx() instanceof MCH_EntitySeat) {
               MCH_EntityAircraft ac = ((MCH_EntitySeat)this.func_184187_bx()).getParent();
               if (ac != null) {
                  this.noCollisionEntities.put(ac, 60);
               }
            } else if (this.func_184187_bx() != null) {
               this.noCollisionEntities.put(this.func_184187_bx(), 60);
            }

            Iterator key = this.noCollisionEntities.keySet().iterator();

            while(key.hasNext()) {
               key = (Entity)key.next();
               this.noCollisionEntities.put(key, (Integer)this.noCollisionEntities.get(key) - 1);
            }

            key = this.noCollisionEntities.values().iterator();

            while(key.hasNext()) {
               if ((Integer)key.next() <= 0) {
                  key.remove();
               }
            }

         }
      }
   }

   public void updateControl() {
      if (!this.world.isRemote) {
         this.setCommonStatus(7, this.moveLeft);
         this.setCommonStatus(8, this.moveRight);
         this.setCommonStatus(9, this.throttleUp);
         this.setCommonStatus(10, this.throttleDown);
      } else if (MCH_MOD.proxy.getClientPlayer() != this.getRiddenByEntity()) {
         this.moveLeft = this.getCommonStatus(7);
         this.moveRight = this.getCommonStatus(8);
         this.throttleUp = this.getCommonStatus(9);
         this.throttleDown = this.getCommonStatus(10);
      }

   }

   public void updateRecoil(float partialTicks) {
      if (this.recoilCount > 0 && this.recoilCount >= 12) {
         float pitch = MathHelper.func_76134_b((float)((double)(this.recoilYaw - this.getRotRoll()) * 3.141592653589793D / 180.0D));
         float roll = MathHelper.func_76126_a((float)((double)(this.recoilYaw - this.getRotRoll()) * 3.141592653589793D / 180.0D));
         float recoil = MathHelper.func_76134_b((float)((double)(this.recoilCount * 6) * 3.141592653589793D / 180.0D)) * this.recoilValue;
         this.setRotPitch(this.getRotPitch() + recoil * pitch * partialTicks);
         this.setRotRoll(this.getRotRoll() + recoil * roll * partialTicks);
      }

   }

   private void updatePartLightHatch() {
      this.prevRotLightHatch = this.rotLightHatch;
      if (this.isSearchLightON()) {
         this.rotLightHatch = (float)((double)this.rotLightHatch + 0.5D);
      } else {
         this.rotLightHatch = (float)((double)this.rotLightHatch - 0.5D);
      }

      if (this.rotLightHatch > 1.0F) {
         this.rotLightHatch = 1.0F;
      }

      if (this.rotLightHatch < 0.0F) {
         this.rotLightHatch = 0.0F;
      }

   }

   public void updateExtraBoundingBox() {
      MCH_BoundingBox[] var1 = this.extraBoundingBox;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MCH_BoundingBox bb = var1[var3];
         bb.updatePosition(this.posX, this.posY, this.posZ, this.getRotYaw(), this.getRotPitch(), this.getRotRoll());
      }

   }

   public void updatePartWheel() {
      if (this.world.isRemote) {
         if (this.getAcInfo() != null) {
            this.prevRotWheel = this.rotWheel;
            this.prevRotYawWheel = this.rotYawWheel;
            double throttle = this.getCurrentThrottle();
            double pivotTurnThrottle = (double)this.getAcInfo().pivotTurnThrottle;
            if (pivotTurnThrottle <= 0.0D) {
               pivotTurnThrottle = 1.0D;
            } else {
               pivotTurnThrottle *= 0.10000000149011612D;
            }

            boolean localMoveLeft = this.moveLeft;
            boolean localMoveRight = this.moveRight;
            if (this.getAcInfo().enableBack && (double)this.throttleBack > 0.01D && throttle <= 0.0D) {
               throttle = (double)(-this.throttleBack * 15.0F);
            }

            if (localMoveLeft && !localMoveRight) {
               this.rotYawWheel += 0.1F;
               if (this.rotYawWheel > 1.0F) {
                  this.rotYawWheel = 1.0F;
               }
            } else if (!localMoveLeft && localMoveRight) {
               this.rotYawWheel -= 0.1F;
               if (this.rotYawWheel < -1.0F) {
                  this.rotYawWheel = -1.0F;
               }
            } else {
               this.rotYawWheel *= 0.9F;
            }

            this.rotWheel = (float)((double)this.rotWheel + throttle * (double)this.getAcInfo().partWheelRot);
            if (this.rotWheel >= 360.0F) {
               this.rotWheel -= 360.0F;
               this.prevRotWheel -= 360.0F;
            } else if (this.rotWheel < 0.0F) {
               this.rotWheel += 360.0F;
               this.prevRotWheel += 360.0F;
            }

         }
      }
   }

   public void updatePartCrawlerTrack() {
      if (this.world.isRemote) {
         if (this.getAcInfo() != null) {
            this.prevRotTrackRoller[0] = this.rotTrackRoller[0];
            this.prevRotTrackRoller[1] = this.rotTrackRoller[1];
            this.prevRotCrawlerTrack[0] = this.rotCrawlerTrack[0];
            this.prevRotCrawlerTrack[1] = this.rotCrawlerTrack[1];
            double throttle = this.getCurrentThrottle();
            double pivotTurnThrottle = (double)this.getAcInfo().pivotTurnThrottle;
            if (pivotTurnThrottle <= 0.0D) {
               pivotTurnThrottle = 1.0D;
            } else {
               pivotTurnThrottle *= 0.10000000149011612D;
            }

            boolean localMoveLeft = this.moveLeft;
            boolean localMoveRight = this.moveRight;
            int dir = 1;
            if (this.getAcInfo().enableBack && this.throttleBack > 0.0F && throttle <= 0.0D) {
               throttle = (double)(-this.throttleBack * 5.0F);
               if (localMoveLeft != localMoveRight) {
                  boolean tmp = localMoveLeft;
                  localMoveLeft = localMoveRight;
                  localMoveRight = tmp;
                  dir = -1;
               }
            }

            float[] tmp305_301;
            byte tmp317_316;
            float[] tmp317_313;
            byte tmp305_304;
            if (localMoveLeft && !localMoveRight) {
               throttle = 0.2D * (double)dir;
               tmp305_304 = 0;
               tmp305_301 = this.throttleCrawlerTrack;
               tmp305_301[tmp305_304] = (float)((double)tmp305_301[tmp305_304] + throttle);
               tmp317_316 = 1;
               tmp317_313 = this.throttleCrawlerTrack;
               tmp317_313[tmp317_316] = (float)((double)tmp317_313[tmp317_316] - pivotTurnThrottle * throttle);
            } else if (!localMoveLeft && localMoveRight) {
               throttle = 0.2D * (double)dir;
               tmp305_304 = 0;
               tmp305_301 = this.throttleCrawlerTrack;
               tmp305_301[tmp305_304] = (float)((double)tmp305_301[tmp305_304] - pivotTurnThrottle * throttle);
               tmp317_316 = 1;
               tmp317_313 = this.throttleCrawlerTrack;
               tmp317_313[tmp317_316] = (float)((double)tmp317_313[tmp317_316] + throttle);
            } else {
               if (throttle > 0.2D) {
                  throttle = 0.2D;
               }

               if (throttle < -0.2D) {
                  throttle = -0.2D;
               }

               tmp305_304 = 0;
               tmp305_301 = this.throttleCrawlerTrack;
               tmp305_301[tmp305_304] = (float)((double)tmp305_301[tmp305_304] + throttle);
               tmp317_316 = 1;
               tmp317_313 = this.throttleCrawlerTrack;
               tmp317_313[tmp317_316] = (float)((double)tmp317_313[tmp317_316] + throttle);
            }

            for(int i = 0; i < 2; ++i) {
               if (this.throttleCrawlerTrack[i] < -0.72F) {
                  this.throttleCrawlerTrack[i] = -0.72F;
               } else if (this.throttleCrawlerTrack[i] > 0.72F) {
                  this.throttleCrawlerTrack[i] = 0.72F;
               }

               float[] var10000 = this.rotTrackRoller;
               var10000[i] += this.throttleCrawlerTrack[i] * this.getAcInfo().trackRollerRot;
               if (this.rotTrackRoller[i] >= 360.0F) {
                  var10000 = this.rotTrackRoller;
                  var10000[i] -= 360.0F;
                  var10000 = this.prevRotTrackRoller;
                  var10000[i] -= 360.0F;
               } else if (this.rotTrackRoller[i] < 0.0F) {
                  var10000 = this.rotTrackRoller;
                  var10000[i] += 360.0F;
                  var10000 = this.prevRotTrackRoller;
                  var10000[i] += 360.0F;
               }

               var10000 = this.rotCrawlerTrack;

               int var10002;
               for(var10000[i] -= this.throttleCrawlerTrack[i]; this.rotCrawlerTrack[i] >= 1.0F; var10002 = this.prevRotCrawlerTrack[i]--) {
                  var10002 = this.rotCrawlerTrack[i]--;
               }

               while(this.rotCrawlerTrack[i] < 0.0F) {
                  var10002 = this.rotCrawlerTrack[i]++;
               }

               while(this.prevRotCrawlerTrack[i] < 0.0F) {
                  var10002 = this.prevRotCrawlerTrack[i]++;
               }

               float[] tmp602_597 = this.throttleCrawlerTrack;
               tmp602_597[i] = (float)((double)tmp602_597[i] * 0.75D);
            }

         }
      }
   }

   public void checkServerNoMove() {
      if (!this.world.isRemote) {
         double moti = this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y;
         if (moti < 1.0E-4D) {
            if (this.serverNoMoveCount < 20) {
               ++this.serverNoMoveCount;
               if (this.serverNoMoveCount >= 20) {
                  this.serverNoMoveCount = 0;
                  if (this.world instanceof WorldServer) {
                     ((WorldServer)this.world).func_73039_n().func_151247_a(this, new SPacketEntityVelocity(this.getEntityId(), 0.0D, 0.0D, 0.0D));
                  }
               }
            }
         } else {
            this.serverNoMoveCount = 0;
         }
      }

   }

   public boolean haveRotPart() {
      return this.world.isRemote && this.getAcInfo() != null && this.rotPartRotation.length > 0 && this.rotPartRotation.length == this.getAcInfo().partRotPart.size();
   }

   public void onUpdate_PartRotation() {
      if (this.haveRotPart()) {
         for(int i = 0; i < this.rotPartRotation.length; ++i) {
            this.prevRotPartRotation[i] = this.rotPartRotation[i];
            if (!this.isDestroyed() && ((MCH_AircraftInfo.RotPart)this.getAcInfo().partRotPart.get(i)).rotAlways || this.getRiddenByEntity() != null) {
               float[] var10000 = this.rotPartRotation;
               var10000[i] += ((MCH_AircraftInfo.RotPart)this.getAcInfo().partRotPart.get(i)).rotSpeed;
               if (this.rotPartRotation[i] < 0.0F) {
                  var10000 = this.rotPartRotation;
                  var10000[i] += 360.0F;
               }

               if (this.rotPartRotation[i] >= 360.0F) {
                  var10000 = this.rotPartRotation;
                  var10000[i] -= 360.0F;
               }
            }
         }
      }

   }

   public void onRideEntity(Entity ridingEntity) {
   }

   public int getAlt(double px, double py, double pz) {
      int i;
      for(i = 0; i < 256 && !(py - (double)i <= 0.0D) && (!(py - (double)i < 256.0D) || 0 == W_WorldFunc.getBlockId(this.world, (int)px, (int)py - i, (int)pz)); ++i) {
      }

      return i;
   }

   public boolean canRepelling(Entity entity) {
      return this.isRepelling() && this.tickRepelling > 50;
   }

   private void onUpdate_Repelling() {
      if (this.getAcInfo() != null && this.getAcInfo().haveRepellingHook()) {
         if (this.isRepelling()) {
            int alt = this.getAlt(this.posX, this.posY, this.posZ);
            if (this.ropesLength > -50.0F && this.ropesLength > (float)(-alt)) {
               this.ropesLength = (float)((double)this.ropesLength - (this.world.isRemote ? 0.30000001192092896D : 0.25D));
            }
         } else {
            this.ropesLength = 0.0F;
         }
      }

      this.onUpdate_UnmountCrewRepelling();
   }

   private void onUpdate_UnmountCrewRepelling() {
      if (this.getAcInfo() != null) {
         if (!this.isRepelling()) {
            this.tickRepelling = 0;
         } else if (this.tickRepelling < 60) {
            ++this.tickRepelling;
         } else if (!this.world.isRemote) {
            for(int ropeIdx = 0; ropeIdx < this.getAcInfo().repellingHooks.size(); ++ropeIdx) {
               MCH_AircraftInfo.RepellingHook hook = (MCH_AircraftInfo.RepellingHook)this.getAcInfo().repellingHooks.get(ropeIdx);
               if (this.getCountOnUpdate() % hook.interval == 0) {
                  for(int i = 1; i < this.getSeatNum(); ++i) {
                     MCH_EntitySeat seat = this.getSeat(i);
                     if (seat != null && seat.getRiddenByEntity() != null && !W_EntityPlayer.isPlayer(seat.getRiddenByEntity()) && !(seat.getRiddenByEntity() instanceof MCH_EntityGunner) && !(this.getSeatInfo(i + 1) instanceof MCH_SeatRackInfo)) {
                        Entity entity = seat.getRiddenByEntity();
                        Vec3d dropPos = this.getTransformedPosition(hook.pos, (Vec3d)this.prevPosition.oldest());
                        seat.posX = dropPos.x;
                        seat.posY = dropPos.y - 2.0D;
                        seat.posZ = dropPos.z;
                        entity.func_184210_p();
                        this.unmountEntityRepelling(entity, dropPos, ropeIdx);
                        this.lastUsedRopeIndex = ropeIdx;
                        break;
                     }
                  }
               }
            }

         }
      }
   }

   public void unmountEntityRepelling(Entity entity, Vec3d dropPos, int ropeIdx) {
      entity.posX = dropPos.x;
      entity.posY = dropPos.y - 2.0D;
      entity.posZ = dropPos.z;
      MCH_EntityHide hideEntity = new MCH_EntityHide(this.world, entity.posX, entity.posY, entity.posZ);
      hideEntity.setParent(this, entity, ropeIdx);
      hideEntity.field_70159_w = entity.field_70159_w = 0.0D;
      hideEntity.field_70181_x = entity.field_70181_x = 0.0D;
      hideEntity.field_70179_y = entity.field_70179_y = 0.0D;
      hideEntity.field_70143_R = entity.field_70143_R = 0.0F;
      this.world.func_72838_d(hideEntity);
   }

   private void onUpdate_UnmountCrew() {
      if (this.getAcInfo() != null) {
         if (this.isParachuting) {
            if (MCH_Lib.getBlockIdY(this, 3, -10) != 0) {
               this.stopUnmountCrew();
            } else if ((!this.haveHatch() || this.getHatchRotation() > 89.0F) && this.getCountOnUpdate() % this.getAcInfo().mobDropOption.interval == 0 && !this.unmountCrew(true)) {
               this.stopUnmountCrew();
            }
         }

      }
   }

   public void unmountAircraft() {
      Vec3d v = new Vec3d(this.posX, this.posY, this.posZ);
      if (this.func_184187_bx() instanceof MCH_EntitySeat) {
         MCH_EntityAircraft ac = ((MCH_EntitySeat)this.func_184187_bx()).getParent();
         MCH_SeatInfo seatInfo = ac.getSeatInfo(this);
         if (seatInfo instanceof MCH_SeatRackInfo) {
            v = ((MCH_SeatRackInfo)seatInfo).getEntryPos();
            v = ac.getTransformedPosition(v);
         }
      } else if (this.func_184187_bx() instanceof EntityMinecartEmpty) {
         this.dismountedUserCtrl = true;
      }

      this.func_70012_b(v.x, v.y, v.z, this.getRotYaw(), this.getRotPitch());
      this.func_184210_p();
      this.func_70012_b(v.x, v.y, v.z, this.getRotYaw(), this.getRotPitch());
   }

   public boolean canUnmount(Entity entity) {
      if (this.getAcInfo() == null) {
         return false;
      } else if (!this.getAcInfo().isEnableParachuting) {
         return false;
      } else if (this.getSeatIdByEntity(entity) <= 1) {
         return false;
      } else {
         return !this.haveHatch() || !(this.getHatchRotation() < 89.0F);
      }
   }

   public void unmount(Entity entity) {
      if (this.getAcInfo() != null) {
         MCH_EntitySeat seat;
         Vec3d dropPos;
         if (this.canRepelling(entity) && this.getAcInfo().haveRepellingHook()) {
            seat = this.getSeatByEntity(entity);
            if (seat != null) {
               this.lastUsedRopeIndex = (this.lastUsedRopeIndex + 1) % this.getAcInfo().repellingHooks.size();
               dropPos = this.getTransformedPosition(((MCH_AircraftInfo.RepellingHook)this.getAcInfo().repellingHooks.get(this.lastUsedRopeIndex)).pos, (Vec3d)this.prevPosition.oldest());
               dropPos = dropPos.func_72441_c(0.0D, -2.0D, 0.0D);
               seat.posX = dropPos.x;
               seat.posY = dropPos.y;
               seat.posZ = dropPos.z;
               entity.func_184210_p();
               entity.posX = dropPos.x;
               entity.posY = dropPos.y;
               entity.posZ = dropPos.z;
               this.unmountEntityRepelling(entity, dropPos, this.lastUsedRopeIndex);
            } else {
               MCH_Lib.Log((Entity)this, "Error:MCH_EntityAircraft.unmount seat=null : " + entity);
            }
         } else if (this.canUnmount(entity)) {
            seat = this.getSeatByEntity(entity);
            if (seat != null) {
               dropPos = this.getTransformedPosition(this.getAcInfo().mobDropOption.pos, (Vec3d)this.prevPosition.oldest());
               seat.posX = dropPos.x;
               seat.posY = dropPos.y;
               seat.posZ = dropPos.z;
               entity.func_184210_p();
               entity.posX = dropPos.x;
               entity.posY = dropPos.y;
               entity.posZ = dropPos.z;
               this.dropEntityParachute(entity);
            } else {
               MCH_Lib.Log((Entity)this, "Error:MCH_EntityAircraft.unmount seat=null : " + entity);
            }
         }

      }
   }

   public boolean canParachuting(Entity entity) {
      if (this.getAcInfo() != null && this.getAcInfo().isEnableParachuting && this.getSeatIdByEntity(entity) > 1 && MCH_Lib.getBlockIdY(this, 3, -13) == 0) {
         if (this.haveHatch() && this.getHatchRotation() > 89.0F) {
            return this.getSeatIdByEntity(entity) > 1;
         } else {
            return this.getSeatIdByEntity(entity) > 1;
         }
      } else {
         return false;
      }
   }

   public void onUpdate_RidingEntity() {
      if (!this.world.isRemote && this.waitMountEntity == 0 && this.getCountOnUpdate() > 20 && this.canMountWithNearEmptyMinecart()) {
         this.mountWithNearEmptyMinecart();
      }

      if (this.waitMountEntity > 0) {
         --this.waitMountEntity;
      }

      if (!this.world.isRemote && this.func_184187_bx() != null) {
         this.setRotRoll(this.getRotRoll() * 0.9F);
         this.setRotPitch(this.getRotPitch() * 0.95F);
         Entity re = this.func_184187_bx();
         float target = MathHelper.func_76142_g(re.field_70177_z + 90.0F);
         if (target - this.field_70177_z > 180.0F) {
            target -= 360.0F;
         }

         if (target - this.field_70177_z < -180.0F) {
            target += 360.0F;
         }

         if (this.field_70173_aa % 2 == 0) {
         }

         float dist = 50.0F * (float)re.func_70092_e(re.field_70169_q, re.field_70167_r, re.field_70166_s);
         if ((double)dist > 0.001D) {
            dist = MathHelper.func_76129_c(dist);
            float distYaw = MCH_Lib.RNG(target - this.field_70177_z, -dist, dist);
            this.field_70177_z += distYaw;
         }

         double bkPosX = this.posX;
         double bkPosY = this.posY;
         double bkPosZ = this.posZ;
         if (this.func_184187_bx().field_70128_L) {
            this.func_184210_p();
            this.waitMountEntity = 20;
         } else if (this.getCurrentThrottle() > 0.8D) {
            this.field_70159_w = this.func_184187_bx().field_70159_w;
            this.field_70181_x = this.func_184187_bx().field_70181_x;
            this.field_70179_y = this.func_184187_bx().field_70179_y;
            this.func_184210_p();
            this.waitMountEntity = 20;
         }

         this.posX = bkPosX;
         this.posY = bkPosY;
         this.posZ = bkPosZ;
      }

   }

   public void explosionByCrash(double prevMotionY) {
      float exp = this.getAcInfo() != null ? (float)this.getAcInfo().maxFuel / 400.0F : 2.0F;
      if (exp < 1.0F) {
         exp = 1.0F;
      }

      if (exp > 15.0F) {
         exp = 15.0F;
      }

      MCH_Lib.DbgLog(this.world, "OnGroundAfterDestroyed:motionY=%.3f", (float)prevMotionY);
      MCH_Explosion.newExplosion(this.world, (Entity)null, (Entity)null, this.posX, this.posY, this.posZ, exp, exp >= 2.0F ? exp * 0.5F : 1.0F, true, true, true, true, 5);
   }

   public void onUpdate_CollisionGroundDamage() {
      if (!this.isDestroyed()) {
         if (MCH_Lib.getBlockIdY(this, 3, -3) > 0 && !this.world.isRemote) {
            float roll = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotRoll()));
            float pitch = MathHelper.func_76135_e(MathHelper.func_76142_g(this.getRotPitch()));
            if (roll > this.getGiveDamageRot() || pitch > this.getGiveDamageRot()) {
               float dmg = MathHelper.func_76135_e(roll) + MathHelper.func_76135_e(pitch);
               if (dmg < 90.0F) {
                  dmg *= 0.4F * (float)this.func_70011_f(this.field_70169_q, this.field_70167_r, this.field_70166_s);
               } else {
                  dmg *= 0.4F;
               }

               if (dmg > 1.0F && this.field_70146_Z.nextInt(4) == 0) {
                  this.func_70097_a(DamageSource.field_76368_d, dmg);
               }
            }
         }

         if (this.getCountOnUpdate() % 30 == 0 && (this.getAcInfo() == null || !this.getAcInfo().isFloat) && MCH_Lib.isBlockInWater(this.world, (int)(this.posX + 0.5D), (int)(this.posY + 1.5D + (double)this.getAcInfo().submergedDamageHeight), (int)(this.posZ + 0.5D))) {
            int hp = this.getMaxHP() / 10;
            if (hp <= 0) {
               hp = 1;
            }

            this.attackEntityFrom(DamageSource.field_76368_d, hp);
         }

      }
   }

   public float getGiveDamageRot() {
      return 40.0F;
   }

   public void applyServerPositionAndRotation() {
      double rpinc = (double)this.aircraftPosRotInc;
      double yaw = MathHelper.func_76138_g(this.aircraftYaw - (double)this.getRotYaw());
      double roll = (double)MathHelper.func_76142_g(this.getServerRoll() - this.getRotRoll());
      if (!this.isDestroyed() && (!W_Lib.isClientPlayer(this.getRiddenByEntity()) || this.func_184187_bx() != null)) {
         this.setRotYaw((float)((double)this.getRotYaw() + yaw / rpinc));
         this.setRotPitch((float)((double)this.getRotPitch() + (this.aircraftPitch - (double)this.getRotPitch()) / rpinc));
         this.setRotRoll((float)((double)this.getRotRoll() + roll / rpinc));
      }

      this.func_70107_b(this.posX + (this.aircraftX - this.posX) / rpinc, this.posY + (this.aircraftY - this.posY) / rpinc, this.posZ + (this.aircraftZ - this.posZ) / rpinc);
      this.func_70101_b(this.getRotYaw(), this.getRotPitch());
      --this.aircraftPosRotInc;
   }

   protected void autoRepair() {
      if (this.timeSinceHit > 0) {
         --this.timeSinceHit;
      }

      if (this.getMaxHP() > 0) {
         if (!this.isDestroyed()) {
            if (this.getDamageTaken() > this.beforeDamageTaken) {
               this.repairCount = 600;
            } else if (this.repairCount > 0) {
               --this.repairCount;
            } else {
               this.repairCount = 40;
               double hpp = (double)(this.getHP() / this.getMaxHP());
               if (hpp >= MCH_Config.AutoRepairHP.prmDouble) {
                  this.repair(this.getMaxHP() / 100);
               }
            }
         }

         this.beforeDamageTaken = this.getDamageTaken();
      }
   }

   public boolean repair(int tpd) {
      if (tpd < 1) {
         tpd = 1;
      }

      int damage = this.getDamageTaken();
      if (damage > 0) {
         if (!this.world.isRemote) {
            this.setDamageTaken(damage - tpd);
         }

         return true;
      } else {
         return false;
      }
   }

   public void repairOtherAircraft() {
      float range = this.getAcInfo() != null ? this.getAcInfo().repairOtherVehiclesRange : 0.0F;
      if (!(range <= 0.0F)) {
         if (!this.world.isRemote && this.getCountOnUpdate() % 20 == 0) {
            List<MCH_EntityAircraft> list = this.world.func_72872_a(MCH_EntityAircraft.class, this.func_70046_E().func_72314_b((double)range, (double)range, (double)range));

            for(int i = 0; i < list.size(); ++i) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
               if (!W_Entity.isEqual(this, ac) && ac.getHP() < ac.getMaxHP()) {
                  ac.setDamageTaken(ac.getDamageTaken() - this.getAcInfo().repairOtherVehiclesValue);
               }
            }
         }

      }
   }

   protected void regenerationMob() {
      if (!this.isDestroyed()) {
         if (!this.world.isRemote) {
            if (this.getAcInfo() != null && this.getAcInfo().regeneration && this.getRiddenByEntity() != null) {
               MCH_EntitySeat[] st = this.getSeats();
               MCH_EntitySeat[] var2 = st;
               int var3 = st.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  MCH_EntitySeat s = var2[var4];
                  if (s != null && !s.field_70128_L) {
                     Entity e = s.getRiddenByEntity();
                     if (W_Lib.isEntityLivingBase(e) && !e.field_70128_L) {
                        PotionEffect pe = W_Entity.getActivePotionEffect(e, MobEffects.field_76428_l);
                        if (pe == null || pe != null && pe.func_76459_b() < 500) {
                           W_Entity.addPotionEffect(e, new PotionEffect(MobEffects.field_76428_l, 250, 0, true, true));
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public double getWaterDepth() {
      byte b0 = 5;
      double d0 = 0.0D;

      for(int i = 0; i < b0; ++i) {
         double d1 = this.func_174813_aQ().field_72338_b + (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) * (double)(i + 0) / (double)b0 - 0.125D;
         double d2 = this.func_174813_aQ().field_72338_b + (this.func_174813_aQ().field_72337_e - this.func_174813_aQ().field_72338_b) * (double)(i + 1) / (double)b0 - 0.125D;
         d1 += (double)this.getAcInfo().floatOffset;
         d2 += (double)this.getAcInfo().floatOffset;
         AxisAlignedBB axisalignedbb = W_AxisAlignedBB.getAABB(this.func_174813_aQ().field_72340_a, d1, this.func_174813_aQ().field_72339_c, this.func_174813_aQ().field_72336_d, d2, this.func_174813_aQ().field_72334_f);
         if (this.world.func_72875_a(axisalignedbb, Material.field_151586_h)) {
            d0 += 1.0D / (double)b0;
         }
      }

      return d0;
   }

   public int getCountOnUpdate() {
      return this.countOnUpdate;
   }

   public boolean canSupply() {
      if (this.canFloatWater()) {
         return MCH_Lib.getBlockIdY(this, 1, -3) != 0;
      } else {
         return MCH_Lib.getBlockIdY(this, 1, -3) != 0 && !this.func_70090_H();
      }
   }

   public void setFuel(int fuel) {
      if (!this.world.isRemote) {
         if (fuel < 0) {
            fuel = 0;
         }

         if (fuel > this.getMaxFuel()) {
            fuel = this.getMaxFuel();
         }

         if (fuel != this.getFuel()) {
            this.dataManager.func_187227_b(FUEL, fuel);
         }
      }

   }

   public int getFuel() {
      return (Integer)this.dataManager.func_187225_a(FUEL);
   }

   public float getFuelP() {
      int m = this.getMaxFuel();
      return m == 0 ? 0.0F : (float)this.getFuel() / (float)m;
   }

   public boolean canUseFuel(boolean checkOtherSeet) {
      return this.getMaxFuel() <= 0 || this.getFuel() > 1 || this.isInfinityFuel(this.getRiddenByEntity(), checkOtherSeet);
   }

   public boolean canUseFuel() {
      return this.canUseFuel(false);
   }

   public int getMaxFuel() {
      return this.getAcInfo() != null ? this.getAcInfo().maxFuel : 0;
   }

   public void supplyFuel() {
      float range = this.getAcInfo() != null ? this.getAcInfo().fuelSupplyRange : 0.0F;
      if (!(range <= 0.0F)) {
         if (!this.world.isRemote && this.getCountOnUpdate() % 10 == 0) {
            List<MCH_EntityAircraft> list = this.world.func_72872_a(MCH_EntityAircraft.class, this.func_70046_E().func_72314_b((double)range, (double)range, (double)range));

            for(int i = 0; i < list.size(); ++i) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
               if (!W_Entity.isEqual(this, ac)) {
                  if ((!this.field_70122_E || ac.canSupply()) && ac.getFuel() < ac.getMaxFuel()) {
                     int fc = ac.getMaxFuel() - ac.getFuel();
                     if (fc > 30) {
                        fc = 30;
                     }

                     ac.setFuel(ac.getFuel() + fc);
                  }

                  ac.fuelSuppliedCount = 40;
               }
            }
         }

      }
   }

   public void updateFuel() {
      if (this.getMaxFuel() != 0) {
         if (this.fuelSuppliedCount > 0) {
            --this.fuelSuppliedCount;
         }

         if (!this.isDestroyed() && !this.world.isRemote) {
            if (this.getCountOnUpdate() % 20 == 0 && this.getFuel() > 1 && this.getThrottle() > 0.0D && this.fuelSuppliedCount <= 0) {
               double t = this.getThrottle() * 1.4D;
               if (t > 1.0D) {
                  t = 1.0D;
               }

               this.fuelConsumption += t * (double)this.getAcInfo().fuelConsumption * (double)this.getFuelConsumptionFactor();
               if (this.fuelConsumption > 1.0D) {
                  int f = (int)this.fuelConsumption;
                  this.fuelConsumption -= (double)f;
                  this.setFuel(this.getFuel() - f);
               }
            }

            int curFuel = this.getFuel();
            if (this.canSupply() && this.getCountOnUpdate() % 10 == 0 && curFuel < this.getMaxFuel()) {
               for(int i = 0; i < 3; ++i) {
                  if (curFuel < this.getMaxFuel()) {
                     ItemStack fuel = this.getGuiInventory().getFuelSlotItemStack(i);
                     if (!fuel.func_190926_b() && fuel.func_77973_b() instanceof MCH_ItemFuel && fuel.func_77960_j() < fuel.func_77958_k()) {
                        int fc = this.getMaxFuel() - curFuel;
                        if (fc > 100) {
                           fc = 100;
                        }

                        if (fuel.func_77960_j() > fuel.func_77958_k() - fc) {
                           fc = fuel.func_77958_k() - fuel.func_77960_j();
                        }

                        fuel.func_77964_b(fuel.func_77960_j() + fc);
                        curFuel += fc;
                     }
                  }
               }

               if (this.getFuel() != curFuel && this.getRiddenByEntity() instanceof EntityPlayerMP) {
                  MCH_CriteriaTriggers.SUPPLY_FUEL.trigger((EntityPlayerMP)this.getRiddenByEntity());
               }

               this.setFuel(curFuel);
            }
         }

      }
   }

   public float getFuelConsumptionFactor() {
      return 1.0F;
   }

   public void updateSupplyAmmo() {
      if (!this.world.isRemote) {
         boolean isReloading = false;
         if (this.getRiddenByEntity() instanceof EntityPlayer && !this.getRiddenByEntity().field_70128_L && ((EntityPlayer)this.getRiddenByEntity()).field_71070_bA instanceof MCH_AircraftGuiContainer) {
            isReloading = true;
         }

         this.setCommonStatus(2, isReloading);
         if (!this.isDestroyed() && this.beforeSupplyAmmo && !isReloading) {
            this.reloadAllWeapon();
            MCH_PacketNotifyAmmoNum.sendAllAmmoNum(this, (EntityPlayer)null);
         }

         this.beforeSupplyAmmo = isReloading;
      }

      if (this.getCommonStatus(2)) {
         this.supplyAmmoWait = 20;
      }

      if (this.supplyAmmoWait > 0) {
         --this.supplyAmmoWait;
      }

   }

   public void supplyAmmo(int weaponID) {
      if (this.world.isRemote) {
         MCH_WeaponSet ws = this.getWeapon(weaponID);
         ws.supplyRestAllAmmo();
      } else {
         if (this.getRiddenByEntity() instanceof EntityPlayerMP) {
            MCH_CriteriaTriggers.SUPPLY_AMMO.trigger((EntityPlayerMP)this.getRiddenByEntity());
         }

         if (this.getRiddenByEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)this.getRiddenByEntity();
            if (this.canPlayerSupplyAmmo(player, weaponID)) {
               MCH_WeaponSet ws = this.getWeapon(weaponID);
               Iterator var4 = ws.getInfo().roundItems.iterator();

               while(var4.hasNext()) {
                  MCH_WeaponInfo.RoundItem ri = (MCH_WeaponInfo.RoundItem)var4.next();
                  int num = ri.num;

                  for(int i = 0; i < player.field_71071_by.field_70462_a.size(); ++i) {
                     ItemStack itemStack = (ItemStack)player.field_71071_by.field_70462_a.get(i);
                     if (!itemStack.func_190926_b() && itemStack.func_77969_a(ri.itemStack)) {
                        if (itemStack.func_77973_b() != W_Item.getItemByName("water_bucket") && itemStack.func_77973_b() != W_Item.getItemByName("lava_bucket")) {
                           if (itemStack.func_190916_E() > num) {
                              itemStack.func_190918_g(num);
                              num = 0;
                           } else {
                              num -= itemStack.func_190916_E();
                              itemStack.func_190920_e(0);
                              player.field_71071_by.field_70462_a.set(i, ItemStack.field_190927_a);
                           }
                        } else if (itemStack.func_190916_E() == 1) {
                           player.field_71071_by.func_70299_a(i, new ItemStack(W_Item.getItemByName("bucket"), 1));
                           --num;
                        }
                     }

                     if (num <= 0) {
                        break;
                     }
                  }
               }

               ws.supplyRestAllAmmo();
            }
         }
      }

   }

   public void supplyAmmoToOtherAircraft() {
      float range = this.getAcInfo() != null ? this.getAcInfo().ammoSupplyRange : 0.0F;
      if (!(range <= 0.0F)) {
         if (!this.world.isRemote && this.getCountOnUpdate() % 40 == 0) {
            List<MCH_EntityAircraft> list = this.world.func_72872_a(MCH_EntityAircraft.class, this.func_70046_E().func_72314_b((double)range, (double)range, (double)range));

            for(int i = 0; i < list.size(); ++i) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
               if (!W_Entity.isEqual(this, ac) && ac.canSupply()) {
                  for(int wid = 0; wid < ac.getWeaponNum(); ++wid) {
                     MCH_WeaponSet ws = ac.getWeapon(wid);
                     int num = ws.getRestAllAmmoNum() + ws.getAmmoNum();
                     if (num < ws.getAllAmmoNum()) {
                        int ammo = ws.getAllAmmoNum() / 10;
                        if (ammo < 1) {
                           ammo = 1;
                        }

                        ws.setRestAllAmmoNum(num + ammo);
                        EntityPlayer player = ac.getEntityByWeaponId(wid);
                        if (num != ws.getRestAllAmmoNum() + ws.getAmmoNum()) {
                           if (ws.getAmmoNum() <= 0) {
                              ws.reloadMag();
                           }

                           MCH_PacketNotifyAmmoNum.sendAmmoNum(ac, player, wid);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public boolean canPlayerSupplyAmmo(EntityPlayer player, int weaponId) {
      if (MCH_Lib.getBlockIdY(this, 1, -3) == 0) {
         return false;
      } else if (!this.canSupply()) {
         return false;
      } else {
         MCH_WeaponSet ws = this.getWeapon(weaponId);
         if (ws.getRestAllAmmoNum() + ws.getAmmoNum() >= ws.getAllAmmoNum()) {
            return false;
         } else {
            Iterator var4 = ws.getInfo().roundItems.iterator();

            int num;
            do {
               if (!var4.hasNext()) {
                  return true;
               }

               MCH_WeaponInfo.RoundItem ri = (MCH_WeaponInfo.RoundItem)var4.next();
               num = ri.num;
               Iterator var7 = player.field_71071_by.field_70462_a.iterator();

               while(var7.hasNext()) {
                  ItemStack itemStack = (ItemStack)var7.next();
                  if (!itemStack.func_190926_b() && itemStack.func_77969_a(ri.itemStack)) {
                     num -= itemStack.func_190916_E();
                  }

                  if (num <= 0) {
                     break;
                  }
               }
            } while(num <= 0);

            return false;
         }
      }
   }

   public MCH_EntityAircraft setTextureName(@Nullable String name) {
      if (name != null && !name.isEmpty()) {
         this.dataManager.func_187227_b(TEXTURE_NAME, name);
      }

      return this;
   }

   public String getTextureName() {
      return (String)this.dataManager.func_187225_a(TEXTURE_NAME);
   }

   public void switchNextTextureName() {
      if (this.getAcInfo() != null) {
         this.setTextureName(this.getAcInfo().getNextTextureName(this.getTextureName()));
      }

   }

   public void zoomCamera() {
      if (this.canZoom()) {
         float z = this.camera.getCameraZoom();
         if ((double)z >= (double)this.getZoomMax() - 0.01D) {
            z = 1.0F;
         } else {
            z *= 2.0F;
            if (z >= (float)this.getZoomMax()) {
               z = (float)this.getZoomMax();
            }
         }

         this.camera.setCameraZoom((double)z <= (double)this.getZoomMax() + 0.01D ? z : 1.0F);
      }

   }

   public int getZoomMax() {
      return this.getAcInfo() != null ? this.getAcInfo().cameraZoom : 1;
   }

   public boolean canZoom() {
      return this.getZoomMax() > 1;
   }

   public boolean canSwitchCameraMode() {
      if (this.isDestroyed()) {
         return false;
      } else {
         return this.getAcInfo() != null && this.getAcInfo().isEnableNightVision;
      }
   }

   public boolean canSwitchCameraMode(int seatID) {
      if (this.isDestroyed()) {
         return false;
      } else {
         return this.canSwitchCameraMode() && this.camera.isValidUid(seatID);
      }
   }

   public int getCameraMode(EntityPlayer player) {
      return this.camera.getMode(this.getSeatIdByEntity(player));
   }

   public String getCameraModeName(EntityPlayer player) {
      return this.camera.getModeName(this.getSeatIdByEntity(player));
   }

   public void switchCameraMode(EntityPlayer player) {
      this.switchCameraMode(player, this.camera.getMode(this.getSeatIdByEntity(player)) + 1);
   }

   public void switchCameraMode(EntityPlayer player, int mode) {
      this.camera.setMode(this.getSeatIdByEntity(player), mode);
   }

   public void updateCameraViewers() {
      for(int i = 0; i < this.getSeatNum() + 1; ++i) {
         this.camera.updateViewer(i, this.getEntityBySeatId(i));
      }

   }

   public void updateRadar(int radarSpeed) {
      if (this.isEntityRadarMounted()) {
         this.radarRotate += radarSpeed;
         if (this.radarRotate >= 360) {
            this.radarRotate = 0;
         }

         if (this.radarRotate == 0) {
            this.entityRadar.updateXZ(this, 64);
         }
      }

   }

   public int getRadarRotate() {
      return this.radarRotate;
   }

   public void initRadar() {
      this.entityRadar.clear();
      this.radarRotate = 0;
   }

   public ArrayList<MCH_Vector2> getRadarEntityList() {
      return this.entityRadar.getEntityList();
   }

   public ArrayList<MCH_Vector2> getRadarEnemyList() {
      return this.entityRadar.getEnemyList();
   }

   public void func_70091_d(MoverType type, double x, double y, double z) {
      if (this.getAcInfo() != null) {
         this.world.field_72984_F.func_76320_a("move");
         double d2 = x;
         double d3 = y;
         double d4 = z;
         List<AxisAlignedBB> list1 = getCollisionBoxes(this, this.func_174813_aQ().func_72321_a(x, y, z));
         AxisAlignedBB axisalignedbb = this.func_174813_aQ();
         if (y != 0.0D) {
            for(int k = 0; k < list1.size(); ++k) {
               y = ((AxisAlignedBB)list1.get(k)).func_72323_b(this.func_174813_aQ(), y);
            }

            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0D, y, 0.0D));
         }

         boolean flag = this.field_70122_E || y != y && y < 0.0D;
         int j6;
         if (x != 0.0D) {
            for(j6 = 0; j6 < list1.size(); ++j6) {
               x = ((AxisAlignedBB)list1.get(j6)).func_72316_a(this.func_174813_aQ(), x);
            }

            if (x != 0.0D) {
               this.func_174826_a(this.func_174813_aQ().func_72317_d(x, 0.0D, 0.0D));
            }
         }

         if (z != 0.0D) {
            for(j6 = list1.size(); j6 < list1.size(); ++j6) {
               z = ((AxisAlignedBB)list1.get(j6)).func_72322_c(this.func_174813_aQ(), z);
            }

            if (z != 0.0D) {
               this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0D, 0.0D, z));
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
            double d8 = y;

            for(int j1 = 0; j1 < list.size(); ++j1) {
               d8 = ((AxisAlignedBB)list.get(j1)).func_72323_b(axisalignedbb3, d8);
            }

            axisalignedbb2 = axisalignedbb2.func_72317_d(0.0D, d8, 0.0D);
            double d18 = d2;

            for(int l1 = 0; l1 < list.size(); ++l1) {
               d18 = ((AxisAlignedBB)list.get(l1)).func_72316_a(axisalignedbb2, d18);
            }

            axisalignedbb2 = axisalignedbb2.func_72317_d(d18, 0.0D, 0.0D);
            double d19 = d4;

            for(int j2 = 0; j2 < list.size(); ++j2) {
               d19 = ((AxisAlignedBB)list.get(j2)).func_72322_c(axisalignedbb2, d19);
            }

            axisalignedbb2 = axisalignedbb2.func_72317_d(0.0D, 0.0D, d19);
            AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
            double d20 = y;

            for(int l2 = 0; l2 < list.size(); ++l2) {
               d20 = ((AxisAlignedBB)list.get(l2)).func_72323_b(axisalignedbb4, d20);
            }

            axisalignedbb4 = axisalignedbb4.func_72317_d(0.0D, d20, 0.0D);
            double d21 = d2;

            for(int j3 = 0; j3 < list.size(); ++j3) {
               d21 = ((AxisAlignedBB)list.get(j3)).func_72316_a(axisalignedbb4, d21);
            }

            axisalignedbb4 = axisalignedbb4.func_72317_d(d21, 0.0D, 0.0D);
            double d22 = d4;

            for(int l3 = 0; l3 < list.size(); ++l3) {
               d22 = ((AxisAlignedBB)list.get(l3)).func_72322_c(axisalignedbb4, d22);
            }

            axisalignedbb4 = axisalignedbb4.func_72317_d(0.0D, 0.0D, d22);
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

            for(int j4 = 0; j4 < list.size(); ++j4) {
               y = ((AxisAlignedBB)list.get(j4)).func_72323_b(this.func_174813_aQ(), y);
            }

            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0D, y, 0.0D));
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
         j6 = MathHelper.func_76128_c(this.posX);
         int i1 = MathHelper.func_76128_c(this.posY - 0.20000000298023224D);
         int k6 = MathHelper.func_76128_c(this.posZ);
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
   }

   private static boolean getCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb, List<AxisAlignedBB> outList) {
      int i = MathHelper.func_76128_c(aabb.field_72340_a) - 1;
      int j = MathHelper.func_76143_f(aabb.field_72336_d) + 1;
      int k = MathHelper.func_76128_c(aabb.field_72338_b) - 1;
      int l = MathHelper.func_76143_f(aabb.field_72337_e) + 1;
      int i1 = MathHelper.func_76128_c(aabb.field_72339_c) - 1;
      int j1 = MathHelper.func_76143_f(aabb.field_72334_f) + 1;
      WorldBorder worldborder = entityIn.world.func_175723_af();
      boolean flag = entityIn != null && entityIn.func_174832_aS();
      boolean flag1 = entityIn != null && entityIn.world.func_191503_g(entityIn);
      IBlockState iblockstate = Blocks.field_150348_b.func_176223_P();
      PooledMutableBlockPos blockpos = PooledMutableBlockPos.func_185346_s();

      try {
         for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = i1; l1 < j1; ++l1) {
               boolean flag2 = k1 == i || k1 == j - 1;
               boolean flag3 = l1 == i1 || l1 == j1 - 1;
               if ((!flag2 || !flag3) && entityIn.world.func_175667_e(blockpos.func_181079_c(k1, 64, l1))) {
                  for(int i2 = k; i2 < l; ++i2) {
                     if (!flag2 && !flag3 || i2 != l - 1) {
                        if (entityIn != null && flag == flag1) {
                           entityIn.func_174821_h(!flag1);
                        }

                        blockpos.func_181079_c(k1, i2, l1);
                        IBlockState iblockstate1;
                        if (!worldborder.func_177746_a(blockpos) && flag1) {
                           iblockstate1 = iblockstate;
                        } else {
                           iblockstate1 = entityIn.world.func_180495_p(blockpos);
                        }

                        iblockstate1.func_185908_a(entityIn.world, blockpos, aabb, outList, entityIn, false);
                     }
                  }
               }
            }
         }
      } finally {
         blockpos.func_185344_t();
      }

      return !outList.isEmpty();
   }

   public static List<AxisAlignedBB> getCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb) {
      List<AxisAlignedBB> list = new ArrayList();
      getCollisionBoxes(entityIn, aabb, list);
      if (entityIn != null) {
         List<Entity> list1 = entityIn.world.func_72839_b(entityIn, aabb.func_186662_g(0.25D));

         for(int i = 0; i < list1.size(); ++i) {
            Entity entity = (Entity)list1.get(i);
            if (!W_Lib.isEntityLivingBase(entity) && !(entity instanceof MCH_EntitySeat) && !(entity instanceof MCH_EntityHitBox)) {
               AxisAlignedBB axisalignedbb = entity.func_70046_E();
               if (axisalignedbb != null && axisalignedbb.func_72326_a(aabb)) {
                  list.add(axisalignedbb);
               }

               axisalignedbb = entityIn.func_70114_g(entity);
               if (axisalignedbb != null && axisalignedbb.func_72326_a(aabb)) {
                  list.add(axisalignedbb);
               }
            }
         }
      }

      return list;
   }

   protected void onUpdate_updateBlock() {
      if (MCH_Config.Collision_DestroyBlock.prmBool) {
         for(int l = 0; l < 4; ++l) {
            int i1 = MathHelper.func_76128_c(this.posX + ((double)(l % 2) - 0.5D) * 0.8D);
            int j1 = MathHelper.func_76128_c(this.posZ + ((double)(l / 2) - 0.5D) * 0.8D);

            for(int k1 = 0; k1 < 2; ++k1) {
               int l1 = MathHelper.func_76128_c(this.posY) + k1;
               Block block = W_WorldFunc.getBlock(this.world, i1, l1, j1);
               if (!W_Block.isNull(block)) {
                  if (block == W_Block.getSnowLayer()) {
                     this.world.func_175698_g(new BlockPos(i1, l1, j1));
                  }

                  if (block == W_Blocks.field_150392_bi || block == W_Blocks.field_150414_aQ) {
                     W_WorldFunc.destroyBlock(this.world, i1, l1, j1, false);
                  }
               }
            }
         }

      }
   }

   public void onUpdate_ParticleSmoke() {
      if (this.world.isRemote) {
         if (!(this.getCurrentThrottle() <= 0.10000000149011612D)) {
            float yaw = this.getRotYaw();
            float pitch = this.getRotPitch();
            float roll = this.getRotRoll();
            MCH_WeaponSet ws = this.getCurrentWeapon(this.getRiddenByEntity());
            if (ws.getFirstWeapon() instanceof MCH_WeaponSmoke) {
               for(int i = 0; i < ws.getWeaponNum(); ++i) {
                  MCH_WeaponBase wb = ws.getWeapon(i);
                  if (wb != null) {
                     MCH_WeaponInfo wi = wb.getInfo();
                     if (wi != null) {
                        Vec3d rot = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -yaw - 180.0F + wb.fixRotationYaw, pitch - wb.fixRotationPitch, roll);
                        if ((double)this.field_70146_Z.nextFloat() <= this.getCurrentThrottle() * 1.5D) {
                           Vec3d pos = MCH_Lib.RotVec3(wb.position, -yaw, -pitch, -roll);
                           double x = this.posX + pos.x + rot.x;
                           double y = this.posY + pos.y + rot.y;
                           double z = this.posZ + pos.z + rot.z;

                           for(int smk = 0; smk < wi.smokeNum; ++smk) {
                              float c = this.field_70146_Z.nextFloat() * 0.05F;
                              int maxAge = (int)(this.field_70146_Z.nextDouble() * (double)wi.smokeMaxAge);
                              MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", x, y, z);
                              prm.setMotion(rot.x * (double)wi.acceleration + (this.field_70146_Z.nextDouble() - 0.5D) * 0.2D, rot.y * (double)wi.acceleration + (this.field_70146_Z.nextDouble() - 0.5D) * 0.2D, rot.z * (double)wi.acceleration + (this.field_70146_Z.nextDouble() - 0.5D) * 0.2D);
                              prm.size = ((float)this.field_70146_Z.nextInt(5) + 5.0F) * wi.smokeSize;
                              prm.setColor(wi.color.a + this.field_70146_Z.nextFloat() * 0.05F, wi.color.r + c, wi.color.g + c, wi.color.b + c);
                              prm.age = maxAge;
                              prm.toWhite = true;
                              prm.diffusible = true;
                              MCH_ParticlesUtil.spawnParticle(prm);
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   protected void onUpdate_ParticleSandCloud(boolean seaOnly) {
      if (!seaOnly || this.getAcInfo().enableSeaSurfaceParticle) {
         double particlePosY = (double)((int)this.posY);
         boolean b = false;
         float scale = this.getAcInfo().particlesScale * 3.0F;
         if (seaOnly) {
            scale *= 2.0F;
         }

         double throttle = this.getCurrentThrottle();
         throttle *= 2.0D;
         if (throttle > 1.0D) {
            throttle = 1.0D;
         }

         int count = seaOnly ? (int)(scale * 7.0F) : 0;
         int rangeY = (int)(scale * 10.0F) + 1;

         int y;
         for(y = 0; y < rangeY && !b; ++y) {
            for(int x = -1; x <= 1; ++x) {
               for(int z = -1; z <= 1; ++z) {
                  Block block = W_WorldFunc.getBlock(this.world, (int)(this.posX + 0.5D) + x, (int)(this.posY + 0.5D) - y, (int)(this.posZ + 0.5D) + z);
                  if (!b && block != null && !Block.func_149680_a(block, Blocks.field_150350_a)) {
                     if (seaOnly && W_Block.isEqual(block, W_Block.getWater())) {
                        --count;
                     }

                     if (count <= 0) {
                        particlePosY = this.posY + 1.0D + (double)(scale / 5.0F) - (double)y;
                        b = true;
                        x += 100;
                        break;
                     }
                  }
               }
            }
         }

         double pn = (double)(rangeY - y + 1) / (5.0D * (double)scale) / 2.0D;
         if (b && this.getAcInfo().particlesScale > 0.01F) {
            for(int k = 0; k < (int)(throttle * 6.0D * pn); ++k) {
               float r = (float)(this.field_70146_Z.nextDouble() * 3.141592653589793D * 2.0D);
               double dx = (double)MathHelper.func_76134_b(r);
               double dz = (double)MathHelper.func_76126_a(r);
               MCH_ParticleParam prm = new MCH_ParticleParam(this.world, "smoke", this.posX + dx * (double)scale * 3.0D, particlePosY + (this.field_70146_Z.nextDouble() - 0.5D) * (double)scale, this.posZ + dz * (double)scale * 3.0D, (double)scale * dx * 0.3D, (double)scale * -0.4D * 0.05D, (double)scale * dz * 0.3D, scale * 5.0F);
               prm.setColor(prm.alpha * 0.6F, prm.red, prm.green, prm.blue);
               prm.age = (int)(10.0F * scale);
               prm.motionYUpAge = seaOnly ? 0.2F : 0.1F;
               MCH_ParticlesUtil.spawnParticle(prm);
            }
         }

      }
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public AxisAlignedBB func_70114_g(Entity par1Entity) {
      return par1Entity.func_174813_aQ();
   }

   public AxisAlignedBB func_70046_E() {
      return this.func_174813_aQ();
   }

   public boolean func_70104_M() {
      return false;
   }

   public double func_70042_X() {
      return 0.0D;
   }

   public float getShadowSize() {
      return 2.0F;
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   public boolean useFlare(int type) {
      if (this.getAcInfo() != null && this.getAcInfo().haveFlare()) {
         int[] var2 = this.getAcInfo().flare.types;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int i = var2[var4];
            if (i == type) {
               this.setCommonStatus(0, true);
               if (this.flareDv.use(type)) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public int getCurrentFlareType() {
      return !this.haveFlare() ? 0 : this.getAcInfo().flare.types[this.currentFlareIndex];
   }

   public void nextFlareType() {
      if (this.haveFlare()) {
         this.currentFlareIndex = (this.currentFlareIndex + 1) % this.getAcInfo().flare.types.length;
      }

   }

   public boolean canUseFlare() {
      if (this.getAcInfo() != null && this.getAcInfo().haveFlare()) {
         if (this.getCommonStatus(0)) {
            return false;
         } else {
            return this.flareDv.tick == 0;
         }
      } else {
         return false;
      }
   }

   public boolean isFlarePreparation() {
      return this.flareDv.isInPreparation();
   }

   public boolean isFlareUsing() {
      return this.flareDv.isUsing();
   }

   public int getFlareTick() {
      return this.flareDv.tick;
   }

   public boolean haveFlare() {
      return this.getAcInfo() != null && this.getAcInfo().haveFlare();
   }

   public boolean haveFlare(int seatID) {
      return this.haveFlare() && seatID >= 0 && seatID <= 1;
   }

   public MCH_EntitySeat[] getSeats() {
      return this.seats != null ? this.seats : seatsDummy;
   }

   public int getSeatIdByEntity(@Nullable Entity entity) {
      if (entity == null) {
         return -1;
      } else if (isEqual(this.getRiddenByEntity(), entity)) {
         return 0;
      } else {
         for(int i = 0; i < this.getSeats().length; ++i) {
            MCH_EntitySeat seat = this.getSeats()[i];
            if (seat != null && isEqual(seat.getRiddenByEntity(), entity)) {
               return i + 1;
            }
         }

         return -1;
      }
   }

   @Nullable
   public MCH_EntitySeat getSeatByEntity(@Nullable Entity entity) {
      int idx = this.getSeatIdByEntity(entity);
      return idx > 0 ? this.getSeat(idx - 1) : null;
   }

   @Nullable
   public Entity getEntityBySeatId(int id) {
      if (id == 0) {
         return this.getRiddenByEntity();
      } else {
         --id;
         if (id >= 0 && id < this.getSeats().length) {
            return this.seats[id] != null ? this.seats[id].getRiddenByEntity() : null;
         } else {
            return null;
         }
      }
   }

   @Nullable
   public EntityPlayer getEntityByWeaponId(int id) {
      if (id >= 0 && id < this.getWeaponNum()) {
         for(int i = 0; i < this.currentWeaponID.length; ++i) {
            if (this.currentWeaponID[i] == id) {
               Entity e = this.getEntityBySeatId(i);
               if (e instanceof EntityPlayer) {
                  return (EntityPlayer)e;
               }
            }
         }
      }

      return null;
   }

   @Nullable
   public Entity getWeaponUserByWeaponName(String name) {
      if (this.getAcInfo() == null) {
         return null;
      } else {
         MCH_AircraftInfo.Weapon weapon = this.getAcInfo().getWeaponByName(name);
         Entity entity = null;
         if (weapon != null) {
            entity = this.getEntityBySeatId(this.getWeaponSeatID((MCH_WeaponInfo)null, weapon));
            if (entity == null && weapon.canUsePilot) {
               entity = this.getRiddenByEntity();
            }
         }

         return entity;
      }
   }

   protected void newSeats(int seatsNum) {
      if (seatsNum >= 2) {
         if (this.seats != null) {
            for(int i = 0; i < this.seats.length; ++i) {
               if (this.seats[i] != null) {
                  this.seats[i].func_70106_y();
                  this.seats[i] = null;
               }
            }
         }

         this.seats = new MCH_EntitySeat[seatsNum - 1];
      }

   }

   @Nullable
   public MCH_EntitySeat getSeat(int idx) {
      return idx < this.seats.length ? this.seats[idx] : null;
   }

   public void setSeat(int idx, MCH_EntitySeat seat) {
      if (idx < this.seats.length) {
         MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.setSeat SeatID=" + idx + " / seat[]" + (this.seats[idx] != null) + " / " + (seat.getRiddenByEntity() != null));
         if (this.seats[idx] != null && this.seats[idx].getRiddenByEntity() != null) {
         }

         this.seats[idx] = seat;
      }

   }

   public boolean isValidSeatID(int seatID) {
      return seatID >= 0 && seatID < this.getSeatNum() + 1;
   }

   public void updateHitBoxPosition() {
   }

   public void updateSeatsPosition(double px, double py, double pz, boolean setPrevPos) {
      MCH_SeatInfo[] info = this.getSeatsInfo();
      py += 0.3499999940395355D;
      if (this.pilotSeat != null && !this.pilotSeat.field_70128_L) {
         this.pilotSeat.field_70169_q = this.pilotSeat.posX;
         this.pilotSeat.field_70167_r = this.pilotSeat.posY;
         this.pilotSeat.field_70166_s = this.pilotSeat.posZ;
         this.pilotSeat.func_70107_b(px, py, pz);
         if (info != null && info.length > 0 && info[0] != null) {
            Vec3d v = this.getTransformedPosition(info[0].pos.x, info[0].pos.y, info[0].pos.z, px, py, pz, info[0].rotSeat);
            this.pilotSeat.func_70107_b(v.x, v.y, v.z);
         }

         this.pilotSeat.field_70125_A = this.getRotPitch();
         this.pilotSeat.field_70177_z = this.getRotYaw();
         if (setPrevPos) {
            this.pilotSeat.field_70169_q = this.pilotSeat.posX;
            this.pilotSeat.field_70167_r = this.pilotSeat.posY;
            this.pilotSeat.field_70166_s = this.pilotSeat.posZ;
         }
      }

      int i = 0;
      MCH_EntitySeat[] var10 = this.seats;
      int var11 = var10.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         MCH_EntitySeat seat = var10[var12];
         ++i;
         if (seat != null && !seat.field_70128_L) {
            float offsetY = -0.5F;
            if (seat.getRiddenByEntity() != null && !W_Lib.isClientPlayer(seat.getRiddenByEntity()) && seat.getRiddenByEntity().field_70131_O >= 1.0F) {
            }

            seat.field_70169_q = seat.posX;
            seat.field_70167_r = seat.posY;
            seat.field_70166_s = seat.posZ;
            MCH_SeatInfo si = i < info.length ? info[i] : info[0];
            Vec3d v = this.getTransformedPosition(si.pos.x, si.pos.y + (double)offsetY, si.pos.z, px, py, pz, si.rotSeat);
            seat.func_70107_b(v.x, v.y, v.z);
            seat.field_70125_A = this.getRotPitch();
            seat.field_70177_z = this.getRotYaw();
            if (setPrevPos) {
               seat.field_70169_q = seat.posX;
               seat.field_70167_r = seat.posY;
               seat.field_70166_s = seat.posZ;
            }

            if (si instanceof MCH_SeatRackInfo) {
               seat.updateRotation(seat.getRiddenByEntity(), ((MCH_SeatRackInfo)si).fixYaw + this.getRotYaw(), ((MCH_SeatRackInfo)si).fixPitch);
            }

            seat.updatePosition(seat.getRiddenByEntity());
         }
      }

   }

   public int getClientPositionDelayCorrection() {
      return 7;
   }

   @SideOnly(Side.CLIENT)
   public void func_180426_a(double par1, double par3, double par5, float par7, float par8, int par9, boolean teleport) {
      this.aircraftPosRotInc = par9 + this.getClientPositionDelayCorrection();
      this.aircraftX = par1;
      this.aircraftY = par3;
      this.aircraftZ = par5;
      this.aircraftYaw = (double)par7;
      this.aircraftPitch = (double)par8;
      this.field_70159_w = this.velocityX;
      this.field_70181_x = this.velocityY;
      this.field_70179_y = this.velocityZ;
   }

   public void updateRiderPosition(Entity passenger, double px, double py, double pz) {
      MCH_SeatInfo[] info = this.getSeatsInfo();
      if (this.func_184196_w(passenger) && !passenger.field_70128_L) {
         float riddenEntityYOffset = 0.0F;
         if (passenger instanceof EntityPlayer && !W_Lib.isClientPlayer(passenger)) {
         }

         Vec3d v;
         if (info != null && info.length > 0) {
            v = this.getTransformedPosition(info[0].pos.x, info[0].pos.y + (double)riddenEntityYOffset - 0.5D, info[0].pos.z, px, py + 0.3499999940395355D, pz, info[0].rotSeat);
         } else {
            v = this.getTransformedPosition(0.0D, (double)(riddenEntityYOffset - 1.0F), 0.0D);
         }

         passenger.func_70107_b(v.x, v.y, v.z);
      }

   }

   public void func_184232_k(Entity passenger) {
      this.updateRiderPosition(passenger, this.posX, this.posY, this.posZ);
   }

   public Vec3d calcOnTurretPos(Vec3d pos) {
      float ry = this.getLastRiderYaw();
      if (this.getRiddenByEntity() != null) {
         ry = this.getRiddenByEntity().field_70177_z;
      }

      Vec3d tpos = this.getAcInfo().turretPosition.func_72441_c(0.0D, pos.y, 0.0D);
      Vec3d v = pos.func_72441_c(-tpos.x, -tpos.y, -tpos.z);
      v = MCH_Lib.RotVec3(v, -ry, 0.0F, 0.0F);
      Vec3d vv = MCH_Lib.RotVec3(tpos, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
      return v.func_178787_e(vv);
   }

   public float getLastRiderYaw() {
      return this.lastRiderYaw;
   }

   public float getLastRiderPitch() {
      return this.lastRiderPitch;
   }

   @SideOnly(Side.CLIENT)
   public void setupAllRiderRenderPosition(float tick, EntityPlayer player) {
      double x = this.field_70142_S + (this.posX - this.field_70142_S) * (double)tick;
      double y = this.field_70137_T + (this.posY - this.field_70137_T) * (double)tick;
      double z = this.field_70136_U + (this.posZ - this.field_70136_U) * (double)tick;
      this.updateRiderPosition(this.getRiddenByEntity(), x, y, z);
      this.updateSeatsPosition(x, y, z, true);

      for(int i = 0; i < this.getSeatNum() + 1; ++i) {
         Entity e = this.getEntityBySeatId(i);
         if (e != null) {
            e.field_70142_S = e.posX;
            e.field_70137_T = e.posY;
            e.field_70136_U = e.posZ;
         }
      }

      if (this.getTVMissile() != null && W_Lib.isClientPlayer(this.getTVMissile().shootingEntity)) {
         Entity tv = this.getTVMissile();
         x = tv.field_70169_q + (tv.posX - tv.field_70169_q) * (double)tick;
         y = tv.field_70167_r + (tv.posY - tv.field_70167_r) * (double)tick;
         z = tv.field_70166_s + (tv.posZ - tv.field_70166_s) * (double)tick;
         MCH_ViewEntityDummy.setCameraPosition(x, y, z);
      } else {
         MCH_AircraftInfo.CameraPosition cpi = this.getCameraPosInfo();
         if (cpi != null && cpi.pos != null) {
            MCH_SeatInfo seatInfo = this.getSeatInfo(player);
            Vec3d v = cpi.pos.func_72441_c(0.0D, 0.3499999940395355D, 0.0D);
            if (seatInfo != null && seatInfo.rotSeat) {
               v = this.calcOnTurretPos(v);
            } else {
               v = MCH_Lib.RotVec3(v, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
            }

            MCH_ViewEntityDummy.setCameraPosition(x + v.x, y + v.y, z + v.z);
            if (!cpi.fixRot) {
            }
         }
      }

   }

   public Vec3d getTurretPos(Vec3d pos, boolean turret) {
      if (turret) {
         float ry = this.getLastRiderYaw();
         if (this.getRiddenByEntity() != null) {
            ry = this.getRiddenByEntity().field_70177_z;
         }

         Vec3d tpos = this.getAcInfo().turretPosition.func_72441_c(0.0D, pos.y, 0.0D);
         Vec3d v = pos.func_72441_c(-tpos.x, -tpos.y, -tpos.z);
         v = MCH_Lib.RotVec3(v, -ry, 0.0F, 0.0F);
         Vec3d vv = MCH_Lib.RotVec3(tpos, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
         return v.func_178787_e(vv);
      } else {
         return Vec3d.ZERO;
      }
   }

   public Vec3d getTransformedPosition(Vec3d v) {
      return this.getTransformedPosition(v.x, v.y, v.z);
   }

   public Vec3d getTransformedPosition(double x, double y, double z) {
      return this.getTransformedPosition(x, y, z, this.posX, this.posY, this.posZ);
   }

   public Vec3d getTransformedPosition(Vec3d v, Vec3d pos) {
      return this.getTransformedPosition(v.x, v.y, v.z, pos.x, pos.y, pos.z);
   }

   public Vec3d getTransformedPosition(Vec3d v, double px, double py, double pz) {
      return this.getTransformedPosition(v.x, v.y, v.z, this.posX, this.posY, this.posZ);
   }

   public Vec3d getTransformedPosition(double x, double y, double z, double px, double py, double pz) {
      Vec3d v = MCH_Lib.RotVec3(x, y, z, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
      return v.func_72441_c(px, py, pz);
   }

   public Vec3d getTransformedPosition(double x, double y, double z, double px, double py, double pz, boolean rotSeat) {
      if (rotSeat && this.getAcInfo() != null) {
         MCH_AircraftInfo info = this.getAcInfo();
         Vec3d tv = MCH_Lib.RotVec3(x - info.turretPosition.x, y - info.turretPosition.y, z - info.turretPosition.z, -this.getLastRiderYaw() + this.getRotYaw(), 0.0F, 0.0F);
         x = tv.x + info.turretPosition.x;
         y = tv.y + info.turretPosition.y;
         z = tv.z + info.turretPosition.z;
      }

      Vec3d v = MCH_Lib.RotVec3(x, y, z, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
      return v.func_72441_c(px, py, pz);
   }

   protected MCH_SeatInfo[] getSeatsInfo() {
      if (this.seatsInfo != null) {
         return this.seatsInfo;
      } else {
         this.newSeatsPos();
         return this.seatsInfo;
      }
   }

   @Nullable
   public MCH_SeatInfo getSeatInfo(int index) {
      MCH_SeatInfo[] seats = this.getSeatsInfo();
      return index >= 0 && seats != null && index < seats.length ? seats[index] : null;
   }

   @Nullable
   public MCH_SeatInfo getSeatInfo(@Nullable Entity entity) {
      return this.getSeatInfo(this.getSeatIdByEntity(entity));
   }

   protected void setSeatsInfo(MCH_SeatInfo[] v) {
      this.seatsInfo = v;
   }

   public int getSeatNum() {
      if (this.getAcInfo() == null) {
         return 0;
      } else {
         int s = this.getAcInfo().getNumSeatAndRack();
         return s >= 1 ? s - 1 : 1;
      }
   }

   protected void newSeatsPos() {
      if (this.getAcInfo() != null) {
         MCH_SeatInfo[] v = new MCH_SeatInfo[this.getAcInfo().getNumSeatAndRack()];

         for(int i = 0; i < v.length; ++i) {
            v[i] = (MCH_SeatInfo)this.getAcInfo().seatList.get(i);
         }

         this.setSeatsInfo(v);
      }

   }

   public void createSeats(String uuid) {
      if (!this.world.isRemote) {
         if (!uuid.isEmpty()) {
            this.setCommonUniqueId(uuid);
            this.seats = new MCH_EntitySeat[this.getSeatNum()];

            for(int i = 0; i < this.seats.length; ++i) {
               this.seats[i] = new MCH_EntitySeat(this.world, this.posX, this.posY, this.posZ);
               this.seats[i].parentUniqueID = this.getCommonUniqueId();
               this.seats[i].seatID = i;
               this.seats[i].setParent(this);
               this.world.func_72838_d(this.seats[i]);
            }

         }
      }
   }

   public boolean interactFirstSeat(EntityPlayer player) {
      if (this.getSeats() == null) {
         return false;
      } else {
         int seatId = 1;
         MCH_EntitySeat[] var3 = this.getSeats();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MCH_EntitySeat seat = var3[var5];
            if (seat != null && seat.getRiddenByEntity() == null && !this.isMountedEntity(player) && this.canRideSeatOrRack(seatId, player)) {
               if (!this.world.isRemote) {
                  player.func_184220_m(seat);
               }
               break;
            }

            ++seatId;
         }

         return true;
      }
   }

   public void onMountPlayerSeat(MCH_EntitySeat seat, Entity entity) {
      if (seat != null) {
         if (entity instanceof EntityPlayer || entity instanceof MCH_EntityGunner) {
         }

         if (this.world.isRemote && MCH_Lib.getClientPlayer() == entity) {
            this.switchGunnerFreeLookMode(false);
         }

         this.initCurrentWeapon(entity);
         MCH_Lib.DbgLog(this.world, "onMountEntitySeat:%d", W_Entity.getEntityId(entity));
         Entity pilot = this.getRiddenByEntity();
         int sid = this.getSeatIdByEntity(entity);
         if (sid == 1 && (this.getAcInfo() == null || !this.getAcInfo().isEnableConcurrentGunnerMode)) {
            this.switchGunnerMode(false);
         }

         if (sid > 0) {
            this.isGunnerModeOtherSeat = true;
         }

         if (pilot != null && this.getAcInfo() != null) {
            int cwid = this.getCurrentWeaponID(pilot);
            MCH_AircraftInfo.Weapon w = this.getAcInfo().getWeaponById(cwid);
            if (w != null && this.getWeaponSeatID(this.getWeaponInfoById(cwid), w) == sid) {
               int next = this.getNextWeaponID(pilot, 1);
               MCH_Lib.DbgLog(this.world, "onMountEntitySeat:%d:->%d", W_Entity.getEntityId(pilot), next);
               if (next >= 0) {
                  this.switchWeapon(pilot, next);
               }
            }
         }

         if (this.world.isRemote) {
            this.updateClientSettings(sid);
         }

      }
   }

   @Nullable
   public MCH_WeaponInfo getWeaponInfoById(int id) {
      if (id >= 0) {
         MCH_WeaponSet ws = this.getWeapon(id);
         if (ws != null) {
            return ws.getInfo();
         }
      }

      return null;
   }

   public abstract boolean canMountWithNearEmptyMinecart();

   protected void mountWithNearEmptyMinecart() {
      if (this.func_184187_bx() == null) {
         int d = 2;
         if (this.dismountedUserCtrl) {
            d = 6;
         }

         List<Entity> list = this.world.func_72839_b(this, this.func_174813_aQ().func_72314_b((double)d, (double)d, (double)d));
         if (list != null && !list.isEmpty()) {
            for(int i = 0; i < list.size(); ++i) {
               Entity entity = (Entity)list.get(i);
               if (entity instanceof EntityMinecartEmpty) {
                  if (this.dismountedUserCtrl) {
                     return;
                  }

                  if (!entity.func_184207_aI() && entity.func_70104_M()) {
                     this.waitMountEntity = 20;
                     MCH_Lib.DbgLog(this.world.isRemote, "MCH_EntityAircraft.mountWithNearEmptyMinecart:" + entity);
                     this.func_184220_m(entity);
                     return;
                  }
               }
            }
         }

         this.dismountedUserCtrl = false;
      }
   }

   public boolean isRidePlayer() {
      if (this.getRiddenByEntity() instanceof EntityPlayer) {
         return true;
      } else {
         MCH_EntitySeat[] var1 = this.getSeats();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MCH_EntitySeat seat = var1[var3];
            if (seat != null && seat.getRiddenByEntity() instanceof EntityPlayer) {
               return true;
            }
         }

         return false;
      }
   }

   public void onUnmountPlayerSeat(MCH_EntitySeat seat, Entity entity) {
      MCH_Lib.DbgLog(this.world, "onUnmountPlayerSeat:%d", W_Entity.getEntityId(entity));
      int sid = this.getSeatIdByEntity(entity);
      this.camera.initCamera(sid, entity);
      MCH_SeatInfo seatInfo = this.getSeatInfo(seat.seatID + 1);
      if (seatInfo != null) {
         this.setUnmountPosition(entity, new Vec3d(seatInfo.pos.x, 0.0D, seatInfo.pos.z));
      }

      if (!this.isRidePlayer()) {
         this.switchGunnerMode(false);
         this.switchHoveringMode(false);
      }

   }

   public boolean isCreatedSeats() {
      return !this.getCommonUniqueId().isEmpty();
   }

   public void onUpdate_Seats() {
      boolean b = false;

      for(int i = 0; i < this.seats.length; ++i) {
         if (this.seats[i] != null) {
            if (!this.seats[i].field_70128_L) {
               this.seats[i].field_70143_R = 0.0F;
            }
         } else {
            b = true;
         }
      }

      if (b) {
         if (this.seatSearchCount > 40) {
            if (this.world.isRemote) {
               MCH_PacketSeatListRequest.requestSeatList(this);
            } else {
               this.searchSeat();
            }

            this.seatSearchCount = 0;
         }

         ++this.seatSearchCount;
      }

   }

   public void searchSeat() {
      List<MCH_EntitySeat> list = this.world.func_72872_a(MCH_EntitySeat.class, this.func_174813_aQ().func_72314_b(60.0D, 60.0D, 60.0D));

      for(int i = 0; i < list.size(); ++i) {
         MCH_EntitySeat seat = (MCH_EntitySeat)list.get(i);
         if (!seat.field_70128_L && seat.parentUniqueID.equals(this.getCommonUniqueId()) && seat.seatID >= 0 && seat.seatID < this.getSeatNum() && this.seats[seat.seatID] == null) {
            this.seats[seat.seatID] = seat;
            seat.setParent(this);
         }
      }

   }

   public String getCommonUniqueId() {
      return this.commonUniqueId;
   }

   public void setCommonUniqueId(String uniqId) {
      this.commonUniqueId = uniqId;
   }

   public void func_70106_y() {
      this.setDead(false);
   }

   public void setDead(boolean dropItems) {
      this.dropContentsWhenDead = dropItems;
      super.func_70106_y();
      if (this.getRiddenByEntity() != null) {
         this.getRiddenByEntity().func_184210_p();
      }

      this.getGuiInventory().setDead();
      MCH_EntitySeat[] var2 = this.seats;
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         MCH_EntitySeat s = var2[var4];
         if (s != null) {
            s.func_70106_y();
         }
      }

      if (this.soundUpdater != null) {
         this.soundUpdater.update();
      }

      if (this.getTowChainEntity() != null) {
         this.getTowChainEntity().func_70106_y();
         this.setTowChainEntity((MCH_EntityChain)null);
      }

      Entity[] var6 = this.func_70021_al();
      var3 = var6.length;

      for(var4 = 0; var4 < var3; ++var4) {
         Entity e = var6[var4];
         if (e != null) {
            e.func_70106_y();
         }
      }

      MCH_Lib.DbgLog(this.world, "setDead:" + (this.getAcInfo() != null ? this.getAcInfo().name : "null"));
   }

   public void unmountEntity() {
      if (!this.isRidePlayer()) {
         this.switchHoveringMode(false);
      }

      this.moveLeft = this.moveRight = this.throttleDown = this.throttleUp = false;
      Entity rByEntity = null;
      if (this.getRiddenByEntity() != null) {
         rByEntity = this.getRiddenByEntity();
         this.camera.initCamera(0, rByEntity);
         if (!this.world.isRemote) {
            this.getRiddenByEntity().func_184210_p();
         }
      } else if (this.lastRiddenByEntity != null) {
         rByEntity = this.lastRiddenByEntity;
         if (rByEntity instanceof EntityPlayer) {
            this.camera.initCamera(0, rByEntity);
         }
      }

      MCH_Lib.DbgLog(this.world, "unmountEntity:" + rByEntity);
      if (!this.isRidePlayer()) {
         this.switchGunnerMode(false);
      }

      this.setCommonStatus(1, false);
      if (!this.isUAV()) {
         this.setUnmountPosition(rByEntity, this.getSeatsInfo()[0].pos);
      } else if (rByEntity != null && rByEntity.func_184187_bx() instanceof MCH_EntityUavStation) {
         rByEntity.func_184210_p();
      }

      this.lastRiddenByEntity = null;
      if (this.cs_dismountAll) {
         this.unmountCrew(false);
      }

   }

   public Entity func_184187_bx() {
      return super.func_184187_bx();
   }

   public void startUnmountCrew() {
      this.isParachuting = true;
      if (this.haveHatch()) {
         this.foldHatch(true, true);
      }

   }

   public void stopUnmountCrew() {
      this.isParachuting = false;
   }

   public void unmountCrew() {
      if (this.getAcInfo() != null) {
         if (this.getAcInfo().haveRepellingHook()) {
            if (!this.isRepelling()) {
               if (MCH_Lib.getBlockIdY(this, 3, -4) > 0) {
                  this.unmountCrew(false);
               } else if (this.canStartRepelling()) {
                  this.startRepelling();
               }
            } else {
               this.stopRepelling();
            }
         } else if (this.isParachuting) {
            this.stopUnmountCrew();
         } else if (this.getAcInfo().isEnableParachuting && MCH_Lib.getBlockIdY(this, 3, -10) == 0) {
            this.startUnmountCrew();
         } else {
            this.unmountCrew(false);
         }

      }
   }

   public boolean isRepelling() {
      return this.getCommonStatus(5);
   }

   public void setRepellingStat(boolean b) {
      this.setCommonStatus(5, b);
   }

   public Vec3d getRopePos(int ropeIndex) {
      return this.getAcInfo() != null && this.getAcInfo().haveRepellingHook() && ropeIndex < this.getAcInfo().repellingHooks.size() ? this.getTransformedPosition(((MCH_AircraftInfo.RepellingHook)this.getAcInfo().repellingHooks.get(ropeIndex)).pos) : new Vec3d(this.posX, this.posY, this.posZ);
   }

   private void startRepelling() {
      MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.startRepelling()");
      this.setRepellingStat(true);
      this.throttleUp = false;
      this.throttleDown = false;
      this.moveLeft = false;
      this.moveRight = false;
      this.tickRepelling = 0;
   }

   private void stopRepelling() {
      MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.stopRepelling()");
      this.setRepellingStat(false);
   }

   public static float abs(float value) {
      return value >= 0.0F ? value : -value;
   }

   public static double abs(double value) {
      return value >= 0.0D ? value : -value;
   }

   public boolean canStartRepelling() {
      if (this.getAcInfo().haveRepellingHook() && this.isHovering() && abs(this.getRotPitch()) < 3.0F && abs(this.getRotRoll()) < 3.0F) {
         Vec3d v = ((Vec3d)this.prevPosition.oldest()).func_72441_c(-this.posX, -this.posY, -this.posZ);
         if (v.func_72433_c() < 0.3D) {
            return true;
         }
      }

      return false;
   }

   public boolean unmountCrew(boolean unmountParachute) {
      boolean ret = false;
      MCH_SeatInfo[] pos = this.getSeatsInfo();

      for(int i = 0; i < this.seats.length; ++i) {
         if (this.seats[i] != null && this.seats[i].getRiddenByEntity() != null) {
            Entity entity = this.seats[i].getRiddenByEntity();
            if (!(entity instanceof EntityPlayer) && !(pos[i + 1] instanceof MCH_SeatRackInfo)) {
               if (unmountParachute) {
                  if (this.getSeatIdByEntity(entity) > 1) {
                     ret = true;
                     Vec3d dropPos = this.getTransformedPosition(this.getAcInfo().mobDropOption.pos, (Vec3d)this.prevPosition.oldest());
                     this.seats[i].posX = dropPos.x;
                     this.seats[i].posY = dropPos.y;
                     this.seats[i].posZ = dropPos.z;
                     entity.func_184210_p();
                     entity.posX = dropPos.x;
                     entity.posY = dropPos.y;
                     entity.posZ = dropPos.z;
                     this.dropEntityParachute(entity);
                     break;
                  }
               } else {
                  ret = true;
                  this.setUnmountPosition(this.seats[i], pos[i + 1].pos);
                  entity.func_184210_p();
                  this.setUnmountPosition(entity, pos[i + 1].pos);
               }
            }
         }
      }

      return ret;
   }

   public void setUnmountPosition(@Nullable Entity rByEntity, Vec3d pos) {
      if (rByEntity != null) {
         MCH_AircraftInfo info = this.getAcInfo();
         Vec3d v;
         if (info != null && info.unmountPosition != null) {
            v = this.getTransformedPosition(info.unmountPosition);
         } else {
            double x = pos.x;
            x = x >= 0.0D ? x + 3.0D : x - 3.0D;
            v = this.getTransformedPosition(x, 2.0D, pos.z);
         }

         rByEntity.func_70107_b(v.x, v.y, v.z);
         this.listUnmountReserve.add(new MCH_EntityAircraft.UnmountReserve(this, rByEntity, v.x, v.y, v.z));
      }

   }

   public boolean unmountEntityFromSeat(@Nullable Entity entity) {
      if (entity != null && this.seats != null && this.seats.length != 0) {
         MCH_EntitySeat[] var2 = this.seats;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            MCH_EntitySeat seat = var2[var4];
            if (seat != null && seat.getRiddenByEntity() != null && W_Entity.isEqual(seat.getRiddenByEntity(), entity)) {
               entity.func_184210_p();
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void ejectSeat(@Nullable Entity entity) {
      int sid = this.getSeatIdByEntity(entity);
      if (sid >= 0 && sid <= 1) {
         if (this.getGuiInventory().haveParachute()) {
            if (sid == 0) {
               this.getGuiInventory().consumeParachute();
               this.unmountEntity();
               this.ejectSeatSub(entity, 0);
               entity = this.getEntityBySeatId(1);
               if (entity instanceof EntityPlayer) {
                  entity = null;
               }
            }

            if (this.getGuiInventory().haveParachute() && entity != null) {
               this.getGuiInventory().consumeParachute();
               this.unmountEntityFromSeat(entity);
               this.ejectSeatSub(entity, 1);
            }
         }

      }
   }

   public void ejectSeatSub(Entity entity, int sid) {
      Vec3d pos = this.getSeatInfo(sid) != null ? this.getSeatInfo(sid).pos : null;
      Vec3d v;
      if (pos != null) {
         v = this.getTransformedPosition(pos.x, pos.y + 2.0D, pos.z);
         entity.func_70107_b(v.x, v.y, v.z);
      }

      v = MCH_Lib.RotVec3(0.0D, 2.0D, 0.0D, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
      entity.field_70159_w = this.field_70159_w + v.x + ((double)this.field_70146_Z.nextFloat() - 0.5D) * 0.1D;
      entity.field_70181_x = this.field_70181_x + v.y;
      entity.field_70179_y = this.field_70179_y + v.z + ((double)this.field_70146_Z.nextFloat() - 0.5D) * 0.1D;
      MCH_EntityParachute parachute = new MCH_EntityParachute(this.world, entity.posX, entity.posY, entity.posZ);
      parachute.field_70177_z = entity.field_70177_z;
      parachute.field_70159_w = entity.field_70159_w;
      parachute.field_70181_x = entity.field_70181_x;
      parachute.field_70179_y = entity.field_70179_y;
      parachute.field_70143_R = entity.field_70143_R;
      parachute.user = entity;
      parachute.setType(2);
      this.world.func_72838_d(parachute);
      if (this.getAcInfo().haveCanopy() && this.isCanopyClose()) {
         this.openCanopy_EjectSeat();
      }

      W_WorldFunc.MOD_playSoundAtEntity(entity, "eject_seat", 5.0F, 1.0F);
   }

   public boolean canEjectSeat(@Nullable Entity entity) {
      int sid = this.getSeatIdByEntity(entity);
      if (sid == 0 && this.isUAV()) {
         return false;
      } else {
         return sid >= 0 && sid < 2 && this.getAcInfo() != null && this.getAcInfo().isEnableEjectionSeat;
      }
   }

   public int getNumEjectionSeat() {
      return 0;
   }

   public int getMountedEntityNum() {
      int num = 0;
      if (this.getRiddenByEntity() != null && !this.getRiddenByEntity().field_70128_L) {
         ++num;
      }

      if (this.seats != null && this.seats.length > 0) {
         MCH_EntitySeat[] var2 = this.seats;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            MCH_EntitySeat seat = var2[var4];
            if (seat != null && seat.getRiddenByEntity() != null && !seat.getRiddenByEntity().field_70128_L) {
               ++num;
            }
         }
      }

      return num;
   }

   public void mountMobToSeats() {
      List<EntityLivingBase> list = this.world.func_72872_a(W_Lib.getEntityLivingBaseClass(), this.func_174813_aQ().func_72314_b(3.0D, 2.0D, 3.0D));

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (!(entity instanceof EntityPlayer) && entity.func_184187_bx() == null) {
            int sid = 1;
            MCH_EntitySeat[] var5 = this.getSeats();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               MCH_EntitySeat seat = var5[var7];
               if (seat != null && seat.getRiddenByEntity() == null && !this.isMountedEntity(entity) && this.canRideSeatOrRack(sid, entity)) {
                  if (this.getSeatInfo(sid) instanceof MCH_SeatRackInfo) {
                     break;
                  }

                  entity.func_184220_m(seat);
               }

               ++sid;
            }
         }
      }

   }

   public void mountEntityToRack() {
      if (!MCH_Config.EnablePutRackInFlying.prmBool) {
         if (this.getCurrentThrottle() > 0.3D) {
            return;
         }

         Block block = MCH_Lib.getBlockY(this, 1, -3, true);
         if (block == null || W_Block.isEqual(block, Blocks.field_150350_a)) {
            return;
         }
      }

      int countRideEntity = 0;

      for(int sid = 0; sid < this.getSeatNum(); ++sid) {
         MCH_EntitySeat seat = this.getSeat(sid);
         if (this.getSeatInfo(1 + sid) instanceof MCH_SeatRackInfo && seat != null && seat.getRiddenByEntity() == null) {
            MCH_SeatRackInfo info = (MCH_SeatRackInfo)this.getSeatInfo(1 + sid);
            Vec3d v = MCH_Lib.RotVec3(info.getEntryPos().x, info.getEntryPos().y, info.getEntryPos().z, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
            v = v.func_72441_c(this.posX, this.posY, this.posZ);
            AxisAlignedBB bb = new AxisAlignedBB(v.x, v.y, v.z, v.x, v.y, v.z);
            float range = info.range;
            List<Entity> list = this.world.func_72839_b(this, bb.func_72314_b((double)range, (double)range, (double)range));

            for(int i = 0; i < list.size(); ++i) {
               Entity entity = (Entity)list.get(i);
               if (this.canRideSeatOrRack(1 + sid, entity)) {
                  if (entity instanceof MCH_IEntityCanRideAircraft) {
                     if (((MCH_IEntityCanRideAircraft)entity).canRideAircraft(this, sid, info)) {
                        MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.mountEntityToRack:%d:%s", sid, entity);
                        entity.func_184220_m(seat);
                        ++countRideEntity;
                        break;
                     }
                  } else if (entity.func_184187_bx() == null) {
                     NBTTagCompound nbt = entity.getEntityData();
                     if (nbt.hasKey("CanMountEntity") && nbt.getBoolean("CanMountEntity")) {
                        MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.mountEntityToRack:%d:%s:%s", sid, entity, entity.getClass());
                        entity.func_184220_m(seat);
                        ++countRideEntity;
                        break;
                     }
                  }
               }
            }
         }
      }

      if (countRideEntity > 0) {
         W_WorldFunc.DEF_playSoundEffect(this.world, this.posX, this.posY, this.posZ, "random.click", 1.0F, 1.0F);
      }

   }

   public void unmountEntityFromRack() {
      for(int sid = this.getSeatNum() - 1; sid >= 0; --sid) {
         MCH_EntitySeat seat = this.getSeat(sid);
         if (this.getSeatInfo(sid + 1) instanceof MCH_SeatRackInfo && seat != null && seat.getRiddenByEntity() != null) {
            MCH_SeatRackInfo info = (MCH_SeatRackInfo)this.getSeatInfo(sid + 1);
            Entity entity = seat.getRiddenByEntity();
            Vec3d pos = info.getEntryPos();
            if (entity instanceof MCH_EntityAircraft) {
               if (pos.z >= (double)this.getAcInfo().bbZ) {
                  pos = pos.func_72441_c(0.0D, 0.0D, 12.0D);
               } else {
                  pos = pos.func_72441_c(0.0D, 0.0D, -12.0D);
               }
            }

            Vec3d v = MCH_Lib.RotVec3(pos.x, pos.y, pos.z, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
            seat.posX = entity.posX = this.posX + v.x;
            seat.posY = entity.posY = this.posY + v.y;
            seat.posZ = entity.posZ = this.posZ + v.z;
            MCH_EntityAircraft.UnmountReserve ur = new MCH_EntityAircraft.UnmountReserve(this, entity, entity.posX, entity.posY, entity.posZ);
            ur.cnt = 8;
            this.listUnmountReserve.add(ur);
            entity.func_184210_p();
            if (MCH_Lib.getBlockIdY(this, 3, -20) > 0) {
               MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.unmountEntityFromRack:%d:%s", sid, entity);
            } else {
               MCH_Lib.DbgLog(this.world, "MCH_EntityAircraft.unmountEntityFromRack:%d Parachute:%s", sid, entity);
               this.dropEntityParachute(entity);
            }
            break;
         }
      }

   }

   public void dropEntityParachute(Entity entity) {
      entity.field_70159_w = this.field_70159_w;
      entity.field_70181_x = this.field_70181_x;
      entity.field_70179_y = this.field_70179_y;
      MCH_EntityParachute parachute = new MCH_EntityParachute(this.world, entity.posX, entity.posY, entity.posZ);
      parachute.field_70177_z = entity.field_70177_z;
      parachute.field_70159_w = entity.field_70159_w;
      parachute.field_70181_x = entity.field_70181_x;
      parachute.field_70179_y = entity.field_70179_y;
      parachute.field_70143_R = entity.field_70143_R;
      parachute.user = entity;
      parachute.setType(3);
      this.world.func_72838_d(parachute);
   }

   public void rideRack() {
      if (this.func_184187_bx() == null) {
         AxisAlignedBB bb = this.func_70046_E();
         List<Entity> list = this.world.func_72839_b(this, bb.func_72314_b(60.0D, 60.0D, 60.0D));

         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (entity instanceof MCH_EntityAircraft) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
               if (ac.getAcInfo() != null) {
                  for(int sid = 0; sid < ac.getSeatNum(); ++sid) {
                     MCH_SeatInfo seatInfo = ac.getSeatInfo(1 + sid);
                     if (seatInfo instanceof MCH_SeatRackInfo && ac.canRideSeatOrRack(1 + sid, entity)) {
                        MCH_SeatRackInfo info = (MCH_SeatRackInfo)seatInfo;
                        MCH_EntitySeat seat = ac.getSeat(sid);
                        if (seat != null && seat.getRiddenByEntity() == null) {
                           Vec3d v = ac.getTransformedPosition(info.getEntryPos());
                           float r = info.range;
                           if (this.posX >= v.x - (double)r && this.posX <= v.x + (double)r && this.posY >= v.y - (double)r && this.posY <= v.y + (double)r && this.posZ >= v.z - (double)r && this.posZ <= v.z + (double)r && this.canRideAircraft(ac, sid, info)) {
                              W_WorldFunc.DEF_playSoundEffect(this.world, this.posX, this.posY, this.posZ, "random.click", 1.0F, 1.0F);
                              this.func_184220_m(seat);
                              return;
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public boolean canPutToRack() {
      for(int i = 0; i < this.getSeatNum(); ++i) {
         MCH_EntitySeat seat = this.getSeat(i);
         MCH_SeatInfo seatInfo = this.getSeatInfo(i + 1);
         if (seat != null && seat.getRiddenByEntity() == null && seatInfo instanceof MCH_SeatRackInfo) {
            return true;
         }
      }

      return false;
   }

   public boolean canDownFromRack() {
      for(int i = 0; i < this.getSeatNum(); ++i) {
         MCH_EntitySeat seat = this.getSeat(i);
         MCH_SeatInfo seatInfo = this.getSeatInfo(i + 1);
         if (seat != null && seat.getRiddenByEntity() != null && seatInfo instanceof MCH_SeatRackInfo) {
            return true;
         }
      }

      return false;
   }

   public void checkRideRack() {
      if (this.getCountOnUpdate() % 10 == 0) {
         this.canRideRackStatus = false;
         if (this.func_184187_bx() == null) {
            AxisAlignedBB bb = this.func_70046_E();
            List<Entity> list = this.world.func_72839_b(this, bb.func_72314_b(60.0D, 60.0D, 60.0D));

            for(int i = 0; i < list.size(); ++i) {
               Entity entity = (Entity)list.get(i);
               if (entity instanceof MCH_EntityAircraft) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
                  if (ac.getAcInfo() != null) {
                     for(int sid = 0; sid < ac.getSeatNum(); ++sid) {
                        MCH_SeatInfo seatInfo = ac.getSeatInfo(1 + sid);
                        if (seatInfo instanceof MCH_SeatRackInfo) {
                           MCH_SeatRackInfo info = (MCH_SeatRackInfo)seatInfo;
                           MCH_EntitySeat seat = ac.getSeat(sid);
                           if (seat != null && seat.getRiddenByEntity() == null) {
                              Vec3d v = ac.getTransformedPosition(info.getEntryPos());
                              float r = info.range;
                              if (this.posX >= v.x - (double)r && this.posX <= v.x + (double)r && this.posY >= v.y - (double)r && this.posY <= v.y + (double)r && this.posZ >= v.z - (double)r && this.posZ <= v.z + (double)r && this.canRideAircraft(ac, sid, info)) {
                                 this.canRideRackStatus = true;
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public boolean canRideRack() {
      return this.func_184187_bx() == null && this.canRideRackStatus;
   }

   public boolean canRideAircraft(MCH_EntityAircraft ac, int seatID, MCH_SeatRackInfo info) {
      if (this.getAcInfo() == null) {
         return false;
      } else if (ac.func_184187_bx() != null) {
         return false;
      } else if (this.func_184187_bx() != null) {
         return false;
      } else {
         boolean canRide = false;
         String[] var5 = info.names;
         int var6 = var5.length;

         int id;
         for(id = 0; id < var6; ++id) {
            String s = var5[id];
            if (s.equalsIgnoreCase(this.getAcInfo().name) || s.equalsIgnoreCase(this.getAcInfo().getKindName())) {
               canRide = true;
               break;
            }
         }

         MCH_EntitySeat seat;
         if (!canRide) {
            Iterator var9 = this.getAcInfo().rideRacks.iterator();

            while(var9.hasNext()) {
               MCH_AircraftInfo.RideRack rr = (MCH_AircraftInfo.RideRack)var9.next();
               id = ac.getAcInfo().getNumSeat() - 1 + (rr.rackID - 1);
               if (id == seatID && rr.name.equalsIgnoreCase(ac.getAcInfo().name)) {
                  seat = ac.getSeat(ac.getAcInfo().getNumSeat() - 1 + rr.rackID - 1);
                  if (seat != null && seat.getRiddenByEntity() == null) {
                     canRide = true;
                     break;
                  }
               }
            }

            if (!canRide) {
               return false;
            }
         }

         MCH_EntitySeat[] var10 = this.getSeats();
         var6 = var10.length;

         for(id = 0; id < var6; ++id) {
            seat = var10[id];
            if (seat != null && seat.getRiddenByEntity() instanceof MCH_IEntityCanRideAircraft) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean isMountedEntity(@Nullable Entity entity) {
      return entity == null ? false : this.isMountedEntity(W_Entity.getEntityId(entity));
   }

   @Nullable
   public EntityPlayer getFirstMountPlayer() {
      if (this.getRiddenByEntity() instanceof EntityPlayer) {
         return (EntityPlayer)this.getRiddenByEntity();
      } else {
         MCH_EntitySeat[] var1 = this.getSeats();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MCH_EntitySeat seat = var1[var3];
            if (seat != null && seat.getRiddenByEntity() instanceof EntityPlayer) {
               return (EntityPlayer)seat.getRiddenByEntity();
            }
         }

         return null;
      }
   }

   public boolean isMountedSameTeamEntity(@Nullable EntityLivingBase player) {
      if (player != null && player.getTeam() != null) {
         if (this.getRiddenByEntity() instanceof EntityLivingBase && player.func_184191_r(this.getRiddenByEntity())) {
            return true;
         } else {
            MCH_EntitySeat[] var2 = this.getSeats();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               MCH_EntitySeat seat = var2[var4];
               if (seat != null && seat.getRiddenByEntity() instanceof EntityLivingBase && player.func_184191_r(seat.getRiddenByEntity())) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isMountedOtherTeamEntity(@Nullable EntityLivingBase player) {
      if (player == null) {
         return false;
      } else {
         EntityLivingBase target = null;
         if (this.getRiddenByEntity() instanceof EntityLivingBase) {
            target = (EntityLivingBase)this.getRiddenByEntity();
            if (player.getTeam() != null && target.getTeam() != null && !player.func_184191_r(target)) {
               return true;
            }
         }

         MCH_EntitySeat[] var3 = this.getSeats();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MCH_EntitySeat seat = var3[var5];
            if (seat != null && seat.getRiddenByEntity() instanceof EntityLivingBase) {
               target = (EntityLivingBase)seat.getRiddenByEntity();
               if (player.getTeam() != null && target.getTeam() != null && !player.func_184191_r(target)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean isMountedEntity(int entityId) {
      if (W_Entity.getEntityId(this.getRiddenByEntity()) == entityId) {
         return true;
      } else {
         MCH_EntitySeat[] var2 = this.getSeats();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            MCH_EntitySeat seat = var2[var4];
            if (seat != null && seat.getRiddenByEntity() != null && W_Entity.getEntityId(seat.getRiddenByEntity()) == entityId) {
               return true;
            }
         }

         return false;
      }
   }

   public void onInteractFirst(EntityPlayer player) {
   }

   public boolean checkTeam(EntityPlayer player) {
      for(int i = 0; i < 1 + this.getSeatNum(); ++i) {
         Entity entity = this.getEntityBySeatId(i);
         if (entity instanceof EntityPlayer || entity instanceof MCH_EntityGunner) {
            EntityLivingBase riddenEntity = (EntityLivingBase)entity;
            if (riddenEntity.getTeam() != null && !riddenEntity.func_184191_r(player)) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean processInitialInteract(EntityPlayer player, boolean ss, EnumHand hand) {
      this.switchSeat = ss;
      boolean ret = this.func_184230_a(player, hand);
      this.switchSeat = false;
      return ret;
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      if (this.isDestroyed()) {
         return false;
      } else if (this.getAcInfo() == null) {
         return false;
      } else if (!this.checkTeam(player)) {
         return false;
      } else {
         ItemStack itemStack = player.func_184586_b(hand);
         if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemWrench) {
            if (!this.world.isRemote && player.func_70093_af()) {
               this.switchNextTextureName();
            }

            return false;
         } else if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemSpawnGunner) {
            return false;
         } else if (player.func_70093_af()) {
            super.displayInventory(player);
            return false;
         } else if (!this.getAcInfo().canRide) {
            return false;
         } else if (this.getRiddenByEntity() == null && !this.isUAV()) {
            if (player.func_184187_bx() instanceof MCH_EntitySeat) {
               return false;
            } else if (!this.canRideSeatOrRack(0, player)) {
               return false;
            } else {
               if (!this.switchSeat) {
                  if (this.getAcInfo().haveCanopy() && this.isCanopyClose()) {
                     this.openCanopy();
                     return false;
                  }

                  if (this.getModeSwitchCooldown() > 0) {
                     return false;
                  }
               }

               this.closeCanopy();
               this.lastRiddenByEntity = null;
               this.initRadar();
               if (!this.world.isRemote) {
                  player.func_184220_m(this);
                  if (!this.keepOnRideRotation) {
                     this.mountMobToSeats();
                  }
               } else {
                  this.updateClientSettings(0);
               }

               this.setCameraId(0);
               this.initPilotWeapon();
               this.lowPassPartialTicks.clear();
               if (this.getAcInfo().name.equalsIgnoreCase("uh-1c") && player instanceof EntityPlayerMP) {
                  MCH_CriteriaTriggers.RIDING_VALKYRIES.trigger((EntityPlayerMP)player);
               }

               this.onInteractFirst(player);
               return true;
            }
         } else {
            return this.interactFirstSeat(player);
         }
      }
   }

   public boolean canRideSeatOrRack(int seatId, Entity entity) {
      if (this.getAcInfo() == null) {
         return false;
      } else {
         Iterator var3 = this.getAcInfo().exclusionSeatList.iterator();

         while(true) {
            Integer[] a;
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               a = (Integer[])var3.next();
            } while(!Arrays.asList(a).contains(seatId));

            Integer[] arr$ = a;
            int len$ = a.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               int id = arr$[i$];
               if (this.getEntityBySeatId(id) != null) {
                  return false;
               }
            }
         }
      }
   }

   public void updateClientSettings(int seatId) {
      this.cs_dismountAll = MCH_Config.DismountAll.prmBool;
      this.cs_heliAutoThrottleDown = MCH_Config.AutoThrottleDownHeli.prmBool;
      this.cs_planeAutoThrottleDown = MCH_Config.AutoThrottleDownPlane.prmBool;
      this.cs_tankAutoThrottleDown = MCH_Config.AutoThrottleDownTank.prmBool;
      this.camera.setShaderSupport(seatId, W_EntityRenderer.isShaderSupport());
      MCH_PacketNotifyClientSetting.send();
   }

   public boolean canLockEntity(Entity entity) {
      return !this.isMountedEntity(entity);
   }

   public void switchNextSeat(Entity entity) {
      if (entity != null) {
         if (this.seats != null && this.seats.length > 0) {
            if (this.isMountedEntity(entity)) {
               boolean isFound = false;
               int sid = 1;
               MCH_EntitySeat[] var4 = this.seats;
               int var5 = var4.length;

               int var6;
               MCH_EntitySeat seat;
               for(var6 = 0; var6 < var5; ++var6) {
                  seat = var4[var6];
                  if (seat != null) {
                     if (this.getSeatInfo(sid) instanceof MCH_SeatRackInfo) {
                        break;
                     }

                     if (W_Entity.isEqual(seat.getRiddenByEntity(), entity)) {
                        isFound = true;
                     } else if (isFound && seat.getRiddenByEntity() == null) {
                        entity.func_184220_m(seat);
                        return;
                     }

                     ++sid;
                  }
               }

               sid = 1;
               var4 = this.seats;
               var5 = var4.length;

               for(var6 = 0; var6 < var5; ++var6) {
                  seat = var4[var6];
                  if (seat != null && seat.getRiddenByEntity() == null) {
                     if (!(this.getSeatInfo(sid) instanceof MCH_SeatRackInfo)) {
                        this.onMountPlayerSeat(seat, entity);
                        return;
                     }
                     break;
                  }

                  ++sid;
               }

            }
         }
      }
   }

   public void switchPrevSeat(Entity entity) {
      if (entity != null) {
         if (this.seats != null && this.seats.length > 0) {
            if (this.isMountedEntity(entity)) {
               boolean isFound = false;

               int i;
               MCH_EntitySeat seat;
               for(i = this.seats.length - 1; i >= 0; --i) {
                  seat = this.seats[i];
                  if (seat != null) {
                     if (W_Entity.isEqual(seat.getRiddenByEntity(), entity)) {
                        isFound = true;
                     } else if (isFound && seat.getRiddenByEntity() == null) {
                        entity.func_184220_m(seat);
                        return;
                     }
                  }
               }

               for(i = this.seats.length - 1; i >= 0; --i) {
                  seat = this.seats[i];
                  if (!(this.getSeatInfo(i + 1) instanceof MCH_SeatRackInfo) && seat != null && seat.getRiddenByEntity() == null) {
                     entity.func_184220_m(seat);
                     return;
                  }
               }

            }
         }
      }
   }

   public Entity[] func_70021_al() {
      return this.partEntities;
   }

   public float getSoundVolume() {
      return 1.0F;
   }

   public float getSoundPitch() {
      return 1.0F;
   }

   public abstract String getDefaultSoundName();

   public String getSoundName() {
      if (this.getAcInfo() == null) {
         return "";
      } else {
         return !this.getAcInfo().soundMove.isEmpty() ? this.getAcInfo().soundMove : this.getDefaultSoundName();
      }
   }

   public boolean isSkipNormalRender() {
      return this.func_184187_bx() instanceof MCH_EntitySeat;
   }

   public boolean isRenderBullet(Entity entity, Entity rider) {
      return !this.isCameraView(rider) || !W_Entity.isEqual(this.getTVMissile(), entity) || !W_Entity.isEqual(this.getTVMissile().shootingEntity, rider);
   }

   public boolean isCameraView(Entity entity) {
      return this.getIsGunnerMode(entity) || this.isUAV();
   }

   public void updateCamera(double x, double y, double z) {
      if (this.world.isRemote) {
         if (this.getTVMissile() != null) {
            this.camera.setPosition(this.TVmissile.posX, this.TVmissile.posY, this.TVmissile.posZ);
            this.camera.setCameraZoom(1.0F);
            this.TVmissile.isSpawnParticle = !this.isMissileCameraMode(this.TVmissile.shootingEntity);
         } else {
            this.setTVMissile((MCH_EntityTvMissile)null);
            MCH_AircraftInfo.CameraPosition cpi = this.getCameraPosInfo();
            Vec3d cp = cpi != null ? cpi.pos : Vec3d.ZERO;
            Vec3d v = MCH_Lib.RotVec3(cp, -this.getRotYaw(), -this.getRotPitch(), -this.getRotRoll());
            this.camera.setPosition(x + v.x, y + v.y, z + v.z);
         }

      }
   }

   public void updateCameraRotate(float yaw, float pitch) {
      this.camera.prevRotationYaw = this.camera.rotationYaw;
      this.camera.prevRotationPitch = this.camera.rotationPitch;
      this.camera.rotationYaw = yaw;
      this.camera.rotationPitch = pitch;
   }

   public void updatePartCameraRotate() {
      if (this.world.isRemote) {
         Entity e = this.getEntityBySeatId(1);
         if (e == null) {
            e = this.getRiddenByEntity();
         }

         if (e != null) {
            this.camera.partRotationYaw = e.field_70177_z;
            float pitch = e.field_70125_A;
            this.camera.prevPartRotationYaw = this.camera.partRotationYaw;
            this.camera.prevPartRotationPitch = this.camera.partRotationPitch;
            this.camera.partRotationPitch = pitch;
         }
      }

   }

   public void setTVMissile(MCH_EntityTvMissile entity) {
      this.TVmissile = entity;
   }

   @Nullable
   public MCH_EntityTvMissile getTVMissile() {
      return this.TVmissile != null && !this.TVmissile.field_70128_L ? this.TVmissile : null;
   }

   public MCH_WeaponSet[] createWeapon(int seat_num) {
      this.currentWeaponID = new int[seat_num];

      for(int i = 0; i < this.currentWeaponID.length; ++i) {
         this.currentWeaponID[i] = -1;
      }

      if (this.getAcInfo() != null && this.getAcInfo().weaponSetList.size() > 0 && seat_num > 0) {
         MCH_WeaponSet[] weaponSetArray = new MCH_WeaponSet[this.getAcInfo().weaponSetList.size()];

         for(int i = 0; i < this.getAcInfo().weaponSetList.size(); ++i) {
            MCH_AircraftInfo.WeaponSet ws = (MCH_AircraftInfo.WeaponSet)this.getAcInfo().weaponSetList.get(i);
            MCH_WeaponBase[] wb = new MCH_WeaponBase[ws.weapons.size()];

            for(int j = 0; j < ws.weapons.size(); ++j) {
               wb[j] = MCH_WeaponCreator.createWeapon(this.world, ws.type, ((MCH_AircraftInfo.Weapon)ws.weapons.get(j)).pos, ((MCH_AircraftInfo.Weapon)ws.weapons.get(j)).yaw, ((MCH_AircraftInfo.Weapon)ws.weapons.get(j)).pitch, this, ((MCH_AircraftInfo.Weapon)ws.weapons.get(j)).turret);
               wb[j].aircraft = this;
            }

            if (wb.length > 0 && wb[0] != null) {
               float defYaw = ((MCH_AircraftInfo.Weapon)ws.weapons.get(0)).defaultYaw;
               weaponSetArray[i] = new MCH_WeaponSet(wb);
               weaponSetArray[i].prevRotationYaw = defYaw;
               weaponSetArray[i].rotationYaw = defYaw;
               weaponSetArray[i].defaultRotationYaw = defYaw;
            }
         }

         return weaponSetArray;
      } else {
         return new MCH_WeaponSet[]{this.dummyWeapon};
      }
   }

   public void switchWeapon(Entity entity, int id) {
      int sid = this.getSeatIdByEntity(entity);
      if (this.isValidSeatID(sid)) {
         if (this.getWeaponNum() > 0 && this.currentWeaponID.length > 0) {
            if (id < 0) {
               this.currentWeaponID[sid] = -1;
            }

            if (id >= this.getWeaponNum()) {
               id = this.getWeaponNum() - 1;
            }

            MCH_Lib.DbgLog(this.world, "switchWeapon:" + W_Entity.getEntityId(entity) + " -> " + id);
            this.getCurrentWeapon(entity).reload();
            this.currentWeaponID[sid] = id;
            MCH_WeaponSet ws = this.getCurrentWeapon(entity);
            ws.onSwitchWeapon(this.world.isRemote, this.isInfinityAmmo(entity));
            if (!this.world.isRemote) {
               MCH_PacketNotifyWeaponID.send(this, sid, id, ws.getAmmoNum(), ws.getRestAllAmmoNum());
            }

         }
      }
   }

   public void updateWeaponID(int sid, int id) {
      if (sid >= 0 && sid < this.currentWeaponID.length) {
         if (this.getWeaponNum() > 0 && this.currentWeaponID.length > 0) {
            if (id < 0) {
               this.currentWeaponID[sid] = -1;
            }

            if (id >= this.getWeaponNum()) {
               id = this.getWeaponNum() - 1;
            }

            MCH_Lib.DbgLog(this.world, "switchWeapon:seatID=" + sid + ", WeaponID=" + id);
            this.currentWeaponID[sid] = id;
         }
      }
   }

   public void updateWeaponRestAmmo(int id, int num) {
      if (id < this.getWeaponNum()) {
         this.getWeapon(id).setRestAllAmmoNum(num);
      }

   }

   @Nullable
   public MCH_WeaponSet getWeaponByName(String name) {
      MCH_WeaponSet[] var2 = this.weapons;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MCH_WeaponSet ws = var2[var4];
         if (ws.isEqual(name)) {
            return ws;
         }
      }

      return null;
   }

   public int getWeaponIdByName(String name) {
      int id = 0;
      MCH_WeaponSet[] var3 = this.weapons;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         MCH_WeaponSet ws = var3[var5];
         if (ws.isEqual(name)) {
            return id;
         }

         ++id;
      }

      return -1;
   }

   public void reloadAllWeapon() {
      for(int i = 0; i < this.getWeaponNum(); ++i) {
         this.getWeapon(i).reloadMag();
      }

   }

   public MCH_WeaponSet getFirstSeatWeapon() {
      return this.currentWeaponID != null && this.currentWeaponID.length > 0 && this.currentWeaponID[0] >= 0 ? this.getWeapon(this.currentWeaponID[0]) : this.getWeapon(0);
   }

   public void initCurrentWeapon(Entity entity) {
      int sid = this.getSeatIdByEntity(entity);
      MCH_Lib.DbgLog(this.world, "initCurrentWeapon:" + W_Entity.getEntityId(entity) + ":%d", sid);
      if (sid >= 0 && sid < this.currentWeaponID.length) {
         this.currentWeaponID[sid] = -1;
         if (entity instanceof EntityPlayer || entity instanceof MCH_EntityGunner) {
            this.currentWeaponID[sid] = this.getNextWeaponID(entity, 1);
            this.switchWeapon(entity, this.getCurrentWeaponID(entity));
            if (this.world.isRemote) {
               MCH_PacketIndNotifyAmmoNum.send(this, -1);
            }
         }

      }
   }

   public void initPilotWeapon() {
      this.currentWeaponID[0] = -1;
   }

   public MCH_WeaponSet getCurrentWeapon(Entity entity) {
      return this.getWeapon(this.getCurrentWeaponID(entity));
   }

   protected MCH_WeaponSet getWeapon(int id) {
      return id >= 0 && this.weapons.length > 0 && id < this.weapons.length ? this.weapons[id] : this.dummyWeapon;
   }

   public int getWeaponIDBySeatID(int sid) {
      return sid >= 0 && sid < this.currentWeaponID.length ? this.currentWeaponID[sid] : -1;
   }

   public double getLandInDistance(Entity user) {
      if (this.lastCalcLandInDistanceCount != (double)this.getCountOnUpdate() && this.getCountOnUpdate() % 5 == 0) {
         this.lastCalcLandInDistanceCount = (double)this.getCountOnUpdate();
         MCH_WeaponParam prm = new MCH_WeaponParam();
         prm.setPosition(this.posX, this.posY, this.posZ);
         prm.entity = this;
         prm.user = user;
         prm.isInfinity = this.isInfinityAmmo(prm.user);
         if (prm.user != null) {
            MCH_WeaponSet currentWs = this.getCurrentWeapon(prm.user);
            if (currentWs != null) {
               int sid = this.getSeatIdByEntity(prm.user);
               if (this.getAcInfo().getWeaponSetById(sid) != null) {
                  prm.isTurret = ((MCH_AircraftInfo.Weapon)this.getAcInfo().getWeaponSetById(sid).weapons.get(0)).turret;
               }

               this.lastLandInDistance = currentWs.getLandInDistance(prm);
            }
         }
      }

      return this.lastLandInDistance;
   }

   public boolean useCurrentWeapon(Entity user) {
      MCH_WeaponParam prm = new MCH_WeaponParam();
      prm.setPosition(this.posX, this.posY, this.posZ);
      prm.entity = this;
      prm.user = user;
      return this.useCurrentWeapon(prm);
   }

   public boolean useCurrentWeapon(MCH_WeaponParam prm) {
      prm.isInfinity = this.isInfinityAmmo(prm.user);
      if (prm.user != null) {
         MCH_WeaponSet currentWs = this.getCurrentWeapon(prm.user);
         if (currentWs != null && currentWs.canUse()) {
            int sid = this.getSeatIdByEntity(prm.user);
            if (this.getAcInfo().getWeaponSetById(sid) != null) {
               prm.isTurret = ((MCH_AircraftInfo.Weapon)this.getAcInfo().getWeaponSetById(sid).weapons.get(0)).turret;
            }

            int lastUsedIndex = currentWs.getCurrentWeaponIndex();
            if (currentWs.use(prm)) {
               MCH_WeaponSet[] var5 = this.weapons;
               int var6 = var5.length;

               int var7;
               for(var7 = 0; var7 < var6; ++var7) {
                  MCH_WeaponSet ws = var5[var7];
                  if (ws != currentWs && !ws.getInfo().group.isEmpty() && ws.getInfo().group.equals(currentWs.getInfo().group)) {
                     ws.waitAndReloadByOther(prm.reload);
                  }
               }

               if (!this.world.isRemote) {
                  int shift = 0;
                  MCH_WeaponSet[] var11 = this.weapons;
                  var7 = var11.length;

                  for(int var12 = 0; var12 < var7; ++var12) {
                     MCH_WeaponSet ws = var11[var12];
                     if (ws == currentWs) {
                        break;
                     }

                     shift += ws.getWeaponNum();
                  }

                  shift += lastUsedIndex;
                  this.useWeaponStat |= shift < 32 ? 1 << shift : 0;
               }

               return true;
            }
         }
      }

      return false;
   }

   public void switchCurrentWeaponMode(Entity entity) {
      this.getCurrentWeapon(entity).switchMode();
   }

   public int getWeaponNum() {
      return this.weapons.length;
   }

   public int getCurrentWeaponID(Entity entity) {
      if (!(entity instanceof EntityPlayer) && !(entity instanceof MCH_EntityGunner)) {
         return -1;
      } else {
         int id = this.getSeatIdByEntity(entity);
         return id >= 0 && id < this.currentWeaponID.length ? this.currentWeaponID[id] : -1;
      }
   }

   public int getNextWeaponID(Entity entity, int step) {
      if (this.getAcInfo() == null) {
         return -1;
      } else {
         int sid = this.getSeatIdByEntity(entity);
         if (sid < 0) {
            return -1;
         } else {
            int id = this.getCurrentWeaponID(entity);

            int i;
            for(i = 0; i < this.getWeaponNum(); ++i) {
               if (step >= 0) {
                  id = (id + 1) % this.getWeaponNum();
               } else {
                  id = id > 0 ? id - 1 : this.getWeaponNum() - 1;
               }

               MCH_AircraftInfo.Weapon w = this.getAcInfo().getWeaponById(id);
               if (w != null) {
                  MCH_WeaponInfo wi = this.getWeaponInfoById(id);
                  int wpsid = this.getWeaponSeatID(wi, w);
                  if (wpsid < this.getSeatNum() + 1 + 1 && (wpsid == sid || sid == 0 && w.canUsePilot && !(this.getEntityBySeatId(wpsid) instanceof EntityPlayer) && !(this.getEntityBySeatId(wpsid) instanceof MCH_EntityGunner))) {
                     break;
                  }
               }
            }

            if (i >= this.getWeaponNum()) {
               return -1;
            } else {
               MCH_Lib.DbgLog(this.world, "getNextWeaponID:%d:->%d", W_Entity.getEntityId(entity), id);
               return id;
            }
         }
      }
   }

   public int getWeaponSeatID(MCH_WeaponInfo wi, MCH_AircraftInfo.Weapon w) {
      return wi == null || (wi.target & 195) != 0 || !wi.type.isEmpty() || !MCH_MOD.proxy.isSinglePlayer() && !MCH_Config.TestMode.prmBool ? w.seatID : 1000;
   }

   public boolean isMissileCameraMode(Entity entity) {
      return this.getTVMissile() != null && this.isCameraView(entity);
   }

   public boolean isPilotReloading() {
      return this.getCommonStatus(2) || this.supplyAmmoWait > 0;
   }

   public int getUsedWeaponStat() {
      if (this.getAcInfo() == null) {
         return 0;
      } else if (this.getAcInfo().getWeaponNum() <= 0) {
         return 0;
      } else {
         int stat = 0;
         int i = 0;
         MCH_WeaponSet[] var3 = this.weapons;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MCH_WeaponSet w = var3[var5];
            if (i >= 32) {
               break;
            }

            for(int wi = 0; wi < w.getWeaponNum() && i < 32; ++wi) {
               stat |= w.isUsed(wi) ? 1 << i : 0;
               ++i;
            }
         }

         return stat;
      }
   }

   public boolean isWeaponNotCooldown(MCH_WeaponSet checkWs, int index) {
      if (this.getAcInfo() == null) {
         return false;
      } else if (this.getAcInfo().getWeaponNum() <= 0) {
         return false;
      } else {
         int shift = 0;
         MCH_WeaponSet[] var4 = this.weapons;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            MCH_WeaponSet ws = var4[var6];
            if (ws == checkWs) {
               break;
            }

            shift += ws.getWeaponNum();
         }

         shift += index;
         return (this.useWeaponStat & 1 << shift) != 0;
      }
   }

   public void updateWeapons() {
      if (this.getAcInfo() != null) {
         if (this.getAcInfo().getWeaponNum() > 0) {
            int prevUseWeaponStat = this.useWeaponStat;
            if (!this.world.isRemote) {
               this.useWeaponStat |= this.getUsedWeaponStat();
               this.dataManager.func_187227_b(USE_WEAPON, this.useWeaponStat);
               this.useWeaponStat = 0;
            } else {
               this.useWeaponStat = (Integer)this.dataManager.func_187225_a(USE_WEAPON);
            }

            float yaw = MathHelper.func_76142_g(this.getRotYaw());
            float pitch = MathHelper.func_76142_g(this.getRotPitch());
            int id = 0;

            for(int wid = 0; wid < this.weapons.length; ++wid) {
               MCH_WeaponSet w = this.weapons[wid];
               boolean isLongDelay = false;
               if (w.getFirstWeapon() != null) {
                  isLongDelay = w.isLongDelayWeapon();
               }

               boolean isSelected = false;
               int[] var9 = this.currentWeaponID;
               int index = var9.length;

               for(int var11 = 0; var11 < index; ++var11) {
                  int swid = var9[var11];
                  if (swid == wid) {
                     isSelected = true;
                     break;
                  }
               }

               boolean isWpnUsed = false;

               float ey;
               for(index = 0; index < w.getWeaponNum(); ++index) {
                  boolean isPrevUsed = id < 32 && (prevUseWeaponStat & 1 << id) != 0;
                  boolean isUsed = id < 32 && (this.useWeaponStat & 1 << id) != 0;
                  if (isLongDelay && isPrevUsed && isUsed) {
                     isUsed = false;
                  }

                  isWpnUsed |= isUsed;
                  if (!isPrevUsed && isUsed) {
                     ey = w.getInfo().recoil;
                     if (ey > 0.0F) {
                        this.recoilCount = 30;
                        this.recoilValue = ey;
                        this.recoilYaw = w.rotationYaw;
                     }
                  }

                  if (this.world.isRemote && isUsed) {
                     Vec3d wrv = MCH_Lib.RotVec3(0.0D, 0.0D, -1.0D, -w.rotationYaw - yaw, -w.rotationPitch);
                     Vec3d spv = w.getCurrentWeapon().getShotPos(this);
                     this.spawnParticleMuzzleFlash(this.world, w.getInfo(), this.posX + spv.x, this.posY + spv.y, this.posZ + spv.z, wrv);
                  }

                  w.updateWeapon(this, isUsed, index);
                  ++id;
               }

               w.update(this, isSelected, isWpnUsed);
               MCH_AircraftInfo.Weapon wi = this.getAcInfo().getWeaponById(wid);
               if (wi != null && !this.isDestroyed()) {
                  Entity entity = this.getEntityBySeatId(this.getWeaponSeatID(this.getWeaponInfoById(wid), wi));
                  if (wi.canUsePilot && !(entity instanceof EntityPlayer) && !(entity instanceof MCH_EntityGunner)) {
                     entity = this.getEntityBySeatId(0);
                  }

                  if (!(entity instanceof EntityPlayer) && !(entity instanceof MCH_EntityGunner)) {
                     w.rotationTurretYaw = this.getLastRiderYaw() - this.getRotYaw();
                     if (this.getTowedChainEntity() != null || this.func_184187_bx() != null) {
                        w.rotationYaw = 0.0F;
                     }
                  } else {
                     float ty;
                     if ((int)wi.minYaw != 0 || (int)wi.maxYaw != 0) {
                        ty = wi.turret ? MathHelper.func_76142_g(this.getLastRiderYaw()) - yaw : 0.0F;
                        ey = MathHelper.func_76142_g(entity.field_70177_z - yaw - wi.defaultYaw - ty);
                        if (Math.abs((int)wi.minYaw) < 360 && Math.abs((int)wi.maxYaw) < 360) {
                           float targetYaw = MCH_Lib.RNG(ey, wi.minYaw, wi.maxYaw);
                           float wy = w.rotationYaw - wi.defaultYaw - ty;
                           if (targetYaw < wy) {
                              if (wy - targetYaw > 15.0F) {
                                 wy -= 15.0F;
                              } else {
                                 wy = targetYaw;
                              }
                           } else if (targetYaw > wy) {
                              if (targetYaw - wy > 15.0F) {
                                 wy += 15.0F;
                              } else {
                                 wy = targetYaw;
                              }
                           }

                           w.rotationYaw = wy + wi.defaultYaw + ty;
                        } else {
                           w.rotationYaw = ey + ty;
                        }
                     }

                     ty = MathHelper.func_76142_g(entity.field_70125_A - pitch);
                     w.rotationPitch = MCH_Lib.RNG(ty, wi.minPitch, wi.maxPitch);
                     w.rotationTurretYaw = 0.0F;
                  }
               }
            }

            this.updateWeaponBay();
            if (this.hitStatus > 0) {
               --this.hitStatus;
            }

         }
      }
   }

   public void updateWeaponsRotation() {
      if (this.getAcInfo() != null) {
         if (this.getAcInfo().getWeaponNum() > 0) {
            if (!this.isDestroyed()) {
               float yaw = MathHelper.func_76142_g(this.getRotYaw());
               float pitch = MathHelper.func_76142_g(this.getRotPitch());

               for(int wid = 0; wid < this.weapons.length; ++wid) {
                  MCH_WeaponSet w = this.weapons[wid];
                  MCH_AircraftInfo.Weapon wi = this.getAcInfo().getWeaponById(wid);
                  if (wi != null) {
                     Entity entity = this.getEntityBySeatId(this.getWeaponSeatID(this.getWeaponInfoById(wid), wi));
                     if (wi.canUsePilot && !(entity instanceof EntityPlayer) && !(entity instanceof MCH_EntityGunner)) {
                        entity = this.getEntityBySeatId(0);
                     }

                     if (!(entity instanceof EntityPlayer) && !(entity instanceof MCH_EntityGunner)) {
                        w.rotationTurretYaw = this.getLastRiderYaw() - this.getRotYaw();
                     } else {
                        float ty;
                        if ((int)wi.minYaw != 0 || (int)wi.maxYaw != 0) {
                           ty = wi.turret ? MathHelper.func_76142_g(this.getLastRiderYaw()) - yaw : 0.0F;
                           float ey = MathHelper.func_76142_g(entity.field_70177_z - yaw - wi.defaultYaw - ty);
                           if (Math.abs((int)wi.minYaw) < 360 && Math.abs((int)wi.maxYaw) < 360) {
                              float targetYaw = MCH_Lib.RNG(ey, wi.minYaw, wi.maxYaw);
                              float wy = w.rotationYaw - wi.defaultYaw - ty;
                              if (targetYaw < wy) {
                                 if (wy - targetYaw > 15.0F) {
                                    wy -= 15.0F;
                                 } else {
                                    wy = targetYaw;
                                 }
                              } else if (targetYaw > wy) {
                                 if (targetYaw - wy > 15.0F) {
                                    wy += 15.0F;
                                 } else {
                                    wy = targetYaw;
                                 }
                              }

                              w.rotationYaw = wy + wi.defaultYaw + ty;
                           } else {
                              w.rotationYaw = ey + ty;
                           }
                        }

                        ty = MathHelper.func_76142_g(entity.field_70125_A - pitch);
                        w.rotationPitch = MCH_Lib.RNG(ty, wi.minPitch, wi.maxPitch);
                        w.rotationTurretYaw = 0.0F;
                     }
                  }

                  w.prevRotationYaw = w.rotationYaw;
               }

            }
         }
      }
   }

   private void spawnParticleMuzzleFlash(World w, MCH_WeaponInfo wi, double px, double py, double pz, Vec3d wrv) {
      Iterator var10;
      MCH_WeaponInfo.MuzzleFlash mf;
      if (wi.listMuzzleFlashSmoke != null) {
         var10 = wi.listMuzzleFlashSmoke.iterator();

         while(var10.hasNext()) {
            mf = (MCH_WeaponInfo.MuzzleFlash)var10.next();
            double x = px + -wrv.x * (double)mf.dist;
            double y = py + -wrv.y * (double)mf.dist;
            double z = pz + -wrv.z * (double)mf.dist;
            MCH_ParticleParam p = new MCH_ParticleParam(w, "smoke", px, py, pz);
            p.size = mf.size;

            for(int i = 0; i < mf.num; ++i) {
               p.alpha = mf.a * 0.9F + w.field_73012_v.nextFloat() * 0.1F;
               float color = w.field_73012_v.nextFloat() * 0.1F;
               p.red = color + mf.r * 0.9F;
               p.green = color + mf.g * 0.9F;
               p.blue = color + mf.b * 0.9F;
               p.age = (int)((double)mf.age + 0.1D * (double)mf.age * (double)w.field_73012_v.nextFloat());
               p.posX = x + (w.field_73012_v.nextDouble() - 0.5D) * (double)mf.range;
               p.posY = y + (w.field_73012_v.nextDouble() - 0.5D) * (double)mf.range;
               p.posZ = z + (w.field_73012_v.nextDouble() - 0.5D) * (double)mf.range;
               p.motionX = w.field_73012_v.nextDouble() * (p.posX < x ? -0.2D : 0.2D);
               p.motionY = w.field_73012_v.nextDouble() * (p.posY < y ? -0.03D : 0.03D);
               p.motionZ = w.field_73012_v.nextDouble() * (p.posZ < z ? -0.2D : 0.2D);
               MCH_ParticlesUtil.spawnParticle(p);
            }
         }
      }

      if (wi.listMuzzleFlash != null) {
         var10 = wi.listMuzzleFlash.iterator();

         while(var10.hasNext()) {
            mf = (MCH_WeaponInfo.MuzzleFlash)var10.next();
            float color = this.field_70146_Z.nextFloat() * 0.1F + 0.9F;
            MCH_ParticlesUtil.spawnParticleExplode(this.world, px + -wrv.x * (double)mf.dist, py + -wrv.y * (double)mf.dist, pz + -wrv.z * (double)mf.dist, mf.size, color * mf.r, color * mf.g, color * mf.b, mf.a, mf.age + w.field_73012_v.nextInt(3));
         }
      }

   }

   private void updateWeaponBay() {
      for(int i = 0; i < this.weaponBays.length; ++i) {
         MCH_EntityAircraft.WeaponBay wb = this.weaponBays[i];
         MCH_AircraftInfo.WeaponBay info = (MCH_AircraftInfo.WeaponBay)this.getAcInfo().partWeaponBay.get(i);
         boolean isSelected = false;
         Integer[] arr$ = info.weaponIds;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int wid = arr$[i$];

            for(int sid = 0; sid < this.currentWeaponID.length; ++sid) {
               if (wid == this.currentWeaponID[sid] && this.getEntityBySeatId(sid) != null) {
                  isSelected = true;
               }
            }
         }

         wb.prevRot = wb.rot;
         if (isSelected) {
            if (wb.rot < 90.0F) {
               wb.rot += 3.0F;
            }

            if (wb.rot >= 90.0F) {
               wb.rot = 90.0F;
            }
         } else {
            if (wb.rot > 0.0F) {
               wb.rot -= 3.0F;
            }

            if (wb.rot <= 0.0F) {
               wb.rot = 0.0F;
            }
         }
      }

   }

   public int getHitStatus() {
      return this.hitStatus;
   }

   public int getMaxHitStatus() {
      return 15;
   }

   public void hitBullet() {
      this.hitStatus = this.getMaxHitStatus();
   }

   public void initRotationYaw(float yaw) {
      this.field_70177_z = yaw;
      this.field_70126_B = yaw;
      this.lastRiderYaw = yaw;
      this.lastSearchLightYaw = yaw;
      MCH_WeaponSet[] var2 = this.weapons;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MCH_WeaponSet w = var2[var4];
         w.rotationYaw = w.defaultRotationYaw;
         w.rotationPitch = 0.0F;
      }

   }

   @Nullable
   public MCH_AircraftInfo getAcInfo() {
      return this.acInfo;
   }

   @Nullable
   public abstract Item getItem();

   public void setAcInfo(@Nullable MCH_AircraftInfo info) {
      this.acInfo = info;
      if (info != null) {
         this.partHatch = this.createHatch();
         this.partCanopy = this.createCanopy();
         this.partLandingGear = this.createLandingGear();
         this.weaponBays = this.createWeaponBays();
         this.rotPartRotation = new float[info.partRotPart.size()];
         this.prevRotPartRotation = new float[info.partRotPart.size()];
         this.extraBoundingBox = this.createExtraBoundingBox();
         this.partEntities = this.createParts();
         this.field_70138_W = info.stepHeight;
      }

   }

   public MCH_BoundingBox[] createExtraBoundingBox() {
      MCH_BoundingBox[] ar = new MCH_BoundingBox[this.getAcInfo().extraBoundingBox.size()];
      int i = 0;

      for(Iterator var3 = this.getAcInfo().extraBoundingBox.iterator(); var3.hasNext(); ++i) {
         MCH_BoundingBox bb = (MCH_BoundingBox)var3.next();
         ar[i] = bb.copy();
      }

      return ar;
   }

   public Entity[] createParts() {
      Entity[] list = new Entity[]{this.partEntities[0]};
      return list;
   }

   public void updateUAV() {
      if (this.isUAV()) {
         if (this.world.isRemote) {
            int eid = (Integer)this.dataManager.func_187225_a(UAV_STATION);
            if (eid > 0) {
               if (this.uavStation == null) {
                  Entity uavEntity = this.world.func_73045_a(eid);
                  if (uavEntity instanceof MCH_EntityUavStation) {
                     this.uavStation = (MCH_EntityUavStation)uavEntity;
                     this.uavStation.setControlAircract(this);
                  }
               }
            } else if (this.uavStation != null) {
               this.uavStation.setControlAircract((MCH_EntityAircraft)null);
               this.uavStation = null;
            }
         } else if (this.uavStation != null) {
            double udx = this.posX - this.uavStation.posX;
            double udz = this.posZ - this.uavStation.posZ;
            if (udx * udx + udz * udz > 15129.0D) {
               this.uavStation.setControlAircract((MCH_EntityAircraft)null);
               this.setUavStation((MCH_EntityUavStation)null);
               this.attackEntityFrom(DamageSource.field_76380_i, this.getMaxHP() + 10);
            }
         }

         if (this.uavStation != null && this.uavStation.field_70128_L) {
            this.uavStation = null;
         }

      }
   }

   public void switchGunnerMode(boolean mode) {
      boolean debug_bk_mode = this.isGunnerMode;
      Entity pilot = this.getEntityBySeatId(0);
      if (!mode || this.canSwitchGunnerMode()) {
         if (this.isGunnerMode && !mode) {
            this.setCurrentThrottle(this.beforeHoverThrottle);
            this.isGunnerMode = false;
            this.camera.setCameraZoom(1.0F);
            this.getCurrentWeapon(pilot).onSwitchWeapon(this.world.isRemote, this.isInfinityAmmo(pilot));
         } else if (!this.isGunnerMode && mode) {
            this.beforeHoverThrottle = this.getCurrentThrottle();
            this.isGunnerMode = true;
            this.camera.setCameraZoom(1.0F);
            this.getCurrentWeapon(pilot).onSwitchWeapon(this.world.isRemote, this.isInfinityAmmo(pilot));
         }
      }

      MCH_Lib.DbgLog(this.world, "switchGunnerMode %s->%s", debug_bk_mode ? "ON" : "OFF", mode ? "ON" : "OFF");
   }

   public boolean canSwitchGunnerMode() {
      if (this.getAcInfo() != null && this.getAcInfo().isEnableGunnerMode) {
         if (!this.isCanopyClose()) {
            return false;
         } else if (!this.getAcInfo().isEnableConcurrentGunnerMode && this.getEntityBySeatId(1) instanceof EntityPlayer) {
            return false;
         } else {
            return !this.isHoveringMode();
         }
      } else {
         return false;
      }
   }

   public boolean canSwitchGunnerModeOtherSeat(EntityPlayer player) {
      int sid = this.getSeatIdByEntity(player);
      if (sid > 0) {
         MCH_SeatInfo info = this.getSeatInfo(sid);
         if (info != null) {
            return info.gunner && info.switchgunner;
         }
      }

      return false;
   }

   public void switchGunnerModeOtherSeat(EntityPlayer player) {
      this.isGunnerModeOtherSeat = !this.isGunnerModeOtherSeat;
   }

   public boolean isHoveringMode() {
      return this.isHoveringMode;
   }

   public void switchHoveringMode(boolean mode) {
      this.stopRepelling();
      if (this.canSwitchHoveringMode() && this.isHoveringMode() != mode) {
         if (mode) {
            this.beforeHoverThrottle = this.getCurrentThrottle();
         } else {
            this.setCurrentThrottle(this.beforeHoverThrottle);
         }

         this.isHoveringMode = mode;
         Entity riddenByEntity = this.getRiddenByEntity();
         if (riddenByEntity != null) {
            riddenByEntity.field_70125_A = 0.0F;
            riddenByEntity.field_70127_C = 0.0F;
         }
      }

   }

   public boolean canSwitchHoveringMode() {
      if (this.getAcInfo() == null) {
         return false;
      } else {
         return !this.isGunnerMode;
      }
   }

   public boolean isHovering() {
      return this.isGunnerMode || this.isHoveringMode();
   }

   public boolean getIsGunnerMode(Entity entity) {
      if (this.getAcInfo() == null) {
         return false;
      } else {
         int id = this.getSeatIdByEntity(entity);
         if (id < 0) {
            return false;
         } else if (id == 0 && this.getAcInfo().isEnableGunnerMode) {
            return this.isGunnerMode;
         } else {
            MCH_SeatInfo[] st = this.getSeatsInfo();
            if (id < st.length && st[id].gunner) {
               return this.world.isRemote && st[id].switchgunner ? this.isGunnerModeOtherSeat : true;
            } else {
               return false;
            }
         }
      }
   }

   public boolean isPilot(Entity player) {
      return W_Entity.isEqual(this.getRiddenByEntity(), player);
   }

   public boolean canSwitchFreeLook() {
      return true;
   }

   public boolean isFreeLookMode() {
      return this.getCommonStatus(1) || this.isRepelling();
   }

   public void switchFreeLookMode(boolean b) {
      this.setCommonStatus(1, b);
   }

   public void switchFreeLookModeClient(boolean b) {
      this.setCommonStatus(1, b, true);
   }

   public boolean canSwitchGunnerFreeLook(EntityPlayer player) {
      MCH_SeatInfo seatInfo = this.getSeatInfo(player);
      return seatInfo != null && seatInfo.fixRot && this.getIsGunnerMode(player);
   }

   public boolean isGunnerLookMode(EntityPlayer player) {
      return this.isPilot(player) ? false : this.isGunnerFreeLookMode;
   }

   public void switchGunnerFreeLookMode(boolean b) {
      this.isGunnerFreeLookMode = b;
   }

   public void switchGunnerFreeLookMode() {
      this.switchGunnerFreeLookMode(!this.isGunnerFreeLookMode);
   }

   public void updateParts(int stat) {
      if (!this.isDestroyed()) {
         MCH_Parts[] parts = new MCH_Parts[]{this.partHatch, this.partCanopy, this.partLandingGear};
         MCH_Parts[] var3 = parts;
         int var4 = parts.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MCH_Parts p = var3[var5];
            if (p != null) {
               p.updateStatusClient(stat);
               p.update();
            }
         }

         if (!this.isDestroyed() && !this.world.isRemote && this.partLandingGear != null) {
            int blockId = false;
            int blockId;
            if (!this.isLandingGearFolded() && this.partLandingGear.getFactor() <= 0.1F) {
               blockId = MCH_Lib.getBlockIdY(this, 3, -20);
               if ((this.getCurrentThrottle() <= 0.800000011920929D || this.field_70122_E || blockId != 0) && this.getAcInfo().isFloat && (this.func_70090_H() || MCH_Lib.getBlockY(this, 3, -20, true) == W_Block.getWater())) {
                  this.partLandingGear.setStatusServer(true);
               }
            } else if (this.isLandingGearFolded() && this.partLandingGear.getFactor() >= 0.9F) {
               blockId = MCH_Lib.getBlockIdY(this, 3, -10);
               if (this.getCurrentThrottle() < (double)this.getUnfoldLandingGearThrottle() && blockId != 0) {
                  boolean unfold = true;
                  if (this.getAcInfo().isFloat) {
                     blockId = MCH_Lib.getBlockIdY(this.world, this.posX, this.posY + 1.0D + (double)this.getAcInfo().floatOffset, this.posZ, 1, 65386, true);
                     if (W_Block.isEqual(blockId, W_Block.getWater())) {
                        unfold = false;
                     }
                  }

                  if (unfold) {
                     this.partLandingGear.setStatusServer(false);
                  }
               } else if (this.getVtolMode() == 2 && blockId != 0) {
                  this.partLandingGear.setStatusServer(false);
               }
            }
         }

      }
   }

   public float getUnfoldLandingGearThrottle() {
      return 0.8F;
   }

   private int getPartStatus() {
      return (Integer)this.dataManager.func_187225_a(PART_STAT);
   }

   private void setPartStatus(int n) {
      this.dataManager.func_187227_b(PART_STAT, n);
   }

   protected void initPartRotation(float yaw, float pitch) {
      this.lastRiderYaw = yaw;
      this.prevLastRiderYaw = yaw;
      this.camera.partRotationYaw = yaw;
      this.camera.prevPartRotationYaw = yaw;
      this.lastSearchLightYaw = yaw;
   }

   public int getLastPartStatusMask() {
      return 24;
   }

   public int getModeSwitchCooldown() {
      return this.modeSwitchCooldown;
   }

   public void setModeSwitchCooldown(int n) {
      this.modeSwitchCooldown = n;
   }

   protected MCH_EntityAircraft.WeaponBay[] createWeaponBays() {
      MCH_EntityAircraft.WeaponBay[] wbs = new MCH_EntityAircraft.WeaponBay[this.getAcInfo().partWeaponBay.size()];

      for(int i = 0; i < wbs.length; ++i) {
         wbs[i] = new MCH_EntityAircraft.WeaponBay(this);
      }

      return wbs;
   }

   protected MCH_Parts createHatch() {
      MCH_Parts hatch = null;
      if (this.getAcInfo().haveHatch()) {
         hatch = new MCH_Parts(this, 4, PART_STAT, "Hatch");
         hatch.rotationMax = 90.0F;
         hatch.rotationInv = 1.5F;
         hatch.soundEndSwichOn.setPrm("plane_cc", 1.0F, 1.0F);
         hatch.soundEndSwichOff.setPrm("plane_cc", 1.0F, 1.0F);
         hatch.soundSwitching.setPrm("plane_cv", 1.0F, 0.5F);
      }

      return hatch;
   }

   public boolean haveHatch() {
      return this.partHatch != null;
   }

   public boolean canFoldHatch() {
      return this.partHatch != null && this.modeSwitchCooldown <= 0 ? this.partHatch.isOFF() : false;
   }

   public boolean canUnfoldHatch() {
      return this.partHatch != null && this.modeSwitchCooldown <= 0 ? this.partHatch.isON() : false;
   }

   public void foldHatch(boolean fold) {
      this.foldHatch(fold, false);
   }

   public void foldHatch(boolean fold, boolean force) {
      if (this.partHatch != null) {
         if (force || this.modeSwitchCooldown <= 0) {
            this.partHatch.setStatusServer(fold);
            this.modeSwitchCooldown = 20;
            if (!fold) {
               this.stopUnmountCrew();
            }

         }
      }
   }

   public float getHatchRotation() {
      return this.partHatch != null ? this.partHatch.rotation : 0.0F;
   }

   public float getPrevHatchRotation() {
      return this.partHatch != null ? this.partHatch.prevRotation : 0.0F;
   }

   public void foldLandingGear() {
      if (this.partLandingGear != null && this.getModeSwitchCooldown() <= 0) {
         this.partLandingGear.setStatusServer(true);
         this.setModeSwitchCooldown(20);
      }
   }

   public void unfoldLandingGear() {
      if (this.partLandingGear != null && this.getModeSwitchCooldown() <= 0) {
         if (this.isLandingGearFolded()) {
            this.partLandingGear.setStatusServer(false);
            this.setModeSwitchCooldown(20);
         }

      }
   }

   public boolean canFoldLandingGear() {
      if (this.getLandingGearRotation() >= 1.0F) {
         return false;
      } else {
         Block block = MCH_Lib.getBlockY(this, 3, -10, true);
         return !this.isLandingGearFolded() && block == W_Blocks.field_150350_a;
      }
   }

   public boolean canUnfoldLandingGear() {
      return this.getLandingGearRotation() < 89.0F ? false : this.isLandingGearFolded();
   }

   public boolean isLandingGearFolded() {
      return this.partLandingGear != null ? this.partLandingGear.getStatus() : false;
   }

   protected MCH_Parts createLandingGear() {
      MCH_Parts lg = null;
      if (this.getAcInfo().haveLandingGear()) {
         lg = new MCH_Parts(this, 2, PART_STAT, "LandingGear");
         lg.rotationMax = 90.0F;
         lg.rotationInv = 2.5F;
         lg.soundStartSwichOn.setPrm("plane_cc", 1.0F, 0.5F);
         lg.soundEndSwichOn.setPrm("plane_cc", 1.0F, 0.5F);
         lg.soundStartSwichOff.setPrm("plane_cc", 1.0F, 0.5F);
         lg.soundEndSwichOff.setPrm("plane_cc", 1.0F, 0.5F);
         lg.soundSwitching.setPrm("plane_cv", 1.0F, 0.75F);
      }

      return lg;
   }

   public float getLandingGearRotation() {
      return this.partLandingGear != null ? this.partLandingGear.rotation : 0.0F;
   }

   public float getPrevLandingGearRotation() {
      return this.partLandingGear != null ? this.partLandingGear.prevRotation : 0.0F;
   }

   public int getVtolMode() {
      return 0;
   }

   public void openCanopy() {
      if (this.partCanopy != null && this.getModeSwitchCooldown() <= 0) {
         this.partCanopy.setStatusServer(true);
         this.setModeSwitchCooldown(20);
      }
   }

   public void openCanopy_EjectSeat() {
      if (this.partCanopy != null) {
         this.partCanopy.setStatusServer(true, false);
         this.setModeSwitchCooldown(40);
      }
   }

   public void closeCanopy() {
      if (this.partCanopy != null && this.getModeSwitchCooldown() <= 0) {
         if (this.getCanopyStat()) {
            this.partCanopy.setStatusServer(false);
            this.setModeSwitchCooldown(20);
         }

      }
   }

   public boolean getCanopyStat() {
      return this.partCanopy != null ? this.partCanopy.getStatus() : false;
   }

   public boolean isCanopyClose() {
      if (this.partCanopy == null) {
         return true;
      } else {
         return !this.getCanopyStat() && this.getCanopyRotation() <= 0.01F;
      }
   }

   public float getCanopyRotation() {
      return this.partCanopy != null ? this.partCanopy.rotation : 0.0F;
   }

   public float getPrevCanopyRotation() {
      return this.partCanopy != null ? this.partCanopy.prevRotation : 0.0F;
   }

   protected MCH_Parts createCanopy() {
      MCH_Parts canopy = null;
      if (this.getAcInfo().haveCanopy()) {
         canopy = new MCH_Parts(this, 0, PART_STAT, "Canopy");
         canopy.rotationMax = 90.0F;
         canopy.rotationInv = 3.5F;
         canopy.soundEndSwichOn.setPrm("plane_cc", 1.0F, 1.0F);
         canopy.soundEndSwichOff.setPrm("plane_cc", 1.0F, 1.0F);
      }

      return canopy;
   }

   public boolean hasBrake() {
      return false;
   }

   public void setBrake(boolean b) {
      if (!this.world.isRemote) {
         this.setCommonStatus(11, b);
      }

   }

   public boolean getBrake() {
      return this.getCommonStatus(11);
   }

   public void setGunnerStatus(boolean b) {
      if (!this.world.isRemote) {
         this.setCommonStatus(12, b);
      }

   }

   public boolean getGunnerStatus() {
      return this.getCommonStatus(12);
   }

   public int func_70302_i_() {
      return this.getAcInfo() != null ? this.getAcInfo().inventorySize : 0;
   }

   public String getInvName() {
      if (this.getAcInfo() == null) {
         return super.getInvName();
      } else {
         String s = this.getAcInfo().displayName;
         return s.length() <= 32 ? s : s.substring(0, 31);
      }
   }

   public boolean isInvNameLocalized() {
      return this.getAcInfo() != null;
   }

   @Nullable
   public MCH_EntityChain getTowChainEntity() {
      return this.towChainEntity;
   }

   public void setTowChainEntity(MCH_EntityChain chainEntity) {
      this.towChainEntity = chainEntity;
   }

   @Nullable
   public MCH_EntityChain getTowedChainEntity() {
      return this.towedChainEntity;
   }

   public void setTowedChainEntity(MCH_EntityChain towedChainEntity) {
      this.towedChainEntity = towedChainEntity;
   }

   public void func_174826_a(AxisAlignedBB bb) {
      super.func_174826_a(new MCH_AircraftBoundingBox(this, bb));
   }

   public double getX() {
      return this.posX;
   }

   public double getY() {
      return this.posY;
   }

   public double getZ() {
      return this.posZ;
   }

   public Entity getEntity() {
      return this;
   }

   public ItemStack getPickedResult(RayTraceResult target) {
      return new ItemStack(this.getItem());
   }

   static {
      DAMAGE = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      ID_TYPE = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.STRING);
      TEXTURE_NAME = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.STRING);
      UAV_STATION = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      STATUS = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      USE_WEAPON = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      FUEL = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      ROT_ROLL = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      COMMAND = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.STRING);
      THROTTLE = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      PART_STAT = EntityDataManager.createKey(MCH_EntityAircraft.class, DataSerializers.VARINT);
      seatsDummy = new MCH_EntitySeat[0];
   }

   public class WeaponBay {
      public float rot = 0.0F;
      public float prevRot = 0.0F;

      public WeaponBay(MCH_EntityAircraft paramMCH_EntityAircraft) {
      }
   }

   public class UnmountReserve {
      final Entity entity;
      final double posX;
      final double posY;
      final double posZ;
      int cnt = 5;

      public UnmountReserve(MCH_EntityAircraft paramMCH_EntityAircraft, Entity e, double x, double y, double z) {
         this.entity = e;
         this.posX = x;
         this.posY = y;
         this.posZ = z;
      }
   }
}
