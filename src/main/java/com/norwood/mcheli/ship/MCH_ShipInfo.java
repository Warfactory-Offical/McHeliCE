package com.norwood.mcheli.ship;

import java.util.ArrayList;
import java.util.List;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.ship.MCH_ItemShip;
import net.minecraft.item.Item;

public class MCH_ShipInfo extends MCH_AircraftInfo {

    public MCH_ItemShip item = null;
    public List nozzles = new ArrayList();
    public List rotorList = new ArrayList();
    public List wingList = new ArrayList();
    public boolean isEnableVtol = false;
    public boolean isDefaultVtol;
    public float vtolYaw = 0.3F;
    public float vtolPitch = 0.2F;
    public boolean isEnableAutoPilot = false;
    public boolean isVariableSweepWing = false;
    public float sweepWingSpeed;
    public boolean iscarrier = false;


    public Item getItem() {
        return this.item;
    }

    public MCH_ShipInfo(String name) {
        super(name);
        this.sweepWingSpeed = super.speed;
    }

    public float getDefaultRotorSpeed() {
        return 47.94F;
    }

    private float getDefaultStepHeight() {
        return 0.6F;
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
        return seatId <= 0?"plane":(seatId == 1?"plane":"gunner");
    }

    public boolean isValidData() throws Exception {
        if(this.haveHatch() && this.haveWing()) {
            this.wingList.clear();
            super.hatchList.clear();
        }

        double var10001 = (double)super.speed;
        MCH_Config var10002 = MCH_MOD.config;
        super.speed = (float)(var10001 * MCH_Config.AllShipSpeed.prmDouble);
        var10001 = (double)this.sweepWingSpeed;
        var10002 = MCH_MOD.config;
        this.sweepWingSpeed = (float)(var10001 * MCH_Config.AllShipSpeed.prmDouble);
        return super.isValidData();
    }

    public void loadItemData(String item, String data) {
        super.loadItemData(item, data);
        String[] s;
        if(item.compareTo("addpartrotor") == 0) {
            s = data.split("\\s*,\\s*");
            if(s.length >= 6) {
                float n = s.length >= 7?this.toFloat(s[6], -180.0F, 180.0F) / 90.0F:1.0F;
                MCH_ShipInfo.Rotor n1 = new MCH_ShipInfo.Rotor(this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), n, "rotor" + this.rotorList.size());
                this.rotorList.add(n1);
            }
        } else if(item.compareTo("addblade") == 0) {
            int s1 = this.rotorList.size() - 1;
            MCH_ShipInfo.Rotor n2 = this.rotorList.size() > 0?(MCH_ShipInfo.Rotor)this.rotorList.get(s1):null;
            if(n2 != null) {
                String[] n4 = data.split("\\s*,\\s*");
                if(n4.length == 8) {
                    MCH_ShipInfo.Blade b = new MCH_ShipInfo.Blade(this.toInt(n4[0]), this.toInt(n4[1]), this.toFloat(n4[2]), this.toFloat(n4[3]), this.toFloat(n4[4]), this.toFloat(n4[5]), this.toFloat(n4[6]), this.toFloat(n4[7]), "blade" + s1);
                    n2.blades.add(b);
                }
            }
        } else {
            MCH_ShipInfo.Wing n3;
            if(item.compareTo("addpartwing") == 0) {
                s = data.split("\\s*,\\s*");
                if(s.length == 7) {
                    n3 = new MCH_ShipInfo.Wing(this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), "wing" + this.wingList.size());
                    this.wingList.add(n3);
                }
            } else if(item.equalsIgnoreCase("AddPartPylon")) {
                s = data.split("\\s*,\\s*");
                if(s.length >= 7 && this.wingList.size() > 0) {
                    n3 = (MCH_ShipInfo.Wing)this.wingList.get(this.wingList.size() - 1);
                    if(n3.pylonList == null) {
                        n3.pylonList = new ArrayList();
                    }

                    MCH_ShipInfo.Pylon n6 = new MCH_ShipInfo.Pylon(this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), n3.modelName + "_pylon" + n3.pylonList.size());
                    n3.pylonList.add(n6);
                }
            } else if(item.compareTo("addpartnozzle") == 0) {
                s = data.split("\\s*,\\s*");
                if(s.length == 6) {
                    MCH_AircraftInfo.DrawnPart n5 = new MCH_AircraftInfo.DrawnPart(this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), "nozzle" + this.nozzles.size());
                    this.nozzles.add(n5);
                }
            } else if(item.compareTo("variablesweepwing") == 0) {
                this.isVariableSweepWing = this.toBool(data);
            } else if(item.compareTo("sweepwingspeed") == 0) {
                this.sweepWingSpeed = this.toFloat(data, 0.0F, 5.0F);
            } else if(item.compareTo("enablevtol") == 0) {
                this.isEnableVtol = this.toBool(data);
            } else if(item.compareTo("defaultvtol") == 0) {
                this.isDefaultVtol = this.toBool(data);
            } else if(item.compareTo("vtolyaw") == 0) {
                this.vtolYaw = this.toFloat(data, 0.0F, 1.0F);
            } else if(item.compareTo("vtolpitch") == 0) {
                this.vtolPitch = this.toFloat(data, 0.01F, 1.0F);
            } else if(item.compareTo("enableautopilot") == 0) {
                this.isEnableAutoPilot = this.toBool(data);
            }
        }

    }

    public String getDirectoryName() {
        return "ships";
    }

    public String getKindName() {
        return "ship";
    }

    public void preReload() {
        super.preReload();
        this.nozzles.clear();
        this.rotorList.clear();
        this.wingList.clear();
    }

    public void postReload() {
        MCH_MOD.proxy.registerModelsShip(super.name, true);
    }

    public class Rotor extends MCH_AircraftInfo.DrawnPart {

        public List blades = new ArrayList();
        public final float maxRotFactor;


        public Rotor(float x, float y, float z, float rx, float ry, float rz, float mrf, String model) {
            super(x, y, z, rx, ry, rz, model);
            this.maxRotFactor = mrf;
        }
    }

    public class Pylon extends MCH_AircraftInfo.DrawnPart {

        public final float maxRotFactor;
        public final float maxRot;


        public Pylon(float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
            super(px, py, pz, rx, ry, rz, name);
            this.maxRot = mr;
            this.maxRotFactor = this.maxRot / 90.0F;
        }
    }

    public class Blade extends MCH_AircraftInfo.DrawnPart {

        public final int numBlade;
        public final int rotBlade;


        public Blade(int num, int r, float px, float py, float pz, float rx, float ry, float rz, String name) {
            super(px, py, pz, rx, ry, rz, name);
            this.numBlade = num;
            this.rotBlade = r;
        }
    }

    public class Wing extends MCH_AircraftInfo.DrawnPart {

        public final float maxRotFactor;
        public final float maxRot;
        public List pylonList;


        public Wing(float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
            super(px, py, pz, rx, ry, rz, name);
            this.maxRot = mr;
            this.maxRotFactor = this.maxRot / 90.0F;
            this.pylonList = null;
        }
    }
}
