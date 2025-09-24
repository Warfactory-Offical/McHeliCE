package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.helper.client._IModelCustom;
import com.norwood.mcheli.helper.info.IItemContent;
import com.norwood.mcheli.hud.MCH_Hud;
import com.norwood.mcheli.hud.MCH_HudManager;
import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public boolean isNewUAV;
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


    /**
     * 雷达种类
     */
    //public EnumRadarType radarType = EnumRadarType.EARLY_AA;

    /**
     * RWR种类
     */
    //public EnumRWRType rwrType = EnumRWRType.DIGITAL;

    /**
     * 当前载具在现代对空雷达中显示的名字
     */
    public String nameOnModernAARadar = "?";
    /**
     * 当前载具在早期对空雷达中显示的名字
     */
    public String nameOnEarlyAARadar = "?";
    /**
     * 当前载具在现代对地雷达中显示的名字
     */
    public String nameOnModernASRadar = "?";
    /**
     * 当前载具在早期对地雷达中显示的名字
     */
    public String nameOnEarlyASRadar = "?";
    /**
     * 载具被摧毁时爆炸范围
     */
    public float explosionSizeByCrash = 5;
    /**
     * 倒车速度倍率，默认1
     */
    public float throttleDownFactor = 1;

    /**
     * 箔条生效时长
     */
    public int chaffUseTime = 100;

    /**
     * 箔条冷却时长
     */
    public int chaffWaitTime = 400;

    /**
     * 维修系统生效时长 （时长即为回血百分比）
     */
    public int maintenanceUseTime = 20;

    /**
     * 维修系统冷却时长
     */
    public int maintenanceWaitTime = 300;

    /**
     * 载具瘫痪阈值，血量低于此百分比将关闭载具引擎
     */
    public int engineShutdownThreshold = 20;

    /**
     * APS生效时长
     */
    public int apsUseTime = 100;
    /**
     * APS冷却时长
     */
    public int apsWaitTime = 400;

    /**
     * APS范围
     */
    public int apsRange = 8;


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

    public void addTextureName(String name) {
        this.textureNameList.add(name);
    }

    public WeaponSet getOrCreateWeaponSet(String type) {
        if (type == null) {
            throw new IllegalArgumentException("type");
        }
        if (type.compareTo(this.lastWeaponType) != 0 || this.lastWeaponIndex < 0) {
            this.weaponSetList.add(new WeaponSet(this, type));
            this.lastWeaponIndex = this.weaponSetList.size() - 1;
            this.lastWeaponType = type;
        }
        return this.weaponSetList.get(this.lastWeaponIndex);
    }

    public PartWeapon getLastWeaponPart() {
        return this.lastWeaponPart;
    }

    public void setLastWeaponPart(PartWeapon part) {
        this.lastWeaponPart = part;
    }
    public int getWeaponCount() {
        return this.weaponSetList.size();
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
            this.pos = vec3.add(0, W_Entity.GLOBAL_Y_OFFSET, 0); //FIXME Seems like basically everthing needs a 0.35 offset when it comes to mcheli rendering, this is temporary so for when I figure out where it belongs
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
        public float len = W_Entity.GLOBAL_Y_OFFSET;
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
            this.pos = new Vec3d(x, y+W_Entity.GLOBAL_Y_OFFSET, z);
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
