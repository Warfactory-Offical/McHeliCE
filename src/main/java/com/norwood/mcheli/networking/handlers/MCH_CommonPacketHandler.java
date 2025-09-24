package com.norwood.mcheli.networking.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.MCH_ServerSettings;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.helper.world.MCH_ExplosionV2;
import com.norwood.mcheli.lweapon.MCH_ClientLightWeaponTickHandler;
import com.norwood.mcheli.networking.packet.MCH_PacketEffectExplosion;
import com.norwood.mcheli.networking.packet.MCH_PacketIndOpenScreen;
import com.norwood.mcheli.networking.packet.PacketSyncServerSettings;
import com.norwood.mcheli.wrapper.W_Reflection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class MCH_CommonPacketHandler {
    @HandleSide({Side.CLIENT})
    public static void onPacketEffectExplosion(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (player.world.isRemote) {
            MCH_PacketEffectExplosion pkt = new MCH_PacketEffectExplosion();
            pkt.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        Entity exploder = null;
                        if (player.getDistanceSq(pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ) <= 40000.0) {
                            if (!pkt.prm.inWater) {
                                if (!MCH_Config.DefaultExplosionParticle.prmBool) {
                                    List<BlockPos> affectedPositions = pkt.prm.getAffectedBlockPositions();
                                    MCH_ExplosionV2.effectMODExplosion(player.world, pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ, pkt.prm.size, affectedPositions);
                                } else {
                                    List<BlockPos> affectedPositions = pkt.prm.getAffectedBlockPositions();
                                    MCH_ExplosionV2.effectVanillaExplosion(player.world, pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ, pkt.prm.size, affectedPositions);
                                }
                            } else {
                                MCH_ExplosionV2.effectExplosionInWater(player.world, pkt.prm.posX, pkt.prm.posY, pkt.prm.posZ, pkt.prm.size);
                            }
                        }
                    }
            );
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacketIndOpenScreen(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketIndOpenScreen pkt = new MCH_PacketIndOpenScreen();
            pkt.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        if (pkt.guiID == 3) {
                            MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
                            if (ac != null) {
                                ac.displayInventory(player);
                            }
                        } else {
                            player.openGui(
                                    MCH_MOD.instance, pkt.guiID, player.world, (int) player.posX, (int) player.posY, (int) player.posZ
                            );
                        }
                    }
            );
        }
    }


}
