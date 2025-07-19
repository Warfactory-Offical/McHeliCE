package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MCH_PacketModList extends MCH_Packet {
    public boolean firstData = false;
    public int id = 0;
    public int num = 0;
    public final List<String> list = new ArrayList<>();

    public static void send(EntityPlayer player, MCH_PacketModList p) {
        W_Network.sendToPlayer(p, player);
    }

    public static void send(List<String> list, int id) {
        MCH_PacketModList p = null;
        int size = 0;
        boolean isFirst = true;

        for (String s : list) {
            if (p == null) {
                p = new MCH_PacketModList();
                p.id = id;
                p.firstData = isFirst;
                isFirst = false;
            }

            p.list.add(s);
            size += s.length() + 2;
            if (size > 1024) {
                p.num = p.list.size();
                W_Network.sendToServer(p);
                p = null;
                size = 0;
            }
        }

        if (p != null) {
            p.num = p.list.size();
            W_Network.sendToServer(p);
        }
    }

    @Override
    public int getMessageID() {
        return 536873473;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.firstData = data.readByte() == 1;
            this.id = data.readInt();
            this.num = data.readInt();

            for (int i = 0; i < this.num; i++) {
                this.list.add(data.readUTF());
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeByte(this.firstData ? 1 : 0);
            dos.writeInt(this.id);
            dos.writeInt(this.num);

            for (String s : this.list) {
                dos.writeUTF(s);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
