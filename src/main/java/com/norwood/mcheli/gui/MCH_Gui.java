package com.norwood.mcheli.gui;

import java.util.ArrayList;
import java.util.Random;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class MCH_Gui extends GuiScreen {
   protected int centerX = 0;
   protected int centerY = 0;
   protected Random rand = new Random();
   protected float smoothCamPartialTicks;
   public static int scaleFactor;

   public MCH_Gui(Minecraft minecraft) {
      this.mc = minecraft;
      this.smoothCamPartialTicks = 0.0F;
      this.zLevel = -110.0F;
   }

   public void initGui() {
      super.initGui();
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void onTick() {
   }

   public abstract boolean isDrawGui(EntityPlayer var1);

   public abstract void drawGui(EntityPlayer var1, boolean var2);

   public void drawScreen(int par1, int par2, float partialTicks) {
      this.smoothCamPartialTicks = partialTicks;
      ScaledResolution scaledresolution = new W_ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      scaleFactor = scaledresolution.getScaleFactor();
      if (!this.mc.gameSettings.hideGUI) {
         this.width = this.mc.displayWidth / scaleFactor;
         this.height = this.mc.displayHeight / scaleFactor;
         this.centerX = this.width / 2;
         this.centerY = this.height / 2;
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.mc.player != null) {
            this.drawGui(this.mc.player, this.mc.gameSettings.thirdPersonView != 0);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }
   }

   public void drawTexturedModalRectRotate(
      double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, float rot
   ) {
      GL11.glPushMatrix();
      GL11.glTranslated(left + width / 2.0, top + height / 2.0, 0.0);
      GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
      float f = 0.00390625F;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(7, DefaultVertexFormats.POSITION_TEX);
      builder.pos(-width / 2.0, height / 2.0, this.zLevel).tex(uLeft * f, (vTop + vHeight) * f).endVertex();
      builder.pos(width / 2.0, height / 2.0, this.zLevel).tex((uLeft + uWidth) * f, (vTop + vHeight) * f).endVertex();
      builder.pos(width / 2.0, -height / 2.0, this.zLevel).tex((uLeft + uWidth) * f, vTop * f).endVertex();
      builder.pos(-width / 2.0, -height / 2.0, this.zLevel).tex(uLeft * f, vTop * f).endVertex();
      tessellator.draw();
      GL11.glPopMatrix();
   }

   public void drawTexturedRect(
      double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, double textureWidth, double textureHeight
   ) {
      float fx = (float)(1.0 / textureWidth);
      float fy = (float)(1.0 / textureHeight);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(7, DefaultVertexFormats.POSITION_TEX);
      builder.pos(left, top + height, this.zLevel).tex(uLeft * fx, (vTop + vHeight) * fy).endVertex();
      builder.pos(left + width, top + height, this.zLevel).tex((uLeft + uWidth) * fx, (vTop + vHeight) * fy).endVertex();
      builder.pos(left + width, top, this.zLevel).tex((uLeft + uWidth) * fx, vTop * fy).endVertex();
      builder.pos(left, top, this.zLevel).tex(uLeft * fx, vTop * fy).endVertex();
      tessellator.draw();
   }

   public void drawLineStipple(double[] line, int color, int factor, int pattern) {
      GL11.glEnable(2852);
      GL11.glLineStipple(factor, (short)pattern);
      this.drawLine(line, color);
      GL11.glDisable(2852);
   }

   public void drawLine(double[] line, int color) {
      this.drawLine(line, color, 1);
   }

   public void drawString(String s, int x, int y, int color) {
      this.drawString(this.mc.fontRenderer, s, x, y, color);
   }

   public void drawDigit(String s, int x, int y, int interval, int color) {
      GL11.glEnable(3042);
      GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
      int srcBlend = GL11.glGetInteger(3041);
      int dstBlend = GL11.glGetInteger(3040);
      GL11.glBlendFunc(770, 771);
      W_McClient.MOD_bindTexture("textures/gui/digit.png");

      for (int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         if (c >= '0' && c <= '9') {
            this.drawTexturedModalRect(x + interval * i, y, 16 * (c - '0'), 0, 16, 16);
         }

         if (c == '-') {
            this.drawTexturedModalRect(x + interval * i, y, 160, 0, 16, 16);
         }
      }

      GL11.glBlendFunc(srcBlend, dstBlend);
      GL11.glDisable(3042);
   }

   public void drawCenteredString(String s, int x, int y, int color) {
      this.drawCenteredString(this.mc.fontRenderer, s, x, y, color);
   }

   public void drawLine(double[] line, int color, int mode) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color >> 0 & 0xFF), (byte)(color >> 24 & 0xFF));
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(mode, DefaultVertexFormats.POSITION);

      for (int i = 0; i < line.length; i += 2) {
         builder.pos(line[i + 0], line[i + 1], this.zLevel).endVertex();
      }

      tessellator.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
      GL11.glPopMatrix();
   }

   public void drawPoints(double[] points, int color, int pointWidth) {
      int prevWidth = GL11.glGetInteger(2833);
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color >> 0 & 0xFF), (byte)(color >> 24 & 0xFF));
      GL11.glPointSize(pointWidth);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(0, DefaultVertexFormats.POSITION);

      for (int i = 0; i < points.length; i += 2) {
         builder.pos(points[i], points[i + 1], 0.0).endVertex();
      }

      tessellator.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
      GL11.glPointSize(prevWidth);
   }

   public void drawPoints(ArrayList<Double> points, int color, int pointWidth) {
      int prevWidth = GL11.glGetInteger(2833);
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color >> 0 & 0xFF), (byte)(color >> 24 & 0xFF));
      GL11.glPointSize(pointWidth);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder builder = tessellator.getBuffer();
      builder.begin(0, DefaultVertexFormats.POSITION);

      for (int i = 0; i < points.size(); i += 2) {
         builder.pos(points.get(i), points.get(i + 1), 0.0).endVertex();
      }

      tessellator.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
      GL11.glPointSize(prevWidth);
   }
}
