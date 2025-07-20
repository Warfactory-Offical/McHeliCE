package com.norwood.mcheli.aircraft;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

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

    public double getDistanceSquareBetween  (AxisAlignedBB box1, AxisAlignedBB box2) {
        double centerX1 = (box1.minX + box1.maxX) / 2.0;
        double centerY1 = (box1.minY + box1.maxY) / 2.0;
        double centerZ1 = (box1.minZ + box1.maxZ) / 2.0;

        double centerX2 = (box2.minX + box2.maxX) / 2.0;
        double centerY2 = (box2.minY + box2.maxY) / 2.0;
        double centerZ2 = (box2.minZ + box2.maxZ) / 2.0;

        double dx = centerX1 - centerX2;
        double dy = centerY1 - centerY2;
        double dz = centerZ1 - centerZ2;

        return dx * dx + dy * dy + dz * dz;
    }


    public boolean intersects(@NotNull AxisAlignedBB aabb) {
        boolean ret = false;
        double dist = 1.0E7;
        this.ac.lastBBDamageFactor = 1.0F;
        if (super.intersects(aabb)) {
            dist = this.getDistanceSquareBetween(aabb, this);
            ret = true;
        }

        for (MCH_BoundingBox bb : this.ac.extraBoundingBox) {
            if (bb.getBoundingBox().intersects(aabb)) {
                double dist2 = this.getDistanceSquareBetween(aabb, this);
                if (dist2 < dist) {
                    dist = dist2;
                    this.ac.lastBBDamageFactor = bb.damegeFactor;
                }

                ret = true;
            }
        }

        return ret;
    }
    public @NotNull AxisAlignedBB grow(double x, double y, double z) {
        double newMinX = this.minX - x;
        double newMinY = this.minY - y;
        double newMinZ = this.minZ - z;
        double newMaxX = this.maxX + x;
        double newMaxY = this.maxY + y;
        double newMaxZ = this.maxZ + z;

        return this.NewAABB(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }


    public @NotNull AxisAlignedBB union(AxisAlignedBB other) {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return this.NewAABB(d0, d1, d2, d3, d4, d5);
    }

    public @NotNull AxisAlignedBB expand(double x, double y, double z) {
        double minX = this.minX + Math.min(0.0, x);
        double maxX = this.maxX + Math.max(0.0, x);
        double minY = this.minY + Math.min(0.0, y);
        double maxY = this.maxY + Math.max(0.0, y);
        double minZ = this.minZ + Math.min(0.0, z);
        double maxZ = this.maxZ + Math.max(0.0, z);

        return this.NewAABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public @NotNull AxisAlignedBB contract(double x, double y, double z) {
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

    public RayTraceResult calculateIntercept(@NotNull Vec3d start, @NotNull Vec3d end) {
        this.ac.lastBBDamageFactor = 1.0F;

        RayTraceResult closestHit = super.calculateIntercept(start, end);
        double closestDistance = (closestHit != null) ? start.distanceTo(closestHit.hitVec) : Double.MAX_VALUE;

        for (MCH_BoundingBox extraBox : this.ac.extraBoundingBox) {
            RayTraceResult extraHit = extraBox.getBoundingBox().calculateIntercept(start, end);
            if (extraHit != null) {
                double extraDistance = start.distanceTo(extraHit.hitVec);
                if (extraDistance < closestDistance) {
                    closestHit = extraHit;
                    closestDistance = extraDistance;
                    this.ac.lastBBDamageFactor = extraBox.damegeFactor;
                }
            }
        }

        return closestHit;
    }


}
