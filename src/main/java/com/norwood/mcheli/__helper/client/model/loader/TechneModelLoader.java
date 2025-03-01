package com.norwood.mcheli.__helper.client.model.loader;

import java.io.IOException;
import java.net.URL;
import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.__helper.client._IModelCustomLoader;
import com.norwood.mcheli.__helper.client._ModelFormatException;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TechneModelLoader implements _IModelCustomLoader, IVertexModelLoader {
   private static final String[] types = new String[]{"tcn"};

   public String getType() {
      return "Techne model";
   }

   public String[] getSuffixes() {
      return types;
   }

   /** @deprecated */
   @Deprecated
   public _IModelCustom loadInstance(ResourceLocation resource) throws _ModelFormatException {
      throw new UnsupportedOperationException("Techne model is unsupported. file:" + resource);
   }

   /** @deprecated */
   @Deprecated
   public _IModelCustom loadInstance(String resourceName, URL resource) throws _ModelFormatException {
      throw new UnsupportedOperationException("Techne model is unsupported. file:" + resource);
   }

   public String getExtension() {
      return "tcn";
   }

   @Nullable
   public _IModelCustom load(IResourceManager resourceManager, ResourceLocation location) throws IOException, _ModelFormatException {
      return null;
   }
}
