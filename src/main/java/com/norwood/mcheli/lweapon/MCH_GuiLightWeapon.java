package com.norwood.mcheli.lweapon;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_KeyName;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.gltd.MCH_EntityGLTD;
import com.norwood.mcheli.gui.MCH_Gui;
import com.norwood.mcheli.weapon.MCH_WeaponGuidanceSystem;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_GuiLightWeapon extends MCH_Gui {
   public MCH_GuiLightWeapon(Minecraft minecraft) {
      super(minecraft);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public boolean func_73868_f() {
      return false;
   }

   public boolean isDrawGui(EntityPlayer player) {
      if (MCH_ItemLightWeaponBase.isHeld(player)) {
         Entity re = player.func_184187_bx();
         if (!(re instanceof MCH_EntityAircraft) && !(re instanceof MCH_EntityGLTD)) {
            return true;
         }
      }

      return false;
   }

   public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
      if (!isThirdPersonView) {
         GL11.glLineWidth((float)scaleFactor);
         if (this.isDrawGui(player)) {
            MCH_WeaponGuidanceSystem gs = MCH_ClientLightWeaponTickHandler.gs;
            if (gs != null && MCH_ClientLightWeaponTickHandler.weapon != null && MCH_ClientLightWeaponTickHandler.weapon.getInfo() != null) {
               PotionEffect pe = player.func_70660_b(MobEffects.field_76439_r);
               if (pe != null) {
                  this.drawNightVisionNoise();
               }

               GL11.glEnable(3042);
               GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
               int srcBlend = GL11.glGetInteger(3041);
               int dstBlend = GL11.glGetInteger(3040);
               GL11.glBlendFunc(770, 771);
               double dist = 0.0D;
               if (gs.getTargetEntity() != null) {
                  double dx = gs.getTargetEntity().field_70165_t - player.field_70165_t;
                  double dz = gs.getTargetEntity().field_70161_v - player.field_70161_v;
                  dist = Math.sqrt(dx * dx + dz * dz);
               }

               boolean canFire = MCH_ClientLightWeaponTickHandler.weaponMode == 0 || dist >= 40.0D || gs.getLockCount() <= 0;
               if ("fgm148".equalsIgnoreCase(MCH_ItemLightWeaponBase.getName(player.func_184614_ca()))) {
                  this.drawGuiFGM148(player, gs, canFire, player.func_184614_ca());
                  this.drawKeyBind(-805306369, true);
               } else {
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  W_McClient.MOD_bindTexture("textures/gui/stinger.png");
                  double size = 512.0D;

                  while(true) {
                     if (!(size < (double)this.field_146294_l) && !(size < (double)this.field_146295_m)) {
                        this.drawTexturedModalRectRotate(-(size - (double)this.field_146294_l) / 2.0D, -(size - (double)this.field_146295_m) / 2.0D - 20.0D, size, size, 0.0D, 0.0D, 256.0D, 256.0D, 0.0F);
                        this.drawKeyBind(-805306369, false);
                        break;
                     }

                     size *= 2.0D;
                  }
               }

               GL11.glBlendFunc(srcBlend, dstBlend);
               GL11.glDisable(3042);
               this.drawLock(-14101432, -2161656, gs.getLockCount(), gs.getLockCountMax());
               this.drawRange(player, gs, canFire, -14101432, -2161656);
            }

         }
      }
   }

   public void drawNightVisionNoise() {
      GL11.glEnable(3042);
      GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.3F);
      int srcBlend = GL11.glGetInteger(3041);
      int dstBlend = GL11.glGetInteger(3040);
      GL11.glBlendFunc(1, 1);
      W_McClient.MOD_bindTexture("textures/gui/alpha.png");
      this.drawTexturedModalRectRotate(0.0D, 0.0D, (double)this.field_146294_l, (double)this.field_146295_m, (double)this.rand.nextInt(256), (double)this.rand.nextInt(256), 256.0D, 256.0D, 0.0F);
      GL11.glBlendFunc(srcBlend, dstBlend);
      GL11.glDisable(3042);
   }

   void drawLock(int color, int colorLock, int cntLock, int cntMax) {
      int posX = this.centerX;
      int posY = this.centerY + 20;
      func_73734_a(posX - 20, posY + 20 + 1, posX - 20 + 40, posY + 20 + 1 + 1 + 3 + 1, color);
      float lock = (float)cntLock / (float)cntMax;
      func_73734_a(posX - 20 + 1, posY + 20 + 1 + 1, posX - 20 + 1 + (int)(38.0D * (double)lock), posY + 20 + 1 + 1 + 3, -2161656);
   }

   void drawRange(EntityPlayer player, MCH_WeaponGuidanceSystem gs, boolean canFire, int color1, int color2) {
      String msgLockDist = "[--.--]";
      int color = color2;
      if (gs.getLockCount() > 0) {
         Entity target = gs.getLockingEntity();
         if (target != null) {
            double dx = target.field_70165_t - player.field_70165_t;
            double dz = target.field_70161_v - player.field_70161_v;
            msgLockDist = String.format("[%.2f]", Math.sqrt(dx * dx + dz * dz));
            color = canFire ? color1 : color2;
            if (!MCH_Config.HideKeybind.prmBool && gs.isLockComplete()) {
               String k = MCH_KeyName.getDescOrName(MCH_Config.KeyAttack.prmInt);
               this.drawCenteredString("Shot : " + k, this.centerX, this.centerY + 65, -805306369);
            }
         }
      }

      this.drawCenteredString(msgLockDist, this.centerX, this.centerY + 50, color);
   }

   void drawGuiFGM148(EntityPlayer player, MCH_WeaponGuidanceSystem gs, boolean canFire, ItemStack itemStack) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      double fac = (double)this.field_146294_l / 800.0D < (double)this.field_146295_m / 700.0D ? (double)this.field_146294_l / 800.0D : (double)this.field_146295_m / 700.0D;
      int size = (int)(1024.0D * fac);
      size = size / 64 * 64;
      fac = (double)size / 1024.0D;
      double left = (double)(-(size - this.field_146294_l) / 2);
      double top = (double)(-(size - this.field_146295_m) / 2 - 20);
      double right = left + (double)size;
      double bottom = top + (double)size;
      Vec3d pos = MCH_ClientLightWeaponTickHandler.getMartEntityPos();
      double x;
      double y;
      double w;
      double h;
      if (gs.getLockCount() > 0) {
         int scale = scaleFactor > 0 ? scaleFactor : 2;
         if (pos == null) {
            pos = new Vec3d((double)(this.field_146294_l / 2 * scale), (double)(this.field_146295_m / 2 * scale), 0.0D);
         }

         x = 280.0D * fac;
         y = 370.0D * fac;
         w = pos.field_72450_a / (double)scale;
         h = (double)this.field_146295_m - pos.field_72448_b / (double)scale;
         double sx = MCH_Lib.RNG(w, left + x, right - x);
         double sy = MCH_Lib.RNG(h, top + y, bottom - y);
         if (gs.getLockCount() >= gs.getLockCountMax() / 2) {
            this.drawLine(new double[]{-1.0D, sy, (double)(this.field_146294_l + 1), sy, sx, -1.0D, sx, (double)(this.field_146295_m + 1)}, -1593835521);
         }

         if (player.field_70173_aa % 6 >= 3) {
            pos = MCH_ClientLightWeaponTickHandler.getMartEntityBBPos();
            if (pos == null) {
               pos = new Vec3d((double)((this.field_146294_l / 2 - 65) * scale), (double)((this.field_146295_m / 2 + 50) * scale), 0.0D);
            }

            double bx = pos.field_72450_a / (double)scale;
            double by = (double)this.field_146295_m - pos.field_72448_b / (double)scale;
            double dx = Math.abs(w - bx);
            double dy = Math.abs(h - by);
            double p = 1.0D - (double)gs.getLockCount() / (double)gs.getLockCountMax();
            dx = MCH_Lib.RNG(dx, 25.0D, 70.0D);
            dy = MCH_Lib.RNG(dy, 15.0D, 70.0D);
            dx += (70.0D - dx) * p;
            dy += (70.0D - dy) * p;
            int lx = 10;
            int ly = 6;
            this.drawLine(new double[]{sx - dx, sy - dy + (double)ly, sx - dx, sy - dy, sx - dx + (double)lx, sy - dy}, -1593835521, 3);
            this.drawLine(new double[]{sx + dx, sy - dy + (double)ly, sx + dx, sy - dy, sx + dx - (double)lx, sy - dy}, -1593835521, 3);
            dy /= 6.0D;
            this.drawLine(new double[]{sx - dx, sy + dy - (double)ly, sx - dx, sy + dy, sx - dx + (double)lx, sy + dy}, -1593835521, 3);
            this.drawLine(new double[]{sx + dx, sy + dy - (double)ly, sx + dx, sy + dy, sx + dx - (double)lx, sy + dy}, -1593835521, 3);
         }
      }

      func_73734_a(-1, -1, (int)left + 1, this.field_146295_m + 1, -16777216);
      func_73734_a((int)right - 1, -1, this.field_146294_l + 1, this.field_146295_m + 1, -16777216);
      func_73734_a(-1, -1, this.field_146294_l + 1, (int)top + 1, -16777216);
      func_73734_a(-1, (int)bottom - 1, this.field_146294_l + 1, this.field_146295_m + 1, -16777216);
      GL11.glEnable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      W_McClient.MOD_bindTexture("textures/gui/javelin.png");
      this.drawTexturedModalRectRotate(left, top, (double)size, (double)size, 0.0D, 0.0D, 256.0D, 256.0D, 0.0F);
      W_McClient.MOD_bindTexture("textures/gui/javelin2.png");
      PotionEffect pe = player.func_70660_b(MobEffects.field_76439_r);
      if (pe == null) {
         x = 247.0D;
         y = 211.0D;
         w = 380.0D;
         h = 350.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (player.func_184612_cw() <= 60) {
         x = 130.0D;
         y = 334.0D;
         w = 257.0D;
         h = 455.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (MCH_ClientLightWeaponTickHandler.selectedZoom == 0) {
         x = 387.0D;
         y = 211.0D;
         w = 510.0D;
         h = 350.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (MCH_ClientLightWeaponTickHandler.selectedZoom == MCH_ClientLightWeaponTickHandler.weapon.getInfo().zoom.length - 1) {
         x = 511.0D;
         y = 211.0D;
         w = 645.0D;
         h = 350.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (gs.getLockCount() > 0) {
         x = 643.0D;
         y = 211.0D;
         w = 775.0D;
         h = 350.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (MCH_ClientLightWeaponTickHandler.weaponMode == 1) {
         x = 768.0D;
         y = 340.0D;
         w = 890.0D;
         h = 455.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      } else {
         x = 768.0D;
         y = 456.0D;
         w = 890.0D;
         h = 565.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (!canFire) {
         x = 379.0D;
         y = 670.0D;
         w = 511.0D;
         h = 810.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (itemStack.func_77960_j() >= itemStack.func_77958_k()) {
         x = 512.0D;
         y = 670.0D;
         w = 645.0D;
         h = 810.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (gs.getLockCount() < gs.getLockCountMax()) {
         x = 646.0D;
         y = 670.0D;
         w = 776.0D;
         h = 810.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

      if (pe != null) {
         x = 768.0D;
         y = 562.0D;
         w = 890.0D;
         h = 694.0D;
         this.drawTexturedRect(left + x * fac, top + y * fac, (w - x) * fac, (h - y) * fac, x, y, w - x, h - y, 1024.0D, 1024.0D);
      }

   }

   public void drawKeyBind(int color, boolean canSwitchMode) {
      int OffX = this.centerX + 55;
      int OffY = this.centerY + 40;
      this.drawString("CAM MODE :", OffX, OffY + 10, color);
      this.drawString("ZOOM      :", OffX, OffY + 20, color);
      if (canSwitchMode) {
         this.drawString("MODE      :", OffX, OffY + 30, color);
      }

      OffX += 60;
      this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt), OffX, OffY + 10, color);
      this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt), OffX, OffY + 20, color);
      if (canSwitchMode) {
         this.drawString(MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt), OffX, OffY + 30, color);
      }

   }
}
