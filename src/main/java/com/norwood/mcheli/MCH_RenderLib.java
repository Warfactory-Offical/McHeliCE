package com.norwood.mcheli;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11; import net.minecraft.client.renderer.GlStateManager;

public class MCH_RenderLib {
    public static void drawLine(Vec3d[] points, int color) {
        drawLine(points, color, 1, 1);
    }

    public static void drawLine(Vec3d[] points, int color, int mode, int width) {
        int prevWidth = GL11.glGetInteger(2849);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color >> 0 & 0xFF), (byte) (color >> 24 & 0xFF));
        GL11.glLineWidth(width);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(mode, DefaultVertexFormats.POSITION);

        for (Vec3d v : points) {
            builder.pos(v.x, v.y, v.z).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color((byte) -1, (byte) -1, (byte) -1, (byte) -1);
        GL11.glLineWidth(prevWidth);
    }
}
