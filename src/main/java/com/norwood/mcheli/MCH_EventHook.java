package com.norwood.mcheli;

import java.util.List;
import java.util.UUID;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_ItemAircraft;
import com.norwood.mcheli.chain.MCH_ItemChain;
import com.norwood.mcheli.command.MCH_Command;
import com.norwood.mcheli.weapon.MCH_EntityBaseBullet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_EntityPlayer;
import com.norwood.mcheli.wrapper.W_EventHook;
import com.norwood.mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.CanUpdate;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class MCH_EventHook extends W_EventHook {
   @Override
   public void commandEvent(CommandEvent event) {
      MCH_Command.onCommandEvent(event);
   }

   @Override
   public void entitySpawn(EntityJoinWorldEvent event) {
      if (W_Lib.isEntityLivingBase(event.getEntity()) && !W_EntityPlayer.isPlayer(event.getEntity())) {
         MCH_MOD.proxy.setRenderEntityDistanceWeight(MCH_Config.MobRenderDistanceWeight.prmDouble);
      } else if (event.getEntity() instanceof MCH_EntityAircraft) {
         MCH_EntityAircraft aircraft = (MCH_EntityAircraft)event.getEntity();
         if (!aircraft.world.isRemote && !aircraft.isCreatedSeats()) {
            aircraft.createSeats(UUID.randomUUID().toString());
         }
      } else if (W_EntityPlayer.isPlayer(event.getEntity())) {
         Entity e = event.getEntity();
         boolean b = Float.isNaN(e.rotationPitch);
         b |= Float.isNaN(e.prevRotationPitch);
         b |= Float.isInfinite(e.rotationPitch);
         b |= Float.isInfinite(e.prevRotationPitch);
         if (b) {
            MCH_Lib.Log(event.getEntity(), "### EntityJoinWorldEvent Error:Player invalid rotation pitch(" + e.rotationPitch + ")");
            e.rotationPitch = 0.0F;
            e.prevRotationPitch = 0.0F;
         }

         b = Float.isInfinite(e.rotationYaw);
         b |= Float.isInfinite(e.prevRotationYaw);
         b |= Float.isNaN(e.rotationYaw);
         b |= Float.isNaN(e.prevRotationYaw);
         if (b) {
            MCH_Lib.Log(event.getEntity(), "### EntityJoinWorldEvent Error:Player invalid rotation yaw(" + e.rotationYaw + ")");
            e.rotationYaw = 0.0F;
            e.prevRotationYaw = 0.0F;
         }

         if (!e.world.isRemote && event.getEntity() instanceof EntityPlayerMP) {
            MCH_Lib.DbgLog(false, "EntityJoinWorldEvent:" + event.getEntity());
            MCH_PacketNotifyServerSettings.send((EntityPlayerMP)event.getEntity());
         }
      }
   }

   @Override
   public void livingAttackEvent(LivingAttackEvent event) {
      MCH_EntityAircraft ac = this.getRiddenAircraft(event.getEntity());
      if (ac != null) {
         if (ac.getAcInfo() != null) {
            if (!ac.isDestroyed()) {
               if (!(ac.getAcInfo().damageFactor > 0.0F)) {
                  Entity attackEntity = event.getSource().getTrueSource();
                  if (attackEntity == null) {
                     event.setCanceled(true);
                  } else if (W_Entity.isEqual(attackEntity, event.getEntity())) {
                     event.setCanceled(true);
                  } else if (ac.isMountedEntity(attackEntity)) {
                     event.setCanceled(true);
                  } else {
                     MCH_EntityAircraft atkac = this.getRiddenAircraft(attackEntity);
                     if (W_Entity.isEqual(atkac, ac)) {
                        event.setCanceled(true);
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void livingHurtEvent(LivingHurtEvent event) {
      MCH_EntityAircraft ac = this.getRiddenAircraft(event.getEntity());
      if (ac != null) {
         if (ac.getAcInfo() != null) {
            if (!ac.isDestroyed()) {
               Entity attackEntity = event.getSource().getTrueSource();
               float f = event.getAmount();
               if (attackEntity == null) {
                  ac.attackEntityFrom(event.getSource(), f * 2.0F);
                  f *= ac.getAcInfo().damageFactor;
               } else if (W_Entity.isEqual(attackEntity, event.getEntity())) {
                  ac.attackEntityFrom(event.getSource(), f * 2.0F);
                  f *= ac.getAcInfo().damageFactor;
               } else if (ac.isMountedEntity(attackEntity)) {
                  f = 0.0F;
                  event.setCanceled(true);
               } else {
                  MCH_EntityAircraft atkac = this.getRiddenAircraft(attackEntity);
                  if (W_Entity.isEqual(atkac, ac)) {
                     f = 0.0F;
                     event.setCanceled(true);
                  } else {
                     ac.attackEntityFrom(event.getSource(), f * 2.0F);
                     f *= ac.getAcInfo().damageFactor;
                  }
               }

               event.setAmount(f);
            }
         }
      }
   }

   public MCH_EntityAircraft getRiddenAircraft(Entity entity) {
      MCH_EntityAircraft ac = null;
      Entity ridden = entity.getRidingEntity();
      if (ridden instanceof MCH_EntityAircraft) {
         ac = (MCH_EntityAircraft)ridden;
      } else if (ridden instanceof MCH_EntitySeat) {
         ac = ((MCH_EntitySeat)ridden).getParent();
      }

      if (ac == null) {
         List<MCH_EntityAircraft> list = entity.world.getEntitiesWithinAABB(MCH_EntityAircraft.class, entity.getEntityBoundingBox().grow(50.0, 50.0, 50.0));
         if (list != null) {
            for (int i = 0; i < list.size(); i++) {
               MCH_EntityAircraft tmp = list.get(i);
               if (tmp.isMountedEntity(entity)) {
                  return tmp;
               }
            }
         }
      }

      return ac;
   }

   @Override
   public void entityInteractEvent(EntityInteract event) {
      ItemStack item = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
      if (!item.isEmpty()) {
         if (item.getItem() instanceof MCH_ItemChain) {
            MCH_ItemChain.interactEntity(item, event.getTarget(), event.getEntityPlayer(), event.getEntityPlayer().world);
            event.setCanceled(true);
         } else if (item.getItem() instanceof MCH_ItemAircraft) {
            ((MCH_ItemAircraft)item.getItem()).rideEntity(item, event.getTarget(), event.getEntityPlayer());
         }
      }
   }

   @Override
   public void entityCanUpdate(CanUpdate event) {
      if (event.getEntity() instanceof MCH_EntityBaseBullet) {
         MCH_EntityBaseBullet bullet = (MCH_EntityBaseBullet)event.getEntity();
         bullet.setDead();
      }
   }
}
