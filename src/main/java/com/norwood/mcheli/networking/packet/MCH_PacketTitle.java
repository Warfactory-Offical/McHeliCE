package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketTitle extends MCH_Packet {
    public ITextComponent chatComponent = null;
    public int showTime = 1;
    public int position = 0;

    public static void send(ITextComponent chat, int showTime, int pos) {
        MCH_PacketTitle s = new MCH_PacketTitle();
        s.chatComponent = chat;
        s.showTime = showTime;
        s.position = pos;
        W_Network.sendToAllPlayers(s);
    }

    @Override
    public int getMessageID() {
        return 268438272;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.chatComponent = Serializer.jsonToComponent(data.readUTF());
            this.showTime = data.readShort();
            this.position = data.readShort();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeUTF(Serializer.componentToJson(this.chatComponent));
            dos.writeShort(this.showTime);
            dos.writeShort(this.position);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
