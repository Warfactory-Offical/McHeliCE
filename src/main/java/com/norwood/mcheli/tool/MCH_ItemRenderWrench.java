package com.norwood.mcheli.tool;

import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.helper.client.IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

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
        GL11.glPushMatrix();
        W_McClient.MOD_bindTexture("textures/wrench.png");
        float size;
        switch (type) {
            case ENTITY:
                size = 2.2F;
                GL11.glScalef(size, size, size);
                GL11.glRotatef(-130.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-40.0F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.1F, 0.5F, -0.1F);
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
                        GL11.glTranslatef(-x, -y, -z);
                        GL11.glRotatef(useFrame + 20, 1.0F, 0.0F, 0.0F);
                        GL11.glTranslatef(x, y, z);
                    }
                }

                GL11.glScalef(size, size, size);
                GL11.glRotatef(-200.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-60.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.2F, 0.5F, -0.1F);
        }

        MCH_ModelManager.render("wrench");
        GL11.glPopMatrix();
    }
}
