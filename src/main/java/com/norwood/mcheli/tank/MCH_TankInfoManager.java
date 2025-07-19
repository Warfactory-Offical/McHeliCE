package com.norwood.mcheli.tank;

import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.aircraft.MCH_AircraftInfoManager;
import net.minecraft.item.Item;

public class MCH_TankInfoManager extends MCH_AircraftInfoManager<MCH_TankInfo> {
   private static MCH_TankInfoManager instance = new MCH_TankInfoManager();

   @Nullable
   public static MCH_TankInfo get(String name) {
      return ContentRegistries.tank().get(name);
   }

   public static MCH_TankInfoManager getInstance() {
      return instance;
   }

   public MCH_TankInfo newInfo(AddonResourceLocation name, String filepath) {
      return new MCH_TankInfo(name, filepath);
   }

   @Nullable
   public static MCH_TankInfo getFromItem(Item item) {
      return getInstance().getAcInfoFromItem(item);
   }

   @Nullable
   public MCH_TankInfo getAcInfoFromItem(Item item) {
      return ContentRegistries.tank().findFirst(info -> info.item == item);
   }

   @Override
   protected boolean contains(String name) {
      return ContentRegistries.tank().contains(name);
   }

   @Override
   protected int size() {
      return ContentRegistries.tank().size();
   }
}
