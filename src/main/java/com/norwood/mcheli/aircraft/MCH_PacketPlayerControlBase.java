package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;

public abstract class MCH_PacketPlayerControlBase extends MCH_Packet {
   public byte isUnmount = 0;
   public byte switchMode = -1;
   public byte switchCameraMode = 0;
   public byte switchWeapon = -1;
   public byte useFlareType = 0;
   public boolean useWeapon = false;
   public int useWeaponOption1 = 0;
   public int useWeaponOption2 = 0;
   public double useWeaponPosX = 0.0D;
   public double useWeaponPosY = 0.0D;
   public double useWeaponPosZ = 0.0D;
   public boolean throttleUp = false;
   public boolean throttleDown = false;
   public boolean moveLeft = false;
   public boolean moveRight = false;
   public boolean openGui;
   public byte switchHatch = 0;
   public byte switchFreeLook = 0;
   public byte switchGear = 0;
   public boolean ejectSeat = false;
   public byte putDownRack = 0;
   public boolean switchSearchLight = false;
   public boolean useBrake = false;
   public boolean switchGunnerStatus = false;

   public void readData(ByteArrayDataInput data) {
      try {
         short bf = data.readShort();
         this.useWeapon = this.getBit(bf, 0);
         this.throttleUp = this.getBit(bf, 1);
         this.throttleDown = this.getBit(bf, 2);
         this.moveLeft = this.getBit(bf, 3);
         this.moveRight = this.getBit(bf, 4);
         this.switchSearchLight = this.getBit(bf, 5);
         this.ejectSeat = this.getBit(bf, 6);
         this.openGui = this.getBit(bf, 7);
         this.useBrake = this.getBit(bf, 8);
         this.switchGunnerStatus = this.getBit(bf, 9);
         bf = (short)data.readByte();
         this.putDownRack = (byte)(bf >> 6 & 3);
         this.isUnmount = (byte)(bf >> 4 & 3);
         this.useFlareType = (byte)(bf >> 0 & 15);
         this.switchMode = data.readByte();
         this.switchWeapon = data.readByte();
         if (this.useWeapon) {
            this.useWeaponOption1 = data.readInt();
            this.useWeaponOption2 = data.readInt();
            this.useWeaponPosX = data.readDouble();
            this.useWeaponPosY = data.readDouble();
            this.useWeaponPosZ = data.readDouble();
         }

         bf = (short)data.readByte();
         this.switchCameraMode = (byte)(bf >> 6 & 3);
         this.switchHatch = (byte)(bf >> 4 & 3);
         this.switchFreeLook = (byte)(bf >> 2 & 3);
         this.switchGear = (byte)(bf >> 0 & 3);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         short bf = 0;
         short bf = this.setBit(bf, 0, this.useWeapon);
         bf = this.setBit(bf, 1, this.throttleUp);
         bf = this.setBit(bf, 2, this.throttleDown);
         bf = this.setBit(bf, 3, this.moveLeft);
         bf = this.setBit(bf, 4, this.moveRight);
         bf = this.setBit(bf, 5, this.switchSearchLight);
         bf = this.setBit(bf, 6, this.ejectSeat);
         bf = this.setBit(bf, 7, this.openGui);
         bf = this.setBit(bf, 8, this.useBrake);
         bf = this.setBit(bf, 9, this.switchGunnerStatus);
         dos.writeShort(bf);
         bf = (short)((byte)((this.putDownRack & 3) << 6 | (this.isUnmount & 3) << 4 | this.useFlareType & 15));
         dos.writeByte(bf);
         dos.writeByte(this.switchMode);
         dos.writeByte(this.switchWeapon);
         if (this.useWeapon) {
            dos.writeInt(this.useWeaponOption1);
            dos.writeInt(this.useWeaponOption2);
            dos.writeDouble(this.useWeaponPosX);
            dos.writeDouble(this.useWeaponPosY);
            dos.writeDouble(this.useWeaponPosZ);
         }

         bf = (short)((byte)((this.switchCameraMode & 3) << 6 | (this.switchHatch & 3) << 4 | (this.switchFreeLook & 3) << 2 | (this.switchGear & 3) << 0));
         dos.writeByte(bf);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }
}
