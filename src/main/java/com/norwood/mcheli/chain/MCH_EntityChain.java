package com.norwood.mcheli.chain;

import java.util.List;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityChain extends W_Entity {
   private static final DataParameter<Integer> TOWED_ID;
   private static final DataParameter<Integer> TOW_ID;
   private int isServerTowEntitySearchCount;
   public Entity towEntity;
   public Entity towedEntity;
   public String towEntityUUID;
   public String towedEntityUUID;
   private int chainLength;
   private boolean isTowing;

   public MCH_EntityChain(World world) {
      super(world);
      this.field_70156_m = true;
      this.func_70105_a(1.0F, 1.0F);
      this.towEntity = null;
      this.towedEntity = null;
      this.towEntityUUID = "";
      this.towedEntityUUID = "";
      this.isTowing = false;
      this.field_70158_ak = true;
      this.setChainLength(4);
      this.isServerTowEntitySearchCount = 50;
   }

   public MCH_EntityChain(World par1World, double par2, double par4, double par6) {
      this(par1World);
      this.func_70107_b(par2, par4, par6);
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

   protected void func_70088_a() {
      this.field_70180_af.func_187214_a(TOWED_ID, 0);
      this.field_70180_af.func_187214_a(TOW_ID, 0);
   }

   public AxisAlignedBB func_70114_g(Entity par1Entity) {
      return par1Entity.func_174813_aQ();
   }

   public AxisAlignedBB func_70046_E() {
      return null;
   }

   public boolean func_70104_M() {
      return true;
   }

   public boolean func_70097_a(DamageSource d, float par2) {
      return false;
   }

   public void setChainLength(int n) {
      if (n > 15) {
         n = 15;
      }

      if (n < 3) {
         n = 3;
      }

      this.chainLength = n;
   }

   public int getChainLength() {
      return this.chainLength;
   }

   public void func_70106_y() {
      super.func_70106_y();
      this.playDisconnectTowingEntity();
      this.isTowing = false;
      this.towEntity = null;
      this.towedEntity = null;
   }

   public boolean isTowingEntity() {
      return this.isTowing && !this.field_70128_L && this.towedEntity != null && this.towEntity != null;
   }

   public boolean func_70067_L() {
      return false;
   }

   public void setTowEntity(Entity towedEntity, Entity towEntity) {
      if (towedEntity != null && towEntity != null && !towedEntity.field_70128_L && !towEntity.field_70128_L && !W_Entity.isEqual(towedEntity, towEntity)) {
         this.isTowing = true;
         this.towedEntity = towedEntity;
         this.towEntity = towEntity;
         if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_187227_b(TOWED_ID, W_Entity.getEntityId(towedEntity));
            this.field_70180_af.func_187227_b(TOW_ID, W_Entity.getEntityId(towEntity));
            this.isServerTowEntitySearchCount = 0;
         }

         if (towEntity instanceof MCH_EntityAircraft) {
            ((MCH_EntityAircraft)towEntity).setTowChainEntity(this);
         }

         if (towedEntity instanceof MCH_EntityAircraft) {
            ((MCH_EntityAircraft)towedEntity).setTowedChainEntity(this);
         }
      } else {
         this.isTowing = false;
         this.towedEntity = null;
         this.towEntity = null;
      }

   }

   public void searchTowingEntity() {
      if ((this.towedEntity == null || this.towEntity == null) && !this.towedEntityUUID.isEmpty() && !this.towEntityUUID.isEmpty() && this.func_174813_aQ() != null) {
         List<Entity> list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72314_b(32.0D, 32.0D, 32.0D));
         if (list != null) {
            for(int i = 0; i < list.size(); ++i) {
               Entity entity = (Entity)list.get(i);
               String uuid = entity.getPersistentID().toString();
               if (this.towedEntity == null && uuid.compareTo(this.towedEntityUUID) == 0) {
                  this.towedEntity = entity;
               } else if (this.towEntity == null && uuid.compareTo(this.towEntityUUID) == 0) {
                  this.towEntity = entity;
               } else if (this.towEntity != null && this.towedEntity != null) {
                  this.setTowEntity(this.towedEntity, this.towEntity);
                  break;
               }
            }
         }
      }

   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.towedEntity == null || this.towedEntity.field_70128_L || this.towEntity == null || this.towEntity.field_70128_L) {
         this.towedEntity = null;
         this.towEntity = null;
         this.isTowing = false;
      }

      if (this.towedEntity != null) {
         this.towedEntity.field_70143_R = 0.0F;
      }

      this.field_70169_q = this.field_70165_t;
      this.field_70167_r = this.field_70163_u;
      this.field_70166_s = this.field_70161_v;
      if (this.field_70170_p.field_72995_K) {
         this.onUpdate_Client();
      } else {
         this.onUpdate_Server();
      }

   }

   public void onUpdate_Client() {
      if (!this.isTowingEntity()) {
         this.setTowEntity(this.field_70170_p.func_73045_a((Integer)this.field_70180_af.func_187225_a(TOWED_ID)), this.field_70170_p.func_73045_a((Integer)this.field_70180_af.func_187225_a(TOW_ID)));
      }

      double d4 = this.field_70165_t + this.field_70159_w;
      double d5 = this.field_70163_u + this.field_70181_x;
      double d11 = this.field_70161_v + this.field_70179_y;
      this.func_70107_b(d4, d5, d11);
      if (this.field_70122_E) {
         this.field_70159_w *= 0.5D;
         this.field_70181_x *= 0.5D;
         this.field_70179_y *= 0.5D;
      }

      this.field_70159_w *= 0.99D;
      this.field_70181_x *= 0.95D;
      this.field_70179_y *= 0.99D;
   }

   public void onUpdate_Server() {
      if (this.isServerTowEntitySearchCount > 0) {
         this.searchTowingEntity();
         if (this.towEntity != null && this.towedEntity != null) {
            this.isServerTowEntitySearchCount = 0;
         } else {
            --this.isServerTowEntitySearchCount;
         }
      } else if (this.towEntity == null || this.towedEntity == null) {
         this.func_70106_y();
      }

      this.field_70181_x -= 0.01D;
      if (!this.isTowing) {
         this.field_70159_w *= 0.8D;
         this.field_70181_x *= 0.8D;
         this.field_70179_y *= 0.8D;
      }

      this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
      if (this.isTowingEntity()) {
         this.func_70107_b(this.towEntity.field_70165_t, this.towEntity.field_70163_u + 2.0D, this.towEntity.field_70161_v);
         this.updateTowingEntityPosRot();
      }

      this.field_70159_w *= 0.99D;
      this.field_70181_x *= 0.95D;
      this.field_70179_y *= 0.99D;
   }

   public void updateTowingEntityPosRot() {
      double dx = this.towedEntity.field_70165_t - this.towEntity.field_70165_t;
      double dy = this.towedEntity.field_70163_u - this.towEntity.field_70163_u;
      double dz = this.towedEntity.field_70161_v - this.towEntity.field_70161_v;
      double dist = (double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz);
      float DIST = (float)this.getChainLength();
      float MAX_DIST = (float)(this.getChainLength() + 2);
      if (dist > (double)DIST) {
         this.towedEntity.field_70177_z = (float)(Math.atan2(dz, dx) * 180.0D / 3.141592653589793D) + 90.0F;
         this.towedEntity.field_70126_B = this.towedEntity.field_70177_z;
         double tmp = dist - (double)DIST;
         float accl = 0.001F;
         Entity var10000 = this.towedEntity;
         var10000.field_70159_w -= dx * (double)accl / tmp;
         var10000 = this.towedEntity;
         var10000.field_70181_x -= dy * (double)accl / tmp;
         var10000 = this.towedEntity;
         var10000.field_70179_y -= dz * (double)accl / tmp;
         if (dist > (double)MAX_DIST) {
            this.towedEntity.func_70107_b(this.towEntity.field_70165_t + dx * (double)MAX_DIST / dist, this.towEntity.field_70163_u + dy * (double)MAX_DIST / dist, this.towEntity.field_70161_v + dz * (double)MAX_DIST / dist);
         }
      }

   }

   public void playDisconnectTowingEntity() {
      W_WorldFunc.MOD_playSoundEffect(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, "chain_ct", 1.0F, 1.0F);
   }

   protected void func_70014_b(NBTTagCompound nbt) {
      if (this.isTowing && this.towEntity != null && !this.towEntity.field_70128_L && this.towedEntity != null && !this.towedEntity.field_70128_L) {
         nbt.func_74778_a("TowEntityUUID", this.towEntity.getPersistentID().toString());
         nbt.func_74778_a("TowedEntityUUID", this.towedEntity.getPersistentID().toString());
         nbt.func_74768_a("ChainLength", this.getChainLength());
      }

   }

   protected void func_70037_a(NBTTagCompound nbt) {
      this.towEntityUUID = nbt.func_74779_i("TowEntityUUID");
      this.towedEntityUUID = nbt.func_74779_i("TowedEntityUUID");
      this.setChainLength(nbt.func_74762_e("ChainLength"));
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      return false;
   }

   static {
      TOWED_ID = EntityDataManager.func_187226_a(MCH_EntityChain.class, DataSerializers.field_187192_b);
      TOW_ID = EntityDataManager.func_187226_a(MCH_EntityChain.class, DataSerializers.field_187192_b);
   }
}
