package com.norwood.mcheli.plane;

import java.util.Iterator;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_RenderAircraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCP_RenderPlane extends MCH_RenderAircraft<MCP_EntityPlane> {
   public static final IRenderFactory<MCP_EntityPlane> FACTORY = MCP_RenderPlane::new;

   public MCP_RenderPlane(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 2.0F;
   }

   public void renderAircraft(MCH_EntityAircraft entity, double posX, double posY, double posZ, float yaw, float pitch, float roll, float tickTime) {
      MCP_PlaneInfo planeInfo = null;
      if (entity != null && entity instanceof MCP_EntityPlane) {
         MCP_EntityPlane plane = (MCP_EntityPlane)entity;
         planeInfo = plane.getPlaneInfo();
         if (planeInfo != null) {
            posY += 0.3499999940395355D;
            this.renderDebugHitBox(plane, posX, posY, posZ, yaw, pitch);
            this.renderDebugPilotSeat(plane, posX, posY, posZ, yaw, pitch, roll);
            GL11.glTranslated(posX, posY, posZ);
            GL11.glRotatef(yaw, 0.0F, -1.0F, 0.0F);
            GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(roll, 0.0F, 0.0F, 1.0F);
            this.bindTexture("textures/planes/" + plane.getTextureName() + ".png", plane);
            if (planeInfo.haveNozzle() && plane.partNozzle != null) {
               this.renderNozzle(plane, planeInfo, tickTime);
            }

            if (planeInfo.haveWing() && plane.partWing != null) {
               this.renderWing(plane, planeInfo, tickTime);
            }

            if (planeInfo.haveRotor() && plane.partNozzle != null) {
               this.renderRotor(plane, planeInfo, tickTime);
            }

            renderBody(planeInfo.model);
         }
      }
   }

   public void renderRotor(MCP_EntityPlane plane, MCP_PlaneInfo planeInfo, float tickTime) {
      float rot = plane.getNozzleRotation();
      float prevRot = plane.getPrevNozzleRotation();
      Iterator var6 = planeInfo.rotorList.iterator();

      while(var6.hasNext()) {
         MCP_PlaneInfo.Rotor r = (MCP_PlaneInfo.Rotor)var6.next();
         GL11.glPushMatrix();
         GL11.glTranslated(r.pos.x, r.pos.y, r.pos.z);
         GL11.glRotatef((prevRot + (rot - prevRot) * tickTime) * r.maxRotFactor, (float)r.rot.x, (float)r.rot.y, (float)r.rot.z);
         GL11.glTranslated(-r.pos.x, -r.pos.y, -r.pos.z);
         renderPart(r.model, planeInfo.model, r.modelName);
         Iterator var8 = r.blades.iterator();

         while(var8.hasNext()) {
            MCP_PlaneInfo.Blade b = (MCP_PlaneInfo.Blade)var8.next();
            float br = plane.prevRotationRotor;
            br += (plane.rotationRotor - plane.prevRotationRotor) * tickTime;
            GL11.glPushMatrix();
            GL11.glTranslated(b.pos.x, b.pos.y, b.pos.z);
            GL11.glRotatef(br, (float)b.rot.x, (float)b.rot.y, (float)b.rot.z);
            GL11.glTranslated(-b.pos.x, -b.pos.y, -b.pos.z);

            for(int i = 0; i < b.numBlade; ++i) {
               GL11.glTranslated(b.pos.x, b.pos.y, b.pos.z);
               GL11.glRotatef((float)b.rotBlade, (float)b.rot.x, (float)b.rot.y, (float)b.rot.z);
               GL11.glTranslated(-b.pos.x, -b.pos.y, -b.pos.z);
               renderPart(b.model, planeInfo.model, b.modelName);
            }

            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
      }

   }

   public void renderWing(MCP_EntityPlane plane, MCP_PlaneInfo planeInfo, float tickTime) {
      float rot = plane.getWingRotation();
      float prevRot = plane.getPrevWingRotation();

      for(Iterator var6 = planeInfo.wingList.iterator(); var6.hasNext(); GL11.glPopMatrix()) {
         MCP_PlaneInfo.Wing w = (MCP_PlaneInfo.Wing)var6.next();
         GL11.glPushMatrix();
         GL11.glTranslated(w.pos.x, w.pos.y, w.pos.z);
         GL11.glRotatef((prevRot + (rot - prevRot) * tickTime) * w.maxRotFactor, (float)w.rot.x, (float)w.rot.y, (float)w.rot.z);
         GL11.glTranslated(-w.pos.x, -w.pos.y, -w.pos.z);
         renderPart(w.model, planeInfo.model, w.modelName);
         if (w.pylonList != null) {
            Iterator var8 = w.pylonList.iterator();

            while(var8.hasNext()) {
               MCP_PlaneInfo.Pylon p = (MCP_PlaneInfo.Pylon)var8.next();
               GL11.glPushMatrix();
               GL11.glTranslated(p.pos.x, p.pos.y, p.pos.z);
               GL11.glRotatef((prevRot + (rot - prevRot) * tickTime) * p.maxRotFactor, (float)p.rot.x, (float)p.rot.y, (float)p.rot.z);
               GL11.glTranslated(-p.pos.x, -p.pos.y, -p.pos.z);
               renderPart(p.model, planeInfo.model, p.modelName);
               GL11.glPopMatrix();
            }
         }
      }

   }

   public void renderNozzle(MCP_EntityPlane plane, MCP_PlaneInfo planeInfo, float tickTime) {
      float rot = plane.getNozzleRotation();
      float prevRot = plane.getPrevNozzleRotation();
      Iterator var6 = planeInfo.nozzles.iterator();

      while(var6.hasNext()) {
         MCH_AircraftInfo.DrawnPart n = (MCH_AircraftInfo.DrawnPart)var6.next();
         GL11.glPushMatrix();
         GL11.glTranslated(n.pos.x, n.pos.y, n.pos.z);
         GL11.glRotatef(prevRot + (rot - prevRot) * tickTime, (float)n.rot.x, (float)n.rot.y, (float)n.rot.z);
         GL11.glTranslated(-n.pos.x, -n.pos.y, -n.pos.z);
         renderPart(n.model, planeInfo.model, n.modelName);
         GL11.glPopMatrix();
      }

   }

   protected ResourceLocation getEntityTexture(MCP_EntityPlane entity) {
      return TEX_DEFAULT;
   }
}
