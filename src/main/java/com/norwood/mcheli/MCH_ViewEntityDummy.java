package com.norwood.mcheli;

import com.norwood.mcheli.__helper.client.MCH_CameraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class MCH_ViewEntityDummy extends EntityPlayerSP {
   private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
   private static MCH_ViewEntityDummy instance = null;
   private float zoom;

   private MCH_ViewEntityDummy(World world) {
      super(Minecraft.func_71410_x(), world, Minecraft.func_71410_x().func_147114_u(), new StatisticsManager(), new RecipeBook());
      this.field_70737_aN = 0;
      this.field_70738_aO = 1;
      this.func_70105_a(1.0F, 1.0F);
   }

   public static MCH_ViewEntityDummy getInstance(World w) {
      if ((instance == null || instance.field_70128_L) && w.isRemote) {
         instance = new MCH_ViewEntityDummy(w);
         if (Minecraft.func_71410_x().field_71439_g != null) {
            instance.field_71158_b = Minecraft.func_71410_x().field_71439_g.field_71158_b;
         }

         instance.func_70107_b(0.0D, -4.0D, 0.0D);
         w.func_72838_d(instance);
      }

      return instance;
   }

   public static void onUnloadWorld() {
      if (instance != null) {
         instance.func_70106_y();
         instance = null;
      }

   }

   public AxisAlignedBB func_174813_aQ() {
      return ZERO_AABB;
   }

   public void func_70071_h_() {
   }

   public void update(MCH_Camera camera) {
      if (camera != null) {
         this.zoom = camera.getCameraZoom();
         this.field_70126_B = this.field_70177_z;
         this.field_70127_C = this.field_70125_A;
         this.field_70177_z = camera.rotationYaw;
         this.field_70125_A = camera.rotationPitch;
         this.field_70169_q = camera.posX;
         this.field_70167_r = camera.posY;
         this.field_70166_s = camera.posZ;
         this.posX = camera.posX;
         this.posY = camera.posY;
         this.posZ = camera.posZ;
         MCH_CameraManager.setCameraZoom(this.zoom);
      }
   }

   public static void setCameraPosition(double x, double y, double z) {
      if (instance != null) {
         instance.field_70169_q = x;
         instance.field_70167_r = y;
         instance.field_70166_s = z;
         instance.field_70142_S = x;
         instance.field_70137_T = y;
         instance.field_70136_U = z;
         instance.posX = x;
         instance.posY = y;
         instance.posZ = z;
      }
   }

   public float func_175156_o() {
      return super.func_175156_o() * (1.0F / this.zoom);
   }

   public float func_70047_e() {
      return 0.0F;
   }
}
