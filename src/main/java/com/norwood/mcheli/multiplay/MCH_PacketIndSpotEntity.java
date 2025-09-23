package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.networking.packet.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.EntityLivingBase;

import java.io.DataOutputStream;

public class MCH_PacketIndSpotEntity extends MCH_Packet {
    public int targetFilter = -1;

    public static void send(EntityLivingBase spoter, int targetFilter) {
        MCH_PacketIndSpotEntity s = new MCH_PacketIndSpotEntity();
        s.targetFilter = targetFilter;
        W_Network.sendToServer(s);
    }

    @Override
    public int getMessageID() {
        return 536873216;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.targetFilter = data.readInt();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.targetFilter);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }
}
