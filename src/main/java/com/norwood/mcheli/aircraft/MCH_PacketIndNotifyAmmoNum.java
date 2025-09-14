package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

//TODO marker
public class MCH_PacketIndNotifyAmmoNum extends MCH_Packet {
    public int entityID_Ac = -1;
    public byte weaponID = -1;

    public static void send(MCH_EntityAircraft ac, int wid) {
        MCH_PacketIndNotifyAmmoNum s = new MCH_PacketIndNotifyAmmoNum();
        s.entityID_Ac = W_Entity.getEntityId(ac);
        s.weaponID = (byte) wid;
        W_Network.sendToServer(s);
    }

    @Override
    public int getMessageID() {
        return 536875061;
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
