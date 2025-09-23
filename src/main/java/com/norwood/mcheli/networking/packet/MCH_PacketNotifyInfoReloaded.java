package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketNotifyInfoReloaded extends MCH_Packet {
    public int type = -1;

    public static void sendRealodAc() {
        MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
        s.type = 0;
        W_Network.sendToServer(s);
    }

    public static void sendRealodAllWeapon() {
        MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
        s.type = 1;
        W_Network.sendToServer(s);
    }

    @Override
    public int getMessageID() {
        return 536875063;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.type = data.readInt();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.type);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
