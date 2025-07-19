package com.norwood.mcheli.gltd;

import com.norwood.mcheli.__helper.client._IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemGLTDRender implements _IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, _IItemRenderer.ItemRenderType type) {
        return type == _IItemRenderer.ItemRenderType.EQUIPPED
                || type == _IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == _IItemRenderer.ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(_IItemRenderer.ItemRenderType type, ItemStack item, _IItemRenderer.ItemRendererHelper helper) {
        return type == _IItemRenderer.ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(_IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glEnable(2884);
        W_McClient.MOD_bindTexture("textures/gltd.png");
        switch (type) {
            case ENTITY:
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                MCH_RenderGLTD.model.renderAll();
                GL11.glDisable(32826);
                break;
            case EQUIPPED:
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glTranslatef(0.0F, 0.005F, -0.165F);
                GL11.glRotatef(-10.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                MCH_RenderGLTD.model.renderAll();
                GL11.glDisable(32826);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glTranslatef(0.3F, 0.5F, -0.5F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                MCH_RenderGLTD.model.renderAll();
                GL11.glDisable(32826);
        }

        GL11.glPopMatrix();
    }
}
