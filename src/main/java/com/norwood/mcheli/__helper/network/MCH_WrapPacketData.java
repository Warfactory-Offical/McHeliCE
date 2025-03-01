package com.norwood.mcheli.__helper.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import com.norwood.mcheli.wrapper.W_PacketBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MCH_WrapPacketData implements IMessage {
   private byte[] data;

   public MCH_WrapPacketData() {
      this.data = new byte[0];
   }

   public MCH_WrapPacketData(W_PacketBase packet) {
      this.data = packet.createData();
   }

   public void fromBytes(ByteBuf buf) {
      this.data = new byte[buf.capacity()];
      buf.getBytes(0, this.data);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBytes(this.data);
   }

   public ByteArrayDataInput createData() {
      return ByteStreams.newDataInput(this.data);
   }
}
