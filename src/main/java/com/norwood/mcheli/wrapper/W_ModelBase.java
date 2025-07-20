package com.norwood.mcheli.wrapper;

import com.norwood.mcheli.helper.client._IModelCustom;
import com.norwood.mcheli.helper.client._IModelCustomLoader;
import com.norwood.mcheli.helper.client._ModelFormatException;
import com.norwood.mcheli.helper.client.model.loader.TechneModelLoader;
import com.norwood.mcheli.wrapper.modelloader.W_MqoModelLoader;
import com.norwood.mcheli.wrapper.modelloader.W_ObjModelLoader;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public abstract class W_ModelBase extends ModelBase {
    private static final _IModelCustomLoader objLoader = new W_ObjModelLoader();
    private static final _IModelCustomLoader mqoLoader = new W_MqoModelLoader();
    private static final _IModelCustomLoader tcnLoader = new TechneModelLoader();

    public static _IModelCustom loadModel(String name) throws IllegalArgumentException, _ModelFormatException {
        ResourceLocation resource = new ResourceLocation("mcheli", name);
        String path = resource.getPath();
        int i = path.lastIndexOf(46);
        if (i == -1) {
            throw new IllegalArgumentException("The resource name is not valid");
        } else if (path.substring(i).equalsIgnoreCase(".mqo")) {
            return mqoLoader.loadInstance(resource);
        } else {
            return path.substring(i).equalsIgnoreCase(".obj") ? objLoader.loadInstance(resource) : tcnLoader.loadInstance(resource);
        }
    }
}
