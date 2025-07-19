package com.norwood.mcheli.__helper.client.model.loader;

import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.__helper.client._IModelCustomLoader;
import com.norwood.mcheli.__helper.client._ModelFormatException;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;

@SideOnly(Side.CLIENT)
public class TechneModelLoader implements _IModelCustomLoader, IVertexModelLoader {
    private static final String[] types = new String[]{"tcn"};

    @Override
    public String getType() {
        return "Techne model";
    }

    @Override
    public String[] getSuffixes() {
        return types;
    }

    @Deprecated
    @Override
    public _IModelCustom loadInstance(ResourceLocation resource) throws _ModelFormatException {
        throw new UnsupportedOperationException("Techne model is unsupported. file:" + resource);
    }

    @Deprecated
    @Override
    public _IModelCustom loadInstance(String resourceName, URL resource) throws _ModelFormatException {
        throw new UnsupportedOperationException("Techne model is unsupported. file:" + resource);
    }

    @Override
    public String getExtension() {
        return "tcn";
    }

    @Nullable
    @Override
    public _IModelCustom load(IResourceManager resourceManager, ResourceLocation location) throws IOException, _ModelFormatException {
        return null;
    }
}
