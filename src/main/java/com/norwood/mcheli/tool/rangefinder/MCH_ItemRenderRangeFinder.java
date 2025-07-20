package com.norwood.mcheli.tool.rangefinder;

import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.helper.client.IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

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
        GL11.glPushMatrix();
        W_McClient.MOD_bindTexture("textures/rangefinder.png");
        float size;
        switch (type) {
            case ENTITY:
                size = 2.2F;
                GL11.glScalef(size, size, size);
                GL11.glRotatef(-130.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(70.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(5.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0.0F, 0.0F, -0.0F);
                MCH_ModelManager.render("rangefinder");
                break;
            case EQUIPPED:
                size = 2.2F;
                GL11.glScalef(size, size, size);
                GL11.glRotatef(-130.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(70.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(5.0F, 0.0F, 0.0F, 1.0F);
                if (Minecraft.getMinecraft().player.getItemInUseMaxCount() > 0) {
                    GL11.glTranslatef(0.4F, -0.35F, -0.3F);
                } else {
                    GL11.glTranslatef(0.2F, -0.35F, -0.3F);
                }

                MCH_ModelManager.render("rangefinder");
                break;
            case EQUIPPED_FIRST_PERSON:
                if (!MCH_ItemRangeFinder.isUsingScope(Minecraft.getMinecraft().player)) {
                    size = 2.2F;
                    GL11.glScalef(size, size, size);
                    GL11.glRotatef(-210.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-10.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.06F, 0.53F, -0.1F);
                    MCH_ModelManager.render("rangefinder");
                }
        }

        GL11.glPopMatrix();
    }
}
