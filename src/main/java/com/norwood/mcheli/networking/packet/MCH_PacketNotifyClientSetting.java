package com.norwood.mcheli.networking.packet;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_EntityRenderer;
import com.norwood.mcheli.wrapper.W_Network;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCH_PacketNotifyClientSetting extends MCH_Packet {
    public boolean dismountAll = true;
    public boolean heliAutoThrottleDown;
    public boolean planeAutoThrottleDown;
    public boolean tankAutoThrottleDown;
    public boolean shaderSupport = false;

    public static void send() {
        MCH_PacketNotifyClientSetting s = new MCH_PacketNotifyClientSetting();
        s.dismountAll = MCH_Config.DismountAll.prmBool;
        s.heliAutoThrottleDown = MCH_Config.AutoThrottleDownHeli.prmBool;
        s.planeAutoThrottleDown = MCH_Config.AutoThrottleDownPlane.prmBool;
        s.tankAutoThrottleDown = MCH_Config.AutoThrottleDownTank.prmBool;
        s.shaderSupport = W_EntityRenderer.isShaderSupport();
        W_Network.sendToServer(s);
    }

    @Override
    public int getMessageID() {
        return 536875072;
    }

    @Override
    public void readData(ByteArrayDataInput di) {
        try {
            byte data;
            data = di.readByte();
            this.dismountAll = this.getBit(data, 0);
            this.heliAutoThrottleDown = this.getBit(data, 1);
            this.planeAutoThrottleDown = this.getBit(data, 2);
            this.tankAutoThrottleDown = this.getBit(data, 3);
            this.shaderSupport = this.getBit(data, 4);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            byte data = 0;
            data = this.setBit(data, 0, this.dismountAll);
            data = this.setBit(data, 1, this.heliAutoThrottleDown);
            data = this.setBit(data, 2, this.planeAutoThrottleDown);
            data = this.setBit(data, 3, this.tankAutoThrottleDown);
            data = this.setBit(data, 4, this.shaderSupport);
            dos.writeByte(data);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}
