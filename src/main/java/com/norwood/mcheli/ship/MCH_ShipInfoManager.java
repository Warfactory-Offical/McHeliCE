package com.norwood.mcheli.ship;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.aircraft.MCH_AircraftInfoManager;
import com.norwood.mcheli.ship.MCH_ShipInfo;
import net.minecraft.item.Item;

public class MCH_ShipInfoManager extends MCH_AircraftInfoManager {

    private static MCH_ShipInfoManager instance = new MCH_ShipInfoManager();
    public static HashMap map = new LinkedHashMap();


    public static MCH_ShipInfo get(String name) {
        return (MCH_ShipInfo)map.get(name);
    }

    public static MCH_ShipInfoManager getInstance() {
        return instance;
    }

    public MCH_BaseInfo newInfo(String name) {
        return new MCH_ShipInfo(name);
    }

    public Map getMap() {
        return map;
    }

    public static MCH_ShipInfo getFromItem(Item item) {
        return getInstance().getAcInfoFromItem(item);
    }

    public MCH_ShipInfo getAcInfoFromItem(Item item) {
        if(item == null) {
            return null;
        } else {
            Iterator i$ = map.values().iterator();

            MCH_ShipInfo info;
            do {
                if(!i$.hasNext()) {
                    return null;
                }

                info = (MCH_ShipInfo)i$.next();
            } while(info.item != item);

            return info;
        }
    }

}
