package com.norwood.mcheli.gui;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.helper.MCH_Utils;
import com.norwood.mcheli.helper.network.PooledGuiParameter;
import com.norwood.mcheli.aircraft.MCH_AircraftGui;
import com.norwood.mcheli.aircraft.MCH_AircraftGuiContainer;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.block.MCH_DraftingTableGui;
import com.norwood.mcheli.block.MCH_DraftingTableGuiContainer;
import com.norwood.mcheli.multiplay.MCH_ContainerScoreboard;
import com.norwood.mcheli.multiplay.MCH_GuiScoreboard;
import com.norwood.mcheli.uav.MCH_ContainerUavStation;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.uav.MCH_GuiUavStation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MCH_GuiCommonHandler implements IGuiHandler {
    public static final int GUIID_UAV_STATION = 0;
    public static final int GUIID_AIRCRAFT = 1;
    public static final int GUIID_CONFG = 2;
    public static final int GUIID_INVENTORY = 3;
    public static final int GUIID_DRAFTING = 4;
    public static final int GUIID_MULTI_MNG = 5;

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        MCH_Lib.DbgLog(world, "MCH_GuiCommonHandler.getServerGuiElement ID=%d (%d, %d, %d)", id, x, y, z);
        switch (id) {
            case 0 -> {
                Entity uavStation = PooledGuiParameter.getEntity(player);
                PooledGuiParameter.resetEntity(player);
                if (uavStation instanceof MCH_EntityUavStation) {
                    return new MCH_ContainerUavStation(player.inventory, (MCH_EntityUavStation) uavStation);
                }
            }
            case 1 -> {
                MCH_EntityAircraft ac = null;
                if (player.getRidingEntity() instanceof MCH_EntityAircraft) {
                    ac = (MCH_EntityAircraft) player.getRidingEntity();
                } else if (player.getRidingEntity() instanceof MCH_EntityUavStation) {
                    ac = ((MCH_EntityUavStation) player.getRidingEntity()).getControlAircract();
                }

                if (ac != null) {
                    return new MCH_AircraftGuiContainer(player, ac);
                }
            }
            case 2 -> {
                return new MCH_ConfigGuiContainer(player);
            }
            case 4 -> {
                return new MCH_DraftingTableGuiContainer(player, x, y, z);
            }
            case 5 -> {
                if (!MCH_Utils.getServer().isSinglePlayer() || MCH_Config.DebugLog) {
                    return new MCH_ContainerScoreboard(player);
                }
            }
            default -> {
            }
        }

        return null;
    }

    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        MCH_Lib.DbgLog(world, "MCH_GuiCommonHandler.getClientGuiElement ID=%d (%d, %d, %d)", id, x, y, z);
        switch (id) {
            case 0 -> {
                Entity uavStation = PooledGuiParameter.getEntity(player);
                PooledGuiParameter.resetEntity(player);
                if (uavStation instanceof MCH_EntityUavStation) {
                    return new MCH_GuiUavStation(player.inventory, (MCH_EntityUavStation) uavStation);
                }
            }
            case 1 -> {
                MCH_EntityAircraft ac = null;
                if (player.getRidingEntity() instanceof MCH_EntityAircraft) {
                    ac = (MCH_EntityAircraft) player.getRidingEntity();
                } else if (player.getRidingEntity() instanceof MCH_EntityUavStation) {
                    ac = ((MCH_EntityUavStation) player.getRidingEntity()).getControlAircract();
                }

                if (ac != null) {
                    return new MCH_AircraftGui(player, ac);
                }
            }
            case 2 -> {
                return new MCH_ConfigGui(player);
            }
            case 4 -> {
                return new MCH_DraftingTableGui(player, x, y, z);
            }
            case 5 -> {
                return new MCH_GuiScoreboard(player);
            }
            default -> {
            }
        }

        return null;
    }
}
