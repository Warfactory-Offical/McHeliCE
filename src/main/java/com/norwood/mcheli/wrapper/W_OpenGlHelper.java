package com.norwood.mcheli.wrapper;

import org.lwjgl.opengl.GL11; import net.minecraft.client.renderer.GlStateManager;

public class W_OpenGlHelper {
    public static void glBlendFunc(int i, int j, int k, int l) {
        GL11.glBlendFunc(i, j);
    }
}
