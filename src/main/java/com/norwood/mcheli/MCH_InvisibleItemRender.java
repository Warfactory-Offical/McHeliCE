package com.norwood.mcheli;

import com.norwood.mcheli.__helper.client._IItemRenderer;
import net.minecraft.item.ItemStack;

@Deprecated
public class MCH_InvisibleItemRender implements _IItemRenderer {
   @Override
   public boolean handleRenderType(ItemStack item, _IItemRenderer.ItemRenderType type) {
      return type == _IItemRenderer.ItemRenderType.EQUIPPED || type == _IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
   }

   @Override
   public boolean shouldUseRenderHelper(_IItemRenderer.ItemRenderType type, ItemStack item, _IItemRenderer.ItemRendererHelper helper) {
      return false;
   }

   public boolean useCurrentWeapon() {
      return false;
   }

   @Override
   public void renderItem(_IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
   }
}
