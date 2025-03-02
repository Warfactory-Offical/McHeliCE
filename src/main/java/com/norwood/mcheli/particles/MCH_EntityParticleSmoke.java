package com.norwood.mcheli.particles;

import java.util.List;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityParticleSmoke extends MCH_EntityParticleBase {
   private static final VertexFormat VERTEX_FORMAT;

   public MCH_EntityParticleSmoke(World par1World, double x, double y, double z, double mx, double my, double mz) {
      super(par1World, x, y, z, mx, my, mz);
      this.field_70552_h = this.field_70553_i = this.field_70551_j = this.field_187136_p.nextFloat() * 0.3F + 0.7F;
      this.setParticleScale(this.field_187136_p.nextFloat() * 0.5F + 5.0F);
      this.setParticleMaxAge((int)(16.0D / ((double)this.field_187136_p.nextFloat() * 0.8D + 0.2D)) + 2);
   }

   public void func_189213_a() {
      this.field_187123_c = this.posX;
      this.field_187124_d = this.posY;
      this.field_187125_e = this.posZ;
      if (this.field_70546_d < this.field_70547_e) {
         this.func_70536_a((int)(8.0D * (double)this.field_70546_d / (double)this.field_70547_e));
         ++this.field_70546_d;
         if (this.diffusible && this.field_70544_f < this.particleMaxScale) {
            this.field_70544_f += 0.8F;
         }

         if (this.toWhite) {
            float mn = this.getMinColor();
            float mx = this.getMaxColor();
            float dist = mx - mn;
            if ((double)dist > 0.2D) {
               this.field_70552_h += (mx - this.field_70552_h) * 0.016F;
               this.field_70553_i += (mx - this.field_70553_i) * 0.016F;
               this.field_70551_j += (mx - this.field_70551_j) * 0.016F;
            }
         }

         this.effectWind();
         if ((float)(this.field_70546_d / this.field_70547_e) > this.moutionYUpAge) {
            this.field_187130_j += 0.02D;
         } else {
            this.field_187130_j += (double)this.gravity;
         }

         this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
         if (this.diffusible) {
            this.field_187129_i *= 0.96D;
            this.field_187131_k *= 0.96D;
            this.field_187130_j *= 0.96D;
         } else {
            this.field_187129_i *= 0.9D;
            this.field_187131_k *= 0.9D;
         }

      } else {
         this.func_187112_i();
      }
   }

   public float getMinColor() {
      return this.min(this.min(this.field_70551_j, this.field_70553_i), this.field_70552_h);
   }

   public float getMaxColor() {
      return this.max(this.max(this.field_70551_j, this.field_70553_i), this.field_70552_h);
   }

   public float min(float a, float b) {
      return a < b ? a : b;
   }

   public float max(float a, float b) {
      return a > b ? a : b;
   }

   public void effectWind() {
      if (this.isEffectedWind) {
         List<MCH_EntityAircraft> list = this.field_187122_b.func_72872_a(MCH_EntityAircraft.class, this.getCollisionBoundingBox().func_72314_b(15.0D, 15.0D, 15.0D));

         for(int i = 0; i < list.size(); ++i) {
            MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
            if (ac.getThrottle() > 0.10000000149011612D) {
               float dist = this.getDistance(ac);
               double vel = (23.0D - (double)dist) * 0.009999999776482582D * ac.getThrottle();
               double mx = ac.posX - this.posX;
               double mz = ac.posZ - this.posZ;
               this.field_187129_i -= mx * vel;
               this.field_187131_k -= mz * vel;
            }
         }
      }

   }

   public int func_70537_b() {
      return 3;
   }

   @SideOnly(Side.CLIENT)
   public int func_189214_a(float p_70070_1_) {
      double y = this.posY;
      this.posY += 3000.0D;
      int i = super.func_189214_a(p_70070_1_);
      this.posY = y;
      return i;
   }

   public void func_180434_a(BufferBuilder buffer, Entity entityIn, float par2, float par3, float par4, float par5, float par6, float par7) {
      W_McClient.MOD_bindTexture("textures/particles/smoke.png");
      GlStateManager.func_179147_l();
      int srcBlend = GlStateManager.func_187397_v(3041);
      int dstBlend = GlStateManager.func_187397_v(3040);
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179140_f();
      GlStateManager.func_179129_p();
      float f6 = (float)this.field_94054_b / 8.0F;
      float f7 = f6 + 0.125F;
      float f8 = 0.0F;
      float f9 = 1.0F;
      float f10 = 0.1F * this.field_70544_f;
      float f11 = (float)(this.field_187123_c + (this.posX - this.field_187123_c) * (double)par2 - field_70556_an);
      float f12 = (float)(this.field_187124_d + (this.posY - this.field_187124_d) * (double)par2 - field_70554_ao);
      float f13 = (float)(this.field_187125_e + (this.posZ - this.field_187125_e) * (double)par2 - field_70555_ap);
      int i = this.func_189214_a(par2);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      buffer.begin(7, VERTEX_FORMAT);
      buffer.pos((double)(f11 - par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 - par5 * f10 - par7 * f10)).func_187315_a((double)f7, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      buffer.pos((double)(f11 - par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 - par5 * f10 + par7 * f10)).func_187315_a((double)f7, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      buffer.pos((double)(f11 + par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 + par5 * f10 + par7 * f10)).func_187315_a((double)f6, (double)f8).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      buffer.pos((double)(f11 + par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 + par5 * f10 - par7 * f10)).func_187315_a((double)f6, (double)f9).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      Tessellator.getInstance().draw();
      GlStateManager.func_179089_o();
      GlStateManager.func_179145_e();
      GlStateManager.func_179112_b(srcBlend, dstBlend);
      GlStateManager.func_179084_k();
   }

   private float getDistance(MCH_EntityAircraft entity) {
      float f = (float)(this.posX - entity.posX);
      float f1 = (float)(this.posY - entity.posY);
      float f2 = (float)(this.posZ - entity.posZ);
      return MathHelper.func_76129_c(f * f + f1 * f1 + f2 * f2);
   }

   static {
      VERTEX_FORMAT = (new VertexFormat()).func_181721_a(DefaultVertexFormats.field_181713_m).func_181721_a(DefaultVertexFormats.field_181715_o).func_181721_a(DefaultVertexFormats.field_181714_n).func_181721_a(DefaultVertexFormats.field_181716_p).func_181721_a(DefaultVertexFormats.field_181717_q).func_181721_a(DefaultVertexFormats.field_181718_r);
   }
}
