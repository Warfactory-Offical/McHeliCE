package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketNotifyLock extends MCH_Packet {
    public int entityID = -1;

    public static void send(Entity target) {
        if (target != null) {
            MCH_PacketNotifyLock s = new MCH_PacketNotifyLock();
            s.entityID = target.getEntityId();
            W_Network.sendToServer(s);
        }
    }

    public static void sendToPlayer(EntityPlayer entity) {
        if (entity instanceof EntityPlayerMP) {
            MCH_PacketNotifyLock s = new MCH_PacketNotifyLock();
            W_Network.sendToPlayer(s, entity);
        }
    }

    @Override
    public int getMessageID() {
        return 536873984;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID = data.readInt();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
