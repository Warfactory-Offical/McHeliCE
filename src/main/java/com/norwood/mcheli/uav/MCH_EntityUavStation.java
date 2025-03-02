package com.norwood.mcheli.uav;

import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Explosion;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.entity.IEntityItemStackPickable;
import com.norwood.mcheli.__helper.entity.IEntitySinglePassenger;
import com.norwood.mcheli.__helper.network.PooledGuiParameter;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.helicopter.MCH_HeliInfo;
import com.norwood.mcheli.helicopter.MCH_HeliInfoManager;
import com.norwood.mcheli.helicopter.MCH_ItemHeli;
import com.norwood.mcheli.multiplay.MCH_Multiplay;
import com.norwood.mcheli.plane.MCP_ItemPlane;
import com.norwood.mcheli.plane.MCP_PlaneInfo;
import com.norwood.mcheli.plane.MCP_PlaneInfoManager;
import com.norwood.mcheli.tank.MCH_ItemTank;
import com.norwood.mcheli.tank.MCH_TankInfo;
import com.norwood.mcheli.tank.MCH_TankInfoManager;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_EntityContainer;
import com.norwood.mcheli.wrapper.W_EntityPlayer;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityUavStation extends W_EntityContainer implements IEntitySinglePassenger, IEntityItemStackPickable {
   public static final float Y_OFFSET = 0.35F;
   private static final DataParameter<Byte> STATUS;
   private static final DataParameter<Integer> LAST_AC_ID;
   private static final DataParameter<BlockPos> UAV_POS;
   protected Entity lastRiddenByEntity;
   public boolean isRequestedSyncStatus;
   @SideOnly(Side.CLIENT)
   protected double velocityX;
   @SideOnly(Side.CLIENT)
   protected double velocityY;
   @SideOnly(Side.CLIENT)
   protected double velocityZ;
   protected int aircraftPosRotInc;
   protected double aircraftX;
   protected double aircraftY;
   protected double aircraftZ;
   protected double aircraftYaw;
   protected double aircraftPitch;
   private MCH_EntityAircraft controlAircraft;
   private MCH_EntityAircraft lastControlAircraft;
   private String loadedLastControlAircraftGuid;
   public int posUavX;
   public int posUavY;
   public int posUavZ;
   public float rotCover;
   public float prevRotCover;

   public MCH_EntityUavStation(World world) {
      super(world);
      this.dropContentsWhenDead = false;
      this.field_70156_m = true;
      this.func_70105_a(2.0F, 0.7F);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.noClip = true;
      this.lastRiddenByEntity = null;
      this.aircraftPosRotInc = 0;
      this.aircraftX = 0.0D;
      this.aircraftY = 0.0D;
      this.aircraftZ = 0.0D;
      this.aircraftYaw = 0.0D;
      this.aircraftPitch = 0.0D;
      this.posUavX = 0;
      this.posUavY = 0;
      this.posUavZ = 0;
      this.rotCover = 0.0F;
      this.prevRotCover = 0.0F;
      this.setControlAircract((MCH_EntityAircraft)null);
      this.setLastControlAircraft((MCH_EntityAircraft)null);
      this.loadedLastControlAircraftGuid = "";
   }

   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(STATUS, (byte)0);
      this.dataManager.register(LAST_AC_ID, 0);
      this.dataManager.register(UAV_POS, BlockPos.field_177992_a);
      this.setOpen(true);
   }

   public int getStatus() {
      return (Byte)this.dataManager.func_187225_a(STATUS);
   }

   public void setStatus(int n) {
      if (!this.world.isRemote) {
         MCH_Lib.DbgLog(this.world, "MCH_EntityUavStation.setStatus(%d)", n);
         this.dataManager.func_187227_b(STATUS, (byte)n);
      }

   }

   public int getKind() {
      return 127 & this.getStatus();
   }

   public void setKind(int n) {
      this.setStatus(this.getStatus() & 128 | n);
   }

   public boolean isOpen() {
      return (this.getStatus() & 128) != 0;
   }

   public void setOpen(boolean b) {
      this.setStatus((b ? 128 : 0) | this.getStatus() & 127);
   }

   @Nullable
   public MCH_EntityAircraft getControlAircract() {
      return this.controlAircraft;
   }

   public void setControlAircract(@Nullable MCH_EntityAircraft ac) {
      this.controlAircraft = ac;
      if (ac != null && !ac.field_70128_L) {
         this.setLastControlAircraft(ac);
      }

   }

   public void setUavPosition(int x, int y, int z) {
      if (!this.world.isRemote) {
         this.posUavX = x;
         this.posUavY = y;
         this.posUavZ = z;
         this.dataManager.func_187227_b(UAV_POS, new BlockPos(x, y, z));
      }

   }

   public void updateUavPosition() {
      BlockPos uavPos = (BlockPos)this.dataManager.func_187225_a(UAV_POS);
      this.posUavX = uavPos.func_177958_n();
      this.posUavY = uavPos.func_177956_o();
      this.posUavZ = uavPos.func_177952_p();
   }

   protected void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.setInteger("UavStatus", this.getStatus());
      nbt.setInteger("PosUavX", this.posUavX);
      nbt.setInteger("PosUavY", this.posUavY);
      nbt.setInteger("PosUavZ", this.posUavZ);
      String s = "";
      if (this.getLastControlAircraft() != null && !this.getLastControlAircraft().field_70128_L) {
         s = this.getLastControlAircraft().getCommonUniqueId();
      }

      if (s.isEmpty()) {
         s = this.loadedLastControlAircraftGuid;
      }

      nbt.setString("LastCtrlAc", s);
   }

   protected void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setUavPosition(nbt.func_74762_e("PosUavX"), nbt.func_74762_e("PosUavY"), nbt.func_74762_e("PosUavZ"));
      if (nbt.hasKey("UavStatus")) {
         this.setStatus(nbt.func_74762_e("UavStatus"));
      } else {
         this.setKind(1);
      }

      this.loadedLastControlAircraftGuid = nbt.func_74779_i("LastCtrlAc");
   }

   public void initUavPostion() {
      int rt = (int)(MCH_Lib.getRotate360((double)(this.field_70177_z + 45.0F)) / 90.0D);
      this.posUavX = rt != 0 && rt != 3 ? -12 : 12;
      this.posUavZ = rt != 0 && rt != 1 ? -12 : 12;
      this.posUavY = 2;
      this.setUavPosition(this.posUavX, this.posUavY, this.posUavZ);
   }

   public void func_70106_y() {
      super.func_70106_y();
   }

   public boolean func_70097_a(DamageSource damageSource, float damage) {
      if (this.func_180431_b(damageSource)) {
         return false;
      } else if (this.field_70128_L) {
         return true;
      } else if (this.world.isRemote) {
         return true;
      } else {
         String dmt = damageSource.func_76355_l();
         damage = MCH_Config.applyDamageByExternal(this, damageSource, damage);
         if (!MCH_Multiplay.canAttackEntity((DamageSource)damageSource, this)) {
            return false;
         } else {
            boolean isCreative = false;
            Entity entity = damageSource.func_76346_g();
            boolean isDamegeSourcePlayer = false;
            if (entity instanceof EntityPlayer) {
               isCreative = ((EntityPlayer)entity).field_71075_bZ.field_75098_d;
               if (dmt.compareTo("player") == 0) {
                  isDamegeSourcePlayer = true;
               }

               W_WorldFunc.MOD_playSoundAtEntity(this, "hit", 1.0F, 1.0F);
            } else {
               W_WorldFunc.MOD_playSoundAtEntity(this, "helidmg", 1.0F, 0.9F + this.field_70146_Z.nextFloat() * 0.1F);
            }

            this.func_70018_K();
            if (damage > 0.0F) {
               Entity riddenByEntity = this.getRiddenByEntity();
               if (riddenByEntity != null) {
                  riddenByEntity.func_184220_m(this);
               }

               this.dropContentsWhenDead = true;
               this.func_70106_y();
               if (!isDamegeSourcePlayer) {
                  MCH_Explosion.newExplosion(this.world, (Entity)null, riddenByEntity, this.posX, this.posY, this.posZ, 1.0F, 0.0F, true, true, false, false, 0);
               }

               if (!isCreative) {
                  int kind = this.getKind();
                  if (kind > 0) {
                     this.func_145778_a(MCH_MOD.itemUavStation[kind - 1], 1, 0.0F);
                  }
               }
            }

            return true;
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
      Entity riddenByEntity = this.getRiddenByEntity();
      if (this.getKind() == 2 && riddenByEntity != null) {
         double px = -Math.sin((double)this.field_70177_z * 3.141592653589793D / 180.0D) * 0.9D;
         double pz = Math.cos((double)this.field_70177_z * 3.141592653589793D / 180.0D) * 0.9D;
         int x = (int)(this.posX + px);
         int y = (int)(this.posY - 0.5D);
         int z = (int)(this.posZ + pz);
         BlockPos blockpos = new BlockPos(x, y, z);
         IBlockState iblockstate = this.world.func_180495_p(blockpos);
         return iblockstate.func_185914_p() ? -0.4D : -0.9D;
      } else {
         return 0.35D;
      }
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 2.0F;
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
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

   public void func_70071_h_() {
      super.func_70071_h_();
      this.prevRotCover = this.rotCover;
      if (this.isOpen()) {
         if (this.rotCover < 1.0F) {
            this.rotCover += 0.1F;
         } else {
            this.rotCover = 1.0F;
         }
      } else if (this.rotCover > 0.0F) {
         this.rotCover -= 0.1F;
      } else {
         this.rotCover = 0.0F;
      }

      Entity riddenByEntity = this.getRiddenByEntity();
      if (riddenByEntity == null) {
         if (this.lastRiddenByEntity != null) {
            this.unmountEntity(true);
         }

         this.setControlAircract((MCH_EntityAircraft)null);
      }

      int uavStationKind = this.getKind();
      if (this.field_70173_aa >= 30 || uavStationKind <= 0 || uavStationKind == 1 || uavStationKind != 2 || this.world.isRemote && !this.isRequestedSyncStatus) {
         this.isRequestedSyncStatus = true;
      }

      this.field_70169_q = this.posX;
      this.field_70167_r = this.posY;
      this.field_70166_s = this.posZ;
      if (this.getControlAircract() != null && this.getControlAircract().field_70128_L) {
         this.setControlAircract((MCH_EntityAircraft)null);
      }

      if (this.getLastControlAircraft() != null && this.getLastControlAircraft().field_70128_L) {
         this.setLastControlAircraft((MCH_EntityAircraft)null);
      }

      if (this.world.isRemote) {
         this.onUpdate_Client();
      } else {
         this.onUpdate_Server();
      }

      this.lastRiddenByEntity = this.getRiddenByEntity();
   }

   @Nullable
   public MCH_EntityAircraft getLastControlAircraft() {
      return this.lastControlAircraft;
   }

   public MCH_EntityAircraft getAndSearchLastControlAircraft() {
      if (this.getLastControlAircraft() == null) {
         int id = this.getLastControlAircraftEntityId();
         if (id > 0) {
            Entity entity = this.world.func_73045_a(id);
            if (entity instanceof MCH_EntityAircraft) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
               if (ac.isUAV()) {
                  this.setLastControlAircraft(ac);
               }
            }
         }
      }

      return this.getLastControlAircraft();
   }

   public void setLastControlAircraft(MCH_EntityAircraft ac) {
      MCH_Lib.DbgLog(this.world, "MCH_EntityUavStation.setLastControlAircraft:" + ac);
      this.lastControlAircraft = ac;
   }

   public Integer getLastControlAircraftEntityId() {
      return (Integer)this.dataManager.func_187225_a(LAST_AC_ID);
   }

   public void setLastControlAircraftEntityId(int s) {
      if (!this.world.isRemote) {
         this.dataManager.func_187227_b(LAST_AC_ID, s);
      }

   }

   public void searchLastControlAircraft() {
      if (!this.loadedLastControlAircraftGuid.isEmpty()) {
         List<MCH_EntityAircraft> list = this.world.func_72872_a(MCH_EntityAircraft.class, this.func_70046_E().func_72314_b(120.0D, 120.0D, 120.0D));
         if (list != null) {
            for(int i = 0; i < list.size(); ++i) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
               if (ac.getCommonUniqueId().equals(this.loadedLastControlAircraftGuid)) {
                  String n = "no info : " + ac;
                  MCH_Lib.DbgLog(this.world, "MCH_EntityUavStation.searchLastControlAircraft:found" + n);
                  this.setLastControlAircraft(ac);
                  this.setLastControlAircraftEntityId(W_Entity.getEntityId(ac));
                  this.loadedLastControlAircraftGuid = "";
                  return;
               }
            }

         }
      }
   }

   protected void onUpdate_Client() {
      if (this.aircraftPosRotInc > 0) {
         double rpinc = (double)this.aircraftPosRotInc;
         double yaw = MathHelper.func_76138_g(this.aircraftYaw - (double)this.field_70177_z);
         this.field_70177_z = (float)((double)this.field_70177_z + yaw / rpinc);
         this.field_70125_A = (float)((double)this.field_70125_A + (this.aircraftPitch - (double)this.field_70125_A) / rpinc);
         this.func_70107_b(this.posX + (this.aircraftX - this.posX) / rpinc, this.posY + (this.aircraftY - this.posY) / rpinc, this.posZ + (this.aircraftZ - this.posZ) / rpinc);
         this.func_70101_b(this.field_70177_z, this.field_70125_A);
         --this.aircraftPosRotInc;
      } else {
         this.func_70107_b(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);
         this.field_70181_x *= 0.96D;
         this.field_70159_w = 0.0D;
         this.field_70179_y = 0.0D;
      }

      this.updateUavPosition();
   }

   private void onUpdate_Server() {
      this.field_70181_x -= 0.03D;
      this.func_70091_d(MoverType.SELF, 0.0D, this.field_70181_x, 0.0D);
      this.field_70181_x *= 0.96D;
      this.field_70159_w = 0.0D;
      this.field_70179_y = 0.0D;
      this.func_70101_b(this.field_70177_z, this.field_70125_A);
      Entity riddenByEntity = this.getRiddenByEntity();
      if (riddenByEntity != null) {
         if (riddenByEntity.field_70128_L) {
            this.unmountEntity(true);
         } else {
            ItemStack item = this.func_70301_a(0);
            if (!item.func_190926_b()) {
               this.handleItem(riddenByEntity, item);
               if (item.func_190916_E() == 0) {
                  this.func_70299_a(0, ItemStack.field_190927_a);
               }
            }
         }
      }

      if (this.getLastControlAircraft() == null && this.field_70173_aa % 40 == 0) {
         this.searchLastControlAircraft();
      }

   }

   public void func_180426_a(double par1, double par3, double par5, float par7, float par8, int par9, boolean teleport) {
      this.aircraftPosRotInc = par9 + 8;
      this.aircraftX = par1;
      this.aircraftY = par3;
      this.aircraftZ = par5;
      this.aircraftYaw = (double)par7;
      this.aircraftPitch = (double)par8;
      this.field_70159_w = this.velocityX;
      this.field_70181_x = this.velocityY;
      this.field_70179_y = this.velocityZ;
   }

   public void func_184232_k(Entity passenger) {
      if (this.func_184196_w(passenger)) {
         double x = -Math.sin((double)this.field_70177_z * 3.141592653589793D / 180.0D) * 0.9D;
         double z = Math.cos((double)this.field_70177_z * 3.141592653589793D / 180.0D) * 0.9D;
         passenger.func_70107_b(this.posX + x, this.posY + this.func_70042_X() + passenger.func_70033_W() + 0.3499999940395355D, this.posZ + z);
      }

   }

   public void controlLastAircraft(Entity user) {
      if (this.getLastControlAircraft() != null && !this.getLastControlAircraft().field_70128_L) {
         this.getLastControlAircraft().setUavStation(this);
         this.setControlAircract(this.getLastControlAircraft());
         W_EntityPlayer.closeScreen(user);
      }

   }

   public void handleItem(@Nullable Entity user, ItemStack itemStack) {
      if (user != null && !user.field_70128_L && !itemStack.func_190926_b() && itemStack.func_190916_E() == 1) {
         if (!this.world.isRemote) {
            MCH_EntityAircraft ac = null;
            double x = this.posX + (double)this.posUavX;
            double y = this.posY + (double)this.posUavY;
            double z = this.posZ + (double)this.posUavZ;
            if (y <= 1.0D) {
               y = 2.0D;
            }

            Item item = itemStack.func_77973_b();
            if (item instanceof MCP_ItemPlane) {
               MCP_PlaneInfo pi = MCP_PlaneInfoManager.getFromItem(item);
               if (pi != null && pi.isUAV) {
                  if (!pi.isSmallUAV && this.getKind() == 2) {
                     ac = null;
                  } else {
                     ac = ((MCP_ItemPlane)item).createAircraft(this.world, x, y, z, itemStack);
                  }
               }
            }

            if (item instanceof MCH_ItemHeli) {
               MCH_HeliInfo hi = MCH_HeliInfoManager.getFromItem(item);
               if (hi != null && hi.isUAV) {
                  if (!hi.isSmallUAV && this.getKind() == 2) {
                     ac = null;
                  } else {
                     ac = ((MCH_ItemHeli)item).createAircraft(this.world, x, y, z, itemStack);
                  }
               }
            }

            if (item instanceof MCH_ItemTank) {
               MCH_TankInfo hi = MCH_TankInfoManager.getFromItem(item);
               if (hi != null && hi.isUAV) {
                  if (!hi.isSmallUAV && this.getKind() == 2) {
                     ac = null;
                  } else {
                     ac = ((MCH_ItemTank)item).createAircraft(this.world, x, y, z, itemStack);
                  }
               }
            }

            if (ac != null) {
               ((MCH_EntityAircraft)ac).field_70177_z = this.field_70177_z - 180.0F;
               ((MCH_EntityAircraft)ac).field_70126_B = ((MCH_EntityAircraft)ac).field_70177_z;
               user.field_70177_z = this.field_70177_z - 180.0F;
               if (this.world.func_184144_a((Entity)ac, ((MCH_EntityAircraft)ac).func_174813_aQ().func_72314_b(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                  itemStack.func_190918_g(1);
                  MCH_Lib.DbgLog(this.world, "Create UAV: %s : %s", item.func_77658_a(), item);
                  user.field_70177_z = this.field_70177_z - 180.0F;
                  if (!((MCH_EntityAircraft)ac).isTargetDrone()) {
                     ((MCH_EntityAircraft)ac).setUavStation(this);
                     this.setControlAircract((MCH_EntityAircraft)ac);
                  }

                  this.world.func_72838_d((Entity)ac);
                  if (!((MCH_EntityAircraft)ac).isTargetDrone()) {
                     ((MCH_EntityAircraft)ac).setFuel((int)((float)((MCH_EntityAircraft)ac).getMaxFuel() * 0.05F));
                     W_EntityPlayer.closeScreen(user);
                  } else {
                     ((MCH_EntityAircraft)ac).setFuel(((MCH_EntityAircraft)ac).getMaxFuel());
                  }
               } else {
                  ((MCH_EntityAircraft)ac).func_70106_y();
               }

            }
         }
      }
   }

   public void _setInventorySlotContents(int par1, ItemStack itemStack) {
      super.func_70299_a(par1, itemStack);
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      if (hand != EnumHand.MAIN_HAND) {
         return false;
      } else {
         int kind = this.getKind();
         if (kind <= 0) {
            return false;
         } else if (this.getRiddenByEntity() != null) {
            return false;
         } else {
            if (kind == 2) {
               if (player.func_70093_af()) {
                  this.setOpen(!this.isOpen());
                  return false;
               }

               if (!this.isOpen()) {
                  return false;
               }
            }

            this.lastRiddenByEntity = null;
            PooledGuiParameter.setEntity(player, this);
            if (!this.world.isRemote) {
               player.func_184220_m(this);
               player.openGui(MCH_MOD.instance, 0, player.world, (int)this.posX, (int)this.posY, (int)this.posZ);
            }

            return true;
         }
      }
   }

   public int func_70302_i_() {
      return 1;
   }

   public int func_70297_j_() {
      return 1;
   }

   public void unmountEntity(boolean unmountAllEntity) {
      Entity rByEntity = null;
      Entity riddenByEntity = this.getRiddenByEntity();
      if (riddenByEntity != null) {
         if (!this.world.isRemote) {
            rByEntity = riddenByEntity;
            riddenByEntity.func_184210_p();
         }
      } else if (this.lastRiddenByEntity != null) {
         rByEntity = this.lastRiddenByEntity;
      }

      if (this.getControlAircract() != null) {
         this.getControlAircract().setUavStation((MCH_EntityUavStation)null);
      }

      this.setControlAircract((MCH_EntityAircraft)null);
      if (this.world.isRemote) {
         W_EntityPlayer.closeScreen(rByEntity);
      }

      this.lastRiddenByEntity = null;
   }

   @Nullable
   public Entity getRiddenByEntity() {
      List<Entity> passengers = this.func_184188_bt();
      return passengers.isEmpty() ? null : (Entity)passengers.get(0);
   }

   public ItemStack getPickedResult(RayTraceResult target) {
      int kind = this.getKind();
      return kind > 0 ? new ItemStack(MCH_MOD.itemUavStation[kind - 1]) : ItemStack.field_190927_a;
   }

   static {
      STATUS = EntityDataManager.createKey(MCH_EntityUavStation.class, DataSerializers.field_187191_a);
      LAST_AC_ID = EntityDataManager.createKey(MCH_EntityUavStation.class, DataSerializers.VARINT);
      UAV_POS = EntityDataManager.createKey(MCH_EntityUavStation.class, DataSerializers.field_187200_j);
   }
}
