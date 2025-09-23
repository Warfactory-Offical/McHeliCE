package com.norwood.mcheli.plane;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.networking.packet.MCH_PacketPlayerControlBase;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCP_PlanePacketPlayerControl extends MCH_PacketPlayerControlBase {
    public byte switchVtol = -1;

    @Override
    public int getMessageID() {
        return 536903696;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        super.readData(data);

        try {
            this.switchVtol = data.readByte();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        super.writeData(dos);

        try {
            dos.writeByte(this.switchVtol);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
