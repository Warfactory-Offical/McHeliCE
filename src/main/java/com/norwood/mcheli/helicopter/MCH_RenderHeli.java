package com.norwood.mcheli.helicopter;

import com.norwood.mcheli.aircraft.MCH_Blade;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_RenderAircraft;
import com.norwood.mcheli.aircraft.MCH_Rotor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderHeli extends MCH_RenderAircraft<MCH_EntityHeli> {
    public static final IRenderFactory<MCH_EntityHeli> FACTORY = MCH_RenderHeli::new;

    public MCH_RenderHeli(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 2.0F;
    }

    @Override
    public void renderAircraft(MCH_EntityAircraft entity, double posX, double posY, double posZ, float yaw, float pitch, float roll, float tickTime) {
        MCH_HeliInfo heliInfo = null;
        if (entity != null && entity instanceof MCH_EntityHeli) {
            MCH_EntityHeli heli = (MCH_EntityHeli) entity;
            heliInfo = heli.getHeliInfo();
            if (heliInfo != null) {
                posY += 0.35F;
                this.renderDebugHitBox(heli, posX, posY, posZ, yaw, pitch);
                this.renderDebugPilotSeat(heli, posX, posY, posZ, yaw, pitch, roll);
                GL11.glTranslated(posX, posY, posZ);
                GL11.glRotatef(yaw, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(roll, 0.0F, 0.0F, 1.0F);
                this.bindTexture("textures/helicopters/" + heli.getTextureName() + ".png", heli);
                renderBody(heliInfo.model);
                this.drawModelBlade(heli, heliInfo, tickTime);
            }
        }
    }

    public void drawModelBlade(MCH_EntityHeli heli, MCH_HeliInfo info, float tickTime) {
        for (int i = 0; i < heli.rotors.length && i < info.rotorList.size(); i++) {
            MCH_HeliInfo.Rotor rotorInfo = info.rotorList.get(i);
            MCH_Rotor rotor = heli.rotors[i];
            GL11.glPushMatrix();
            if (rotorInfo.oldRenderMethod) {
                GL11.glTranslated(rotorInfo.pos.x, rotorInfo.pos.y, rotorInfo.pos.z);
            }

            for (MCH_Blade b : rotor.blades) {
                GL11.glPushMatrix();
                float rot = b.getRotation();
                float prevRot = b.getPrevRotation();
                if (rot - prevRot < -180.0F) {
                    prevRot -= 360.0F;
                } else if (prevRot - rot < -180.0F) {
                    prevRot += 360.0F;
                }

                if (!rotorInfo.oldRenderMethod) {
                    GL11.glTranslated(rotorInfo.pos.x, rotorInfo.pos.y, rotorInfo.pos.z);
                }

                GL11.glRotatef(prevRot + (rot - prevRot) * tickTime, (float) rotorInfo.rot.x, (float) rotorInfo.rot.y, (float) rotorInfo.rot.z);
                if (!rotorInfo.oldRenderMethod) {
                    GL11.glTranslated(-rotorInfo.pos.x, -rotorInfo.pos.y, -rotorInfo.pos.z);
                }

                renderPart(rotorInfo.model, info.model, rotorInfo.modelName);
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(MCH_EntityHeli entity) {
        return TEX_DEFAULT;
    }
}
