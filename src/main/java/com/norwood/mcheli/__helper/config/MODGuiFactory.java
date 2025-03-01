package com.norwood.mcheli.__helper.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

public class MODGuiFactory extends DefaultGuiFactory {
   public MODGuiFactory() {
      super("mcheli", "MC Helicopter MOD");
   }

   public GuiScreen createConfigGui(GuiScreen parentScreen) {
      return new GuiMODConfigTop(parentScreen);
   }
}
