package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketStatusResponse extends MCH_Packet {
    public int entityID_AC = -1;
    public byte seatNum = -1;
    public byte[] weaponIDs = new byte[]{-1};

    public static void sendStatus(MCH_EntityAircraft ac, EntityPlayer player) {
        MCH_PacketStatusResponse s = new MCH_PacketStatusResponse();
        s.setParameter(ac);
        W_Network.sendToPlayer(s, player);
    }

    @Override
    public int getMessageID() {
        return 268439649;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID_AC = data.readInt();
            this.seatNum = data.readByte();
            if (this.seatNum > 0) {
                this.weaponIDs = new byte[this.seatNum];

                for (int i = 0; i < this.seatNum; i++) {
                    this.weaponIDs[i] = data.readByte();
                }
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID_AC);
            if (this.seatNum > 0 && this.weaponIDs != null && this.weaponIDs.length == this.seatNum) {
                dos.writeByte(this.seatNum);

                for (int i = 0; i < this.seatNum; i++) {
                    dos.writeByte(this.weaponIDs[i]);
                }
            } else {
                dos.writeByte(-1);
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    protected void setParameter(MCH_EntityAircraft ac) {
        if (ac != null) {
            this.entityID_AC = W_Entity.getEntityId(ac);
            this.seatNum = (byte) (ac.getSeatNum() + 1);
            if (this.seatNum > 0) {
                this.weaponIDs = new byte[this.seatNum];

                for (int i = 0; i < this.seatNum; i++) {
                    this.weaponIDs[i] = (byte) ac.getWeaponIDBySeatID(i);
                }
            } else {
                this.weaponIDs = new byte[]{-1};
            }
        }
    }
}
