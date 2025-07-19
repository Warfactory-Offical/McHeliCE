package com.norwood.mcheli.__helper.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.client.model.loader.IVertexModelLoader;
import com.norwood.mcheli.__helper.client.model.loader.MetasequoiaModelLoader;
import com.norwood.mcheli.__helper.client.model.loader.TechneModelLoader;
import com.norwood.mcheli.__helper.client.model.loader.WavefrontModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MCH_Models {
   private static IVertexModelLoader objLoader = new WavefrontModelLoader();
   private static IVertexModelLoader mqoLoader = new MetasequoiaModelLoader();
   private static IVertexModelLoader tcnLoader = new TechneModelLoader();

   public static _IModelCustom loadModel(String name) throws IllegalArgumentException, _ModelFormatException {
      ResourceLocation resource = MCH_Utils.suffix("models/" + name);
      IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
      IVertexModelLoader[] loaders = new IVertexModelLoader[]{objLoader, mqoLoader, tcnLoader};
      _IModelCustom model = null;

      for (IVertexModelLoader loader : loaders) {
         try {
            model = loader.load(resourceManager, resource);
         } catch (FileNotFoundException var10) {
            MCH_Utils.logger().debug("model file not found '" + resource + "' at ." + loader.getExtension());
         } catch (IOException var11) {
            MCH_Utils.logger().error("load model error '" + resource + "' at ." + loader.getExtension(), var11);
            return null;
         }

         if (model != null) {
            break;
         }
      }

      return model;
   }
}
