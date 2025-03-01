package com.norwood.mcheli.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.MCH_IRecipeList;
import com.norwood.mcheli.MCH_ItemRecipe;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.MCH_Recipes;
import com.norwood.mcheli.__helper.client.renderer.GlUtil;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_RenderAircraft;
import com.norwood.mcheli.gui.MCH_GuiSliderVertical;
import com.norwood.mcheli.helicopter.MCH_HeliInfoManager;
import com.norwood.mcheli.plane.MCP_PlaneInfoManager;
import com.norwood.mcheli.tank.MCH_TankInfoManager;
import com.norwood.mcheli.vehicle.MCH_VehicleInfoManager;
import com.norwood.mcheli.wrapper.W_GuiButton;
import com.norwood.mcheli.wrapper.W_GuiContainer;
import com.norwood.mcheli.wrapper.W_KeyBinding;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class MCH_DraftingTableGui extends W_GuiContainer {
   private final EntityPlayer thePlayer;
   private MCH_GuiSliderVertical listSlider;
   private GuiButton buttonCreate;
   private GuiButton buttonNext;
   private GuiButton buttonPrev;
   private GuiButton buttonNextPage;
   private GuiButton buttonPrevPage;
   private int drawFace;
   private int buttonClickWait;
   public static final int RECIPE_HELI = 0;
   public static final int RECIPE_PLANE = 1;
   public static final int RECIPE_VEHICLE = 2;
   public static final int RECIPE_TANK = 3;
   public static final int RECIPE_ITEM = 4;
   public MCH_IRecipeList currentList;
   public MCH_CurrentRecipe current;
   public static final int BUTTON_HELI = 10;
   public static final int BUTTON_PLANE = 11;
   public static final int BUTTON_VEHICLE = 12;
   public static final int BUTTON_TANK = 13;
   public static final int BUTTON_ITEM = 14;
   public static final int BUTTON_NEXT = 20;
   public static final int BUTTON_PREV = 21;
   public static final int BUTTON_CREATE = 30;
   public static final int BUTTON_SELECT = 40;
   public static final int BUTTON_NEXT_PAGE = 50;
   public static final int BUTTON_PREV_PAGE = 51;
   public List<List<GuiButton>> screenButtonList;
   public int screenId = 0;
   public static final int SCREEN_MAIN = 0;
   public static final int SCREEN_LIST = 1;
   public static float modelZoom = 1.0F;
   public static float modelRotX = 0.0F;
   public static float modelRotY = 0.0F;
   public static float modelPosX = 0.0F;
   public static float modelPosY = 0.0F;

   public MCH_DraftingTableGui(EntityPlayer player, int posX, int posY, int posZ) {
      super(new MCH_DraftingTableGuiContainer(player, posX, posY, posZ));
      this.thePlayer = player;
      this.field_146999_f = 400;
      this.field_147000_g = 240;
      this.screenButtonList = new ArrayList();
      this.drawFace = 0;
      this.buttonClickWait = 0;
      MCH_Lib.DbgLog(player.field_70170_p, "MCH_DraftingTableGui.MCH_DraftingTableGui");
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      this.screenButtonList.clear();
      this.screenButtonList.add(new ArrayList());
      this.screenButtonList.add(new ArrayList());
      List<GuiButton> list = (List)this.screenButtonList.get(0);
      GuiButton btnHeli = new GuiButton(10, this.field_147003_i + 20, this.field_147009_r + 20, 90, 20, "Helicopter List");
      GuiButton btnPlane = new GuiButton(11, this.field_147003_i + 20, this.field_147009_r + 40, 90, 20, "Plane List");
      GuiButton btnVehicle = new GuiButton(12, this.field_147003_i + 20, this.field_147009_r + 60, 90, 20, "Vehicle List");
      GuiButton btnTank = new GuiButton(13, this.field_147003_i + 20, this.field_147009_r + 80, 90, 20, "Tank List");
      GuiButton btnItem = new GuiButton(14, this.field_147003_i + 20, this.field_147009_r + 100, 90, 20, "Item List");
      btnHeli.field_146124_l = MCH_HeliInfoManager.getInstance().getRecipeListSize() > 0;
      btnPlane.field_146124_l = MCP_PlaneInfoManager.getInstance().getRecipeListSize() > 0;
      btnVehicle.field_146124_l = MCH_VehicleInfoManager.getInstance().getRecipeListSize() > 0;
      btnTank.field_146124_l = MCH_TankInfoManager.getInstance().getRecipeListSize() > 0;
      btnItem.field_146124_l = MCH_ItemRecipe.getInstance().getRecipeListSize() > 0;
      list.add(btnHeli);
      list.add(btnPlane);
      list.add(btnVehicle);
      list.add(btnTank);
      list.add(btnItem);
      this.buttonCreate = new GuiButton(30, this.field_147003_i + 120, this.field_147009_r + 89, 50, 20, "Create");
      this.buttonPrev = new GuiButton(21, this.field_147003_i + 120, this.field_147009_r + 111, 36, 20, "<<");
      this.buttonNext = new GuiButton(20, this.field_147003_i + 155, this.field_147009_r + 111, 35, 20, ">>");
      list.add(this.buttonCreate);
      list.add(this.buttonPrev);
      list.add(this.buttonNext);
      this.buttonPrevPage = new GuiButton(51, this.field_147003_i + 210, this.field_147009_r + 210, 60, 20, "Prev Page");
      this.buttonNextPage = new GuiButton(50, this.field_147003_i + 270, this.field_147009_r + 210, 60, 20, "Next Page");
      list.add(this.buttonPrevPage);
      list.add(this.buttonNextPage);
      list = (List)this.screenButtonList.get(1);
      int y = 0;

      int i;
      int j;
      for(i = 0; y < 3; ++y) {
         for(j = 0; j < 2; ++i) {
            int px = this.field_147003_i + 30 + j * 140;
            int py = this.field_147009_r + 40 + y * 70;
            list.add(new GuiButton(40 + i, px, py, 45, 20, "Select"));
            ++j;
         }
      }

      this.listSlider = new MCH_GuiSliderVertical(0, this.field_147003_i + 360, this.field_147009_r + 20, 20, 200, "", 0.0F, 0.0F, 0.0F, 1.0F);
      list.add(this.listSlider);

      for(i = 0; i < this.screenButtonList.size(); ++i) {
         list = (List)this.screenButtonList.get(i);

         for(j = 0; j < list.size(); ++j) {
            this.field_146292_n.add(list.get(j));
         }
      }

      this.switchScreen(0);
      initModelTransform();
      modelRotX = 180.0F;
      modelRotY = 90.0F;
      if (MCH_ItemRecipe.getInstance().getRecipeListSize() > 0) {
         this.switchRecipeList(MCH_ItemRecipe.getInstance());
      } else if (MCH_HeliInfoManager.getInstance().getRecipeListSize() > 0) {
         this.switchRecipeList(MCH_HeliInfoManager.getInstance());
      } else if (MCP_PlaneInfoManager.getInstance().getRecipeListSize() > 0) {
         this.switchRecipeList(MCP_PlaneInfoManager.getInstance());
      } else if (MCH_VehicleInfoManager.getInstance().getRecipeListSize() > 0) {
         this.switchRecipeList(MCH_VehicleInfoManager.getInstance());
      } else if (MCH_TankInfoManager.getInstance().getRecipeListSize() > 0) {
         this.switchRecipeList(MCH_TankInfoManager.getInstance());
      } else {
         this.switchRecipeList(MCH_ItemRecipe.getInstance());
      }

   }

   public static void initModelTransform() {
      modelRotX = 0.0F;
      modelRotY = 0.0F;
      modelPosX = 0.0F;
      modelPosY = 0.0F;
      modelZoom = 1.0F;
   }

   public void updateListSliderSize(int listSize) {
      int s = listSize / 2;
      if (listSize % 2 != 0) {
         ++s;
      }

      if (s > 3) {
         this.listSlider.valueMax = (float)(s - 3);
      } else {
         this.listSlider.valueMax = 0.0F;
      }

      this.listSlider.setSliderValue(0.0F);
   }

   public void switchScreen(int id) {
      this.screenId = id;

      for(int i = 0; i < this.field_146292_n.size(); ++i) {
         W_GuiButton.setVisible((GuiButton)this.field_146292_n.get(i), false);
      }

      if (id < this.screenButtonList.size()) {
         List<GuiButton> list = (List)this.screenButtonList.get(id);
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            GuiButton b = (GuiButton)var3.next();
            W_GuiButton.setVisible(b, true);
         }
      }

      if (this.getScreenId() == 0 && this.current != null && this.current.getDescMaxPage() > 1) {
         W_GuiButton.setVisible(this.buttonNextPage, true);
         W_GuiButton.setVisible(this.buttonPrevPage, true);
      } else {
         W_GuiButton.setVisible(this.buttonNextPage, false);
         W_GuiButton.setVisible(this.buttonPrevPage, false);
      }

   }

   public void setCurrentRecipe(MCH_CurrentRecipe currentRecipe) {
      modelPosX = 0.0F;
      modelPosY = 0.0F;
      if (this.current == null || currentRecipe == null || !this.current.recipe.func_77571_b().func_77969_a(currentRecipe.recipe.func_77571_b())) {
         this.drawFace = 0;
      }

      this.current = currentRecipe;
      if (this.getScreenId() == 0 && this.current != null && this.current.getDescMaxPage() > 1) {
         W_GuiButton.setVisible(this.buttonNextPage, true);
         W_GuiButton.setVisible(this.buttonPrevPage, true);
      } else {
         W_GuiButton.setVisible(this.buttonNextPage, false);
         W_GuiButton.setVisible(this.buttonPrevPage, false);
      }

      this.updateEnableCreateButton();
   }

   public MCH_IRecipeList getCurrentList() {
      return this.currentList;
   }

   public void switchRecipeList(MCH_IRecipeList list) {
      if (this.getCurrentList() != list) {
         this.setCurrentRecipe(new MCH_CurrentRecipe(list, 0));
         this.currentList = list;
         this.updateListSliderSize(list.getRecipeListSize());
      } else {
         this.listSlider.setSliderValue((float)(this.current.index / 2));
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.buttonClickWait > 0) {
         --this.buttonClickWait;
      }

   }

   public void func_146281_b() {
      super.func_146281_b();
      MCH_Lib.DbgLog(this.thePlayer.field_70170_p, "MCH_DraftingTableGui.onGuiClosed");
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (this.buttonClickWait <= 0) {
         if (button.field_146124_l) {
            this.buttonClickWait = 3;
            int index = false;
            int page = this.current.getDescCurrentPage();
            int index;
            switch(button.field_146127_k) {
            case 10:
               initModelTransform();
               modelRotX = 180.0F;
               modelRotY = 90.0F;
               this.switchRecipeList(MCH_HeliInfoManager.getInstance());
               this.switchScreen(1);
               break;
            case 11:
               initModelTransform();
               modelRotX = 90.0F;
               modelRotY = 180.0F;
               this.switchRecipeList(MCP_PlaneInfoManager.getInstance());
               this.switchScreen(1);
               break;
            case 12:
               initModelTransform();
               modelRotX = 180.0F;
               modelRotY = 90.0F;
               this.switchRecipeList(MCH_VehicleInfoManager.getInstance());
               this.switchScreen(1);
               break;
            case 13:
               initModelTransform();
               modelRotX = 180.0F;
               modelRotY = 90.0F;
               this.switchRecipeList(MCH_TankInfoManager.getInstance());
               this.switchScreen(1);
               break;
            case 14:
               this.switchRecipeList(MCH_ItemRecipe.getInstance());
               this.switchScreen(1);
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 46:
            case 47:
            case 48:
            case 49:
            default:
               break;
            case 20:
               if (this.current.isCurrentPageTexture()) {
                  page = 0;
               }

               index = (this.current.index + 1) % this.getCurrentList().getRecipeListSize();
               this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
               this.current.setDescCurrentPage(page);
               break;
            case 21:
               if (this.current.isCurrentPageTexture()) {
                  page = 0;
               }

               index = this.current.index - 1;
               if (index < 0) {
                  index = this.getCurrentList().getRecipeListSize() - 1;
               }

               this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
               this.current.setDescCurrentPage(page);
               break;
            case 30:
               MCH_DraftingTableCreatePacket.send(this.current.recipe);
               break;
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
               index = (int)this.listSlider.getSliderValue() * 2 + (button.field_146127_k - 40);
               if (index < this.getCurrentList().getRecipeListSize()) {
                  this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
                  this.switchScreen(0);
               }
               break;
            case 50:
               if (this.current != null) {
                  this.current.switchNextPage();
               }
               break;
            case 51:
               if (this.current != null) {
                  this.current.switchPrevPage();
               }
            }

         }
      }
   }

   private void updateEnableCreateButton() {
      MCH_DraftingTableGuiContainer container = (MCH_DraftingTableGuiContainer)this.field_147002_h;
      this.buttonCreate.field_146124_l = false;
      if (!container.func_75139_a(container.outputSlotIndex).func_75216_d()) {
         this.buttonCreate.field_146124_l = MCH_Recipes.canCraft(this.thePlayer, this.current.recipe);
      }

      if (this.thePlayer.field_71075_bZ.field_75098_d) {
         this.buttonCreate.field_146124_l = true;
      }

   }

   protected void func_73869_a(char par1, int keycode) throws IOException {
      if (keycode == 1 || keycode == W_KeyBinding.getKeyCode(Minecraft.func_71410_x().field_71474_y.field_151445_Q)) {
         if (this.getScreenId() == 0) {
            this.field_146297_k.field_71439_g.func_71053_j();
         } else {
            this.switchScreen(0);
         }
      }

      if (this.getScreenId() == 0) {
         if (keycode == 205) {
            this.func_146284_a(this.buttonNext);
         }

         if (keycode == 203) {
            this.func_146284_a(this.buttonPrev);
         }
      } else if (this.getScreenId() == 1) {
         if (keycode == 200) {
            this.listSlider.scrollDown(1.0F);
         }

         if (keycode == 208) {
            this.listSlider.scrollUp(1.0F);
         }
      }

   }

   protected void func_146979_b(int mx, int my) {
      super.func_146979_b(mx, my);
      this.field_73735_i = 0.0F;
      GlStateManager.func_179147_l();
      int i;
      if (this.getScreenId() == 0) {
         ArrayList<String> list = new ArrayList();
         if (this.current != null) {
            if (this.current.isCurrentPageTexture()) {
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               this.field_146297_k.func_110434_K().func_110577_a(this.current.getCurrentPageTexture());
               this.drawTexturedModalRect(210, 20, 170, 190, 0, 0, 340, 380);
            } else if (this.current.isCurrentPageAcInfo()) {
               for(i = 0; i < this.current.infoItem.size(); ++i) {
                  this.field_146289_q.func_78276_b((String)this.current.infoItem.get(i), 210, 40 + 10 * i, -9491968);
                  String data = (String)this.current.infoData.get(i);
                  if (!data.isEmpty()) {
                     this.field_146289_q.func_78276_b(data, 280, 40 + 10 * i, -9491968);
                  }
               }
            } else {
               W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
               this.drawTexturedModalRect(340, 215, 45, 15, 400, 60, 90, 30);
               if (mx >= 350 && mx <= 400 && my >= 214 && my <= 230) {
                  boolean lb = Mouse.isButtonDown(0);
                  boolean rb = Mouse.isButtonDown(1);
                  boolean mb = Mouse.isButtonDown(2);
                  list.add((lb ? TextFormatting.AQUA : "") + "Mouse left button drag : Rotation model");
                  list.add((rb ? TextFormatting.AQUA : "") + "Mouse right button drag : Zoom model");
                  list.add((mb ? TextFormatting.AQUA : "") + "Mouse middle button drag : Move model");
               }
            }
         }

         this.drawString(this.current.displayName, 120, 20, -1);
         this.drawItemRecipe(this.current.recipe, 121, 34);
         if (list.size() > 0) {
            this.drawHoveringText(list, mx - 30, my - 0, this.field_146289_q);
         }
      }

      if (this.getScreenId() == 1) {
         int index = 2 * (int)this.listSlider.getSliderValue();
         i = 0;

         int rx;
         int ry;
         int r;
         int c;
         for(r = 0; r < 3; ++r) {
            for(c = 0; c < 2; ++c) {
               if (index + i < this.getCurrentList().getRecipeListSize()) {
                  rx = 110 + 140 * c;
                  ry = 20 + 70 * r;
                  String s = this.getCurrentList().getRecipe(index + i).func_77571_b().func_82833_r();
                  this.drawCenteredString(s, rx, ry, -1);
               }

               ++i;
            }
         }

         W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
         i = 0;

         for(r = 0; r < 3; ++r) {
            for(c = 0; c < 2; ++c) {
               if (index + i < this.getCurrentList().getRecipeListSize()) {
                  rx = 80 + 140 * c - 1;
                  ry = 30 + 70 * r - 1;
                  this.func_73729_b(rx, ry, 400, 0, 75, 54);
               }

               ++i;
            }
         }

         i = 0;

         for(r = 0; r < 3; ++r) {
            for(c = 0; c < 2; ++c) {
               if (index + i < this.getCurrentList().getRecipeListSize()) {
                  rx = 80 + 140 * c;
                  ry = 30 + 70 * r;
                  this.drawItemRecipe(this.getCurrentList().getRecipe(index + i), rx, ry);
               }

               ++i;
            }
         }
      }

   }

   protected void func_184098_a(Slot slotIn, int slotId, int clickedButton, ClickType clickType) {
      if (this.getScreenId() != 1) {
         super.func_184098_a(slotIn, slotId, clickedButton, clickType);
      }

   }

   private int getScreenId() {
      return this.screenId;
   }

   public void drawItemRecipe(IRecipe recipe, int x, int y) {
      if (recipe != null) {
         if (!recipe.func_77571_b().func_190926_b()) {
            if (recipe.func_77571_b().func_77973_b() != null) {
               RenderHelper.func_74520_c();
               NonNullList<Ingredient> ingredients = recipe.func_192400_c();

               for(int i = 0; i < ingredients.size(); ++i) {
                  this.drawIngredient((Ingredient)ingredients.get(i), x + i % 3 * 18, y + i / 3 * 18);
               }

               this.drawItemStack(recipe.func_77571_b(), x + 54 + 3, y + 18);
               RenderHelper.func_74518_a();
            }
         }
      }
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      int dx = Mouse.getEventDX();
      int dy = Mouse.getEventDY();
      if (this.getScreenId() == 0 && Mouse.getX() > this.field_146297_k.field_71443_c / 2) {
         if (Mouse.isButtonDown(0) && (dx != 0 || dy != 0)) {
            modelRotX = (float)((double)modelRotX - (double)dy / 2.0D);
            modelRotY = (float)((double)modelRotY - (double)dx / 2.0D);
            if (modelRotX > 360.0F) {
               modelRotX -= 360.0F;
            }

            if (modelRotX < -360.0F) {
               modelRotX += 360.0F;
            }

            if (modelRotY > 360.0F) {
               modelRotY -= 360.0F;
            }

            if (modelRotY < -360.0F) {
               modelRotY += 360.0F;
            }
         }

         if (Mouse.isButtonDown(2) && (dx != 0 || dy != 0)) {
            modelPosX = (float)((double)modelPosX + (double)dx / 2.0D);
            modelPosY = (float)((double)modelPosY - (double)dy / 2.0D);
            if (modelRotX > 1000.0F) {
               modelRotX = 1000.0F;
            }

            if (modelRotX < -1000.0F) {
               modelRotX = -1000.0F;
            }

            if (modelRotY > 1000.0F) {
               modelRotY = 1000.0F;
            }

            if (modelRotY < -1000.0F) {
               modelRotY = -1000.0F;
            }
         }

         if (Mouse.isButtonDown(1) && dy != 0) {
            modelZoom = (float)((double)modelZoom + (double)dy / 100.0D);
            if ((double)modelZoom < 0.1D) {
               modelZoom = 0.1F;
            }

            if (modelZoom > 10.0F) {
               modelZoom = 10.0F;
            }
         }
      }

      int wheel = Mouse.getEventDWheel();
      if (wheel != 0) {
         if (this.getScreenId() == 1) {
            if (wheel > 0) {
               this.listSlider.scrollDown(1.0F);
            } else if (wheel < 0) {
               this.listSlider.scrollUp(1.0F);
            }
         } else if (this.getScreenId() == 0) {
            if (wheel > 0) {
               this.func_146284_a(this.buttonPrev);
            } else if (wheel < 0) {
               this.func_146284_a(this.buttonNext);
            }
         }
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.func_179147_l();
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.getScreenId() == 0) {
         super.func_73863_a(mouseX, mouseY, partialTicks);
      } else {
         List<Slot> inventory = this.field_147002_h.field_75151_b;
         this.field_147002_h.field_75151_b = new ArrayList();
         super.func_73863_a(mouseX, mouseY, partialTicks);
         this.field_147002_h.field_75151_b = inventory;
      }

      if (this.getScreenId() == 0 && this.current.isCurrentPageModel()) {
         RenderHelper.func_74520_c();
         this.drawModel(partialTicks);
      }

   }

   public void drawModel(float partialTicks) {
      W_ModelCustom model = this.current.getModel();
      double scl = 162.0D / ((double)MathHelper.func_76135_e(model.size) < 0.01D ? 0.01D : (double)model.size);
      this.field_146297_k.func_110434_K().func_110577_a(this.current.getModelTexture());
      GlStateManager.func_179094_E();
      double cx = (double)(model.maxX - model.minX) * 0.5D + (double)model.minX;
      double cy = (double)(model.maxY - model.minY) * 0.5D + (double)model.minY;
      double cz = (double)(model.maxZ - model.minZ) * 0.5D + (double)model.minZ;
      if (this.current.modelRot == 0) {
         GlStateManager.func_179137_b(cx * scl, cz * scl, 0.0D);
      } else {
         GlStateManager.func_179137_b(cz * scl, cy * scl, 0.0D);
      }

      GlStateManager.func_179137_b((double)((float)(this.field_147003_i + 300) + modelPosX), (double)((float)(this.field_147009_r + 110) + modelPosY), 550.0D);
      GlStateManager.func_179114_b(modelRotX, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(modelRotY, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179139_a(scl * (double)modelZoom, scl * (double)modelZoom, -scl * (double)modelZoom);
      GlStateManager.func_179101_C();
      GlStateManager.func_179140_f();
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      int faceNum = model.getFaceNum();
      if (this.drawFace < faceNum * 2) {
         GlStateManager.func_179131_c(0.1F, 0.1F, 0.1F, 1.0F);
         GlStateManager.func_179090_x();
         GlUtil.polygonMode(CullFace.FRONT_AND_BACK, GlUtil.RasterizeType.LINE);
         GlUtil.pushLineWidth(1.0F);
         model.renderAll(this.drawFace - faceNum, this.drawFace);
         MCH_RenderAircraft.renderCrawlerTrack((MCH_EntityAircraft)null, this.current.getAcInfo(), partialTicks);
         GlUtil.popLineWidth();
         GlUtil.polygonMode(CullFace.FRONT_AND_BACK, GlUtil.RasterizeType.FILL);
         GlStateManager.func_179098_w();
      }

      if (this.drawFace >= faceNum) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         model.renderAll(0, this.drawFace - faceNum);
         MCH_RenderAircraft.renderCrawlerTrack((MCH_EntityAircraft)null, this.current.getAcInfo(), partialTicks);
      }

      GlStateManager.func_179091_B();
      GlStateManager.func_179145_e();
      GlStateManager.func_179121_F();
      if (this.drawFace < 10000000) {
         this.drawFace = (int)((float)this.drawFace + 20.0F);
      }

   }

   protected void func_146976_a(float var1, int var2, int var3) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      float z = this.field_73735_i;
      this.field_73735_i = 0.0F;
      W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
      if (this.getScreenId() == 0) {
         this.func_73729_b(this.field_147003_i, this.field_147009_r, 0, 0, this.field_146999_f, this.field_147000_g);
      }

      if (this.getScreenId() == 1) {
         this.func_73729_b(this.field_147003_i, this.field_147009_r, 0, this.field_147000_g, this.field_146999_f, this.field_147000_g);
         List<GuiButton> list = (List)this.screenButtonList.get(1);
         int index = (int)this.listSlider.getSliderValue() * 2;

         for(int i = 0; i < 6; ++i) {
            W_GuiButton.setVisible((GuiButton)list.get(i), index + i < this.getCurrentList().getRecipeListSize());
         }
      }

      this.field_73735_i = z;
   }

   public void func_73729_b(int par1, int par2, int par3, int par4, int par5, int par6) {
      float w = 0.001953125F;
      float h = 0.001953125F;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      buffer.func_181662_b((double)(par1 + 0), (double)(par2 + par6), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + 0) * w), (double)((float)(par4 + par6) * h)).func_181675_d();
      buffer.func_181662_b((double)(par1 + par5), (double)(par2 + par6), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + par5) * w), (double)((float)(par4 + par6) * h)).func_181675_d();
      buffer.func_181662_b((double)(par1 + par5), (double)(par2 + 0), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + par5) * w), (double)((float)(par4 + 0) * h)).func_181675_d();
      buffer.func_181662_b((double)(par1 + 0), (double)(par2 + 0), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + 0) * w), (double)((float)(par4 + 0) * h)).func_181675_d();
      tessellator.func_78381_a();
   }

   public void drawTexturedModalRect(int dx, int dy, int dw, int dh, int u, int v, int tw, int th) {
      float w = 0.001953125F;
      float h = 0.001953125F;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      buffer.func_181662_b((double)(dx + 0), (double)(dy + dh), (double)this.field_73735_i).func_187315_a((double)((float)(u + 0) * w), (double)((float)(v + th) * h)).func_181675_d();
      buffer.func_181662_b((double)(dx + dw), (double)(dy + dh), (double)this.field_73735_i).func_187315_a((double)((float)(u + tw) * w), (double)((float)(v + th) * h)).func_181675_d();
      buffer.func_181662_b((double)(dx + dw), (double)(dy + 0), (double)this.field_73735_i).func_187315_a((double)((float)(u + tw) * w), (double)((float)(v + 0) * h)).func_181675_d();
      buffer.func_181662_b((double)(dx + 0), (double)(dy + 0), (double)this.field_73735_i).func_187315_a((double)((float)(u + 0) * w), (double)((float)(v + 0) * h)).func_181675_d();
      tessellator.func_78381_a();
   }

   public void drawTexturedModalRectWithColor(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int color) {
      float w = 0.001953125F;
      float h = 0.001953125F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      float f3 = (float)(color >> 24 & 255) / 255.0F;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buf = tessellator.func_178180_c();
      buf.func_181668_a(7, DefaultVertexFormats.field_181709_i);
      buf.func_181662_b((double)x, (double)(y + height), (double)this.field_73735_i).func_187315_a((double)((float)u * w), (double)((float)(v + vHeight) * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
      buf.func_181662_b((double)(x + width), (double)(y + height), (double)this.field_73735_i).func_187315_a((double)((float)(u + uWidth) * w), (double)((float)(v + vHeight) * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
      buf.func_181662_b((double)(x + width), (double)y, (double)this.field_73735_i).func_187315_a((double)((float)(u + uWidth) * w), (double)((float)v * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
      buf.func_181662_b((double)x, (double)y, (double)this.field_73735_i).func_187315_a((double)((float)u * w), (double)((float)v * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
      tessellator.func_78381_a();
   }
}
