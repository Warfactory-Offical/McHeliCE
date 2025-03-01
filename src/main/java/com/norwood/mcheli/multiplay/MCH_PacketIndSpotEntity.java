package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.EntityLivingBase;

public class MCH_PacketIndSpotEntity extends MCH_Packet {
   public int targetFilter = -1;

   public int getMessageID() {
      return 536873216;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.targetFilter = data.readInt();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.targetFilter);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void send(EntityLivingBase spoter, int targetFilter) {
      MCH_PacketIndSpotEntity s = new MCH_PacketIndSpotEntity();
      s.targetFilter = targetFilter;
      W_Network.sendToServer(s);
   }
}
