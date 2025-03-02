package com.norwood.mcheli.mob;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_SeatInfo;
import com.norwood.mcheli.vehicle.MCH_EntityVehicle;
import com.norwood.mcheli.weapon.MCH_WeaponBase;
import com.norwood.mcheli.weapon.MCH_WeaponEntitySeeker;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import com.norwood.mcheli.weapon.MCH_WeaponSet;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class MCH_EntityGunner extends EntityLivingBase {
   private static final DataParameter<String> TEAM_NAME;
   public boolean isCreative;
   public String ownerUUID;
   public int targetType;
   public int despawnCount;
   public int switchTargetCount;
   public Entity targetEntity;
   public double targetPrevPosX;
   public double targetPrevPosY;
   public double targetPrevPosZ;
   public boolean waitCooldown;
   public int idleCount;
   public int idleRotation;

   public MCH_EntityGunner(World world) {
      super(world);
      this.isCreative = false;
      this.ownerUUID = "";
      this.targetType = 0;
      this.despawnCount = 0;
      this.switchTargetCount = 0;
      this.targetEntity = null;
      this.targetPrevPosX = 0.0D;
      this.targetPrevPosY = 0.0D;
      this.targetPrevPosZ = 0.0D;
      this.waitCooldown = false;
      this.idleCount = 0;
      this.idleRotation = 0;
   }

   public MCH_EntityGunner(World world, double x, double y, double z) {
      this(world);
      this.func_70107_b(x, y, z);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(TEAM_NAME, "");
   }

   public String getTeamName() {
      return (String)this.dataManager.func_187225_a(TEAM_NAME);
   }

   public void setTeamName(String name) {
      this.dataManager.func_187227_b(TEAM_NAME, name);
   }

   public Team getTeam() {
      return this.world.func_96441_U().func_96508_e(this.getTeamName());
   }

   public boolean func_184191_r(Entity entityIn) {
      return super.func_184191_r(entityIn);
   }

   public ITextComponent func_145748_c_() {
      Team team = this.getTeam();
      if (team != null) {
         String name = MCH_MOD.isTodaySep01() ? "'s EMB4" : " Gunner";
         return new TextComponentString(ScorePlayerTeam.func_96667_a(team, team.func_96661_b() + name));
      } else {
         return new TextComponentString("");
      }
   }

   public boolean func_180431_b(DamageSource source) {
      return this.isCreative;
   }

   public void func_70645_a(DamageSource source) {
      super.func_70645_a(source);
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      if (this.world.isRemote) {
         return false;
      } else if (this.func_184187_bx() == null) {
         return false;
      } else if (player.field_71075_bZ.field_75098_d) {
         this.removeFromAircraft(player);
         return true;
      } else if (this.isCreative) {
         player.func_145747_a(new TextComponentString("Creative mode only."));
         return false;
      } else if (this.getTeam() != null && !this.func_184191_r(player)) {
         player.func_145747_a(new TextComponentString("You are other team."));
         return false;
      } else {
         this.removeFromAircraft(player);
         return true;
      }
   }

   public void removeFromAircraft(EntityPlayer player) {
      if (!this.world.isRemote) {
         W_WorldFunc.MOD_playSoundAtEntity(player, "wrench", 1.0F, 1.0F);
         this.func_70106_y();
         MCH_EntityAircraft ac = null;
         if (this.func_184187_bx() instanceof MCH_EntityAircraft) {
            ac = (MCH_EntityAircraft)this.func_184187_bx();
         } else if (this.func_184187_bx() instanceof MCH_EntitySeat) {
            ac = ((MCH_EntitySeat)this.func_184187_bx()).getParent();
         }

         String name = "";
         if (ac != null && ac.getAcInfo() != null) {
            name = " on " + ac.getAcInfo().displayName + " seat " + (ac.getSeatIdByEntity(this) + 1);
         }

         String playerName = ScorePlayerTeam.func_96667_a(player.getTeam(), player.func_145748_c_().func_150254_d());
         if (MCH_MOD.isTodaySep01()) {
            player.func_145747_a(new TextComponentTranslation("chat.type.text", new Object[]{"EMB4", new TextComponentString("Bye " + playerName + "! Good vehicle" + name)}));
         } else {
            player.func_145747_a(new TextComponentString("Remove gunner" + name + " by " + playerName + "."));
         }

         this.func_184210_p();
      }

   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.world.isRemote && !this.field_70128_L) {
         if (this.func_184187_bx() != null && this.func_184187_bx().field_70128_L) {
            this.func_184210_p();
         }

         if (this.func_184187_bx() instanceof MCH_EntityAircraft) {
            this.shotTarget((MCH_EntityAircraft)this.func_184187_bx());
         } else if (this.func_184187_bx() instanceof MCH_EntitySeat && ((MCH_EntitySeat)this.func_184187_bx()).getParent() != null) {
            this.shotTarget(((MCH_EntitySeat)this.func_184187_bx()).getParent());
         } else if (this.despawnCount < 20) {
            ++this.despawnCount;
         } else if (this.func_184187_bx() == null || this.field_70173_aa > 100) {
            this.func_70106_y();
         }

         if (this.targetEntity == null) {
            if (this.idleCount == 0) {
               this.idleCount = (3 + this.field_70146_Z.nextInt(5)) * 20;
               this.idleRotation = this.field_70146_Z.nextInt(5) - 2;
            }

            this.field_70177_z += (float)this.idleRotation / 2.0F;
         } else {
            this.idleCount = 60;
         }
      }

      if (this.switchTargetCount > 0) {
         --this.switchTargetCount;
      }

      if (this.idleCount > 0) {
         --this.idleCount;
      }

   }

   public boolean canAttackEntity(EntityLivingBase entity, MCH_EntityAircraft ac, MCH_WeaponSet ws) {
      boolean ret = false;
      if (this.targetType == 0) {
         ret = entity != this && !(entity instanceof EntityEnderman) && !entity.field_70128_L && !this.func_184191_r(entity) && entity.func_110143_aJ() > 0.0F && !ac.isMountedEntity(entity);
      } else {
         ret = entity != this && !((EntityPlayer)entity).field_71075_bZ.field_75098_d && !entity.field_70128_L && !this.getTeamName().isEmpty() && !this.func_184191_r(entity) && entity.func_110143_aJ() > 0.0F && !ac.isMountedEntity(entity);
      }

      if (ret && ws.getCurrentWeapon().getGuidanceSystem() != null) {
         ret = ws.getCurrentWeapon().getGuidanceSystem().canLockEntity(entity);
      }

      return ret;
   }

   public void shotTarget(MCH_EntityAircraft ac) {
      if (!ac.isDestroyed()) {
         if (ac.getGunnerStatus()) {
            MCH_WeaponSet ws = ac.getCurrentWeapon(this);
            if (ws != null && ws.getInfo() != null && ws.getCurrentWeapon() != null) {
               MCH_WeaponBase cw = ws.getCurrentWeapon();
               if (this.targetEntity != null && (this.targetEntity.field_70128_L || ((EntityLivingBase)this.targetEntity).func_110143_aJ() <= 0.0F) && this.switchTargetCount > 20) {
                  this.switchTargetCount = 20;
               }

               Vec3d pos = this.getGunnerWeaponPos(ac, ws);
               if (this.targetEntity == null && this.switchTargetCount <= 0 || this.switchTargetCount <= 0) {
                  this.switchTargetCount = 20;
                  EntityLivingBase nextTarget = null;
                  List list;
                  int i;
                  int rv;
                  if (this.targetType == 0) {
                     i = MCH_Config.RangeOfGunner_VsMonster_Horizontal.prmInt;
                     rv = MCH_Config.RangeOfGunner_VsMonster_Vertical.prmInt;
                     list = this.world.func_175647_a(EntityLivingBase.class, this.func_174813_aQ().func_72314_b((double)i, (double)rv, (double)i), IMob.field_82192_a);
                  } else {
                     i = MCH_Config.RangeOfGunner_VsPlayer_Horizontal.prmInt;
                     rv = MCH_Config.RangeOfGunner_VsPlayer_Vertical.prmInt;
                     list = this.world.func_72872_a(EntityPlayer.class, this.func_174813_aQ().func_72314_b((double)i, (double)rv, (double)i));
                  }

                  for(i = 0; i < list.size(); ++i) {
                     EntityLivingBase entity = (EntityLivingBase)list.get(i);
                     if (this.canAttackEntity(entity, ac, ws) && this.checkPitch(entity, ac, pos) && (nextTarget == null || this.func_70032_d(entity) < this.func_70032_d(nextTarget)) && this.func_70685_l(entity) && this.isInAttackable(entity, ac, ws, pos)) {
                        nextTarget = entity;
                        this.switchTargetCount = 60;
                     }
                  }

                  if (nextTarget != null && this.targetEntity != nextTarget) {
                     this.targetPrevPosX = nextTarget.posX;
                     this.targetPrevPosY = nextTarget.posY;
                     this.targetPrevPosZ = nextTarget.posZ;
                  }

                  this.targetEntity = nextTarget;
               }

               if (this.targetEntity != null) {
                  float rotSpeed = 10.0F;
                  if (ac.isPilot(this)) {
                     rotSpeed = ac.getAcInfo().cameraRotationSpeed / 10.0F;
                  }

                  this.field_70125_A = MathHelper.func_76142_g(this.field_70125_A);
                  this.field_70177_z = MathHelper.func_76142_g(this.field_70177_z);
                  double dist = (double)this.func_70032_d(this.targetEntity);
                  double tick = 1.0D;
                  if (dist >= 10.0D && ws.getInfo().acceleration > 1.0F) {
                     tick = dist / (double)ws.getInfo().acceleration;
                  }

                  if (this.targetEntity.func_184187_bx() instanceof MCH_EntitySeat || this.targetEntity.func_184187_bx() instanceof MCH_EntityAircraft) {
                     tick -= (double)MCH_Config.HitBoxDelayTick.prmInt;
                  }

                  double dx = (this.targetEntity.posX - this.targetPrevPosX) * tick;
                  double dy = (this.targetEntity.posY - this.targetPrevPosY) * tick + (double)this.targetEntity.field_70131_O * this.field_70146_Z.nextDouble();
                  double dz = (this.targetEntity.posZ - this.targetPrevPosZ) * tick;
                  double d0 = this.targetEntity.posX + dx - pos.x;
                  double d1 = this.targetEntity.posY + dy - pos.y;
                  double d2 = this.targetEntity.posZ + dz - pos.z;
                  double d3 = (double)MathHelper.func_76133_a(d0 * d0 + d2 * d2);
                  float yaw = MathHelper.func_76142_g((float)(Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F);
                  float pitch = (float)(-(Math.atan2(d1, d3) * 180.0D / 3.141592653589793D));
                  if (Math.abs(this.field_70125_A - pitch) < rotSpeed && Math.abs(this.field_70177_z - yaw) < rotSpeed) {
                     float r = ac.isPilot(this) ? 0.1F : 0.5F;
                     this.field_70125_A = pitch + (this.field_70146_Z.nextFloat() - 0.5F) * r - cw.fixRotationPitch;
                     this.field_70177_z = yaw + (this.field_70146_Z.nextFloat() - 0.5F) * r;
                     if (!this.waitCooldown || ws.currentHeat <= 0 || ws.getInfo().maxHeatCount <= 0) {
                        this.waitCooldown = false;
                        MCH_WeaponParam prm = new MCH_WeaponParam();
                        prm.setPosition(ac.posX, ac.posY, ac.posZ);
                        prm.user = this;
                        prm.entity = ac;
                        prm.option1 = cw instanceof MCH_WeaponEntitySeeker ? this.targetEntity.getEntityId() : 0;
                        if (ac.useCurrentWeapon(prm) && ws.getInfo().maxHeatCount > 0 && ws.currentHeat > ws.getInfo().maxHeatCount * 4 / 5) {
                           this.waitCooldown = true;
                        }
                     }
                  }

                  if (Math.abs(pitch - this.field_70125_A) >= rotSpeed) {
                     this.field_70125_A += pitch > this.field_70125_A ? rotSpeed : -rotSpeed;
                  }

                  if (Math.abs(yaw - this.field_70177_z) >= rotSpeed) {
                     if (Math.abs(yaw - this.field_70177_z) <= 180.0F) {
                        this.field_70177_z += yaw > this.field_70177_z ? rotSpeed : -rotSpeed;
                     } else {
                        this.field_70177_z += yaw > this.field_70177_z ? -rotSpeed : rotSpeed;
                     }
                  }

                  this.field_70759_as = this.field_70177_z;
                  this.targetPrevPosX = this.targetEntity.posX;
                  this.targetPrevPosY = this.targetEntity.posY;
                  this.targetPrevPosZ = this.targetEntity.posZ;
               } else {
                  this.field_70125_A *= 0.95F;
               }

            }
         }
      }
   }

   private boolean checkPitch(EntityLivingBase entity, MCH_EntityAircraft ac, Vec3d pos) {
      try {
         double d0 = entity.posX - pos.x;
         double d1 = entity.posY - pos.y;
         double d2 = entity.posZ - pos.z;
         double d3 = (double)MathHelper.func_76133_a(d0 * d0 + d2 * d2);
         float pitch = (float)(-(Math.atan2(d1, d3) * 180.0D / 3.141592653589793D));
         MCH_AircraftInfo ai = ac.getAcInfo();
         if (ac instanceof MCH_EntityVehicle && ac.isPilot(this) && Math.abs(ai.minRotationPitch) + Math.abs(ai.maxRotationPitch) > 0.0F) {
            if (pitch < ai.minRotationPitch) {
               return false;
            }

            if (pitch > ai.maxRotationPitch) {
               return false;
            }
         }

         MCH_WeaponBase cw = ac.getCurrentWeapon(this).getCurrentWeapon();
         if (!(cw instanceof MCH_WeaponEntitySeeker)) {
            MCH_AircraftInfo.Weapon wi = ai.getWeaponById(ac.getCurrentWeaponID(this));
            if (Math.abs(wi.minPitch) + Math.abs(wi.maxPitch) > 0.0F) {
               if (pitch < wi.minPitch) {
                  return false;
               }

               if (pitch > wi.maxPitch) {
                  return false;
               }
            }
         }
      } catch (Exception var16) {
      }

      return true;
   }

   public Vec3d getGunnerWeaponPos(MCH_EntityAircraft ac, MCH_WeaponSet ws) {
      MCH_SeatInfo seatInfo = ac.getSeatInfo(this);
      return (seatInfo == null || !seatInfo.rotSeat) && !(ac instanceof MCH_EntityVehicle) ? ac.getTransformedPosition(ws.getCurrentWeapon().position) : ac.calcOnTurretPos(ws.getCurrentWeapon().position).func_72441_c(ac.posX, ac.posY, ac.posZ);
   }

   private boolean isInAttackable(EntityLivingBase entity, MCH_EntityAircraft ac, MCH_WeaponSet ws, Vec3d pos) {
      if (ac instanceof MCH_EntityVehicle) {
         return true;
      } else {
         try {
            if (ac.getCurrentWeapon(this).getCurrentWeapon() instanceof MCH_WeaponEntitySeeker) {
               return true;
            } else {
               MCH_AircraftInfo.Weapon wi = ac.getAcInfo().getWeaponById(ac.getCurrentWeaponID(this));
               Vec3d v1 = new Vec3d(0.0D, 0.0D, 1.0D);
               float yaw = -ac.getRotYaw() + (wi.maxYaw + wi.minYaw) / 2.0F - wi.defaultYaw;
               v1 = v1.func_178785_b(yaw * 3.1415927F / 180.0F);
               Vec3d v2 = (new Vec3d(entity.posX - pos.x, 0.0D, entity.posZ - pos.z)).normalize();
               double dot = v1.func_72430_b(v2);
               double rad = Math.acos(dot);
               double deg = rad * 180.0D / 3.141592653589793D;
               return deg < (double)(Math.abs(wi.maxYaw - wi.minYaw) / 2.0F);
            }
         } catch (Exception var15) {
            return false;
         }
      }
   }

   @Nullable
   public MCH_EntityAircraft getAc() {
      if (this.func_184187_bx() == null) {
         return null;
      } else {
         return this.func_184187_bx() instanceof MCH_EntityAircraft ? (MCH_EntityAircraft)this.func_184187_bx() : (this.func_184187_bx() instanceof MCH_EntitySeat ? ((MCH_EntitySeat)this.func_184187_bx()).getParent() : null);
      }
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.setBoolean("Creative", this.isCreative);
      nbt.setString("OwnerUUID", this.ownerUUID);
      nbt.setString("TeamName", this.getTeamName());
      nbt.setInteger("TargetType", this.targetType);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.isCreative = nbt.getBoolean("Creative");
      this.ownerUUID = nbt.func_74779_i("OwnerUUID");
      this.setTeamName(nbt.func_74779_i("TeamName"));
      this.targetType = nbt.func_74762_e("TargetType");
   }

   @Nullable
   public Entity changeDimension(int dimensionIn, ITeleporter teleporter) {
      return null;
   }

   public void func_70106_y() {
      if (!this.world.isRemote && !this.field_70128_L && !this.isCreative) {
         if (this.targetType == 0) {
            this.func_145779_a(MCH_MOD.itemSpawnGunnerVsMonster, 1);
         } else {
            this.func_145779_a(MCH_MOD.itemSpawnGunnerVsPlayer, 1);
         }
      }

      super.func_70106_y();
      MCH_Lib.DbgLog(this.world, "MCH_EntityGunner.setDead type=%d :" + this.toString(), this.targetType);
   }

   public boolean func_70097_a(DamageSource ds, float amount) {
      if (ds == DamageSource.field_76380_i) {
         this.func_70106_y();
      }

      return super.func_70097_a(ds, amount);
   }

   public ItemStack func_184582_a(EntityEquipmentSlot slotIn) {
      return ItemStack.field_190927_a;
   }

   public void func_184201_a(EntityEquipmentSlot slotIn, ItemStack stack) {
   }

   public Iterable<ItemStack> func_184193_aE() {
      return Collections.emptyList();
   }

   public EnumHandSide func_184591_cq() {
      return EnumHandSide.RIGHT;
   }

   static {
      TEAM_NAME = EntityDataManager.createKey(MCH_EntityGunner.class, DataSerializers.STRING);
   }
}
