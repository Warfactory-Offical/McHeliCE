package com.norwood.mcheli.aircraft;

import java.util.ArrayList;
import java.util.List;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class MCH_BoundingBox {
   private AxisAlignedBB boundingBox;
   public final double offsetX;
   public final double offsetY;
   public final double offsetZ;
   public final float width;
   public final float height;
   public Vec3d rotatedOffset;
   public List<Vec3d> pos = new ArrayList();
   public final float damegeFactor;

   public MCH_BoundingBox(double x, double y, double z, float w, float h, float df) {
      this.offsetX = x;
      this.offsetY = y;
      this.offsetZ = z;
      this.width = w;
      this.height = h;
      this.damegeFactor = df;
      this.boundingBox = new AxisAlignedBB(x - (double)(w / 2.0F), y - (double)(h / 2.0F), z - (double)(w / 2.0F), x + (double)(w / 2.0F), y + (double)(h / 2.0F), z + (double)(w / 2.0F));
      this.updatePosition(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
   }

   public void add(double x, double y, double z) {
      this.pos.add(0, new Vec3d(x, y, z));

      while(this.pos.size() > MCH_Config.HitBoxDelayTick.prmInt + 2) {
         this.pos.remove(MCH_Config.HitBoxDelayTick.prmInt + 2);
      }

   }

   public MCH_BoundingBox copy() {
      return new MCH_BoundingBox(this.offsetX, this.offsetY, this.offsetZ, this.width, this.height, this.damegeFactor);
   }

   public void updatePosition(double posX, double posY, double posZ, float yaw, float pitch, float roll) {
      Vec3d v = new Vec3d(this.offsetX, this.offsetY, this.offsetZ);
      this.rotatedOffset = MCH_Lib.RotVec3(v, -yaw, -pitch, -roll);
      this.add(posX + this.rotatedOffset.field_72450_a, posY + this.rotatedOffset.field_72448_b, posZ + this.rotatedOffset.field_72449_c);
      int index = MCH_Config.HitBoxDelayTick.prmInt;
      Vec3d cp = index + 0 < this.pos.size() ? (Vec3d)this.pos.get(index + 0) : (Vec3d)this.pos.get(this.pos.size() - 1);
      Vec3d pp = index + 1 < this.pos.size() ? (Vec3d)this.pos.get(index + 1) : (Vec3d)this.pos.get(this.pos.size() - 1);
      double sx = ((double)this.width + Math.abs(cp.field_72450_a - pp.field_72450_a)) / 2.0D;
      double sy = ((double)this.height + Math.abs(cp.field_72448_b - pp.field_72448_b)) / 2.0D;
      double sz = ((double)this.width + Math.abs(cp.field_72449_c - pp.field_72449_c)) / 2.0D;
      double x = (cp.field_72450_a + pp.field_72450_a) / 2.0D;
      double y = (cp.field_72448_b + pp.field_72448_b) / 2.0D;
      double z = (cp.field_72449_c + pp.field_72449_c) / 2.0D;
      this.boundingBox = new AxisAlignedBB(x - sx, y - sy, z - sz, x + sx, y + sy, z + sz);
   }

   public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
   }
}
