package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.MCH_Color;
import com.norwood.mcheli.MCH_DamageFactor;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCH_WeaponInfo extends MCH_BaseInfo {
    public static Random rand = new Random();
    public final String name;
    public String explosionType;
    public int nukeYield;
    public int chemYield;
    public double chemSpeed;
    public int chemType;
    public boolean nukeEffectOnly;
    public String displayName;
    public String type;
    public int power;
    public float acceleration;
    public float accelerationInWater;
    public int explosion;
    public int explosionBlock;
    public int explosionInWater;
    public int explosionAltitude;
    public int delayFuse;
    public float bound;
    public int timeFuse;
    public boolean flaming;
    public MCH_SightType sight;
    public float[] zoom;
    public int delay;
    public int reloadTime;
    public int round;
    public int suppliedNum;
    public int maxAmmo;
    public final List<MCH_WeaponInfo.RoundItem> roundItems;
    public int soundDelay;
    public float soundVolume;
    public float soundPitch;
    public float soundPitchRandom;
    public final int soundPattern;
    public int lockTime;
    public boolean ridableOnly;
    public float proximityFuseDist;
    public int rigidityTime;
    public float accuracy;
    public int bomblet;
    public int bombletSTime;
    public float bombletDiff;
    public int modeNum;
    public int fixMode;
    public int piercing;
    public int heatCount;
    public int maxHeatCount;
    public boolean isFAE;
    public boolean isGuidedTorpedo;
    public float gravity;
    public float gravityInWater;
    public float velocityInWater;
    public boolean destruct;
    public String trajectoryParticleName;
    public int trajectoryParticleStartTick;
    public boolean disableSmoke;
    public MCH_Cartridge cartridge;
    public MCH_Color color;
    public MCH_Color colorInWater;
    public String soundFileName;
    public float smokeSize;
    public int smokeNum;
    public int smokeMaxAge;
    public Item dispenseItem;
    public int dispenseDamege;
    public int dispenseRange;
    public int recoilBufCount;
    public int recoilBufCountSpeed;
    public float length;
    public float radius;
    public float angle;
    public boolean displayMortarDistance;
    public boolean fixCameraPitch;
    public float cameraRotationSpeedPitch;
    public int target;
    public int markTime;
    public float recoil;
    public String bulletModelName;
    public MCH_BulletModel bulletModel;
    public String bombletModelName;
    public MCH_BulletModel bombletModel;
    public MCH_DamageFactor damageFactor;
    public String group;
    public List<MCH_WeaponInfo.MuzzleFlash> listMuzzleFlash;
    public List<MCH_WeaponInfo.MuzzleFlash> listMuzzleFlashSmoke;

    /**
     * 生成的方块破碎粒子数量
     */
    public int flakParticlesCrack = 10;
    /**
     * 生成的白色烟雾粒子数量
     */
    public int numParticlesFlak = 3;
    /**
     * 生成的方块破碎粒子扩散，推荐值0.1(步枪子弹) ~ 0.6(反坦克步枪)
     */
    public float flakParticlesDiff = 0.3F;
    public String hitSound;
    public String hitSoundIron = "hit_metal";
    public float hitSoundRange;
    public boolean hitSoundEnable = false;
    public boolean entityHitSoundEnable = false;
    /**
     * 是否为红外弹，会受到热焰弹干扰
     */
    public boolean isHeatSeekerMissile = true;
    /**
     * 是否为雷达弹，会受到箔条干扰
     */
    public boolean isRadarMissile = false;
    //弹药导引头最大导引角度
    public int maxDegreeOfMissile = 60;
    //脱锁延时，-1为永远锁定
    public int tickEndHoming = -1;
    /**
     * 最大锁定距离
     */
    public int maxLockOnRange = 300;
    /**
     * 机载雷达最大锁定角度
     */
    public int maxLockOnAngle = 10;
    /**
     * 速度门雷达最大角度，超过此角度将脱锁 (也可用于红外弹尾后攻击)
     */
    public float pdHDNMaxDegree = 1000f;
    /**
     * 速度门雷达脱锁间隔，超过最大角度后，在该tick后导弹脱锁
     */
    public int pdHDNMaxDegreeLockOutCount = 10;
    /**
     * 导弹抗干扰时长，-1为不抗干扰
     */
    public int antiFlareCount = -1;
    /**
     * 雷达弹多径杂波检测高度，飞机低于这个高度将使雷达弹脱锁
     */
    public int lockMinHeight = 25;
    /**
     * 半主动雷达弹需要持续引导
     */
    public boolean passiveRadar = false;

    /**
     * 半主动雷达弹脱离引导后脱锁计时
     */
    public int passiveRadarLockOutCount = 20;

    /**
     * 对TV弹启用激光制导
     */
    public boolean laserGuidance = false;

    /**
     * 是否有激光吊舱
     */
    public boolean hasLaserGuidancePod = true;

    /**
     * 允许离轴射击 AA弹
     */
    public boolean enableOffAxis = true;

    /**
     * 导弹机动参数，越小越平滑，值设为1时为原版导弹机动，推荐值为0.1
     */
    public double turningFactor = 0.5;

    /**
     * 启用区块加载器(试验功能)
     */
    public boolean enableChunkLoader = false;

    /**
     * 主动雷达弹 BVR 发射后自动追踪目标
     */
    public boolean activeRadar = false;

    /**
     * 主动雷达弹 扫描间隔
     */
    public int scanInterval = 20;

    /**
     * 武器切换冷却
     */
    public int weaponSwitchCount = 0;

    /**
     * 武器切换音效
     */
    public String weaponSwitchSound = "";

    /**
     * 武器垂直后坐力
     */
    public float recoilPitch = 0.0F;
    /**
     * 武器水平后坐力（固定方向）
     */
    public float recoilYaw = 0.0F;
    /**
     * 武器随机垂直后坐力 (Recoil 2 + rndRecoil 0.5 == 1.5-2.5 Recoil range)
     */
    public float recoilPitchRange = 0.0F;
    /**
     * 武器随机水平后坐力
     */
    public float recoilYawRange = 0.0F;
    /**
     * 武器后坐力恢复速度
     */
    public float recoilRecoverFactor = 0.8F;

    /**
     * 每tick速度增加数值，小于0减速，大于0加速
     */
    public float speedFactor = 0F;
    /**
     * 每tick的速度乘数生效时长
     */
    public int speedFactorStartTick = 0;
    /**
     * 每tick的速度乘数结束时长
     */
    public int speedFactorEndTick = 0;
    /**
     * 速度是否跟随载机，最终速度 = 载机速度 + 子弹速度
     */
    public boolean speedDependsAircraft = false;
    /**
     * 是否可以锁定导弹实体
     */
    public boolean canLockMissile = false;
    /**
     * 允许超视距索敌
     */
    public boolean enableBVR = false;
    /**
     * 超视距索敌功能最小启用距离
     */
    public int minRangeBVR = 300;

    public MCH_WeaponInfo(AddonResourceLocation location, String path) {
        super(location, path);
        this.name = location.getPath();
        this.displayName = this.name;
        this.type = "";
        this.power = 0;
        this.acceleration = 1.0F;
        this.accelerationInWater = 1.0F;
        this.explosion = 0;
        this.explosionBlock = -1;
        this.explosionInWater = 0;
        this.explosionAltitude = 0;
        this.delayFuse = 0;
        this.timeFuse = 0;
        this.flaming = false;
        this.sight = MCH_SightType.NONE;
        this.zoom = new float[]{1.0F};
        this.delay = 10;
        this.reloadTime = 30;
        this.round = 0;
        this.suppliedNum = 1;
        this.roundItems = new ArrayList<>();
        this.maxAmmo = 0;
        this.soundDelay = 0;
        this.soundPattern = 0;
        this.soundVolume = 1.0F;
        this.soundPitch = 1.0F;
        this.soundPitchRandom = 0.1F;
        this.lockTime = 30;
        this.ridableOnly = false;
        this.proximityFuseDist = 0.0F;
        this.rigidityTime = 7;
        this.accuracy = 0.0F;
        this.bomblet = 0;
        this.bombletSTime = 10;
        this.bombletDiff = 0.3F;
        this.modeNum = 0;
        this.fixMode = 0;
        this.piercing = 0;
        this.heatCount = 0;
        this.maxHeatCount = 0;
        this.bulletModelName = "";
        this.bombletModelName = "";
        this.bulletModel = null;
        this.bombletModel = null;
        this.isFAE = false;
        this.isGuidedTorpedo = false;
        this.gravity = 0.0F;
        this.gravityInWater = 0.0F;
        this.velocityInWater = 0.999F;
        this.destruct = false;
        this.trajectoryParticleName = "explode";
        this.trajectoryParticleStartTick = 0;
        this.cartridge = null;
        this.disableSmoke = false;
        this.color = new MCH_Color();
        this.colorInWater = new MCH_Color();
        this.soundFileName = this.name + "_snd";
        this.smokeMaxAge = 100;
        this.smokeNum = 1;
        this.smokeSize = 2.0F;
        this.dispenseItem = null;
        this.dispenseDamege = 0;
        this.dispenseRange = 1;
        this.recoilBufCount = 2;
        this.recoilBufCountSpeed = 3;
        this.length = 0.0F;
        this.radius = 0.0F;
        this.target = 1;
        this.recoil = 0.0F;
        this.damageFactor = null;
        this.group = "";
        this.listMuzzleFlash = null;
        this.listMuzzleFlashSmoke = null;
        this.displayMortarDistance = false;
        this.fixCameraPitch = false;
        this.cameraRotationSpeedPitch = 1.0F;
        this.nukeYield = 0;
        this.chemYield = 0;
        this.chemSpeed = 1.25;
        this.chemType = 0;
    }

    public void checkData() {
        if (this.explosionBlock < 0) {
            this.explosionBlock = this.explosion;
        }

        if (this.fixMode >= this.modeNum) {
            this.fixMode = 0;
        }

        if (this.round <= 0) {
            this.round = this.maxAmmo;
        }

        if (this.round > this.maxAmmo) {
            this.round = this.maxAmmo;
        }

        if (this.explosion <= 0) {
            this.isFAE = false;
        }

        if (this.delayFuse <= 0) {
            this.bound = 0.0F;
        }

        if (this.isFAE) {
            this.explosionInWater = 0;
        }

        if (this.bomblet > 0 && this.bombletSTime < 1) {
            this.bombletSTime = 1;
        }

        if (this.destruct) {
            this.delay = 1000000;
        }

        this.angle = (float) (Math.atan2(this.radius, this.length) * 180.0 / Math.PI);
    }


    public float getDamageFactor(Entity e) {
        return this.damageFactor != null ? this.damageFactor.getDamageFactor(e) : 1.0F;
    }

    public String getWeaponTypeName() {
        if (this.type.equalsIgnoreCase("MachineGun1")) {
            return "MachineGun";
        } else if (this.type.equalsIgnoreCase("MachineGun2")) {
            return "MachineGun";
        } else if (this.type.equalsIgnoreCase("Torpedo")) {
            return "Torpedo";
        } else if (this.type.equalsIgnoreCase("CAS")) {
            return "CAS";
        } else if (this.type.equalsIgnoreCase("Rocket")) {
            return "Rocket";
        } else if (this.type.equalsIgnoreCase("ASMissile")) {
            return "AS Missile";
        } else if (this.type.equalsIgnoreCase("AAMissile")) {
            return "AA Missile";
        } else if (this.type.equalsIgnoreCase("TVMissile")) {
            return "TV Missile";
        } else if (this.type.equalsIgnoreCase("ATMissile")) {
            return "AT Missile";
        } else if (this.type.equalsIgnoreCase("Bomb")) {
            return "Bomb";
        } else if (this.type.equalsIgnoreCase("MkRocket")) {
            return "Mk Rocket";
        } else if (this.type.equalsIgnoreCase("Dummy")) {
            return "Dummy";
        } else if (this.type.equalsIgnoreCase("Smoke")) {
            return "Smoke";
        } else if (this.type.equalsIgnoreCase("Dispenser")) {
            return "Dispenser";
        } else {
            return this.type.equalsIgnoreCase("TargetingPod") ? "Targeting Pod" : "";
        }
    }


    @Override
    public void onPostReload() {
        MCH_WeaponInfoManager.setRoundItems();
    }


    public static class MuzzleFlash {
        public final float dist;
        public final float size;
        public final float range;
        public final int age;
        public final float a;
        public final float r;
        public final float g;
        public final float b;
        public final int num;

        public MuzzleFlash(float dist, float size, float range, int age, float a, float r, float g, float b, int num) {
            this.dist = dist;
            this.size = size;
            this.range = range;
            this.age = age;
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
            this.num = num;
        }
    }

    public static class RoundItem {
        public final int num;
        public final ResourceLocation itemName;
        public final int damage;
        public ItemStack itemStack = ItemStack.EMPTY;

        public RoundItem(int n, String name, int damage) {
            this.num = n;
            this.itemName = new ResourceLocation(name);
            this.damage = damage;
        }
    }
}
