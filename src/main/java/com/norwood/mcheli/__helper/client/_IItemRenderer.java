package com.norwood.mcheli.__helper.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** @deprecated */
@Deprecated
@SideOnly(Side.CLIENT)
public interface _IItemRenderer {
   boolean handleRenderType(ItemStack var1, _IItemRenderer.ItemRenderType var2);

   boolean shouldUseRenderHelper(_IItemRenderer.ItemRenderType var1, ItemStack var2, _IItemRenderer.ItemRendererHelper var3);

   void renderItem(_IItemRenderer.ItemRenderType var1, ItemStack var2, Object... var3);

   /** @deprecated */
   @Deprecated
   public static enum ItemRendererHelper {
      ENTITY_ROTATION,
      ENTITY_BOBBING,
      EQUIPPED_BLOCK,
      BLOCK_3D,
      INVENTORY_BLOCK;
   }

   /** @deprecated */
   @Deprecated
   public static enum ItemRenderType {
      ENTITY,
      EQUIPPED,
      EQUIPPED_FIRST_PERSON,
      INVENTORY,
      FIRST_PERSON_MAP;
   }
}
