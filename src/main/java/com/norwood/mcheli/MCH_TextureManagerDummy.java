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
      super((IResourceManager)null);
      this.tm = t;
   }

   public void func_110577_a(ResourceLocation resouce) {
      if (MCH_ClientCommonTickHandler.cameraMode == 2) {
         this.tm.func_110577_a(R);
      } else {
         this.tm.func_110577_a(resouce);
      }

   }

   public boolean func_110580_a(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
      return this.tm.func_110580_a(textureLocation, textureObj);
   }

   public boolean func_110579_a(ResourceLocation textureLocation, ITextureObject textureObj) {
      return this.tm.func_110579_a(textureLocation, textureObj);
   }

   public ITextureObject func_110581_b(ResourceLocation textureLocation) {
      return this.tm.func_110581_b(textureLocation);
   }

   public ResourceLocation func_110578_a(String name, DynamicTexture texture) {
      return this.tm.func_110578_a(name, texture);
   }

   public void func_110550_d() {
      this.tm.func_110550_d();
   }

   public void func_147645_c(ResourceLocation textureLocation) {
      this.tm.func_147645_c(textureLocation);
   }

   public void func_110549_a(IResourceManager resourceManager) {
      this.tm.func_110549_a(resourceManager);
   }
}
