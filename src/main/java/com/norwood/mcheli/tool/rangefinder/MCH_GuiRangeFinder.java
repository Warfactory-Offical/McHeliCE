package com.norwood.mcheli.tool.rangefinder;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_KeyName;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.gui.MCH_Gui;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_GuiRangeFinder extends MCH_Gui {
   public MCH_GuiRangeFinder(Minecraft minecraft) {
      super(minecraft);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public boolean func_73868_f() {
      return false;
   }

   public boolean isDrawGui(EntityPlayer player) {
      return MCH_ItemRangeFinder.canUse(player);
   }

   public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
      if (!isThirdPersonView) {
         GL11.glLineWidth((float)scaleFactor);
         if (this.isDrawGui(player)) {
            GL11.glDisable(3042);
            if (MCH_ItemRangeFinder.isUsingScope(player)) {
               this.drawRF(player);
            }

         }
      }
   }

   void drawRF(EntityPlayer player) {
      GL11.glEnable(3042);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      int srcBlend = GL11.glGetInteger(3041);
      int dstBlend = GL11.glGetInteger(3040);
      GL11.glBlendFunc(770, 771);
      W_McClient.MOD_bindTexture("textures/gui/rangefinder.png");

      double size;
      for(size = 512.0D; size < (double)this.field_146294_l || size < (double)this.field_146295_m; size *= 2.0D) {
      }

      this.drawTexturedModalRectRotate(-(size - (double)this.field_146294_l) / 2.0D, -(size - (double)this.field_146295_m) / 2.0D, size, size, 0.0D, 0.0D, 256.0D, 256.0D, 0.0F);
      GL11.glBlendFunc(srcBlend, dstBlend);
      GL11.glDisable(3042);
      double factor = size / 512.0D;
      double SCALE_FACTOR = (double)scaleFactor * factor;
      double CX = (double)(this.field_146297_k.field_71443_c / 2);
      double CY = (double)(this.field_146297_k.field_71440_d / 2);
      double px = (CX - 80.0D * SCALE_FACTOR) / SCALE_FACTOR;
      double py = (CY + 55.0D * SCALE_FACTOR) / SCALE_FACTOR;
      GL11.glPushMatrix();
      GL11.glScaled(factor, factor, factor);
      ItemStack item = player.func_184614_ca();
      int damage = (int)((double)(item.func_77958_k() - item.func_77960_j()) / (double)item.func_77958_k() * 100.0D);
      this.drawDigit(String.format("%3d", damage), (int)px, (int)py, 13, damage > 0 ? -15663328 : -61424);
      if (damage <= 0) {
         this.drawString("Please craft", (int)px + 40, (int)py + 0, -65536);
         this.drawString("redstone", (int)px + 40, (int)py + 10, -65536);
      }

      px = (CX - 20.0D * SCALE_FACTOR) / SCALE_FACTOR;
      if (damage > 0) {
         Vec3d vs = new Vec3d(player.posX, player.posY + (double)player.func_70047_e(), player.posZ);
         Vec3d ve = MCH_Lib.Rot2Vec3(player.field_70177_z, player.field_70125_A);
         ve = vs.func_72441_c(ve.x * 300.0D, ve.y * 300.0D, ve.z * 300.0D);
         RayTraceResult mop = player.world.func_72901_a(vs, ve, true);
         if (mop != null && mop.field_72313_a != Type.MISS) {
            int range = (int)player.func_70011_f(mop.field_72307_f.x, mop.field_72307_f.y, mop.field_72307_f.z);
            this.drawDigit(String.format("%4d", range), (int)px, (int)py, 13, -15663328);
         } else {
            this.drawDigit(String.format("----"), (int)px, (int)py, 13, -61424);
         }
      }

      py -= 4.0D;
      px -= 80.0D;
      func_73734_a((int)px, (int)py, (int)px + 30, (int)py + 2, -15663328);
      func_73734_a((int)px, (int)py, (int)px + MCH_ItemRangeFinder.rangeFinderUseCooldown / 2, (int)py + 2, -61424);
      this.drawString(String.format("x%.1f", MCH_ItemRangeFinder.zoom), (int)px, (int)py - 20, -1);
      px += 130.0D;
      int mode = MCH_ItemRangeFinder.mode;
      this.drawString(">", (int)px, (int)py - 30 + mode * 10, -1);
      px += 10.0D;
      this.drawString("Players/Vehicles", (int)px, (int)py - 30, mode == 0 ? -1 : -12566464);
      this.drawString("Monsters/Mobs", (int)px, (int)py - 20, mode == 1 ? -1 : -12566464);
      this.drawString("Mark Point", (int)px, (int)py - 10, mode == 2 ? -1 : -12566464);
      GL11.glPopMatrix();
      px = (CX - 160.0D * SCALE_FACTOR) / (double)scaleFactor;
      py = (CY - 100.0D * SCALE_FACTOR) / (double)scaleFactor;
      if (px < 10.0D) {
         px = 10.0D;
      }

      if (py < 10.0D) {
         py = 10.0D;
      }

      String s = "Spot      : " + MCH_KeyName.getDescOrName(MCH_Config.KeyAttack.prmInt);
      this.drawString(s, (int)px, (int)py + 0, -1);
      s = "Zoom in   : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
      this.drawString(s, (int)px, (int)py + 10, MCH_ItemRangeFinder.zoom < 10.0F ? -1 : -12566464);
      s = "Zoom out : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt);
      this.drawString(s, (int)px, (int)py + 20, MCH_ItemRangeFinder.zoom > 1.2F ? -1 : -12566464);
      s = "Mode      : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFlare.prmInt);
      this.drawString(s, (int)px, (int)py + 30, -1);
   }
}
