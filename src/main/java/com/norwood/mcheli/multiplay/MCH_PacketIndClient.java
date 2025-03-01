package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_PacketIndClient extends MCH_Packet {
   public int CmdID = -1;
   public String CmdStr;

   public int getMessageID() {
      return 268438032;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.CmdID = data.readInt();
         this.CmdStr = data.readUTF();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.CmdID);
         dos.writeUTF(this.CmdStr);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void send(EntityPlayer player, int cmd_id, String str) {
      if (cmd_id > 0) {
         MCH_PacketIndClient s = new MCH_PacketIndClient();
         s.CmdID = cmd_id;
         s.CmdStr = str;
         W_Network.sendToPlayer(s, player);
      }
   }
}
