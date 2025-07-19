package com.norwood.mcheli.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MCH_EntityParticleExplode extends MCH_EntityParticleBase {
   private static final VertexFormat VERTEX_FORMAT = new VertexFormat()
      .addElement(DefaultVertexFormats.POSITION_3F)
      .addElement(DefaultVertexFormats.TEX_2F)
      .addElement(DefaultVertexFormats.COLOR_4UB)
      .addElement(DefaultVertexFormats.TEX_2S)
      .addElement(DefaultVertexFormats.NORMAL_3B)
      .addElement(DefaultVertexFormats.PADDING_1B);
   private static final ResourceLocation texture = new ResourceLocation("textures/entity/explosion.png");
   private int nowCount;
   private int endCount;
   private TextureManager theRenderEngine = Minecraft.getMinecraft().renderEngine;
   private float size;

   public MCH_EntityParticleExplode(World w, double x, double y, double z, double size, double age, double mz) {
      super(w, x, y, z, 0.0, 0.0, 0.0);
      this.endCount = 1 + (int)age;
      this.size = (float)size;
   }

   public void renderParticle(
      BufferBuilder buffer, Entity entityIn, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_
   ) {
      int i = (int)((this.nowCount + p_70539_2_) * 15.0F / this.endCount);
      if (i <= 15) {
         GlStateManager.enableBlend();
         int srcBlend = GlStateManager.glGetInteger(3041);
         int dstBlend = GlStateManager.glGetInteger(3040);
         GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
         GlStateManager.disableCull();
         this.theRenderEngine.bindTexture(texture);
         float f6 = i % 4 / 4.0F;
         float f7 = f6 + 0.24975F;
         float f8 = i / 4 / 4.0F;
         float f9 = f8 + 0.24975F;
         float f10 = 2.0F * this.size;
         float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * p_70539_2_ - interpPosX);
         float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * p_70539_2_ - interpPosY);
         float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * p_70539_2_ - interpPosZ);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         RenderHelper.disableStandardItemLighting();
         int j = 15728880;
         int k = j >> 16 & 65535;
         int l = j & 65535;
         buffer.begin(7, VERTEX_FORMAT);
         buffer.pos(f11 - p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10, f13 - p_70539_5_ * f10 - p_70539_7_ * f10)
            .tex(f7, f9)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(k, l)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         buffer.pos(f11 - p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10, f13 - p_70539_5_ * f10 + p_70539_7_ * f10)
            .tex(f7, f8)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(k, l)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         buffer.pos(f11 + p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10, f13 + p_70539_5_ * f10 + p_70539_7_ * f10)
            .tex(f6, f8)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(k, l)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         buffer.pos(f11 + p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10, f13 + p_70539_5_ * f10 - p_70539_7_ * f10)
            .tex(f6, f9)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(k, l)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         Tessellator.getInstance().draw();
         GlStateManager.doPolygonOffset(0.0F, 0.0F);
         GlStateManager.enableLighting();
         GlStateManager.enableCull();
         GlStateManager.blendFunc(srcBlend, dstBlend);
         GlStateManager.disableBlend();
      }
   }

   public int getBrightnessForRender(float p_70070_1_) {
      return 15728880;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.nowCount++;
      if (this.nowCount == this.endCount) {
         this.setExpired();
      }
   }

   @Override
   public int getFXLayer() {
      return 3;
   }
}
