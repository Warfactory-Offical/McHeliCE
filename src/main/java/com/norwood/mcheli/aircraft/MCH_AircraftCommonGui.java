package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_KeyName;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.gui.MCH_Gui;
import com.norwood.mcheli.hud.MCH_Hud;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import com.norwood.mcheli.weapon.MCH_WeaponSet;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class MCH_AircraftCommonGui extends MCH_Gui {
   public MCH_AircraftCommonGui(Minecraft minecraft) {
      super(minecraft);
   }

   public void drawHud(MCH_EntityAircraft ac, EntityPlayer player, int seatId) {
      MCH_AircraftInfo info = ac.getAcInfo();
      if (info != null) {
         if (ac.isMissileCameraMode(player) && ac.getTVMissile() != null && info.hudTvMissile != null) {
            info.hudTvMissile.draw(ac, player, this.smoothCamPartialTicks);
         } else {
            if (seatId < 0) {
               return;
            }

            if (seatId < info.hudList.size()) {
               MCH_Hud hud = (MCH_Hud)info.hudList.get(seatId);
               if (hud != null) {
                  hud.draw(ac, player, this.smoothCamPartialTicks);
               }
            }
         }

      }
   }

   public void drawDebugtInfo(MCH_EntityAircraft ac) {
      if (MCH_Config.DebugLog) {
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

   public void drawHitBullet(int hs, int hsMax, int color) {
      if (hs > 0) {
         int cx = this.centerX;
         int cy = this.centerY;
         int IVX = 10;
         int IVY = 10;
         int SZX = 5;
         int SZY = 5;
         double[] ls = new double[]{(double)(cx - IVX), (double)(cy - IVY), (double)(cx - SZX), (double)(cy - SZY), (double)(cx - IVX), (double)(cy + IVY), (double)(cx - SZX), (double)(cy + SZY), (double)(cx + IVX), (double)(cy - IVY), (double)(cx + SZX), (double)(cy - SZY), (double)(cx + IVX), (double)(cy + IVY), (double)(cx + SZX), (double)(cy + SZY)};
         color = MCH_Config.hitMarkColorRGB;
         int alpha = hs * (256 / hsMax);
         color |= (int)(MCH_Config.hitMarkColorAlpha * (float)alpha) << 24;
         this.drawLine(ls, color);
      }

   }

   public void drawHitBullet(MCH_EntityAircraft ac, int color, int seatID) {
      this.drawHitBullet(ac.getHitStatus(), ac.getMaxHitStatus(), color);
   }

   protected void drawTvMissileNoise(MCH_EntityAircraft ac, MCH_EntityTvMissile tvmissile) {
      GL11.glEnable(3042);
      GL11.glColor4f(0.5F, 0.5F, 0.5F, 0.4F);
      int srcBlend = GL11.glGetInteger(3041);
      int dstBlend = GL11.glGetInteger(3040);
      GL11.glBlendFunc(1, 1);
      W_McClient.MOD_bindTexture("textures/gui/noise.png");
      this.drawTexturedModalRectRotate(0.0D, 0.0D, (double)this.field_146294_l, (double)this.field_146295_m, (double)this.rand.nextInt(256), (double)this.rand.nextInt(256), 256.0D, 256.0D, 0.0F);
      GL11.glBlendFunc(srcBlend, dstBlend);
      GL11.glDisable(3042);
   }

   public void drawKeyBind(MCH_EntityAircraft ac, MCH_AircraftInfo info, EntityPlayer player, int seatID, int RX, int LX, int colorActive, int colorInactive) {
      String msg = "";
      int c = false;
      if (seatID == 0 && ac.canPutToRack()) {
         msg = "PutRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyPutToRack.prmInt);
         this.drawString(msg, LX, this.centerY - 10, colorActive);
      }

      if (seatID == 0 && ac.canDownFromRack()) {
         msg = "DownRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyDownFromRack.prmInt);
         this.drawString(msg, LX, this.centerY - 0, colorActive);
      }

      if (seatID == 0 && ac.canRideRack()) {
         msg = "RideRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyPutToRack.prmInt);
         this.drawString(msg, LX, this.centerY + 10, colorActive);
      }

      if (seatID == 0 && ac.func_184187_bx() != null) {
         msg = "DismountRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyDownFromRack.prmInt);
         this.drawString(msg, LX, this.centerY + 10, colorActive);
      }

      int c;
      if (seatID > 0 && ac.getSeatNum() > 1 || Keyboard.isKeyDown(MCH_Config.KeyFreeLook.prmInt)) {
         c = seatID == 0 ? 'Ｐ' : colorActive;
         String sk = seatID == 0 ? MCH_KeyName.getDescOrName(MCH_Config.KeyFreeLook.prmInt) + " + " : "";
         msg = "NextSeat : " + sk + MCH_KeyName.getDescOrName(MCH_Config.KeyGUI.prmInt);
         this.drawString(msg, RX, this.centerY - 70, c);
         msg = "PrevSeat : " + sk + MCH_KeyName.getDescOrName(MCH_Config.KeyExtra.prmInt);
         this.drawString(msg, RX, this.centerY - 60, c);
      }

      msg = "Gunner " + (ac.getGunnerStatus() ? "ON" : "OFF") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFreeLook.prmInt) + " + " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
      this.drawString(msg, LX, this.centerY - 40, colorActive);
      if (seatID >= 0 && seatID <= 1 && ac.haveFlare()) {
         c = ac.isFlarePreparation() ? colorInactive : colorActive;
         msg = "Flare : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFlare.prmInt);
         this.drawString(msg, RX, this.centerY - 50, c);
      }

      if (seatID == 0 && info.haveLandingGear()) {
         if (ac.canFoldLandingGear()) {
            msg = "Gear Up : " + MCH_KeyName.getDescOrName(MCH_Config.KeyGearUpDown.prmInt);
            this.drawString(msg, RX, this.centerY - 40, colorActive);
         } else if (ac.canUnfoldLandingGear()) {
            msg = "Gear Down : " + MCH_KeyName.getDescOrName(MCH_Config.KeyGearUpDown.prmInt);
            this.drawString(msg, RX, this.centerY - 40, colorActive);
         }
      }

      MCH_WeaponSet ws = ac.getCurrentWeapon(player);
      if (ac.getWeaponNum() > 1) {
         msg = "Weapon : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchWeapon2.prmInt);
         this.drawString(msg, LX, this.centerY - 70, colorActive);
      }

      if (ws.getCurrentWeapon().numMode > 0) {
         msg = "WeaponMode : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt);
         this.drawString(msg, LX, this.centerY - 60, colorActive);
      }

      if (ac.canSwitchSearchLight(player)) {
         msg = "SearchLight : " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
         this.drawString(msg, LX, this.centerY - 50, colorActive);
      } else if (ac.canSwitchCameraMode(seatID)) {
         msg = "CameraMode : " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
         this.drawString(msg, LX, this.centerY - 50, colorActive);
      }

      if (seatID == 0 && ac.getSeatNum() >= 1) {
         int color = colorActive;
         if (info.isEnableParachuting && MCH_Lib.getBlockIdY(ac, 3, -10) == 0) {
            msg = "Parachuting : " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt);
         } else if (ac.canStartRepelling()) {
            msg = "Repelling : " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt);
            color = 65280;
         } else {
            msg = "Dismount : " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt);
         }

         this.drawString(msg, LX, this.centerY - 30, color);
      }

      if (seatID == 0 && ac.canSwitchFreeLook() || seatID > 0 && ac.canSwitchGunnerModeOtherSeat(player)) {
         msg = "FreeLook : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFreeLook.prmInt);
         this.drawString(msg, LX, this.centerY - 20, colorActive);
      }

      if (seatID > 1 && info.haveRepellingHook() && ac.canRepelling(player)) {
         msg = "Use Repelling Drop : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchHovering.prmInt);
         this.drawString(msg, LX, this.centerY - 90, colorActive);
      }

   }
}
