package com.norwood.mcheli.uav;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderUavStation extends W_Render<MCH_EntityUavStation> {
    public static final IRenderFactory<MCH_EntityUavStation> FACTORY = MCH_RenderUavStation::new;
    public static final String[] MODEL_NAME = new String[]{"uav_station", "uav_portable_controller"};
    public static final String[] TEX_NAME_ON = new String[]{"uav_station_on", "uav_portable_controller_on"};
    public static final String[] TEX_NAME_OFF = new String[]{"uav_station", "uav_portable_controller"};

    public MCH_RenderUavStation(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 1.0F;
    }

    public void doRender(@NotNull MCH_EntityUavStation entity, double posX, double posY, double posZ, float par8, float tickTime) {
        if (entity.getKind() > 0) {
            int kind = entity.getKind() - 1;
            GL11.glPushMatrix();
            GL11.glTranslated(posX, posY + 0.35F, posZ);
            GL11.glEnable(2884);
            GL11.glRotatef(entity.rotationYaw, 0.0F, -1.0F, 0.0F);
            GL11.glRotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
            GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
            GL11.glEnable(3042);
            int srcBlend = GL11.glGetInteger(3041);
            int dstBlend = GL11.glGetInteger(3040);
            GL11.glBlendFunc(770, 771);
            if (kind == 0) {
                if (entity.getControlAircract() != null && entity.getRiddenByEntity() != null) {
                    this.bindTexture("textures/" + TEX_NAME_ON[kind] + ".png");
                } else {
                    this.bindTexture("textures/" + TEX_NAME_OFF[kind] + ".png");
                }

                MCH_ModelManager.render(MODEL_NAME[kind]);
            } else {
                if (entity.rotCover > 0.95F) {
                    this.bindTexture("textures/" + TEX_NAME_ON[kind] + ".png");
                } else {
                    this.bindTexture("textures/" + TEX_NAME_OFF[kind] + ".png");
                }

                this.renderPortableController(entity, MODEL_NAME[kind], tickTime);
            }

            GL11.glBlendFunc(srcBlend, dstBlend);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }

    public void renderPortableController(MCH_EntityUavStation uavSt, String name, float tickTime) {
        MCH_ModelManager.renderPart(name, "$body");
        float rot = MCH_Lib.smooth(uavSt.rotCover, uavSt.prevRotCover, tickTime);
        this.renderRotPart(name, "$cover", rot * 60.0F, 0.0, -0.1812, -0.3186);
        this.renderRotPart(name, "$laptop_cover", rot * 95.0F, 0.0, -0.1808, -0.0422);
        this.renderRotPart(name, "$display", rot * -85.0F, 0.0, -0.1807, 0.2294);
    }

    private void renderRotPart(String modelName, String partName, float rot, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(rot, -1.0F, 0.0F, 0.0F);
        GL11.glTranslated(-x, -y, -z);
        MCH_ModelManager.renderPart(modelName, partName);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(@NotNull MCH_EntityUavStation entity) {
        return TEX_DEFAULT;
    }
}
