package com.norwood.mcheli.vehicle;

import java.util.ArrayList;
import java.util.List;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import net.minecraft.item.Item;

public class MCH_VehicleInfo extends MCH_AircraftInfo {
   public MCH_ItemVehicle item = null;
   public boolean isEnableMove = false;
   public boolean isEnableRot = false;
   public List<MCH_VehicleInfo.VPart> partList = new ArrayList();

   public float getMinRotationPitch() {
      return -90.0F;
   }

   public float getMaxRotationPitch() {
      return 90.0F;
   }

   public Item getItem() {
      return this.item;
   }

   public MCH_VehicleInfo(AddonResourceLocation location, String path) {
      super(location, path);
   }

   public boolean validate() throws Exception {
      return super.validate();
   }

   public String getDefaultHudName(int seatId) {
      return "vehicle";
   }

   public void loadItemData(String item, String data) {
      super.loadItemData(item, data);
      if (item.compareTo("canmove") == 0) {
         this.isEnableMove = this.toBool(data);
      } else if (item.compareTo("canrotation") == 0) {
         this.isEnableRot = this.toBool(data);
      } else if (item.compareTo("rotationpitchmin") == 0) {
         super.loadItemData("minrotationpitch", data);
      } else if (item.compareTo("rotationpitchmax") == 0) {
         super.loadItemData("maxrotationpitch", data);
      } else {
         String[] s;
         float rb;
         MCH_VehicleInfo.VPart p;
         if (item.compareTo("addpart") == 0) {
            s = data.split("\\s*,\\s*");
            if (s.length >= 7) {
               rb = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
               p = new MCH_VehicleInfo.VPart(this, this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), "part" + this.partList.size(), this.toBool(s[0]), this.toBool(s[1]), this.toBool(s[2]), this.toInt(s[3]), rb);
               this.partList.add(p);
            }
         } else if (item.compareTo("addchildpart") == 0 && this.partList.size() > 0) {
            s = data.split("\\s*,\\s*");
            if (s.length >= 7) {
               rb = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
               p = (MCH_VehicleInfo.VPart)this.partList.get(this.partList.size() - 1);
               if (p.child == null) {
                  p.child = new ArrayList();
               }

               MCH_VehicleInfo.VPart n = new MCH_VehicleInfo.VPart(this, this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), p.modelName + "_" + p.child.size(), this.toBool(s[0]), this.toBool(s[1]), this.toBool(s[2]), this.toInt(s[3]), rb);
               p.child.add(n);
            }
         }
      }

   }

   public String getDirectoryName() {
      return "vehicles";
   }

   public String getKindName() {
      return "vehicle";
   }

   public void onPostReload() {
      MCH_MOD.proxy.registerModelsVehicle(this, true);
   }

   public class VPart extends MCH_AircraftInfo.DrawnPart {
      public final boolean rotPitch;
      public final boolean rotYaw;
      public final int type;
      public List<MCH_VehicleInfo.VPart> child;
      public final boolean drawFP;
      public final float recoilBuf;

      public VPart(MCH_VehicleInfo paramMCH_VehicleInfo, float x, float y, float z, String model, boolean drawfp, boolean roty, boolean rotp, int type, float rb) {
         super(paramMCH_VehicleInfo, x, y, z, 0.0F, 0.0F, 0.0F, model);
         this.rotYaw = roty;
         this.rotPitch = rotp;
         this.type = type;
         this.child = null;
         this.drawFP = drawfp;
         this.recoilBuf = rb;
      }
   }
}
