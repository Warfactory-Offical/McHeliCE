package com.norwood.mcheli.networking.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.multiplay.MCH_Multiplay;
import com.norwood.mcheli.multiplay.MCH_PacketIndSpotEntity;
import com.norwood.mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

public class MCH_ToolPacketHandler {
    @HandleSide({Side.SERVER})
    public static void onPacket_IndSpotEntity(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketIndSpotEntity pc = new MCH_PacketIndSpotEntity();
            pc.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        ItemStack itemStack = player.getHeldItemMainhand();
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof MCH_ItemRangeFinder) {
                            if (pc.targetFilter == 0) {
                                if (MCH_Multiplay.markPoint(player, player.posX, player.posY + player.getEyeHeight(), player.posZ)) {
                                    W_WorldFunc.MOD_playSoundAtEntity(player, "pi", 1.0F, 1.0F);
                                } else {
                                    W_WorldFunc.MOD_playSoundAtEntity(player, "ng", 1.0F, 1.0F);
                                }
                            } else if (itemStack.getMetadata() < itemStack.getMaxDamage()) {
                                if (MCH_Config.RangeFinderConsume.prmBool) {
                                    itemStack.damageItem(1, player);
                                }

                                int time = (pc.targetFilter & 252) == 0 ? 60 : MCH_Config.RangeFinderSpotTime.prmInt;
                                if (MCH_Multiplay.spotEntity(
                                        player,
                                        null,
                                        player.posX,
                                        player.posY + player.getEyeHeight(),
                                        player.posZ,
                                        pc.targetFilter,
                                        MCH_Config.RangeFinderSpotDist.prmInt,
                                        time,
                                        20.0F
                                )) {
                                    W_WorldFunc.MOD_playSoundAtEntity(player, "pi", 1.0F, 1.0F);
                                } else {
                                    W_WorldFunc.MOD_playSoundAtEntity(player, "ng", 1.0F, 1.0F);
                                }
                            }
                        }
                    }
            );
        }
    }
}
