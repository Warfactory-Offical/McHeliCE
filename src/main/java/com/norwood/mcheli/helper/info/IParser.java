package com.norwood.mcheli.helper.info;

import com.norwood.mcheli.helicopter.MCH_HeliInfo;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.hud.MCH_Hud;
import com.norwood.mcheli.item.MCH_ItemInfo;
import com.norwood.mcheli.plane.MCP_PlaneInfo;
import com.norwood.mcheli.ship.MCH_ShipInfo;
import com.norwood.mcheli.tank.MCH_TankInfo;
import com.norwood.mcheli.throwable.MCH_ThrowableInfo;
import com.norwood.mcheli.vehicle.MCH_VehicleInfo;
import com.norwood.mcheli.weapon.MCH_WeaponInfo;

import javax.annotation.Nullable;
import java.util.List;

public interface IParser {
    @Nullable
    MCH_HeliInfo parseHelicopter(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCP_PlaneInfo parsePlane(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCH_ShipInfo parseShip(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCH_TankInfo parseTank(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCH_VehicleInfo parseVehicle(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCH_WeaponInfo parseWeapon(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCH_ThrowableInfo parseThrowable(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCH_Hud parseHud(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;

    @Nullable
    MCH_ItemInfo parseItem(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception;
}
