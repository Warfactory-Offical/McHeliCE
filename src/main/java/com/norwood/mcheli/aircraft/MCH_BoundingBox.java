package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class MCH_BoundingBox {
    public final double offsetX;
    public final double offsetY;
    public final double offsetZ;
    public final float width;
    public final float height;
    public final float damegeFactor;
    public Vec3d rotatedOffset;
    public List<Vec3d> pos = new ArrayList<>();
    private AxisAlignedBB boundingBox;

    public MCH_BoundingBox(double x, double y, double z, float w, float h, float df) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.width = w;
        this.height = h;
        this.damegeFactor = df;
        this.boundingBox = new AxisAlignedBB(x - w / 2.0F, y - h / 2.0F, z - w / 2.0F, x + w / 2.0F, y + h / 2.0F, z + w / 2.0F);
        this.updatePosition(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
    }

    public void add(double x, double y, double z) {
        this.pos.add(0, new Vec3d(x, y, z));

        while (this.pos.size() > MCH_Config.HitBoxDelayTick.prmInt + 2) {
            this.pos.remove(MCH_Config.HitBoxDelayTick.prmInt + 2);
        }
    }

    public MCH_BoundingBox copy() {
        return new MCH_BoundingBox(this.offsetX, this.offsetY, this.offsetZ, this.width, this.height, this.damegeFactor);
    }

    public void updatePosition(double posX, double posY, double posZ, float yaw, float pitch, float roll) {
        Vec3d v = new Vec3d(this.offsetX, this.offsetY, this.offsetZ);
        this.rotatedOffset = MCH_Lib.RotVec3(v, -yaw, -pitch, -roll);
        this.add(posX + this.rotatedOffset.x, posY + this.rotatedOffset.y, posZ + this.rotatedOffset.z);
        int index = MCH_Config.HitBoxDelayTick.prmInt;
        Vec3d cp = index < this.pos.size() ? this.pos.get(index) : this.pos.get(this.pos.size() - 1);
        Vec3d pp = index + 1 < this.pos.size() ? this.pos.get(index + 1) : this.pos.get(this.pos.size() - 1);
        double sx = (this.width + Math.abs(cp.x - pp.x)) / 2.0;
        double sy = (this.height + Math.abs(cp.y - pp.y)) / 2.0;
        double sz = (this.width + Math.abs(cp.z - pp.z)) / 2.0;
        double x = (cp.x + pp.x) / 2.0;
        double y = (cp.y + pp.y) / 2.0;
        double z = (cp.z + pp.z) / 2.0;
        this.boundingBox = new AxisAlignedBB(x - sx, y - sy, z - sz, x + sx, y + sy, z + sz);
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
}
