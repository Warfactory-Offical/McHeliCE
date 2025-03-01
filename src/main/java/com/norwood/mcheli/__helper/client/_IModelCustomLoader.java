package com.norwood.mcheli.__helper.client;

import java.net.URL;
import net.minecraft.util.ResourceLocation;

/** @deprecated */
@Deprecated
public interface _IModelCustomLoader {
   String getType();

   String[] getSuffixes();

   /** @deprecated */
   @Deprecated
   _IModelCustom loadInstance(ResourceLocation var1) throws _ModelFormatException;

   /** @deprecated */
   @Deprecated
   _IModelCustom loadInstance(String var1, URL var2) throws _ModelFormatException;
}
