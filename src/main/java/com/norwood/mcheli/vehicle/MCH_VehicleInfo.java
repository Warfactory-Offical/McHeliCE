package com.norwood.mcheli.vehicle;

import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class MCH_VehicleInfo extends MCH_AircraftInfo {
    public MCH_ItemVehicle item = null;
    public boolean isEnableMove = false;
    public boolean isEnableRot = false;
    public final List<MCH_VehicleInfo.VPart> partList = new ArrayList<>();

    public MCH_VehicleInfo(AddonResourceLocation location, String path) {
        super(location, path);
    }

    @Override
    public float getMinRotationPitch() {
        return -90.0F;
    }

    @Override
    public float getMaxRotationPitch() {
        return 90.0F;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public boolean validate() throws Exception {
        return super.validate();
    }

    @Override
    public String getDefaultHudName(int seatId) {
        return "vehicle";
    }

    @Override
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
        } else if (item.compareTo("addpart") == 0) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length >= 7) {
                float rb = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
                MCH_VehicleInfo.VPart n = new VPart(
                        this,
                        this.toFloat(s[4]),
                        this.toFloat(s[5]),
                        this.toFloat(s[6]),
                        "part" + this.partList.size(),
                        this.toBool(s[0]),
                        this.toBool(s[1]),
                        this.toBool(s[2]),
                        this.toInt(s[3]),
                        rb
                );
                this.partList.add(n);
            }
        } else if (item.compareTo("addchildpart") == 0 && !this.partList.isEmpty()) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length >= 7) {
                float rb = s.length >= 8 ? this.toFloat(s[7]) : 0.0F;
                MCH_VehicleInfo.VPart p = this.partList.get(this.partList.size() - 1);
                if (p.child == null) {
                    p.child = new ArrayList<>();
                }

                MCH_VehicleInfo.VPart n = new VPart(
                        this,
                        this.toFloat(s[4]),
                        this.toFloat(s[5]),
                        this.toFloat(s[6]),
                        p.modelName + "_" + p.child.size(),
                        this.toBool(s[0]),
                        this.toBool(s[1]),
                        this.toBool(s[2]),
                        this.toInt(s[3]),
                        rb
                );
                p.child.add(n);
            }
        }
    }

    @Override
    public String getDirectoryName() {
        return "vehicles";
    }

    @Override
    public String getKindName() {
        return "vehicle";
    }

    @Override
    public void onPostReload() {
        MCH_MOD.proxy.registerModelsVehicle(this, true);
    }

    public static class VPart extends MCH_AircraftInfo.DrawnPart {
        public final boolean rotPitch;
        public final boolean rotYaw;
        public final int type;
        public final boolean drawFP;
        public final float recoilBuf;
        public List<MCH_VehicleInfo.VPart> child;

        public VPart(
                MCH_VehicleInfo paramMCH_VehicleInfo, float x, float y, float z, String model, boolean drawfp, boolean roty, boolean rotp, int type, float rb
        ) {
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
