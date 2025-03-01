package com.norwood.mcheli.aircraft;

import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.entity.IEntitySinglePassenger;
import com.norwood.mcheli.mob.MCH_ItemSpawnGunner;
import com.norwood.mcheli.tool.MCH_ItemWrench;
import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntitySeat extends W_Entity implements IEntitySinglePassenger {
   public String parentUniqueID;
   private MCH_EntityAircraft parent;
   public int seatID;
   public int parentSearchCount;
   protected Entity lastRiddenByEntity;
   public static final float BB_SIZE = 1.0F;

   public MCH_EntitySeat(World world) {
      super(world);
      this.func_70105_a(1.0F, 1.0F);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.seatID = -1;
      this.setParent((MCH_EntityAircraft)null);
      this.parentSearchCount = 0;
      this.lastRiddenByEntity = null;
      this.field_70158_ak = true;
      this.field_70178_ae = true;
   }

   public MCH_EntitySeat(World world, double x, double y, double z) {
      this(world);
      this.func_70107_b(x, y + 1.0D, z);
      this.field_70169_q = x;
      this.field_70167_r = y + 1.0D;
      this.field_70166_s = z;
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
      return -0.3D;
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      return this.getParent() != null ? this.getParent().func_70097_a(par1DamageSource, par2) : false;
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   @SideOnly(Side.CLIENT)
   public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
   }

   public void func_70106_y() {
      super.func_70106_y();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      this.field_70143_R = 0.0F;
      Entity riddenByEntity = this.getRiddenByEntity();
      if (riddenByEntity != null) {
         riddenByEntity.field_70143_R = 0.0F;
      }

      if (this.lastRiddenByEntity == null && riddenByEntity != null) {
         if (this.getParent() != null) {
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntitySeat.onUpdate:SeatID=%d", this.seatID, riddenByEntity.toString());
            this.getParent().onMountPlayerSeat(this, riddenByEntity);
         }
      } else if (this.lastRiddenByEntity != null && riddenByEntity == null && this.getParent() != null) {
         MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntitySeat.onUpdate:SeatID=%d", this.seatID, this.lastRiddenByEntity.toString());
         this.getParent().onUnmountPlayerSeat(this, this.lastRiddenByEntity);
      }

      if (this.field_70170_p.field_72995_K) {
         this.onUpdate_Client();
      } else {
         this.onUpdate_Server();
      }

      this.lastRiddenByEntity = this.getRiddenByEntity();
   }

   private void onUpdate_Client() {
      this.checkDetachmentAndDelete();
   }

   private void onUpdate_Server() {
      this.checkDetachmentAndDelete();
      Entity riddenByEntity = this.getRiddenByEntity();
      if (riddenByEntity != null && riddenByEntity.field_70128_L) {
      }

   }

   public void func_184232_k(Entity passenger) {
      this.updatePosition(passenger);
   }

   public void updatePosition(@Nullable Entity ridEnt) {
      if (ridEnt != null) {
         ridEnt.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
         ridEnt.field_70159_w = ridEnt.field_70181_x = ridEnt.field_70179_y = 0.0D;
      }

   }

   public void updateRotation(@Nullable Entity ridEnt, float yaw, float pitch) {
      if (ridEnt != null) {
         ridEnt.field_70177_z = yaw;
         ridEnt.field_70125_A = pitch;
      }

   }

   protected void checkDetachmentAndDelete() {
      if (this.field_70128_L || this.seatID >= 0 && this.getParent() != null && !this.getParent().field_70128_L) {
         this.parentSearchCount = 0;
      } else {
         if (this.getParent() != null && this.getParent().field_70128_L) {
            this.parentSearchCount = 100000000;
         }

         if (this.parentSearchCount >= 1200) {
            this.func_70106_y();
            if (!this.field_70170_p.field_72995_K) {
               Entity riddenByEntity = this.getRiddenByEntity();
               if (riddenByEntity != null) {
                  riddenByEntity.func_184210_p();
               }
            }

            this.setParent((MCH_EntityAircraft)null);
            MCH_Lib.DbgLog(this.field_70170_p, "[Error]座席エンティティは本体が見つからないため削除 seat=%d, parentUniqueID=%s", this.seatID, this.parentUniqueID);
         } else {
            ++this.parentSearchCount;
         }
      }

   }

   protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.func_74768_a("SeatID", this.seatID);
      par1NBTTagCompound.func_74778_a("ParentUniqueID", this.parentUniqueID);
   }

   protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      this.seatID = par1NBTTagCompound.func_74762_e("SeatID");
      this.parentUniqueID = par1NBTTagCompound.func_74779_i("ParentUniqueID");
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public boolean canRideMob(Entity entity) {
      if (this.getParent() != null && this.seatID >= 0) {
         return !(this.getParent().getSeatInfo(this.seatID + 1) instanceof MCH_SeatRackInfo);
      } else {
         return false;
      }
   }

   public boolean isGunnerMode() {
      Entity riddenByEntity = this.getRiddenByEntity();
      return riddenByEntity != null && this.getParent() != null ? this.getParent().getIsGunnerMode(riddenByEntity) : false;
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      if (this.getParent() != null && !this.getParent().isDestroyed()) {
         if (!this.getParent().checkTeam(player)) {
            return false;
         } else {
            ItemStack itemStack = player.func_184586_b(hand);
            if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemWrench) {
               return this.getParent().func_184230_a(player, hand);
            } else if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemSpawnGunner) {
               return this.getParent().func_184230_a(player, hand);
            } else {
               Entity riddenByEntity = this.getRiddenByEntity();
               if (riddenByEntity != null) {
                  return false;
               } else if (player.func_184187_bx() != null) {
                  return false;
               } else if (!this.canRideMob(player)) {
                  return false;
               } else {
                  player.func_184220_m(this);
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   @Nullable
   public MCH_EntityAircraft getParent() {
      return this.parent;
   }

   public void setParent(MCH_EntityAircraft parent) {
      this.parent = parent;
      Entity riddenByEntity = this.getRiddenByEntity();
      if (riddenByEntity != null) {
         MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntitySeat.setParent:SeatID=%d %s : " + this.getParent(), this.seatID, riddenByEntity.toString());
         if (this.getParent() != null) {
            this.getParent().onMountPlayerSeat(this, riddenByEntity);
         }
      }

   }

   @Nullable
   public Entity getRiddenByEntity() {
      List<Entity> passengers = this.func_184188_bt();
      return passengers.isEmpty() ? null : (Entity)passengers.get(0);
   }
}
