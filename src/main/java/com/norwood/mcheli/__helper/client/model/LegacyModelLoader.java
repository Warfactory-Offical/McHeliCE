package com.norwood.mcheli.__helper.client.model;

import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public enum LegacyModelLoader implements ICustomModelLoader {
    INSTANCE;

    public static final String VARIANT = "mcheli_legacy";
    static final String TEMPLATE = "{'parent':'item/generated','textures':{'layer0':'__item__'}}".replaceAll("'", "\"");

    public void onResourceManagerReload(IResourceManager resourceManager) {
    }

    public boolean accepts(ResourceLocation modelLocation) {
        if (!(modelLocation instanceof ModelResourceLocation)) {
            return false;
        } else {
            ModelResourceLocation location = (ModelResourceLocation) modelLocation;
            return location.getNamespace().equals("mcheli") && location.getVariant().equals("mcheli_legacy");
        }
    }

    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        String path = modelLocation.getNamespace() + ":items/" + modelLocation.getPath();
        ModelBlock modelblock = ModelBlock.deserialize(TEMPLATE.replaceAll("__item__", path));
        ModelBlock parent = ModelLoaderRegistry.getModel(modelblock.getParentLocation()).asVanillaModel().get();
        modelblock.parent = parent;
        return new MCH_WrapperItemLayerModel(modelblock);
    }
}
