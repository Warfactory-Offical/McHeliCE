package com.norwood.mcheli.__helper;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Lib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@EventBusSubscriber(
   modid = "mcheli"
)
public class MCH_SoundEvents {
   private static final Set<ResourceLocation> registryWrapper = Sets.newLinkedHashSet();

   @SubscribeEvent
   static void onSoundEventRegisterEvent(Register<SoundEvent> event) {
      Iterator var1 = registryWrapper.iterator();

      while(var1.hasNext()) {
         ResourceLocation soundLocation = (ResourceLocation)var1.next();
         event.getRegistry().register((new SoundEvent(soundLocation)).setRegistryName(soundLocation));
      }

   }

   public static void registerSoundEventName(String name) {
      registerSoundEventName(MCH_Utils.suffix(name));
   }

   public static void registerSoundEventName(ResourceLocation name) {
      registryWrapper.add(name);
   }

   public static void playSound(World w, double x, double y, double z, String name, float volume, float pitch) {
      SoundEvent sound = getSound(name);
      w.func_184148_a((EntityPlayer)null, x, y, z, sound, SoundCategory.MASTER, volume, pitch);
   }

   public static SoundEvent getSound(String name) {
      SoundEvent sound = getSound(new ResourceLocation(name));
      return (SoundEvent)Objects.requireNonNull(sound);
   }

   @Nullable
   public static SoundEvent getSound(ResourceLocation location) {
      SoundEvent sound = (SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(location);
      if (sound == null) {
         MCH_Lib.Log("[WARNING] Sound event does not found. event name= " + location);
      }

      return sound;
   }
}
