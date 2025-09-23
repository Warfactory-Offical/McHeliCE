package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketStatusRequest extends MCH_Packet {
    public int entityID_AC = -1;

    public static void requestStatus(MCH_EntityAircraft ac) {
        if (ac.world.isRemote) {
            MCH_PacketStatusRequest s = new MCH_PacketStatusRequest();
            s.entityID_AC = W_Entity.getEntityId(ac);
            W_Network.sendToServer(s);
        }
    }

    @Override
    public int getMessageID() {
        return 536875104;
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
