package com.norwood.mcheli.__helper.client.model.loader;

import java.io.IOException;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.__helper.client._ModelFormatException;
import com.norwood.mcheli.wrapper.modelloader.W_MetasequoiaObject;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MetasequoiaModelLoader implements IVertexModelLoader {
   public _IModelCustom load(IResourceManager resourceManager, ResourceLocation location) throws IOException, _ModelFormatException {
      ResourceLocation modelLocation = this.withExtension(location);
      IResource resource = resourceManager.func_110536_a(modelLocation);
      return new W_MetasequoiaObject(modelLocation, resource);
   }

   public String getExtension() {
      return "mqo";
   }
}
