package com.norwood.mcheli.wrapper;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_MOD;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public abstract class W_Render<T extends Entity> extends Render<T> {
    protected static final ResourceLocation TEX_DEFAULT = new ResourceLocation(MCH_MOD.DOMAIN, "textures/default.png");
    private static final FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
    public int srcBlend;
    public int dstBlend;

    protected W_Render(RenderManager renderManager) {
        super(renderManager);
    }

    public static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        return colorBuffer;
    }

    protected void bindTexture(String path) {
        super.bindTexture(new ResourceLocation(MCH_MOD.DOMAIN, path));
    }

    protected ResourceLocation getEntityTexture(@NotNull T entity) {
        return TEX_DEFAULT;
    }

    public void setCommonRenderParam(boolean smoothShading, int lighting) {
        if (smoothShading && MCH_Config.SmoothShading.prmBool) {
            GL11.glShadeModel(7425);
        }

        GL11.glAlphaFunc(516, 0.001F);
        GL11.glEnable(2884);
        int j = lighting % 65536;
        int k = lighting / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
        GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
        GL11.glEnable(3042);
        this.srcBlend = GL11.glGetInteger(3041);
        this.dstBlend = GL11.glGetInteger(3040);
        GL11.glBlendFunc(770, 771);
    }

    public void restoreCommonRenderParam() {
        GL11.glBlendFunc(this.srcBlend, this.dstBlend);
        GL11.glDisable(3042);
        GL11.glShadeModel(7424);
    }

}
