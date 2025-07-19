package com.norwood.mcheli.command;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketCommandSave extends MCH_Packet {
    public String str = "";

    public static void send(String cmd) {
        MCH_PacketCommandSave s = new MCH_PacketCommandSave();
        s.str = cmd;
        W_Network.sendToServer(s);
    }

    @Override
    public int getMessageID() {
        return 536873729;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.str = data.readUTF();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeUTF(this.str);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
