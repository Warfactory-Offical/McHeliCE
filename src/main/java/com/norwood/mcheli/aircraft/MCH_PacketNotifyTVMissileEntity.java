package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;

public class MCH_PacketNotifyTVMissileEntity extends MCH_Packet {
   public int entityID_Ac = -1;
   public int entityID_TVMissile = -1;

   @Override
   public int getMessageID() {
      return 268439600;
   }

   @Override
   public void readData(ByteArrayDataInput data) {
      try {
         this.entityID_Ac = data.readInt();
         this.entityID_TVMissile = data.readInt();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.entityID_Ac);
         dos.writeInt(this.entityID_TVMissile);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public static void send(int heliEntityID, int tvMissileEntityID) {
      MCH_PacketNotifyTVMissileEntity s = new MCH_PacketNotifyTVMissileEntity();
      s.entityID_Ac = heliEntityID;
      s.entityID_TVMissile = tvMissileEntityID;
      W_Network.sendToAllPlayers(s);
   }
}
