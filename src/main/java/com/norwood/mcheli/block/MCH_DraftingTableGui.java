package com.norwood.mcheli.block;

import com.norwood.mcheli.MCH_IRecipeList;
import com.norwood.mcheli.MCH_ItemRecipe;
import com.norwood.mcheli.aircraft.MCH_RenderAircraft;
import com.norwood.mcheli.gui.MCH_GuiSliderVertical;
import com.norwood.mcheli.helicopter.MCH_HeliInfoManager;
import com.norwood.mcheli.helper.MCH_Logger;
import com.norwood.mcheli.helper.MCH_Recipes;
import com.norwood.mcheli.networking.packet.PacketDrafttingTableCreate;
import com.norwood.mcheli.plane.MCP_PlaneInfoManager;
import com.norwood.mcheli.ship.MCH_ShipInfoManager;
import com.norwood.mcheli.tank.MCH_TankInfoManager;
import com.norwood.mcheli.vehicle.MCH_VehicleInfoManager;
import com.norwood.mcheli.wrapper.W_GuiButton;
import com.norwood.mcheli.wrapper.W_GuiContainer;
import com.norwood.mcheli.wrapper.W_KeyBinding;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MCH_DraftingTableGui extends W_GuiContainer {

    public static final int RECIPE_HELI = 0;
    public static final int RECIPE_PLANE = 1;
    public static final int RECIPE_VEHICLE = 2;
    public static final int RECIPE_TANK = 3;
    public static final int RECIPE_ITEM = 4;
    public static final int RECIPE_SHIP = 5;
    public static final int BUTTON_HELI = 10;
    public static final int BUTTON_PLANE = 11;
    public static final int BUTTON_VEHICLE = 12;
    public static final int BUTTON_TANK = 13;
    public static final int BUTTON_ITEM = 14;
    public static final int BUTTON_SHIP = 15;
    public static final int BUTTON_NEXT = 20;
    public static final int BUTTON_PREV = 21;
    public static final int BUTTON_CREATE = 30;
    public static final int BUTTON_SELECT = 40;
    public static final int BUTTON_NEXT_PAGE = 50;
    public static final int BUTTON_PREV_PAGE = 51;
    public static final int SCREEN_MAIN = 0;
    public static final int SCREEN_LIST = 1;
    public static float modelZoom = 1.0F;
    public static float modelRotX = 0.0F;
    public static float modelRotY = 0.0F;
    public static float modelPosX = 0.0F;
    public static float modelPosY = 0.0F;
    private final EntityPlayer thePlayer;
    public MCH_IRecipeList currentList;
    public MCH_CurrentRecipe current;
    public final List<List<GuiButton>> screenButtonList;
    public int screenId = 0;
    private GuiTextField searchField;
    private MCH_GuiSliderVertical listSlider;
    private GuiButton buttonCreate;
    private GuiButton buttonNext;
    private GuiButton buttonPrev;
    private GuiButton buttonNextPage;
    private GuiButton buttonPrevPage;
    private int drawFace;
    private int buttonClickWait;

    public MCH_DraftingTableGui(EntityPlayer player, int posX, int posY, int posZ) {
        super(new MCH_DraftingTableGuiContainer(player, posX, posY, posZ));
        this.thePlayer = player;
        this.xSize = 400;
        this.ySize = 240;
        this.screenButtonList = new ArrayList<>();
        this.drawFace = 0;
        this.buttonClickWait = 0;
        MCH_Logger.debugLog(player.world, "MCH_DraftingTableGui.MCH_DraftingTableGui");
    }

    public static void initModelTransform() {
        modelRotX = 0.0F;
        modelRotY = 0.0F;
        modelPosX = 0.0F;
        modelPosY = 0.0F;
        modelZoom = 1.0F;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        super.initGui();
        this.buttonList.clear();
        this.screenButtonList.clear();
        this.screenButtonList.add(new ArrayList<>());
        this.screenButtonList.add(new ArrayList<>());
        List<GuiButton> list = this.screenButtonList.get(0);
        GuiButton btnHeli = new GuiButton(BUTTON_HELI, this.guiLeft + 20, this.guiTop + 20, 90, 20, "Helicopter List");
        GuiButton btnPlane = new GuiButton(BUTTON_PLANE, this.guiLeft + 20, this.guiTop + 40, 90, 20, "Plane List");
        GuiButton btnVehicle = new GuiButton(BUTTON_VEHICLE, this.guiLeft + 20, this.guiTop + 60, 90, 20, "Vehicle List");
        GuiButton btnTank = new GuiButton(BUTTON_TANK, this.guiLeft + 20, this.guiTop + 80, 90, 20, "Tank List");
        GuiButton btnItem = new GuiButton(BUTTON_ITEM, this.guiLeft + 20, this.guiTop + 100, 90, 20, "Item List");
        GuiButton btnShip = new GuiButton(BUTTON_SHIP, this.guiLeft + 20, this.guiTop + 120, 90, 20, "Ship List");
        btnHeli.enabled = MCH_HeliInfoManager.getInstance().getRecipeListSize() > 0;
        btnPlane.enabled = MCP_PlaneInfoManager.getInstance().getRecipeListSize() > 0;
        btnVehicle.enabled = MCH_VehicleInfoManager.getInstance().getRecipeListSize() > 0;
        btnTank.enabled = MCH_TankInfoManager.getInstance().getRecipeListSize() > 0;
        btnItem.enabled = MCH_ItemRecipe.getInstance().getRecipeListSize() > 0;
        btnShip.enabled = MCH_ShipInfoManager.getInstance().getRecipeListSize() > 0;
        list.add(btnHeli);
        list.add(btnPlane);
        list.add(btnVehicle);
        list.add(btnTank);
        list.add(btnItem);
        list.add(btnShip);
        this.buttonCreate = new GuiButton(BUTTON_CREATE, this.guiLeft + 120, this.guiTop + 89, 50, 20, "Create");
        this.buttonPrev = new GuiButton(BUTTON_PREV, this.guiLeft + 120, this.guiTop + 111, 36, 20, "<<");
        this.buttonNext = new GuiButton(BUTTON_NEXT, this.guiLeft + 155, this.guiTop + 111, 35, 20, ">>");
        list.add(this.buttonCreate);
        list.add(this.buttonPrev);
        list.add(this.buttonNext);
        this.buttonPrevPage = new GuiButton(BUTTON_PREV_PAGE, this.guiLeft + 210, this.guiTop + 210, 60, 20, "Prev Page");
        this.buttonNextPage = new GuiButton(BUTTON_NEXT_PAGE, this.guiLeft + 270, this.guiTop + 210, 60, 20, "Next Page");
        list.add(this.buttonPrevPage);
        list.add(this.buttonNextPage);
        list = this.screenButtonList.get(1);

        for (int i = 0; i < 6; i++) {
            int px = this.guiLeft + 30 + (i % 2) * 140;
            int py = this.guiTop + 40 + (i / 2) * 70;
            list.add(new GuiButton(BUTTON_SELECT + i, px, py, 45, 20, "Select"));
        }

        this.listSlider = new MCH_GuiSliderVertical(0, this.guiLeft + 360, this.guiTop + 20, 20, 200, "", 0.0F, 0.0F,
                0.0F, 1.0F);
        list.add(this.listSlider);

        for (List<GuiButton> guiButtons : this.screenButtonList) {
            this.buttonList.addAll(guiButtons);
        }

        this.searchField = new GuiTextField(0, this.fontRenderer, this.guiLeft + 10, this.guiTop + 5, 100, 12);
        this.searchField.setMaxStringLength(50);
        this.searchField.setEnableBackgroundDrawing(true);
        this.searchField.setVisible(true);
        this.searchField.setFocused(false);

        this.switchScreen(0);
        initModelTransform();
        modelRotX = 180.0F;
        modelRotY = 90.0F;
        this.switchRecipeList(this.defaultRecipeList());
    }

    private List<MCH_IRecipeList> allRecipeLists() {
        return Arrays.asList(
                MCH_ItemRecipe.getInstance(),
                MCH_HeliInfoManager.getInstance(),
                MCP_PlaneInfoManager.getInstance(),
                MCH_VehicleInfoManager.getInstance(),
                MCH_TankInfoManager.getInstance(),
                MCH_ShipInfoManager.getInstance());
    }

    private MCH_IRecipeList defaultRecipeList() {
        for (MCH_IRecipeList list : this.allRecipeLists()) {
            if (list.getRecipeListSize() > 0) {
                return list;
            }
        }
        return MCH_ItemRecipe.getInstance();
    }

    private void applySearch(String searchText) {
        if (searchText.isEmpty()) {
            this.switchRecipeList(this.defaultRecipeList());
            return;
        }

        FilteredRecipeList filtered = new FilteredRecipeList(this.allRecipeLists(), searchText);
        if (filtered.getRecipeListSize() > 0) {
            this.switchRecipeList(filtered);
            this.switchScreen(SCREEN_LIST);
        }
    }

    public void updateListSliderSize(int listSize) {
        int s = listSize / 2;
        if (listSize % 2 != 0) {
            s++;
        }

        if (s > 3) {
            this.listSlider.valueMax = s - 3;
        } else {
            this.listSlider.valueMax = 0.0F;
        }

        this.listSlider.setSliderValue(0.0F);
    }

    public void switchScreen(int id) {
        this.screenId = id;

        for (GuiButton guiButton : this.buttonList) {
            W_GuiButton.setVisible(guiButton, false);
        }

        if (id < this.screenButtonList.size()) {
            for (GuiButton b : this.screenButtonList.get(id)) {
                W_GuiButton.setVisible(b, true);
            }
        }

        this.updatePageButtonVisibility();
    }

    private void updatePageButtonVisibility() {
        boolean show = this.getScreenId() == SCREEN_MAIN && this.current != null && this.current.getDescMaxPage() > 1;
        W_GuiButton.setVisible(this.buttonNextPage, show);
        W_GuiButton.setVisible(this.buttonPrevPage, show);
    }

    public void setCurrentRecipe(MCH_CurrentRecipe currentRecipe) {
        modelPosX = 0.0F;
        modelPosY = 0.0F;

        if (this.current == null || this.current.recipe == null || currentRecipe == null
                || currentRecipe.recipe == null
                || !this.current.recipe.getRecipeOutput().isItemEqual(currentRecipe.recipe.getRecipeOutput())) {
            this.drawFace = 0;
        }

        this.current = currentRecipe;
        this.updatePageButtonVisibility();
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
        } else if (this.current != null) {
            this.listSlider.setSliderValue((float) this.current.index / 2F);
        }
    }

    public void updateScreen() {
        super.updateScreen();
        if (this.searchField != null) {
            this.searchField.updateCursorCounter();
        }
        if (this.buttonClickWait > 0) {
            this.buttonClickWait--;
        }
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        MCH_Logger.debugLog(this.thePlayer.world, "MCH_DraftingTableGui.onGuiClosed");
    }

    protected void actionPerformed(@NotNull GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (this.buttonClickWait <= 0 && button.enabled) {
            this.buttonClickWait = 3;
            switch (button.id) {
                case BUTTON_HELI -> {
                    initModelTransform();
                    modelRotX = 180.0F;
                    modelRotY = 90.0F;
                    this.switchRecipeList(MCH_HeliInfoManager.getInstance());
                    this.switchScreen(SCREEN_LIST);
                }
                case BUTTON_PLANE -> {
                    initModelTransform();
                    modelRotX = 90.0F;
                    modelRotY = 180.0F;
                    this.switchRecipeList(MCP_PlaneInfoManager.getInstance());
                    this.switchScreen(SCREEN_LIST);
                }
                case BUTTON_VEHICLE -> {
                    initModelTransform();
                    modelRotX = 180.0F;
                    modelRotY = 90.0F;
                    this.switchRecipeList(MCH_VehicleInfoManager.getInstance());
                    this.switchScreen(SCREEN_LIST);
                }
                case BUTTON_TANK -> {
                    initModelTransform();
                    modelRotX = 180.0F;
                    modelRotY = 90.0F;
                    this.switchRecipeList(MCH_TankInfoManager.getInstance());
                    this.switchScreen(SCREEN_LIST);
                }
                case BUTTON_ITEM -> {
                    this.switchRecipeList(MCH_ItemRecipe.getInstance());
                    this.switchScreen(SCREEN_LIST);
                }
                case BUTTON_SHIP -> {
                    initModelTransform();
                    modelRotX = 90.0F;
                    modelRotY = 180.0F;
                    this.switchRecipeList(MCH_ShipInfoManager.getInstance());
                    this.switchScreen(SCREEN_LIST);
                }
                case BUTTON_NEXT -> {
                    int page = this.current.getDescCurrentPage();
                    if (this.current.isCurrentPageTexture()) {
                        page = 0;
                    }
                    int index = (this.current.index + 1) % this.getCurrentList().getRecipeListSize();
                    this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
                    this.current.setDescCurrentPage(page);
                }
                case BUTTON_PREV -> {
                    int page = this.current.getDescCurrentPage();
                    if (this.current.isCurrentPageTexture()) {
                        page = 0;
                    }
                    int index = this.current.index - 1;
                    if (index < 0) {
                        index = this.getCurrentList().getRecipeListSize() - 1;
                    }
                    this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
                    this.current.setDescCurrentPage(page);
                }
                case BUTTON_CREATE -> PacketDrafttingTableCreate.send(this.current.recipe);
                case BUTTON_SELECT, 41, 42, 43, 44, 45 -> {
                    int index = (int) this.listSlider.getSliderValue() * 2 + (button.id - BUTTON_SELECT);
                    if (index < this.getCurrentList().getRecipeListSize()) {
                        this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
                        this.switchScreen(SCREEN_MAIN);
                    }
                }
                case BUTTON_NEXT_PAGE -> {
                    if (this.current != null) {
                        this.current.switchNextPage();
                    }
                }
                case BUTTON_PREV_PAGE -> {
                    if (this.current != null) {
                        this.current.switchPrevPage();
                    }
                }
                default -> {
                }
            }
        }
    }

    private void updateEnableCreateButton() {
        this.buttonCreate.enabled = false;
        if (this.current == null || this.current.recipe == null) {
            return;
        }

        MCH_DraftingTableGuiContainer container = (MCH_DraftingTableGuiContainer) this.inventorySlots;
        if (!container.getSlot(container.outputSlotIndex).getHasStack()) {
            this.buttonCreate.enabled = MCH_Recipes.canCraft(this.thePlayer, this.current.recipe);
        }

        if (this.thePlayer.capabilities.isCreativeMode) {
            this.buttonCreate.enabled = true;
        }
    }

    protected void keyTyped(char par1, int keycode) throws IOException {
        if (this.searchField.textboxKeyTyped(par1, keycode)) {
            this.applySearch(this.searchField.getText().trim());
            return;
        }

        if (keycode == 1 || keycode == W_KeyBinding.getKeyCode(Minecraft.getMinecraft().gameSettings.keyBindInventory)) {
            if (this.getScreenId() == SCREEN_MAIN) {
                this.mc.player.closeScreen();
            } else {
                this.switchScreen(SCREEN_MAIN);
            }
        }

        if (this.getScreenId() == SCREEN_MAIN) {
            if (keycode == 205) {
                this.actionPerformed(this.buttonNext);
            }

            if (keycode == 203) {
                this.actionPerformed(this.buttonPrev);
            }
        } else if (this.getScreenId() == SCREEN_LIST) {
            if (keycode == 200) {
                this.listSlider.scrollDown(1.0F);
            }

            if (keycode == 208) {
                this.listSlider.scrollUp(1.0F);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        this.zLevel = 0.0F;
        GlStateManager.enableBlend();
        if (this.getScreenId() == SCREEN_MAIN) {
            ArrayList<String> list = new ArrayList<>();
            if (this.current != null) {
                if (this.current.isCurrentPageTexture()) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.mc.getTextureManager().bindTexture(this.current.getCurrentPageTexture());
                    this.drawTexturedModalRect(210, 20, 170, 190, 0, 0, 340, 380);
                } else if (this.current.isCurrentPageAcInfo()) {
                    for (int i = 0; i < this.current.infoItem.size(); i++) {
                        this.fontRenderer.drawString(this.current.infoItem.get(i), 210, 40 + 10 * i, -9491968);
                        String data = this.current.infoData.get(i);
                        if (!data.isEmpty()) {
                            this.fontRenderer.drawString(data, 280, 40 + 10 * i, -9491968);
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

                this.drawString(this.current.displayName, 120, 20, -1);
                this.drawItemRecipe(this.current.recipe, 121, 34);
            }

            if (!list.isEmpty()) {
                this.drawHoveringText(list, mx - 30, my, this.fontRenderer);
            }
        }

        if (this.getScreenId() == SCREEN_LIST) {
            int index = 2 * (int) this.listSlider.getSliderValue();
            int ix = 0;

            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 2; c++) {
                    if (index + ix < this.getCurrentList().getRecipeListSize()) {
                        int rx = 110 + 140 * c;
                        int ry = 20 + 70 * r;
                        String s = this.getCurrentList().getRecipe(index + ix).getRecipeOutput().getDisplayName();
                        this.drawCenteredString(s, rx, ry, -1);
                    }

                    ix++;
                }
            }

            W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
            ix = 0;

            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 2; c++) {
                    if (index + ix < this.getCurrentList().getRecipeListSize()) {
                        int rx = 80 + 140 * c - 1;
                        int ry = 30 + 70 * r - 1;
                        this.drawTexturedModalRect(rx, ry, 400, 0, 75, 54);
                    }

                    ix++;
                }
            }

            ix = 0;

            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 2; c++) {
                    if (index + ix < this.getCurrentList().getRecipeListSize()) {
                        int rx = 80 + 140 * c;
                        int ry = 30 + 70 * r;
                        this.drawItemRecipe(this.getCurrentList().getRecipe(index + ix), rx, ry);
                    }

                    ix++;
                }
            }
        }
    }

    protected void handleMouseClick(@NotNull Slot slotIn, int slotId, int clickedButton, @NotNull ClickType clickType) {
        if (this.getScreenId() != SCREEN_LIST) {
            super.handleMouseClick(slotIn, slotId, clickedButton, clickType);
        }
    }

    private int getScreenId() {
        return this.screenId;
    }

    public void drawItemRecipe(IRecipe recipe, int x, int y) {
        if (recipe != null && !recipe.getRecipeOutput().isEmpty() && recipe.getRecipeOutput().getItem() != null) {
            RenderHelper.enableGUIStandardItemLighting();
            NonNullList<Ingredient> ingredients = recipe.getIngredients();

            for (int i = 0; i < ingredients.size(); i++) {
                this.drawIngredient(ingredients.get(i), x + i % 3 * 18, y + i / 3 * 18);
            }

            this.drawItemStack(recipe.getRecipeOutput(), x + 54 + 3, y + 18);
            RenderHelper.disableStandardItemLighting();
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dx = Mouse.getEventDX();
        int dy = Mouse.getEventDY();
        if (this.getScreenId() == SCREEN_MAIN && Mouse.getX() > this.mc.displayWidth / 2) {
            if (Mouse.isButtonDown(0) && (dx != 0 || dy != 0)) {
                modelRotX = (float) (modelRotX - dy / 2.0);
                modelRotY = (float) (modelRotY - dx / 2.0);
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
                modelPosX = (float) (modelPosX + dx / 2.0);
                modelPosY = (float) (modelPosY - dy / 2.0);
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
                modelZoom = (float) (modelZoom + dy / 100.0);
                if (modelZoom < 0.1) {
                    modelZoom = 0.1F;
                }

                if (modelZoom > 10.0F) {
                    modelZoom = 10.0F;
                }
            }
        }

        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            if (this.getScreenId() == SCREEN_LIST) {
                if (wheel > 0) {
                    this.listSlider.scrollDown(1.0F);
                } else {
                    this.listSlider.scrollUp(1.0F);
                }
            } else if (this.getScreenId() == SCREEN_MAIN) {
                if (wheel > 0) {
                    this.actionPerformed(this.buttonPrev);
                } else {
                    this.actionPerformed(this.buttonNext);
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.getScreenId() == SCREEN_MAIN) {
            super.drawScreen(mouseX, mouseY, partialTicks);
        } else {
            List<Slot> inventory = this.inventorySlots.inventorySlots;
            this.inventorySlots.inventorySlots = new ArrayList<>();
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.inventorySlots.inventorySlots = inventory;
        }

        if (this.getScreenId() == SCREEN_MAIN && this.current != null && this.current.isCurrentPageModel()) {
            RenderHelper.enableGUIStandardItemLighting();
            this.drawModel(partialTicks);
        }

        this.searchField.drawTextBox();
    }

    public void drawModel(float partialTicks) {
        W_ModelCustom model = this.current.getModel();
        double scl = 162.0 / (MathHelper.abs(model.size) < 0.01 ? 0.01 : model.size);
        this.mc.getTextureManager().bindTexture(this.current.getModelTexture());
        GlStateManager.pushMatrix();
        double cx = (model.maxX - model.minX) * 0.5 + model.minX;
        double cy = (model.maxY - model.minY) * 0.5 + model.minY;
        double cz = (model.maxZ - model.minZ) * 0.5 + model.minZ;
        if (this.current.modelRot == 0) {
            GlStateManager.translate(cx * scl, cz * scl, 0.0);
        } else {
            GlStateManager.translate(cz * scl, cy * scl, 0.0);
        }

        GlStateManager.translate(this.guiLeft + 300 + modelPosX, this.guiTop + 110 + modelPosY, 550.0);
        GlStateManager.rotate(modelRotX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(modelRotY, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(scl * modelZoom, scl * modelZoom, -scl * modelZoom);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        int faceNum = model.getFaceNum();
        if (this.drawFace < faceNum * 2) {
            GlStateManager.color(0.1F, 0.1F, 0.1F, 1.0F);
            GlStateManager.disableTexture2D();
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            float lw = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
            GL11.glLineWidth(1.0F);
            model.renderAll(this.drawFace - faceNum, this.drawFace);
            MCH_RenderAircraft.renderCrawlerTrack(null, this.current.getAcInfo(), partialTicks);
            GL11.glLineWidth(lw);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GlStateManager.enableTexture2D();
        }

        if (this.drawFace >= faceNum) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            model.renderAll(0, this.drawFace - faceNum);
            MCH_RenderAircraft.renderCrawlerTrack(null, this.current.getAcInfo(), partialTicks);
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        if (this.drawFace < 10000000) {
            this.drawFace = (int) (this.drawFace + 20.0F);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float z = this.zLevel;
        this.zLevel = 0.0F;
        W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
        if (this.getScreenId() == SCREEN_MAIN) {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        }

        if (this.getScreenId() == SCREEN_LIST) {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, this.ySize, this.xSize, this.ySize);
            List<GuiButton> list = this.screenButtonList.get(1);
            int index = (int) this.listSlider.getSliderValue() * 2;

            for (int i = 0; i < 6; i++) {
                W_GuiButton.setVisible(list.get(i), index + i < this.getCurrentList().getRecipeListSize());
            }
        }

        this.zLevel = z;
    }

    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float w = 0.001953125F;
        float h = 0.001953125F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(par1, par2 + par6, this.zLevel).tex((par3) * w, (par4 + par6) * h).endVertex();
        buffer.pos(par1 + par5, par2 + par6, this.zLevel).tex((par3 + par5) * w, (par4 + par6) * h).endVertex();
        buffer.pos(par1 + par5, par2, this.zLevel).tex((par3 + par5) * w, (par4) * h).endVertex();
        buffer.pos(par1, par2, this.zLevel).tex((par3) * w, (par4) * h).endVertex();
        tessellator.draw();
    }

    public void drawTexturedModalRect(int dx, int dy, int dw, int dh, int u, int v, int tw, int th) {
        float w = 0.001953125F;
        float h = 0.001953125F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(dx, dy + dh, this.zLevel).tex((u) * w, (v + th) * h).endVertex();
        buffer.pos(dx + dw, dy + dh, this.zLevel).tex((u + tw) * w, (v + th) * h).endVertex();
        buffer.pos(dx + dw, dy, this.zLevel).tex((u + tw) * w, (v) * h).endVertex();
        buffer.pos(dx, dy, this.zLevel).tex((u) * w, (v) * h).endVertex();
        tessellator.draw();
    }

    private static class FilteredRecipeList implements MCH_IRecipeList {

        private final List<IRecipe> filtered = new ArrayList<>();

        FilteredRecipeList(List<MCH_IRecipeList> bases, String searchText) {
            String needle = searchText.toLowerCase();
            for (MCH_IRecipeList base : bases) {
                for (int i = 0; i < base.getRecipeListSize(); i++) {
                    IRecipe r = base.getRecipe(i);
                    if (r != null && !r.getRecipeOutput().isEmpty()) {
                        String name = r.getRecipeOutput().getDisplayName();
                        if (name != null && name.toLowerCase().contains(needle)) {
                            this.filtered.add(r);
                        }
                    }
                }
            }
        }

        @Override
        public int getRecipeListSize() {
            return this.filtered.size();
        }

        @Override
        public IRecipe getRecipe(int index) {
            return this.filtered.get(index);
        }
    }
}
