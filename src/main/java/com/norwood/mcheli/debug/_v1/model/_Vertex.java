package com.norwood.mcheli.debug._v1.model;

import com.norwood.mcheli.__helper.debug.DebugInfoObject;
import com.norwood.mcheli.debug._v1.PrintStreamWrapper;

class _Vertex implements DebugInfoObject {
    public final float x;
    public final float y;
    public final float z;

    _Vertex(float x, float y) {
        this(x, y, 0.0F);
    }

    _Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public _Vertex normalize() {
        double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return new _Vertex((float) (this.x / d), (float) (this.y / d), (float) (this.z / d));
    }

    public _Vertex add(_Vertex v) {
        return new _Vertex(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    @Override
    public void printInfo(PrintStreamWrapper stream) {
        stream.println(String.format("V: [%.6f, %.6f, %.6f]", this.x, this.y, this.z));
    }
}
