package com.norwood.mcheli.plane;

import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.aircraft.MCH_AircraftInfoManager;
import net.minecraft.item.Item;

public class MCP_PlaneInfoManager extends MCH_AircraftInfoManager<MCP_PlaneInfo> {
   private static MCP_PlaneInfoManager instance = new MCP_PlaneInfoManager();

   public static MCP_PlaneInfo get(String name) {
      return (MCP_PlaneInfo)ContentRegistries.plane().get(name);
   }

   public static MCP_PlaneInfoManager getInstance() {
      return instance;
   }

   public MCP_PlaneInfo newInfo(AddonResourceLocation name, String filepath) {
      return new MCP_PlaneInfo(name, filepath);
   }

   @Nullable
   public static MCP_PlaneInfo getFromItem(@Nullable Item item) {
      return getInstance().getAcInfoFromItem(item);
   }

   @Nullable
   public MCP_PlaneInfo getAcInfoFromItem(@Nullable Item item) {
      return (MCP_PlaneInfo)ContentRegistries.plane().findFirst((info) -> {
         return info.item == item;
      });
   }

   protected boolean contains(String name) {
      return ContentRegistries.plane().contains(name);
   }

   protected int size() {
      return ContentRegistries.plane().size();
   }
}
