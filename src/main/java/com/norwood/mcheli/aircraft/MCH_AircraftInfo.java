package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.helper.client._IModelCustom;
import com.norwood.mcheli.helper.info.IItemContent;
import com.norwood.mcheli.hud.MCH_Hud;
import com.norwood.mcheli.hud.MCH_HudManager;
import com.norwood.mcheli.weapon.MCH_WeaponInfoManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public abstract class MCH_AircraftInfo extends MCH_BaseInfo implements IItemContent {
    public final String name;
    public String displayName;
    public final HashMap<String, String> displayNameLang;
    public int itemID;
    public List<String> recipeString;
    public final List<IRecipe> recipe;
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
    public final MCH_AircraftInfo.Flare flare;
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
    public final List<MCH_AircraftInfo.WeaponSet> weaponSetList;
    public final List<MCH_SeatInfo> seatList;
    public final List<Integer[]> exclusionSeatList;
    public final List<MCH_Hud> hudList;
    public MCH_Hud hudTvMissile;
    public float damageFactor;
    public float submergedDamageHeight;
    public boolean regeneration;
    public final List<MCH_BoundingBox> extraBoundingBox;
    public final List<MCH_AircraftInfo.Wheel> wheels;
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
    public final List<MCH_SeatRackInfo> entityRackList;
    public int mobSeatNum;
    public int entityRackNum;
    public final MCH_MobDropOption mobDropOption;
    public final List<MCH_AircraftInfo.RepellingHook> repellingHooks;
    public final List<MCH_AircraftInfo.RideRack> rideRacks;
    public final List<MCH_AircraftInfo.ParticleSplash> particleSplashs;
    public final List<MCH_AircraftInfo.SearchLight> searchLights;
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
    public final List<MCH_AircraftInfo.CameraPosition> cameraPosition;
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
    public int textureCount;
    public float particlesScale;
    public boolean hideEntity;
    public boolean smoothShading;
    public String soundMove;
    public float soundRange;
    public float soundVolume;
    public float soundPitch;
    public _IModelCustom model;
    public final List<MCH_AircraftInfo.Hatch> hatchList;
    public final List<MCH_AircraftInfo.Camera> cameraList;
    public final List<MCH_AircraftInfo.PartWeapon> partWeapon;
    public final List<MCH_AircraftInfo.WeaponBay> partWeaponBay;
    public final List<MCH_AircraftInfo.Canopy> canopyList;
    public final List<MCH_AircraftInfo.LandingGear> landingGear;
    public final List<MCH_AircraftInfo.Throttle> partThrottle;
    public final List<MCH_AircraftInfo.RotPart> partRotPart;
    public final List<MCH_AircraftInfo.CrawlerTrack> partCrawlerTrack;
    public final List<MCH_AircraftInfo.TrackRoller> partTrackRoller;
    public final List<MCH_AircraftInfo.PartWheel> partWheel;
    public final List<MCH_AircraftInfo.PartWheel> partSteeringWheel;
    public final List<MCH_AircraftInfo.Hatch> lightHatchList;
    private final List<String> textureNameList;
    private String lastWeaponType = "";
    private int lastWeaponIndex = -1;
    private MCH_AircraftInfo.PartWeapon lastWeaponPart;

    public MCH_AircraftInfo(AddonResourceLocation location, String path) {
        super(location, path);
        this.name = location.getPath();
        this.displayName = this.name;
        this.displayNameLang = new HashMap<>();
        this.itemID = 0;
        this.recipeString = new ArrayList<>();
        this.recipe = new ArrayList<>();
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
        this.flare = new Flare(this);
        this.weaponSetList = new ArrayList<>();
        this.seatList = new ArrayList<>();
        this.exclusionSeatList = new ArrayList<>();
        this.hudList = new ArrayList<>();
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
        this.extraBoundingBox = new ArrayList<>();
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
        this.entityRackList = new ArrayList<>();
        this.mobSeatNum = 0;
        this.entityRackNum = 0;
        this.mobDropOption = new MCH_MobDropOption();
        this.repellingHooks = new ArrayList<>();
        this.rideRacks = new ArrayList<>();
        this.particleSplashs = new ArrayList<>();
        this.searchLights = new ArrayList<>();
        this.markerHeight = 1.0F;
        this.markerWidth = 2.0F;
        this.bbZmax = 1.0F;
        this.bbZmin = -1.0F;
        this.rotorSpeed = this.getDefaultRotorSpeed();
        this.wheels = this.getDefaultWheelList();
        this.onGroundPitchFactor = 0.0F;
        this.onGroundRollFactor = 0.0F;
        this.turretPosition = new Vec3d(0.0, 0.0, 0.0);
        this.defaultFreelook = false;
        this.unmountPosition = null;
        this.thirdPersonDist = 4.0F;
        this.cameraPosition = new ArrayList<>();
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
        this.textureNameList = new ArrayList<>();
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
        this.hatchList = new ArrayList<>();
        this.cameraList = new ArrayList<>();
        this.partWeapon = new ArrayList<>();
        this.lastWeaponPart = null;
        this.partWeaponBay = new ArrayList<>();
        this.canopyList = new ArrayList<>();
        this.landingGear = new ArrayList<>();
        this.partThrottle = new ArrayList<>();
        this.partRotPart = new ArrayList<>();
        this.partCrawlerTrack = new ArrayList<>();
        this.partTrackRoller = new ArrayList<>();
        this.partWheel = new ArrayList<>();
        this.partSteeringWheel = new ArrayList<>();
        this.lightHatchList = new ArrayList<>();
    }

    public static String[] getCannotReloadItem() {
        return new String[]{
                "DisplayName", "AddDisplayName", "ItemID", "AddRecipe", "AddShapelessRecipe", "InventorySize", "Sound", "UAV", "SmallUAV", "TargetDrone", "Category"
        };
    }

    public ItemStack getItemStack() {
        return new ItemStack(this.getItem());
    }

    public abstract String getDirectoryName();

    public abstract String getKindName();

    public float getDefaultSoundRange() {
        return 100.0F;
    }

    public List<MCH_AircraftInfo.Wheel> getDefaultWheelList() {
        return new ArrayList<>();
    }

    public float getDefaultRotorSpeed() {
        return 0.0F;
    }

    private float getDefaultStepHeight() {
        return 0.0F;
    }

    public boolean haveRepellingHook() {
        return !this.repellingHooks.isEmpty();
    }

    public boolean haveFlare() {
        return this.flare.types.length > 0;
    }

    public boolean haveCanopy() {
        return !this.canopyList.isEmpty();
    }

    public boolean haveLandingGear() {
        return !this.landingGear.isEmpty();
    }

    public abstract String getDefaultHudName(int var1);

    @Override
    public boolean validate() throws Exception {
        if (this.cameraPosition.isEmpty()) {
            this.cameraPosition.add(new CameraPosition(this));
        }

        this.bbZ = (this.bbZmax + this.bbZmin) / 2.0F;
        if (this.isTargetDrone) {
            this.isUAV = true;
        }

        if (this.isEnableParachuting && !this.repellingHooks.isEmpty()) {
            this.isEnableParachuting = false;
            this.repellingHooks.clear();
        }

        if (this.isUAV) {
            this.alwaysCameraView = true;
            if (this.seatList.isEmpty()) {
                MCH_SeatInfo s = new MCH_SeatInfo(new Vec3d(0.0, 0.0, 0.0), false);
                this.seatList.add(s);
            }
        }

        this.mobSeatNum = this.seatList.size();
        this.entityRackNum = this.entityRackList.size();
        if (this.getNumSeat() < 1) {
            throw new Exception("At least one seat must be set.");
        } else {
            if (this.getNumHud() < this.getNumSeat()) {
                for (int i = this.getNumHud(); i < this.getNumSeat(); i++) {
                    this.hudList.add(MCH_HudManager.get(this.getDefaultHudName(i)));
                }
            }

            if (this.getNumSeat() == 1 && this.getNumHud() == 1) {
                this.hudList.add(MCH_HudManager.get(this.getDefaultHudName(1)));
            }

            this.seatList.addAll(this.entityRackList);

            this.entityRackList.clear();
            if (this.hudTvMissile == null) {
                this.hudTvMissile = MCH_HudManager.get("tv_missile");
            }

            if (this.textureNameList.isEmpty()) {
                throw new Exception("At least one texture must be set.");
            } else {
                if (this.itemID <= 0) {
                }

                for (int i = 0; i < this.partWeaponBay.size(); i++) {
                    MCH_AircraftInfo.WeaponBay wb = this.partWeaponBay.get(i);
                    String[] weaponNames = wb.weaponName.split("\\s*/\\s*");
                    if (weaponNames.length == 0) {
                        this.partWeaponBay.remove(i);
                    } else {
                        List<Integer> list = new ArrayList<>();

                        for (String s : weaponNames) {
                            int id = this.getWeaponIdByName(s);
                            if (id >= 0) {
                                list.add(id);
                            }
                        }

                        if (list.isEmpty()) {
                            this.partWeaponBay.remove(i);
                        } else {
                            this.partWeaponBay.get(i).weaponIds = list.toArray(new Integer[0]);
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
        return !this.hatchList.isEmpty();
    }

    public boolean havePartCamera() {
        return !this.cameraList.isEmpty();
    }

    public boolean havePartThrottle() {
        return !this.partThrottle.isEmpty();
    }

    public MCH_AircraftInfo.WeaponSet getWeaponSetById(int id) {
        return id >= 0 && id < this.weaponSetList.size() ? this.weaponSetList.get(id) : null;
    }

    public MCH_AircraftInfo.Weapon getWeaponById(int id) {
        MCH_AircraftInfo.WeaponSet ws = this.getWeaponSetById(id);
        return ws != null ? ws.weapons.get(0) : null;
    }

    public int getWeaponIdByName(String s) {
        for (int i = 0; i < this.weaponSetList.size(); i++) {
            if (this.weaponSetList.get(i).type.equalsIgnoreCase(s)) {
                return i;
            }
        }

        return -1;
    }

    public MCH_AircraftInfo.Weapon getWeaponByName(String s) {
        for (int i = 0; i < this.weaponSetList.size(); i++) {
            if (this.weaponSetList.get(i).type.equalsIgnoreCase(s)) {
                return this.getWeaponById(i);
            }
        }

        return null;
    }

    public int getWeaponCount() {
        return this.weaponSetList.size();
    }

    @Override
    public void loadItemData(String item, String data) {
        if (item.compareTo("displayname") == 0) {
            this.displayName = data.trim();
        } else if (item.compareTo("adddisplayname") == 0) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length == 2) {
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
            String[] s = this.splitParam(data);
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
        } else if (item.equalsIgnoreCase("AddParticleSplash")) {
            String[] s = this.splitParam(data);
            if (s.length >= 3) {
                Vec3d v = this.toVec3(s[0], s[1], s[2]);
                int num = s.length >= 4 ? this.toInt(s[3], 1, 100) : 2;
                float size = s.length >= 5 ? this.toFloat(s[4]) : 2.0F;
                float acc = s.length >= 6 ? this.toFloat(s[5]) : 1.0F;
                int age = s.length >= 7 ? this.toInt(s[6], 1, 100000) : 80;
                float motionY = s.length >= 8 ? this.toFloat(s[7]) : 0.01F;
                float gravity = s.length >= 9 ? this.toFloat(s[8]) : 0.0F;
                this.particleSplashs.add(new ParticleSplash(this, v, num, size, acc, age, motionY, gravity));
            }
        } else if (item.equalsIgnoreCase("AddSearchLight") || item.equalsIgnoreCase("AddFixedSearchLight") || item.equalsIgnoreCase("AddSteeringSearchLight")) {
            String[] s = this.splitParam(data);
            if (s.length >= 7) {
                Vec3d v = this.toVec3(s[0], s[1], s[2]);
                int cs = this.hex2dec(s[3]);
                int ce = this.hex2dec(s[4]);
                float h = this.toFloat(s[5]);
                float w = this.toFloat(s[6]);
                float yaw = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
                float pitch = s.length >= 9 ? this.toFloat(s[8]) : 0.0F;
                float stRot = s.length >= 10 ? this.toFloat(s[9]) : 0.0F;
                boolean fixDir = !item.equalsIgnoreCase("AddSearchLight");
                boolean steering = item.equalsIgnoreCase("AddSteeringSearchLight");
                this.searchLights.add(new SearchLight(this, v, cs, ce, h, w, fixDir, yaw, pitch, steering, stRot));
            }
        } else if (item.equalsIgnoreCase("AddPartLightHatch")) {
            String[] s = this.splitParam(data);
            if (s.length >= 6) {
                float mx = s.length >= 7 ? this.toFloat(s[6], -1800.0F, 1800.0F) : 90.0F;
                this.lightHatchList
                        .add(
                                new Hatch(
                                        this,
                                        this.toFloat(s[0]),
                                        this.toFloat(s[1]),
                                        this.toFloat(s[2]),
                                        this.toFloat(s[3]),
                                        this.toFloat(s[4]),
                                        this.toFloat(s[5]),
                                        mx,
                                        "light_hatch" + this.lightHatchList.size(),
                                        false
                                )
                        );
            }
        } else if (item.equalsIgnoreCase("AddRepellingHook")) {
            String[] s = this.splitParam(data);
            if (s != null && s.length >= 3) {
                int inv = s.length >= 4 ? this.toInt(s[3], 1, 100000) : 10;
                this.repellingHooks.add(new RepellingHook(this, this.toVec3(s[0], s[1], s[2]), inv));
            }
        } else if (item.equalsIgnoreCase("AddRack")) {
            String[] s = data.toLowerCase().split("\\s*,\\s*");
            if (s.length >= 7) {
                String[] names = s[0].split("\\s*/\\s*");
                float range = s.length >= 8 ? this.toFloat(s[7]) : 6.0F;
                float para = s.length >= 9 ? this.toFloat(s[8], 0.0F, 1000000.0F) : 20.0F;
                float yaw = s.length >= 10 ? this.toFloat(s[9]) : 0.0F;
                float pitch = s.length >= 11 ? this.toFloat(s[10]) : 0.0F;
                boolean rs = s.length >= 12 && this.toBool(s[11]);
                this.entityRackList
                        .add(
                                new MCH_SeatRackInfo(
                                        names,
                                        this.toDouble(s[1]),
                                        this.toDouble(s[2]),
                                        this.toDouble(s[3]),
                                        new CameraPosition(this, this.toVec3(s[4], s[5], s[6]).add(0.0, 1.5, 0.0)),
                                        range,
                                        para,
                                        yaw,
                                        pitch,
                                        rs
                                )
                        );
            }
        } else if (item.equalsIgnoreCase("RideRack")) {
            String[] s = this.splitParam(data);
            if (s.length >= 2) {
                MCH_AircraftInfo.RideRack r = new RideRack(this, s[0].trim().toLowerCase(), this.toInt(s[1], 1, 10000));
                this.rideRacks.add(r);
            }
        } else if (item.equalsIgnoreCase("AddSeat") || item.equalsIgnoreCase("AddGunnerSeat") || item.equalsIgnoreCase("AddFixRotSeat")) {
            if (this.seatList.size() >= this.getInfo_MaxSeatNum()) {
                return;
            }

            String[] s = this.splitParam(data);
            if (s.length < 3) {
                return;
            }

            Vec3d p = this.toVec3(s[0], s[1], s[2]);
            if (item.equalsIgnoreCase("AddSeat")) {
                boolean rs = s.length >= 4 && this.toBool(s[3]);
                MCH_SeatInfo seat = new MCH_SeatInfo(p, rs);
                this.seatList.add(seat);
            } else {
                MCH_SeatInfo seat;
                if (s.length >= 6) {
                    MCH_AircraftInfo.CameraPosition c = new CameraPosition(this, this.toVec3(s[3], s[4], s[5]));
                    boolean sg = s.length >= 7 && this.toBool(s[6]);
                    if (item.equalsIgnoreCase("AddGunnerSeat")) {
                        if (s.length >= 9) {
                            float minPitch = this.toFloat(s[7], -90.0F, 90.0F);
                            float maxPitch = this.toFloat(s[8], -90.0F, 90.0F);
                            if (minPitch > maxPitch) {
                                float t = minPitch;
                                minPitch = maxPitch;
                                maxPitch = t;
                            }

                            boolean rs = s.length >= 10 && this.toBool(s[9]);
                            seat = new MCH_SeatInfo(p, true, c, true, sg, false, 0.0F, 0.0F, minPitch, maxPitch, rs);
                        } else {
                            seat = new MCH_SeatInfo(p, true, c, true, sg, false, 0.0F, 0.0F, false);
                        }
                    } else {
                        boolean fixRot = s.length >= 9;
                        float fixYaw = fixRot ? this.toFloat(s[7]) : 0.0F;
                        float fixPitch = fixRot ? this.toFloat(s[8]) : 0.0F;
                        boolean rs = s.length >= 10 && this.toBool(s[9]);
                        seat = new MCH_SeatInfo(p, true, c, true, sg, fixRot, fixYaw, fixPitch, rs);
                    }
                } else {
                    seat = new MCH_SeatInfo(p, true, new CameraPosition(this), false, false, false, 0.0F, 0.0F, false);
                }

                this.seatList.add(seat);
            }
        } else if (item.equalsIgnoreCase("SetWheelPos")) {
            String[] sx = this.splitParam(data);
            if (sx.length >= 4) {
                float x = Math.abs(this.toFloat(sx[0]));
                float y = this.toFloat(sx[1]);
                this.wheels.clear();

                for (int i = 2; i < sx.length; i++) {
                    this.wheels.add(new Wheel(this, new Vec3d(x, y, this.toFloat(sx[i]))));
                }

                this.wheels.sort((arg0, arg1) -> arg0.pos.z > arg1.pos.z ? -1 : 1);
            }
        } else if (item.equalsIgnoreCase("ExclusionSeat")) {
            String[] sx = this.splitParam(data);
            if (sx.length >= 2) {
                Integer[] a = new Integer[sx.length];

                for (int i = 0; i < a.length; i++) {
                    a[i] = this.toInt(sx[i], 1, 10000) - 1;
                }

                this.exclusionSeatList.add(a);
            }
        } else if (MCH_MOD.proxy.isRemote() && item.equalsIgnoreCase("HUD")) {
            this.hudList.clear();
            String[] ss = data.split("\\s*,\\s*");

            for (String sx : ss) {
                MCH_Hud hud = MCH_HudManager.get(sx);
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
            String[] sx = this.splitParam(data);
            if (sx.length >= 3) {
                this.mobDropOption.pos = this.toVec3(sx[0], sx[1], sx[2]);
                this.mobDropOption.interval = sx.length >= 4 ? this.toInt(sx[3]) : 12;
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
        } else if (item.compareTo("cameraposition") == 0) {
            String[] sx = data.split("\\s*,\\s*");
            if (sx.length >= 3) {
                this.alwaysCameraView = sx.length >= 4 && this.toBool(sx[3]);
                boolean fixRot = sx.length >= 5;
                float yaw = sx.length >= 5 ? this.toFloat(sx[4]) : 0.0F;
                float pitch = sx.length >= 6 ? this.toFloat(sx[5]) : 0.0F;
                this.cameraPosition.add(new CameraPosition(this, this.toVec3(sx[0], sx[1], sx[2]), fixRot, yaw, pitch));
            }
        } else if (item.equalsIgnoreCase("UnmountPosition")) {
            String[] sx = data.split("\\s*,\\s*");
            if (sx.length >= 3) {
                this.unmountPosition = this.toVec3(sx[0], sx[1], sx[2]);
            }
        } else if (item.equalsIgnoreCase("ThirdPersonDist")) {
            this.thirdPersonDist = this.toFloat(data, 4.0F, 100.0F);
        } else if (item.equalsIgnoreCase("TurretPosition")) {
            String[] sx = data.split("\\s*,\\s*");
            if (sx.length >= 3) {
                this.turretPosition = this.toVec3(sx[0], sx[1], sx[2]);
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
        } else if (item.equalsIgnoreCase("AddWeapon") || item.equalsIgnoreCase("AddTurretWeapon")) {
            String[] sx = data.split("\\s*,\\s*");
            String type = sx[0].toLowerCase();
            if (sx.length >= 4 && MCH_WeaponInfoManager.contains(type)) {
                float y = sx.length >= 5 ? this.toFloat(sx[4]) : 0.0F;
                float p = sx.length >= 6 ? this.toFloat(sx[5]) : 0.0F;
                boolean canUsePilot = sx.length < 7 || this.toBool(sx[6]);
                int seatID = sx.length >= 8 ? this.toInt(sx[7], 1, this.getInfo_MaxSeatNum()) - 1 : 0;
                if (seatID <= 0) {
                    canUsePilot = true;
                }

                float dfy = sx.length >= 9 ? this.toFloat(sx[8]) : 0.0F;
                dfy = MathHelper.wrapDegrees(dfy);
                float mny = sx.length >= 10 ? this.toFloat(sx[9]) : 0.0F;
                float mxy = sx.length >= 11 ? this.toFloat(sx[10]) : 0.0F;
                float mnp = sx.length >= 12 ? this.toFloat(sx[11]) : 0.0F;
                float mxp = sx.length >= 13 ? this.toFloat(sx[12]) : 0.0F;
                MCH_AircraftInfo.Weapon e = new Weapon(
                        this,
                        this.toFloat(sx[1]),
                        this.toFloat(sx[2]),
                        this.toFloat(sx[3]),
                        y,
                        p,
                        canUsePilot,
                        seatID,
                        dfy,
                        mny,
                        mxy,
                        mnp,
                        mxp,
                        item.equalsIgnoreCase("AddTurretWeapon")
                );
                if (type.compareTo(this.lastWeaponType) != 0) {
                    this.weaponSetList.add(new WeaponSet(this, type));
                    this.lastWeaponIndex++;
                    this.lastWeaponType = type;
                }

                this.weaponSetList.get(this.lastWeaponIndex).weapons.add(e);
            }
        } else if (item.equalsIgnoreCase("AddPartWeapon")
                || item.equalsIgnoreCase("AddPartRotWeapon")
                || item.equalsIgnoreCase("AddPartTurretWeapon")
                || item.equalsIgnoreCase("AddPartTurretRotWeapon")
                || item.equalsIgnoreCase("AddPartWeaponMissile")) {
            String[] sx = data.split("\\s*,\\s*");
            if (sx.length >= 7) {
                float rx = 0.0F;
                float ry = 0.0F;
                float rz = 0.0F;
                float rb = 0.0F;
                boolean isRot = item.equalsIgnoreCase("AddPartRotWeapon") || item.equalsIgnoreCase("AddPartTurretRotWeapon");
                boolean isMissile = item.equalsIgnoreCase("AddPartWeaponMissile");
                boolean turret = item.equalsIgnoreCase("AddPartTurretWeapon") || item.equalsIgnoreCase("AddPartTurretRotWeapon");
                if (isRot) {
                    rx = sx.length >= 10 ? this.toFloat(sx[7]) : 0.0F;
                    ry = sx.length >= 10 ? this.toFloat(sx[8]) : 0.0F;
                    rz = sx.length >= 10 ? this.toFloat(sx[9]) : -1.0F;
                } else {
                    rb = sx.length >= 8 ? this.toFloat(sx[7]) : 0.0F;
                }

                MCH_AircraftInfo.PartWeapon w = new PartWeapon(
                        this,
                        this.splitParamSlash(sx[0].toLowerCase().trim()),
                        isRot,
                        isMissile,
                        this.toBool(sx[1]),
                        this.toBool(sx[2]),
                        this.toBool(sx[3]),
                        this.toFloat(sx[4]),
                        this.toFloat(sx[5]),
                        this.toFloat(sx[6]),
                        "weapon" + this.partWeapon.size(),
                        rx,
                        ry,
                        rz,
                        rb,
                        turret
                );
                this.lastWeaponPart = w;
                this.partWeapon.add(w);
            }
        } else if (item.equalsIgnoreCase("AddPartWeaponChild")) {
            String[] sx = data.split("\\s*,\\s*");
            if (sx.length >= 5 && this.lastWeaponPart != null) {
                float rb = sx.length >= 6 ? this.toFloat(sx[5]) : 0.0F;
                MCH_AircraftInfo.PartWeaponChild w = new PartWeaponChild(
                        this,
                        this.lastWeaponPart.name,
                        this.toBool(sx[0]),
                        this.toBool(sx[1]),
                        this.toFloat(sx[2]),
                        this.toFloat(sx[3]),
                        this.toFloat(sx[4]),
                        this.lastWeaponPart.modelName + "_" + this.lastWeaponPart.child.size(),
                        0.0F,
                        0.0F,
                        0.0F,
                        rb
                );
                this.lastWeaponPart.child.add(w);
            }
        } else if (item.compareTo("addrecipe") == 0 || item.compareTo("addshapelessrecipe") == 0) {
            this.isShapedRecipe = item.compareTo("addrecipe") == 0;
            this.recipeString.add(data.toUpperCase());
        } else if (item.compareTo("maxhp") == 0) {
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
            String[] sx = data.split("\\s*,\\s*");
            this.flare.types = new int[sx.length];

            for (int i = 0; i < sx.length; i++) {
                this.flare.types[i] = this.toInt(sx[i], 1, 10);
            }
        } else if (item.equalsIgnoreCase("FlareOption")) {
            String[] sx = this.splitParam(data);
            if (sx.length >= 3) {
                this.flare.pos = this.toVec3(sx[0], sx[1], sx[2]);
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
        } else if (item.equalsIgnoreCase("AddPartWeaponBay") || item.equalsIgnoreCase("AddPartSlideWeaponBay")) {
            boolean slide = item.equalsIgnoreCase("AddPartSlideWeaponBay");
            String[] sx = data.split("\\s*,\\s*");
            MCH_AircraftInfo.WeaponBay n;
            if (slide) {
                if (sx.length >= 4) {
                    n = new WeaponBay(
                            this,
                            sx[0].trim().toLowerCase(),
                            this.toFloat(sx[1]),
                            this.toFloat(sx[2]),
                            this.toFloat(sx[3]),
                            0.0F,
                            0.0F,
                            0.0F,
                            90.0F,
                            "wb" + this.partWeaponBay.size(),
                            true
                    );
                    this.partWeaponBay.add(n);
                }
            } else if (sx.length >= 7) {
                float mx = sx.length >= 8 ? this.toFloat(sx[7], -180.0F, 180.0F) : 90.0F;
                n = new WeaponBay(
                        this,
                        sx[0].trim().toLowerCase(),
                        this.toFloat(sx[1]),
                        this.toFloat(sx[2]),
                        this.toFloat(sx[3]),
                        this.toFloat(sx[4]),
                        this.toFloat(sx[5]),
                        this.toFloat(sx[6]),
                        mx / 90.0F,
                        "wb" + this.partWeaponBay.size(),
                        false
                );
                this.partWeaponBay.add(n);
            }
        } else if (item.compareTo("addparthatch") == 0 || item.compareTo("addpartslidehatch") == 0) {
            boolean slide = item.compareTo("addpartslidehatch") == 0;
            String[] sx = data.split("\\s*,\\s*");
            MCH_AircraftInfo.Hatch n;
            if (slide) {
                if (sx.length >= 3) {
                    n = new Hatch(
                            this, this.toFloat(sx[0]), this.toFloat(sx[1]), this.toFloat(sx[2]), 0.0F, 0.0F, 0.0F, 90.0F, "hatch" + this.hatchList.size(), true
                    );
                    this.hatchList.add(n);
                }
            } else if (sx.length >= 6) {
                float mx = sx.length >= 7 ? this.toFloat(sx[6], -180.0F, 180.0F) : 90.0F;
                n = new Hatch(
                        this,
                        this.toFloat(sx[0]),
                        this.toFloat(sx[1]),
                        this.toFloat(sx[2]),
                        this.toFloat(sx[3]),
                        this.toFloat(sx[4]),
                        this.toFloat(sx[5]),
                        mx,
                        "hatch" + this.hatchList.size(),
                        false
                );
                this.hatchList.add(n);
            }
        } else if (item.compareTo("addpartcanopy") == 0 || item.compareTo("addpartslidecanopy") == 0) {
            String[] sx = data.split("\\s*,\\s*");
            boolean slide = item.compareTo("addpartslidecanopy") == 0;
            int canopyNum = this.canopyList.size();
            if (canopyNum > 0) {
                canopyNum--;
            }

            if (slide) {
                if (sx.length >= 3) {
                    MCH_AircraftInfo.Canopy c = new Canopy(
                            this, this.toFloat(sx[0]), this.toFloat(sx[1]), this.toFloat(sx[2]), 0.0F, 0.0F, 0.0F, 90.0F, "canopy" + canopyNum, true
                    );
                    this.canopyList.add(c);
                    if (canopyNum == 0) {
                        c = new Canopy(this, this.toFloat(sx[0]), this.toFloat(sx[1]), this.toFloat(sx[2]), 0.0F, 0.0F, 0.0F, 90.0F, "canopy", true);
                        this.canopyList.add(c);
                    }
                }
            } else if (sx.length >= 6) {
                float mx = sx.length >= 7 ? this.toFloat(sx[6], -180.0F, 180.0F) : 90.0F;
                mx /= 90.0F;
                MCH_AircraftInfo.Canopy c = new Canopy(
                        this,
                        this.toFloat(sx[0]),
                        this.toFloat(sx[1]),
                        this.toFloat(sx[2]),
                        this.toFloat(sx[3]),
                        this.toFloat(sx[4]),
                        this.toFloat(sx[5]),
                        mx,
                        "canopy" + canopyNum,
                        false
                );
                this.canopyList.add(c);
                if (canopyNum == 0) {
                    c = new Canopy(
                            this,
                            this.toFloat(sx[0]),
                            this.toFloat(sx[1]),
                            this.toFloat(sx[2]),
                            this.toFloat(sx[3]),
                            this.toFloat(sx[4]),
                            this.toFloat(sx[5]),
                            mx,
                            "canopy",
                            false
                    );
                    this.canopyList.add(c);
                }
            }
        } else if (item.equalsIgnoreCase("AddPartLG")
                || item.equalsIgnoreCase("AddPartSlideRotLG")
                || item.equalsIgnoreCase("AddPartLGRev")
                || item.equalsIgnoreCase("AddPartLGHatch")) {
            String[] sxx = data.split("\\s*,\\s*");
            if (!item.equalsIgnoreCase("AddPartSlideRotLG") && sxx.length >= 6) {
                float maxRot = sxx.length >= 7 ? this.toFloat(sxx[6], -180.0F, 180.0F) : 90.0F;
                maxRot /= 90.0F;
                MCH_AircraftInfo.LandingGear n = new LandingGear(
                        this,
                        this.toFloat(sxx[0]),
                        this.toFloat(sxx[1]),
                        this.toFloat(sxx[2]),
                        this.toFloat(sxx[3]),
                        this.toFloat(sxx[4]),
                        this.toFloat(sxx[5]),
                        "lg" + this.landingGear.size(),
                        maxRot,
                        item.equalsIgnoreCase("AddPartLgRev"),
                        item.equalsIgnoreCase("AddPartLGHatch")
                );
                if (sxx.length >= 8) {
                    n.enableRot2 = true;
                    n.maxRotFactor2 = sxx.length >= 11 ? this.toFloat(sxx[10], -180.0F, 180.0F) : 90.0F;
                    n.maxRotFactor2 /= 90.0F;
                    n.rot2 = new Vec3d(this.toFloat(sxx[7]), this.toFloat(sxx[8]), this.toFloat(sxx[9]));
                }

                this.landingGear.add(n);
            }

            if (item.equalsIgnoreCase("AddPartSlideRotLG") && sxx.length >= 9) {
                float maxRot = sxx.length >= 10 ? this.toFloat(sxx[9], -180.0F, 180.0F) : 90.0F;
                maxRot /= 90.0F;
                MCH_AircraftInfo.LandingGear n = new LandingGear(
                        this,
                        this.toFloat(sxx[3]),
                        this.toFloat(sxx[4]),
                        this.toFloat(sxx[5]),
                        this.toFloat(sxx[6]),
                        this.toFloat(sxx[7]),
                        this.toFloat(sxx[8]),
                        "lg" + this.landingGear.size(),
                        maxRot,
                        false,
                        false
                );
                n.slide = new Vec3d(this.toFloat(sxx[0]), this.toFloat(sxx[1]), this.toFloat(sxx[2]));
                this.landingGear.add(n);
            }
        } else if (item.equalsIgnoreCase("AddPartThrottle")) {
            String[] sxxx = data.split("\\s*,\\s*");
            if (sxxx.length >= 7) {
                float x = sxxx.length >= 8 ? this.toFloat(sxxx[7]) : 0.0F;
                float yx = sxxx.length >= 9 ? this.toFloat(sxxx[8]) : 0.0F;
                float z = sxxx.length >= 10 ? this.toFloat(sxxx[9]) : 0.0F;
                MCH_AircraftInfo.Throttle c = new Throttle(
                        this,
                        this.toFloat(sxxx[0]),
                        this.toFloat(sxxx[1]),
                        this.toFloat(sxxx[2]),
                        this.toFloat(sxxx[3]),
                        this.toFloat(sxxx[4]),
                        this.toFloat(sxxx[5]),
                        this.toFloat(sxxx[6]),
                        "throttle" + this.partThrottle.size(),
                        x,
                        yx,
                        z
                );
                this.partThrottle.add(c);
            }
        } else if (item.equalsIgnoreCase("AddPartRotation")) {
            String[] sxxx = data.split("\\s*,\\s*");
            if (sxxx.length >= 7) {
                boolean always = sxxx.length < 8 || this.toBool(sxxx[7]);
                MCH_AircraftInfo.RotPart c = new RotPart(
                        this,
                        this.toFloat(sxxx[0]),
                        this.toFloat(sxxx[1]),
                        this.toFloat(sxxx[2]),
                        this.toFloat(sxxx[3]),
                        this.toFloat(sxxx[4]),
                        this.toFloat(sxxx[5]),
                        this.toFloat(sxxx[6]),
                        always,
                        "rotpart" + this.partThrottle.size()
                );
                this.partRotPart.add(c);
            }
        } else if (item.compareTo("addpartcamera") == 0) {
            String[] sxxx = data.split("\\s*,\\s*");
            if (sxxx.length >= 3) {
                boolean ys = sxxx.length < 4 || this.toBool(sxxx[3]);
                boolean ps = sxxx.length >= 5 && this.toBool(sxxx[4]);
                MCH_AircraftInfo.Camera c = new Camera(
                        this, this.toFloat(sxxx[0]), this.toFloat(sxxx[1]), this.toFloat(sxxx[2]), 0.0F, -1.0F, 0.0F, "camera" + this.cameraList.size(), ys, ps
                );
                this.cameraList.add(c);
            }
        } else if (item.equalsIgnoreCase("AddPartWheel")) {
            String[] sxxx = this.splitParam(data);
            if (sxxx.length >= 3) {
                float rd = sxxx.length >= 4 ? this.toFloat(sxxx[3], -1800.0F, 1800.0F) : 0.0F;
                float rx = sxxx.length >= 7 ? this.toFloat(sxxx[4]) : 0.0F;
                float ry = sxxx.length >= 7 ? this.toFloat(sxxx[5]) : 1.0F;
                float rz = sxxx.length >= 7 ? this.toFloat(sxxx[6]) : 0.0F;
                float px = sxxx.length >= 10 ? this.toFloat(sxxx[7]) : this.toFloat(sxxx[0]);
                float py = sxxx.length >= 10 ? this.toFloat(sxxx[8]) : this.toFloat(sxxx[1]);
                float pz = sxxx.length >= 10 ? this.toFloat(sxxx[9]) : this.toFloat(sxxx[2]);
                this.partWheel
                        .add(
                                new PartWheel(
                                        this, this.toFloat(sxxx[0]), this.toFloat(sxxx[1]), this.toFloat(sxxx[2]), rx, ry, rz, rd, px, py, pz, "wheel" + this.partWheel.size()
                                )
                        );
            }
        } else if (item.equalsIgnoreCase("AddPartSteeringWheel")) {
            String[] sxxx = this.splitParam(data);
            if (sxxx.length >= 7) {
                this.partSteeringWheel
                        .add(
                                new PartWheel(
                                        this,
                                        this.toFloat(sxxx[0]),
                                        this.toFloat(sxxx[1]),
                                        this.toFloat(sxxx[2]),
                                        this.toFloat(sxxx[3]),
                                        this.toFloat(sxxx[4]),
                                        this.toFloat(sxxx[5]),
                                        this.toFloat(sxxx[6]),
                                        "steering_wheel" + this.partSteeringWheel.size()
                                )
                        );
            }
        } else if (item.equalsIgnoreCase("AddTrackRoller")) {
            String[] sxxx = this.splitParam(data);
            if (sxxx.length >= 3) {
                this.partTrackRoller
                        .add(
                                new TrackRoller(
                                        this, this.toFloat(sxxx[0]), this.toFloat(sxxx[1]), this.toFloat(sxxx[2]), "track_roller" + this.partTrackRoller.size()
                                )
                        );
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
            String[] sxxx = data.split("\\s*,\\s*");
            if (sxxx.length >= 5) {
                float df = sxxx.length >= 6 ? this.toFloat(sxxx[5]) : 1.0F;
                MCH_BoundingBox c = new MCH_BoundingBox(
                        this.toFloat(sxxx[0]), this.toFloat(sxxx[1]), this.toFloat(sxxx[2]), this.toFloat(sxxx[3]), this.toFloat(sxxx[4]), df
                );
                this.extraBoundingBox.add(c);
                if (c.getBoundingBox().maxY > this.markerHeight) {
                    this.markerHeight = (float) c.getBoundingBox().maxY;
                }

                this.markerWidth = (float) Math.max(this.markerWidth, Math.abs(c.getBoundingBox().maxX) / 2.0);
                this.markerWidth = (float) Math.max(this.markerWidth, Math.abs(c.getBoundingBox().minX) / 2.0);
                this.markerWidth = (float) Math.max(this.markerWidth, Math.abs(c.getBoundingBox().maxZ) / 2.0);
                this.markerWidth = (float) Math.max(this.markerWidth, Math.abs(c.getBoundingBox().minZ) / 2.0);
                this.bbZmin = (float) Math.min(this.bbZmin, c.getBoundingBox().minZ);
                this.bbZmax = (float) Math.min(this.bbZmax, c.getBoundingBox().maxZ);
            }
        } else if (item.equalsIgnoreCase("RotorSpeed")) {
            this.rotorSpeed = this.toFloat(data, -10000.0F, 10000.0F);
            if (this.rotorSpeed > 0.01) {
                this.rotorSpeed = (float) (this.rotorSpeed - 0.01);
            }

            if (this.rotorSpeed < -0.01) {
                this.rotorSpeed = (float) (this.rotorSpeed + 0.01);
            }
        } else if (item.equalsIgnoreCase("OnGroundPitchFactor")) {
            this.onGroundPitchFactor = this.toFloat(data, 0.0F, 180.0F);
        } else if (item.equalsIgnoreCase("OnGroundRollFactor")) {
            this.onGroundRollFactor = this.toFloat(data, 0.0F, 180.0F);
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

            for (int i = 0; i < PC; i++) {
                int idx = !REV ? i : PC - i - 1;
                String[] xy = this.splitParamSlash(s[3 + idx]);
                cx[i] = this.toFloat(xy[0]);
                cy[i] = this.toFloat(xy[1]);
            }

            List<MCH_AircraftInfo.CrawlerTrackPrm> lp = new ArrayList<>();
            lp.add(new CrawlerTrackPrm(this, (float) cx[0], (float) cy[0]));
            double dist = 0.0;

            for (int i = 0; i < PC; i++) {
                double x = cx[(i + 1) % PC] - cx[i];
                double y = cy[(i + 1) % PC] - cy[i];
                dist += Math.sqrt(x * x + y * y);
                double dist2 = dist;

                for (int j = 1; dist >= LEN; j++) {
                    lp.add(new CrawlerTrackPrm(this, (float) (cx[i] + x * (LEN * j / dist2)), (float) (cy[i] + y * (LEN * j / dist2))));
                    dist -= LEN;
                }
            }

            for (int i = 0; i < lp.size(); i++) {
                MCH_AircraftInfo.CrawlerTrackPrm pp = lp.get((i + lp.size() - 1) % lp.size());
                MCH_AircraftInfo.CrawlerTrackPrm cp = lp.get(i);
                MCH_AircraftInfo.CrawlerTrackPrm np = lp.get((i + 1) % lp.size());
                float pr = (float) (Math.atan2(pp.x - cp.x, pp.y - cp.y) * 180.0 / Math.PI);
                float nr = (float) (Math.atan2(np.x - cp.x, np.y - cp.y) * 180.0 / Math.PI);
                float ppr = (pr + 360.0F) % 360.0F;
                float nnr = nr + 180.0F;
                if ((nnr < ppr - 0.3 || nnr > ppr + 0.3) && nnr - ppr < 100.0F && nnr - ppr > -100.0F) {
                    nnr = (nnr + ppr) / 2.0F;
                }

                cp.r = nnr;
            }

            MCH_AircraftInfo.CrawlerTrack c = new CrawlerTrack(this, name);
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
        String s = this.textureNameList.get(this.textureCount);
        this.textureCount = (this.textureCount + 1) % this.textureNameList.size();
        return s;
    }

    public String getNextTextureName(String base) {
        if (this.textureNameList.size() >= 2) {
            for (int i = 0; i < this.textureNameList.size(); i++) {
                String s = this.textureNameList.get(i);
                if (s.equalsIgnoreCase(base)) {
                    i = (i + 1) % this.textureNameList.size();
                    return this.textureNameList.get(i);
                }
            }
        }

        return base;
    }

    @Override
    public boolean canReloadItem(String item) {
        String[] ignoreItems = getCannotReloadItem();

        for (String s : ignoreItems) {
            if (s.equalsIgnoreCase(item)) {
                return false;
            }
        }

        return true;
    }

    public static class Camera extends MCH_AircraftInfo.DrawnPart {
        public final boolean yawSync;
        public final boolean pitchSync;

        public Camera(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, String name, boolean ys, boolean ps) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
            this.yawSync = ys;
            this.pitchSync = ps;
        }
    }

    public static class CameraPosition {
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
            this(paramMCH_AircraftInfo, new Vec3d(0.0, 0.0, 0.0));
        }
    }

    public static class Canopy extends MCH_AircraftInfo.DrawnPart {
        public final float maxRotFactor;
        public final boolean isSlide;

        public Canopy(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name, boolean slide) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
            this.maxRotFactor = mr;
            this.isSlide = slide;
        }
    }

    public static class CrawlerTrack extends MCH_AircraftInfo.DrawnPart {
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

    public static class CrawlerTrackPrm {
        final float x;
        final float y;
        float nx;
        float ny;
        float r;

        public CrawlerTrackPrm(MCH_AircraftInfo paramMCH_AircraftInfo, float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class DrawnPart {
        public final Vec3d pos;
        public final Vec3d rot;
        public final String modelName;
        public _IModelCustom model;

        public DrawnPart(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, String name) {
            this.pos = new Vec3d(px, py, pz);
            this.rot = new Vec3d(rx, ry, rz);
            this.modelName = name;
            this.model = null;
        }
    }

    public static class Flare {
        public int[] types = new int[0];
        public Vec3d pos = new Vec3d(0.0, 0.0, 0.0);

        public Flare(MCH_AircraftInfo paramMCH_AircraftInfo) {
        }
    }

    public static class Hatch extends MCH_AircraftInfo.DrawnPart {
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

    public static class LandingGear extends MCH_AircraftInfo.DrawnPart {
        public final float maxRotFactor;
        public final boolean reverse;
        public final boolean hatch;
        public Vec3d slide = null;
        public boolean enableRot2;
        public Vec3d rot2;
        public float maxRotFactor2;

        public LandingGear(
                MCH_AircraftInfo paramMCH_AircraftInfo,
                float x,
                float y,
                float z,
                float rx,
                float ry,
                float rz,
                String model,
                float maxRotF,
                boolean rev,
                boolean isHatch
        ) {
            super(paramMCH_AircraftInfo, x, y, z, rx, ry, rz, model);
            this.maxRotFactor = maxRotF;
            this.enableRot2 = false;
            this.rot2 = new Vec3d(0.0, 0.0, 0.0);
            this.maxRotFactor2 = 0.0F;
            this.reverse = rev;
            this.hatch = isHatch;
        }
    }

    public static class PartWeapon extends MCH_AircraftInfo.DrawnPart {
        public final String[] name;
        public final boolean rotBarrel;
        public final boolean isMissile;
        public final boolean hideGM;
        public final boolean yaw;
        public final boolean pitch;
        public final float recoilBuf;
        public final boolean turret;
        public final List<MCH_AircraftInfo.PartWeaponChild> child;

        public PartWeapon(
                MCH_AircraftInfo paramMCH_AircraftInfo,
                String[] name,
                boolean rotBrl,
                boolean missile,
                boolean hgm,
                boolean y,
                boolean p,
                float px,
                float py,
                float pz,
                String modelName,
                float rx,
                float ry,
                float rz,
                float rb,
                boolean turret
        ) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, modelName);
            this.name = name;
            this.rotBarrel = rotBrl;
            this.isMissile = missile;
            this.hideGM = hgm;
            this.yaw = y;
            this.pitch = p;
            this.recoilBuf = rb;
            this.child = new ArrayList<>();
            this.turret = turret;
        }
    }

    public static class PartWeaponChild extends MCH_AircraftInfo.DrawnPart {
        public final String[] name;
        public final boolean yaw;
        public final boolean pitch;
        public final float recoilBuf;

        public PartWeaponChild(
                MCH_AircraftInfo paramMCH_AircraftInfo,
                String[] name,
                boolean y,
                boolean p,
                float px,
                float py,
                float pz,
                String modelName,
                float rx,
                float ry,
                float rz,
                float rb
        ) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, modelName);
            this.name = name;
            this.yaw = y;
            this.pitch = p;
            this.recoilBuf = rb;
        }
    }

    public static class PartWheel extends MCH_AircraftInfo.DrawnPart {
        final float rotDir;
        final Vec3d pos2;

        public PartWheel(
                MCH_AircraftInfo paramMCH_AircraftInfo,
                float px,
                float py,
                float pz,
                float rx,
                float ry,
                float rz,
                float rd,
                float px2,
                float py2,
                float pz2,
                String name
        ) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
            this.rotDir = rd;
            this.pos2 = new Vec3d(px2, py2, pz2);
        }

        public PartWheel(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float rd, String name) {
            this(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, rd, px, py, pz, name);
        }
    }

    public static class ParticleSplash {
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

    public static class RepellingHook {
        final Vec3d pos;
        final int interval;

        public RepellingHook(MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d pos, int inv) {
            this.pos = pos;
            this.interval = inv;
        }
    }

    public static class RideRack {
        public final String name;
        public final int rackID;

        public RideRack(MCH_AircraftInfo paramMCH_AircraftInfo, String n, int id) {
            this.name = n;
            this.rackID = id;
        }
    }

    public static class RotPart extends MCH_AircraftInfo.DrawnPart {
        public final float rotSpeed;
        public final boolean rotAlways;

        public RotPart(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, boolean a, String name) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
            this.rotSpeed = mr;
            this.rotAlways = a;
        }
    }

    public static class SearchLight {
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

        public SearchLight(
                MCH_AircraftInfo paramMCH_AircraftInfo, Vec3d pos, int cs, int ce, float h, float w, boolean fix, float y, float p, boolean st, float stRot
        ) {
            this.colorStart = cs;
            this.colorEnd = ce;
            this.pos = pos;
            this.height = h;
            this.width = w;
            this.angle = (float) (Math.atan2(w / 2.0F, h) * 180.0 / Math.PI);
            this.fixDir = fix;
            this.steering = st;
            this.yaw = y;
            this.pitch = p;
            this.stRot = stRot;
        }
    }

    public static class Throttle extends MCH_AircraftInfo.DrawnPart {
        public final Vec3d slide;
        public final float rot2;

        public Throttle(
                MCH_AircraftInfo paramMCH_AircraftInfo,
                float px,
                float py,
                float pz,
                float rx,
                float ry,
                float rz,
                float rot,
                String name,
                float px2,
                float py2,
                float pz2
        ) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
            this.rot2 = rot;
            this.slide = new Vec3d(px2, py2, pz2);
        }
    }

    public static class TrackRoller extends MCH_AircraftInfo.DrawnPart {
        final int side;

        public TrackRoller(MCH_AircraftInfo paramMCH_AircraftInfo, float px, float py, float pz, String name) {
            super(paramMCH_AircraftInfo, px, py, pz, 0.0F, 0.0F, 0.0F, name);
            this.side = px >= 0.0F ? 1 : 0;
        }
    }

    public static class Weapon {
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

        public Weapon(
                MCH_AircraftInfo paramMCH_AircraftInfo,
                float x,
                float y,
                float z,
                float yaw,
                float pitch,
                boolean canPirot,
                int seatId,
                float defy,
                float mny,
                float mxy,
                float mnp,
                float mxp,
                boolean turret
        ) {
            this.pos = new Vec3d(x, y, z);
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

    public static class WeaponBay extends MCH_AircraftInfo.DrawnPart {
        public final float maxRotFactor;
        public final boolean isSlide;
        private final String weaponName;
        public Integer[] weaponIds;

        public WeaponBay(
                MCH_AircraftInfo paramMCH_AircraftInfo, String wn, float px, float py, float pz, float rx, float ry, float rz, float mr, String name, boolean slide
        ) {
            super(paramMCH_AircraftInfo, px, py, pz, rx, ry, rz, name);
            this.maxRotFactor = mr;
            this.isSlide = slide;
            this.weaponName = wn;
            this.weaponIds = new Integer[0];
        }
    }

    public static class WeaponSet {
        public final String type;
        public final ArrayList<MCH_AircraftInfo.Weapon> weapons;

        public WeaponSet(MCH_AircraftInfo paramMCH_AircraftInfo, String t) {
            this.type = t;
            this.weapons = new ArrayList<>();
        }
    }

    public static class Wheel {
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
}
