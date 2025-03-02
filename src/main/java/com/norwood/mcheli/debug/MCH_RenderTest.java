package com.norwood.mcheli.debug;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderTest extends W_Render<Entity> {
   protected MCH_ModelTest model;
   private float offsetX;
   private float offsetY;
   private float offsetZ;
   private String textureName;

   public static final IRenderFactory<Entity> factory(float x, float y, float z, String texture_name) {
      return (renderManager) -> {
         return new MCH_RenderTest(renderManager, x, y, z, texture_name);
      };
   }

   public MCH_RenderTest(RenderManager renderManager, float x, float y, float z, String texture_name) {
      super(renderManager);
      this.offsetX = x;
      this.offsetY = y;
      this.offsetZ = z;
      this.textureName = texture_name;
      this.model = new MCH_ModelTest();
   }

   public void func_76986_a(Entity e, double posX, double posY, double posZ, float par8, float par9) {
      if (MCH_Config.TestMode.prmBool) {
         GL11.glPushMatrix();
         GL11.glTranslated(posX + (double)this.offsetX, posY + (double)this.offsetY, posZ + (double)this.offsetZ);
         GL11.glScalef(e.field_70130_N, e.field_70131_O, e.field_70130_N);
         GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
         float prevYaw;
         if (e.field_70177_z - e.field_70126_B < -180.0F) {
            prevYaw = e.field_70126_B - 360.0F;
         } else if (e.field_70126_B - e.field_70177_z < -180.0F) {
            prevYaw = e.field_70126_B + 360.0F;
         } else {
            prevYaw = e.field_70126_B;
         }

         float yaw = -(prevYaw + (e.field_70177_z - prevYaw) * par9) - 180.0F;
         float pitch = -(e.field_70127_C + (e.field_70125_A - e.field_70127_C) * par9);
         GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
         this.bindTexture("textures/" + this.textureName + ".png");
         this.model.renderModel(0.0D, 0.0D, 0.1F);
         GL11.glPopMatrix();
      }
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return TEX_DEFAULT;
   }
}
