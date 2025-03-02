package com.norwood.mcheli.container;

import java.util.List;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.entity.IEntityItemStackPickable;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_IEntityCanRideAircraft;
import com.norwood.mcheli.aircraft.MCH_SeatRackInfo;
import com.norwood.mcheli.multiplay.MCH_Multiplay;
import com.norwood.mcheli.wrapper.W_AxisAlignedBB;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Blocks;
import com.norwood.mcheli.wrapper.W_EntityContainer;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
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

public class MCH_EntityContainer extends W_EntityContainer implements MCH_IEntityCanRideAircraft, IEntityItemStackPickable {
   public static final float Y_OFFSET = 0.5F;
   private static final DataParameter<Integer> TIME_SINCE_HIT;
   private static final DataParameter<Integer> FORWARD_DIR;
   private static final DataParameter<Integer> DAMAGE_TAKEN;
   private double speedMultiplier;
   private int boatPosRotationIncrements;
   private double boatX;
   private double boatY;
   private double boatZ;
   private double boatYaw;
   private double boatPitch;
   @SideOnly(Side.CLIENT)
   private double velocityX;
   @SideOnly(Side.CLIENT)
   private double velocityY;
   @SideOnly(Side.CLIENT)
   private double velocityZ;

   public MCH_EntityContainer(World par1World) {
      super(par1World);
      this.speedMultiplier = 0.07D;
      this.field_70156_m = true;
      this.func_70105_a(2.0F, 1.0F);
      this.field_70138_W = 0.6F;
      this.field_70178_ae = true;
      this._renderDistanceWeight = 2.0D;
   }

   public MCH_EntityContainer(World par1World, double par2, double par4, double par6) {
      this(par1World);
      this.func_70107_b(par2, par4 + 0.5D, par6);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.field_70169_q = par2;
      this.field_70167_r = par4;
      this.field_70166_s = par6;
   }

   protected boolean func_70041_e_() {
      return false;
   }

   protected void entityInit() {
      this.dataManager.register(TIME_SINCE_HIT, 0);
      this.dataManager.register(FORWARD_DIR, 1);
      this.dataManager.register(DAMAGE_TAKEN, 0);
   }

   public AxisAlignedBB func_70114_g(Entity par1Entity) {
      return par1Entity.func_174813_aQ();
   }

   public AxisAlignedBB func_70046_E() {
      return this.func_174813_aQ();
   }

   public boolean func_70104_M() {
      return true;
   }

   public int func_70302_i_() {
      return 54;
   }

   public String getInvName() {
      return "Container " + super.getInvName();
   }

   public double func_70042_X() {
      return -0.3D;
   }

   public boolean func_70097_a(DamageSource ds, float damage) {
      if (this.func_180431_b(ds)) {
         return false;
      } else if (!this.world.isRemote && !this.field_70128_L) {
         damage = MCH_Config.applyDamageByExternal(this, ds, damage);
         if (!MCH_Multiplay.canAttackEntity((DamageSource)ds, this)) {
            return false;
         } else if (ds.func_76346_g() instanceof EntityPlayer && ds.func_76355_l().equalsIgnoreCase("player")) {
            MCH_Lib.DbgLog(this.world, "MCH_EntityContainer.attackEntityFrom:damage=%.1f:%s", damage, ds.func_76355_l());
            W_WorldFunc.MOD_playSoundAtEntity(this, "hit", 1.0F, 1.3F);
            this.setDamageTaken(this.getDamageTaken() + (int)(damage * 20.0F));
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.func_70018_K();
            boolean flag = ds.func_76346_g() instanceof EntityPlayer && ((EntityPlayer)ds.func_76346_g()).field_71075_bZ.field_75098_d;
            if (flag || (float)this.getDamageTaken() > 40.0F) {
               if (!flag) {
                  this.func_145778_a(MCH_MOD.itemContainer, 1, 0.0F);
               }

               this.func_70106_y();
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public void func_70057_ab() {
      this.setForwardDirection(-this.getForwardDirection());
      this.setTimeSinceHit(10);
      this.setDamageTaken(this.getDamageTaken() * 11);
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   @SideOnly(Side.CLIENT)
   public void func_180426_a(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
      this.boatPosRotationIncrements = posRotationIncrements + 10;
      this.boatX = x;
      this.boatY = y;
      this.boatZ = z;
      this.boatYaw = (double)yaw;
      this.boatPitch = (double)pitch;
      this.field_70159_w = this.velocityX;
      this.field_70181_x = this.velocityY;
      this.field_70179_y = this.velocityZ;
   }

   @SideOnly(Side.CLIENT)
   public void func_70016_h(double par1, double par3, double par5) {
      this.velocityX = this.field_70159_w = par1;
      this.velocityY = this.field_70181_x = par3;
      this.velocityZ = this.field_70179_y = par5;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.getTimeSinceHit() > 0) {
         this.setTimeSinceHit(this.getTimeSinceHit() - 1);
      }

      if ((float)this.getDamageTaken() > 0.0F) {
         this.setDamageTaken(this.getDamageTaken() - 1);
      }

      this.field_70169_q = this.posX;
      this.field_70167_r = this.posY;
      this.field_70166_s = this.posZ;
      byte b0 = 5;
      double d0 = 0.0D;

      double d4;
      double d5;
      for(int i = 0; i < b0; ++i) {
         AxisAlignedBB boundingBox = this.func_174813_aQ();
         d4 = boundingBox.field_72338_b + (boundingBox.field_72337_e - boundingBox.field_72338_b) * (double)(i + 0) / (double)b0 - 0.125D;
         d5 = boundingBox.field_72338_b + (boundingBox.field_72337_e - boundingBox.field_72338_b) * (double)(i + 1) / (double)b0 - 0.125D;
         AxisAlignedBB axisalignedbb = W_AxisAlignedBB.getAABB(boundingBox.field_72340_a, d4, boundingBox.field_72339_c, boundingBox.field_72336_d, d5, boundingBox.field_72334_f);
         if (this.world.func_72875_a(axisalignedbb, Material.field_151586_h)) {
            d0 += 1.0D / (double)b0;
         } else if (this.world.func_72875_a(axisalignedbb, Material.field_151587_i)) {
            d0 += 1.0D / (double)b0;
         }
      }

      double d3 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
      if (d3 > 0.2625D) {
      }

      double d10;
      double d11;
      if (this.world.isRemote) {
         if (this.boatPosRotationIncrements > 0) {
            d4 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
            d5 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
            d11 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;
            d10 = MathHelper.func_76138_g(this.boatYaw - (double)this.field_70177_z);
            this.field_70177_z = (float)((double)this.field_70177_z + d10 / (double)this.boatPosRotationIncrements);
            this.field_70125_A = (float)((double)this.field_70125_A + (this.boatPitch - (double)this.field_70125_A) / (double)this.boatPosRotationIncrements);
            --this.boatPosRotationIncrements;
            this.func_70107_b(d4, d5, d11);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
         } else {
            d4 = this.posX + this.field_70159_w;
            d5 = this.posY + this.field_70181_x;
            d11 = this.posZ + this.field_70179_y;
            this.func_70107_b(d4, d5, d11);
            if (this.field_70122_E) {
               this.field_70159_w *= 0.8999999761581421D;
               this.field_70179_y *= 0.8999999761581421D;
            }

            this.field_70159_w *= 0.99D;
            this.field_70181_x *= 0.95D;
            this.field_70179_y *= 0.99D;
         }
      } else {
         if (d0 < 1.0D) {
            d4 = d0 * 2.0D - 1.0D;
            this.field_70181_x += 0.04D * d4;
         } else {
            if (this.field_70181_x < 0.0D) {
               this.field_70181_x /= 2.0D;
            }

            this.field_70181_x += 0.007D;
         }

         d4 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
         if (d4 > 0.35D) {
            d5 = 0.35D / d4;
            this.field_70159_w *= d5;
            this.field_70179_y *= d5;
            d4 = 0.35D;
         }

         if (d4 > d3 && this.speedMultiplier < 0.35D) {
            this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
            if (this.speedMultiplier > 0.35D) {
               this.speedMultiplier = 0.35D;
            }
         } else {
            this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
            if (this.speedMultiplier < 0.07D) {
               this.speedMultiplier = 0.07D;
            }
         }

         if (this.field_70122_E) {
            this.field_70159_w *= 0.8999999761581421D;
            this.field_70179_y *= 0.8999999761581421D;
         }

         this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
         this.field_70159_w *= 0.99D;
         this.field_70181_x *= 0.95D;
         this.field_70179_y *= 0.99D;
         this.field_70125_A = 0.0F;
         d5 = (double)this.field_70177_z;
         d11 = this.field_70169_q - this.posX;
         d10 = this.field_70166_s - this.posZ;
         if (d11 * d11 + d10 * d10 > 0.001D) {
            d5 = (double)((float)(Math.atan2(d10, d11) * 180.0D / 3.141592653589793D));
         }

         double d12 = MathHelper.func_76138_g(d5 - (double)this.field_70177_z);
         if (d12 > 5.0D) {
            d12 = 5.0D;
         }

         if (d12 < -5.0D) {
            d12 = -5.0D;
         }

         this.field_70177_z = (float)((double)this.field_70177_z + d12);
         this.func_70101_b(this.field_70177_z, this.field_70125_A);
         if (!this.world.isRemote) {
            List<Entity> list = this.world.func_72839_b(this, this.func_174813_aQ().func_72314_b(0.2D, 0.0D, 0.2D));
            int l;
            if (list != null && !list.isEmpty()) {
               for(l = 0; l < list.size(); ++l) {
                  Entity entity = (Entity)list.get(l);
                  if (entity.func_70104_M() && entity instanceof MCH_EntityContainer) {
                     entity.func_70108_f(this);
                  }
               }
            }

            if (MCH_Config.Collision_DestroyBlock.prmBool) {
               for(l = 0; l < 4; ++l) {
                  int i1 = MathHelper.func_76128_c(this.posX + ((double)(l % 2) - 0.5D) * 0.8D);
                  int j1 = MathHelper.func_76128_c(this.posZ + ((double)(l / 2) - 0.5D) * 0.8D);

                  for(int k1 = 0; k1 < 2; ++k1) {
                     int l1 = MathHelper.func_76128_c(this.posY) + k1;
                     if (W_WorldFunc.isEqualBlock(this.world, i1, l1, j1, W_Block.getSnowLayer())) {
                        this.world.func_175698_g(new BlockPos(i1, l1, j1));
                     } else if (W_WorldFunc.isEqualBlock(this.world, i1, l1, j1, W_Blocks.field_150392_bi)) {
                        W_WorldFunc.destroyBlock(this.world, i1, l1, j1, true);
                     }
                  }
               }
            }
         }
      }

   }

   protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
   }

   protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 2.0F;
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      if (player != null) {
         this.displayInventory(player);
      }

      return true;
   }

   public void setDamageTaken(int par1) {
      this.dataManager.func_187227_b(DAMAGE_TAKEN, par1);
   }

   public int getDamageTaken() {
      return (Integer)this.dataManager.func_187225_a(DAMAGE_TAKEN);
   }

   public void setTimeSinceHit(int par1) {
      this.dataManager.func_187227_b(TIME_SINCE_HIT, par1);
   }

   public int getTimeSinceHit() {
      return (Integer)this.dataManager.func_187225_a(TIME_SINCE_HIT);
   }

   public void setForwardDirection(int par1) {
      this.dataManager.func_187227_b(FORWARD_DIR, par1);
   }

   public int getForwardDirection() {
      return (Integer)this.dataManager.func_187225_a(FORWARD_DIR);
   }

   public boolean canRideAircraft(MCH_EntityAircraft ac, int seatID, MCH_SeatRackInfo info) {
      String[] var4 = info.names;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String s = var4[var6];
         if (s.equalsIgnoreCase("container")) {
            return ac.func_184187_bx() == null && this.func_184187_bx() == null;
         }
      }

      return false;
   }

   public boolean isSkipNormalRender() {
      return this.func_184187_bx() instanceof MCH_EntitySeat;
   }

   public ItemStack getPickedResult(RayTraceResult target) {
      return new ItemStack(MCH_MOD.itemContainer);
   }

   static {
      TIME_SINCE_HIT = EntityDataManager.createKey(MCH_EntityContainer.class, DataSerializers.VARINT);
      FORWARD_DIR = EntityDataManager.createKey(MCH_EntityContainer.class, DataSerializers.VARINT);
      DAMAGE_TAKEN = EntityDataManager.createKey(MCH_EntityContainer.class, DataSerializers.VARINT);
   }
}
