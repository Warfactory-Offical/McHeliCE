package com.norwood.mcheli.flare;

import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderFlare extends W_Render<MCH_EntityFlare> {
    public static final IRenderFactory<MCH_EntityFlare> FACTORY = MCH_RenderFlare::new;
    protected final MCH_ModelFlare model = new MCH_ModelFlare();

    public MCH_RenderFlare(RenderManager renderManager) {
        super(renderManager);
    }

    public void doRender(MCH_EntityFlare entity, double posX, double posY, double posZ, float yaw, float partialTickTime) {
        GL11.glPushMatrix();
        GL11.glEnable(2884);
        GL11.glTranslated(posX, posY, posZ);
        GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 0.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 0.5F, 1.0F);
        this.bindTexture("textures/flare.png");
        this.model.renderModel(0.0, 0.0, 0.0625F);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(@NotNull MCH_EntityFlare entity) {
        return TEX_DEFAULT;
    }
}
