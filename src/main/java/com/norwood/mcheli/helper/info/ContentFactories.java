package com.norwood.mcheli.helper.info;

import com.google.common.collect.Maps;
import com.norwood.mcheli.helper.MCH_Utils;

import javax.annotation.Nullable;
import java.util.Map;

public class ContentFactories {
    private static final Map<String, ContentType> TABLE = Maps.newHashMap();

    static {
        TABLE.put("helicopters", ContentType.HELICOPTER);
        TABLE.put("planes", ContentType.PLANE);
        TABLE.put("ships", ContentType.SHIP);
        TABLE.put("tanks", ContentType.TANK);
        TABLE.put("vehicles", ContentType.VEHICLE);
        TABLE.put("throwable", ContentType.THROWABLE);
        TABLE.put("weapons", ContentType.WEAPON);
        if (MCH_Utils.isClient()) {
            TABLE.put("hud", ContentType.HUD);
        }

        TxtParser.register();
    }

    private ContentFactories() {
    }

    @Nullable
    public static ContentType getType(@Nullable String dirName) {
        return dirName == null ? null : TABLE.get(dirName);
    }
}
