package com.norwood.mcheli.aircraft;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_ClientCommonTickHandler;
import com.norwood.mcheli.MCH_ClientEventHook;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.MCH_ColorInt;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.__helper.client.renderer.MCH_Verts;
import com.norwood.mcheli.debug._v3.WeaponPointRenderer;
import com.norwood.mcheli.gui.MCH_Gui;
import com.norwood.mcheli.lweapon.MCH_ClientLightWeaponTickHandler;
import com.norwood.mcheli.multiplay.MCH_GuiTargetMarker;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.weapon.MCH_WeaponGuidanceSystem;
import com.norwood.mcheli.weapon.MCH_WeaponSet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_EntityRenderer;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_Render;
import com.norwood.mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public abstract class MCH_RenderAircraft<T extends MCH_EntityAircraft> extends W_Render<T> {
   public static boolean renderingEntity = false;
   public static _IModelCustom debugModel = null;

   protected MCH_RenderAircraft(RenderManager renderManager) {
      super(renderManager);
   }

   public void doRender(T entity, double posX, double posY, double posZ, float par8, float tickTime) {
      MCH_AircraftInfo info = entity.getAcInfo();
      if (info != null) {
         GL11.glPushMatrix();
         float yaw = this.calcRot(entity.getRotYaw(), entity.field_70126_B, tickTime);
         float pitch = entity.calcRotPitch(tickTime);
         float roll = this.calcRot(entity.getRotRoll(), entity.prevRotationRoll, tickTime);
         if (MCH_Config.EnableModEntityRender.prmBool) {
            this.renderRiddenEntity(entity, tickTime, yaw, pitch + info.entityPitch, roll + info.entityRoll, info.entityWidth, info.entityHeight);
         }

         if (!shouldSkipRender(entity)) {
            this.setCommonRenderParam(info.smoothShading, entity.func_70070_b());
            if (entity.isDestroyed()) {
               GL11.glColor4f(0.15F, 0.15F, 0.15F, 1.0F);
            } else {
               GL11.glColor4f(0.75F, 0.75F, 0.75F, (float)MCH_Config.__TextureAlpha.prmDouble);
            }

            this.renderAircraft(entity, posX, posY, posZ, yaw, pitch, roll, tickTime);
            this.renderCommonPart(entity, info, posX, posY, posZ, tickTime);
            renderLight(posX, posY, posZ, tickTime, entity, info);
            this.restoreCommonRenderParam();
         }

         GL11.glPopMatrix();
         MCH_GuiTargetMarker.addMarkEntityPos(1, entity, posX, posY + (double)info.markerHeight, posZ);
         MCH_ClientLightWeaponTickHandler.markEntity(entity, posX, posY, posZ);
         renderEntityMarker(entity);
         if (MCH_Config.TestMode.prmBool) {
            WeaponPointRenderer.renderWeaponPoints(entity, info, posX, posY, posZ);
         }
      }

   }

   public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
      return true;
   }

   public static boolean shouldSkipRender(Entity entity) {
      if (entity instanceof MCH_IEntityCanRideAircraft) {
         MCH_IEntityCanRideAircraft e = (MCH_IEntityCanRideAircraft)entity;
         if (e.isSkipNormalRender()) {
            return !renderingEntity;
         }
      } else if ((entity.getClass().toString().indexOf("flansmod.common.driveables.EntityPlane") > 0 || entity.getClass().toString().indexOf("flansmod.common.driveables.EntityVehicle") > 0) && entity.func_184187_bx() instanceof MCH_EntitySeat) {
         return !renderingEntity;
      }

      return false;
   }

   public void func_76979_b(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
      if (entity.func_90999_ad()) {
         this.renderEntityOnFire(entity, x, y, z, partialTicks);
      }

   }

   private void renderEntityOnFire(Entity entity, double x, double y, double z, float tick) {
      GL11.glDisable(2896);
      TextureMap texturemap = Minecraft.getMinecraft().func_147117_R();
      TextureAtlasSprite textureatlassprite = texturemap.func_110572_b("minecraft:blocks/fire_layer_0");
      TextureAtlasSprite textureatlassprite1 = texturemap.func_110572_b("minecraft:blocks/fire_layer_1");
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x, (float)y, (float)z);
      float f1 = entity.field_70130_N * 1.4F;
      GL11.glScalef(f1 * 2.0F, f1 * 2.0F, f1 * 2.0F);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      float f2 = 1.5F;
      float f3 = 0.0F;
      float f4 = entity.field_70131_O / f1;
      float f5 = (float)(entity.posY + entity.func_174813_aQ().field_72338_b);
      GL11.glRotatef(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)((int)f4) * 0.02F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float f6 = 0.0F;
      int i = 0;
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

      while(f4 > 0.0F) {
         TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
         this.func_110776_a(TextureMap.field_110575_b);
         float f7 = textureatlassprite2.func_94209_e();
         float f8 = textureatlassprite2.func_94206_g();
         float f9 = textureatlassprite2.func_94212_f();
         float f10 = textureatlassprite2.func_94210_h();
         if (i / 2 % 2 == 0) {
            float f11 = f9;
            f9 = f7;
            f7 = f11;
         }

         bufferbuilder.pos((double)(f2 - f3), (double)(0.0F - f5), (double)f6).func_187315_a((double)f9, (double)f10).func_181675_d();
         bufferbuilder.pos((double)(-f2 - f3), (double)(0.0F - f5), (double)f6).func_187315_a((double)f7, (double)f10).func_181675_d();
         bufferbuilder.pos((double)(-f2 - f3), (double)(1.4F - f5), (double)f6).func_187315_a((double)f7, (double)f8).func_181675_d();
         bufferbuilder.pos((double)(f2 - f3), (double)(1.4F - f5), (double)f6).func_187315_a((double)f9, (double)f8).func_181675_d();
         f4 -= 0.45F;
         f5 -= 0.45F;
         f2 *= 0.9F;
         f6 += 0.03F;
         ++i;
      }

      tessellator.draw();
      GL11.glPopMatrix();
      GL11.glEnable(2896);
   }

   public static void renderLight(double x, double y, double z, float tickTime, MCH_EntityAircraft ac, MCH_AircraftInfo info) {
      if (ac.haveSearchLight()) {
         if (ac.isSearchLightON()) {
            Entity entity = ac.getEntityBySeatId(1);
            if (entity != null) {
               ac.lastSearchLightYaw = entity.field_70177_z;
               ac.lastSearchLightPitch = entity.field_70125_A;
            } else {
               entity = ac.getEntityBySeatId(0);
               if (entity != null) {
                  ac.lastSearchLightYaw = entity.field_70177_z;
                  ac.lastSearchLightPitch = entity.field_70125_A;
               }
            }

            float yaw = ac.lastSearchLightYaw;
            float pitch = ac.lastSearchLightPitch;
            RenderHelper.func_74518_a();
            GL11.glDisable(3553);
            GL11.glShadeModel(7425);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(3008);
            GL11.glDisable(2884);
            GL11.glDepthMask(false);
            float rot = ac.prevRotYawWheel + (ac.rotYawWheel - ac.prevRotYawWheel) * tickTime;
            Iterator var13 = info.searchLights.iterator();

            while(var13.hasNext()) {
               MCH_AircraftInfo.SearchLight sl = (MCH_AircraftInfo.SearchLight)var13.next();
               GL11.glPushMatrix();
               GL11.glTranslated(sl.pos.x, sl.pos.y, sl.pos.z);
               float height;
               if (!sl.fixDir) {
                  GL11.glRotatef(yaw - ac.getRotYaw() + sl.yaw, 0.0F, -1.0F, 0.0F);
                  GL11.glRotatef(pitch + 90.0F - ac.getRotPitch() + sl.pitch, 1.0F, 0.0F, 0.0F);
               } else {
                  height = 0.0F;
                  if (sl.steering) {
                     height = -rot * sl.stRot;
                  }

                  GL11.glRotatef(0.0F + sl.yaw + height, 0.0F, -1.0F, 0.0F);
                  GL11.glRotatef(90.0F + sl.pitch, 1.0F, 0.0F, 0.0F);
               }

               height = sl.height;
               float width = sl.width / 2.0F;
               Tessellator tessellator = Tessellator.getInstance();
               BufferBuilder builder = tessellator.getBuffer();
               builder.begin(6, DefaultVertexFormats.field_181706_f);
               MCH_ColorInt cs = new MCH_ColorInt(sl.colorStart);
               MCH_ColorInt ce = new MCH_ColorInt(sl.colorEnd);
               builder.pos(0.0D, 0.0D, 0.0D).func_181669_b(cs.r, cs.g, cs.b, cs.a).func_181675_d();

               for(int i = 0; i < 25; ++i) {
                  float angle = (float)(15.0D * (double)i / 180.0D * 3.141592653589793D);
                  builder.pos((double)(MathHelper.func_76126_a(angle) * width), (double)height, (double)(MathHelper.func_76134_b(angle) * width)).func_181669_b(ce.r, ce.g, ce.b, ce.a).func_181675_d();
               }

               tessellator.draw();
               GL11.glPopMatrix();
            }

            GL11.glDepthMask(true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glBlendFunc(770, 771);
            RenderHelper.func_74519_b();
         }
      }
   }

   protected void bindTexture(String path, MCH_EntityAircraft ac) {
      if (ac == MCH_ClientCommonTickHandler.ridingAircraft) {
         int bk = MCH_ClientCommonTickHandler.cameraMode;
         MCH_ClientCommonTickHandler.cameraMode = 0;
         super.func_110776_a(MCH_Utils.suffix(path));
         MCH_ClientCommonTickHandler.cameraMode = bk;
      } else {
         super.func_110776_a(MCH_Utils.suffix(path));
      }

   }

   public void renderRiddenEntity(MCH_EntityAircraft ac, float tickTime, float yaw, float pitch, float roll, float width, float height) {
      MCH_ClientEventHook.setCancelRender(false);
      GL11.glPushMatrix();
      this.renderEntitySimple(ac, ac.getRiddenByEntity(), tickTime, yaw, pitch, roll, width, height);
      MCH_EntitySeat[] var8 = ac.getSeats();
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         MCH_EntitySeat s = var8[var10];
         if (s != null) {
            this.renderEntitySimple(ac, s.getRiddenByEntity(), tickTime, yaw, pitch, roll, width, height);
         }
      }

      GL11.glPopMatrix();
      MCH_ClientEventHook.setCancelRender(true);
   }

   public void renderEntitySimple(MCH_EntityAircraft ac, Entity entity, float tickTime, float yaw, float pitch, float roll, float width, float height) {
      if (entity != null) {
         boolean isPilot = ac.isPilot(entity);
         boolean isClientPlayer = W_Lib.isClientPlayer(entity);
         if (!isClientPlayer || !W_Lib.isFirstPerson() || isClientPlayer && isPilot && ac.getCameraId() > 0) {
            GL11.glPushMatrix();
            if (entity.field_70173_aa == 0) {
               entity.field_70142_S = entity.posX;
               entity.field_70137_T = entity.posY;
               entity.field_70136_U = entity.posZ;
            }

            double x = entity.field_70142_S + (entity.posX - entity.field_70142_S) * (double)tickTime;
            double y = entity.field_70137_T + (entity.posY - entity.field_70137_T) * (double)tickTime;
            double z = entity.field_70136_U + (entity.posZ - entity.field_70136_U) * (double)tickTime;
            float f1 = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * tickTime;
            int i = entity.func_70070_b();
            if (entity.func_70027_ad()) {
               i = 15728880;
            }

            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            double dx = x - TileEntityRendererDispatcher.field_147554_b;
            double dy = y - TileEntityRendererDispatcher.field_147555_c;
            double dz = z - TileEntityRendererDispatcher.field_147552_d;
            GL11.glTranslated(dx, dy, dz);
            GL11.glRotatef(yaw, 0.0F, -1.0F, 0.0F);
            GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(roll, 0.0F, 0.0F, 1.0F);
            GL11.glScaled((double)width, (double)height, (double)width);
            GL11.glRotatef(-yaw, 0.0F, -1.0F, 0.0F);
            GL11.glTranslated(-dx, -dy, -dz);
            boolean bk = renderingEntity;
            renderingEntity = true;
            Entity ridingEntity = entity.func_184187_bx();
            if (!W_Lib.isEntityLivingBase(entity) && !(entity instanceof MCH_IEntityCanRideAircraft)) {
               entity.func_184210_p();
            }

            EntityLivingBase entityLiving = entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null;
            float bkPitch = 0.0F;
            float bkPrevPitch = 0.0F;
            if (isPilot && entityLiving != null) {
               entityLiving.field_70761_aq = ac.getRotYaw();
               entityLiving.field_70760_ar = ac.getRotYaw();
               if (ac.getCameraId() > 0) {
                  entityLiving.field_70759_as = ac.getRotYaw();
                  entityLiving.field_70758_at = ac.getRotYaw();
                  bkPitch = entityLiving.field_70125_A;
                  bkPrevPitch = entityLiving.field_70127_C;
                  entityLiving.field_70125_A = ac.getRotPitch();
                  entityLiving.field_70127_C = ac.getRotPitch();
               }
            }

            if (isClientPlayer) {
               Entity viewEntity = this.field_76990_c.field_78734_h;
               this.field_76990_c.field_78734_h = entity;
               W_EntityRenderer.renderEntityWithPosYaw(this.field_76990_c, entity, dx, dy, dz, f1, tickTime, false);
               this.field_76990_c.field_78734_h = viewEntity;
            } else {
               W_EntityRenderer.renderEntityWithPosYaw(this.field_76990_c, entity, dx, dy, dz, f1, tickTime, false);
            }

            if (isPilot && entityLiving != null && ac.getCameraId() > 0) {
               entityLiving.field_70125_A = bkPitch;
               entityLiving.field_70127_C = bkPrevPitch;
            }

            entity.func_184220_m(ridingEntity);
            renderingEntity = bk;
            GL11.glPopMatrix();
         }
      }

   }

   public static void Test_Material(int light, float a, float b, float c) {
      GL11.glMaterial(1032, light, setColorBuffer(a, b, c, 1.0F));
   }

   public static void Test_Light(int light, float a, float b, float c) {
      GL11.glLight(16384, light, setColorBuffer(a, b, c, 1.0F));
      GL11.glLight(16385, light, setColorBuffer(a, b, c, 1.0F));
   }

   public abstract void renderAircraft(MCH_EntityAircraft var1, double var2, double var4, double var6, float var8, float var9, float var10, float var11);

   public float calcRot(float rot, float prevRot, float tickTime) {
      rot = MathHelper.func_76142_g(rot);
      prevRot = MathHelper.func_76142_g(prevRot);
      if (rot - prevRot < -180.0F) {
         prevRot -= 360.0F;
      } else if (prevRot - rot < -180.0F) {
         prevRot += 360.0F;
      }

      return prevRot + (rot - prevRot) * tickTime;
   }

   public void renderDebugHitBox(MCH_EntityAircraft e, double x, double y, double z, float yaw, float pitch) {
      if (MCH_Config.TestMode.prmBool && debugModel != null) {
         GL11.glPushMatrix();
         GL11.glTranslated(x, y, z);
         GL11.glScalef(e.field_70130_N, e.field_70131_O, e.field_70130_N);
         this.bindTexture("textures/hit_box.png");
         debugModel.renderAll();
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslated(x, y, z);
         MCH_BoundingBox[] var10 = e.extraBoundingBox;
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            MCH_BoundingBox bb = var10[var12];
            GL11.glPushMatrix();
            GL11.glTranslated(bb.rotatedOffset.x, bb.rotatedOffset.y, bb.rotatedOffset.z);
            GL11.glPushMatrix();
            GL11.glScalef(bb.width, bb.height, bb.width);
            this.bindTexture("textures/bounding_box.png");
            debugModel.renderAll();
            GL11.glPopMatrix();
            this.drawHitBoxDetail(bb);
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
      }

   }

   public void drawHitBoxDetail(MCH_BoundingBox bb) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float f1 = 0.080000006F;
      String s = String.format("%.2f", bb.damegeFactor);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.5F + (float)(bb.offsetY * 0.0D + (double)bb.height), 0.0F);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(-f1, -f1, f1);
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      OpenGlHelper.func_148821_a(770, 771, 1, 0);
      GL11.glDisable(3553);
      FontRenderer fontrenderer = this.func_76983_a();
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(7, DefaultVertexFormats.field_181706_f);
      int i = fontrenderer.func_78256_a(s) / 2;
      builder.pos((double)(-i - 1), -1.0D, 0.1D).func_181666_a(0.0F, 0.0F, 0.0F, 0.4F).func_181675_d();
      builder.pos((double)(-i - 1), 8.0D, 0.1D).func_181666_a(0.0F, 0.0F, 0.0F, 0.4F).func_181675_d();
      builder.pos((double)(i + 1), 8.0D, 0.1D).func_181666_a(0.0F, 0.0F, 0.0F, 0.4F).func_181675_d();
      builder.pos((double)(i + 1), -1.0D, 0.1D).func_181666_a(0.0F, 0.0F, 0.0F, 0.4F).func_181675_d();
      tessellator.draw();
      GL11.glEnable(3553);
      GL11.glDepthMask(false);
      int color = bb.damegeFactor > 1.0F ? 16711680 : (bb.damegeFactor < 1.0F ? '\uffff' : 16777215);
      fontrenderer.func_78276_b(s, -fontrenderer.func_78256_a(s) / 2, 0, -1073741824 | color);
      GL11.glDepthMask(true);
      GL11.glEnable(2896);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public void renderDebugPilotSeat(MCH_EntityAircraft e, double x, double y, double z, float yaw, float pitch, float roll) {
      if (MCH_Config.TestMode.prmBool && debugModel != null) {
         GL11.glPushMatrix();
         MCH_SeatInfo seat = e.getSeatInfo(0);
         GL11.glTranslated(x, y, z);
         GL11.glRotatef(yaw, 0.0F, -1.0F, 0.0F);
         GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(roll, 0.0F, 0.0F, 1.0F);
         GL11.glTranslated(seat.pos.x, seat.pos.y, seat.pos.z);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         this.bindTexture("textures/seat_pilot.png");
         debugModel.renderAll();
         GL11.glPopMatrix();
      }

   }

   public static void renderBody(@Nullable _IModelCustom model) {
      if (model != null) {
         if (model instanceof W_ModelCustom) {
            if (((W_ModelCustom)model).containsPart("$body")) {
               model.renderPart("$body");
            } else {
               model.renderAll();
            }
         } else {
            model.renderAll();
         }
      }

   }

   public static void renderPart(@Nullable _IModelCustom model, @Nullable _IModelCustom modelBody, String partName) {
      if (model != null) {
         model.renderAll();
      } else if (modelBody instanceof W_ModelCustom && ((W_ModelCustom)modelBody).containsPart("$" + partName)) {
         modelBody.renderPart("$" + partName);
      }

   }

   public void renderCommonPart(MCH_EntityAircraft ac, MCH_AircraftInfo info, double x, double y, double z, float tickTime) {
      renderRope(ac, info, x, y, z, tickTime);
      renderWeapon(ac, info, tickTime);
      renderRotPart(ac, info, tickTime);
      renderHatch(ac, info, tickTime);
      renderTrackRoller(ac, info, tickTime);
      renderCrawlerTrack(ac, info, tickTime);
      renderSteeringWheel(ac, info, tickTime);
      renderLightHatch(ac, info, tickTime);
      renderWheel(ac, info, tickTime);
      renderThrottle(ac, info, tickTime);
      renderCamera(ac, info, tickTime);
      renderLandingGear(ac, info, tickTime);
      renderWeaponBay(ac, info, tickTime);
      renderCanopy(ac, info, tickTime);
   }

   public static void renderLightHatch(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.lightHatchList.size() > 0) {
         float rot = ac.prevRotLightHatch + (ac.rotLightHatch - ac.prevRotLightHatch) * tickTime;
         Iterator var4 = info.lightHatchList.iterator();

         while(var4.hasNext()) {
            MCH_AircraftInfo.Hatch t = (MCH_AircraftInfo.Hatch)var4.next();
            GL11.glPushMatrix();
            GL11.glTranslated(t.pos.x, t.pos.y, t.pos.z);
            GL11.glRotated((double)(rot * t.maxRot), t.rot.x, t.rot.y, t.rot.z);
            GL11.glTranslated(-t.pos.x, -t.pos.y, -t.pos.z);
            renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderSteeringWheel(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.partSteeringWheel.size() > 0) {
         float rot = ac.prevRotYawWheel + (ac.rotYawWheel - ac.prevRotYawWheel) * tickTime;
         Iterator var4 = info.partSteeringWheel.iterator();

         while(var4.hasNext()) {
            MCH_AircraftInfo.PartWheel t = (MCH_AircraftInfo.PartWheel)var4.next();
            GL11.glPushMatrix();
            GL11.glTranslated(t.pos.x, t.pos.y, t.pos.z);
            GL11.glRotated((double)(rot * t.rotDir), t.rot.x, t.rot.y, t.rot.z);
            GL11.glTranslated(-t.pos.x, -t.pos.y, -t.pos.z);
            renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderWheel(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.partWheel.size() > 0) {
         float yaw = ac.prevRotYawWheel + (ac.rotYawWheel - ac.prevRotYawWheel) * tickTime;
         Iterator var4 = info.partWheel.iterator();

         while(var4.hasNext()) {
            MCH_AircraftInfo.PartWheel t = (MCH_AircraftInfo.PartWheel)var4.next();
            GL11.glPushMatrix();
            GL11.glTranslated(t.pos2.x, t.pos2.y, t.pos2.z);
            GL11.glRotated((double)(yaw * t.rotDir), t.rot.x, t.rot.y, t.rot.z);
            GL11.glTranslated(-t.pos2.x, -t.pos2.y, -t.pos2.z);
            GL11.glTranslated(t.pos.x, t.pos.y, t.pos.z);
            GL11.glRotatef(ac.prevRotWheel + (ac.rotWheel - ac.prevRotWheel) * tickTime, 1.0F, 0.0F, 0.0F);
            GL11.glTranslated(-t.pos.x, -t.pos.y, -t.pos.z);
            renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderRotPart(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (ac.haveRotPart()) {
         for(int i = 0; i < ac.rotPartRotation.length; ++i) {
            float rot = ac.rotPartRotation[i];
            float prevRot = ac.prevRotPartRotation[i];
            if (prevRot > rot) {
               rot += 360.0F;
            }

            rot = MCH_Lib.smooth(rot, prevRot, tickTime);
            MCH_AircraftInfo.RotPart h = (MCH_AircraftInfo.RotPart)info.partRotPart.get(i);
            GL11.glPushMatrix();
            GL11.glTranslated(h.pos.x, h.pos.y, h.pos.z);
            GL11.glRotatef(rot, (float)h.rot.x, (float)h.rot.y, (float)h.rot.z);
            GL11.glTranslated(-h.pos.x, -h.pos.y, -h.pos.z);
            renderPart(h.model, info.model, h.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderWeapon(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      MCH_WeaponSet beforeWs = null;
      Entity e = ac.getRiddenByEntity();
      int weaponIndex = 0;
      Iterator var6 = info.partWeapon.iterator();

      while(true) {
         MCH_AircraftInfo.PartWeapon w;
         MCH_WeaponSet ws;
         float rotYaw;
         float prevYaw;
         float rotPitch;
         float prevPitch;
         boolean rev_sign;
         int var16;
         while(true) {
            if (!var6.hasNext()) {
               return;
            }

            w = (MCH_AircraftInfo.PartWeapon)var6.next();
            ws = ac.getWeaponByName(w.name[0]);
            if (ws != beforeWs) {
               weaponIndex = 0;
               beforeWs = ws;
            }

            rotYaw = 0.0F;
            prevYaw = 0.0F;
            rotPitch = 0.0F;
            prevPitch = 0.0F;
            if (!w.hideGM || !W_Lib.isFirstPerson()) {
               break;
            }

            if (ws == null) {
               if (ac.isMountedEntity(MCH_Lib.getClientPlayer())) {
                  continue;
               }
               break;
            } else {
               rev_sign = false;
               String[] var14 = w.name;
               int var15 = var14.length;

               for(var16 = 0; var16 < var15; ++var16) {
                  String s = var14[var16];
                  if (W_Lib.isClientPlayer(ac.getWeaponUserByWeaponName(s))) {
                     rev_sign = true;
                     break;
                  }
               }

               if (!rev_sign) {
                  break;
               }
            }
         }

         GL11.glPushMatrix();
         float ty;
         if (w.turret) {
            GL11.glTranslated(info.turretPosition.x, info.turretPosition.y, info.turretPosition.z);
            ty = MCH_Lib.smooth(ac.getLastRiderYaw() - ac.getRotYaw(), ac.prevLastRiderYaw - ac.field_70126_B, tickTime);
            GL11.glRotatef(ty, 0.0F, -1.0F, 0.0F);
            GL11.glTranslated(-info.turretPosition.x, -info.turretPosition.y, -info.turretPosition.z);
         }

         GL11.glTranslated(w.pos.x, w.pos.y, w.pos.z);
         if (w.yaw) {
            if (ws != null) {
               rotYaw = ws.rotationYaw - ws.defaultRotationYaw;
               prevYaw = ws.prevRotationYaw - ws.defaultRotationYaw;
            } else if (e != null) {
               rotYaw = e.field_70177_z - ac.getRotYaw();
               prevYaw = e.field_70126_B - ac.field_70126_B;
            } else {
               rotYaw = ac.getLastRiderYaw() - ac.field_70177_z;
               prevYaw = ac.prevLastRiderYaw - ac.field_70126_B;
            }

            if (rotYaw - prevYaw > 180.0F) {
               prevYaw += 360.0F;
            } else if (rotYaw - prevYaw < -180.0F) {
               prevYaw -= 360.0F;
            }

            GL11.glRotatef(prevYaw + (rotYaw - prevYaw) * tickTime, 0.0F, -1.0F, 0.0F);
         }

         if (w.turret) {
            ty = MCH_Lib.smooth(ac.getLastRiderYaw() - ac.getRotYaw(), ac.prevLastRiderYaw - ac.field_70126_B, tickTime);
            ty -= ws.rotationTurretYaw;
            GL11.glRotatef(-ty, 0.0F, -1.0F, 0.0F);
         }

         rev_sign = false;
         float rotBrl;
         if (ws != null && (int)ws.defaultRotationYaw != 0) {
            rotBrl = MathHelper.func_76142_g(ws.defaultRotationYaw);
            rev_sign = rotBrl >= 45.0F && rotBrl <= 135.0F || rotBrl <= -45.0F && rotBrl >= -135.0F;
            GL11.glRotatef(-ws.defaultRotationYaw, 0.0F, -1.0F, 0.0F);
         }

         if (w.pitch) {
            if (ws != null) {
               rotPitch = ws.rotationPitch;
               prevPitch = ws.prevRotationPitch;
            } else if (e != null) {
               rotPitch = e.field_70125_A;
               prevPitch = e.field_70127_C;
            } else {
               rotPitch = ac.getLastRiderPitch();
               prevPitch = ac.prevLastRiderPitch;
            }

            if (rev_sign) {
               rotPitch = -rotPitch;
               prevPitch = -prevPitch;
            }

            GL11.glRotatef(prevPitch + (rotPitch - prevPitch) * tickTime, 1.0F, 0.0F, 0.0F);
         }

         if (ws != null && w.recoilBuf != 0.0F) {
            MCH_WeaponSet.Recoil r = ws.recoilBuf[0];
            if (w.name.length > 1) {
               String[] var23 = w.name;
               var16 = var23.length;

               for(int var27 = 0; var27 < var16; ++var27) {
                  String wnm = var23[var27];
                  MCH_WeaponSet tws = ac.getWeaponByName(wnm);
                  if (tws != null && tws.recoilBuf[0].recoilBuf > r.recoilBuf) {
                     r = tws.recoilBuf[0];
                  }
               }
            }

            float recoilBuf = r.prevRecoilBuf + (r.recoilBuf - r.prevRecoilBuf) * tickTime;
            GL11.glTranslated(0.0D, 0.0D, (double)(w.recoilBuf * recoilBuf));
         }

         if (ws != null) {
            GL11.glRotatef(ws.defaultRotationYaw, 0.0F, -1.0F, 0.0F);
            if (w.rotBarrel) {
               rotBrl = ws.prevRotBarrel + (ws.rotBarrel - ws.prevRotBarrel) * tickTime;
               GL11.glRotatef(rotBrl, (float)w.rot.x, (float)w.rot.y, (float)w.rot.z);
            }
         }

         GL11.glTranslated(-w.pos.x, -w.pos.y, -w.pos.z);
         if (!w.isMissile || !ac.isWeaponNotCooldown(ws, weaponIndex)) {
            renderPart(w.model, info.model, w.modelName);
            Iterator var25 = w.child.iterator();

            while(var25.hasNext()) {
               MCH_AircraftInfo.PartWeaponChild wc = (MCH_AircraftInfo.PartWeaponChild)var25.next();
               GL11.glPushMatrix();
               renderWeaponChild(ac, info, wc, ws, e, tickTime);
               GL11.glPopMatrix();
            }
         }

         GL11.glPopMatrix();
         ++weaponIndex;
      }
   }

   public static void renderWeaponChild(MCH_EntityAircraft ac, MCH_AircraftInfo info, MCH_AircraftInfo.PartWeaponChild w, MCH_WeaponSet ws, Entity e, float tickTime) {
      float rotYaw = 0.0F;
      float prevYaw = 0.0F;
      float rotPitch = 0.0F;
      float prevPitch = 0.0F;
      GL11.glTranslated(w.pos.x, w.pos.y, w.pos.z);
      if (w.yaw) {
         if (ws != null) {
            rotYaw = ws.rotationYaw - ws.defaultRotationYaw;
            prevYaw = ws.prevRotationYaw - ws.defaultRotationYaw;
         } else if (e != null) {
            rotYaw = e.field_70177_z - ac.getRotYaw();
            prevYaw = e.field_70126_B - ac.field_70126_B;
         } else {
            rotYaw = ac.getLastRiderYaw() - ac.field_70177_z;
            prevYaw = ac.prevLastRiderYaw - ac.field_70126_B;
         }

         if (rotYaw - prevYaw > 180.0F) {
            prevYaw += 360.0F;
         } else if (rotYaw - prevYaw < -180.0F) {
            prevYaw -= 360.0F;
         }

         GL11.glRotatef(prevYaw + (rotYaw - prevYaw) * tickTime, 0.0F, -1.0F, 0.0F);
      }

      boolean rev_sign = false;
      if (ws != null && (int)ws.defaultRotationYaw != 0) {
         float t = MathHelper.func_76142_g(ws.defaultRotationYaw);
         rev_sign = t >= 45.0F && t <= 135.0F || t <= -45.0F && t >= -135.0F;
         GL11.glRotatef(-ws.defaultRotationYaw, 0.0F, -1.0F, 0.0F);
      }

      if (w.pitch) {
         if (ws != null) {
            rotPitch = ws.rotationPitch;
            prevPitch = ws.prevRotationPitch;
         } else if (e != null) {
            rotPitch = e.field_70125_A;
            prevPitch = e.field_70127_C;
         } else {
            rotPitch = ac.getLastRiderPitch();
            prevPitch = ac.prevLastRiderPitch;
         }

         if (rev_sign) {
            rotPitch = -rotPitch;
            prevPitch = -prevPitch;
         }

         GL11.glRotatef(prevPitch + (rotPitch - prevPitch) * tickTime, 1.0F, 0.0F, 0.0F);
      }

      if (ws != null && w.recoilBuf != 0.0F) {
         MCH_WeaponSet.Recoil r = ws.recoilBuf[0];
         if (w.name.length > 1) {
            String[] var12 = w.name;
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               String wnm = var12[var14];
               MCH_WeaponSet tws = ac.getWeaponByName(wnm);
               if (tws != null && tws.recoilBuf[0].recoilBuf > r.recoilBuf) {
                  r = tws.recoilBuf[0];
               }
            }
         }

         float recoilBuf = r.prevRecoilBuf + (r.recoilBuf - r.prevRecoilBuf) * tickTime;
         GL11.glTranslated(0.0D, 0.0D, (double)(-w.recoilBuf * recoilBuf));
      }

      if (ws != null) {
         GL11.glRotatef(ws.defaultRotationYaw, 0.0F, -1.0F, 0.0F);
      }

      GL11.glTranslated(-w.pos.x, -w.pos.y, -w.pos.z);
      renderPart(w.model, info.model, w.modelName);
   }

   public static void renderTrackRoller(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.partTrackRoller.size() > 0) {
         float[] rot = ac.rotTrackRoller;
         float[] prevRot = ac.prevRotTrackRoller;
         Iterator var5 = info.partTrackRoller.iterator();

         while(var5.hasNext()) {
            MCH_AircraftInfo.TrackRoller t = (MCH_AircraftInfo.TrackRoller)var5.next();
            GL11.glPushMatrix();
            GL11.glTranslated(t.pos.x, t.pos.y, t.pos.z);
            GL11.glRotatef(prevRot[t.side] + (rot[t.side] - prevRot[t.side]) * tickTime, 1.0F, 0.0F, 0.0F);
            GL11.glTranslated(-t.pos.x, -t.pos.y, -t.pos.z);
            renderPart(t.model, info.model, t.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderCrawlerTrack(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.partCrawlerTrack.size() > 0) {
         int prevWidth = GL11.glGetInteger(2833);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder builder = tessellator.getBuffer();
         Iterator var6 = info.partCrawlerTrack.iterator();

         while(var6.hasNext()) {
            MCH_AircraftInfo.CrawlerTrack c = (MCH_AircraftInfo.CrawlerTrack)var6.next();
            GL11.glPointSize(c.len * 20.0F);
            int L;
            if (MCH_Config.TestMode.prmBool) {
               GL11.glDisable(3553);
               GL11.glDisable(3042);
               builder.begin(0, DefaultVertexFormats.field_181706_f);

               for(L = 0; L < c.cx.length; ++L) {
                  builder.pos((double)c.z, c.cx[L], c.cy[L]).func_181669_b((int)(255.0F / (float)c.cx.length * (float)L), 80, 255 - (int)(255.0F / (float)c.cx.length * (float)L), 255).func_181675_d();
               }

               tessellator.draw();
            }

            GL11.glEnable(3553);
            GL11.glEnable(3042);
            L = c.lp.size() - 1;
            double rc = ac != null ? (double)ac.rotCrawlerTrack[c.side] : 0.0D;
            double pc = ac != null ? (double)ac.prevRotCrawlerTrack[c.side] : 0.0D;

            for(int i = 0; i < L; ++i) {
               MCH_AircraftInfo.CrawlerTrackPrm cp = (MCH_AircraftInfo.CrawlerTrackPrm)c.lp.get(i);
               MCH_AircraftInfo.CrawlerTrackPrm np = (MCH_AircraftInfo.CrawlerTrackPrm)c.lp.get((i + 1) % L);
               double x1 = (double)cp.x;
               double x2 = (double)np.x;
               double r1 = (double)cp.r;
               double y1 = (double)cp.y;
               double y2 = (double)np.y;
               double r2 = (double)np.r;
               if (r2 - r1 < -180.0D) {
                  r2 += 360.0D;
               }

               if (r2 - r1 > 180.0D) {
                  r2 -= 360.0D;
               }

               double sx = x1 + (x2 - x1) * rc;
               double sy = y1 + (y2 - y1) * rc;
               double sr = r1 + (r2 - r1) * rc;
               double ex = x1 + (x2 - x1) * pc;
               double ey = y1 + (y2 - y1) * pc;
               double er = r1 + (r2 - r1) * pc;
               double x = sx + (ex - sx) * pc;
               double y = sy + (ey - sy) * pc;
               double r = sr + (er - sr) * pc;
               GL11.glPushMatrix();
               GL11.glTranslated(0.0D, x, y);
               GL11.glRotatef((float)r, -1.0F, 0.0F, 0.0F);
               renderPart(c.model, info.model, c.modelName);
               GL11.glPopMatrix();
            }
         }

         GL11.glEnable(3042);
         GL11.glPointSize((float)prevWidth);
      }
   }

   public static void renderHatch(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.haveHatch() && ac.partHatch != null) {
         float rot = ac.getHatchRotation();
         float prevRot = ac.getPrevHatchRotation();
         Iterator var5 = info.hatchList.iterator();

         while(var5.hasNext()) {
            MCH_AircraftInfo.Hatch h = (MCH_AircraftInfo.Hatch)var5.next();
            GL11.glPushMatrix();
            if (h.isSlide) {
               float r = ac.partHatch.rotation / ac.partHatch.rotationMax;
               float pr = ac.partHatch.prevRotation / ac.partHatch.rotationMax;
               float f = pr + (r - pr) * tickTime;
               GL11.glTranslated(h.pos.x * (double)f, h.pos.y * (double)f, h.pos.z * (double)f);
            } else {
               GL11.glTranslated(h.pos.x, h.pos.y, h.pos.z);
               GL11.glRotatef((prevRot + (rot - prevRot) * tickTime) * h.maxRotFactor, (float)h.rot.x, (float)h.rot.y, (float)h.rot.z);
               GL11.glTranslated(-h.pos.x, -h.pos.y, -h.pos.z);
            }

            renderPart(h.model, info.model, h.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderThrottle(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.havePartThrottle()) {
         float throttle = MCH_Lib.smooth((float)ac.getCurrentThrottle(), (float)ac.getPrevCurrentThrottle(), tickTime);
         Iterator var4 = info.partThrottle.iterator();

         while(var4.hasNext()) {
            MCH_AircraftInfo.Throttle h = (MCH_AircraftInfo.Throttle)var4.next();
            GL11.glPushMatrix();
            GL11.glTranslated(h.pos.x, h.pos.y, h.pos.z);
            GL11.glRotatef(throttle * h.rot2, (float)h.rot.x, (float)h.rot.y, (float)h.rot.z);
            GL11.glTranslated(-h.pos.x, -h.pos.y, -h.pos.z);
            GL11.glTranslated(h.slide.x * (double)throttle, h.slide.y * (double)throttle, h.slide.z * (double)throttle);
            renderPart(h.model, info.model, h.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderWeaponBay(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      for(int i = 0; i < info.partWeaponBay.size(); ++i) {
         MCH_AircraftInfo.WeaponBay w = (MCH_AircraftInfo.WeaponBay)info.partWeaponBay.get(i);
         MCH_EntityAircraft.WeaponBay ws = ac.weaponBays[i];
         GL11.glPushMatrix();
         if (w.isSlide) {
            float r = ws.rot / 90.0F;
            float pr = ws.prevRot / 90.0F;
            float f = pr + (r - pr) * tickTime;
            GL11.glTranslated(w.pos.x * (double)f, w.pos.y * (double)f, w.pos.z * (double)f);
         } else {
            GL11.glTranslated(w.pos.x, w.pos.y, w.pos.z);
            GL11.glRotatef((ws.prevRot + (ws.rot - ws.prevRot) * tickTime) * w.maxRotFactor, (float)w.rot.x, (float)w.rot.y, (float)w.rot.z);
            GL11.glTranslated(-w.pos.x, -w.pos.y, -w.pos.z);
         }

         renderPart(w.model, info.model, w.modelName);
         GL11.glPopMatrix();
      }

   }

   public static void renderCamera(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.havePartCamera()) {
         float rotYaw = ac.camera.partRotationYaw;
         float prevRotYaw = ac.camera.prevPartRotationYaw;
         float rotPitch = ac.camera.partRotationPitch;
         float prevRotPitch = ac.camera.prevPartRotationPitch;
         float yaw = prevRotYaw + (rotYaw - prevRotYaw) * tickTime - ac.getRotYaw();
         float pitch = prevRotPitch + (rotPitch - prevRotPitch) * tickTime - ac.getRotPitch();
         Iterator var9 = info.cameraList.iterator();

         while(var9.hasNext()) {
            MCH_AircraftInfo.Camera c = (MCH_AircraftInfo.Camera)var9.next();
            GL11.glPushMatrix();
            GL11.glTranslated(c.pos.x, c.pos.y, c.pos.z);
            if (c.yawSync) {
               GL11.glRotatef(yaw, 0.0F, -1.0F, 0.0F);
            }

            if (c.pitchSync) {
               GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
            }

            GL11.glTranslated(-c.pos.x, -c.pos.y, -c.pos.z);
            renderPart(c.model, info.model, c.modelName);
            GL11.glPopMatrix();
         }

      }
   }

   public static void renderCanopy(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.haveCanopy() && ac.partCanopy != null) {
         float rot = ac.getCanopyRotation();
         float prevRot = ac.getPrevCanopyRotation();
         Iterator var5 = info.canopyList.iterator();

         while(var5.hasNext()) {
            MCH_AircraftInfo.Canopy c = (MCH_AircraftInfo.Canopy)var5.next();
            GL11.glPushMatrix();
            if (c.isSlide) {
               float r = ac.partCanopy.rotation / ac.partCanopy.rotationMax;
               float pr = ac.partCanopy.prevRotation / ac.partCanopy.rotationMax;
               float f = pr + (r - pr) * tickTime;
               GL11.glTranslated(c.pos.x * (double)f, c.pos.y * (double)f, c.pos.z * (double)f);
            } else {
               GL11.glTranslated(c.pos.x, c.pos.y, c.pos.z);
               GL11.glRotatef((prevRot + (rot - prevRot) * tickTime) * c.maxRotFactor, (float)c.rot.x, (float)c.rot.y, (float)c.rot.z);
               GL11.glTranslated(-c.pos.x, -c.pos.y, -c.pos.z);
            }

            renderPart(c.model, info.model, c.modelName);
            GL11.glPopMatrix();
         }
      }

   }

   public static void renderLandingGear(MCH_EntityAircraft ac, MCH_AircraftInfo info, float tickTime) {
      if (info.haveLandingGear() && ac.partLandingGear != null) {
         float rot = ac.getLandingGearRotation();
         float prevRot = ac.getPrevLandingGearRotation();
         float revR = 90.0F - rot;
         float revPr = 90.0F - prevRot;
         float rot1 = prevRot + (rot - prevRot) * tickTime;
         float rot1Rev = revPr + (revR - revPr) * tickTime;
         float rotHatch = 90.0F * MathHelper.func_76126_a(rot1 * 2.0F * 3.1415927F / 180.0F) * 3.0F;
         if (rotHatch > 90.0F) {
            rotHatch = 90.0F;
         }

         Iterator var10 = info.landingGear.iterator();

         while(var10.hasNext()) {
            MCH_AircraftInfo.LandingGear n = (MCH_AircraftInfo.LandingGear)var10.next();
            GL11.glPushMatrix();
            GL11.glTranslated(n.pos.x, n.pos.y, n.pos.z);
            if (!n.reverse) {
               if (!n.hatch) {
                  GL11.glRotatef(rot1 * n.maxRotFactor, (float)n.rot.x, (float)n.rot.y, (float)n.rot.z);
               } else {
                  GL11.glRotatef(rotHatch * n.maxRotFactor, (float)n.rot.x, (float)n.rot.y, (float)n.rot.z);
               }
            } else {
               GL11.glRotatef(rot1Rev * n.maxRotFactor, (float)n.rot.x, (float)n.rot.y, (float)n.rot.z);
            }

            if (n.enableRot2) {
               if (!n.reverse) {
                  GL11.glRotatef(rot1 * n.maxRotFactor2, (float)n.rot2.x, (float)n.rot2.y, (float)n.rot2.z);
               } else {
                  GL11.glRotatef(rot1Rev * n.maxRotFactor2, (float)n.rot2.x, (float)n.rot2.y, (float)n.rot2.z);
               }
            }

            GL11.glTranslated(-n.pos.x, -n.pos.y, -n.pos.z);
            if (n.slide != null) {
               float f = rot / 90.0F;
               if (n.reverse) {
                  f = 1.0F - f;
               }

               GL11.glTranslated((double)f * n.slide.x, (double)f * n.slide.y, (double)f * n.slide.z);
            }

            renderPart(n.model, info.model, n.modelName);
            GL11.glPopMatrix();
         }
      }

   }

   public static void renderEntityMarker(Entity entity) {
      Entity player = Minecraft.getMinecraft().player;
      if (player != null) {
         if (!W_Entity.isEqual(player, entity)) {
            MCH_EntityAircraft ac = null;
            if (player.func_184187_bx() instanceof MCH_EntityAircraft) {
               ac = (MCH_EntityAircraft)player.func_184187_bx();
            } else if (player.func_184187_bx() instanceof MCH_EntitySeat) {
               ac = ((MCH_EntitySeat)player.func_184187_bx()).getParent();
            } else if (player.func_184187_bx() instanceof MCH_EntityUavStation) {
               ac = ((MCH_EntityUavStation)player.func_184187_bx()).getControlAircract();
            }

            if (ac != null) {
               if (!W_Entity.isEqual(ac, entity)) {
                  MCH_WeaponGuidanceSystem gs = ac.getCurrentWeapon(player).getCurrentWeapon().getGuidanceSystem();
                  if (gs != null && gs.canLockEntity(entity)) {
                     RenderManager rm = Minecraft.getMinecraft().func_175598_ae();
                     double dist = entity.func_70068_e(rm.field_78734_h);
                     double x = entity.posX - TileEntityRendererDispatcher.field_147554_b;
                     double y = entity.posY - TileEntityRendererDispatcher.field_147555_c;
                     double z = entity.posZ - TileEntityRendererDispatcher.field_147552_d;
                     if (dist < 10000.0D) {
                        GL11.glPushMatrix();
                        GL11.glTranslatef((float)x, (float)y + entity.field_70131_O + 0.5F, (float)z);
                        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(-rm.field_78735_i, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(rm.field_78732_j, 1.0F, 0.0F, 0.0F);
                        GL11.glScalef(-0.02666667F, -0.02666667F, 0.02666667F);
                        GL11.glDisable(2896);
                        GL11.glTranslatef(0.0F, 9.374999F, 0.0F);
                        GL11.glDepthMask(false);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(3553);
                        int prevWidth = GL11.glGetInteger(2849);
                        float size = Math.max(entity.field_70130_N, entity.field_70131_O) * 20.0F;
                        if (entity instanceof MCH_EntityAircraft) {
                           size *= 2.0F;
                        }

                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder builder = tessellator.getBuffer();
                        builder.begin(2, MCH_Verts.POS_COLOR_LMAP);
                        boolean isLockEntity = gs.isLockingEntity(entity);
                        if (isLockEntity) {
                           GL11.glLineWidth((float)MCH_Gui.scaleFactor * 1.5F);
                           builder.pos((double)(-size - 1.0F), 0.0D, 0.0D).func_181666_a(1.0F, 0.0F, 0.0F, 1.0F).func_187314_a(0, 240).func_181675_d();
                           builder.pos((double)(-size - 1.0F), (double)(size * 2.0F), 0.0D).func_181666_a(1.0F, 0.0F, 0.0F, 1.0F).func_187314_a(0, 240).func_181675_d();
                           builder.pos((double)(size + 1.0F), (double)(size * 2.0F), 0.0D).func_181666_a(1.0F, 0.0F, 0.0F, 1.0F).func_187314_a(0, 240).func_181675_d();
                           builder.pos((double)(size + 1.0F), 0.0D, 0.0D).func_181666_a(1.0F, 0.0F, 0.0F, 1.0F).func_187314_a(0, 240).func_181675_d();
                        } else {
                           GL11.glLineWidth((float)MCH_Gui.scaleFactor);
                           builder.pos((double)(-size - 1.0F), 0.0D, 0.0D).func_181666_a(1.0F, 0.3F, 0.0F, 8.0F).func_187314_a(0, 240).func_181675_d();
                           builder.pos((double)(-size - 1.0F), (double)(size * 2.0F), 0.0D).func_181666_a(1.0F, 0.3F, 0.0F, 8.0F).func_187314_a(0, 240).func_181675_d();
                           builder.pos((double)(size + 1.0F), (double)(size * 2.0F), 0.0D).func_181666_a(1.0F, 0.3F, 0.0F, 8.0F).func_187314_a(0, 240).func_181675_d();
                           builder.pos((double)(size + 1.0F), 0.0D, 0.0D).func_181666_a(1.0F, 0.3F, 0.0F, 8.0F).func_187314_a(0, 240).func_181675_d();
                        }

                        tessellator.draw();
                        GL11.glPopMatrix();
                        if (!ac.isUAV() && isLockEntity && Minecraft.getMinecraft().field_71474_y.field_74320_O == 0) {
                           GL11.glPushMatrix();
                           builder.begin(1, MCH_Verts.POS_COLOR_LMAP);
                           GL11.glLineWidth(1.0F);
                           builder.pos(x, y + (double)(entity.field_70131_O / 2.0F), z).func_181666_a(1.0F, 0.0F, 0.0F, 1.0F).func_187314_a(0, 240).func_181675_d();
                           builder.pos(ac.field_70142_S - TileEntityRendererDispatcher.field_147554_b, ac.field_70137_T - TileEntityRendererDispatcher.field_147555_c - 1.0D, ac.field_70136_U - TileEntityRendererDispatcher.field_147552_d).func_181666_a(1.0F, 0.0F, 0.0F, 1.0F).func_187314_a(0, 240).func_181675_d();
                           tessellator.draw();
                           GL11.glPopMatrix();
                        }

                        GL11.glLineWidth((float)prevWidth);
                        GL11.glEnable(3553);
                        GL11.glDepthMask(true);
                        GL11.glEnable(2896);
                        GL11.glDisable(3042);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     }

                  }
               }
            }
         }
      }
   }

   public static void renderRope(MCH_EntityAircraft ac, MCH_AircraftInfo info, double x, double y, double z, float tickTime) {
      GL11.glPushMatrix();
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      if (ac.isRepelling()) {
         GL11.glDisable(3553);
         GL11.glDisable(2896);

         for(int i = 0; i < info.repellingHooks.size(); ++i) {
            builder.begin(3, DefaultVertexFormats.field_181706_f);
            builder.pos(((MCH_AircraftInfo.RepellingHook)info.repellingHooks.get(i)).pos.x, ((MCH_AircraftInfo.RepellingHook)info.repellingHooks.get(i)).pos.y, ((MCH_AircraftInfo.RepellingHook)info.repellingHooks.get(i)).pos.z).func_181669_b(0, 0, 0, 255).func_181675_d();
            builder.pos(((MCH_AircraftInfo.RepellingHook)info.repellingHooks.get(i)).pos.x, ((MCH_AircraftInfo.RepellingHook)info.repellingHooks.get(i)).pos.y + (double)ac.ropesLength, ((MCH_AircraftInfo.RepellingHook)info.repellingHooks.get(i)).pos.z).func_181669_b(0, 0, 0, 255).func_181675_d();
            tessellator.draw();
         }

         GL11.glEnable(2896);
         GL11.glEnable(3553);
      }

      GL11.glPopMatrix();
   }
}
