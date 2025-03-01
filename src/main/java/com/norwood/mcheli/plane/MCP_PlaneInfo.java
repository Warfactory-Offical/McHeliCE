package com.norwood.mcheli.plane;

import java.util.ArrayList;
import java.util.List;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import net.minecraft.item.Item;

public class MCP_PlaneInfo extends MCH_AircraftInfo {
   public MCP_ItemPlane item = null;
   public List<MCH_AircraftInfo.DrawnPart> nozzles = new ArrayList();
   public List<MCP_PlaneInfo.Rotor> rotorList = new ArrayList();
   public List<MCP_PlaneInfo.Wing> wingList = new ArrayList();
   public boolean isEnableVtol = false;
   public boolean isDefaultVtol;
   public float vtolYaw = 0.3F;
   public float vtolPitch = 0.2F;
   public boolean isEnableAutoPilot = false;
   public boolean isVariableSweepWing = false;
   public float sweepWingSpeed;

   public Item getItem() {
      return this.item;
   }

   public MCP_PlaneInfo(AddonResourceLocation location, String path) {
      super(location, path);
      this.sweepWingSpeed = this.speed;
   }

   public float getDefaultRotorSpeed() {
      return 47.94F;
   }

   public boolean haveNozzle() {
      return this.nozzles.size() > 0;
   }

   public boolean haveRotor() {
      return this.rotorList.size() > 0;
   }

   public boolean haveWing() {
      return this.wingList.size() > 0;
   }

   public float getMaxSpeed() {
      return 1.8F;
   }

   public int getDefaultMaxZoom() {
      return 8;
   }

   public String getDefaultHudName(int seatId) {
      if (seatId <= 0) {
         return "plane";
      } else {
         return seatId == 1 ? "plane" : "gunner";
      }
   }

   public boolean validate() throws Exception {
      if (this.haveHatch() && this.haveWing()) {
         this.wingList.clear();
         this.hatchList.clear();
      }

      this.speed = (float)((double)this.speed * MCH_Config.AllPlaneSpeed.prmDouble);
      this.sweepWingSpeed = (float)((double)this.sweepWingSpeed * MCH_Config.AllPlaneSpeed.prmDouble);
      return super.validate();
   }

   public void loadItemData(String item, String data) {
      super.loadItemData(item, data);
      String[] s;
      if (item.compareTo("addpartrotor") == 0) {
         s = data.split("\\s*,\\s*");
         if (s.length >= 6) {
            float m = s.length >= 7 ? this.toFloat(s[6], -180.0F, 180.0F) / 90.0F : 1.0F;
            MCP_PlaneInfo.Rotor e = new MCP_PlaneInfo.Rotor(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), m, "rotor" + this.rotorList.size());
            this.rotorList.add(e);
         }
      } else if (item.compareTo("addblade") == 0) {
         int idx = this.rotorList.size() - 1;
         MCP_PlaneInfo.Rotor r = this.rotorList.size() > 0 ? (MCP_PlaneInfo.Rotor)this.rotorList.get(idx) : null;
         if (r != null) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length == 8) {
               MCP_PlaneInfo.Blade b = new MCP_PlaneInfo.Blade(this, this.toInt(s[0]), this.toInt(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), this.toFloat(s[7]), "blade" + idx);
               r.blades.add(b);
            }
         }
      } else {
         MCP_PlaneInfo.Wing w;
         if (item.compareTo("addpartwing") == 0) {
            s = data.split("\\s*,\\s*");
            if (s.length == 7) {
               w = new MCP_PlaneInfo.Wing(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), "wing" + this.wingList.size());
               this.wingList.add(w);
            }
         } else if (item.equalsIgnoreCase("AddPartPylon")) {
            s = data.split("\\s*,\\s*");
            if (s.length >= 7 && this.wingList.size() > 0) {
               w = (MCP_PlaneInfo.Wing)this.wingList.get(this.wingList.size() - 1);
               if (w.pylonList == null) {
                  w.pylonList = new ArrayList();
               }

               MCP_PlaneInfo.Pylon n = new MCP_PlaneInfo.Pylon(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), w.modelName + "_pylon" + w.pylonList.size());
               w.pylonList.add(n);
            }
         } else if (item.compareTo("addpartnozzle") == 0) {
            s = data.split("\\s*,\\s*");
            if (s.length == 6) {
               MCH_AircraftInfo.DrawnPart n = new MCH_AircraftInfo.DrawnPart(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), "nozzle" + this.nozzles.size());
               this.nozzles.add(n);
            }
         } else if (item.compareTo("variablesweepwing") == 0) {
            this.isVariableSweepWing = this.toBool(data);
         } else if (item.compareTo("sweepwingspeed") == 0) {
            this.sweepWingSpeed = this.toFloat(data, 0.0F, 5.0F);
         } else if (item.compareTo("enablevtol") == 0) {
            this.isEnableVtol = this.toBool(data);
         } else if (item.compareTo("defaultvtol") == 0) {
            this.isDefaultVtol = this.toBool(data);
         } else if (item.compareTo("vtolyaw") == 0) {
            this.vtolYaw = this.toFloat(data, 0.0F, 1.0F);
         } else if (item.compareTo("vtolpitch") == 0) {
            this.vtolPitch = this.toFloat(data, 0.01F, 1.0F);
         } else if (item.compareTo("enableautopilot") == 0) {
            this.isEnableAutoPilot = this.toBool(data);
         }
      }

   }

   public String getDirectoryName() {
      return "planes";
   }

   public String getKindName() {
      return "plane";
   }

   public void onPostReload() {
      MCH_MOD.proxy.registerModelsPlane(this, true);
   }

   public class Wing extends MCH_AircraftInfo.DrawnPart {
      public final float maxRotFactor;
      public final float maxRot;
      public List<MCP_PlaneInfo.Pylon> pylonList;

      public Wing(MCP_PlaneInfo paramMCP_PlaneInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
         super(paramMCP_PlaneInfo, px, py, pz, rx, ry, rz, name);
         this.maxRot = mr;
         this.maxRotFactor = this.maxRot / 90.0F;
         this.pylonList = null;
      }
   }

   public class Rotor extends MCH_AircraftInfo.DrawnPart {
      public List<MCP_PlaneInfo.Blade> blades = new ArrayList();
      public final float maxRotFactor;

      public Rotor(MCP_PlaneInfo paramMCP_PlaneInfo, float x, float y, float z, float rx, float ry, float rz, float mrf, String model) {
         super(paramMCP_PlaneInfo, x, y, z, rx, ry, rz, model);
         this.maxRotFactor = mrf;
      }
   }

   public class Pylon extends MCH_AircraftInfo.DrawnPart {
      public final float maxRotFactor;
      public final float maxRot;

      public Pylon(MCP_PlaneInfo paramMCP_PlaneInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
         super(paramMCP_PlaneInfo, px, py, pz, rx, ry, rz, name);
         this.maxRot = mr;
         this.maxRotFactor = this.maxRot / 90.0F;
      }
   }

   public class Blade extends MCH_AircraftInfo.DrawnPart {
      public final int numBlade;
      public final int rotBlade;

      public Blade(MCP_PlaneInfo paramMCP_PlaneInfo, int num, int r, float px, float py, float pz, float rx, float ry, float rz, String name) {
         super(paramMCP_PlaneInfo, px, py, pz, rx, ry, rz, name);
         this.numBlade = num;
         this.rotBlade = r;
      }
   }
}
