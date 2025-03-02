package com.norwood.mcheli.tool;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.__helper.network.HandleSide;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
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
         scheduler.func_152344_a(() -> {
            ItemStack itemStack = player.func_184614_ca();
            if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemRangeFinder) {
               if (pc.targetFilter == 0) {
                  if (MCH_Multiplay.markPoint(player, player.posX, player.posY + (double)player.func_70047_e(), player.posZ)) {
                     W_WorldFunc.MOD_playSoundAtEntity(player, "pi", 1.0F, 1.0F);
                  } else {
                     W_WorldFunc.MOD_playSoundAtEntity(player, "ng", 1.0F, 1.0F);
                  }
               } else if (itemStack.func_77960_j() < itemStack.func_77958_k()) {
                  if (MCH_Config.RangeFinderConsume.prmBool) {
                     itemStack.func_77972_a(1, player);
                  }

                  int time = (pc.targetFilter & 252) == 0 ? 60 : MCH_Config.RangeFinderSpotTime.prmInt;
                  if (MCH_Multiplay.spotEntity(player, (MCH_EntityAircraft)null, player.posX, player.posY + (double)player.func_70047_e(), player.posZ, pc.targetFilter, (float)MCH_Config.RangeFinderSpotDist.prmInt, time, 20.0F)) {
                     W_WorldFunc.MOD_playSoundAtEntity(player, "pi", 1.0F, 1.0F);
                  } else {
                     W_WorldFunc.MOD_playSoundAtEntity(player, "ng", 1.0F, 1.0F);
                  }
               }
            }

         });
      }
   }
}
