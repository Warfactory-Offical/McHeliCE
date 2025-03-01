package com.norwood.mcheli.uav;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;

public class MCH_UavPacketStatus extends MCH_Packet {
   public byte posUavX = 0;
   public byte posUavY = 0;
   public byte posUavZ = 0;
   public boolean continueControl = false;

   public int getMessageID() {
      return 537133072;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.posUavX = data.readByte();
         this.posUavY = data.readByte();
         this.posUavZ = data.readByte();
         this.continueControl = data.readByte() != 0;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeByte(this.posUavX);
         dos.writeByte(this.posUavY);
         dos.writeByte(this.posUavZ);
         dos.writeByte(this.continueControl ? 1 : 0);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
