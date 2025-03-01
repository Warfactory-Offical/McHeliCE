package com.norwood.mcheli.gltd;

import java.util.Random;
import com.norwood.mcheli.MCH_RenderLib;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderGLTD extends W_Render<MCH_EntityGLTD> {
   public static final IRenderFactory<MCH_EntityGLTD> FACTORY = MCH_RenderGLTD::new;
   public static final Random rand = new Random();
   public static _IModelCustom model;

   public MCH_RenderGLTD(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 0.5F;
   }

   public void doRender(MCH_EntityGLTD entity, double posX, double posY, double posZ, float par8, float tickTime) {
      GL11.glPushMatrix();
      GL11.glTranslated(posX, posY + 0.25D, posZ);
      this.setCommonRenderParam(true, entity.func_70070_b());
      this.bindTexture("textures/gltd.png");
      Minecraft mc = Minecraft.func_71410_x();
      boolean isNotRenderHead = false;
      if (entity.getRiddenByEntity() != null) {
         entity.isUsedPlayer = true;
         entity.renderRotaionYaw = entity.getRiddenByEntity().field_70177_z;
         entity.renderRotaionPitch = entity.getRiddenByEntity().field_70125_A;
         isNotRenderHead = mc.field_71474_y.field_74320_O == 0 && W_Lib.isClientPlayer(entity.getRiddenByEntity());
      }

      if (entity.isUsedPlayer) {
         GL11.glPushMatrix();
         GL11.glRotatef(-entity.field_70177_z, 0.0F, 1.0F, 0.0F);
         model.renderPart("$body");
         GL11.glPopMatrix();
      } else {
         GL11.glRotatef(-entity.field_70177_z, 0.0F, 1.0F, 0.0F);
         model.renderPart("$body");
      }

      GL11.glTranslatef(0.0F, 0.45F, 0.0F);
      if (entity.isUsedPlayer) {
         GL11.glRotatef(entity.renderRotaionYaw, 0.0F, -1.0F, 0.0F);
         GL11.glRotatef(entity.renderRotaionPitch, 1.0F, 0.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, -0.45F, 0.0F);
      if (!isNotRenderHead) {
         model.renderPart("$head");
      }

      GL11.glTranslatef(0.0F, 0.45F, 0.0F);
      this.restoreCommonRenderParam();
      GL11.glDisable(2896);
      Vec3d[] v = new Vec3d[]{new Vec3d(0.0D, 0.2D, 0.0D), new Vec3d(0.0D, 0.2D, 100.0D)};
      int a = rand.nextInt(64);
      MCH_RenderLib.drawLine(v, 1619066752 | a << 24);
      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }

   public boolean shouldRender(MCH_EntityGLTD livingEntity, ICamera camera, double camX, double camY, double camZ) {
      return true;
   }

   protected ResourceLocation getEntityTexture(MCH_EntityGLTD entity) {
      return TEX_DEFAULT;
   }
}
