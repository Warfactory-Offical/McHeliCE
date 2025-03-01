package com.norwood.mcheli.__helper.network;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.wrapper.W_NetworkRegistry;
import com.norwood.mcheli.wrapper.W_PacketDummy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MCH_WrapPacketHandler implements IMessageHandler<MCH_WrapPacketData, W_PacketDummy> {
   public W_PacketDummy onMessage(MCH_WrapPacketData message, MessageContext ctx) {
      try {
         ByteArrayDataInput data = message.createData();
         if (ctx.side.isClient()) {
            if (MCH_Lib.getClientPlayer() != null) {
               W_NetworkRegistry.packetHandler.onPacket(data, (EntityPlayer)MCH_Lib.getClientPlayer(), ctx);
            }
         } else {
            W_NetworkRegistry.packetHandler.onPacket(data, ctx.getServerHandler().field_147369_b, ctx);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return null;
   }
}
