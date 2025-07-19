package com.norwood.mcheli.__helper.client.model;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class PooledModelParameters {
   private static EntityLivingBase heldItemUser;
   private static ItemStack rendererTargetItem = ItemStack.EMPTY;
   private static TransformType transformType = TransformType.NONE;

   static void setItemAndUser(ItemStack itemstack, @Nullable EntityLivingBase user) {
      rendererTargetItem = itemstack;
      heldItemUser = user;
   }

   static void setTransformType(TransformType type) {
      transformType = type;
   }

   @Nullable
   public static EntityLivingBase getEntity() {
      return heldItemUser;
   }

   public static ItemStack getTargetRendererStack() {
      return rendererTargetItem;
   }

   public static TransformType getTransformType() {
      return transformType;
   }
}
