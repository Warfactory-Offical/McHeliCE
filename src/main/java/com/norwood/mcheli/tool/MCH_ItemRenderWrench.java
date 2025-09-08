package com.norwood.mcheli.tool;

import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.helper.client.IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11; import net.minecraft.client.renderer.GlStateManager;

@Deprecated
public class MCH_ItemRenderWrench implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GlStateManager.pushMatrix();
        W_McClient.MOD_bindTexture("textures/wrench.png");
        float size;
        switch (type) {
            case ENTITY:
                size = 2.2F;
                GlStateManager.scale(size, size, size);
                GlStateManager.rotate(-130.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-40.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.1F, 0.5F, -0.1F);
                break;
            case EQUIPPED:
                int useFrame = MCH_ItemWrench.getUseAnimCount(item) - 8;
                if (useFrame < 0) {
                    useFrame = -useFrame;
                }

                size = 2.2F;
                if (data.length >= 2 && data[1] instanceof EntityPlayer player) {
                    if (player.getItemInUseCount() > 0) {
                        float x = 0.8567F;
                        float y = -0.0298F;
                        float z = 0.0F;
                        GlStateManager.translate(-x, -y, -z);
                        GlStateManager.rotate(useFrame + 20, 1.0F, 0.0F, 0.0F);
                        GlStateManager.translate(x, y, z);
                    }
                }

                GlStateManager.scale(size, size, size);
                GlStateManager.rotate(-200.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-60.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(0.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(-0.2F, 0.5F, -0.1F);
        }

        MCH_ModelManager.render("wrench");
        GlStateManager.popMatrix();
    }
}
