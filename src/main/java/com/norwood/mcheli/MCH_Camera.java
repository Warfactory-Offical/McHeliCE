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

   public MCH_Camera(World world, Entity player) {
      this.worldObj = world;
      this.mode = new int[]{MODE_NORMAL, MODE_NORMAL};
      this.zoom = 1.0F;
      this.lastMode = new int[this.getUserMax()];
      this.lastZoomDir = 0;
      this.canUseShader = new boolean[this.getUserMax()];
   }

   public MCH_Camera(World world, Entity player, double x, double y, double z) {
      this(world, player);
      this.setPosition(x, y, z);
      this.setCameraZoom(1.0F);
   }

   public int getUserMax() {
      return this.mode.length;
   }

   public void initCamera(int uid, @Nullable Entity viewer) {
      this.setCameraZoom(1.0F);
      this.setMode(uid, MODE_NORMAL);
      this.updateViewer(uid, viewer);
   }

   public void setMode(int uid, int mode) {
      if (this.isValidUid(uid)) {
         this.mode[uid] = mode < 0 ? MODE_NORMAL : mode % this.getModeNum(uid);

         switch (this.mode[uid]) {
            case MODE_NORMAL:
            case MODE_NIGHTVISION:
               if (this.worldObj.isRemote) {
                  W_EntityRenderer.deactivateShader();
               }
               break;
            case MODE_THERMALVISION:
               if (this.worldObj.isRemote) {
                  W_EntityRenderer.activateShader("pencil");
               }
               break;
         }
      }
   }

   public void setShaderSupport(int uid, Boolean enabled) {
      if (this.isValidUid(uid)) {
         this.setMode(uid, MODE_NORMAL);
         this.canUseShader[uid] = enabled;
      }
   }

   public boolean isValidUid(int uid) {
      return uid >= 0 && uid < this.getUserMax();
   }

   public int getModeNum(int uid) {
      return this.isValidUid(uid) ? (this.canUseShader[uid] ? 3 : 2) : 2;
   }

   public int getMode(int uid) {
      return this.isValidUid(uid) ? this.mode[uid] : MODE_NORMAL;
   }

   public String getModeName(int uid) {
      switch (this.getMode(uid)) {
         case MODE_NIGHTVISION:
            return "NIGHT VISION";
         case MODE_THERMALVISION:
            return "THERMAL VISION";
         default:
            return "";
      }
   }

   public void updateViewer(int uid, @Nullable Entity viewer) {
      if (this.isValidUid(uid) && viewer != null) {
         if (W_Lib.isEntityLivingBase(viewer) && !viewer.isDead) {
            PotionEffect effect;

            // Remove night vision if switching back to normal mode
            if (this.getMode(uid) == MODE_NORMAL && this.lastMode[uid] != MODE_NORMAL) {
               effect = W_Entity.getActivePotionEffect(viewer, MobEffects.NIGHT_VISION);

               if (effect != null && effect.getDuration() > 0 && effect.getDuration() < 500) {
                  if (viewer.world.isRemote) {
                     W_Entity.removePotionEffectClient(viewer, MobEffects.NIGHT_VISION);
                  } else {
                     W_Entity.removePotionEffect(viewer, MobEffects.NIGHT_VISION);
                  }
               }
            }

            // Apply night vision for night/thermal vision modes
            if (this.getMode(uid) == MODE_NIGHTVISION || this.getMode(uid) == MODE_THERMALVISION) {
               effect = W_Entity.getActivePotionEffect(viewer, MobEffects.NIGHT_VISION);

               if ((effect == null || (effect.getDuration() < 500)) && !viewer.world.isRemote) {
                  W_Entity.addPotionEffect(viewer, new PotionEffect(MobEffects.NIGHT_VISION, 250, 0, true, false));
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

   public void setCameraZoom(float zoomLevel) {
      float prevZoom = this.zoom;
      this.zoom = Math.max(1.0F, zoomLevel);

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
