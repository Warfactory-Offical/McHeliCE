package com.norwood.mcheli.hud;

import com.norwood.mcheli.MCH_Camera;
import com.norwood.mcheli.MCH_Lib;
import net.minecraft.entity.Entity;

public class MCH_HudItemCameraRot extends MCH_HudItem {
   private final String drawPosX;
   private final String drawPosY;

   public MCH_HudItemCameraRot(int fileLine, String posx, String posy) {
      super(fileLine);
      this.drawPosX = toFormula(posx);
      this.drawPosY = toFormula(posy);
   }

   public void execute() {
      this.drawCommonGunnerCamera(ac, ac.camera, colorSetting, centerX + calc(this.drawPosX), centerY + calc(this.drawPosY));
   }

   private void drawCommonGunnerCamera(Entity ac, MCH_Camera camera, int color, double posX, double posY) {
      if (camera != null) {
         double[] line = new double[]{posX - 21.0D, posY - 11.0D, posX + 21.0D, posY - 11.0D, posX + 21.0D, posY + 11.0D, posX - 21.0D, posY + 11.0D};
         this.drawLine(line, color, 2);
         line = new double[]{posX - 21.0D, posY, posX, posY, posX + 21.0D, posY, posX, posY, posX, posY - 11.0D, posX, posY, posX, posY + 11.0D, posX, posY};
         this.drawLineStipple(line, color, 1, 52428);
         float pitch = camera.rotationPitch;
         if (pitch < -30.0F) {
            pitch = -30.0F;
         }

         if (pitch > 70.0F) {
            pitch = 70.0F;
         }

         pitch -= 20.0F;
         pitch = (float)((double)pitch * 0.16D);
         float yaw = (float)MCH_Lib.getRotateDiff((double)ac.field_70177_z, (double)camera.rotationYaw);
         yaw *= 2.0F;
         if (yaw < -50.0F) {
            yaw = -50.0F;
         }

         if (yaw > 50.0F) {
            yaw = 50.0F;
         }

         yaw = (float)((double)yaw * 0.34D);
         line = new double[]{posX + (double)yaw - 3.0D, posY + (double)pitch - 2.0D, posX + (double)yaw + 3.0D, posY + (double)pitch - 2.0D, posX + (double)yaw + 3.0D, posY + (double)pitch + 2.0D, posX + (double)yaw - 3.0D, posY + (double)pitch + 2.0D};
         this.drawLine(line, color, 2);
      }
   }
}
