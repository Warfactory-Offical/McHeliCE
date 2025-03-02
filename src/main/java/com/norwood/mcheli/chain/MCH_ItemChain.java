package com.norwood.mcheli.chain;

import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.aircraft.MCH_EntityHitBox;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.parachute.MCH_EntityParachute;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.vehicle.MCH_EntityVehicle;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Item;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MCH_ItemChain extends W_Item {
   public MCH_ItemChain(int par1) {
      super(par1);
      this.func_77625_d(1);
   }

   public static void interactEntity(ItemStack item, @Nullable Entity entity, EntityPlayer player, World world) {
      if (!world.isRemote && entity != null && !entity.field_70128_L) {
         if (entity instanceof EntityItem) {
            return;
         }

         if (entity instanceof MCH_EntityChain) {
            return;
         }

         if (entity instanceof MCH_EntityHitBox) {
            return;
         }

         if (entity instanceof MCH_EntitySeat) {
            return;
         }

         if (entity instanceof MCH_EntityUavStation) {
            return;
         }

         if (entity instanceof MCH_EntityParachute) {
            return;
         }

         if (W_Lib.isEntityLivingBase(entity)) {
            return;
         }

         if (MCH_Config.FixVehicleAtPlacedPoint.prmBool && entity instanceof MCH_EntityVehicle) {
            return;
         }

         MCH_EntityChain towingChain = getTowedEntityChain(entity);
         if (towingChain != null) {
            towingChain.func_70106_y();
            return;
         }

         Entity entityTowed = getTowedEntity(item, world);
         if (entityTowed == null) {
            playConnectTowedEntity(entity);
            setTowedEntity(item, entity);
         } else {
            if (W_Entity.isEqual(entityTowed, entity)) {
               return;
            }

            double diff = (double)entity.func_70032_d(entityTowed);
            if (diff < 2.0D || diff > 16.0D) {
               return;
            }

            MCH_EntityChain chain = new MCH_EntityChain(world, (entityTowed.posX + entity.posX) / 2.0D, (entityTowed.posY + entity.posY) / 2.0D, (entityTowed.posZ + entity.posZ) / 2.0D);
            chain.setChainLength((int)diff);
            chain.setTowEntity(entityTowed, entity);
            chain.field_70169_q = chain.posX;
            chain.field_70167_r = chain.posY;
            chain.field_70166_s = chain.posZ;
            world.func_72838_d(chain);
            playConnectTowingEntity(entity);
            setTowedEntity(item, (Entity)null);
         }
      }

   }

   public static void playConnectTowingEntity(Entity e) {
      W_WorldFunc.MOD_playSoundEffect(e.world, e.posX, e.posY, e.posZ, "chain_ct", 1.0F, 1.0F);
   }

   public static void playConnectTowedEntity(Entity e) {
      W_WorldFunc.MOD_playSoundEffect(e.world, e.posX, e.posY, e.posZ, "chain", 1.0F, 1.0F);
   }

   public void func_77622_d(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
   }

   @Nullable
   public static MCH_EntityChain getTowedEntityChain(Entity entity) {
      List<MCH_EntityChain> list = entity.world.func_72872_a(MCH_EntityChain.class, entity.func_174813_aQ().func_72314_b(25.0D, 25.0D, 25.0D));
      if (list == null) {
         return null;
      } else {
         for(int i = 0; i < list.size(); ++i) {
            MCH_EntityChain chain = (MCH_EntityChain)list.get(i);
            if (chain.isTowingEntity()) {
               if (W_Entity.isEqual(chain.towEntity, entity)) {
                  return chain;
               }

               if (W_Entity.isEqual(chain.towedEntity, entity)) {
                  return chain;
               }
            }
         }

         return null;
      }
   }

   public static void setTowedEntity(ItemStack item, @Nullable Entity entity) {
      NBTTagCompound nbt = item.func_77978_p();
      if (nbt == null) {
         nbt = new NBTTagCompound();
         item.func_77982_d(nbt);
      }

      if (entity != null && !entity.field_70128_L) {
         nbt.setInteger("TowedEntityId", W_Entity.getEntityId(entity));
         nbt.setString("TowedEntityUUID", entity.getPersistentID().toString());
      } else {
         nbt.setInteger("TowedEntityId", 0);
         nbt.setString("TowedEntityUUID", "");
      }

   }

   @Nullable
   public static Entity getTowedEntity(ItemStack item, World world) {
      NBTTagCompound nbt = item.func_77978_p();
      if (nbt == null) {
         nbt = new NBTTagCompound();
         item.func_77982_d(nbt);
      } else if (nbt.hasKey("TowedEntityId") && nbt.hasKey("TowedEntityUUID")) {
         int id = nbt.func_74762_e("TowedEntityId");
         String uuid = nbt.func_74779_i("TowedEntityUUID");
         Entity entity = world.func_73045_a(id);
         if (entity != null && !entity.field_70128_L && uuid.compareTo(entity.getPersistentID().toString()) == 0) {
            return entity;
         }
      }

      return null;
   }
}
