package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketSeatPlayerControl extends MCH_Packet {
    public boolean isUnmount = false;
    public byte switchSeat = 0;
    public boolean parachuting;

    @Override
    public int getMessageID() {
        return 536875040;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            byte bf = data.readByte();
            this.isUnmount = (bf >> 3 & 1) != 0;
            this.switchSeat = (byte) (bf >> 1 & 3);
            this.parachuting = (bf >> 0 & 1) != 0;
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            byte bf = (byte) ((this.isUnmount ? 8 : 0) | this.switchSeat << 1 | (this.parachuting ? 1 : 0));
            dos.writeByte(bf);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
