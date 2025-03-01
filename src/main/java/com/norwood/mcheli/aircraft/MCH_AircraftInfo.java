package com.norwood.mcheli.aircraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.__helper.info.IItemContent;
import com.norwood.mcheli.hud.MCH_Hud;
import com.norwood.mcheli.hud.MCH_HudManager;
import com.norwood.mcheli.weapon.MCH_WeaponInfoManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class MCH_AircraftInfo extends MCH_BaseInfo implements IItemContent {
   public final String name;
   public String displayName;
   public HashMap<String, String> displayNameLang;
   public int itemID;
   public List<String> recipeString;
   public List<IRecipe> recipe;
   public boolean isShapedRecipe;
   public String category;
   public boolean creativeOnly;
   public boolean invulnerable;
   public boolean isEnableGunnerMode;
   public int cameraZoom;
   public boolean isEnableConcurrentGunnerMode;
   public boolean isEnableNightVision;
   public boolean isEnableEntityRadar;
   public boolean isEnableEjectionSeat;
   public boolean isEnableParachuting;
   public MCH_AircraftInfo.Flare flare;
   public float bodyHeight;
   public float bodyWidth;
   public boolean isFloat;
   public float floatOffset;
   public float gravity;
   public float gravityInWater;
   public int maxHp;
   public float armorMinDamage;
   public float armorMaxDamage;
   public float armorDamageFactor;
   public boolean enableBack;
   public int inventorySize;
   public boolean isUAV;
   public boolean isSmallUAV;
   public boolean isTargetDrone;
   public float autoPilotRot;
   public float onGroundPitch;
   public boolean canMoveOnGround;
   public boolean canRotOnGround;
   public List<MCH_AircraftInfo.WeaponSet> weaponSetList;
   public List<MCH_SeatInfo> seatList;
   public List<Integer[]> exclusionSeatList;
   public List<MCH_Hud> hudList;
   public MCH_Hud hudTvMissile;
   public float damageFactor;
   public float submergedDamageHeight;
   public boolean regeneration;
   public List<MCH_BoundingBox> extraBoundingBox;
   public List<MCH_AircraftInfo.Wheel> wheels;
   public int maxFuel;
   public float fuelConsumption;
   public float fuelSupplyRange;
   public float ammoSupplyRange;
   public float repairOtherVehiclesRange;
   public int repairOtherVehiclesValue;
   public float stealth;
   public boolean canRide;
   public float entityWidth;
   public float entityHeight;
   public float entityPitch;
   public float entityRoll;
   public float stepHeight;
   public List<MCH_SeatRackInfo> entityRackList;
   public int mobSeatNum;
   public int entityRackNum;
   public MCH_MobDropOption mobDropOption;
   public List<MCH_AircraftInfo.RepellingHook> repellingHooks;
   public List<MCH_AircraftInfo.RideRack> rideRacks;
   public List<MCH_AircraftInfo.ParticleSplash> particleSplashs;
   public List<MCH_AircraftInfo.SearchLight> searchLights;
   public float rotorSpeed;
   public boolean enableSeaSurfaceParticle;
   public float pivotTurnThrottle;
   public float trackRollerRot;
   public float partWheelRot;
   public float onGroundPitchFactor;
   public float onGroundRollFactor;
   public Vec3d turretPosition;
   public boolean defaultFreelook;
   public Vec3d unmountPosition;
   public float thirdPersonDist;
   public float markerWidth;
   public float markerHeight;
   public float bbZmin;
   public float bbZmax;
   public float bbZ;
   public boolean alwaysCameraView;
   public List<MCH_AircraftInfo.CameraPosition> cameraPosition;
   public float cameraRotationSpeed;
   public float speed;
   public float motionFactor;
   public float mobilityYaw;
   public float mobilityPitch;
   public float mobilityRoll;
   public float mobilityYawOnGround;
   public float minRotationPitch;
   public float maxRotationPitch;
   public float minRotationRoll;
   public float maxRotationRoll;
   public boolean limitRotation;
   public float throttleUpDown;
   public float throttleUpDownOnEntity;
   private List<String> textureNameList;
   public int textureCount;
   public float particlesScale;
   public boolean hideEntity;
   public boolean smoothShading;
   public String soundMove;
   public float soundRange;
   public float soundVolume;
   public float soundPitch;
   public _IModelCustom model;
   public List<MCH_AircraftInfo.Hatch> hatchList;
   public List<MCH_AircraftInfo.Camera> cameraList;
   public List<MCH_AircraftInfo.PartWeapon> partWeapon;
   public List<MCH_AircraftInfo.WeaponBay> partWeaponBay;
   public List<MCH_AircraftInfo.Canopy> canopyList;
   public List<MCH_AircraftInfo.LandingGear> landingGear;
   public List<MCH_AircraftInfo.Throttle> partThrottle;
   public List<MCH_AircraftInfo.RotPart> partRotPart;
   public List<MCH_AircraftInfo.CrawlerTrack> partCrawlerTrack;
   public List<MCH_AircraftInfo.TrackRoller> partTrackRoller;
   public List<MCH_AircraftInfo.PartWheel> partWheel;
   public List<MCH_AircraftInfo.PartWheel> partSteeringWheel;
   public List<MCH_AircraftInfo.Hatch> lightHatchList;
   private String lastWeaponType = "";
   private int lastWeaponIndex = -1;
   private MCH_AircraftInfo.PartWeapon lastWeaponPart;

   public ItemStack getItemStack() {
      return new ItemStack(this.getItem());
   }

   public abstract String getDirectoryName();

   public abstract String getKindName();

   public MCH_AircraftInfo(AddonResourceLocation location, String path) {
      super(location, path);
      this.name = location.func_110623_a();
      this.displayName = this.name;
      this.displayNameLang = new HashMap();
      this.itemID = 0;
      this.recipeString = new ArrayList();
      this.recipe = new ArrayList();
      this.isShapedRecipe = true;
      this.category = "zzz";
      this.creativeOnly = false;
      this.invulnerable = false;
      this.isEnableGunnerMode = false;
      this.isEnableConcurrentGunnerMode = false;
      this.isEnableNightVision = false;
      this.isEnableEntityRadar = false;
      this.isEnableEjectionSeat = false;
      this.isEnableParachuting = false;
      this.flare = new MCH_AircraftInfo.Flare(this);
      this.weaponSetList = new ArrayList();
      this.seatList = new ArrayList();
      this.exclusionSeatList = new ArrayList();
      this.hudList = new ArrayList();
      this.hudTvMissile = null;
      this.bodyHeight = 0.7F;
      this.bodyWidth = 2.0F;
      this.isFloat = false;
      this.floatOffset = 0.0F;
      this.gravity = -0.04F;
      this.gravityInWater = -0.04F;
      this.maxHp = 50;
      this.damageFactor = 0.2F;
      this.submergedDamageHeight = 0.0F;
      this.inventorySize = 0;
      this.armorDamageFactor = 1.0F;
      this.armorMaxDamage = 100000.0F;
      this.armorMinDamage = 0.0F;
      this.enableBack = false;
      this.isUAV = false;
      this.isSmallUAV = false;
      this.isTargetDrone = false;
      this.autoPilotRot = -0.6F;
      this.regeneration = false;
      this.onGroundPitch = 0.0F;
      this.canMoveOnGround = true;
      this.canRotOnGround = true;
      this.cameraZoom = this.getDefaultMaxZoom();
      this.extraBoundingBox = new ArrayList();
      this.maxFuel = 0;
      this.fuelConsumption = 1.0F;
      this.fuelSupplyRange = 0.0F;
      this.ammoSupplyRange = 0.0F;
      this.repairOtherVehiclesRange = 0.0F;
      this.repairOtherVehiclesValue = 10;
      this.stealth = 0.0F;
      this.canRide = true;
      this.entityWidth = 1.0F;
      this.entityHeight = 1.0F;
      this.entityPitch = 0.0F;
      this.entityRoll = 0.0F;
      this.stepHeight = this.getDefaultStepHeight();
      this.entityRackList = new ArrayList();
      this.mobSeatNum = 0;
      this.entityRackNum = 0;
      this.mobDropOption = new MCH_MobDropOption();
      this.repellingHooks = new ArrayList();
      this.rideRacks = new ArrayList();
      this.particleSplashs = new ArrayList();
      this.searchLights = new ArrayList();
      this.markerHeight = 1.0F;
      this.markerWidth = 2.0F;
      this.bbZmax = 1.0F;
      this.bbZmin = -1.0F;
      this.rotorSpeed = this.getDefaultRotorSpeed();
      this.wheels = this.getDefaultWheelList();
      this.onGroundPitchFactor = 0.0F;
      this.onGroundRollFactor = 0.0F;
      this.turretPosition = new Vec3d(0.0D, 0.0D, 0.0D);
      this.defaultFreelook = false;
      this.unmountPosition = null;
      this.thirdPersonDist = 4.0F;
      this.cameraPosition = new ArrayList();
      this.alwaysCameraView = false;
      this.cameraRotationSpeed = 100.0F;
      this.speed = 0.1F;
      this.motionFactor = 0.96F;
      this.mobilityYaw = 1.0F;
      this.mobilityPitch = 1.0F;
      this.mobilityRoll = 1.0F;
      this.mobilityYawOnGround = 1.0F;
      this.minRotationPitch = this.getMinRotationPitch();
      this.maxRotationPitch = this.getMaxRotationPitch();
      this.minRotationRoll = this.getMinRotationPitch();
      this.maxRotationRoll = this.getMaxRotationPitch();
      this.limitRotation = false;
      this.throttleUpDown = 1.0F;
      this.throttleUpDownOnEntity = 2.0F;
      this.pivotTurnThrottle = 0.0F;
      this.trackRollerRot = 30.0F;
      this.partWheelRot = 30.0F;
      this.textureNameList = new ArrayList();
      this.textureNameList.add(this.name);
      this.textureCount = 0;
      this.particlesScale = 1.0F;
      this.enableSeaSurfaceParticle = false;
      this.hideEntity = false;
      this.smoothShading = true;
      this.soundMove = "";
      this.soundPitch = 1.0F;
      this.soundVolume = 1.0F;
      this.soundRange = this.getDefaultSoundRange();
      this.model = null;
      this.hatchList = new ArrayList();
      this.cameraList = new ArrayList();
      this.partWeapon = new ArrayList();
      this.lastWeaponPart = null;
      this.partWeaponBay = new ArrayList();
      this.canopyList = new ArrayList();
      this.landingGear = new ArrayList();
      this.partThrottle = new ArrayList();
      this.partRotPart = new ArrayList();
      this.partCrawlerTrack = new ArrayList();
      this.partTrackRoller = new ArrayList();
      this.partWheel = new ArrayList();
      this.partSteeringWheel = new ArrayList();
      this.lightHatchList = new ArrayList();
   }

   public float getDefaultSoundRange() {
      return 100.0F;
   }

   public List<MCH_AircraftInfo.Wheel> getDefaultWheelList() {
      return new ArrayList();
   }

   public float getDefaultRotorSpeed() {
      return 0.0F;
   }

   private float getDefaultStepHeight() {
      return 0.0F;
   }

   public boolean haveRepellingHook() {
      return this.repellingHooks.size() > 0;
   }

   public boolean haveFlare() {
      return this.flare.types.length > 0;
   }

   public boolean haveCanopy() {
      return this.canopyList.size() > 0;
   }

   public boolean haveLandingGear() {
      return this.landingGear.size() > 0;
   }

   public abstract String getDefaultHudName(int var1);

   public boolean validate() throws Exception {
      if (this.cameraPosition.size() <= 0) {
         this.cameraPosition.add(new MCH_AircraftInfo.CameraPosition(this));
      }

      this.bbZ = (this.bbZmax + this.bbZmin) / 2.0F;
      if (this.isTargetDrone) {
         this.isUAV = true;
      }

      if (this.isEnableParachuting && this.repellingHooks.size() > 0) {
         this.isEnableParachuting = false;
         this.repellingHooks.clear();
      }

      if (this.isUAV) {
         this.alwaysCameraView = true;
         if (this.seatList.size() == 0) {
            MCH_SeatInfo s = new MCH_SeatInfo(new Vec3d(0.0D, 0.0D, 0.0D), false);
            this.seatList.add(s);
         }
      }

      this.mobSeatNum = this.seatList.size();
      this.entityRackNum = this.entityRackList.size();
      if (this.getNumSeat() < 1) {
         throw new Exception("At least one seat must be set.");
      } else {
         int i;
         if (this.getNumHud() < this.getNumSeat()) {
            for(i = this.getNumHud(); i < this.getNumSeat(); ++i) {
               this.hudList.add(MCH_HudManager.get(this.getDefaultHudName(i)));
            }
         }

         if (this.getNumSeat() == 1 && this.getNumHud() == 1) {
            this.hudList.add(MCH_HudManager.get(this.getDefaultHudName(1)));
         }

         Iterator var11 = this.entityRackList.iterator();

         while(var11.hasNext()) {
            MCH_SeatRackInfo ei = (MCH_SeatRackInfo)var11.next();
            this.seatList.add(ei);
         }

         this.entityRackList.clear();
         if (this.hudTvMissile == null) {
            this.hudTvMissile = MCH_HudManager.get("tv_missile");
         }

         if (this.textureNameList.size() < 1) {
            throw new Exception("At least one texture must be set.");
         } else {
            if (this.itemID <= 0) {
            }

            for(i = 0; i < this.partWeaponBay.size(); ++i) {
               MCH_AircraftInfo.WeaponBay wb = (MCH_AircraftInfo.WeaponBay)this.partWeaponBay.get(i);
               String[] weaponNames = wb.weaponName.split("\\s*/\\s*");
               if (weaponNames.length <= 0) {
                  this.partWeaponBay.remove(i);
               } else {
                  List<Integer> list = new ArrayList();
                  String[] var5 = weaponNames;
                  int var6 = weaponNames.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     String s = var5[var7];
                     int id = this.getWeaponIdByName(s);
                     if (id >= 0) {
                        list.add(id);
                     }
                  }

                  if (list.size() <= 0) {
                     this.partWeaponBay.remove(i);
                  } else {
                     ((MCH_AircraftInfo.WeaponBay)this.partWeaponBay.get(i)).weaponIds = (Integer[])list.toArray(new Integer[0]);
                  }
               }
            }

            return true;
         }
      }
   }

   public int getInfo_MaxSeatNum() {
      return 30;
   }

   public int getNumSeatAndRack() {
      return this.seatList.size();
   }

   public int getNumSeat() {
      return this.mobSeatNum;
   }

   public int getNumRack() {
      return this.entityRackNum;
   }

   public int getNumHud() {
      return this.hudList.size();
   }

   public float getMaxSpeed() {
      return 0.8F;
   }

   public float getMinRotationPitch() {
      return -89.9F;
   }

   public float getMaxRotationPitch() {
      return 80.0F;
   }

   public float getMinRotationRoll() {
      return -80.0F;
   }

   public float getMaxRotationRoll() {
      return 80.0F;
   }

   public int getDefaultMaxZoom() {
      return 1;
   }

   public boolean haveHatch() {
      return this.hatchList.size() > 0;
   }

   public boolean havePartCamera() {
      return this.cameraList.size() > 0;
   }

   public boolean havePartThrottle() {
      return this.partThrottle.size() > 0;
   }

   public MCH_AircraftInfo.WeaponSet getWeaponSetById(int id) {
      return id >= 0 && id < this.weaponSetList.size() ? (MCH_AircraftInfo.WeaponSet)this.weaponSetList.get(id) : null;
   }

   public MCH_AircraftInfo.Weapon getWeaponById(int id) {
      MCH_AircraftInfo.WeaponSet ws = this.getWeaponSetById(id);
      return ws != null ? (MCH_AircraftInfo.Weapon)ws.weapons.get(0) : null;
   }

   public int getWeaponIdByName(String s) {
      for(int i = 0; i < this.weaponSetList.size(); ++i) {
         if (((MCH_AircraftInfo.WeaponSet)this.weaponSetList.get(i)).type.equalsIgnoreCase(s)) {
            return i;
         }
      }

      return -1;
   }

   public MCH_AircraftInfo.Weapon getWeaponByName(String s) {
      for(int i = 0; i < this.weaponSetList.size(); ++i) {
         if (((MCH_AircraftInfo.WeaponSet)this.weaponSetList.get(i)).type.equalsIgnoreCase(s)) {
            return this.getWeaponById(i);
         }
      }

      return null;
   }

   public int getWeaponNum() {
      return this.weaponSetList.size();
   }

   public void loadItemData(String item, String data) {
      if (item.compareTo("displayname") == 0) {
         this.displayName = data.trim();
      } else {
         String[] s;
         if (item.compareTo("adddisplayname") == 0) {
            s = data.split("\\s*,\\s*");
            if (s != null && s.length == 2) {
               this.displayNameLang.put(s[0].toLowerCase().trim(), s[1].trim());
            }
         } else if (item.equalsIgnoreCase("Category")) {
            this.category = data.toUpperCase().replaceAll("[,;:]", ".").replaceAll("[ \t]", "");
         } else if (item.equalsIgnoreCase("CanRide")) {
            this.canRide = this.toBool(data, true);
         } else if (item.equalsIgnoreCase("CreativeOnly")) {
            this.creativeOnly = this.toBool(data, false);
         } else if (item.equalsIgnoreCase("Invulnerable")) {
            this.invulnerable = this.toBool(data, false);
         } else if (item.equalsIgnoreCase("MaxFuel")) {
            this.maxFuel = this.toInt(data, 0, 100000000);
         } else if (item.equalsIgnoreCase("FuelConsumption")) {
            this.fuelConsumption = this.toFloat(data, 0.0F, 10000.0F);
         } else if (item.equalsIgnoreCase("FuelSupplyRange")) {
            this.fuelSupplyRange = this.toFloat(data, 0.0F, 1000.0F);
         } else if (item.equalsIgnoreCase("AmmoSupplyRange")) {
            this.ammoSupplyRange = this.toFloat(data, 0.0F, 1000.0F);
         } else if (item.equalsIgnoreCase("RepairOtherVehicles")) {
            s = this.splitParam(data);
            if (s.length >= 1) {
               this.repairOtherVehiclesRange = this.toFloat(s[0], 0.0F, 1000.0F);
               if (s.length >= 2) {
                  this.repairOtherVehiclesValue = this.toInt(s[1], 0, 10000000);
               }
            }
         } else if (item.compareTo("itemid") == 0) {
            this.itemID = this.toInt(data, 0, 65535);
         } else if (item.compareTo("addtexture") == 0) {
            this.textureNameList.add(data.toLowerCase());
         } else if (item.compareTo("particlesscale") == 0) {
            this.particlesScale = this.toFloat(data, 0.0F, 50.0F);
         } else if (item.equalsIgnoreCase("EnableSeaSurfaceParticle")) {
            this.enableSeaSurfaceParticle = this.toBool(data);
         } else {
            Vec3d p;
            int canopyNum;
            float ry;
            float rz;
            int seatID;
            float py;
            float pz;
            if (item.equalsIgnoreCase("AddParticleSplash")) {
               s = this.splitParam(data);
               if (s.length >= 3) {
                  p = this.toVec3(s[0], s[1], s[2]);
                  canopyNum = s.length >= 4 ? this.toInt(s[3], 1, 100) : 2;
                  ry = s.length >= 5 ? this.toFloat(s[4]) : 2.0F;
                  rz = s.length >= 6 ? this.toFloat(s[5]) : 1.0F;
                  seatID = s.length >= 7 ? this.toInt(s[6], 1, 100000) : 80;
                  py = s.length >= 8 ? this.toFloat(s[7]) : 0.01F;
                  pz = s.length >= 9 ? this.toFloat(s[8]) : 0.0F;
                  this.particleSplashs.add(new MCH_AircraftInfo.ParticleSplash(this, p, canopyNum, ry, rz, seatID, py, pz));
               }
            } else {
               float mxy;
               int i;
               float px;
               if (!item.equalsIgnoreCase("AddSearchLight") && !item.equalsIgnoreCase("AddFixedSearchLight") && !item.equalsIgnoreCase("AddSteeringSearchLight")) {
                  float df;
                  if (item.equalsIgnoreCase("AddPartLightHatch")) {
                     s = this.splitParam(data);
                     if (s.length >= 6) {
                        df = s.length >= 7 ? this.toFloat(s[6], -1800.0F, 1800.0F) : 90.0F;
                        this.lightHatchList.add(new MCH_AircraftInfo.Hatch(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), df, "light_hatch" + this.lightHatchList.size(), false));
                     }
                  } else {
                     int i;
                     if (item.equalsIgnoreCase("AddRepellingHook")) {
                        s = this.splitParam(data);
                        if (s != null && s.length >= 3) {
                           i = s.length >= 4 ? this.toInt(s[3], 1, 100000) : 10;
                           this.repellingHooks.add(new MCH_AircraftInfo.RepellingHook(this, this.toVec3(s[0], s[1], s[2]), i));
                        }
                     } else {
                        String[] s;
                        float rx;
                        boolean isMissile;
                        if (item.equalsIgnoreCase("AddRack")) {
                           s = data.toLowerCase().split("\\s*,\\s*");
                           if (s != null && s.length >= 7) {
                              s = s[0].split("\\s*/\\s*");
                              rx = s.length >= 8 ? this.toFloat(s[7]) : 6.0F;
                              ry = s.length >= 9 ? this.toFloat(s[8], 0.0F, 1000000.0F) : 20.0F;
                              rz = s.length >= 10 ? this.toFloat(s[9]) : 0.0F;
                              px = s.length >= 11 ? this.toFloat(s[10]) : 0.0F;
                              isMissile = s.length >= 12 ? this.toBool(s[11]) : false;
                              this.entityRackList.add(new MCH_SeatRackInfo(s, this.toDouble(s[1]), this.toDouble(s[2]), this.toDouble(s[3]), new MCH_AircraftInfo.CameraPosition(this, this.toVec3(s[4], s[5], s[6]).func_72441_c(0.0D, 1.5D, 0.0D)), rx, ry, rz, px, isMissile));
                           }
                        } else if (item.equalsIgnoreCase("RideRack")) {
                           s = this.splitParam(data);
                           if (s.length >= 2) {
                              MCH_AircraftInfo.RideRack r = new MCH_AircraftInfo.RideRack(this, s[0].trim().toLowerCase(), this.toInt(s[1], 1, 10000));
                              this.rideRacks.add(r);
                           }
                        } else {
                           boolean ps;
                           MCH_SeatInfo n;
                           boolean canUsePilot;
                           boolean isRot;
                           boolean turret;
                           if (!item.equalsIgnoreCase("AddSeat") && !item.equalsIgnoreCase("AddGunnerSeat") && !item.equalsIgnoreCase("AddFixRotSeat")) {
                              if (item.equalsIgnoreCase("SetWheelPos")) {
                                 s = this.splitParam(data);
                                 if (s.length >= 4) {
                                    df = Math.abs(this.toFloat(s[0]));
                                    rx = this.toFloat(s[1]);
                                    this.wheels.clear();

                                    for(i = 2; i < s.length; ++i) {
                                       this.wheels.add(new MCH_AircraftInfo.Wheel(this, new Vec3d((double)df, (double)rx, (double)this.toFloat(s[i]))));
                                    }

                                    Collections.sort(this.wheels, new Comparator<MCH_AircraftInfo.Wheel>() {
                                       public int compare(MCH_AircraftInfo.Wheel arg0, MCH_AircraftInfo.Wheel arg1) {
                                          return arg0.pos.field_72449_c > arg1.pos.field_72449_c ? -1 : 1;
                                       }
                                    });
                                 }
                              } else if (item.equalsIgnoreCase("ExclusionSeat")) {
                                 s = this.splitParam(data);
                                 if (s.length >= 2) {
                                    Integer[] a = new Integer[s.length];

                                    for(canopyNum = 0; canopyNum < a.length; ++canopyNum) {
                                       a[canopyNum] = this.toInt(s[canopyNum], 1, 10000) - 1;
                                    }

                                    this.exclusionSeatList.add(a);
                                 }
                              } else if (MCH_MOD.proxy.isRemote() && item.equalsIgnoreCase("HUD")) {
                                 this.hudList.clear();
                                 s = data.split("\\s*,\\s*");
                                 s = s;
                                 canopyNum = s.length;

                                 for(i = 0; i < canopyNum; ++i) {
                                    String s = s[i];
                                    MCH_Hud hud = MCH_HudManager.get(s);
                                    if (hud == null) {
                                       hud = MCH_Hud.NoDisp;
                                    }

                                    this.hudList.add(hud);
                                 }
                              } else if (item.compareTo("enablenightvision") == 0) {
                                 this.isEnableNightVision = this.toBool(data);
                              } else if (item.compareTo("enableentityradar") == 0) {
                                 this.isEnableEntityRadar = this.toBool(data);
                              } else if (item.equalsIgnoreCase("EnableEjectionSeat")) {
                                 this.isEnableEjectionSeat = this.toBool(data);
                              } else if (item.equalsIgnoreCase("EnableParachuting")) {
                                 this.isEnableParachuting = this.toBool(data);
                              } else if (item.equalsIgnoreCase("MobDropOption")) {
                                 s = this.splitParam(data);
                                 if (s.length >= 3) {
                                    this.mobDropOption.pos = this.toVec3(s[0], s[1], s[2]);
                                    this.mobDropOption.interval = s.length >= 4 ? this.toInt(s[3]) : 12;
                                 }
                              } else if (item.equalsIgnoreCase("Width")) {
                                 this.bodyWidth = this.toFloat(data, 0.1F, 1000.0F);
                              } else if (item.equalsIgnoreCase("Height")) {
                                 this.bodyHeight = this.toFloat(data, 0.1F, 1000.0F);
                              } else if (item.compareTo("float") == 0) {
                                 this.isFloat = this.toBool(data);
                              } else if (item.compareTo("floatoffset") == 0) {
                                 this.floatOffset = -this.toFloat(data);
                              } else if (item.compareTo("gravity") == 0) {
                                 this.gravity = this.toFloat(data, -50.0F, 50.0F);
                              } else if (item.compareTo("gravityinwater") == 0) {
                                 this.gravityInWater = this.toFloat(data, -50.0F, 50.0F);
                              } else {
                                 boolean ys;
                                 if (item.compareTo("cameraposition") == 0) {
                                    s = data.split("\\s*,\\s*");
                                    if (s.length >= 3) {
                                       this.alwaysCameraView = s.length >= 4 ? this.toBool(s[3]) : false;
                                       ys = s.length >= 5;
                                       rx = s.length >= 5 ? this.toFloat(s[4]) : 0.0F;
                                       ry = s.length >= 6 ? this.toFloat(s[5]) : 0.0F;
                                       this.cameraPosition.add(new MCH_AircraftInfo.CameraPosition(this, this.toVec3(s[0], s[1], s[2]), ys, rx, ry));
                                    }
                                 } else if (item.equalsIgnoreCase("UnmountPosition")) {
                                    s = data.split("\\s*,\\s*");
                                    if (s.length >= 3) {
                                       this.unmountPosition = this.toVec3(s[0], s[1], s[2]);
                                    }
                                 } else if (item.equalsIgnoreCase("ThirdPersonDist")) {
                                    this.thirdPersonDist = this.toFloat(data, 4.0F, 100.0F);
                                 } else if (item.equalsIgnoreCase("TurretPosition")) {
                                    s = data.split("\\s*,\\s*");
                                    if (s.length >= 3) {
                                       this.turretPosition = this.toVec3(s[0], s[1], s[2]);
                                    }
                                 } else if (item.equalsIgnoreCase("CameraRotationSpeed")) {
                                    this.cameraRotationSpeed = this.toFloat(data, 0.0F, 10000.0F);
                                 } else if (item.compareTo("regeneration") == 0) {
                                    this.regeneration = this.toBool(data);
                                 } else if (item.compareTo("speed") == 0) {
                                    this.speed = this.toFloat(data, 0.0F, this.getMaxSpeed());
                                 } else if (item.equalsIgnoreCase("EnableBack")) {
                                    this.enableBack = this.toBool(data);
                                 } else if (item.equalsIgnoreCase("MotionFactor")) {
                                    this.motionFactor = this.toFloat(data, 0.0F, 1.0F);
                                 } else if (item.equalsIgnoreCase("MobilityYawOnGround")) {
                                    this.mobilityYawOnGround = this.toFloat(data, 0.0F, 100.0F);
                                 } else if (item.equalsIgnoreCase("MobilityYaw")) {
                                    this.mobilityYaw = this.toFloat(data, 0.0F, 100.0F);
                                 } else if (item.equalsIgnoreCase("MobilityPitch")) {
                                    this.mobilityPitch = this.toFloat(data, 0.0F, 100.0F);
                                 } else if (item.equalsIgnoreCase("MobilityRoll")) {
                                    this.mobilityRoll = this.toFloat(data, 0.0F, 100.0F);
                                 } else if (item.equalsIgnoreCase("MinRotationPitch")) {
                                    this.limitRotation = true;
                                    this.minRotationPitch = this.toFloat(data, this.getMinRotationPitch(), 0.0F);
                                 } else if (item.equalsIgnoreCase("MaxRotationPitch")) {
                                    this.limitRotation = true;
                                    this.maxRotationPitch = this.toFloat(data, 0.0F, this.getMaxRotationPitch());
                                 } else if (item.equalsIgnoreCase("MinRotationRoll")) {
                                    this.limitRotation = true;
                                    this.minRotationRoll = this.toFloat(data, this.getMinRotationRoll(), 0.0F);
                                 } else if (item.equalsIgnoreCase("MaxRotationRoll")) {
                                    this.limitRotation = true;
                                    this.maxRotationRoll = this.toFloat(data, 0.0F, this.getMaxRotationRoll());
                                 } else if (item.compareTo("throttleupdown") == 0) {
                                    this.throttleUpDown = this.toFloat(data, 0.0F, 3.0F);
                                 } else if (item.equalsIgnoreCase("ThrottleUpDownOnEntity")) {
                                    this.throttleUpDownOnEntity = this.toFloat(data, 0.0F, 100000.0F);
                                 } else if (item.equalsIgnoreCase("Stealth")) {
                                    this.stealth = this.toFloat(data, 0.0F, 1.0F);
                                 } else if (item.equalsIgnoreCase("EntityWidth")) {
                                    this.entityWidth = this.toFloat(data, -100.0F, 100.0F);
                                 } else if (item.equalsIgnoreCase("EntityHeight")) {
                                    this.entityHeight = this.toFloat(data, -100.0F, 100.0F);
                                 } else if (item.equalsIgnoreCase("EntityPitch")) {
                                    this.entityPitch = this.toFloat(data, -360.0F, 360.0F);
                                 } else if (item.equalsIgnoreCase("EntityRoll")) {
                                    this.entityRoll = this.toFloat(data, -360.0F, 360.0F);
                                 } else if (item.equalsIgnoreCase("StepHeight")) {
                                    this.stepHeight = this.toFloat(data, 0.0F, 1000.0F);
                                 } else if (item.equalsIgnoreCase("CanMoveOnGround")) {
                                    this.canMoveOnGround = this.toBool(data);
                                 } else if (item.equalsIgnoreCase("CanRotOnGround")) {
                                    this.canRotOnGround = this.toBool(data);
                                 } else if (!item.equalsIgnoreCase("AddWeapon") && !item.equalsIgnoreCase("AddTurretWeapon")) {
                                    if (!item.equalsIgnoreCase("AddPartWeapon") && !item.equalsIgnoreCase("AddPartRotWeapon") && !item.equalsIgnoreCase("AddPartTurretWeapon") && !item.equalsIgnoreCase("AddPartTurretRotWeapon") && !item.equalsIgnoreCase("AddPartWeaponMissile")) {
                                       if (item.equalsIgnoreCase("AddPartWeaponChild")) {
                                          s = data.split("\\s*,\\s*");
                                          if (s.length >= 5 && this.lastWeaponPart != null) {
                                             df = s.length >= 6 ? this.toFloat(s[5]) : 0.0F;
                                             MCH_AircraftInfo.PartWeaponChild w = new MCH_AircraftInfo.PartWeaponChild(this, this.lastWeaponPart.name, this.toBool(s[0]), this.toBool(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.lastWeaponPart.modelName + "_" + this.lastWeaponPart.child.size(), 0.0F, 0.0F, 0.0F, df);
                                             this.lastWeaponPart.child.add(w);
                                          }
                                       } else if (item.compareTo("addrecipe") != 0 && item.compareTo("addshapelessrecipe") != 0) {
                                          if (item.compareTo("maxhp") == 0) {
                                             this.maxHp = this.toInt(data, 1, 1000000000);
                                          } else if (item.compareTo("inventorysize") == 0) {
                                             this.inventorySize = this.toInt(data, 0, 54);
                                          } else if (item.compareTo("damagefactor") == 0) {
                                             this.damageFactor = this.toFloat(data, 0.0F, 1.0F);
                                          } else if (item.equalsIgnoreCase("SubmergedDamageHeight")) {
                                             this.submergedDamageHeight = this.toFloat(data, -1000.0F, 1000.0F);
                                          } else if (item.equalsIgnoreCase("ArmorDamageFactor")) {
                                             this.armorDamageFactor = this.toFloat(data, 0.0F, 10000.0F);
                                          } else if (item.equalsIgnoreCase("ArmorMinDamage")) {
                                             this.armorMinDamage = this.toFloat(data, 0.0F, 1000000.0F);
                                          } else if (item.equalsIgnoreCase("ArmorMaxDamage")) {
                                             this.armorMaxDamage = this.toFloat(data, 0.0F, 1000000.0F);
                                          } else if (item.equalsIgnoreCase("FlareType")) {
                                             s = data.split("\\s*,\\s*");
                                             this.flare.types = new int[s.length];

                                             for(i = 0; i < s.length; ++i) {
                                                this.flare.types[i] = this.toInt(s[i], 1, 10);
                                             }
                                          } else if (item.equalsIgnoreCase("FlareOption")) {
                                             s = this.splitParam(data);
                                             if (s.length >= 3) {
                                                this.flare.pos = this.toVec3(s[0], s[1], s[2]);
                                             }
                                          } else if (item.equalsIgnoreCase("Sound")) {
                                             this.soundMove = data.toLowerCase();
                                          } else if (item.equalsIgnoreCase("SoundRange")) {
                                             this.soundRange = this.toFloat(data, 1.0F, 1000.0F);
                                          } else if (item.equalsIgnoreCase("SoundVolume")) {
                                             this.soundVolume = this.toFloat(data, 0.0F, 10.0F);
                                          } else if (item.equalsIgnoreCase("SoundPitch")) {
                                             this.soundPitch = this.toFloat(data, 0.0F, 10.0F);
                                          } else if (item.equalsIgnoreCase("UAV")) {
                                             this.isUAV = this.toBool(data);
                                             this.isSmallUAV = false;
                                          } else if (item.equalsIgnoreCase("SmallUAV")) {
                                             this.isUAV = this.toBool(data);
                                             this.isSmallUAV = true;
                                          } else if (item.equalsIgnoreCase("TargetDrone")) {
                                             this.isTargetDrone = this.toBool(data);
                                          } else if (item.compareTo("autopilotrot") == 0) {
                                             this.autoPilotRot = this.toFloat(data, -5.0F, 5.0F);
                                          } else if (item.compareTo("ongroundpitch") == 0) {
                                             this.onGroundPitch = -this.toFloat(data, -90.0F, 90.0F);
                                          } else if (item.compareTo("enablegunnermode") == 0) {
                                             this.isEnableGunnerMode = this.toBool(data);
                                          } else if (item.compareTo("hideentity") == 0) {
                                             this.hideEntity = this.toBool(data);
                                          } else if (item.equalsIgnoreCase("SmoothShading")) {
                                             this.smoothShading = this.toBool(data);
                                          } else if (item.compareTo("concurrentgunnermode") == 0) {
                                             this.isEnableConcurrentGunnerMode = this.toBool(data);
                                          } else {
                                             boolean slide;
                                             if (!item.equalsIgnoreCase("AddPartWeaponBay") && !item.equalsIgnoreCase("AddPartSlideWeaponBay")) {
                                                if (item.compareTo("addparthatch") != 0 && item.compareTo("addpartslidehatch") != 0) {
                                                   if (item.compareTo("addpartcanopy") != 0 && item.compareTo("addpartslidecanopy") != 0) {
                                                      if (!item.equalsIgnoreCase("AddPartLG") && !item.equalsIgnoreCase("AddPartSlideRotLG") && !item.equalsIgnoreCase("AddPartLGRev") && !item.equalsIgnoreCase("AddPartLGHatch")) {
                                                         if (item.equalsIgnoreCase("AddPartThrottle")) {
                                                            s = data.split("\\s*,\\s*");
                                                            if (s.length >= 7) {
                                                               df = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
                                                               rx = s.length >= 9 ? this.toFloat(s[8]) : 0.0F;
                                                               ry = s.length >= 10 ? this.toFloat(s[9]) : 0.0F;
                                                               MCH_AircraftInfo.Throttle c = new MCH_AircraftInfo.Throttle(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), "throttle" + this.partThrottle.size(), df, rx, ry);
                                                               this.partThrottle.add(c);
                                                            }
                                                         } else if (item.equalsIgnoreCase("AddPartRotation")) {
                                                            s = data.split("\\s*,\\s*");
                                                            if (s.length >= 7) {
                                                               ys = s.length >= 8 ? this.toBool(s[7]) : true;
                                                               MCH_AircraftInfo.RotPart c = new MCH_AircraftInfo.RotPart(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), ys, "rotpart" + this.partThrottle.size());
                                                               this.partRotPart.add(c);
                                                            }
                                                         } else if (item.compareTo("addpartcamera") == 0) {
                                                            s = data.split("\\s*,\\s*");
                                                            if (s.length >= 3) {
                                                               ys = s.length >= 4 ? this.toBool(s[3]) : true;
                                                               ps = s.length >= 5 ? this.toBool(s[4]) : false;
                                                               MCH_AircraftInfo.Camera c = new MCH_AircraftInfo.Camera(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), 0.0F, -1.0F, 0.0F, "camera" + this.cameraList.size(), ys, ps);
                                                               this.cameraList.add(c);
                                                            }
                                                         } else if (item.equalsIgnoreCase("AddPartWheel")) {
                                                            s = this.splitParam(data);
                                                            if (s.length >= 3) {
                                                               df = s.length >= 4 ? this.toFloat(s[3], -1800.0F, 1800.0F) : 0.0F;
                                                               rx = s.length >= 7 ? this.toFloat(s[4]) : 0.0F;
                                                               ry = s.length >= 7 ? this.toFloat(s[5]) : 1.0F;
                                                               rz = s.length >= 7 ? this.toFloat(s[6]) : 0.0F;
                                                               px = s.length >= 10 ? this.toFloat(s[7]) : this.toFloat(s[0]);
                                                               py = s.length >= 10 ? this.toFloat(s[8]) : this.toFloat(s[1]);
                                                               pz = s.length >= 10 ? this.toFloat(s[9]) : this.toFloat(s[2]);
                                                               this.partWheel.add(new MCH_AircraftInfo.PartWheel(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), rx, ry, rz, df, px, py, pz, "wheel" + this.partWheel.size()));
                                                            }
                                                         } else if (item.equalsIgnoreCase("AddPartSteeringWheel")) {
                                                            s = this.splitParam(data);
                                                            if (s.length >= 7) {
                                                               this.partSteeringWheel.add(new MCH_AircraftInfo.PartWheel(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), "steering_wheel" + this.partSteeringWheel.size()));
                                                            }
                                                         } else if (item.equalsIgnoreCase("AddTrackRoller")) {
                                                            s = this.splitParam(data);
                                                            if (s.length >= 3) {
                                                               this.partTrackRoller.add(new MCH_AircraftInfo.TrackRoller(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), "track_roller" + this.partTrackRoller.size()));
                                                            }
                                                         } else if (item.equalsIgnoreCase("AddCrawlerTrack")) {
                                                            this.partCrawlerTrack.add(this.createCrawlerTrack(data, "crawler_track" + this.partCrawlerTrack.size()));
                                                         } else if (item.equalsIgnoreCase("PivotTurnThrottle")) {
                                                            this.pivotTurnThrottle = this.toFloat(data, 0.0F, 1.0F);
                                                         } else if (item.equalsIgnoreCase("TrackRollerRot")) {
                                                            this.trackRollerRot = this.toFloat(data, -10000.0F, 10000.0F);
                                                         } else if (item.equalsIgnoreCase("PartWheelRot")) {
                                                            this.partWheelRot = this.toFloat(data, -10000.0F, 10000.0F);
                                                         } else if (item.compareTo("camerazoom") == 0) {
                                                            this.cameraZoom = this.toInt(data, 1, 10);
                                                         } else if (item.equalsIgnoreCase("DefaultFreelook")) {
                                                            this.defaultFreelook = this.toBool(data);
                                                         } else if (item.equalsIgnoreCase("BoundingBox")) {
                                                            s = data.split("\\s*,\\s*");
                                                            if (s.length >= 5) {
                                                               df = s.length >= 6 ? this.toFloat(s[5]) : 1.0F;
                                                               MCH_BoundingBox c = new MCH_BoundingBox((double)this.toFloat(s[0]), (double)this.toFloat(s[1]), (double)this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), df);
                                                               this.extraBoundingBox.add(c);
                                                               if (c.getBoundingBox().field_72337_e > (double)this.markerHeight) {
                                                                  this.markerHeight = (float)c.getBoundingBox().field_72337_e;
                                                               }

                                                               this.markerWidth = (float)Math.max((double)this.markerWidth, Math.abs(c.getBoundingBox().field_72336_d) / 2.0D);
                                                               this.markerWidth = (float)Math.max((double)this.markerWidth, Math.abs(c.getBoundingBox().field_72340_a) / 2.0D);
                                                               this.markerWidth = (float)Math.max((double)this.markerWidth, Math.abs(c.getBoundingBox().field_72334_f) / 2.0D);
                                                               this.markerWidth = (float)Math.max((double)this.markerWidth, Math.abs(c.getBoundingBox().field_72339_c) / 2.0D);
                                                               this.bbZmin = (float)Math.min((double)this.bbZmin, c.getBoundingBox().field_72339_c);
                                                               this.bbZmax = (float)Math.min((double)this.bbZmax, c.getBoundingBox().field_72334_f);
                                                            }
                                                         } else if (item.equalsIgnoreCase("RotorSpeed")) {
                                                            this.rotorSpeed = this.toFloat(data, -10000.0F, 10000.0F);
                                                            if ((double)this.rotorSpeed > 0.01D) {
                                                               this.rotorSpeed = (float)((double)this.rotorSpeed - 0.01D);
                                                            }

                                                            if ((double)this.rotorSpeed < -0.01D) {
                                                               this.rotorSpeed = (float)((double)this.rotorSpeed + 0.01D);
                                                            }
                                                         } else if (item.equalsIgnoreCase("OnGroundPitchFactor")) {
                                                            this.onGroundPitchFactor = this.toFloat(data, 0.0F, 180.0F);
                                                         } else if (item.equalsIgnoreCase("OnGroundRollFactor")) {
                                                            this.onGroundRollFactor = this.toFloat(data, 0.0F, 180.0F);
                                                         }
                                                      } else {
                                                         s = data.split("\\s*,\\s*");
                                                         MCH_AircraftInfo.LandingGear n;
                                                         if (!item.equalsIgnoreCase("AddPartSlideRotLG") && s.length >= 6) {
                                                            df = s.length >= 7 ? this.toFloat(s[6], -180.0F, 180.0F) : 90.0F;
                                                            df /= 90.0F;
                                                            n = new MCH_AircraftInfo.LandingGear(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), "lg" + this.landingGear.size(), df, item.equalsIgnoreCase("AddPartLgRev"), item.equalsIgnoreCase("AddPartLGHatch"));
                                                            if (s.length >= 8) {
                                                               n.enableRot2 = true;
                                                               n.maxRotFactor2 = s.length >= 11 ? this.toFloat(s[10], -180.0F, 180.0F) : 90.0F;
                                                               n.maxRotFactor2 /= 90.0F;
                                                               n.rot2 = new Vec3d((double)this.toFloat(s[7]), (double)this.toFloat(s[8]), (double)this.toFloat(s[9]));
                                                            }

                                                            this.landingGear.add(n);
                                                         }

                                                         if (item.equalsIgnoreCase("AddPartSlideRotLG") && s.length >= 9) {
                                                            df = s.length >= 10 ? this.toFloat(s[9], -180.0F, 180.0F) : 90.0F;
                                                            df /= 90.0F;
                                                            n = new MCH_AircraftInfo.LandingGear(this, this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), this.toFloat(s[7]), this.toFloat(s[8]), "lg" + this.landingGear.size(), df, false, false);
                                                            n.slide = new Vec3d((double)this.toFloat(s[0]), (double)this.toFloat(s[1]), (double)this.toFloat(s[2]));
                                                            this.landingGear.add(n);
                                                         }
                                                      }
                                                   } else {
                                                      s = data.split("\\s*,\\s*");
                                                      ys = item.compareTo("addpartslidecanopy") == 0;
                                                      canopyNum = this.canopyList.size();
                                                      if (canopyNum > 0) {
                                                         --canopyNum;
                                                      }

                                                      if (ys) {
                                                         if (s.length >= 3) {
                                                            MCH_AircraftInfo.Canopy c = new MCH_AircraftInfo.Canopy(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), 0.0F, 0.0F, 0.0F, 90.0F, "canopy" + canopyNum, ys);
                                                            this.canopyList.add(c);
                                                            if (canopyNum == 0) {
                                                               c = new MCH_AircraftInfo.Canopy(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), 0.0F, 0.0F, 0.0F, 90.0F, "canopy", ys);
                                                               this.canopyList.add(c);
                                                            }
                                                         }
                                                      } else if (s.length >= 6) {
                                                         ry = s.length >= 7 ? this.toFloat(s[6], -180.0F, 180.0F) : 90.0F;
                                                         ry /= 90.0F;
                                                         MCH_AircraftInfo.Canopy c = new MCH_AircraftInfo.Canopy(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), ry, "canopy" + canopyNum, ys);
                                                         this.canopyList.add(c);
                                                         if (canopyNum == 0) {
                                                            c = new MCH_AircraftInfo.Canopy(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), ry, "canopy", ys);
                                                            this.canopyList.add(c);
                                                         }
                                                      }
                                                   }
                                                } else {
                                                   slide = item.compareTo("addpartslidehatch") == 0;
                                                   s = data.split("\\s*,\\s*");
                                                   n = null;
                                                   MCH_AircraftInfo.Hatch n;
                                                   if (slide) {
                                                      if (s.length >= 3) {
                                                         n = new MCH_AircraftInfo.Hatch(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), 0.0F, 0.0F, 0.0F, 90.0F, "hatch" + this.hatchList.size(), slide);
                                                         this.hatchList.add(n);
                                                      }
                                                   } else if (s.length >= 6) {
                                                      ry = s.length >= 7 ? this.toFloat(s[6], -180.0F, 180.0F) : 90.0F;
                                                      n = new MCH_AircraftInfo.Hatch(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), ry, "hatch" + this.hatchList.size(), slide);
                                                      this.hatchList.add(n);
                                                   }
                                                }
                                             } else {
                                                slide = item.equalsIgnoreCase("AddPartSlideWeaponBay");
                                                s = data.split("\\s*,\\s*");
                                                n = null;
                                                MCH_AircraftInfo.WeaponBay n;
                                                if (slide) {
                                                   if (s.length >= 4) {
                                                      n = new MCH_AircraftInfo.WeaponBay(this, s[0].trim().toLowerCase(), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), 0.0F, 0.0F, 0.0F, 90.0F, "wb" + this.partWeaponBay.size(), slide);
                                                      this.partWeaponBay.add(n);
                                                   }
                                                } else if (s.length >= 7) {
                                                   ry = s.length >= 8 ? this.toFloat(s[7], -180.0F, 180.0F) : 90.0F;
                                                   n = new MCH_AircraftInfo.WeaponBay(this, s[0].trim().toLowerCase(), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), ry / 90.0F, "wb" + this.partWeaponBay.size(), slide);
                                                   this.partWeaponBay.add(n);
                                                }
                                             }
                                          }
                                       } else {
                                          this.isShapedRecipe = item.compareTo("addrecipe") == 0;
                                          this.recipeString.add(data.toUpperCase());
                                       }
                                    } else {
                                       s = data.split("\\s*,\\s*");
                                       if (s.length >= 7) {
                                          df = 0.0F;
                                          rx = 0.0F;
                                          ry = 0.0F;
                                          rz = 0.0F;
                                          isRot = item.equalsIgnoreCase("AddPartRotWeapon") || item.equalsIgnoreCase("AddPartTurretRotWeapon");
                                          isMissile = item.equalsIgnoreCase("AddPartWeaponMissile");
                                          turret = item.equalsIgnoreCase("AddPartTurretWeapon") || item.equalsIgnoreCase("AddPartTurretRotWeapon");
                                          if (isRot) {
                                             df = s.length >= 10 ? this.toFloat(s[7]) : 0.0F;
                                             rx = s.length >= 10 ? this.toFloat(s[8]) : 0.0F;
                                             ry = s.length >= 10 ? this.toFloat(s[9]) : -1.0F;
                                          } else {
                                             rz = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
                                          }

                                          MCH_AircraftInfo.PartWeapon w = new MCH_AircraftInfo.PartWeapon(this, this.splitParamSlash(s[0].toLowerCase().trim()), isRot, isMissile, this.toBool(s[1]), this.toBool(s[2]), this.toBool(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), "weapon" + this.partWeapon.size(), df, rx, ry, rz, turret);
                                          this.lastWeaponPart = w;
                                          this.partWeapon.add(w);
                                       }
                                    }
                                 } else {
                                    s = data.split("\\s*,\\s*");
                                    String type = s[0].toLowerCase();
                                    if (s.length >= 4 && MCH_WeaponInfoManager.contains(type)) {
                                       rx = s.length >= 5 ? this.toFloat(s[4]) : 0.0F;
                                       ry = s.length >= 6 ? this.toFloat(s[5]) : 0.0F;
                                       canUsePilot = s.length >= 7 ? this.toBool(s[6]) : true;
                                       seatID = s.length >= 8 ? this.toInt(s[7], 1, this.getInfo_MaxSeatNum()) - 1 : 0;
                                       if (seatID <= 0) {
                                          canUsePilot = true;
                                       }

                                       py = s.length >= 9 ? this.toFloat(s[8]) : 0.0F;
                                       py = MathHelper.func_76142_g(py);
                                       pz = s.length >= 10 ? this.toFloat(s[9]) : 0.0F;
                                       mxy = s.length >= 11 ? this.toFloat(s[10]) : 0.0F;
                                       float mnp = s.length >= 12 ? this.toFloat(s[11]) : 0.0F;
                                       float mxp = s.length >= 13 ? this.toFloat(s[12]) : 0.0F;
                                       MCH_AircraftInfo.Weapon e = new MCH_AircraftInfo.Weapon(this, this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), rx, ry, canUsePilot, seatID, py, pz, mxy, mnp, mxp, item.equalsIgnoreCase("AddTurretWeapon"));
                                       if (type.compareTo(this.lastWeaponType) != 0) {
                                          this.weaponSetList.add(new MCH_AircraftInfo.WeaponSet(this, type));
                                          ++this.lastWeaponIndex;
                                          this.lastWeaponType = type;
                                       }

                                       ((MCH_AircraftInfo.WeaponSet)this.weaponSetList.get(this.lastWeaponIndex)).weapons.add(e);
                                    }
                                 }
                              }
                           } else {
                              if (this.seatList.size() >= this.getInfo_MaxSeatNum()) {
                                 return;
                              }

                              s = this.splitParam(data);
                              if (s.length < 3) {
                                 return;
                              }

                              p = this.toVec3(s[0], s[1], s[2]);
                              if (item.equalsIgnoreCase("AddSeat")) {
                                 ps = s.length >= 4 ? this.toBool(s[3]) : false;
                                 MCH_SeatInfo seat = new MCH_SeatInfo(p, ps);
                                 this.seatList.add(seat);
                              } else {
                                 if (s.length >= 6) {
                                    MCH_AircraftInfo.CameraPosition c = new MCH_AircraftInfo.CameraPosition(this, this.toVec3(s[3], s[4], s[5]));
                                    canUsePilot = s.length >= 7 ? this.toBool(s[6]) : false;
                                    if (item.equalsIgnoreCase("AddGunnerSeat")) {
                                       if (s.length >= 9) {
                                          px = this.toFloat(s[7], -90.0F, 90.0F);
                                          py = this.toFloat(s[8], -90.0F, 90.0F);
                                          if (px > py) {
                                             pz = px;
                                             px = py;
                                             py = pz;
                                          }

                                          turret = s.length >= 10 ? this.toBool(s[9]) : false;
                                          n = new MCH_SeatInfo(p, true, c, true, canUsePilot, false, 0.0F, 0.0F, px, py, turret);
                                       } else {
                                          n = new MCH_SeatInfo(p, true, c, true, canUsePilot, false, 0.0F, 0.0F, false);
                                       }
                                    } else {
                                       isRot = s.length >= 9;
                                       py = isRot ? this.toFloat(s[7]) : 0.0F;
                                       pz = isRot ? this.toFloat(s[8]) : 0.0F;
                                       boolean rs = s.length >= 10 ? this.toBool(s[9]) : false;
                                       n = new MCH_SeatInfo(p, true, c, true, canUsePilot, isRot, py, pz, rs);
                                    }
                                 } else {
                                    n = new MCH_SeatInfo(p, true, new MCH_AircraftInfo.CameraPosition(this), false, false, false, 0.0F, 0.0F, false);
                                 }

                                 this.seatList.add(n);
                              }
                           }
                        }
                     }
                  }
               } else {
                  s = this.splitParam(data);
                  if (s.length >= 7) {
                     p = this.toVec3(s[0], s[1], s[2]);
                     canopyNum = this.hex2dec(s[3]);
                     i = this.hex2dec(s[4]);
                     rz = this.toFloat(s[5]);
                     px = this.toFloat(s[6]);
                     py = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
                     pz = s.length >= 9 ? this.toFloat(s[8]) : 0.0F;
                     mxy = s.length >= 10 ? this.toFloat(s[9]) : 0.0F;
                     boolean fixDir = !item.equalsIgnoreCase("AddSearchLight");
                     boolean steering = item.equalsIgnoreCase("AddSteeringSearchLight");
                     this.searchLights.add(new MCH_AircraftInfo.SearchLight(this, p, canopyNum, i, rz, px, fixDir, py, pz, steering, mxy));
                  }
               }
            }
         }
      }

   }

   public MCH_AircraftInfo.CrawlerTrack createCrawlerTrack(String data, String name) {
      String[] s = this.splitParam(data);
      int PC = s.length - 3;
      boolean REV = this.toBool(s[0]);
      float LEN = this.toFloat(s[1], 0.001F, 1000.0F) * 0.9F;
      float Z = this.toFloat(s[2]);
      if (PC < 4) {
         return null;
      } else {
         double[] cx = new double[PC];
         double[] cy = new double[PC];

         for(int i = 0; i < PC; ++i) {
            int idx = !REV ? i : PC - i - 1;
            String[] xy = this.splitParamSlash(s[3 + idx]);
            cx[i] = (double)this.toFloat(xy[0]);
            cy[i] = (double)this.toFloat(xy[1]);
         }

         List<MCH_AircraftInfo.CrawlerTrackPrm> lp = new ArrayList();
         lp.add(new MCH_AircraftInfo.CrawlerTrackPrm(this, (float)cx[0], (float)cy[0]));
         double dist = 0.0D;

         int i;
         for(i = 0; i < PC; ++i) {
            double x = cx[(i + 1) % PC] - cx[i];
            double y = cy[(i + 1) % PC] - cy[i];
            dist += Math.sqrt(x * x + y * y);
            double dist2 = dist;

            for(int j = 1; dist >= (double)LEN; ++j) {
               lp.add(new MCH_AircraftInfo.CrawlerTrackPrm(this, (float)(cx[i] + x * ((double)(LEN * (float)j) / dist2)), (float)(cy[i] + y * ((double)(LEN * (float)j) / dist2))));
               dist -= (double)LEN;
            }
         }

         for(i = 0; i < lp.size(); ++i) {
            MCH_AircraftInfo.CrawlerTrackPrm pp = (MCH_AircraftInfo.CrawlerTrackPrm)lp.get((i + lp.size() - 1) % lp.size());
            MCH_AircraftInfo.CrawlerTrackPrm cp = (MCH_AircraftInfo.CrawlerTrackPrm)lp.get(i);
            MCH_AircraftInfo.CrawlerTrackPrm np = (MCH_AircraftInfo.CrawlerTrackPrm)lp.get((i + 1) % lp.size());
            float pr = (float)(Math.atan2((double)(pp.x - cp.x), (double)(pp.y - cp.y)) * 180.0D / 3.141592653589793D);
            float nr = (float)(Math.atan2((double)(np.x - cp.x), (double)(np.y - cp.y)) * 180.0D / 3.141592653589793D);
            float ppr = (pr + 360.0F) % 360.0F;
            float nnr = nr + 180.0F;
            if (((double)nnr < (double)ppr - 0.3D || (double)nnr > (double)ppr + 0.3D) && nnr - ppr < 100.0F && nnr - ppr > -100.0F) {
               nnr = (nnr + ppr) / 2.0F;
            }

            cp.r = nnr;
         }

         MCH_AircraftInfo.CrawlerTrack c = new MCH_AircraftInfo.CrawlerTrack(this, name);
         c.len = LEN;
         c.cx = cx;
         c.cy = cy;
         c.lp = lp;
         c.z = Z;
         c.side = Z >= 0.0F ? 1 : 0;
         return c;
      }
   }

   public String getTextureName() {
      String s = (String)this.textureNameList.get(this.textureCount);
      this.textureCount = (this.textureCount + 1) % this.textureNameList.size();
      return s;
   }

   public String getNextTextureName(String base) {
      if (this.textureNameList.size() >= 2) {
         for(int i = 0; i < this.textureNameList.size(); ++i) {
            String s = (String)this.textureNameList.get(i);
            if (s.equalsIgnoreCase(base)) {
               i = (i + 1) % this.textureNameList.size();
               return (String)this.textureNameList.get(i);
            }
         }
      }

      return base;
   }

   public static String[] getCannotReloadItem() {
      return new String[]{"DisplayName", "AddDisplayName", "ItemID", "AddRecipe", "AddShapelessRecipe", "InventorySize", "Sound", "UAV", "SmallUAV", "TargetDrone", "Category"};
   }

   public boolean canReloadItem(String item) {
      String[] ignoreItems = getCannotReloadItem();
      String[] var3 = ignoreItems;
      int var4 = ignoreItems.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String s = var3[var5];
         if (s.equalsIgnoreCase(item)) {
            return false;
         }
      }

      return true;
   }

   public class Wheel {
      public final float size;
      public final Vec3d pos;

      public Wheel(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d v, float sz) {
         this.pos = v;
         this.size = sz;
      }

      public Wheel(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d v) {
         this(paramMCH_AircraftInfo, v, 1.0F);
      }
   }

   public class WeaponSet {
      public final String type;
      public ArrayList<MCH_AircraftInfo.Weapon> weapons;

      public WeaponSet(MCH_AircraftInfo paramMCH_AircraftInfo, String t) {
         this.type = t;
         this.weapons = new ArrayList();
      }
   }

   public class WeaponBay extends MCH_AircraftInfo.DrawnPart {
      public final float maxRotFactor;
      public final boolean isSlide;
      private final String weaponName;
      public Integer[] weaponIds;

      public WeaponBay(MCH_AircraftInfo paramMCH_AircraftInfo, String wn, float px, float py, float pz, float rx, float ry, float rz, float mr, String name, boolean slide) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
         this.maxRotFactor = mr;
         this.isSlide = slide;
         this.weaponName = wn;
         this.weaponIds = new Integer[0];
      }
   }

   public class Weapon {
      public final Vec3d pos;
      public final float yaw;
      public final float pitch;
      public final boolean canUsePilot;
      public final int seatID;
      public final float defaultYaw;
      public final float minYaw;
      public final float maxYaw;
      public final float minPitch;
      public final float maxPitch;
      public final boolean turret;

      public Weapon(MCH_AircraftInfo paramMCH_AircraftInfo, float x, float y, float z, float yaw, float pitch, boolean canPirot, int seatId, float defy, float mny, float mxy, float mnp, float mxp, boolean turret) {
         this.pos = new Vec3d((double)x, (double)y, (double)z);
         this.yaw = yaw;
         this.pitch = pitch;
         this.canUsePilot = canPirot;
         this.seatID = seatId;
         this.defaultYaw = defy;
         this.minYaw = mny;
         this.maxYaw = mxy;
         this.minPitch = mnp;
         this.maxPitch = mxp;
         this.turret = turret;
      }
   }

   public class TrackRoller extends MCH_AircraftInfo.DrawnPart {
      final int side;

      public TrackRoller(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, String name) {
         super(paramMCH_AircraftInfo, px, py, pz, 0.0F, 0.0F, 0.0F, name);
         this.side = px >= 0.0F ? 1 : 0;
      }
   }

   public class Throttle extends MCH_AircraftInfo.DrawnPart {
      public final Vec3d slide;
      public final float rot2;

      public Throttle(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float rot, String name, float px2, float py2, float pz2) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
         this.rot2 = rot;
         this.slide = new Vec3d((double)px2, (double)py2, (double)pz2);
      }
   }

   public class SearchLight {
      public final int colorStart;
      public final int colorEnd;
      public final Vec3d pos;
      public final float height;
      public final float width;
      public final float angle;
      public final boolean fixDir;
      public final float yaw;
      public final float pitch;
      public final boolean steering;
      public final float stRot;

      public SearchLight(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d pos, int cs, int ce, float h, float w, boolean fix, float y, float p, boolean st, float stRot) {
         this.colorStart = cs;
         this.colorEnd = ce;
         this.pos = pos;
         this.height = h;
         this.width = w;
         this.angle = (float)(Math.atan2((double)(w / 2.0F), (double)h) * 180.0D / 3.141592653589793D);
         this.fixDir = fix;
         this.steering = st;
         this.yaw = y;
         this.pitch = p;
         this.stRot = stRot;
      }
   }

   public class RotPart extends MCH_AircraftInfo.DrawnPart {
      public final float rotSpeed;
      public final boolean rotAlways;

      public RotPart(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, boolean a, String name) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
         this.rotSpeed = mr;
         this.rotAlways = a;
      }
   }

   public class RideRack {
      public final String name;
      public final int rackID;

      public RideRack(MCH_AircraftInfo paramMCH_AircraftInfo, String n, int id) {
         this.name = n;
         this.rackID = id;
      }
   }

   public class RepellingHook {
      final Vec3d pos;
      final int interval;

      public RepellingHook(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d pos, int inv) {
         this.pos = pos;
         this.interval = inv;
      }
   }

   public class PartWheel extends MCH_AircraftInfo.DrawnPart {
      final float rotDir;
      final Vec3d pos2;

      public PartWheel(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float rd, float px2, float py2, float pz2, String name) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
         this.rotDir = rd;
         this.pos2 = new Vec3d((double)px2, (double)py2, (double)pz2);
      }

      public PartWheel(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float rd, String name) {
         this(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, rd, px, py, pz, name);
      }
   }

   public class PartWeaponChild extends MCH_AircraftInfo.DrawnPart {
      public final String[] name;
      public final boolean yaw;
      public final boolean pitch;
      public final float recoilBuf;

      public PartWeaponChild(MCH_AircraftInfo paramMCH_AircraftInfo, String[] name, boolean y, boolean p, float px, float py, float pz, String modelName, float rx, float ry, float rz, float rb) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, modelName);
         this.name = name;
         this.yaw = y;
         this.pitch = p;
         this.recoilBuf = rb;
      }
   }

   public class PartWeapon extends MCH_AircraftInfo.DrawnPart {
      public final String[] name;
      public final boolean rotBarrel;
      public final boolean isMissile;
      public final boolean hideGM;
      public final boolean yaw;
      public final boolean pitch;
      public final float recoilBuf;
      public List<MCH_AircraftInfo.PartWeaponChild> child;
      public final boolean turret;

      public PartWeapon(MCH_AircraftInfo paramMCH_AircraftInfo, String[] name, boolean rotBrl, boolean missile, boolean hgm, boolean y, boolean p, float px, float py, float pz, String modelName, float rx, float ry, float rz, float rb, boolean turret) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, modelName);
         this.name = name;
         this.rotBarrel = rotBrl;
         this.isMissile = missile;
         this.hideGM = hgm;
         this.yaw = y;
         this.pitch = p;
         this.recoilBuf = rb;
         this.child = new ArrayList();
         this.turret = turret;
      }
   }

   public class ParticleSplash {
      public final int num;
      public final float acceleration;
      public final float size;
      public final Vec3d pos;
      public final int age;
      public final float motionY;
      public final float gravity;

      public ParticleSplash(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d v, int nm, float siz, float acc, int ag, float my, float gr) {
         this.num = nm;
         this.pos = v;
         this.size = siz;
         this.acceleration = acc;
         this.age = ag;
         this.motionY = my;
         this.gravity = gr;
      }
   }

   public class LandingGear extends MCH_AircraftInfo.DrawnPart {
      public Vec3d slide = null;
      public final float maxRotFactor;
      public boolean enableRot2;
      public Vec3d rot2;
      public float maxRotFactor2;
      public final boolean reverse;
      public final boolean hatch;

      public LandingGear(MCH_AircraftInfo paramMCH_AircraftInfo, float x, float y, float z, float rx, float ry, float rz, String model, float maxRotF, boolean rev, boolean isHatch) {
         super(paramMCH_AircraftInfo, x, y, z, rx, ry, rz, model);
         this.maxRotFactor = maxRotF;
         this.enableRot2 = false;
         this.rot2 = new Vec3d(0.0D, 0.0D, 0.0D);
         this.maxRotFactor2 = 0.0F;
         this.reverse = rev;
         this.hatch = isHatch;
      }
   }

   public class Hatch extends MCH_AircraftInfo.DrawnPart {
      public final float maxRotFactor;
      public final float maxRot;
      public final boolean isSlide;

      public Hatch(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name, boolean slide) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
         this.maxRot = mr;
         this.maxRotFactor = this.maxRot / 90.0F;
         this.isSlide = slide;
      }
   }

   public class Flare {
      public int[] types = new int[0];
      public Vec3d pos = new Vec3d(0.0D, 0.0D, 0.0D);

      public Flare(MCH_AircraftInfo paramMCH_AircraftInfo) {
      }
   }

   public class DrawnPart {
      public final Vec3d pos;
      public final Vec3d rot;
      public final String modelName;
      public _IModelCustom model;

      public DrawnPart(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, String name) {
         this.pos = new Vec3d((double)px, (double)py, (double)pz);
         this.rot = new Vec3d((double)rx, (double)ry, (double)rz);
         this.modelName = name;
         this.model = null;
      }
   }

   public class CrawlerTrackPrm {
      float x;
      float y;
      float nx;
      float ny;
      float r;

      public CrawlerTrackPrm(MCH_AircraftInfo paramMCH_AircraftInfo, float x, float y) {
         this.x = x;
         this.y = y;
      }
   }

   public class CrawlerTrack extends MCH_AircraftInfo.DrawnPart {
      public float len = 0.35F;
      public double[] cx;
      public double[] cy;
      public List<MCH_AircraftInfo.CrawlerTrackPrm> lp;
      public float z;
      public int side;

      public CrawlerTrack(MCH_AircraftInfo paramMCH_AircraftInfo, String name) {
         super(paramMCH_AircraftInfo, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, name);
      }
   }

   public class Canopy extends MCH_AircraftInfo.DrawnPart {
      public final float maxRotFactor;
      public final boolean isSlide;

      public Canopy(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name, boolean slide) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
         this.maxRotFactor = mr;
         this.isSlide = slide;
      }
   }

   public class CameraPosition {
      public final Vec3d pos;
      public final boolean fixRot;
      public final float yaw;
      public final float pitch;

      public CameraPosition(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d vec3, boolean fixRot, float yaw, float pitch) {
         this.pos = vec3;
         this.fixRot = fixRot;
         this.yaw = yaw;
         this.pitch = pitch;
      }

      public CameraPosition(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d vec3) {
         this(paramMCH_AircraftInfo, vec3, false, 0.0F, 0.0F);
      }

      public CameraPosition(MCH_AircraftInfo paramMCH_AircraftInfo) {
         this(paramMCH_AircraftInfo, new Vec3d(0.0D, 0.0D, 0.0D));
      }
   }

   public class Camera extends MCH_AircraftInfo.DrawnPart {
      public final boolean yawSync;
      public final boolean pitchSync;

      public Camera(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, String name, boolean ys, boolean ps) {
         super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
         this.yawSync = ys;
         this.pitchSync = ps;
      }
   }
}
