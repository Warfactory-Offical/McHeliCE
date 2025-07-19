package com.norwood.mcheli.debug._v1.model;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class ModelLoaderDebug {
    public static void main(String[] args) {
        try {
            ObjModel model = ObjParser.parse(Files.newInputStream(new File(getResource("models/gltd.obj")).toPath()));
            model.printInfo();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    static URI getResource(String path) {
        URL url = ModelLoaderDebug.class.getClassLoader().getResource("assets/mcheli/" + path);

        try {
            return url != null ? url.toURI() : null;
        } catch (URISyntaxException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
