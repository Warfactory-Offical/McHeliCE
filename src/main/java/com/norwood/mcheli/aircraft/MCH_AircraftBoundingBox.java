package com.norwood.mcheli.aircraft;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class MCH_AircraftBoundingBox extends AxisAlignedBB {
   private final MCH_EntityAircraft ac;

   protected MCH_AircraftBoundingBox(MCH_EntityAircraft ac) {
      this(ac, ac.func_174813_aQ());
   }

   public MCH_AircraftBoundingBox(MCH_EntityAircraft ac, AxisAlignedBB aabb) {
      super(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c, aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f);
      this.ac = ac;
   }

   public AxisAlignedBB NewAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
      return new MCH_AircraftBoundingBox(this.ac, new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
   }

   public double getDistSq(AxisAlignedBB a1, AxisAlignedBB a2) {
      double x1 = (a1.field_72336_d + a1.field_72340_a) / 2.0D;
      double y1 = (a1.field_72337_e + a1.field_72338_b) / 2.0D;
      double z1 = (a1.field_72334_f + a1.field_72339_c) / 2.0D;
      double x2 = (a2.field_72336_d + a2.field_72340_a) / 2.0D;
      double y2 = (a2.field_72337_e + a2.field_72338_b) / 2.0D;
      double z2 = (a2.field_72334_f + a2.field_72339_c) / 2.0D;
      double dx = x1 - x2;
      double dy = y1 - y2;
      double dz = z1 - z2;
      return dx * dx + dy * dy + dz * dz;
   }

   public boolean func_72326_a(AxisAlignedBB aabb) {
      boolean ret = false;
      double dist = 1.0E7D;
      this.ac.lastBBDamageFactor = 1.0F;
      if (super.func_72326_a(aabb)) {
         dist = this.getDistSq(aabb, this);
         ret = true;
      }

      MCH_BoundingBox[] var5 = this.ac.extraBoundingBox;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         MCH_BoundingBox bb = var5[var7];
         if (bb.getBoundingBox().func_72326_a(aabb)) {
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

   public AxisAlignedBB func_72314_b(double x, double y, double z) {
      double d3 = this.field_72340_a - x;
      double d4 = this.field_72338_b - y;
      double d5 = this.field_72339_c - z;
      double d6 = this.field_72336_d + x;
      double d7 = this.field_72337_e + y;
      double d8 = this.field_72334_f + z;
      return this.NewAABB(d3, d4, d5, d6, d7, d8);
   }

   public AxisAlignedBB func_111270_a(AxisAlignedBB other) {
      double d0 = Math.min(this.field_72340_a, other.field_72340_a);
      double d1 = Math.min(this.field_72338_b, other.field_72338_b);
      double d2 = Math.min(this.field_72339_c, other.field_72339_c);
      double d3 = Math.max(this.field_72336_d, other.field_72336_d);
      double d4 = Math.max(this.field_72337_e, other.field_72337_e);
      double d5 = Math.max(this.field_72334_f, other.field_72334_f);
      return this.NewAABB(d0, d1, d2, d3, d4, d5);
   }

   public AxisAlignedBB func_72321_a(double x, double y, double z) {
      double d3 = this.field_72340_a;
      double d4 = this.field_72338_b;
      double d5 = this.field_72339_c;
      double d6 = this.field_72336_d;
      double d7 = this.field_72337_e;
      double d8 = this.field_72334_f;
      if (x < 0.0D) {
         d3 += x;
      }

      if (x > 0.0D) {
         d6 += x;
      }

      if (y < 0.0D) {
         d4 += y;
      }

      if (y > 0.0D) {
         d7 += y;
      }

      if (z < 0.0D) {
         d5 += z;
      }

      if (z > 0.0D) {
         d8 += z;
      }

      return this.NewAABB(d3, d4, d5, d6, d7, d8);
   }

   public AxisAlignedBB func_191195_a(double x, double y, double z) {
      double d3 = this.field_72340_a + x;
      double d4 = this.field_72338_b + y;
      double d5 = this.field_72339_c + z;
      double d6 = this.field_72336_d - x;
      double d7 = this.field_72337_e - y;
      double d8 = this.field_72334_f - z;
      return this.NewAABB(d3, d4, d5, d6, d7, d8);
   }

   public AxisAlignedBB copy() {
      return this.NewAABB(this.field_72340_a, this.field_72338_b, this.field_72339_c, this.field_72336_d, this.field_72337_e, this.field_72334_f);
   }

   public AxisAlignedBB getOffsetBoundingBox(double x, double y, double z) {
      return this.NewAABB(this.field_72340_a + x, this.field_72338_b + y, this.field_72339_c + z, this.field_72336_d + x, this.field_72337_e + y, this.field_72334_f + z);
   }

   public RayTraceResult func_72327_a(Vec3d v1, Vec3d v2) {
      this.ac.lastBBDamageFactor = 1.0F;
      RayTraceResult mop = super.func_72327_a(v1, v2);
      double dist = 1.0E7D;
      if (mop != null) {
         dist = v1.func_72438_d(mop.field_72307_f);
      }

      MCH_BoundingBox[] var6 = this.ac.extraBoundingBox;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         MCH_BoundingBox bb = var6[var8];
         RayTraceResult mop2 = bb.getBoundingBox().func_72327_a(v1, v2);
         if (mop2 != null) {
            double dist2 = v1.func_72438_d(mop2.field_72307_f);
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
