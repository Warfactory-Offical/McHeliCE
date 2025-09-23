package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.Entity;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketNotifyWeaponID extends MCH_Packet {
    public int entityID_Ac = -1;
    public int seatID = -1;
    public int weaponID = -1;
    public short ammo = 0;
    public short restAmmo = 0;

    public static void send(Entity sender, int sid, int wid, int ammo, int rest_ammo) {
        MCH_PacketNotifyWeaponID s = new MCH_PacketNotifyWeaponID();
        s.entityID_Ac = W_Entity.getEntityId(sender);
        s.seatID = sid;
        s.weaponID = wid;
        s.ammo = (short) ammo;
        s.restAmmo = (short) rest_ammo;
        W_Network.sendToAllAround(s, sender, 150.0);
    }

    @Override
    public int getMessageID() {
        return 268439601;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID_Ac = data.readInt();
            this.seatID = data.readByte();
            this.weaponID = data.readByte();
            this.ammo = data.readShort();
            this.restAmmo = data.readShort();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID_Ac);
            dos.writeByte(this.seatID);
            dos.writeByte(this.weaponID);
            dos.writeShort(this.ammo);
            dos.writeShort(this.restAmmo);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
