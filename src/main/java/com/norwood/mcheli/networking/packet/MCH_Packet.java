package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.wrapper.W_PacketBase;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class MCH_Packet extends W_PacketBase {


    public MCH_Packet() {
    }

    public MCH_Packet(ByteArrayDataInput data) {
        this.readData(data);
    }

    public byte setBit(byte data, int bit, boolean b) {
        return (byte) (data | (b ? 1 : 0) << bit);
    }

    public short setBit(short data, int bit, boolean b) {
        return (short) (data | (b ? 1 : 0) << bit);
    }

    public boolean getBit(byte data, int bit) {
        return (data >> bit & 1) != 0;
    }

    public boolean getBit(short data, int bit) {
        return (data >> bit & 1) != 0;
    }

    public abstract void readData(ByteArrayDataInput var1);

    public abstract void writeData(DataOutputStream var1);

    public abstract int getMessageID();

    @Override
    public byte[] createData() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(data);

        try {
            dos.writeInt(this.getMessageID());
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        this.writeData(dos);
        return data.toByteArray();
    }
}
