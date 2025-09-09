package com.norwood.mcheli.tank;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.vehicle.MCH_ItemVehicle;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MCH_TankInfo extends MCH_AircraftInfo {
    public MCH_ItemTank item = null;
    public int weightType = 0;
    public float weightedCenterZ = 0.0F;

    public MCH_TankInfo(AddonResourceLocation location, String path) {
        super(location, path);
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public List<MCH_AircraftInfo.Wheel> getDefaultWheelList() {
        List<MCH_AircraftInfo.Wheel> list = new ArrayList<>();
        list.add(new Wheel(this, new Vec3d(1.5, -0.24, 2.0)));
        list.add(new Wheel(this, new Vec3d(1.5, -0.24, -2.0)));
        return list;
    }

    @Override
    public float getDefaultSoundRange() {
        return 50.0F;
    }

    @Override
    public float getDefaultRotorSpeed() {
        return 47.94F;
    }

    private float getDefaultStepHeight() {
        return 0.6F;
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
            return "tank";
        } else {
            return seatId == 1 ? "tank" : "gunner";
        }
    }

    @Override
    public boolean validate() throws Exception {
        this.speed = (float) (this.speed * MCH_Config.AllTankSpeed.prmDouble);
        return super.validate();
    }

    @Override
    public void loadItemData(String item, String data) {
        super.loadItemData(item, data);
        if (item.equalsIgnoreCase("WeightType")) {
            data = data.toLowerCase();
            this.weightType = data.equals("car") ? 1 : (data.equals("tank") ? 2 : 0);
        } else if (item.equalsIgnoreCase("WeightedCenterZ")) {
            this.weightedCenterZ = this.toFloat(data, -1000.0F, 1000.0F);
        }
    }

    @Override
    public String getDirectoryName() {
        return "tanks";
    }

    @Override
    public String getKindName() {
        return "tank";
    }

    @Override
    public void onPostReload() {
        item = (MCH_ItemTank) ForgeRegistries.ITEMS.getValue(new ResourceLocation(MCH_MOD.MOD_ID, name));
        MCH_MOD.proxy.registerModelsTank(this, true);
    }
}
