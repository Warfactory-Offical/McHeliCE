package com.norwood.mcheli;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.wrapper.W_Network;

public class MCH_PacketIndOpenScreen extends MCH_Packet {
   public int guiID = -1;

   @Override
   public int getMessageID() {
      return 536872992;
   }

   @Override
   public void readData(ByteArrayDataInput data) {
      try {
         this.guiID = data.readInt();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.guiID);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public static void send(int gui_id) {
      if (gui_id >= 0) {
         MCH_PacketIndOpenScreen s = new MCH_PacketIndOpenScreen();
         s.guiID = gui_id;
         W_Network.sendToServer(s);
      }
   }
}
