package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;

public class MCH_PacketNotifyInfoReloaded extends MCH_Packet {
   public int type = -1;

   public int getMessageID() {
      return 536875063;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.type = data.readInt();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.type);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void sendRealodAc() {
      MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
      s.type = 0;
      W_Network.sendToServer(s);
   }

   public static void sendRealodAllWeapon() {
      MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
      s.type = 1;
      W_Network.sendToServer(s);
   }
}
