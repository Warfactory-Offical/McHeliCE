package com.norwood.mcheli.wrapper.modelloader;

import java.net.URL;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.__helper.client._IModelCustomLoader;
import com.norwood.mcheli.__helper.client._ModelFormatException;
import net.minecraft.util.ResourceLocation;

public class W_MqoModelLoader implements _IModelCustomLoader {
   private static final String[] types = new String[]{"mqo"};

   @Override
   public String getType() {
      return "Metasequoia model";
   }

   @Override
   public String[] getSuffixes() {
      return types;
   }

   @Override
   public _IModelCustom loadInstance(ResourceLocation resource) throws _ModelFormatException {
      return new W_MetasequoiaObject(resource);
   }

   @Override
   public _IModelCustom loadInstance(String resourceName, URL resource) throws _ModelFormatException {
      return new W_MetasequoiaObject(resourceName, resource);
   }
}
