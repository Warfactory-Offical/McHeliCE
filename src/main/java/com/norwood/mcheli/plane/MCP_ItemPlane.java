package com.norwood.mcheli.plane;

import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_ItemAircraft;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MCP_ItemPlane extends MCH_ItemAircraft {
   public MCP_ItemPlane(int par1) {
      super(par1);
      this.field_77777_bU = 1;
   }

   public MCH_AircraftInfo getAircraftInfo() {
      return MCP_PlaneInfoManager.getFromItem(this);
   }

   @Nullable
   public MCP_EntityPlane createAircraft(World world, double x, double y, double z, ItemStack itemStack) {
      MCP_PlaneInfo info = MCP_PlaneInfoManager.getFromItem(this);
      if (info == null) {
         MCH_Lib.Log(world, "##### MCP_EntityPlane Plane info null %s", this.func_77658_a());
         return null;
      } else {
         MCP_EntityPlane plane = new MCP_EntityPlane(world);
         plane.func_70107_b(x, y, z);
         plane.field_70169_q = x;
         plane.field_70167_r = y;
         plane.field_70166_s = z;
         plane.camera.setPosition(x, y, z);
         plane.setTypeName(info.name);
         if (!world.field_72995_K) {
            plane.setTextureName(info.getTextureName());
         }

         return plane;
      }
   }
}
