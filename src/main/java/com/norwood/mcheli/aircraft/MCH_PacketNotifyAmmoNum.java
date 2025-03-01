package com.norwood.mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import com.norwood.mcheli.MCH_Packet;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_PacketNotifyAmmoNum extends MCH_Packet {
   public int entityID_Ac = -1;
   public boolean all = false;
   public byte weaponID = -1;
   public byte num = 0;
   public short[] ammo = new short[0];
   public short[] restAmmo = new short[0];

   public int getMessageID() {
      return 268439604;
   }

   public void readData(ByteArrayDataInput data) {
      try {
         this.entityID_Ac = data.readInt();
         this.all = data.readByte() != 0;
         if (this.all) {
            this.num = data.readByte();
            this.ammo = new short[this.num];
            this.restAmmo = new short[this.num];

            for(int i = 0; i < this.num; ++i) {
               this.ammo[i] = data.readShort();
               this.restAmmo[i] = data.readShort();
            }
         } else {
            this.weaponID = data.readByte();
            this.ammo = new short[]{data.readShort()};
            this.restAmmo = new short[]{data.readShort()};
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void writeData(DataOutputStream dos) {
      try {
         dos.writeInt(this.entityID_Ac);
         dos.writeByte(this.all ? 1 : 0);
         if (this.all) {
            dos.writeByte(this.num);

            for(int i = 0; i < this.num; ++i) {
               dos.writeShort(this.ammo[i]);
               dos.writeShort(this.restAmmo[i]);
            }
         } else {
            dos.writeByte(this.weaponID);
            dos.writeShort(this.ammo[0]);
            dos.writeShort(this.restAmmo[0]);
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void sendAllAmmoNum(MCH_EntityAircraft ac, EntityPlayer target) {
      MCH_PacketNotifyAmmoNum s = new MCH_PacketNotifyAmmoNum();
      s.entityID_Ac = W_Entity.getEntityId(ac);
      s.all = true;
      s.num = (byte)ac.getWeaponNum();
      s.ammo = new short[s.num];
      s.restAmmo = new short[s.num];

      for(int i = 0; i < s.num; ++i) {
         s.ammo[i] = (short)ac.getWeapon(i).getAmmoNum();
         s.restAmmo[i] = (short)ac.getWeapon(i).getRestAllAmmoNum();
      }

      send(s, ac, target);
   }

   public static void sendAmmoNum(MCH_EntityAircraft ac, EntityPlayer target, int wid) {
      sendAmmoNum(ac, target, wid, ac.getWeapon(wid).getAmmoNum(), ac.getWeapon(wid).getRestAllAmmoNum());
   }

   public static void sendAmmoNum(MCH_EntityAircraft ac, EntityPlayer target, int wid, int ammo, int rest_ammo) {
      MCH_PacketNotifyAmmoNum s = new MCH_PacketNotifyAmmoNum();
      s.entityID_Ac = W_Entity.getEntityId(ac);
      s.all = false;
      s.weaponID = (byte)wid;
      s.ammo = new short[]{(short)ammo};
      s.restAmmo = new short[]{(short)rest_ammo};
      send(s, ac, target);
   }

   public static void send(MCH_PacketNotifyAmmoNum s, MCH_EntityAircraft ac, EntityPlayer target) {
      if (target == null) {
         for(int i = 0; i < ac.getSeatNum() + 1; ++i) {
            Entity e = ac.getEntityBySeatId(i);
            if (e instanceof EntityPlayer) {
               W_Network.sendToPlayer(s, (EntityPlayer)e);
            }
         }
      } else {
         W_Network.sendToPlayer(s, target);
      }

   }
}
