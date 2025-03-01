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

   public void func_110549_a(IResourceManager resourceManager) {
   }

   public boolean accepts(ResourceLocation modelLocation) {
      if (!(modelLocation instanceof ModelResourceLocation)) {
         return false;
      } else {
         ModelResourceLocation location = (ModelResourceLocation)modelLocation;
         return location.func_110624_b().equals("mcheli") && location.func_177518_c().equals("mcheli_legacy");
      }
   }

   public IModel loadModel(ResourceLocation modelLocation) throws Exception {
      String path = modelLocation.func_110624_b() + ":items/" + modelLocation.func_110623_a();
      ModelBlock modelblock = ModelBlock.func_178294_a(TEMPLATE.replaceAll("__item__", path));
      ModelBlock parent = (ModelBlock)ModelLoaderRegistry.getModel(modelblock.func_178305_e()).asVanillaModel().get();
      modelblock.field_178315_d = parent;
      return new MCH_WrapperItemLayerModel(modelblock);
   }
}
