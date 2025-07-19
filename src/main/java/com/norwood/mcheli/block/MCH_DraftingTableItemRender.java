package com.norwood.mcheli.block;

import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.__helper.client._IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_DraftingTableItemRender implements _IItemRenderer {
   @Override
   public boolean handleRenderType(ItemStack item, _IItemRenderer.ItemRenderType type) {
      switch (type) {
         case ENTITY:
         case EQUIPPED:
         case EQUIPPED_FIRST_PERSON:
         case INVENTORY:
            return true;
         default:
            return false;
      }
   }

   @Override
   public boolean shouldUseRenderHelper(_IItemRenderer.ItemRenderType type, ItemStack item, _IItemRenderer.ItemRendererHelper helper) {
      return true;
   }

   @Override
   public void renderItem(_IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      GL11.glPushMatrix();
      W_McClient.MOD_bindTexture("textures/blocks/drafting_table.png");
      GL11.glEnable(32826);
      switch (type) {
         case ENTITY:
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            break;
         case EQUIPPED:
            GL11.glTranslatef(0.0F, 0.0F, 0.5F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            break;
         case EQUIPPED_FIRST_PERSON:
            GL11.glTranslatef(0.75F, 0.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
            break;
         case INVENTORY:
            GL11.glTranslatef(0.0F, -0.5F, 0.0F);
            GL11.glScalef(0.75F, 0.75F, 0.75F);
      }

      MCH_ModelManager.render("blocks", "drafting_table");
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
   }
}
