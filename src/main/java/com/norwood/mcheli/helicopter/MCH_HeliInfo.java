package com.norwood.mcheli.helicopter;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MCH_HeliInfo extends MCH_AircraftInfo {
    public MCH_ItemHeli item = null;
    public boolean isEnableFoldBlade;
    public final List<MCH_HeliInfo.Rotor> rotorList;

    public MCH_HeliInfo(AddonResourceLocation location, String path) {
        super(location, path);
        this.isEnableGunnerMode = false;
        this.isEnableFoldBlade = false;
        this.rotorList = new ArrayList<>();
        this.minRotationPitch = -20.0F;
        this.maxRotationPitch = 20.0F;
    }
    @Override
    public boolean validate() throws Exception {
        this.speed = (float) (this.speed * MCH_Config.AllHeliSpeed.prmDouble);
        return super.validate();
    }
    @Override
    public float getDefaultSoundRange() {
        return 80.0F;
    }
    @Override
    public float getDefaultRotorSpeed() {
        return 79.99F;
    }
    @Override
    public int getDefaultMaxZoom() {
        return 8;
    }
    @Override
    public Item getItem() {
        return this.item;
    }
    @Override
    public String getDefaultHudName(int seatId) {
        if (seatId <= 0) {
            return "heli";
        } else {
            return seatId == 1 ? "heli_gnr" : "gunner";
        }
    }
    @Override
    public String getDirectoryName() {
        return "helicopters";
    }
    @Override
    public String getKindName() {
        return "helicopter";
    }
    @Override
    public void onPostReload() {
        item = (MCH_ItemHeli) ForgeRegistries.ITEMS.getValue(new ResourceLocation(MCH_MOD.MOD_ID, name));
        MCH_MOD.proxy.registerModelsHeli(this, true);
    }

    public static class Rotor extends MCH_AircraftInfo.DrawnPart {
        public final int bladeNum;
        public final int bladeRot;
        public final boolean haveFoldFunc;
        public final boolean oldRenderMethod;

        public Rotor(
                MCH_HeliInfo paramMCH_HeliInfo, int b, int br, float x, float y, float z, float rx, float ry, float rz, String model, boolean hf, boolean old
        ) {
            super(paramMCH_HeliInfo, x, y, z, rx, ry, rz, model);
            this.bladeNum = b;
            this.bladeRot = br;
            this.haveFoldFunc = hf;
            this.oldRenderMethod = old;
        }
    }
}
