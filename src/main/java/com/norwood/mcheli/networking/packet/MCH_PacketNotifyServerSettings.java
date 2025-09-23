package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.helper.MCH_Utils;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketNotifyServerSettings extends MCH_Packet {
    public boolean enableCamDistChange = true;
    public boolean enableEntityMarker = true;
    public boolean enablePVP = true;
    public double stingerLockRange = 120.0;
    public boolean enableDebugBoundingBox = true;
    public boolean enableRotationLimit = false;
    public byte pitchLimitMax = 10;
    public byte pitchLimitMin = 10;
    public byte rollLimit = 35;

    public static void send(@Nullable EntityPlayerMP player) {
        MCH_PacketNotifyServerSettings s = new MCH_PacketNotifyServerSettings();
        s.enableCamDistChange = !MCH_Config.DisableCameraDistChange.prmBool;
        s.enableEntityMarker = MCH_Config.DisplayEntityMarker.prmBool;
        s.enablePVP = MCH_Utils.getServer().isPVPEnabled();
        s.stingerLockRange = MCH_Config.StingerLockRange.prmDouble;
        s.enableDebugBoundingBox = MCH_Config.EnableDebugBoundingBox.prmBool;
        s.enableRotationLimit = MCH_Config.EnableRotationLimit.prmBool;
        s.pitchLimitMax = (byte) MCH_Config.PitchLimitMax.prmInt;
        s.pitchLimitMin = (byte) MCH_Config.PitchLimitMin.prmInt;
        s.rollLimit = (byte) MCH_Config.RollLimit.prmInt;
        if (player != null) {
            W_Network.sendToPlayer(s, player);
        } else {
            W_Network.sendToAllPlayers(s);
        }
    }

    public static void sendAll() {
        send(null);
    }

    @Override
    public int getMessageID() {
        return 268437568;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            byte b = data.readByte();
            this.enableCamDistChange = this.getBit(b, 0);
            this.enableEntityMarker = this.getBit(b, 1);
            this.enablePVP = this.getBit(b, 2);
            this.enableDebugBoundingBox = this.getBit(b, 3);
            this.enableRotationLimit = this.getBit(b, 4);
            this.stingerLockRange = data.readFloat();
            this.pitchLimitMax = data.readByte();
            this.pitchLimitMin = data.readByte();
            this.rollLimit = data.readByte();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            byte b = 0;
            b = this.setBit(b, 0, this.enableCamDistChange);
            b = this.setBit(b, 1, this.enableEntityMarker);
            b = this.setBit(b, 2, this.enablePVP);
            b = this.setBit(b, 3, this.enableDebugBoundingBox);
            b = this.setBit(b, 4, this.enableRotationLimit);
            dos.writeByte(b);
            dos.writeFloat((float) this.stingerLockRange);
            dos.writeByte(this.pitchLimitMax);
            dos.writeByte(this.pitchLimitMin);
            dos.writeByte(this.rollLimit);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
