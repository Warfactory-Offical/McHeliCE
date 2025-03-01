package com.norwood.mcheli.wrapper;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;

public abstract class W_GuiContainer extends GuiContainer {
   private float time;

   public W_GuiContainer(Container par1Container) {
      super(par1Container);
   }

   public void drawItemStack(ItemStack item, int x, int y) {
      if (!item.func_190926_b()) {
         if (item.func_77973_b() != null) {
            FontRenderer font = item.func_77973_b().getFontRenderer(item);
            if (font == null) {
               font = this.field_146289_q;
            }

            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
            this.field_146296_j.func_180450_b(item, x, y);
            this.field_146296_j.func_180453_a(font, item, x, y, (String)null);
            this.field_73735_i = 0.0F;
            this.field_146296_j.field_77023_b = 0.0F;
         }
      }
   }

   public void drawIngredient(Ingredient ingredient, int x, int y) {
      if (ingredient != Ingredient.field_193370_a) {
         ItemStack[] itemstacks = ingredient.func_193365_a();
         int index = MathHelper.func_76141_d(this.time / 20.0F) % itemstacks.length;
         this.drawItemStack(itemstacks[index], x, y);
      }

   }

   public void drawString(String s, int x, int y, int color) {
      this.func_73731_b(this.field_146289_q, s, x, y, color);
   }

   public void drawCenteredString(String s, int x, int y, int color) {
      this.func_73732_a(this.field_146289_q, s, x, y, color);
   }

   public int getStringWidth(String s) {
      return this.field_146289_q.func_78256_a(s);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.time += partialTicks;
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   public float getAnimationTime() {
      return this.time;
   }
}
