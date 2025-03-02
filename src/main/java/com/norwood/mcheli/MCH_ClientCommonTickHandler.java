package com.norwood.mcheli;

import java.util.Iterator;
import com.norwood.mcheli.__helper.client.MCH_CameraManager;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_ClientSeatTickHandler;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.aircraft.MCH_SeatInfo;
import com.norwood.mcheli.command.MCH_GuiTitle;
import com.norwood.mcheli.gltd.MCH_ClientGLTDTickHandler;
import com.norwood.mcheli.gltd.MCH_EntityGLTD;
import com.norwood.mcheli.gltd.MCH_GuiGLTD;
import com.norwood.mcheli.gui.MCH_Gui;
import com.norwood.mcheli.helicopter.MCH_ClientHeliTickHandler;
import com.norwood.mcheli.helicopter.MCH_EntityHeli;
import com.norwood.mcheli.helicopter.MCH_GuiHeli;
import com.norwood.mcheli.lweapon.MCH_ClientLightWeaponTickHandler;
import com.norwood.mcheli.lweapon.MCH_GuiLightWeapon;
import com.norwood.mcheli.mob.MCH_GuiSpawnGunner;
import com.norwood.mcheli.multiplay.MCH_GuiScoreboard;
import com.norwood.mcheli.multiplay.MCH_GuiTargetMarker;
import com.norwood.mcheli.multiplay.MCH_MultiplayClient;
import com.norwood.mcheli.plane.MCP_ClientPlaneTickHandler;
import com.norwood.mcheli.plane.MCP_EntityPlane;
import com.norwood.mcheli.plane.MCP_GuiPlane;
import com.norwood.mcheli.tank.MCH_ClientTankTickHandler;
import com.norwood.mcheli.tank.MCH_EntityTank;
import com.norwood.mcheli.tank.MCH_GuiTank;
import com.norwood.mcheli.tool.MCH_ClientToolTickHandler;
import com.norwood.mcheli.tool.MCH_GuiWrench;
import com.norwood.mcheli.tool.MCH_ItemWrench;
import com.norwood.mcheli.tool.rangefinder.MCH_GuiRangeFinder;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.vehicle.MCH_ClientVehicleTickHandler;
import com.norwood.mcheli.vehicle.MCH_EntityVehicle;
import com.norwood.mcheli.vehicle.MCH_GuiVehicle;
import com.norwood.mcheli.weapon.MCH_WeaponSet;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.W_Reflection;
import com.norwood.mcheli.wrapper.W_TickHandler;
import com.norwood.mcheli.wrapper.W_Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.Display;

@SideOnly(Side.CLIENT)
public class MCH_ClientCommonTickHandler extends W_TickHandler {
   public static MCH_ClientCommonTickHandler instance;
   public MCH_GuiCommon gui_Common;
   public MCH_Gui gui_Heli;
   public MCH_Gui gui_Plane;
   public MCH_Gui gui_Tank;
   public MCH_Gui gui_GLTD;
   public MCH_Gui gui_Vehicle;
   public MCH_Gui gui_LWeapon;
   public MCH_Gui gui_Wrench;
   public MCH_Gui gui_EMarker;
   public MCH_Gui gui_SwnGnr;
   public MCH_Gui gui_RngFndr;
   public MCH_Gui gui_Title;
   public MCH_Gui[] guis;
   public MCH_Gui[] guiTicks;
   public MCH_ClientTickHandlerBase[] ticks;
   public MCH_Key[] Keys;
   public MCH_Key KeyCamDistUp;
   public MCH_Key KeyCamDistDown;
   public MCH_Key KeyScoreboard;
   public MCH_Key KeyMultiplayManager;
   public static int cameraMode = 0;
   public static MCH_EntityAircraft ridingAircraft = null;
   public static boolean isDrawScoreboard = false;
   public static int sendLDCount = 0;
   public static boolean isLocked = false;
   public static int lockedSoundCount = 0;
   int debugcnt;
   private static double prevMouseDeltaX;
   private static double prevMouseDeltaY;
   private static double mouseDeltaX = 0.0D;
   private static double mouseDeltaY = 0.0D;
   private static double mouseRollDeltaX = 0.0D;
   private static double mouseRollDeltaY = 0.0D;
   private static boolean isRideAircraft = false;
   private static float prevTick = 0.0F;

   public MCH_ClientCommonTickHandler(Minecraft minecraft, MCH_Config config) {
      super(minecraft);
      this.gui_Common = new MCH_GuiCommon(minecraft);
      this.gui_Heli = new MCH_GuiHeli(minecraft);
      this.gui_Plane = new MCP_GuiPlane(minecraft);
      this.gui_Tank = new MCH_GuiTank(minecraft);
      this.gui_GLTD = new MCH_GuiGLTD(minecraft);
      this.gui_Vehicle = new MCH_GuiVehicle(minecraft);
      this.gui_LWeapon = new MCH_GuiLightWeapon(minecraft);
      this.gui_Wrench = new MCH_GuiWrench(minecraft);
      this.gui_SwnGnr = new MCH_GuiSpawnGunner(minecraft);
      this.gui_RngFndr = new MCH_GuiRangeFinder(minecraft);
      this.gui_EMarker = new MCH_GuiTargetMarker(minecraft);
      this.gui_Title = new MCH_GuiTitle(minecraft);
      this.guis = new MCH_Gui[]{this.gui_RngFndr, this.gui_LWeapon, this.gui_Heli, this.gui_Plane, this.gui_Tank, this.gui_GLTD, this.gui_Vehicle};
      this.guiTicks = new MCH_Gui[]{this.gui_Common, this.gui_Heli, this.gui_Plane, this.gui_Tank, this.gui_GLTD, this.gui_Vehicle, this.gui_LWeapon, this.gui_Wrench, this.gui_SwnGnr, this.gui_RngFndr, this.gui_EMarker, this.gui_Title};
      this.ticks = new MCH_ClientTickHandlerBase[]{new MCH_ClientHeliTickHandler(minecraft, config), new MCP_ClientPlaneTickHandler(minecraft, config), new MCH_ClientTankTickHandler(minecraft, config), new MCH_ClientGLTDTickHandler(minecraft, config), new MCH_ClientVehicleTickHandler(minecraft, config), new MCH_ClientLightWeaponTickHandler(minecraft, config), new MCH_ClientSeatTickHandler(minecraft, config), new MCH_ClientToolTickHandler(minecraft, config)};
      this.updatekeybind(config);
   }

   public void updatekeybind(MCH_Config config) {
      this.KeyCamDistUp = new MCH_Key(MCH_Config.KeyCameraDistUp.prmInt);
      this.KeyCamDistDown = new MCH_Key(MCH_Config.KeyCameraDistDown.prmInt);
      this.KeyScoreboard = new MCH_Key(MCH_Config.KeyScoreboard.prmInt);
      this.KeyMultiplayManager = new MCH_Key(MCH_Config.KeyMultiplayManager.prmInt);
      this.Keys = new MCH_Key[]{this.KeyCamDistUp, this.KeyCamDistDown, this.KeyScoreboard, this.KeyMultiplayManager};
      MCH_ClientTickHandlerBase[] var2 = this.ticks;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MCH_ClientTickHandlerBase t = var2[var4];
         t.updateKeybind(config);
      }

   }

   public String getLabel() {
      return null;
   }

   public void onTick() {
      MCH_ClientTickHandlerBase.initRotLimit();
      MCH_Key[] var1 = this.Keys;
      int camdist = var1.length;

      for(int var3 = 0; var3 < camdist; ++var3) {
         MCH_Key k = var1[var3];
         k.update();
      }

      EntityPlayer player = this.mc.field_71439_g;
      if (player != null && this.mc.field_71462_r == null) {
         if (MCH_ServerSettings.enableCamDistChange && (this.KeyCamDistUp.isKeyDown() || this.KeyCamDistDown.isKeyDown())) {
            camdist = (int)W_Reflection.getThirdPersonDistance();
            if (this.KeyCamDistUp.isKeyDown() && camdist < 72) {
               camdist += 4;
               if (camdist > 72) {
                  camdist = 72;
               }

               W_Reflection.setThirdPersonDistance((float)camdist);
            } else if (this.KeyCamDistDown.isKeyDown()) {
               camdist -= 4;
               if (camdist < 4) {
                  camdist = 4;
               }

               W_Reflection.setThirdPersonDistance((float)camdist);
            }
         }

         if (this.mc.field_71462_r == null && (!this.mc.func_71356_B() || MCH_Config.DebugLog)) {
            isDrawScoreboard = this.KeyScoreboard.isKeyPress();
            if (!isDrawScoreboard && this.KeyMultiplayManager.isKeyDown()) {
               MCH_PacketIndOpenScreen.send(5);
            }
         }
      }

      if (sendLDCount < 10) {
         ++sendLDCount;
      } else {
         MCH_MultiplayClient.sendImageData();
         sendLDCount = 0;
      }

      boolean inOtherGui = this.mc.field_71462_r != null;
      MCH_ClientTickHandlerBase[] var8 = this.ticks;
      int var10 = var8.length;

      int var5;
      for(var5 = 0; var5 < var10; ++var5) {
         MCH_ClientTickHandlerBase t = var8[var5];
         t.onTick(inOtherGui);
      }

      MCH_Gui[] var9 = this.guiTicks;
      var10 = var9.length;

      for(var5 = 0; var5 < var10; ++var5) {
         MCH_Gui g = var9[var5];
         g.onTick();
      }

      MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
      if (player != null && ac != null && !ac.isDestroyed()) {
         if (isLocked && lockedSoundCount == 0) {
            isLocked = false;
            lockedSoundCount = 20;
            MCH_ClientTickHandlerBase.playSound("locked");
         }

         MCH_CameraManager.setRidingAircraft(ac);
      } else {
         lockedSoundCount = 0;
         isLocked = false;
         MCH_CameraManager.setRidingAircraft(ac);
      }

      if (lockedSoundCount > 0) {
         --lockedSoundCount;
      }

   }

   public void onTickPre() {
      if (this.mc.field_71439_g != null && this.mc.field_71441_e != null) {
         this.onTick();
      }

   }

   public void onTickPost() {
      if (this.mc.field_71439_g != null && this.mc.field_71441_e != null) {
         MCH_GuiTargetMarker.onClientTick();
      }

   }

   public static double getCurrentStickX() {
      return mouseRollDeltaX;
   }

   public static double getCurrentStickY() {
      double inv = 1.0D;
      if (Minecraft.func_71410_x().field_71474_y.field_74338_d) {
         inv = -inv;
      }

      if (MCH_Config.InvertMouse.prmBool) {
         inv = -inv;
      }

      return mouseRollDeltaY * inv;
   }

   public static double getMaxStickLength() {
      return 40.0D;
   }

   public void updateMouseDelta(boolean stickMode, float partialTicks) {
      prevMouseDeltaX = mouseDeltaX;
      prevMouseDeltaY = mouseDeltaY;
      mouseDeltaX = 0.0D;
      mouseDeltaY = 0.0D;
      if (this.mc.field_71415_G && Display.isActive() && this.mc.field_71462_r == null) {
         if (stickMode) {
            if (Math.abs(mouseRollDeltaX) < getMaxStickLength() * 0.2D) {
               mouseRollDeltaX *= (double)(1.0F - 0.15F * partialTicks);
            }

            if (Math.abs(mouseRollDeltaY) < getMaxStickLength() * 0.2D) {
               mouseRollDeltaY *= (double)(1.0F - 0.15F * partialTicks);
            }
         }

         this.mc.field_71417_B.func_74374_c();
         float f1 = this.mc.field_71474_y.field_74341_c * 0.6F + 0.2F;
         float f2 = f1 * f1 * f1 * 8.0F;
         double ms = MCH_Config.MouseSensitivity.prmDouble * 0.1D;
         mouseDeltaX = ms * (double)this.mc.field_71417_B.field_74377_a * (double)f2;
         mouseDeltaY = ms * (double)this.mc.field_71417_B.field_74375_b * (double)f2;
         byte inv = 1;
         if (this.mc.field_71474_y.field_74338_d) {
            inv = -1;
         }

         if (MCH_Config.InvertMouse.prmBool) {
            inv *= -1;
         }

         mouseRollDeltaX += mouseDeltaX;
         mouseRollDeltaY += mouseDeltaY * (double)inv;
         double dist = mouseRollDeltaX * mouseRollDeltaX + mouseRollDeltaY * mouseRollDeltaY;
         if (dist > 1.0D) {
            dist = (double)MathHelper.func_76133_a(dist);
            double d = dist;
            if (dist > getMaxStickLength()) {
               d = getMaxStickLength();
            }

            mouseRollDeltaX /= dist;
            mouseRollDeltaY /= dist;
            mouseRollDeltaX *= d;
            mouseRollDeltaY *= d;
         }
      }

   }

   public void onRenderTickPre(float partialTicks) {
      MCH_GuiTargetMarker.clearMarkEntityPos();
      if (!MCH_ServerSettings.enableDebugBoundingBox) {
         Minecraft.func_71410_x().func_175598_ae().func_178629_b(false);
      }

      MCH_ClientEventHook.haveSearchLightAircraft.clear();
      if (this.mc != null && this.mc.field_71441_e != null) {
         Iterator var2 = Minecraft.func_71410_x().field_71441_e.field_72996_f.iterator();

         while(var2.hasNext()) {
            Object o = var2.next();
            if (o instanceof MCH_EntityAircraft && ((MCH_EntityAircraft)o).haveSearchLight()) {
               MCH_ClientEventHook.haveSearchLightAircraft.add((MCH_EntityAircraft)o);
            }
         }
      }

      if (!W_McClient.isGamePaused()) {
         EntityPlayer player = this.mc.field_71439_g;
         if (player != null) {
            ItemStack currentItemstack = player.func_184586_b(EnumHand.MAIN_HAND);
            if (currentItemstack != null && currentItemstack.func_77973_b() instanceof MCH_ItemWrench && player.func_184605_cv() > 0) {
               W_Reflection.setItemRendererMainProgress(1.0F);
            }

            ridingAircraft = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
            if (ridingAircraft != null) {
               cameraMode = ridingAircraft.getCameraMode(player);
            } else if (player.func_184187_bx() instanceof MCH_EntityGLTD) {
               MCH_EntityGLTD gltd = (MCH_EntityGLTD)player.func_184187_bx();
               cameraMode = gltd.camera.getMode(0);
            } else {
               cameraMode = 0;
            }

            MCH_EntityAircraft ac = null;
            if (!(player.func_184187_bx() instanceof MCH_EntityHeli) && !(player.func_184187_bx() instanceof MCP_EntityPlane) && !(player.func_184187_bx() instanceof MCH_EntityTank)) {
               if (player.func_184187_bx() instanceof MCH_EntityUavStation) {
                  ac = ((MCH_EntityUavStation)player.func_184187_bx()).getControlAircract();
               } else if (player.func_184187_bx() instanceof MCH_EntityVehicle) {
                  MCH_EntityAircraft vehicle = (MCH_EntityAircraft)player.func_184187_bx();
                  vehicle.setupAllRiderRenderPosition(partialTicks, player);
               }
            } else {
               ac = (MCH_EntityAircraft)player.func_184187_bx();
            }

            boolean stickMode = false;
            if (ac instanceof MCH_EntityHeli) {
               stickMode = MCH_Config.MouseControlStickModeHeli.prmBool;
            }

            if (ac instanceof MCP_EntityPlane) {
               stickMode = MCH_Config.MouseControlStickModePlane.prmBool;
            }

            for(int i = 0; i < 10 && prevTick > partialTicks; ++i) {
               --prevTick;
            }

            float roll;
            float yaw;
            if (ac != null && ac.canMouseRot()) {
               if (!isRideAircraft) {
                  ac.onInteractFirst(player);
               }

               isRideAircraft = true;
               this.updateMouseDelta(stickMode, partialTicks);
               boolean fixRot = false;
               float fixYaw = 0.0F;
               float fixPitch = 0.0F;
               MCH_SeatInfo seatInfo = ac.getSeatInfo(player);
               if (seatInfo != null && seatInfo.fixRot && ac.getIsGunnerMode(player) && !ac.isGunnerLookMode(player)) {
                  fixRot = true;
                  fixYaw = seatInfo.fixYaw;
                  fixPitch = seatInfo.fixPitch;
                  mouseRollDeltaX *= 0.0D;
                  mouseRollDeltaY *= 0.0D;
                  mouseDeltaX *= 0.0D;
                  mouseDeltaY *= 0.0D;
               } else if (ac.isPilot(player)) {
                  MCH_AircraftInfo.CameraPosition cp = ac.getCameraPosInfo();
                  if (cp != null) {
                     fixYaw = cp.yaw;
                     fixPitch = cp.pitch;
                  }
               }

               if (ac.getAcInfo() == null) {
                  player.func_70082_c((float)mouseDeltaX, (float)mouseDeltaY);
               } else {
                  ac.setAngles(player, fixRot, fixYaw, fixPitch, (float)(mouseDeltaX + prevMouseDeltaX) / 2.0F, (float)(mouseDeltaY + prevMouseDeltaY) / 2.0F, (float)mouseRollDeltaX, (float)mouseRollDeltaY, partialTicks - prevTick);
               }

               ac.setupAllRiderRenderPosition(partialTicks, player);
               double dist = (double)MathHelper.func_76133_a(mouseRollDeltaX * mouseRollDeltaX + mouseRollDeltaY * mouseRollDeltaY);
               if (!stickMode || dist < getMaxStickLength() * 0.1D) {
                  mouseRollDeltaX *= 0.95D;
                  mouseRollDeltaY *= 0.95D;
               }

               roll = MathHelper.func_76142_g(ac.getRotRoll());
               yaw = MathHelper.func_76142_g(ac.getRotYaw() - player.field_70177_z);
               roll *= MathHelper.func_76134_b((float)((double)yaw * 3.141592653589793D / 180.0D));
               if (ac.getTVMissile() != null && W_Lib.isClientPlayer(ac.getTVMissile().shootingEntity) && ac.getIsGunnerMode(player)) {
                  roll = 0.0F;
               }

               W_Reflection.setCameraRoll(roll);
               this.correctViewEntityDummy(player);
            } else {
               MCH_EntitySeat seat = player.func_184187_bx() instanceof MCH_EntitySeat ? (MCH_EntitySeat)player.func_184187_bx() : null;
               if (seat != null && seat.getParent() != null) {
                  this.updateMouseDelta(stickMode, partialTicks);
                  ac = seat.getParent();
                  boolean fixRot = false;
                  MCH_SeatInfo seatInfo = ac.getSeatInfo(player);
                  if (seatInfo != null && seatInfo.fixRot && ac.getIsGunnerMode(player) && !ac.isGunnerLookMode(player)) {
                     fixRot = true;
                     mouseRollDeltaX *= 0.0D;
                     mouseRollDeltaY *= 0.0D;
                     mouseDeltaX *= 0.0D;
                     mouseDeltaY *= 0.0D;
                  }

                  Vec3d v = new Vec3d(mouseDeltaX, mouseRollDeltaY, 0.0D);
                  v = W_Vec3.rotateRoll((float)((double)(ac.calcRotRoll(partialTicks) / 180.0F) * 3.141592653589793D), v);
                  MCH_WeaponSet ws = ac.getCurrentWeapon(player);
                  mouseDeltaY *= ws != null && ws.getInfo() != null ? (double)ws.getInfo().cameraRotationSpeedPitch : 1.0D;
                  player.func_70082_c((float)mouseDeltaX, (float)mouseDeltaY);
                  float y = ac.getRotYaw();
                  roll = ac.getRotPitch();
                  yaw = ac.getRotRoll();
                  ac.setRotYaw(ac.calcRotYaw(partialTicks));
                  ac.setRotPitch(ac.calcRotPitch(partialTicks));
                  ac.setRotRoll(ac.calcRotRoll(partialTicks));
                  float revRoll = 0.0F;
                  if (fixRot) {
                     player.field_70177_z = ac.getRotYaw() + seatInfo.fixYaw;
                     player.field_70125_A = ac.getRotPitch() + seatInfo.fixPitch;
                     if (player.field_70125_A > 90.0F) {
                        player.field_70127_C -= (player.field_70125_A - 90.0F) * 2.0F;
                        player.field_70125_A -= (player.field_70125_A - 90.0F) * 2.0F;
                        player.field_70126_B += 180.0F;
                        player.field_70177_z += 180.0F;
                        revRoll = 180.0F;
                     } else if (player.field_70125_A < -90.0F) {
                        player.field_70127_C -= (player.field_70125_A - 90.0F) * 2.0F;
                        player.field_70125_A -= (player.field_70125_A - 90.0F) * 2.0F;
                        player.field_70126_B += 180.0F;
                        player.field_70177_z += 180.0F;
                        revRoll = 180.0F;
                     }
                  }

                  ac.setupAllRiderRenderPosition(partialTicks, player);
                  ac.setRotYaw(y);
                  ac.setRotPitch(roll);
                  ac.setRotRoll(yaw);
                  mouseRollDeltaX *= 0.9D;
                  mouseRollDeltaY *= 0.9D;
                  float roll = MathHelper.func_76142_g(ac.getRotRoll());
                  float yaw = MathHelper.func_76142_g(ac.getRotYaw() - player.field_70177_z);
                  roll *= MathHelper.func_76134_b((float)((double)yaw * 3.141592653589793D / 180.0D));
                  if (ac.getTVMissile() != null && W_Lib.isClientPlayer(ac.getTVMissile().shootingEntity) && ac.getIsGunnerMode(player)) {
                     roll = 0.0F;
                  }

                  W_Reflection.setCameraRoll(roll + revRoll);
                  this.correctViewEntityDummy(player);
               } else {
                  if (isRideAircraft) {
                     W_Reflection.setCameraRoll(0.0F);
                     isRideAircraft = false;
                  }

                  mouseRollDeltaX = 0.0D;
                  mouseRollDeltaY = 0.0D;
               }
            }

            if (ac != null) {
               if (ac.getSeatIdByEntity(player) == 0 && !ac.isDestroyed()) {
                  ac.lastRiderYaw = player.field_70177_z;
                  ac.prevLastRiderYaw = player.field_70126_B;
                  ac.lastRiderPitch = player.field_70125_A;
                  ac.prevLastRiderPitch = player.field_70127_C;
               }

               ac.updateWeaponsRotation();
            }

            Entity de = MCH_ViewEntityDummy.getInstance(player.world);
            if (de != null) {
               de.field_70177_z = player.field_70177_z;
               de.field_70126_B = player.field_70126_B;
               if (ac != null) {
                  MCH_WeaponSet wi = ac.getCurrentWeapon(player);
                  if (wi != null && wi.getInfo() != null && wi.getInfo().fixCameraPitch) {
                     de.field_70125_A = de.field_70127_C = 0.0F;
                  }
               }
            }

            prevTick = partialTicks;
         }
      }
   }

   public void correctViewEntityDummy(Entity entity) {
      Entity de = MCH_ViewEntityDummy.getInstance(entity.world);
      if (de != null) {
         if (de.field_70177_z - de.field_70126_B > 180.0F) {
            de.field_70126_B += 360.0F;
         } else if (de.field_70177_z - de.field_70126_B < -180.0F) {
            de.field_70126_B -= 360.0F;
         }
      }

   }

   public void onPlayerTickPre(EntityPlayer player) {
      if (player.world.isRemote) {
         ItemStack currentItemstack = player.func_184586_b(EnumHand.MAIN_HAND);
         if (!currentItemstack.func_190926_b() && currentItemstack.func_77973_b() instanceof MCH_ItemWrench && player.func_184605_cv() > 0 && player.func_184607_cu() != currentItemstack) {
            int maxdm = currentItemstack.func_77958_k();
            int dm = currentItemstack.func_77960_j();
            if (dm <= maxdm && dm > 0) {
               player.func_184598_c(EnumHand.MAIN_HAND);
            }
         }
      }

   }

   public void onPlayerTickPost(EntityPlayer player) {
   }

   public void onRenderTickPost(float partialTicks) {
      if (this.mc.field_71439_g != null) {
         MCH_ClientTickHandlerBase.applyRotLimit(this.mc.field_71439_g);
         Entity e = MCH_ViewEntityDummy.getInstance(this.mc.field_71439_g.world);
         if (e != null) {
            e.field_70125_A = this.mc.field_71439_g.field_70125_A;
            e.field_70177_z = this.mc.field_71439_g.field_70177_z;
            e.field_70127_C = this.mc.field_71439_g.field_70127_C;
            e.field_70126_B = this.mc.field_71439_g.field_70126_B;
         }
      }

      if (this.mc.field_71462_r == null || this.mc.field_71462_r instanceof GuiChat || this.mc.field_71462_r.getClass().toString().indexOf("GuiDriveableController") >= 0) {
         MCH_Gui[] var6 = this.guis;
         int var3 = var6.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            MCH_Gui gui = var6[var4];
            if (this.drawGui(gui, partialTicks)) {
               break;
            }
         }

         this.drawGui(this.gui_Common, partialTicks);
         this.drawGui(this.gui_Wrench, partialTicks);
         this.drawGui(this.gui_SwnGnr, partialTicks);
         this.drawGui(this.gui_EMarker, partialTicks);
         if (isDrawScoreboard) {
            MCH_GuiScoreboard.drawList(this.mc, this.mc.field_71466_p, false);
         }

         this.drawGui(this.gui_Title, partialTicks);
      }

   }

   public boolean drawGui(MCH_Gui gui, float partialTicks) {
      if (gui.isDrawGui(this.mc.field_71439_g)) {
         gui.func_73863_a(0, 0, partialTicks);
         return true;
      } else {
         return false;
      }
   }
}
