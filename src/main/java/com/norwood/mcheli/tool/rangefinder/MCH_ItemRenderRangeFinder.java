package com.norwood.mcheli.tool.rangefinder;

import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.helper.client.IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11; import net.minecraft.client.renderer.GlStateManager;

@Deprecated
public class MCH_ItemRenderRangeFinder implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED
                || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == IItemRenderer.ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED
                || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == IItemRenderer.ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GlStateManager.pushMatrix();
        W_McClient.MOD_bindTexture("textures/rangefinder.png");
        float size;
        switch (type) {
            case ENTITY:
                size = 2.2F;
                GlStateManager.scale(size, size, size);
                GlStateManager.rotate(-130.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(70.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(5.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(0.0F, 0.0F, -0.0F);
                MCH_ModelManager.render("rangefinder");
                break;
            case EQUIPPED:
                size = 2.2F;
                GlStateManager.scale(size, size, size);
                GlStateManager.rotate(-130.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(70.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(5.0F, 0.0F, 0.0F, 1.0F);
                if (Minecraft.getMinecraft().player.getItemInUseMaxCount() > 0) {
                    GlStateManager.translate(0.4F, -0.35F, -0.3F);
                } else {
                    GlStateManager.translate(0.2F, -0.35F, -0.3F);
                }

                MCH_ModelManager.render("rangefinder");
                break;
            case EQUIPPED_FIRST_PERSON:
                if (!MCH_ItemRangeFinder.isUsingScope(Minecraft.getMinecraft().player)) {
                    size = 2.2F;
                    GlStateManager.scale(size, size, size);
                    GlStateManager.rotate(-210.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-10.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(-10.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(0.06F, 0.53F, -0.1F);
                    MCH_ModelManager.render("rangefinder");
                }
        }

        GlStateManager.popMatrix();
    }
}
