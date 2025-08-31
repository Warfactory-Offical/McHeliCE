package com.norwood.mcheli.compat;

import net.minecraftforge.fml.common.Loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModCompatManager {

    public static final Map<String, Boolean> LOADED_CACHE = new ConcurrentHashMap<>();

    public static final String MODID_HBM = "hbm";

    public static boolean isLoaded(String modid) {
        return LOADED_CACHE.computeIfAbsent(modid, Loader::isModLoaded);
    }


}
