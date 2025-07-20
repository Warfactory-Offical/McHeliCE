package com.norwood.mcheli.debug._v1.model;

import com.norwood.mcheli.helper.debug.DebugInfoObject;
import com.norwood.mcheli.debug._v1.PrintStreamWrapper;

import java.util.ArrayList;
import java.util.List;

public class _GroupObject implements DebugInfoObject {
    public final String name;
    private final List<_Face> faces;

    private _GroupObject(String name, List<_Face> faces) {
        this.name = name;
        this.faces = faces;
    }

    public static _GroupObject.Builder builder() {
        return new _GroupObject.Builder();
    }

    @Override
    public void printInfo(PrintStreamWrapper stream) {
        stream.push(String.format("G: [name: %s]", this.name));
        this.faces.forEach(f -> f.printInfo(stream));
        stream.pop();
        stream.println();
    }

    public static class Builder {
        private String name;
        private final List<_Face> faces = new ArrayList<>();

        public _GroupObject.Builder name(String name) {
            this.name = name;
            return this;
        }

        public _GroupObject.Builder addFace(_Face face) {
            this.faces.add(face);
            return this;
        }

        public int faceSize() {
            return this.faces.size();
        }

        public _GroupObject build() {
            return new _GroupObject(this.name, this.faces);
        }
    }
}
