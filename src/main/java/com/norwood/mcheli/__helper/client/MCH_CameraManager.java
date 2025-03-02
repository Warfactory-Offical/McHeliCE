package com.norwood.mcheli.__helper.client;

import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_ViewEntityDummy;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(
   modid = "mcheli",
   value = {Side.CLIENT}
)
public class MCH_CameraManager {
   private static final float DEF_THIRD_CAMERA_DIST = 4.0F;
   private static final Minecraft mc = Minecraft.getMinecraft();
   private static float cameraRoll = 0.0F;
   private static float cameraDistance = 4.0F;
   private static float cameraZoom = 1.0F;
   private static MCH_EntityAircraft ridingAircraft = null;

   @SubscribeEvent
   static void onCameraSetupEvent(CameraSetup event) {
      Entity entity = event.getEntity();
      float f = event.getEntity().func_70047_e();
      if (mc.field_71474_y.field_74320_O > 0) {
         if (mc.field_71474_y.field_74320_O == 2) {
            GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         }

         GlStateManager.func_179109_b(0.0F, 0.0F, -(cameraDistance - 4.0F));
         if (mc.field_71474_y.field_74320_O == 2) {
            GlStateManager.func_179114_b(-180.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      MCH_EntityAircraft ridingEntity = ridingAircraft;
      if (ridingEntity != null && ridingEntity.canSwitchFreeLook() && ridingEntity.isPilot(mc.player)) {
         boolean flag = !(entity instanceof MCH_ViewEntityDummy);
         GlStateManager.func_179109_b(0.0F, -f, 0.0F);
         if (flag) {
            GlStateManager.func_179114_b(cameraRoll, 0.0F, 0.0F, 1.0F);
         }

         if (ridingEntity.isOverridePlayerPitch() && flag) {
            GlStateManager.func_179114_b(ridingEntity.field_70125_A, 1.0F, 0.0F, 0.0F);
            event.setPitch(event.getPitch() - ridingEntity.field_70125_A);
         }

         if (ridingEntity.isOverridePlayerYaw() && !ridingEntity.isHovering() && flag) {
            GlStateManager.func_179114_b(ridingEntity.field_70177_z, 0.0F, 1.0F, 0.0F);
            event.setYaw(event.getYaw() - ridingEntity.field_70177_z);
         }

         GlStateManager.func_179109_b(0.0F, f, 0.0F);
      }

   }

   @SubscribeEvent
   static void onFOVModifierEvent(FOVModifier event) {
      MCH_ViewEntityDummy viewer = MCH_ViewEntityDummy.getInstance(mc.world);
      if (viewer == event.getEntity() || MCH_ItemRangeFinder.isUsingScope(mc.player)) {
         event.setFOV(event.getFOV() * (1.0F / cameraZoom));
      }

   }

   public static void setCameraRoll(float roll) {
      roll = MathHelper.func_76142_g(roll);
      cameraRoll = roll;
   }

   public static void setThirdPeasonCameraDistance(float distance) {
      distance = MathHelper.func_76131_a(distance, 4.0F, 60.0F);
      cameraDistance = distance;
   }

   public static void setCameraZoom(float zoom) {
      cameraZoom = zoom;
   }

   public static float getThirdPeasonCameraDistance() {
      return cameraDistance;
   }

   public static void setRidingAircraft(@Nullable MCH_EntityAircraft aircraft) {
      ridingAircraft = aircraft;
   }
}
