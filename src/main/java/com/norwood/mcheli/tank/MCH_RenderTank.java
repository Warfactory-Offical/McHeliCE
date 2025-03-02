package com.norwood.mcheli.tank;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.__helper.MCH_ColorInt;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_RenderAircraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderTank extends MCH_RenderAircraft<MCH_EntityTank> {
   public static final IRenderFactory<MCH_EntityTank> FACTORY = MCH_RenderTank::new;

   public MCH_RenderTank(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 2.0F;
   }

   public void renderAircraft(MCH_EntityAircraft entity, double posX, double posY, double posZ, float yaw, float pitch, float roll, float tickTime) {
      MCH_TankInfo tankInfo = null;
      if (entity != null && entity instanceof MCH_EntityTank) {
         MCH_EntityTank tank = (MCH_EntityTank)entity;
         tankInfo = tank.getTankInfo();
         if (tankInfo != null) {
            posY += 0.3499999940395355D;
            this.renderWheel(tank, posX, posY, posZ);
            this.renderDebugHitBox(tank, posX, posY, posZ, yaw, pitch);
            this.renderDebugPilotSeat(tank, posX, posY, posZ, yaw, pitch, roll);
            GL11.glTranslated(posX, posY, posZ);
            GL11.glRotatef(yaw, 0.0F, -1.0F, 0.0F);
            GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(roll, 0.0F, 0.0F, 1.0F);
            this.bindTexture("textures/tanks/" + tank.getTextureName() + ".png", tank);
            renderBody(tankInfo.model);
         }
      }
   }

   public void renderWheel(MCH_EntityTank tank, double posX, double posY, double posZ) {
      if (MCH_Config.TestMode.prmBool) {
         if (debugModel != null) {
            GL11.glColor4f(0.75F, 0.75F, 0.75F, 0.5F);
            MCH_EntityWheel[] var8 = tank.WheelMng.wheels;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               MCH_EntityWheel w = var8[var10];
               GL11.glPushMatrix();
               GL11.glTranslated(w.posX - tank.posX + posX, w.posY - tank.posY + posY + 0.25D, w.posZ - tank.posZ + posZ);
               GL11.glScalef(w.field_70130_N, w.field_70131_O / 2.0F, w.field_70130_N);
               this.bindTexture("textures/seat_pilot.png");
               debugModel.renderAll();
               GL11.glPopMatrix();
            }

            GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            builder.begin(1, DefaultVertexFormats.field_181706_f);
            Vec3d wp = tank.getTransformedPosition(tank.WheelMng.weightedCenter);
            wp = wp.func_178786_a(tank.posX, tank.posY, tank.posZ);

            for(int i = 0; i < tank.WheelMng.wheels.length / 2; ++i) {
               MCH_ColorInt cint = new MCH_ColorInt((i & 4) > 0 ? 16711680 : 0, (i & 2) > 0 ? '\uff00' : 0, (i & 1) > 0 ? 255 : 0, 192);
               MCH_EntityWheel w1 = tank.WheelMng.wheels[i * 2 + 0];
               MCH_EntityWheel w2 = tank.WheelMng.wheels[i * 2 + 1];
               if (w1.isPlus) {
                  builder.pos(w2.posX - tank.posX + posX, w2.posY - tank.posY + posY, w2.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(w1.posX - tank.posX + posX, w1.posY - tank.posY + posY, w1.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(w1.posX - tank.posX + posX, w1.posY - tank.posY + posY, w1.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(posX + wp.x, posY + wp.y, posZ + wp.z).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(posX + wp.x, posY + wp.y, posZ + wp.z).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(w2.posX - tank.posX + posX, w2.posY - tank.posY + posY, w2.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
               } else {
                  builder.pos(w1.posX - tank.posX + posX, w1.posY - tank.posY + posY, w1.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(w2.posX - tank.posX + posX, w2.posY - tank.posY + posY, w2.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(w2.posX - tank.posX + posX, w2.posY - tank.posY + posY, w2.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(posX + wp.x, posY + wp.y, posZ + wp.z).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(posX + wp.x, posY + wp.y, posZ + wp.z).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
                  builder.pos(w1.posX - tank.posX + posX, w1.posY - tank.posY + posY, w1.posZ - tank.posZ + posZ).func_181669_b(cint.r, cint.g, cint.b, cint.a).func_181675_d();
               }
            }

            tessellator.draw();
         }
      }
   }

   protected ResourceLocation getEntityTexture(MCH_EntityTank entity) {
      return TEX_DEFAULT;
   }
}
