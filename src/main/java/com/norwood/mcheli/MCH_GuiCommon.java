package com.norwood.mcheli;

import com.norwood.mcheli.aircraft.MCH_AircraftCommonGui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_GuiCommon extends MCH_AircraftCommonGui {
   public int hitCount = 0;

   public MCH_GuiCommon(Minecraft minecraft) {
      super(minecraft);
   }

   public boolean isDrawGui(EntityPlayer player) {
      return true;
   }

   public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
      GL11.glLineWidth((float)scaleFactor);
      this.drawHitBullet(this.hitCount, 15, -805306369);
   }

   public void onTick() {
      super.onTick();
      if (this.hitCount > 0) {
         --this.hitCount;
      }

   }

   public void hitBullet() {
      this.hitCount = 15;
   }
}
