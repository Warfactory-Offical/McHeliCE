package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.util.List;
import java.util.UUID;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.__helper.network.HandleSide;
import com.norwood.mcheli.weapon.MCH_EntityTvMissile;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_AircraftPacketHandler {
   @HandleSide({Side.SERVER})
   public static void onPacketIndRotation(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player != null && !player.field_70170_p.field_72995_K) {
         MCH_PacketIndRotation req = new MCH_PacketIndRotation();
         req.readData(data);
         if (req.entityID_Ac > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(req.entityID_Ac);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)e;
                  ac.setRotRoll(req.roll);
                  if (req.rollRev) {
                     MCH_Lib.DbgLog(ac.field_70170_p, "onPacketIndRotation Error:req.rollRev y=%.2f, p=%.2f, r=%.2f", req.yaw, req.pitch, req.roll);
                     if (ac.getRiddenByEntity() != null) {
                        ac.getRiddenByEntity().field_70177_z = req.yaw;
                        ac.getRiddenByEntity().field_70126_B = req.yaw;
                     }

                     for(int sid = 0; sid < ac.getSeatNum(); ++sid) {
                        Entity entity = ac.getEntityBySeatId(1 + sid);
                        if (entity != null) {
                           entity.field_70177_z += entity.field_70177_z <= 0.0F ? 180.0F : -180.0F;
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

   /** @deprecated */
   @Deprecated
   @HandleSide({Side.SERVER})
   public static void onPacketOnMountEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player != null && player.field_70170_p.field_72995_K) {
         MCH_PacketNotifyOnMountEntity req = new MCH_PacketNotifyOnMountEntity();
         req.readData(data);
         scheduler.func_152344_a(() -> {
            MCH_Lib.DbgLog(player.field_70170_p, "onPacketOnMountEntity.rcv:%d, %d, %d, %d", W_Entity.getEntityId(player), req.entityID_Ac, req.entityID_rider, req.seatID);
            if (req.entityID_Ac > 0) {
               if (req.entityID_rider > 0) {
                  if (req.seatID >= 0) {
                     Entity e = player.field_70170_p.func_73045_a(req.entityID_Ac);
                     if (e instanceof MCH_EntityAircraft) {
                        MCH_Lib.DbgLog(player.field_70170_p, "onPacketOnMountEntity:" + W_Entity.getEntityId(player));
                     }

                  }
               }
            }
         });
      }
   }

   @HandleSide({Side.CLIENT})
   public static void onPacketNotifyAmmoNum(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player != null && player.field_70170_p.field_72995_K) {
         MCH_PacketNotifyAmmoNum status = new MCH_PacketNotifyAmmoNum();
         status.readData(data);
         if (status.entityID_Ac > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(status.entityID_Ac);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)e;
                  String msg = "onPacketNotifyAmmoNum:";
                  msg = msg + (ac.getAcInfo() != null ? ac.getAcInfo().displayName : "null") + ":";
                  if (status.all) {
                     msg = msg + "All=true, Num=" + status.num;

                     for(int i = 0; i < ac.getWeaponNum() && i < status.num; ++i) {
                        ac.getWeapon(i).setAmmoNum(status.ammo[i]);
                        ac.getWeapon(i).setRestAllAmmoNum(status.restAmmo[i]);
                        msg = msg + ", [" + status.ammo[i] + "/" + status.restAmmo[i] + "]";
                     }

                     MCH_Lib.DbgLog(e.field_70170_p, msg);
                  } else if (status.weaponID < ac.getWeaponNum()) {
                     msg = msg + "All=false, WeaponID=" + status.weaponID + ", " + status.ammo[0] + ", " + status.restAmmo[0];
                     ac.getWeapon(status.weaponID).setAmmoNum(status.ammo[0]);
                     ac.getWeapon(status.weaponID).setRestAllAmmoNum(status.restAmmo[0]);
                     MCH_Lib.DbgLog(e.field_70170_p, msg);
                  } else {
                     MCH_Lib.DbgLog(e.field_70170_p, "Error:" + status.weaponID);
                  }
               }

            });
         }
      }
   }

   @HandleSide({Side.SERVER})
   public static void onPacketStatusRequest(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.field_70170_p.field_72995_K) {
         MCH_PacketStatusRequest req = new MCH_PacketStatusRequest();
         req.readData(data);
         if (req.entityID_AC > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(req.entityID_AC);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_PacketStatusResponse.sendStatus((MCH_EntityAircraft)e, player);
               }

            });
         }
      }
   }

   @HandleSide({Side.SERVER})
   public static void onPacketIndNotifyAmmoNum(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.field_70170_p.field_72995_K) {
         MCH_PacketIndNotifyAmmoNum req = new MCH_PacketIndNotifyAmmoNum();
         req.readData(data);
         if (req.entityID_Ac > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(req.entityID_Ac);
               if (e instanceof MCH_EntityAircraft) {
                  if (req.weaponID >= 0) {
                     MCH_PacketNotifyAmmoNum.sendAmmoNum((MCH_EntityAircraft)e, player, req.weaponID);
                  } else {
                     MCH_PacketNotifyAmmoNum.sendAllAmmoNum((MCH_EntityAircraft)e, player);
                  }
               }

            });
         }
      }
   }

   @HandleSide({Side.SERVER})
   public static void onPacketIndReload(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.field_70170_p.field_72995_K) {
         MCH_PacketIndReload ind = new MCH_PacketIndReload();
         ind.readData(data);
         if (ind.entityID_Ac > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(ind.entityID_Ac);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)e;
                  MCH_Lib.DbgLog(e.field_70170_p, "onPacketIndReload :%s", ac.getAcInfo().displayName);
                  ac.supplyAmmo(ind.weaponID);
               }

            });
         }
      }
   }

   @HandleSide({Side.CLIENT})
   public static void onPacketStatusResponse(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.field_70170_p.field_72995_K) {
         MCH_PacketStatusResponse status = new MCH_PacketStatusResponse();
         status.readData(data);
         if (status.entityID_AC > 0) {
            scheduler.func_152344_a(() -> {
               String msg = "onPacketStatusResponse:EID=" + status.entityID_AC + ":";
               Entity e = player.field_70170_p.func_73045_a(status.entityID_AC);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)e;
                  if (status.seatNum > 0 && status.weaponIDs != null && status.weaponIDs.length == status.seatNum) {
                     msg = msg + "seatNum=" + status.seatNum + ":";

                     for(int i = 0; i < status.seatNum; ++i) {
                        ac.updateWeaponID(i, status.weaponIDs[i]);
                        msg = msg + "[" + i + "," + status.weaponIDs[i] + "]";
                     }
                  } else {
                     msg = msg + "Error seatNum=" + status.seatNum;
                  }
               }

               MCH_Lib.DbgLog(true, msg);
            });
         }
      }
   }

   @HandleSide({Side.CLIENT})
   public static void onPacketNotifyWeaponID(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.field_70170_p.field_72995_K) {
         MCH_PacketNotifyWeaponID status = new MCH_PacketNotifyWeaponID();
         status.readData(data);
         if (status.entityID_Ac > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(status.entityID_Ac);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)e;
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
      if (player.field_70170_p.field_72995_K) {
         MCH_PacketNotifyHitBullet status = new MCH_PacketNotifyHitBullet();
         status.readData(data);
         scheduler.func_152344_a(() -> {
            if (status.entityID_Ac <= 0) {
               MCH_MOD.proxy.hitBullet();
            } else {
               Entity e = player.field_70170_p.func_73045_a(status.entityID_Ac);
               if (e instanceof MCH_EntityAircraft) {
                  ((MCH_EntityAircraft)e).hitBullet();
               }
            }

         });
      }
   }

   @HandleSide({Side.SERVER})
   public static void onPacketSeatListRequest(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.field_70170_p.field_72995_K) {
         MCH_PacketSeatListRequest req = new MCH_PacketSeatListRequest();
         req.readData(data);
         if (req.entityID_AC > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(req.entityID_AC);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_PacketSeatListResponse.sendSeatList((MCH_EntityAircraft)e, player);
               }

            });
         }
      }
   }

   @HandleSide({Side.CLIENT})
   public static void onPacketNotifyTVMissileEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.field_70170_p.field_72995_K) {
         MCH_PacketNotifyTVMissileEntity packet = new MCH_PacketNotifyTVMissileEntity();
         packet.readData(data);
         if (packet.entityID_Ac <= 0) {
            return;
         }

         if (packet.entityID_TVMissile <= 0) {
            return;
         }

         scheduler.func_152344_a(() -> {
            Entity e = player.field_70170_p.func_73045_a(packet.entityID_Ac);
            if (e != null && e instanceof MCH_EntityAircraft) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)e;
               e = player.field_70170_p.func_73045_a(packet.entityID_TVMissile);
               if (e != null && e instanceof MCH_EntityTvMissile) {
                  ((MCH_EntityTvMissile)e).shootingEntity = player;
                  ac.setTVMissile((MCH_EntityTvMissile)e);
               }
            }
         });
      }

   }

   @HandleSide({Side.CLIENT})
   public static void onPacketSeatListResponse(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (player.field_70170_p.field_72995_K) {
         MCH_PacketSeatListResponse seatList = new MCH_PacketSeatListResponse();
         seatList.readData(data);
         if (seatList.entityID_AC > 0) {
            scheduler.func_152344_a(() -> {
               Entity e = player.field_70170_p.func_73045_a(seatList.entityID_AC);
               if (e instanceof MCH_EntityAircraft) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)e;
                  if (seatList.seatNum > 0 && seatList.seatNum == ac.getSeats().length && seatList.seatEntityID != null && seatList.seatEntityID.length == seatList.seatNum) {
                     for(int i = 0; i < seatList.seatNum; ++i) {
                        Entity entity = player.field_70170_p.func_73045_a(seatList.seatEntityID[i]);
                        if (entity instanceof MCH_EntitySeat) {
                           MCH_EntitySeat seat = (MCH_EntitySeat)entity;
                           seat.seatID = i;
                           seat.parentUniqueID = ac.getCommonUniqueId();
                           ac.setSeat(i, seat);
                           seat.setParent(ac);
                        }
                     }
                  }
               }

            });
         }
      }
   }

   @HandleSide({Side.SERVER})
   public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
      if (!player.field_70170_p.field_72995_K) {
         MCH_PacketSeatPlayerControl pc = new MCH_PacketSeatPlayerControl();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            MCH_EntityAircraft ac = null;
            if (player.func_184187_bx() instanceof MCH_EntitySeat) {
               MCH_EntitySeat seat = (MCH_EntitySeat)player.func_184187_bx();
               ac = seat.getParent();
            } else {
               ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
            }

            if (ac != null) {
               if (pc.isUnmount) {
                  ac.unmountEntityFromSeat(player);
               } else if (pc.switchSeat > 0) {
                  if (pc.switchSeat == 3) {
                     player.func_184210_p();
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
      if (!player.field_70170_p.field_72995_K) {
         MCH_PacketNotifyClientSetting pc = new MCH_PacketNotifyClientSetting();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
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
      if (!player.field_70170_p.field_72995_K) {
         MCH_PacketNotifyInfoReloaded pc = new MCH_PacketNotifyInfoReloaded();
         pc.readData(data);
         scheduler.func_152344_a(() -> {
            MCH_EntityAircraft ac;
            int var5;
            switch(pc.type) {
            case 0:
               ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
               if (ac != null && ac.getAcInfo() != null) {
                  String name = ac.getAcInfo().name;
                  WorldServer[] var11 = MCH_Utils.getServer().field_71305_c;
                  var5 = var11.length;

                  for(int var12 = 0; var12 < var5; ++var12) {
                     WorldServer world = var11[var12];
                     List<Entity> list = world.field_72996_f;

                     for(int ix = 0; ix < list.size(); ++ix) {
                        if (list.get(ix) instanceof MCH_EntityAircraft) {
                           ac = (MCH_EntityAircraft)list.get(ix);
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
               WorldServer[] var3 = MCH_Utils.getServer().field_71305_c;
               int var4 = var3.length;

               for(var5 = 0; var5 < var4; ++var5) {
                  WorldServer worldx = var3[var5];
                  List<Entity> listx = worldx.field_72996_f;

                  for(int i = 0; i < listx.size(); ++i) {
                     if (listx.get(i) instanceof MCH_EntityAircraft) {
                        ac = (MCH_EntityAircraft)listx.get(i);
                        if (ac.getAcInfo() != null) {
                           ac.changeType(ac.getAcInfo().name);
                           ac.createSeats(UUID.randomUUID().toString());
                        }
                     }
                  }
               }
            }

         });
      }
   }
}
