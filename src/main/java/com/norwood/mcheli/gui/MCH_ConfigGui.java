package com.norwood.mcheli.gui;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.norwood.mcheli.MCH_ClientCommonTickHandler;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_PacketNotifyInfoReloaded;
import com.norwood.mcheli.multiplay.MCH_GuiTargetMarker;
import com.norwood.mcheli.weapon.MCH_WeaponInfo;
import com.norwood.mcheli.wrapper.W_GuiButton;
import com.norwood.mcheli.wrapper.W_GuiContainer;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MCH_ConfigGui extends W_GuiContainer {
   private final EntityPlayer thePlayer;
   private MCH_GuiOnOffButton buttonMouseInv;
   private MCH_GuiOnOffButton buttonStickModeHeli;
   private MCH_GuiOnOffButton buttonStickModePlane;
   private MCH_GuiOnOffButton buttonHideKeyBind;
   private MCH_GuiOnOffButton buttonShowHUDTP;
   private MCH_GuiOnOffButton buttonSmoothShading;
   private MCH_GuiOnOffButton buttonShowEntityMarker;
   private MCH_GuiOnOffButton buttonMarkThroughWall;
   private MCH_GuiOnOffButton buttonReplaceCamera;
   private MCH_GuiOnOffButton buttonNewExplosion;
   private MCH_GuiSlider sliderEntityMarkerSize;
   private MCH_GuiSlider sliderBlockMarkerSize;
   private MCH_GuiSlider sliderSensitivity;
   private MCH_GuiSlider[] sliderHitMark;
   private MCH_GuiOnOffButton buttonTestMode;
   private MCH_GuiOnOffButton buttonThrottleHeli;
   private MCH_GuiOnOffButton buttonThrottlePlane;
   private MCH_GuiOnOffButton buttonThrottleTank;
   private MCH_GuiOnOffButton buttonFlightSimMode;
   private MCH_GuiOnOffButton buttonSwitchWeaponWheel;
   private W_GuiButton buttonReloadAircraftInfo;
   private W_GuiButton buttonReloadWeaponInfo;
   private W_GuiButton buttonReloadAllHUD;
   private MCH_GuiSlider __sliderTextureAlpha;
   public List<W_GuiButton> listControlButtons;
   public List<W_GuiButton> listRenderButtons;
   public List<W_GuiButton> listKeyBindingButtons;
   public List<W_GuiButton> listDevelopButtons;
   public MCH_GuiList keyBindingList;
   public int waitKeyButtonId;
   public int waitKeyAcceptCount;
   public static final int BUTTON_RENDER = 50;
   public static final int BUTTON_KEY_BINDING = 51;
   public static final int BUTTON_PREV_CONTROL = 52;
   public static final int BUTTON_DEVELOP = 55;
   public static final int BUTTON_KEY_LIST = 53;
   public static final int BUTTON_KEY_RESET_ALL = 54;
   public static final int BUTTON_KEY_LIST_BASE = 200;
   public static final int BUTTON_KEY_RESET_BASE = 300;
   public static final int BUTTON_DEV_RELOAD_AC = 400;
   public static final int BUTTON_DEV_RELOAD_WEAPON = 401;
   public static final int BUTTON_DEV_RELOAD_HUD = 402;
   public static final int BUTTON_SAVE_CLOSE = 100;
   public static final int BUTTON_CANCEL = 101;
   public int currentScreenId = 0;
   public static final int SCREEN_CONTROLS = 0;
   public static final int SCREEN_RENDER = 1;
   public static final int SCREEN_KEY_BIND = 2;
   public static final int SCREEN_DEVELOP = 3;
   private int ignoreButtonCounter = 0;

   public MCH_ConfigGui(EntityPlayer player) {
      super(new MCH_ConfigGuiContainer(player));
      this.thePlayer = player;
      this.field_146999_f = 330;
      this.field_147000_g = 200;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_146292_n.clear();
      int x1 = this.field_147003_i + 10;
      int x2 = this.field_147003_i + 10 + 150 + 10;
      int y = this.field_147009_r;
      this.listControlButtons = new ArrayList();
      this.buttonMouseInv = new MCH_GuiOnOffButton(0, x1, y + 25, 150, 20, "Invert Mouse : ");
      this.sliderSensitivity = new MCH_GuiSlider(0, x1, y + 50, 150, 20, "Sensitivity : %.1f", 0.0F, 0.0F, 30.0F, 0.1F);
      this.buttonFlightSimMode = new MCH_GuiOnOffButton(0, x1, y + 75, 150, 20, "Mouse Flight Sim Mode : ");
      this.buttonSwitchWeaponWheel = new MCH_GuiOnOffButton(0, x1, y + 100, 150, 20, "Switch Weapon Wheel : ");
      this.listControlButtons.add(new W_GuiButton(50, x1, y + 125, 150, 20, "Render Settings >>"));
      this.listControlButtons.add(new W_GuiButton(51, x1, y + 150, 150, 20, "Key Binding >>"));
      this.listControlButtons.add(new W_GuiButton(55, x2, y + 150, 150, 20, "Development >>"));
      this.buttonTestMode = new MCH_GuiOnOffButton(0, x1, y + 175, 150, 20, "Test Mode : ");
      this.buttonStickModeHeli = new MCH_GuiOnOffButton(0, x2, y + 25, 150, 20, "Stick Mode Heli : ");
      this.buttonStickModePlane = new MCH_GuiOnOffButton(0, x2, y + 50, 150, 20, "Stick Mode Plane : ");
      this.buttonThrottleHeli = new MCH_GuiOnOffButton(0, x2, y + 75, 150, 20, "Throttle Down Heli : ");
      this.buttonThrottlePlane = new MCH_GuiOnOffButton(0, x2, y + 100, 150, 20, "Throttle Down Plane : ");
      this.buttonThrottleTank = new MCH_GuiOnOffButton(0, x2, y + 125, 150, 20, "Throttle Down Tank : ");
      this.listControlButtons.add(this.buttonMouseInv);
      this.listControlButtons.add(this.buttonStickModeHeli);
      this.listControlButtons.add(this.buttonStickModePlane);
      this.listControlButtons.add(this.sliderSensitivity);
      this.listControlButtons.add(this.buttonThrottleHeli);
      this.listControlButtons.add(this.buttonThrottlePlane);
      this.listControlButtons.add(this.buttonThrottleTank);
      this.listControlButtons.add(this.buttonTestMode);
      this.listControlButtons.add(this.buttonFlightSimMode);
      this.listControlButtons.add(this.buttonSwitchWeaponWheel);
      Iterator var4 = this.listControlButtons.iterator();

      GuiButton b;
      while(var4.hasNext()) {
         b = (GuiButton)var4.next();
         this.field_146292_n.add(b);
      }

      this.listRenderButtons = new ArrayList();
      this.buttonShowHUDTP = new MCH_GuiOnOffButton(0, x1, y + 25, 150, 20, "Show HUD Third Person : ");
      this.buttonHideKeyBind = new MCH_GuiOnOffButton(0, x1, y + 50, 150, 20, "Hide Key Binding : ");
      this.sliderHitMark = new MCH_GuiSlider[]{new MCH_GuiSlider(0, x1 + 0, y + 125, 75, 20, "Alpha:%.0f", 0.0F, 0.0F, 255.0F, 16.0F), new MCH_GuiSlider(0, x1 + 75, y + 75, 75, 20, "Red:%.0f", 0.0F, 0.0F, 255.0F, 16.0F), new MCH_GuiSlider(0, x1 + 75, y + 100, 75, 20, "Green:%.0f", 0.0F, 0.0F, 255.0F, 16.0F), new MCH_GuiSlider(0, x1 + 75, y + 125, 75, 20, "Blue:%.0f", 0.0F, 0.0F, 255.0F, 16.0F)};
      this.buttonReplaceCamera = new MCH_GuiOnOffButton(0, x1, y + 150, 150, 20, "Change Camera Pos : ");
      this.listRenderButtons.add(new W_GuiButton(52, x1, y + 175, 90, 20, "Controls <<"));
      this.buttonSmoothShading = new MCH_GuiOnOffButton(0, x2, y + 25, 150, 20, "Smooth Shading : ");
      this.buttonShowEntityMarker = new MCH_GuiOnOffButton(0, x2, y + 50, 150, 20, "Show Entity Maker : ");
      this.sliderEntityMarkerSize = new MCH_GuiSlider(0, x2 + 30, y + 75, 120, 20, "Entity Marker Size:%.0f", 10.0F, 0.0F, 30.0F, 1.0F);
      this.sliderBlockMarkerSize = new MCH_GuiSlider(0, x2 + 60, y + 100, 90, 20, "Block Marker Size:%.0f", 10.0F, 0.0F, 20.0F, 1.0F);
      this.buttonMarkThroughWall = new MCH_GuiOnOffButton(0, x2 + 30, y + 100, 120, 20, "Mark Through Wall : ");
      this.buttonNewExplosion = new MCH_GuiOnOffButton(0, x2, y + 150, 150, 20, "Default Explosion : ");
      this.listRenderButtons.add(this.buttonShowHUDTP);

      for(int i = 0; i < this.sliderHitMark.length; ++i) {
         this.listRenderButtons.add(this.sliderHitMark[i]);
      }

      this.listRenderButtons.add(this.buttonSmoothShading);
      this.listRenderButtons.add(this.buttonHideKeyBind);
      this.listRenderButtons.add(this.buttonShowEntityMarker);
      this.listRenderButtons.add(this.buttonReplaceCamera);
      this.listRenderButtons.add(this.buttonNewExplosion);
      this.listRenderButtons.add(this.sliderEntityMarkerSize);
      this.listRenderButtons.add(this.sliderBlockMarkerSize);
      var4 = this.listRenderButtons.iterator();

      while(var4.hasNext()) {
         b = (GuiButton)var4.next();
         this.field_146292_n.add(b);
      }

      this.listKeyBindingButtons = new ArrayList();
      this.waitKeyButtonId = 0;
      this.waitKeyAcceptCount = 0;
      this.keyBindingList = new MCH_GuiList(53, 7, x1, y + 25 - 2, 310, 150, "");
      this.listKeyBindingButtons.add(this.keyBindingList);
      this.listKeyBindingButtons.add(new W_GuiButton(52, x1, y + 175, 90, 20, "Controls <<"));
      this.listKeyBindingButtons.add(new W_GuiButton(54, x1 + 90, y + 175, 60, 20, "Reset All"));
      MCH_GuiListItemKeyBind[] listKeyBindItems = new MCH_GuiListItemKeyBind[]{new MCH_GuiListItemKeyBind(200, 300, x1, "Up", MCH_Config.KeyUp), new MCH_GuiListItemKeyBind(201, 301, x1, "Down", MCH_Config.KeyDown), new MCH_GuiListItemKeyBind(202, 302, x1, "Right", MCH_Config.KeyRight), new MCH_GuiListItemKeyBind(203, 303, x1, "Left", MCH_Config.KeyLeft), new MCH_GuiListItemKeyBind(204, 304, x1, "Switch Gunner", MCH_Config.KeySwitchMode), new MCH_GuiListItemKeyBind(205, 305, x1, "Switch Hovering", MCH_Config.KeySwitchHovering), new MCH_GuiListItemKeyBind(206, 306, x1, "Switch Weapon1", MCH_Config.KeySwitchWeapon1), new MCH_GuiListItemKeyBind(207, 307, x1, "Switch Weapon2", MCH_Config.KeySwitchWeapon2), new MCH_GuiListItemKeyBind(208, 308, x1, "Switch Weapon Mode", MCH_Config.KeySwWeaponMode), new MCH_GuiListItemKeyBind(209, 309, x1, "Zoom / Fold Wing", MCH_Config.KeyZoom), new MCH_GuiListItemKeyBind(210, 310, x1, "Camera Mode", MCH_Config.KeyCameraMode), new MCH_GuiListItemKeyBind(211, 311, x1, "Unmount Mobs", MCH_Config.KeyUnmount), new MCH_GuiListItemKeyBind(212, 312, x1, "Flare", MCH_Config.KeyFlare), new MCH_GuiListItemKeyBind(213, 313, x1, "Vtol / Drop / Fold Blade", MCH_Config.KeyExtra), new MCH_GuiListItemKeyBind(214, 314, x1, "Third Person Distance Up", MCH_Config.KeyCameraDistUp), new MCH_GuiListItemKeyBind(215, 315, x1, "Third Person Distance Down", MCH_Config.KeyCameraDistDown), new MCH_GuiListItemKeyBind(216, 316, x1, "Switch Free Look", MCH_Config.KeyFreeLook), new MCH_GuiListItemKeyBind(217, 317, x1, "Open GUI", MCH_Config.KeyGUI), new MCH_GuiListItemKeyBind(218, 318, x1, "Gear Up Down", MCH_Config.KeyGearUpDown), new MCH_GuiListItemKeyBind(219, 319, x1, "Put entity in the rack", MCH_Config.KeyPutToRack), new MCH_GuiListItemKeyBind(220, 320, x1, "Drop entity from the rack", MCH_Config.KeyDownFromRack), new MCH_GuiListItemKeyBind(221, 321, x1, "[MP]Score board", MCH_Config.KeyScoreboard), new MCH_GuiListItemKeyBind(222, 322, x1, "[MP][OP]Multiplay manager", MCH_Config.KeyMultiplayManager)};
      MCH_GuiListItemKeyBind[] var11 = listKeyBindItems;
      int var6 = listKeyBindItems.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         MCH_GuiListItemKeyBind item = var11[var7];
         this.keyBindingList.addItem(item);
      }

      Iterator var12 = this.listKeyBindingButtons.iterator();

      GuiButton b;
      while(var12.hasNext()) {
         b = (GuiButton)var12.next();
         this.field_146292_n.add(b);
      }

      this.listDevelopButtons = new ArrayList();
      if (Minecraft.func_71410_x().func_71356_B()) {
         this.buttonReloadAircraftInfo = new W_GuiButton(400, x1, y + 50, 150, 20, "Reload aircraft setting");
         this.buttonReloadWeaponInfo = new W_GuiButton(401, x1, y + 75, 150, 20, "Reload All Weapons");
         this.buttonReloadAllHUD = new W_GuiButton(402, x1, y + 100, 150, 20, "Reload All HUD");
         this.__sliderTextureAlpha = new MCH_GuiSlider(432, x1, y + 125, 150, 20, "Texture Alpha:%.0f", 1.0F, 0.0F, 255.0F, 1.0F);
         this.listDevelopButtons.add(this.buttonReloadAircraftInfo);
         this.listDevelopButtons.add(this.buttonReloadWeaponInfo);
         this.listDevelopButtons.add(this.buttonReloadAllHUD);
         this.listDevelopButtons.add(this.__sliderTextureAlpha);
      }

      this.listDevelopButtons.add(new W_GuiButton(52, x1, y + 175, 90, 20, "Controls <<"));
      var12 = this.listDevelopButtons.iterator();

      while(var12.hasNext()) {
         b = (GuiButton)var12.next();
         this.field_146292_n.add(b);
      }

      this.field_146292_n.add(new GuiButton(100, x2, y + 175, 80, 20, "Save & Close"));
      this.field_146292_n.add(new GuiButton(101, x2 + 90, y + 175, 60, 20, "Cancel"));
      this.switchScreen(0);
      this.applySwitchScreen();
      this.getAllStatusFromConfig();
   }

   public boolean canButtonClick() {
      return this.ignoreButtonCounter <= 0;
   }

   public void getAllStatusFromConfig() {
      this.buttonMouseInv.setOnOff(MCH_Config.InvertMouse.prmBool);
      this.buttonStickModeHeli.setOnOff(MCH_Config.MouseControlStickModeHeli.prmBool);
      this.buttonStickModePlane.setOnOff(MCH_Config.MouseControlStickModePlane.prmBool);
      this.sliderSensitivity.setSliderValue((float)MCH_Config.MouseSensitivity.prmDouble);
      this.buttonShowHUDTP.setOnOff(MCH_Config.DisplayHUDThirdPerson.prmBool);
      this.buttonSmoothShading.setOnOff(MCH_Config.SmoothShading.prmBool);
      this.buttonHideKeyBind.setOnOff(MCH_Config.HideKeybind.prmBool);
      this.buttonShowEntityMarker.setOnOff(MCH_Config.DisplayEntityMarker.prmBool);
      this.buttonMarkThroughWall.setOnOff(MCH_Config.DisplayMarkThroughWall.prmBool);
      this.sliderEntityMarkerSize.setSliderValue((float)MCH_Config.EntityMarkerSize.prmDouble);
      this.sliderBlockMarkerSize.setSliderValue((float)MCH_Config.BlockMarkerSize.prmDouble);
      this.buttonReplaceCamera.setOnOff(MCH_Config.ReplaceRenderViewEntity.prmBool);
      this.buttonNewExplosion.setOnOff(MCH_Config.DefaultExplosionParticle.prmBool);
      this.sliderHitMark[0].setSliderValue(MCH_Config.hitMarkColorAlpha * 255.0F);
      this.sliderHitMark[1].setSliderValue((float)(MCH_Config.hitMarkColorRGB >> 16 & 255));
      this.sliderHitMark[2].setSliderValue((float)(MCH_Config.hitMarkColorRGB >> 8 & 255));
      this.sliderHitMark[3].setSliderValue((float)(MCH_Config.hitMarkColorRGB >> 0 & 255));
      this.buttonThrottleHeli.setOnOff(MCH_Config.AutoThrottleDownHeli.prmBool);
      this.buttonThrottlePlane.setOnOff(MCH_Config.AutoThrottleDownPlane.prmBool);
      this.buttonThrottleTank.setOnOff(MCH_Config.AutoThrottleDownTank.prmBool);
      this.buttonTestMode.setOnOff(MCH_Config.TestMode.prmBool);
      this.buttonFlightSimMode.setOnOff(MCH_Config.MouseControlFlightSimMode.prmBool);
      this.buttonSwitchWeaponWheel.setOnOff(MCH_Config.SwitchWeaponWithMouseWheel.prmBool);
      if (this.__sliderTextureAlpha != null) {
         this.__sliderTextureAlpha.setSliderValue((float)MCH_Config.__TextureAlpha.prmDouble * 255.0F);
      }

   }

   public void saveAndApplyConfig() {
      MCH_Config.InvertMouse.setPrm(this.buttonMouseInv.getOnOff());
      MCH_Config.MouseControlStickModeHeli.setPrm(this.buttonStickModeHeli.getOnOff());
      MCH_Config.MouseControlStickModePlane.setPrm(this.buttonStickModePlane.getOnOff());
      MCH_Config.MouseControlFlightSimMode.setPrm(this.buttonFlightSimMode.getOnOff());
      MCH_Config.SwitchWeaponWithMouseWheel.setPrm(this.buttonSwitchWeaponWheel.getOnOff());
      MCH_Config.MouseSensitivity.setPrm((double)this.sliderSensitivity.getSliderValueInt(1));
      MCH_Config.DisplayHUDThirdPerson.setPrm(this.buttonShowHUDTP.getOnOff());
      MCH_Config.SmoothShading.setPrm(this.buttonSmoothShading.getOnOff());
      MCH_Config.HideKeybind.setPrm(this.buttonHideKeyBind.getOnOff());
      MCH_Config.DisplayEntityMarker.setPrm(this.buttonShowEntityMarker.getOnOff());
      MCH_Config.DisplayMarkThroughWall.setPrm(this.buttonMarkThroughWall.getOnOff());
      MCH_Config.EntityMarkerSize.setPrm((double)this.sliderEntityMarkerSize.getSliderValueInt(1));
      MCH_Config.BlockMarkerSize.setPrm((double)this.sliderBlockMarkerSize.getSliderValueInt(1));
      MCH_Config.ReplaceRenderViewEntity.setPrm(this.buttonReplaceCamera.getOnOff());
      MCH_Config.DefaultExplosionParticle.setPrm(this.buttonNewExplosion.getOnOff());
      float a = this.sliderHitMark[0].getSliderValue();
      int r = (int)this.sliderHitMark[1].getSliderValue();
      int g = (int)this.sliderHitMark[2].getSliderValue();
      int b = (int)this.sliderHitMark[3].getSliderValue();
      MCH_Config.hitMarkColorAlpha = a / 255.0F;
      MCH_Config.hitMarkColorRGB = r << 16 | g << 8 | b;
      MCH_Config.HitMarkColor.setPrm(String.format("%d, %d, %d, %d", (int)a, r, g, b));
      boolean b1 = MCH_Config.AutoThrottleDownHeli.prmBool;
      boolean b2 = MCH_Config.AutoThrottleDownPlane.prmBool;
      MCH_Config.AutoThrottleDownHeli.setPrm(this.buttonThrottleHeli.getOnOff());
      MCH_Config.AutoThrottleDownPlane.setPrm(this.buttonThrottlePlane.getOnOff());
      MCH_Config.AutoThrottleDownTank.setPrm(this.buttonThrottleTank.getOnOff());
      if (b1 != MCH_Config.AutoThrottleDownHeli.prmBool || b2 != MCH_Config.AutoThrottleDownPlane.prmBool) {
         this.sendClientSettings();
      }

      for(int i = 0; i < this.keyBindingList.getItemNum(); ++i) {
         ((MCH_GuiListItemKeyBind)this.keyBindingList.getItem(i)).applyKeycode();
      }

      MCH_ClientCommonTickHandler.instance.updatekeybind(MCH_MOD.config);
      MCH_Config.TestMode.setPrm(this.buttonTestMode.getOnOff());
      if (this.__sliderTextureAlpha != null) {
         MCH_Config.__TextureAlpha.setPrm((double)this.__sliderTextureAlpha.getSliderValue() / 255.0D);
      }

      MCH_MOD.config.write();
   }

   public void switchScreen(int screenID) {
      this.waitKeyButtonId = 0;
      this.currentScreenId = screenID;
      Iterator i$ = this.listControlButtons.iterator();

      W_GuiButton b;
      while(i$.hasNext()) {
         b = (W_GuiButton)i$.next();
         b.setVisible(false);
      }

      i$ = this.listRenderButtons.iterator();

      while(i$.hasNext()) {
         b = (W_GuiButton)i$.next();
         b.setVisible(false);
      }

      i$ = this.listKeyBindingButtons.iterator();

      while(i$.hasNext()) {
         b = (W_GuiButton)i$.next();
         b.setVisible(false);
      }

      i$ = this.listDevelopButtons.iterator();

      while(i$.hasNext()) {
         b = (W_GuiButton)i$.next();
         b.setVisible(false);
      }

      this.ignoreButtonCounter = 3;
   }

   public void applySwitchScreen() {
      W_GuiButton b;
      Iterator i$;
      switch(this.currentScreenId) {
      case 0:
      default:
         i$ = this.listControlButtons.iterator();

         while(i$.hasNext()) {
            b = (W_GuiButton)i$.next();
            b.setVisible(true);
         }

         return;
      case 1:
         i$ = this.listRenderButtons.iterator();

         while(i$.hasNext()) {
            b = (W_GuiButton)i$.next();
            b.setVisible(true);
         }

         return;
      case 2:
         i$ = this.listKeyBindingButtons.iterator();

         while(i$.hasNext()) {
            b = (W_GuiButton)i$.next();
            b.setVisible(true);
         }

         return;
      case 3:
         i$ = this.listDevelopButtons.iterator();

         while(i$.hasNext()) {
            b = (W_GuiButton)i$.next();
            b.setVisible(true);
         }

      }
   }

   public void sendClientSettings() {
      if (this.field_146297_k.field_71439_g != null) {
         MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(this.field_146297_k.field_71439_g);
         if (ac != null) {
            int seatId = ac.getSeatIdByEntity(this.field_146297_k.field_71439_g);
            if (seatId == 0) {
               ac.updateClientSettings(seatId);
            }
         }
      }

   }

   public void func_73869_a(char a, int code) throws IOException {
      if (this.waitKeyButtonId != 0) {
         if (code != 1) {
            super.func_73869_a(a, code);
         }

         this.acceptKeycode(code);
         this.waitKeyButtonId = 0;
      } else {
         super.func_73869_a(a, code);
      }

   }

   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
      super.func_73864_a(par1, par2, par3);
      if (this.waitKeyButtonId != 0 && this.waitKeyAcceptCount == 0) {
         this.acceptKeycode(par3 - 100);
         this.waitKeyButtonId = 0;
      }

   }

   public void acceptKeycode(int code) {
      if (code != 1 && this.field_146297_k.field_71462_r instanceof MCH_ConfigGui) {
         MCH_GuiListItemKeyBind kb = (MCH_GuiListItemKeyBind)this.keyBindingList.getItem(this.waitKeyButtonId - 200);
         if (kb != null) {
            kb.setKeycode(code);
         }
      }

   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      if (this.waitKeyButtonId == 0) {
         int var16 = Mouse.getEventDWheel();
         if (var16 != 0) {
            if (var16 > 0) {
               this.keyBindingList.scrollDown(2.0F);
            } else if (var16 < 0) {
               this.keyBindingList.scrollUp(2.0F);
            }
         }

      }
   }

   public void func_73876_c() {
      super.func_73876_c();
      if (this.waitKeyAcceptCount > 0) {
         --this.waitKeyAcceptCount;
      }

      if (this.ignoreButtonCounter > 0) {
         --this.ignoreButtonCounter;
         if (this.ignoreButtonCounter == 0) {
            this.applySwitchScreen();
         }
      }

   }

   public void func_146281_b() {
      super.func_146281_b();
   }

   protected void func_146284_a(GuiButton button) {
      try {
         super.func_146284_a(button);
         if (!button.field_146124_l) {
            return;
         }

         if (this.waitKeyButtonId != 0) {
            return;
         }

         if (!this.canButtonClick()) {
            return;
         }

         MCH_EntityAircraft ac;
         int i;
         switch(button.field_146127_k) {
         case 50:
            this.switchScreen(1);
            break;
         case 51:
            this.switchScreen(2);
            break;
         case 52:
            this.switchScreen(0);
            break;
         case 53:
            MCH_GuiListItem item = this.keyBindingList.lastPushItem;
            if (item != null) {
               MCH_GuiListItemKeyBind kb = (MCH_GuiListItemKeyBind)item;
               if (kb.lastPushButton != null) {
                  int kb_num = this.keyBindingList.getItemNum();
                  if (kb.lastPushButton.field_146127_k >= 200 && kb.lastPushButton.field_146127_k < 200 + kb_num) {
                     this.waitKeyButtonId = kb.lastPushButton.field_146127_k;
                     this.waitKeyAcceptCount = 5;
                  } else if (kb.lastPushButton.field_146127_k >= 300 && kb.lastPushButton.field_146127_k < 300 + kb_num) {
                     kb.resetKeycode();
                  }

                  kb.lastPushButton = null;
               }
            }
            break;
         case 54:
            for(int i = 0; i < this.keyBindingList.getItemNum(); ++i) {
               ((MCH_GuiListItemKeyBind)this.keyBindingList.getItem(i)).resetKeycode();
            }

            return;
         case 55:
            this.switchScreen(3);
            break;
         case 100:
            this.saveAndApplyConfig();
            this.field_146297_k.field_71439_g.func_71053_j();
            break;
         case 101:
            this.field_146297_k.field_71439_g.func_71053_j();
            break;
         case 401:
            MCH_Lib.DbgLog(true, "MCH_BaseInfo.reload all weapon info.");
            ContentRegistries.get(MCH_WeaponInfo.class).reloadAll();
            MCH_PacketNotifyInfoReloaded.sendRealodAllWeapon();
            List<Entity> list = this.field_146297_k.field_71441_e.field_72996_f;
            Set<String> reloaded = Sets.newHashSet();

            for(i = 0; i < list.size(); ++i) {
               if (list.get(i) instanceof MCH_EntityAircraft) {
                  ac = (MCH_EntityAircraft)list.get(i);
                  if (ac.getAcInfo() != null && !reloaded.contains(ac.getAcInfo().name)) {
                     ContentRegistries.get(ac.getAcInfo().getClass()).reload(ac.getAcInfo().name);
                     ac.changeType(ac.getAcInfo().name);
                     ac.onAcInfoReloaded();
                     reloaded.add(ac.getAcInfo().name);
                  }
               }
            }

            this.field_146297_k.field_71439_g.func_71053_j();
            break;
         case 402:
            MCH_MOD.proxy.reloadHUD();
         case 400:
            ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(this.thePlayer);
            if (ac != null && ac.getAcInfo() != null) {
               String name = ac.getAcInfo().name;
               MCH_Lib.DbgLog(true, "MCH_BaseInfo.reload : " + name);
               ContentRegistries.get(ac.getAcInfo().getClass()).reload(name);
               List<Entity> list = this.field_146297_k.field_71441_e.field_72996_f;

               for(i = 0; i < list.size(); ++i) {
                  if (list.get(i) instanceof MCH_EntityAircraft) {
                     ac = (MCH_EntityAircraft)list.get(i);
                     if (ac.getAcInfo() != null && ac.getAcInfo().name.equals(name)) {
                        ac.changeType(name);
                        ac.onAcInfoReloaded();
                     }
                  }
               }

               MCH_PacketNotifyInfoReloaded.sendRealodAc();
            }

            this.field_146297_k.field_71439_g.func_71053_j();
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public boolean func_73868_f() {
      return true;
   }

   protected void func_146979_b(int par1, int par2) {
      super.func_146979_b(par1, par2);
      this.drawString("MC Helicopter MOD Options", 10, 10, 16777215);
      if (this.currentScreenId == 0) {
         this.drawString("< Controls >", 170, 10, 16777215);
      } else if (this.currentScreenId == 1) {
         this.drawString("< Render >", 170, 10, 16777215);
         this.drawString("Hit Mark", 10, 75, 16777215);
         int color = 0;
         int color = color | (int)this.sliderHitMark[0].getSliderValue() << 24;
         color |= (int)this.sliderHitMark[1].getSliderValue() << 16;
         color |= (int)this.sliderHitMark[2].getSliderValue() << 8;
         color |= (int)this.sliderHitMark[3].getSliderValue() << 0;
         this.drawSampleHitMark(40, 105, color);
         double size = (double)this.sliderEntityMarkerSize.getSliderValue();
         double x = 170.0D + (30.0D - size) / 2.0D;
         double y = (double)(this.sliderEntityMarkerSize.field_146129_i - this.sliderEntityMarkerSize.getHeight());
         double[] ls = new double[]{x + size, y, x, y, x + size / 2.0D, y + size};
         this.drawLine(ls, -65536, 4);
         size = (double)this.sliderBlockMarkerSize.getSliderValue();
         x = 185.0D;
         y = (double)this.sliderBlockMarkerSize.field_146129_i;
         color = -65536;
         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glDisable(3553);
         GL11.glBlendFunc(770, 771);
         GL11.glColor4ub((byte)(color >> 16 & 255), (byte)(color >> 8 & 255), (byte)(color >> 0 & 255), (byte)(color >> 24 & 255));
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder builder = tessellator.getBuffer();
         builder.begin(1, DefaultVertexFormats.field_181706_f);
         MCH_GuiTargetMarker.drawRhombus(builder, 15, x, y, (double)this.field_73735_i, size, color);
         tessellator.draw();
         GL11.glEnable(3553);
         GL11.glDisable(3042);
         GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
         GL11.glPopMatrix();
      } else {
         int y;
         if (this.currentScreenId == 2) {
            this.drawString("< Key Binding >", 170, 10, 16777215);
            if (this.waitKeyButtonId != 0) {
               func_73734_a(30, 30, this.field_146999_f - 30, this.field_147000_g - 30, -533712848);
               String msg = "Please ant key or mouse button.";
               y = this.getStringWidth(msg);
               this.drawString(msg, (this.field_146999_f - y) / 2, this.field_147000_g / 2 - 4, 16777215);
            }
         } else if (this.currentScreenId == 3) {
            this.drawString("< Development >", 170, 10, 16777215);
            this.drawString("Single player only!", 10, 30, 16711680);
            if (this.buttonReloadAircraftInfo != null && this.buttonReloadAircraftInfo.isOnMouseOver()) {
               this.drawString("The following items are not reload.", 170, 30, 16777215);
               String[] ignoreItems = MCH_AircraftInfo.getCannotReloadItem();
               y = 10;
               String[] var5 = ignoreItems;
               int var17 = ignoreItems.length;

               for(int var7 = 0; var7 < var17; ++var7) {
                  String s = var5[var7];
                  this.drawString("  " + s, 170, 30 + y, 16777215);
                  y += 10;
               }
            }
         }
      }

   }

   protected void func_146976_a(float var1, int var2, int var3) {
      W_McClient.MOD_bindTexture("textures/gui/config.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int x = (this.field_146294_l - this.field_146999_f) / 2;
      int y = (this.field_146295_m - this.field_147000_g) / 2;
      this.drawTexturedModalRectRotate((double)x, (double)y, (double)this.field_146999_f, (double)this.field_147000_g, 0.0D, 0.0D, (double)this.field_146999_f, (double)this.field_147000_g, 0.0F, 512.0D, 256.0D);
   }

   public void drawSampleHitMark(int x, int y, int color) {
      int IVX = 10;
      int IVY = 10;
      int SZX = 5;
      int SZY = 5;
      double[] ls = new double[]{(double)(x - IVX), (double)(y - IVY), (double)(x - SZX), (double)(y - SZY), (double)(x - IVX), (double)(y + IVY), (double)(x - SZX), (double)(y + SZY), (double)(x + IVX), (double)(y - IVY), (double)(x + SZX), (double)(y - SZY), (double)(x + IVX), (double)(y + IVY), (double)(x + SZX), (double)(y + SZY)};
      this.drawLine(ls, color, 1);
   }

   public void drawLine(double[] line, int color, int mode) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4ub((byte)(color >> 16 & 255), (byte)(color >> 8 & 255), (byte)(color >> 0 & 255), (byte)(color >> 24 & 255));
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.getBuffer();
      buffer.begin(mode, DefaultVertexFormats.field_181705_e);

      for(int i = 0; i < line.length; i += 2) {
         buffer.pos(line[i + 0], line[i + 1], (double)this.field_73735_i).func_181675_d();
      }

      tessellator.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
      GL11.glPopMatrix();
   }

   public void drawTexturedModalRectRotate(double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, float rot, double texWidth, double texHeight) {
      GL11.glPushMatrix();
      GL11.glTranslated(left + width / 2.0D, top + height / 2.0D, 0.0D);
      GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
      float fw = (float)(1.0D / texWidth);
      float fh = (float)(1.0D / texHeight);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.getBuffer();
      buffer.begin(7, DefaultVertexFormats.field_181707_g);
      buffer.pos(-width / 2.0D, height / 2.0D, (double)this.field_73735_i).func_187315_a(uLeft * (double)fw, (vTop + vHeight) * (double)fh).func_181675_d();
      buffer.pos(width / 2.0D, height / 2.0D, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)fw, (vTop + vHeight) * (double)fh).func_181675_d();
      buffer.pos(width / 2.0D, -height / 2.0D, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)fw, vTop * (double)fh).func_181675_d();
      buffer.pos(-width / 2.0D, -height / 2.0D, (double)this.field_73735_i).func_187315_a(uLeft * (double)fw, vTop * (double)fh).func_181675_d();
      tessellator.draw();
      GL11.glPopMatrix();
   }
}
