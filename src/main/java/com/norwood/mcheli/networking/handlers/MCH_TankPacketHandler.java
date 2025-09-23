package com.norwood.mcheli.networking.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.networking.packet.MCH_TankPacketPlayerControl;
import com.norwood.mcheli.tank.MCH_EntityTank;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_TankPacketHandler {
    @HandleSide({Side.SERVER})
    public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_TankPacketPlayerControl pc = new MCH_TankPacketPlayerControl();
            pc.readData(data);
            scheduler.addScheduledTask(() -> {
                MCH_EntityTank tank = null;
                if (player.getRidingEntity() instanceof MCH_EntityTank) {
                    tank = (MCH_EntityTank) player.getRidingEntity();
                } else if (player.getRidingEntity() instanceof MCH_EntitySeat) {
                    if (((MCH_EntitySeat) player.getRidingEntity()).getParent() instanceof MCH_EntityTank) {
                        tank = (MCH_EntityTank) ((MCH_EntitySeat) player.getRidingEntity()).getParent();
                    }
                } else if (player.getRidingEntity() instanceof MCH_EntityUavStation uavStation) {
                    if (uavStation.getControlAircract() instanceof MCH_EntityTank) {
                        tank = (MCH_EntityTank) uavStation.getControlAircract();
                    }
                }

                if (tank != null) {
                    if (pc.isUnmount == 1) {
                        tank.unmountEntity();
                    } else if (pc.isUnmount == 2) {
                        tank.unmountCrew();
                    } else if (pc.ejectSeat) {
                        tank.ejectSeat(player);
                    } else {
                        if (pc.switchMode == 0) {
                            tank.switchGunnerMode(false);
                        }

                        if (pc.switchMode == 1) {
                            tank.switchGunnerMode(true);
                        }

                        if (pc.switchMode == 2) {
                            tank.switchHoveringMode(false);
                        }

                        if (pc.switchMode == 3) {
                            tank.switchHoveringMode(true);
                        }

                        if (pc.switchSearchLight) {
                            tank.setSearchLight(!tank.isSearchLightON());
                        }

                        if (pc.switchCameraMode > 0) {
                            tank.switchCameraMode(player, pc.switchCameraMode - 1);
                        }

                        if (pc.switchWeapon >= 0) {
                            tank.switchWeapon(player, pc.switchWeapon);
                        }

                        if (pc.useWeapon) {
                            MCH_WeaponParam prm = new MCH_WeaponParam();
                            prm.entity = tank;
                            prm.user = player;
                            prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, 0.0F, 0.0F);
                            prm.option1 = pc.useWeaponOption1;
                            prm.option2 = pc.useWeaponOption2;
                            tank.useCurrentWeapon(prm);
                        }

                        if (tank.isPilot(player)) {
                            tank.throttleUp = pc.throttleUp;
                            tank.throttleDown = pc.throttleDown;
                            double dx = tank.posX - tank.prevPosX;
                            double dz = tank.posZ - tank.prevPosZ;
                            double dist = dx * dx + dz * dz;
                            if (pc.useBrake && tank.getCurrentThrottle() <= 0.03 && dist < 0.01) {
                                tank.moveLeft = false;
                                tank.moveRight = false;
                            }

                            tank.setBrake(pc.useBrake);
                        }

                        if (pc.useFlareType > 0) {
                            tank.useFlare(pc.useFlareType);
                        }

                        if (pc.openGui) {
                            tank.openGui(player);
                        }

                        if (pc.switchHatch > 0 && tank.getAcInfo().haveHatch()) {
                            tank.foldHatch(pc.switchHatch == 2);
                        }

                        if (pc.switchFreeLook > 0) {
                            tank.switchFreeLookMode(pc.switchFreeLook == 1);
                        }

                        if (pc.switchGear == 1) {
                            tank.foldLandingGear();
                        }

                        if (pc.switchGear == 2) {
                            tank.unfoldLandingGear();
                        }

                        if (pc.putDownRack == 1) {
                            tank.mountEntityToRack();
                        }

                        if (pc.putDownRack == 2) {
                            tank.unmountEntityFromRack();
                        }

                        if (pc.putDownRack == 3) {
                            tank.rideRack();
                        }

                        if (pc.isUnmount == 3) {
                            tank.unmountAircraft();
                        }

                        if (pc.switchGunnerStatus) {
                            tank.setGunnerStatus(!tank.getGunnerStatus());
                        }
                    }
                }
            });
        }
    }
}
