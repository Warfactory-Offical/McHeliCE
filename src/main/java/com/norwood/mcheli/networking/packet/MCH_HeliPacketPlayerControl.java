package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_HeliPacketPlayerControl extends MCH_PacketPlayerControlBase {
    public byte switchFold = -1;
    public int unhitchChainId = -1;

    @Override
    public int getMessageID() {
        return 536879120;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        super.readData(data);

        try {
            this.switchFold = data.readByte();
            this.unhitchChainId = data.readInt();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        super.writeData(dos);

        try {
            dos.writeByte(this.switchFold);
            dos.writeInt(this.unhitchChainId);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
