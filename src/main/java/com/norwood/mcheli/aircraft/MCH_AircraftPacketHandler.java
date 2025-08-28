package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.helper.MCH_Utils;
import com.norwood.mcheli.helper.info.ContentRegistries;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MCH_AircraftPacketHandler {
    @HandleSide({Side.SERVER})
    public static void onPacketIndRotation(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player != null && !player.world.isRemote) {
            MCH_PacketIndRotation req = new MCH_PacketIndRotation();
            req.readData(data);
            if (req.entityID_Ac > 0) {
                scheduler.addScheduledTask(() -> {
                    Entity e = player.world.getEntityByID(req.entityID_Ac);
                    if (e instanceof MCH_EntityAircraft ac) {
                        ac.setRotRoll(req.roll);
                        if (req.rollRev) {
                            MCH_Lib.DbgLog(ac.world, "onPacketIndRotation Error:req.rollRev y=%.2f, p=%.2f, r=%.2f", req.yaw, req.pitch, req.roll);
                            if (ac.getRiddenByEntity() != null) {
                                ac.getRiddenByEntity().rotationYaw = req.yaw;
                                ac.getRiddenByEntity().prevRotationYaw = req.yaw;
                            }

                            for (int sid = 0; sid < ac.getSeatNum(); sid++) {
                                Entity entity = ac.getEntityBySeatId(1 + sid);
                                if (entity != null) {
                                    entity.rotationYaw = entity.rotationYaw + (entity.rotationYaw <= 0.0F ? 180.0F : -180.0F);
                                }
                            }
                        }

                        ac.setRotYaw(req.yaw);
                        ac.setRotPitch(req.pitch);
                    }
                });
            }
        }
    }

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
    public static void onPacketNotifyAmmoNum(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player != null && player.world.isRemote) {
            MCH_PacketNotifyAmmoNum status = new MCH_PacketNotifyAmmoNum();
            status.readData(data);
            if (status.entityID_Ac > 0) {
                scheduler.addScheduledTask(() -> {
                    Entity e = player.world.getEntityByID(status.entityID_Ac);
                    if (e instanceof MCH_EntityAircraft ac) {
                        StringBuilder msg = new StringBuilder("onPacketNotifyAmmoNum:");
                        msg.append(ac.getAcInfo() != null ? ac.getAcInfo().displayName : "null").append(":");
                        if (status.all) {
                            msg.append("All=true, Num=").append(status.num);

                            for (int i = 0; i < ac.getWeaponNum() && i < status.num; i++) {
                                ac.getWeapon(i).setAmmoNum(status.ammo[i]);
                                ac.getWeapon(i).setRestAllAmmoNum(status.restAmmo[i]);
                                msg.append(", [").append(status.ammo[i]).append("/").append(status.restAmmo[i]).append("]");
                            }

                            MCH_Lib.DbgLog(e.world, msg.toString());
                        } else if (status.weaponID < ac.getWeaponNum()) {
                            msg.append("All=false, WeaponID=").append(status.weaponID).append(", ").append(status.ammo[0]).append(", ").append(status.restAmmo[0]);
                            ac.getWeapon(status.weaponID).setAmmoNum(status.ammo[0]);
                            ac.getWeapon(status.weaponID).setRestAllAmmoNum(status.restAmmo[0]);
                            MCH_Lib.DbgLog(e.world, msg.toString());
                        } else {
                            MCH_Lib.DbgLog(e.world, "Error:" + status.weaponID);
                        }
                    }
                });
            }
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketStatusRequest(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketStatusRequest req = new MCH_PacketStatusRequest();
            req.readData(data);
            if (req.entityID_AC > 0) {
                scheduler.addScheduledTask(() -> {
                    Entity e = player.world.getEntityByID(req.entityID_AC);
                    if (e instanceof MCH_EntityAircraft) {
                        MCH_PacketStatusResponse.sendStatus((MCH_EntityAircraft) e, player);
                    }
                });
            }
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketIndNotifyAmmoNum(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketIndNotifyAmmoNum req = new MCH_PacketIndNotifyAmmoNum();
            req.readData(data);
            if (req.entityID_Ac > 0) {
                scheduler.addScheduledTask(() -> {
                    Entity e = player.world.getEntityByID(req.entityID_Ac);
                    if (e instanceof MCH_EntityAircraft) {
                        if (req.weaponID >= 0) {
                            MCH_PacketNotifyAmmoNum.sendAmmoNum((MCH_EntityAircraft) e, player, req.weaponID);
                        } else {
                            MCH_PacketNotifyAmmoNum.sendAllAmmoNum((MCH_EntityAircraft) e, player);
                        }
                    }
                });
            }
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketIndReload(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketIndReload ind = new MCH_PacketIndReload();
            ind.readData(data);
            if (ind.entityID_Ac > 0) {
                scheduler.addScheduledTask(() -> {
                    Entity e = player.world.getEntityByID(ind.entityID_Ac);
                    if (e instanceof MCH_EntityAircraft ac) {
                        MCH_Lib.DbgLog(e.world, "onPacketIndReload :%s", ac.getAcInfo().displayName);
                        ac.supplyAmmo(ind.weaponID);
                    }
                });
            }
        }
    }

    @HandleSide({Side.CLIENT})
    public static void onPacketStatusResponse(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketStatusResponse status = new MCH_PacketStatusResponse();
            status.readData(data);
            if (status.entityID_AC > 0) {
                scheduler.addScheduledTask(() -> {
                    StringBuilder msg = new StringBuilder("onPacketStatusResponse:EID=" + status.entityID_AC + ":");
                    Entity e = player.world.getEntityByID(status.entityID_AC);
                    if (e instanceof MCH_EntityAircraft ac) {
                        if (status.seatNum > 0 && status.weaponIDs != null && status.weaponIDs.length == status.seatNum) {
                            msg.append("seatNum=").append(status.seatNum).append(":");

                            for (int i = 0; i < status.seatNum; i++) {
                                ac.updateWeaponID(i, status.weaponIDs[i]);
                                msg.append("[").append(i).append(",").append(status.weaponIDs[i]).append("]");
                            }
                        } else {
                            msg.append("Error seatNum=").append(status.seatNum);
                        }
                    }

                    MCH_Lib.DbgLog(true, msg.toString());
                });
            }
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
    public static void onPacketNotifyTVMissileEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketNotifyTVMissileEntity packet = new MCH_PacketNotifyTVMissileEntity();
            packet.readData(data);
            if (packet.entityID_Ac <= 0) {
                return;
            }

            if (packet.entityID_TVMissile <= 0) {
                return;
            }

            scheduler.addScheduledTask(() -> {
                Entity e = player.world.getEntityByID(packet.entityID_Ac);
                if (e instanceof MCH_EntityAircraft ac) {
                    e = player.world.getEntityByID(packet.entityID_TVMissile);
                    if (e instanceof MCH_EntityTvMissile) {
                        ((MCH_EntityTvMissile) e).shootingEntity = player;
                        ac.setTVMissile((MCH_EntityTvMissile) e);
                    }
                }
            });
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

    @HandleSide({Side.SERVER})
    public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketSeatPlayerControl pc = new MCH_PacketSeatPlayerControl();
            pc.readData(data);
            scheduler.addScheduledTask(() -> {
                MCH_EntityAircraft ac;
                if (player.getRidingEntity() instanceof MCH_EntitySeat seat) {
                    ac = seat.getParent();
                } else {
                    ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
                }

                if (ac != null) {
                    if (pc.isUnmount) {
                        ac.unmountEntityFromSeat(player);
                    } else if (pc.switchSeat > 0) {
                        if (pc.switchSeat == 3) {
                            player.dismountRidingEntity();
                            ac.keepOnRideRotation = true;
                            ac.processInitialInteract(player, true, EnumHand.MAIN_HAND);
                        }

                        if (pc.switchSeat == 1) {
                            ac.switchNextSeat(player);
                        }

                        if (pc.switchSeat == 2) {
                            ac.switchPrevSeat(player);
                        }
                    } else if (pc.parachuting) {
                        ac.unmount(player);
                    }
                }
            });
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacket_ClientSetting(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketNotifyClientSetting pc = new MCH_PacketNotifyClientSetting();
            pc.readData(data);
            scheduler.addScheduledTask(() -> {
                MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
                if (ac != null) {
                    int sid = ac.getSeatIdByEntity(player);
                    if (sid == 0) {
                        ac.cs_dismountAll = pc.dismountAll;
                        ac.cs_heliAutoThrottleDown = pc.heliAutoThrottleDown;
                        ac.cs_planeAutoThrottleDown = pc.planeAutoThrottleDown;
                        ac.cs_tankAutoThrottleDown = pc.tankAutoThrottleDown;
                    }

                    ac.camera.setShaderSupport(sid, pc.shaderSupport);
                }
            });
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketNotifyInfoReloaded(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketNotifyInfoReloaded pc = new MCH_PacketNotifyInfoReloaded();
            pc.readData(data);
            scheduler.addScheduledTask(() -> {
                switch (pc.type) {
                    case 0:
                        MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
                        if (ac != null && ac.getAcInfo() != null) {
                            String name = ac.getAcInfo().name;

                            for (WorldServer world : MCH_Utils.getServer().worlds) {
                                List<Entity> list = new ArrayList<>(world.loadedEntityList);

                                for (Entity entity : list) {
                                    if (entity instanceof MCH_EntityAircraft) {
                                        ac = (MCH_EntityAircraft) entity;
                                        if (ac.getAcInfo() != null && ac.getAcInfo().name.equals(name)) {
                                            ac.changeType(name);
                                            ac.createSeats(UUID.randomUUID().toString());
                                            ac.onAcInfoReloaded();
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case 1:
                        ContentRegistries.weapon().reloadAll();

                        for (WorldServer world : MCH_Utils.getServer().worlds) {
                            List<Entity> list = new ArrayList<>(world.loadedEntityList);

                            for (Entity entity : list) {
                                if (entity instanceof MCH_EntityAircraft entityAircraft) {
                                    if (entityAircraft.getAcInfo() != null) {
                                        entityAircraft.changeType(entityAircraft.getAcInfo().name);
                                        entityAircraft.createSeats(UUID.randomUUID().toString());
                                    }
                                }
                            }
                        }
                }
            });
        }
    }
}
