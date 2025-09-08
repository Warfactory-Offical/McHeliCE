package com.norwood.mcheli.gltd;

import com.norwood.mcheli.helper.client.IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11; import net.minecraft.client.renderer.GlStateManager;

@Deprecated
public class MCH_ItemGLTDRender implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED
                || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == IItemRenderer.ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return type == IItemRenderer.ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GlStateManager.pushMatrix();
        GlStateManager.enableCull();
        W_McClient.MOD_bindTexture("textures/gltd.png");
        switch (type) {
            case ENTITY:
                 GlStateManager.enableRescaleNormal();;
                GlStateManager.enableColorMaterial();
                GlStateManager.scale(1.0F, 1.0F, 1.0F);
                MCH_RenderGLTD.model.renderAll();
                 GlStateManager.disableRescaleNormal();;
                break;
            case EQUIPPED:
                 GlStateManager.enableRescaleNormal();;
                GlStateManager.enableColorMaterial();
                GlStateManager.translate(0.0F, 0.005F, -0.165F);
                GlStateManager.rotate(-10.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(-10.0F, 1.0F, 0.0F, 0.0F);
                MCH_RenderGLTD.model.renderAll();
                 GlStateManager.disableRescaleNormal();;
                break;
            case EQUIPPED_FIRST_PERSON:
                 GlStateManager.enableRescaleNormal();;
                GlStateManager.enableColorMaterial();
                GlStateManager.translate(0.3F, 0.5F, -0.5F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-10.0F, 1.0F, 0.0F, 0.0F);
                MCH_RenderGLTD.model.renderAll();
                 GlStateManager.disableRescaleNormal();;
        }

        GlStateManager.popMatrix();
    }
}
