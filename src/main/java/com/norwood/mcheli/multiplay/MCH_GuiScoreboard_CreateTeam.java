package com.norwood.mcheli.multiplay;

import java.io.IOException;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class MCH_GuiScoreboard_CreateTeam extends MCH_GuiScoreboard_Base {
   private GuiButton buttonCreateTeamOK;
   private GuiButton buttonCreateTeamFF;
   private GuiTextField editCreateTeamName;
   private static boolean friendlyFire = true;
   private int lastTeamColor = 0;
   private static final String[] colorNames = new String[]{"RESET", "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW"};

   public MCH_GuiScoreboard_CreateTeam(MCH_IGuiScoreboard switcher, EntityPlayer player) {
      super(switcher, player);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      ScaledResolution sr = new W_ScaledResolution(this.field_146297_k, this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d);
      int factor = sr.func_78325_e() > 0 ? sr.func_78325_e() : 1;
      this.field_147003_i = 0;
      this.field_147009_r = 0;
      int x = this.field_146297_k.field_71443_c / 2 / factor;
      int y = this.field_146297_k.field_71440_d / 2 / factor;
      GuiButton buttonCTNextC = new GuiButton(576, x + 40, y - 20, 40, 20, ">");
      GuiButton buttonCTPrevC = new GuiButton(577, x - 80, y - 20, 40, 20, "<");
      this.buttonCreateTeamFF = new GuiButton(560, x - 80, y + 20, 160, 20, "");
      this.buttonCreateTeamOK = new GuiButton(528, x - 80, y + 60, 80, 20, "OK");
      GuiButton buttonCTCancel = new GuiButton(544, x + 0, y + 60, 80, 20, "Cancel");
      this.editCreateTeamName = new GuiTextField(599, this.field_146289_q, x - 80, y - 55, 160, 20);
      this.editCreateTeamName.func_146180_a("");
      this.editCreateTeamName.func_146193_g(-1);
      this.editCreateTeamName.func_146203_f(16);
      this.editCreateTeamName.func_146195_b(true);
      this.listGui.add(buttonCTNextC);
      this.listGui.add(buttonCTPrevC);
      this.listGui.add(this.buttonCreateTeamFF);
      this.listGui.add(this.buttonCreateTeamOK);
      this.listGui.add(buttonCTCancel);
      this.listGui.add(this.editCreateTeamName);
   }

   public void func_73876_c() {
      String teamName = this.editCreateTeamName.func_146179_b();
      this.buttonCreateTeamOK.field_146124_l = teamName.length() > 0 && teamName.length() <= 16;
      this.editCreateTeamName.func_146178_a();
      this.buttonCreateTeamFF.field_146126_j = "Friendly Fire : " + (friendlyFire ? "ON" : "OFF");
   }

   public void acviveScreen() {
      this.editCreateTeamName.func_146180_a("");
      this.editCreateTeamName.func_146195_b(true);
   }

   protected void func_73869_a(char c, int code) throws IOException {
      if (code == 1) {
         this.switchScreen(MCH_GuiScoreboard_Base.SCREEN_ID.MAIN);
      } else {
         this.editCreateTeamName.func_146201_a(c, code);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.editCreateTeamName.func_146192_a(mouseX, mouseY, mouseButton);
      super.func_73864_a(mouseX, mouseY, mouseButton);
   }

   protected void func_146284_a(GuiButton btn) throws IOException {
      if (btn != null && btn.field_146124_l) {
         switch(btn.field_146127_k) {
         case 528:
            String teamName = this.editCreateTeamName.func_146179_b();
            if (teamName.length() > 0 && teamName.length() <= 16) {
               MCH_PacketIndMultiplayCommand.send(768, "scoreboard teams add " + teamName);
               MCH_PacketIndMultiplayCommand.send(768, "scoreboard teams option " + teamName + " color " + colorNames[this.lastTeamColor]);
               MCH_PacketIndMultiplayCommand.send(768, "scoreboard teams option " + teamName + " friendlyfire " + friendlyFire);
            }

            this.switchScreen(MCH_GuiScoreboard_Base.SCREEN_ID.MAIN);
            break;
         case 544:
            this.switchScreen(MCH_GuiScoreboard_Base.SCREEN_ID.MAIN);
            break;
         case 560:
            friendlyFire = !friendlyFire;
            break;
         case 576:
            ++this.lastTeamColor;
            if (this.lastTeamColor >= colorNames.length) {
               this.lastTeamColor = 0;
            }
            break;
         case 577:
            --this.lastTeamColor;
            if (this.lastTeamColor < 0) {
               this.lastTeamColor = colorNames.length - 1;
            }
         }
      }

   }

   protected void func_146976_a(float par1, int par2, int par3) {
      drawList(this.field_146297_k, this.field_146289_q, true);
      ScaledResolution sr = new W_ScaledResolution(this.field_146297_k, this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d);
      int factor = sr.func_78325_e() > 0 ? sr.func_78325_e() : 1;
      W_McClient.MOD_bindTexture("textures/gui/mp_new_team.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int x = (this.field_146297_k.field_71443_c / factor - 222) / 2;
      int y = (this.field_146297_k.field_71440_d / factor - 200) / 2;
      this.func_73729_b(x, y, 0, 0, 222, 200);
      x = this.field_146297_k.field_71443_c / 2 / factor;
      y = this.field_146297_k.field_71440_d / 2 / factor;
      this.drawCenteredString("Create team", x, y - 85, -1);
      this.drawCenteredString("Team name", x, y - 70, -1);
      TextFormatting ecf = TextFormatting.func_96300_b(colorNames[this.lastTeamColor]);
      this.drawCenteredString(ecf + "Team Color" + ecf, x, y - 13, -1);
      this.editCreateTeamName.func_146194_f();
   }
}
