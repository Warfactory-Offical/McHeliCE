package com.norwood.mcheli.plane;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.aircraft.MCH_PacketPlayerControlBase;

public class MCP_PlanePacketPlayerControl extends MCH_PacketPlayerControlBase {
   public byte switchVtol = -1;

   public int getMessageID() {
      return 536903696;
   }

   public void readData(ByteArrayDataInput data) {
      super.readData(data);

      try {
         this.switchVtol = data.readByte();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      super.writeData(dos);

      try {
         dos.writeByte(this.switchVtol);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
