package com.norwood.mcheli.aircraft;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class MCH_AircraftBoundingBox extends AxisAlignedBB {
   private final MCH_EntityAircraft ac;

   protected MCH_AircraftBoundingBox(MCH_EntityAircraft ac) {
      this(ac, ac.getEntityBoundingBox());
   }

   public MCH_AircraftBoundingBox(MCH_EntityAircraft ac, AxisAlignedBB aabb) {
      super(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
      this.ac = ac;
   }

   public AxisAlignedBB NewAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
      return new MCH_AircraftBoundingBox(this.ac, new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
   }

   public double getDistSq(AxisAlignedBB a1, AxisAlignedBB a2) {
      double x1 = (a1.maxX + a1.minX) / 2.0;
      double y1 = (a1.maxY + a1.minY) / 2.0;
      double z1 = (a1.maxZ + a1.minZ) / 2.0;
      double x2 = (a2.maxX + a2.minX) / 2.0;
      double y2 = (a2.maxY + a2.minY) / 2.0;
      double z2 = (a2.maxZ + a2.minZ) / 2.0;
      double dx = x1 - x2;
      double dy = y1 - y2;
      double dz = z1 - z2;
      return dx * dx + dy * dy + dz * dz;
   }

   public boolean intersects(AxisAlignedBB aabb) {
      boolean ret = false;
      double dist = 1.0E7;
      this.ac.lastBBDamageFactor = 1.0F;
      if (super.intersects(aabb)) {
         dist = this.getDistSq(aabb, this);
         ret = true;
      }

      for (MCH_BoundingBox bb : this.ac.extraBoundingBox) {
         if (bb.getBoundingBox().intersects(aabb)) {
            double dist2 = this.getDistSq(aabb, this);
            if (dist2 < dist) {
               dist = dist2;
               this.ac.lastBBDamageFactor = bb.damegeFactor;
            }

            ret = true;
         }
      }

      return ret;
   }

   public AxisAlignedBB grow(double x, double y, double z) {
      double d3 = this.minX - x;
      double d4 = this.minY - y;
      double d5 = this.minZ - z;
      double d6 = this.maxX + x;
      double d7 = this.maxY + y;
      double d8 = this.maxZ + z;
      return this.NewAABB(d3, d4, d5, d6, d7, d8);
   }

   public AxisAlignedBB union(AxisAlignedBB other) {
      double d0 = Math.min(this.minX, other.minX);
      double d1 = Math.min(this.minY, other.minY);
      double d2 = Math.min(this.minZ, other.minZ);
      double d3 = Math.max(this.maxX, other.maxX);
      double d4 = Math.max(this.maxY, other.maxY);
      double d5 = Math.max(this.maxZ, other.maxZ);
      return this.NewAABB(d0, d1, d2, d3, d4, d5);
   }

   public AxisAlignedBB expand(double x, double y, double z) {
      double d3 = this.minX;
      double d4 = this.minY;
      double d5 = this.minZ;
      double d6 = this.maxX;
      double d7 = this.maxY;
      double d8 = this.maxZ;
      if (x < 0.0) {
         d3 += x;
      }

      if (x > 0.0) {
         d6 += x;
      }

      if (y < 0.0) {
         d4 += y;
      }

      if (y > 0.0) {
         d7 += y;
      }

      if (z < 0.0) {
         d5 += z;
      }

      if (z > 0.0) {
         d8 += z;
      }

      return this.NewAABB(d3, d4, d5, d6, d7, d8);
   }

   public AxisAlignedBB contract(double x, double y, double z) {
      double d3 = this.minX + x;
      double d4 = this.minY + y;
      double d5 = this.minZ + z;
      double d6 = this.maxX - x;
      double d7 = this.maxY - y;
      double d8 = this.maxZ - z;
      return this.NewAABB(d3, d4, d5, d6, d7, d8);
   }

   public AxisAlignedBB copy() {
      return this.NewAABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
   }

   public AxisAlignedBB getOffsetBoundingBox(double x, double y, double z) {
      return this.NewAABB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
   }

   public RayTraceResult calculateIntercept(Vec3d v1, Vec3d v2) {
      this.ac.lastBBDamageFactor = 1.0F;
      RayTraceResult mop = super.calculateIntercept(v1, v2);
      double dist = 1.0E7;
      if (mop != null) {
         dist = v1.distanceTo(mop.hitVec);
      }

      for (MCH_BoundingBox bb : this.ac.extraBoundingBox) {
         RayTraceResult mop2 = bb.getBoundingBox().calculateIntercept(v1, v2);
         if (mop2 != null) {
            double dist2 = v1.distanceTo(mop2.hitVec);
            if (dist2 < dist) {
               mop = mop2;
               dist = dist2;
               this.ac.lastBBDamageFactor = bb.damegeFactor;
            }
         }
      }

      return mop;
   }
}
