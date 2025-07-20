package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.helper.client.IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemAircraftRender implements IItemRenderer {
    final float size = 0.1F;
    final float x = 0.1F;
    final float y = 0.1F;
    final float z = 0.1F;

    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        if (item != null && item.getItem() instanceof MCH_ItemAircraft) {
            MCH_AircraftInfo info = ((MCH_ItemAircraft) item.getItem()).getAircraftInfo();
            if (info == null) {
                return false;
            }

            if (info.name.equalsIgnoreCase("mh-60l_dap")) {
                return type == IItemRenderer.ItemRenderType.EQUIPPED
                        || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON
                        || type == IItemRenderer.ItemRenderType.ENTITY
                        || type == IItemRenderer.ItemRenderType.INVENTORY;
            }
        }

        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return type == IItemRenderer.ItemRenderType.ENTITY || type == IItemRenderer.ItemRenderType.INVENTORY;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glEnable(2884);
        W_McClient.MOD_bindTexture("textures/helicopters/mh-60l_dap.png");
        switch (type) {
            case ENTITY:
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glScalef(0.1F, 0.1F, 0.1F);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                GL11.glDisable(32826);
                break;
            case EQUIPPED:
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glTranslatef(0.0F, 0.005F, -0.165F);
                GL11.glScalef(0.1F, 0.1F, 0.1F);
                GL11.glRotatef(-10.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(-50.0F, 1.0F, 0.0F, 0.0F);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                GL11.glDisable(32826);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glTranslatef(0.3F, 0.5F, -0.5F);
                GL11.glScalef(0.1F, 0.1F, 0.1F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(140.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                GL11.glDisable(32826);
                break;
            case INVENTORY:
                GL11.glTranslatef(this.x, this.y, this.z);
                GL11.glScalef(this.size, this.size, this.size);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
            case FIRST_PERSON_MAP:
        }

        GL11.glPopMatrix();
    }
}
