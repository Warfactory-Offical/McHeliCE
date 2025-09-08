package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.helper.client.IItemRenderer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11; import net.minecraft.client.renderer.GlStateManager;

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
        GlStateManager.pushMatrix();
        GlStateManager.enableCull();
        W_McClient.MOD_bindTexture("textures/helicopters/mh-60l_dap.png");
        switch (type) {
            case ENTITY:
                 GlStateManager.enableRescaleNormal();;
                GlStateManager.enableColorMaterial();
                GlStateManager.scale(0.1F, 0.1F, 0.1F);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                 GlStateManager.disableRescaleNormal();;
                break;
            case EQUIPPED:
                 GlStateManager.enableRescaleNormal();;
                GlStateManager.enableColorMaterial();
                GlStateManager.translate(0.0F, 0.005F, -0.165F);
                GlStateManager.scale(0.1F, 0.1F, 0.1F);
                GlStateManager.rotate(-10.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(90.0F, 0.0F, -1.0F, 0.0F);
                GlStateManager.rotate(-50.0F, 1.0F, 0.0F, 0.0F);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                 GlStateManager.disableRescaleNormal();;
                break;
            case EQUIPPED_FIRST_PERSON:
                 GlStateManager.enableRescaleNormal();;
                GlStateManager.enableColorMaterial();
                GlStateManager.translate(0.3F, 0.5F, -0.5F);
                GlStateManager.scale(0.1F, 0.1F, 0.1F);
                GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(140.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-10.0F, 1.0F, 0.0F, 0.0F);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                 GlStateManager.disableRescaleNormal();;
                break;
            case INVENTORY:
                GlStateManager.translate(this.x, this.y, this.z);
                GlStateManager.scale(this.size, this.size, this.size);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
            case FIRST_PERSON_MAP:
        }

        GlStateManager.popMatrix();
    }
}
