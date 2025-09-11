package com.norwood.mcheli.helper;

import com.google.common.collect.Sets;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_MOD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISoundEventAccessor;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(
        modid = "mcheli"
)
public class MCH_SoundEvents {
    private static final Set<ResourceLocation> registryWrapper = Sets.newLinkedHashSet();

    @SubscribeEvent
    static void onSoundEventRegisterEvent(Register<SoundEvent> event) {
        for (ResourceLocation soundLocation : registryWrapper) {
            event.getRegistry().register(new SoundEvent(soundLocation).setRegistryName(soundLocation));

        }
    }

    public static void registerSoundEventName(String name) {
        registerSoundEventName(new ResourceLocation(MCH_MOD.MOD_ID, name));
    }

    public static void registerSoundEventName(ResourceLocation name) {
        registryWrapper.add(name);
    }

    public static void playSound(World w, double x, double y, double z, String name, float volume, float pitch) {
        SoundEvent sound = getSound(name);
        w.playSound(null, x, y, z, sound, SoundCategory.MASTER, volume, pitch);
    }

    public static SoundEvent getSound(String name) {
        SoundEvent sound = getSound(new ResourceLocation(name));
        return Objects.requireNonNull(sound);
    }

    @Nullable
    public static SoundEvent getSound(ResourceLocation location) {
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(location);
        if (sound == null) {
            MCH_Lib.Log("[WARNING] Sound event does not found. event name= " + location);
        }

        return sound;
    }

    //Because of 1.12's namespaces, old json files never had to worry about it due to ID system
    public static void fixSoundEntires() {
        var mcSoundRegistry = Minecraft.getMinecraft().getSoundHandler().soundRegistry.soundRegistry;

        for (SoundEvent event : ForgeRegistries.SOUND_EVENTS.getValuesCollection()) {
            if (!event.getSoundName().getNamespace().equals(MCH_MOD.MOD_ID)) continue;
            SoundEventAccessor oldAcessor = mcSoundRegistry.get(event.getRegistryName());
            if (oldAcessor != null) {
                List<ISoundEventAccessor<Sound>> fixedAccessorList = oldAcessor.accessorList
                        .stream().map((accessor) -> {
                                    Sound sound = accessor.cloneEntry();
                                    if (sound.name.getNamespace().equals("minecraft")) {
//                                        MCH_Lib.Log("Sound entry: %s had invalid namespace mapping, patching. Please prepend \"mcheli:\" before this sound entry in your content json file",  sound.name.getPath());
                                        //Dude this logger sucks dick
                                        System.out.println("Sound entry:" + sound.name.getPath() +  " had invalid namespace mapping, patching. Please prepend \"mcheli:\" before this sound entry in your content json file");
                                        sound.name = new ResourceLocation(MCH_MOD.MOD_ID, sound.name.getPath());
                                    }

                                    return (ISoundEventAccessor<Sound>) sound;
                                }
                        ).collect(Collectors.toList());

                oldAcessor.accessorList = fixedAccessorList;
            }

        }

    }

}
