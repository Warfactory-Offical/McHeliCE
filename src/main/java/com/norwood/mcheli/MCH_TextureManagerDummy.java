package com.norwood.mcheli;

import com.norwood.mcheli.__helper.MCH_Utils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class MCH_TextureManagerDummy extends TextureManager {
   public static ResourceLocation R = MCH_Utils.suffix("textures/test.png");
   private TextureManager tm;

   public MCH_TextureManagerDummy(TextureManager t) {
      super(null);
      this.tm = t;
   }

   public void bindTexture(ResourceLocation resouce) {
      if (MCH_ClientCommonTickHandler.cameraMode == 2) {
         this.tm.bindTexture(R);
      } else {
         this.tm.bindTexture(resouce);
      }
   }

   public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
      return this.tm.loadTickableTexture(textureLocation, textureObj);
   }

   public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
      return this.tm.loadTexture(textureLocation, textureObj);
   }

   public ITextureObject getTexture(ResourceLocation textureLocation) {
      return this.tm.getTexture(textureLocation);
   }

   public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
      return this.tm.getDynamicTextureLocation(name, texture);
   }

   public void tick() {
      this.tm.tick();
   }

   public void deleteTexture(ResourceLocation textureLocation) {
      this.tm.deleteTexture(textureLocation);
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      this.tm.onResourceManagerReload(resourceManager);
   }
}
