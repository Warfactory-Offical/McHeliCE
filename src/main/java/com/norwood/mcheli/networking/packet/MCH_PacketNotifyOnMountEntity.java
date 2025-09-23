package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketNotifyOnMountEntity extends MCH_Packet {
    public int entityID_Ac = -1;
    public int entityID_rider = -1;
    public int seatID = -1;

    public static void send(MCH_EntityAircraft ac, Entity rider, int seatId) {
        if (ac != null && rider != null) {
            Entity pilot = ac.getRiddenByEntity();
            if (pilot instanceof EntityPlayer && !pilot.isDead) {
                MCH_PacketNotifyOnMountEntity s = new MCH_PacketNotifyOnMountEntity();
                s.entityID_Ac = W_Entity.getEntityId(ac);
                s.entityID_rider = W_Entity.getEntityId(rider);
                s.seatID = seatId;
                W_Network.sendToPlayer(s, (EntityPlayer) pilot);
            }
        }
    }

    @Override
    public int getMessageID() {
        return 268439632;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID_Ac = data.readInt();
            this.entityID_rider = data.readInt();
            this.seatID = data.readByte();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID_Ac);
            dos.writeInt(this.entityID_rider);
            dos.writeByte(this.seatID);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
