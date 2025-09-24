package com.norwood.mcheli.networking.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.networking.packet.*;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_AircraftPacketHandler {

    @Deprecated
    @HandleSide({Side.SERVER})
    public static void onPacketOnMountEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player != null && player.world.isRemote) {
            MCH_PacketNotifyOnMountEntity req = new MCH_PacketNotifyOnMountEntity();
            req.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        MCH_Lib.DbgLog(
                                player.world,
                                "onPacketOnMountEntity.rcv:%d, %d, %d, %d",
                                W_Entity.getEntityId(player),
                                req.entityID_Ac,
                                req.entityID_rider,
                                req.seatID
                        );
                        if (req.entityID_Ac > 0) {
                            if (req.entityID_rider > 0) {
                                if (req.seatID >= 0) {
                                    Entity e = player.world.getEntityByID(req.entityID_Ac);
                                    if (e instanceof MCH_EntityAircraft) {
                                        MCH_Lib.DbgLog(player.world, "onPacketOnMountEntity:" + W_Entity.getEntityId(player));
                                    }
                                }
                            }
                        }
                    }
            );
        }
    }


    @HandleSide({Side.CLIENT})
    public static void onPacketNotifyWeaponID(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketNotifyWeaponID status = new MCH_PacketNotifyWeaponID();
            status.readData(data);
            if (status.entityID_Ac > 0) {
                scheduler.addScheduledTask(() -> {
                    Entity e = player.world.getEntityByID(status.entityID_Ac);
                    if (e instanceof MCH_EntityAircraft ac) {
                        if (ac.isValidSeatID(status.seatID)) {
                            ac.getWeapon(status.weaponID).setAmmoNum(status.ammo);
                            ac.getWeapon(status.weaponID).setRestAllAmmoNum(status.restAmmo);
                            MCH_Lib.DbgLog(true, "onPacketNotifyWeaponID:WeaponID=%d (%d / %d)", status.weaponID, status.ammo, status.restAmmo);
                            if (W_Lib.isClientPlayer(ac.getEntityBySeatId(status.seatID))) {
                                MCH_Lib.DbgLog(true, "onPacketNotifyWeaponID:#discard:SeatID=%d, WeaponID=%d", status.seatID, status.weaponID);
                            } else {
                                MCH_Lib.DbgLog(true, "onPacketNotifyWeaponID:SeatID=%d, WeaponID=%d", status.seatID, status.weaponID);
                                ac.updateWeaponID(status.seatID, status.weaponID);
                            }
                        }
                    }
                });
            }
        }
    }

    @HandleSide({Side.CLIENT})
    public static void onPacketNotifyHitBullet(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketNotifyHitBullet status = new MCH_PacketNotifyHitBullet();
            status.readData(data);
            scheduler.addScheduledTask(() -> {
                if (status.entityID_Ac <= 0) {
                    MCH_MOD.proxy.hitBullet();
                } else {
                    Entity e = player.world.getEntityByID(status.entityID_Ac);
                    if (e instanceof MCH_EntityAircraft) {
                        ((MCH_EntityAircraft) e).hitBullet();
                    }
                }
            });
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketSeatListRequest(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketSeatListRequest req = new MCH_PacketSeatListRequest();
            req.readData(data);
            if (req.entityID_AC > 0) {
                scheduler.addScheduledTask(() -> {
                    Entity e = player.world.getEntityByID(req.entityID_AC);
                    if (e instanceof MCH_EntityAircraft) {
                        MCH_PacketSeatListResponse.sendSeatList((MCH_EntityAircraft) e, player);
                    }
                });
            }
        }
    }


    @HandleSide({Side.CLIENT})
    public static void onPacketSeatListResponse(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketSeatListResponse seatList = new MCH_PacketSeatListResponse();
            seatList.readData(data);
            if (seatList.entityID_AC > 0) {
                scheduler.addScheduledTask(
                        () -> {
                            Entity e = player.world.getEntityByID(seatList.entityID_AC);
                            if (e instanceof MCH_EntityAircraft ac) {
                                if (seatList.seatNum > 0
                                        && seatList.seatNum == ac.getSeats().length
                                        && seatList.seatEntityID != null
                                        && seatList.seatEntityID.length == seatList.seatNum) {
                                    for (int i = 0; i < seatList.seatNum; i++) {
                                        Entity entity = player.world.getEntityByID(seatList.seatEntityID[i]);
                                        if (entity instanceof MCH_EntitySeat seat) {
                                            seat.seatID = i;
                                            seat.parentUniqueID = ac.getCommonUniqueId();
                                            ac.setSeat(i, seat);
                                            seat.setParent(ac);
                                        }
                                    }
                                }
                            }
                        }
                );
            }
        }
    }


}
