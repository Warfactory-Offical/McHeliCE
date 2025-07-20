package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataOutputStream;
import java.util.Arrays;

public class MCH_PacketNotifySpotedEntity extends MCH_Packet {
    private static final int MAX_COUNT = 30000;
    private static final int MAX_ENTITIES = 300;

    public int count = 0;
    public int num = 0;
    public int[] entityId = new int[0];

    public static void send(EntityPlayer player, int count, int[] entityIds) {
        if (player == null || entityIds == null || entityIds.length == 0 || count <= 0) return;

        int limitedCount = Math.min(count, MAX_COUNT);
        int limitedNum = Math.min(entityIds.length, MAX_ENTITIES);

        MCH_PacketNotifySpotedEntity pkt = new MCH_PacketNotifySpotedEntity();
        pkt.count = limitedCount;
        pkt.num = limitedNum;
        pkt.entityId = Arrays.copyOf(entityIds, limitedNum);

        W_Network.sendToPlayer(pkt, player);
    }

    @Override
    public int getMessageID() {
        return 268437761;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.count = data.readUnsignedShort(); // safer than signed readShort
            this.num = Math.min(data.readUnsignedShort(), MAX_ENTITIES);

            this.entityId = new int[this.num];
            for (int i = 0; i < this.num; i++) {
                this.entityId[i] = data.readInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.count = 0;
            this.num = 0;
            this.entityId = new int[0];
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeShort(Math.min(this.count, MAX_COUNT));
            dos.writeShort(Math.min(this.num, MAX_ENTITIES));

            for (int i = 0; i < Math.min(this.num, this.entityId.length); i++) {
                dos.writeInt(this.entityId[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

