package com.norwood.mcheli;

import javax.annotation.Nullable;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_EntityRenderer;
import com.norwood.mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class MCH_Camera {
   private final World worldObj;
   private float zoom;
   private int[] mode;
   private boolean[] canUseShader;
   private int[] lastMode;
   public double posX;
   public double posY;
   public double posZ;
   public float rotationYaw;
   public float rotationPitch;
   public float prevRotationYaw;
   public float prevRotationPitch;
   private int lastZoomDir;
   public float partRotationYaw;
   public float partRotationPitch;
   public float prevPartRotationYaw;
   public float prevPartRotationPitch;
   public static final int MODE_NORMAL = 0;
   public static final int MODE_NIGHTVISION = 1;
   public static final int MODE_THERMALVISION = 2;

   public MCH_Camera(World w, Entity p) {
      this.worldObj = w;
      this.mode = new int[]{0, 0};
      this.zoom = 1.0F;
      this.lastMode = new int[this.getUserMax()];
      this.lastZoomDir = 0;
      this.canUseShader = new boolean[this.getUserMax()];
   }

   public MCH_Camera(World w, Entity p, double x, double y, double z) {
      this(w, p);
      this.setPosition(x, y, z);
      this.setCameraZoom(1.0F);
   }

   public int getUserMax() {
      return this.mode.length;
   }

   public void initCamera(int uid, @Nullable Entity viewer) {
      this.setCameraZoom(1.0F);
      this.setMode(uid, 0);
      this.updateViewer(uid, viewer);
   }

   public void setMode(int uid, int m) {
      if (this.isValidUid(uid)) {
         this.mode[uid] = m < 0 ? 0 : m % this.getModeNum(uid);
         switch(this.mode[uid]) {
         case 0:
         case 1:
            if (this.worldObj.isRemote) {
               W_EntityRenderer.deactivateShader();
            }
            break;
         case 2:
            if (this.worldObj.isRemote) {
               W_EntityRenderer.activateShader("pencil");
            }
         }

      }
   }

   public void setShaderSupport(int uid, Boolean b) {
      if (this.isValidUid(uid)) {
         this.setMode(uid, 0);
         this.canUseShader[uid] = b;
      }

   }

   public boolean isValidUid(int uid) {
      return uid >= 0 && uid < this.getUserMax();
   }

   public int getModeNum(int uid) {
      if (!this.isValidUid(uid)) {
         return 2;
      } else {
         return this.canUseShader[uid] ? 3 : 2;
      }
   }

   public int getMode(int uid) {
      return this.isValidUid(uid) ? this.mode[uid] : 0;
   }

   public String getModeName(int uid) {
      if (this.getMode(uid) == 1) {
         return "NIGHT VISION";
      } else {
         return this.getMode(uid) == 2 ? "THERMAL VISION" : "";
      }
   }

   public void updateViewer(int uid, @Nullable Entity viewer) {
      if (this.isValidUid(uid) && viewer != null) {
         if (W_Lib.isEntityLivingBase(viewer) && !viewer.field_70128_L) {
            PotionEffect pe;
            if (this.getMode(uid) == 0 && this.lastMode[uid] != 0) {
               pe = W_Entity.getActivePotionEffect(viewer, MobEffects.field_76439_r);
               if (pe != null && pe.func_76459_b() > 0 && pe.func_76459_b() < 500) {
                  if (viewer.world.isRemote) {
                     W_Entity.removePotionEffectClient(viewer, MobEffects.field_76439_r);
                  } else {
                     W_Entity.removePotionEffect(viewer, MobEffects.field_76439_r);
                  }
               }
            }

            if (this.getMode(uid) == 1 || this.getMode(uid) == 2) {
               pe = W_Entity.getActivePotionEffect(viewer, MobEffects.field_76439_r);
               if ((pe == null || pe != null && pe.func_76459_b() < 500) && !viewer.world.isRemote) {
                  W_Entity.addPotionEffect(viewer, new PotionEffect(MobEffects.field_76439_r, 250, 0, true, false));
               }
            }
         }

         this.lastMode[uid] = this.getMode(uid);
      }
   }

   public void setPosition(double x, double y, double z) {
      this.posX = x;
      this.posY = y;
      this.posZ = z;
   }

   public void setCameraZoom(float z) {
      float prevZoom = this.zoom;
      this.zoom = z < 1.0F ? 1.0F : z;
      if (this.zoom > prevZoom) {
         this.lastZoomDir = 1;
      } else if (this.zoom < prevZoom) {
         this.lastZoomDir = -1;
      } else {
         this.lastZoomDir = 0;
      }

   }

   public float getCameraZoom() {
      return this.zoom;
   }

   public int getLastZoomDir() {
      return this.lastZoomDir;
   }
}
