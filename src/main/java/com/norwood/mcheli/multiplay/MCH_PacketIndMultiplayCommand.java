package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.networking.packet.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketIndMultiplayCommand extends MCH_Packet {
    public int CmdID = -1;
    public String CmdStr;

    public static void send(int cmd_id, String str) {
        if (cmd_id > 0) {
            MCH_PacketIndMultiplayCommand s = new MCH_PacketIndMultiplayCommand();
            s.CmdID = cmd_id;
            s.CmdStr = str;
            W_Network.sendToServer(s);
        }
    }

    @Override
    public int getMessageID() {
        return 536873088;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.CmdID = data.readInt();
            this.CmdStr = data.readUTF();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.CmdID);
            dos.writeUTF(this.CmdStr);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
