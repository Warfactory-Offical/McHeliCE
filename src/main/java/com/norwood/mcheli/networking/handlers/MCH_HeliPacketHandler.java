package com.norwood.mcheli.networking.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.helicopter.MCH_EntityHeli;
import com.norwood.mcheli.helper.MCH_CriteriaTriggers;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.chain.MCH_EntityChain;
import com.norwood.mcheli.container.MCH_EntityContainer;
import com.norwood.mcheli.networking.packet.MCH_HeliPacketPlayerControl;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_HeliPacketHandler {
    @HandleSide({Side.SERVER})
    public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_HeliPacketPlayerControl pc = new MCH_HeliPacketPlayerControl();
            pc.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        MCH_EntityHeli heli = null;
                        if (player.getRidingEntity() instanceof MCH_EntityHeli) {
                            heli = (MCH_EntityHeli) player.getRidingEntity();
                        } else if (player.getRidingEntity() instanceof MCH_EntitySeat) {
                            if (((MCH_EntitySeat) player.getRidingEntity()).getParent() instanceof MCH_EntityHeli) {
                                heli = (MCH_EntityHeli) ((MCH_EntitySeat) player.getRidingEntity()).getParent();
                            }
                        } else if (player.getRidingEntity() instanceof MCH_EntityUavStation uavStation) {
                            if (uavStation.getControlAircract() instanceof MCH_EntityHeli) {
                                heli = (MCH_EntityHeli) uavStation.getControlAircract();
                            }
                        }

                        if (heli != null) {
                            if (pc.isUnmount == 1) {
                                heli.unmountEntity();
                            } else if (pc.isUnmount == 2) {
                                heli.unmountCrew();
                            } else {
                                if (pc.switchFold == 0) {
                                    heli.setFoldBladeStat((byte) 3);
                                }

                                if (pc.switchFold == 1) {
                                    heli.setFoldBladeStat((byte) 1);
                                }

                                if (pc.switchMode == 0) {
                                    heli.switchGunnerMode(false);
                                }

                                if (pc.switchMode == 1) {
                                    heli.switchGunnerMode(true);
                                }

                                if (pc.switchMode == 2) {
                                    heli.switchHoveringMode(false);
                                }

                                if (pc.switchMode == 3) {
                                    heli.switchHoveringMode(true);
                                }

                                if (pc.switchSearchLight) {
                                    heli.setSearchLight(!heli.isSearchLightON());
                                }

                                if (pc.switchCameraMode > 0) {
                                    heli.switchCameraMode(player, pc.switchCameraMode - 1);
                                }

                                if (pc.switchWeapon >= 0) {
                                    heli.switchWeapon(player, pc.switchWeapon);
                                }

                                if (pc.useWeapon) {
                                    MCH_WeaponParam prm = new MCH_WeaponParam();
                                    prm.entity = heli;
                                    prm.user = player;
                                    prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, 0.0F, 0.0F);
                                    prm.option1 = pc.useWeaponOption1;
                                    prm.option2 = pc.useWeaponOption2;
                                    heli.useCurrentWeapon(prm);
                                }

                                if (heli.isPilot(player)) {
                                    heli.throttleUp = pc.throttleUp;
                                    heli.throttleDown = pc.throttleDown;
                                    heli.moveLeft = pc.moveLeft;
                                    heli.moveRight = pc.moveRight;
                                }

                                if (pc.useFlareType > 0) {
                                    heli.useFlare(pc.useFlareType);
                                }

                                if (pc.unhitchChainId >= 0) {
                                    Entity e = player.world.getEntityByID(pc.unhitchChainId);
                                    if (e instanceof MCH_EntityChain) {
                                        if (((MCH_EntityChain) e).towedEntity instanceof MCH_EntityContainer
                                                && MCH_Lib.getBlockIdY(heli, 3, -20) == 0
                                                && player instanceof EntityPlayerMP) {
                                            MCH_CriteriaTriggers.RELIEF_SUPPLIES.trigger((EntityPlayerMP) player);
                                        }

                                        e.setDead();
                                    }
                                }

                                if (pc.openGui) {
                                    heli.openGui(player);
                                }

                                if (pc.switchHatch > 0) {
                                    heli.foldHatch(pc.switchHatch == 2);
                                }

                                if (pc.switchFreeLook > 0) {
                                    heli.switchFreeLookMode(pc.switchFreeLook == 1);
                                }

                                if (pc.switchGear == 1) {
                                    heli.foldLandingGear();
                                }

                                if (pc.switchGear == 2) {
                                    heli.unfoldLandingGear();
                                }

                                if (pc.putDownRack == 1) {
                                    heli.mountEntityToRack();
                                }

                                if (pc.putDownRack == 2) {
                                    heli.unmountEntityFromRack();
                                }

                                if (pc.putDownRack == 3) {
                                    heli.rideRack();
                                }

                                if (pc.isUnmount == 3) {
                                    heli.unmountAircraft();
                                }

                                if (pc.switchGunnerStatus) {
                                    heli.setGunnerStatus(!heli.getGunnerStatus());
                                }
                            }
                        }
                    }
            );
        }
    }
}
