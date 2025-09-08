package com.norwood.mcheli.debug._v1.model;

import com.norwood.mcheli.helper.debug.DebugInfoObject;
import com.norwood.mcheli.debug._v1.PrintStreamWrapper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class _Face implements DebugInfoObject {
    private final int[] verticesID;
    private final _Vertex[] vertices;
    private final _Vertex[] vertexNormals;
    private final _TextureCoord[] textureCoordinates;
    private final _Vertex faceNormal;

    _Face(int[] ids, _Vertex[] verts, _TextureCoord[] texCoords) {
        this(ids, verts, verts, texCoords);
    }

    _Face(int[] ids, _Vertex[] verts, _Vertex[] normals, _TextureCoord[] texCoords) {
        this.verticesID = ids;
        this.vertices = verts;
        this.vertexNormals = normals;
        this.textureCoordinates = texCoords;
        this.faceNormal = calculateFaceNormal(verts);
    }

    private static _Vertex calculateFaceNormal(_Vertex[] verts) {
        Vec3d v1 = new Vec3d(verts[1].x - verts[0].x, verts[1].y - verts[0].y, verts[1].z - verts[0].z);
        Vec3d v2 = new Vec3d(verts[2].x - verts[0].x, verts[2].y - verts[0].y, verts[2].z - verts[0].z);
        Vec3d normalVector = v1.crossProduct(v2).normalize();
        return new _Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
    }

    private static _Vertex getVerticesNormalFromFace(_Vertex fnormal, int verticesID, List<_Face> faces, float facet) {
        _Vertex v = new _Vertex(0.0F, 0.0F, 0.0F);

        for (_Face f : faces) {
            for (int id : f.verticesID) {
                if (id == verticesID) {
                    if (!(f.faceNormal.x * fnormal.x + f.faceNormal.y * fnormal.y + f.faceNormal.z * fnormal.z < facet)) {
                        v = v.add(f.faceNormal);
                    }
                    break;
                }
            }
        }

        return v.normalize();
    }

    _Face calcVerticesNormal(List<_Face> faces, boolean shading, double facet) {
        _Vertex[] vnormals = new _Vertex[this.verticesID.length];

        IntStream.range(0, this.verticesID.length).parallel().forEach(i -> {
            _Vertex vn = getVerticesNormalFromFace(this.faceNormal, this.verticesID[i], faces, (float) facet).normalize();
            if (shading) {
                double dot = this.faceNormal.x * vn.x + this.faceNormal.y * vn.y + this.faceNormal.z * vn.z;
                vnormals[i] = (dot >= facet) ? vn : this.faceNormal;
            } else {
                vnormals[i] = this.faceNormal;
            }
        });

        return new _Face(this.verticesID, this.vertices, vnormals, this.textureCoordinates);

    }

    @Override
    public void printInfo(PrintStreamWrapper stream) {
        stream.println("F: [");
        stream.push();
        stream.println("ids: " + Arrays.toString(this.verticesID));
        stream.println("--- verts");
        Arrays.stream(this.vertices).forEach(v -> v.printInfo(stream));
        stream.println("--- normals");
        Arrays.stream(this.vertexNormals).forEach(n -> n.printInfo(stream));
        stream.println("--- tex coords");
        Arrays.stream(this.textureCoordinates).forEach(t -> t.printInfo(stream));
        stream.println("--- face normal");
        this.faceNormal.printInfo(stream);
        stream.pop();
        stream.println("]");
    }
}
