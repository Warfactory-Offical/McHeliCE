package com.norwood.mcheli.__helper.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.norwood.mcheli.__helper.addon.LegacyPackAssistant;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.client.config.HoverChecker;

public class GuiMODConfigTop extends GuiScreen {
   public final GuiScreen parentScreen;
   private HoverChecker assistHoverChecker;
   private int genAnimationTicks;

   public GuiMODConfigTop(GuiScreen parentScreen) {
      this.parentScreen = parentScreen;
   }

   public void func_73866_w_() {
      String doneText = I18n.func_135052_a("gui.done", new Object[0]);
      int doneWidth = Math.max(this.field_146297_k.field_71466_p.func_78256_a(doneText) + 20, 100);
      this.func_189646_b(new GuiButtonExt(1, this.field_146294_l - 20 - doneWidth, this.field_146295_m - 29, doneWidth, 20, doneText));
      GuiButtonExt configBtn = new GuiButtonExt(16, this.field_146294_l / 2 - 100, 32, 200, 20, "Game config (in progress...)");
      configBtn.field_146124_l = false;
      GuiButtonExt assistBtn = new GuiButtonExt(16, this.field_146294_l / 2 - 100, 64, 200, 20, I18n.func_135052_a("gui.com.norwood.mcheli.generateLegacyAsisst", new Object[0]));
      this.func_189646_b(configBtn);
      this.func_189646_b(assistBtn);
      this.assistHoverChecker = new HoverChecker(assistBtn, 800);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146289_q, "MC Helicopter MOD Config", this.field_146294_l / 2, 8, 16777215);
      if (this.genAnimationTicks > 0) {
         float f = this.genAnimationTicks > 10 ? 1.0F : (float)this.genAnimationTicks / 10.0F;
         this.func_73731_b(this.field_146289_q, "Generate Done!", this.field_146294_l / 2 - 100, 92, 16720418 | (int)(255.0F * f) << 24);
      }

      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (this.assistHoverChecker.checkHover(mouseX, mouseY)) {
         this.drawToolTip(Arrays.asList(I18n.func_135052_a("gui.com.norwood.mcheli.generateLegacyAsisst.desc", new Object[0]).split("\n")), mouseX, mouseY);
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.genAnimationTicks > 0) {
         --this.genAnimationTicks;
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146127_k == 1) {
         this.field_146297_k.func_147108_a(this.parentScreen);
      } else if (button.field_146127_k == 16) {
         LegacyPackAssistant.generateDirectoryPack();
         this.genAnimationTicks = 60;
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1) {
         this.field_146297_k.func_147108_a(this.parentScreen);
      } else {
         super.func_73869_a(typedChar, keyCode);
      }

   }

   private void drawToolTip(List<String> stringList, int x, int y) {
      GuiUtils.drawHoveringText(stringList, x, y, this.field_146294_l, this.field_146295_m, 300, this.field_146289_q);
   }
}
