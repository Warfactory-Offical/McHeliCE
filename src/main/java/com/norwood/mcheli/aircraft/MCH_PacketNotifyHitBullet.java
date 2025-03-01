package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_PacketNotifyHitBullet extends MCH_Packet {
   public int entityID_Ac = -1;

   public int getMessageID() {
      return 268439602;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.entityID_Ac = data.readInt();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.entityID_Ac);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void send(MCH_EntityAircraft ac, EntityPlayer rider) {
      if (rider != null && !rider.field_70128_L) {
         MCH_PacketNotifyHitBullet s = new MCH_PacketNotifyHitBullet();
         s.entityID_Ac = ac != null && !ac.field_70128_L ? W_Entity.getEntityId(ac) : -1;
         W_Network.sendToPlayer(s, rider);
      }
   }
}
