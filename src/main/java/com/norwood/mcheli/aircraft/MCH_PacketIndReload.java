package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketIndReload extends MCH_Packet {
    public int entityID_Ac = -1;
    public int weaponID = -1;

    public static void send(MCH_EntityAircraft ac, int weaponId) {
        if (ac != null) {
            MCH_PacketIndReload s = new MCH_PacketIndReload();
            s.entityID_Ac = W_Entity.getEntityId(ac);
            s.weaponID = weaponId;
            W_Network.sendToServer(s);
        }
    }

    @Override
    public int getMessageID() {
        return 536875059;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID_Ac = data.readInt();
            this.weaponID = data.readByte();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID_Ac);
            dos.writeByte(this.weaponID);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
