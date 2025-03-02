package com.norwood.mcheli.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.gui.MCH_Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_GuiTitle extends MCH_Gui {
   private final List<ChatLine> chatLines = new ArrayList();
   private int prevPlayerTick = 0;
   private int restShowTick = 0;
   private int showTick = 0;
   private float colorAlpha = 0.0F;
   private int position = 0;

   public MCH_GuiTitle(Minecraft minecraft) {
      super(minecraft);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public boolean func_73868_f() {
      return false;
   }

   public boolean isDrawGui(EntityPlayer player) {
      if (this.restShowTick > 0 && this.chatLines.size() > 0 && player != null && player.world != null) {
         if (this.prevPlayerTick != player.field_70173_aa) {
            ++this.showTick;
            --this.restShowTick;
         }

         this.prevPlayerTick = player.field_70173_aa;
      }

      return this.restShowTick > 0;
   }

   public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
      GL11.glLineWidth((float)(scaleFactor * 2));
      GL11.glDisable(3042);
      if (scaleFactor <= 0) {
         scaleFactor = 1;
      }

      this.colorAlpha = 1.0F;
      if (this.restShowTick > 20 && this.showTick < 5) {
         this.colorAlpha = 0.2F * (float)this.showTick;
      }

      if (this.showTick > 0 && this.restShowTick < 5) {
         this.colorAlpha = 0.2F * (float)this.restShowTick;
      }

      this.drawChat();
   }

   private String formatColors(String s) {
      return Minecraft.func_71410_x().field_71474_y.field_74344_o ? s : TextFormatting.func_110646_a(s);
   }

   private int calculateChatboxWidth() {
      short short1 = 320;
      byte b0 = 40;
      return MathHelper.func_76141_d(this.field_146297_k.field_71474_y.field_96692_F * (float)(short1 - b0) + (float)b0);
   }

   public void setupTitle(ITextComponent chatComponent, int showTime, int pos) {
      int displayTime = 20;
      int line = 0;
      this.chatLines.clear();
      this.position = pos;
      this.showTick = 0;
      this.restShowTick = showTime;
      int k = MathHelper.func_76141_d((float)this.calculateChatboxWidth() / this.field_146297_k.field_71474_y.field_96691_E);
      int l = 0;
      TextComponentString chatcomponenttext = new TextComponentString("");
      ArrayList<ITextComponent> arraylist = Lists.newArrayList();
      ArrayList<ITextComponent> arraylist1 = Lists.newArrayList(chatComponent);

      ITextComponent ichatcomponent1;
      for(int i1 = 0; i1 < arraylist1.size(); ++i1) {
         ichatcomponent1 = (ITextComponent)arraylist1.get(i1);
         String[] splitLine = (ichatcomponent1.func_150261_e() + "").split("\n");
         int lineCnt = 0;
         String[] var15 = splitLine;
         int var16 = splitLine.length;

         for(int var17 = 0; var17 < var16; ++var17) {
            String sLine = var15[var17];
            String s = this.formatColors(ichatcomponent1.func_150256_b().func_150218_j() + sLine);
            int j1 = this.field_146297_k.field_71466_p.func_78256_a(s);
            TextComponentString chatcomponenttext1 = new TextComponentString(s);
            chatcomponenttext1.func_150255_a(ichatcomponent1.func_150256_b().func_150232_l());
            boolean flag1 = false;
            if (l + j1 > k) {
               String s1 = this.field_146297_k.field_71466_p.func_78262_a(s, k - l, false);
               String s2 = s1.length() < s.length() ? s.substring(s1.length()) : null;
               if (s2 != null && s2.length() > 0) {
                  int k1 = s1.lastIndexOf(" ");
                  if (k1 >= 0 && this.field_146297_k.field_71466_p.func_78256_a(s.substring(0, k1)) > 0) {
                     s1 = s.substring(0, k1);
                     s2 = s.substring(k1);
                  }

                  TextComponentString chatcomponenttext2 = new TextComponentString(s2);
                  chatcomponenttext2.func_150255_a(ichatcomponent1.func_150256_b().func_150232_l());
                  arraylist1.add(i1 + 1, chatcomponenttext2);
               }

               j1 = this.field_146297_k.field_71466_p.func_78256_a(s1);
               chatcomponenttext1 = new TextComponentString(s1);
               chatcomponenttext1.func_150255_a(ichatcomponent1.func_150256_b().func_150232_l());
               flag1 = true;
            }

            if (l + j1 <= k) {
               l += j1;
               chatcomponenttext.func_150257_a(chatcomponenttext1);
            } else {
               flag1 = true;
            }

            if (flag1) {
               arraylist.add(chatcomponenttext);
               l = 0;
               chatcomponenttext = new TextComponentString("");
            }

            ++lineCnt;
            if (lineCnt < splitLine.length) {
               arraylist.add(chatcomponenttext);
               l = 0;
               chatcomponenttext = new TextComponentString("");
            }
         }
      }

      arraylist.add(chatcomponenttext);
      Iterator iterator = arraylist.iterator();

      while(iterator.hasNext()) {
         ichatcomponent1 = (ITextComponent)iterator.next();
         this.chatLines.add(new ChatLine(displayTime, ichatcomponent1, line));
      }

      while(this.chatLines.size() > 100) {
         this.chatLines.remove(this.chatLines.size() - 1);
      }

   }

   private int calculateChatboxHeight() {
      short short1 = 180;
      byte b0 = 20;
      return MathHelper.func_76141_d(this.field_146297_k.field_71474_y.field_96694_H * (float)(short1 - b0) + (float)b0);
   }

   private void drawChat() {
      float charAlpha = this.field_146297_k.field_71474_y.field_74357_r * 0.9F + 0.1F;
      float scale = this.field_146297_k.field_71474_y.field_96691_E * 2.0F;
      GL11.glPushMatrix();
      float posY = 0.0F;
      switch(this.position) {
      case 0:
      default:
         posY = (float)(this.field_146297_k.field_71440_d / 2 / scaleFactor) - (float)this.chatLines.size() / 2.0F * 9.0F * scale;
         break;
      case 1:
         posY = 0.0F;
         break;
      case 2:
         posY = (float)(this.field_146297_k.field_71440_d / scaleFactor) - (float)this.chatLines.size() * 9.0F * scale;
         break;
      case 3:
         posY = (float)(this.field_146297_k.field_71440_d / 3 / scaleFactor) - (float)this.chatLines.size() / 2.0F * 9.0F * scale;
         break;
      case 4:
         posY = (float)(this.field_146297_k.field_71440_d * 2 / 3 / scaleFactor) - (float)this.chatLines.size() / 2.0F * 9.0F * scale;
      }

      GL11.glTranslatef(0.0F, posY, 0.0F);
      GL11.glScalef(scale, scale, 1.0F);

      for(int i = 0; i < this.chatLines.size(); ++i) {
         ChatLine chatline = (ChatLine)this.chatLines.get(i);
         if (chatline != null) {
            int alpha = (int)(255.0F * charAlpha * this.colorAlpha);
            int y = i * 9;
            func_73734_a(0, y + 9, this.field_146297_k.field_71443_c, y, alpha / 2 << 24);
            GL11.glEnable(3042);
            String s = chatline.func_151461_a().func_150254_d();
            int sw = this.field_146297_k.field_71443_c / 2 / scaleFactor - this.field_146297_k.field_71466_p.func_78256_a(s);
            sw = (int)((float)sw / scale);
            this.field_146297_k.field_71466_p.func_175063_a(s, (float)sw, (float)(y + 1), 16777215 + (alpha << 24));
            GL11.glDisable(3008);
         }
      }

      GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
      GL11.glPopMatrix();
   }
}
