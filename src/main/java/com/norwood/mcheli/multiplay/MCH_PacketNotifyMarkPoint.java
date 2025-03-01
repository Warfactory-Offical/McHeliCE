package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_PacketNotifyMarkPoint extends MCH_Packet {
   public int px;
   public int py;
   public int pz;

   public MCH_PacketNotifyMarkPoint() {
      this.px = this.pz = 0;
      this.py = 0;
   }

   public int getMessageID() {
      return 268437762;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.px = data.readInt();
         this.py = data.readInt();
         this.pz = data.readInt();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.px);
         dos.writeInt(this.py);
         dos.writeInt(this.pz);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void send(EntityPlayer player, int x, int y, int z) {
      MCH_PacketNotifyMarkPoint pkt = new MCH_PacketNotifyMarkPoint();
      pkt.px = x;
      pkt.py = y;
      pkt.pz = z;
      W_Network.sendToPlayer(pkt, player);
   }
}
