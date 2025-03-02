package com.norwood.mcheli.__helper.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITargetMarkerObject {
   double getX();

   double getY();

   double getZ();

   @Nullable
   default Entity getEntity() {
      return null;
   }

   static ITargetMarkerObject fromEntity(Entity target) {
      return new ITargetMarkerObject.EntityWrapper(target);
   }

   @SideOnly(Side.CLIENT)
   public static class EntityWrapper implements ITargetMarkerObject {
      private final Entity target;

      public EntityWrapper(Entity entity) {
         this.target = entity;
      }

      public double getX() {
         return this.target.posX;
      }

      public double getY() {
         return this.target.posY;
      }

      public double getZ() {
         return this.target.posZ;
      }

      public Entity getEntity() {
         return this.target;
      }
   }
}
