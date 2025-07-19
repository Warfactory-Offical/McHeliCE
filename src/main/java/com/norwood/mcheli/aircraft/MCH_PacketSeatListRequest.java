package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketSeatListRequest extends MCH_Packet {
    public int entityID_AC = -1;

    public static void requestSeatList(MCH_EntityAircraft ac) {
        MCH_PacketSeatListRequest s = new MCH_PacketSeatListRequest();
        s.entityID_AC = W_Entity.getEntityId(ac);
        W_Network.sendToServer(s);
    }

    @Override
    public int getMessageID() {
        return 536875024;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID_AC = data.readInt();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID_AC);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
