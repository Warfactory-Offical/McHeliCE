package com.norwood.mcheli.ship;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MCH_ShipInfo extends MCH_AircraftInfo {
    public MCH_ItemShip item = null;
    public final List<MCH_AircraftInfo.DrawnPart> nozzles = new ArrayList<>();
    public final List<MCH_ShipInfo.Rotor> rotorList = new ArrayList<>();
    public final List<MCH_ShipInfo.Wing> wingList = new ArrayList<>();
    public boolean isEnableVtol = false;
    public boolean isDefaultVtol;
    public float vtolYaw = 0.3F;
    public float vtolPitch = 0.2F;
    public boolean isEnableAutoPilot = false;
    public boolean isVariableSweepWing = false;
    public float sweepWingSpeed = this.speed;

    public MCH_ShipInfo(AddonResourceLocation location, String path) {
        super(location, path);
    }
    @Override
    public Item getItem() {
        return this.item;
    }
    @Override
    public float getDefaultRotorSpeed() {
        return 47.94F;
    }

    public boolean haveNozzle() {
        return !this.nozzles.isEmpty();
    }

    public boolean haveRotor() {
        return !this.rotorList.isEmpty();
    }

    public boolean haveWing() {
        return !this.wingList.isEmpty();
    }
    @Override
    public float getMaxSpeed() {
        return 1.8F;
    }
    @Override
    public int getDefaultMaxZoom() {
        return 8;
    }
    @Override
    public String getDefaultHudName(int seatId) {
        if (seatId <= 0) {
            return "plane";
        } else {
            return seatId == 1 ? "plane" : "gunner";
        }
    }
    @Override
    public boolean validate() throws Exception {
        if (this.haveHatch() && this.haveWing()) {
            this.wingList.clear();
            this.hatchList.clear();
        }

        this.speed = (float) (this.speed * MCH_Config.AllShipSpeed.prmDouble);
        this.sweepWingSpeed = (float) (this.sweepWingSpeed * MCH_Config.AllShipSpeed.prmDouble);
        return super.validate();
    }
    @Override
    public String getDirectoryName() {
        return "ships";
    }
    @Override
    public String getKindName() {
        return "ship";
    }
    @Override
    public void onPostReload() {
        item = (MCH_ItemShip) ForgeRegistries.ITEMS.getValue(new ResourceLocation(MCH_MOD.MOD_ID, name));
        MCH_MOD.proxy.registerModelsShip(this, true);
    }

    public static class Blade extends MCH_AircraftInfo.DrawnPart {
        public final int numBlade;
        public final int rotBlade;

        public Blade(MCH_ShipInfo paramMCH_ShipInfo, int num, int r, float px, float py, float pz, float rx, float ry, float rz, String name) {
            super(paramMCH_ShipInfo, px, py, pz, rx, ry, rz, name);
            this.numBlade = num;
            this.rotBlade = r;
        }
    }

    public static class Pylon extends MCH_AircraftInfo.DrawnPart {
        public final float maxRotFactor;
        public final float maxRot;

        public Pylon(MCH_ShipInfo paramMCH_ShipInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
            super(paramMCH_ShipInfo, px, py, pz, rx, ry, rz, name);
            this.maxRot = mr;
            this.maxRotFactor = this.maxRot / 90.0F;
        }
    }

    public static class Rotor extends MCH_AircraftInfo.DrawnPart {
        public final float maxRotFactor;
        public final List<MCH_ShipInfo.Blade> blades = new ArrayList<>();

        public Rotor(MCH_ShipInfo paramMCH_ShipInfo, float x, float y, float z, float rx, float ry, float rz, float mrf, String model) {
            super(paramMCH_ShipInfo, x, y, z, rx, ry, rz, model);
            this.maxRotFactor = mrf;
        }
    }

    public static class Wing extends MCH_AircraftInfo.DrawnPart {
        public final float maxRotFactor;
        public final float maxRot;
        public List<MCH_ShipInfo.Pylon> pylonList;

        public Wing(MCH_ShipInfo paramMCH_ShipInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
            super(paramMCH_ShipInfo, px, py, pz, rx, ry, rz, name);
            this.maxRot = mr;
            this.maxRotFactor = this.maxRot / 90.0F;
            this.pylonList = null;
        }
    }
}
