package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_PacketSeatListResponse extends MCH_Packet {
   public int entityID_AC = -1;
   public byte seatNum = -1;
   public int[] seatEntityID = new int[]{-1};

   public int getMessageID() {
      return 268439569;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.entityID_AC = data.readInt();
         this.seatNum = data.readByte();
         if (this.seatNum > 0) {
            this.seatEntityID = new int[this.seatNum];

            for(int i = 0; i < this.seatNum; ++i) {
               this.seatEntityID[i] = data.readInt();
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.entityID_AC);
         if (this.seatNum > 0 && this.seatEntityID != null && this.seatEntityID.length == this.seatNum) {
            dos.writeByte(this.seatNum);

            for(int i = 0; i < this.seatNum; ++i) {
               dos.writeInt(this.seatEntityID[i]);
            }
         } else {
            dos.writeByte(-1);
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void sendSeatList(MCH_EntityAircraft ac, EntityPlayer player) {
      MCH_PacketSeatListResponse s = new MCH_PacketSeatListResponse();
      s.setParameter(ac);
      W_Network.sendToPlayer(s, player);
   }

   protected void setParameter(MCH_EntityAircraft ac) {
      if (ac != null) {
         this.entityID_AC = W_Entity.getEntityId(ac);
         this.seatNum = (byte)ac.getSeats().length;
         if (this.seatNum > 0) {
            this.seatEntityID = new int[this.seatNum];

            for(int i = 0; i < this.seatNum; ++i) {
               this.seatEntityID[i] = W_Entity.getEntityId(ac.getSeat(i));
            }
         } else {
            this.seatEntityID = new int[]{-1};
         }

      }
   }
}
