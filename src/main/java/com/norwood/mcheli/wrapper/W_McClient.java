package com.norwood.mcheli.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class W_McClient {
   public static void playSoundClick(float volume, float pitch) {
      playSound(SoundEvents.field_187909_gi, volume, pitch);
   }

   public static void playSound(SoundEvent sound, float volume, float pitch) {
      Minecraft.getMinecraft().func_147118_V().func_147682_a(new W_Sound(sound, volume, pitch));
   }

   public static void DEF_playSoundFX(String name, float volume, float pitch) {
      Minecraft.getMinecraft().func_147118_V().func_147682_a(new W_Sound(new ResourceLocation(name), volume, pitch));
   }

   public static void MOD_playSoundFX(String name, float volume, float pitch) {
      DEF_playSoundFX(W_MOD.DOMAIN + ":" + name, volume, pitch);
   }

   public static void addSound(String name) {
   }

   public static void DEF_bindTexture(String tex) {
      Minecraft.getMinecraft().field_71446_o.func_110577_a(new ResourceLocation(tex));
   }

   public static void MOD_bindTexture(String tex) {
      Minecraft.getMinecraft().field_71446_o.func_110577_a(new ResourceLocation(W_MOD.DOMAIN, tex));
   }

   public static boolean isGamePaused() {
      Minecraft mc = Minecraft.getMinecraft();
      return mc.func_147113_T();
   }

   public static Entity getRenderEntity() {
      return Minecraft.getMinecraft().func_175606_aa();
   }

   public static void setRenderEntity(EntityLivingBase entity) {
      Minecraft.getMinecraft().func_175607_a(entity);
   }
}
