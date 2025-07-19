package com.norwood.mcheli;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import com.norwood.mcheli.wrapper.W_Network;
import net.minecraft.util.math.BlockPos;

public class MCH_PacketEffectExplosion extends MCH_Packet {
   MCH_PacketEffectExplosion.ExplosionParam prm = new MCH_PacketEffectExplosion.ExplosionParam(this);

   @Override
   public int getMessageID() {
      return 268437520;
   }

   @Override
   public void readData(ByteArrayDataInput data) {
      try {
         this.prm.posX = data.readDouble();
         this.prm.posY = data.readDouble();
         this.prm.posZ = data.readDouble();
         this.prm.size = data.readFloat();
         this.prm.exploderID = data.readInt();
         this.prm.inWater = data.readByte() != 0;
         this.prm.readAffectedPositions(data);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public void writeData(DataOutputStream dos) {
      try {
         dos.writeDouble(this.prm.posX);
         dos.writeDouble(this.prm.posY);
         dos.writeDouble(this.prm.posZ);
         dos.writeFloat(this.prm.size);
         dos.writeInt(this.prm.exploderID);
         dos.writeByte(this.prm.inWater ? 1 : 0);
         this.prm.writeAffectedPositions(dos);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public static MCH_PacketEffectExplosion.ExplosionParam create() {
      return new MCH_PacketEffectExplosion().aaa();
   }

   private MCH_PacketEffectExplosion.ExplosionParam aaa() {
      return new MCH_PacketEffectExplosion.ExplosionParam(this);
   }

   public static void send(MCH_PacketEffectExplosion.ExplosionParam param) {
      if (param != null) {
         MCH_PacketEffectExplosion s = new MCH_PacketEffectExplosion();
         s.prm = param;
         W_Network.sendToAllPlayers(s);
      }
   }

   public class ExplosionParam {
      public double posX;
      public double posY;
      public double posZ;
      public float size;
      public int exploderID;
      public boolean inWater;
      private List<BlockPos> affectedPositions;

      public ExplosionParam(MCH_PacketEffectExplosion paramMCH_PacketEffectExplosion) {
      }

      public void setAffectedPositions(List<BlockPos> affectedPositions) {
         this.affectedPositions = Lists.newArrayList(affectedPositions);
      }

      public List<BlockPos> getAffectedBlockPositions() {
         return this.affectedPositions;
      }

      void writeAffectedPositions(DataOutputStream dos) throws IOException {
         dos.writeInt(this.affectedPositions.size());

         for (BlockPos blockpos : this.affectedPositions) {
            dos.writeInt(blockpos.getX());
            dos.writeInt(blockpos.getY());
            dos.writeInt(blockpos.getZ());
         }
      }

      void readAffectedPositions(ByteArrayDataInput data) {
         int i = data.readInt();
         this.affectedPositions = Lists.newArrayListWithCapacity(i);

         for (int i1 = 0; i1 < i; i1++) {
            int j1 = data.readInt();
            int k1 = data.readInt();
            int l1 = data.readInt();
            this.affectedPositions.add(new BlockPos(j1, k1, l1));
         }
      }
   }
}
