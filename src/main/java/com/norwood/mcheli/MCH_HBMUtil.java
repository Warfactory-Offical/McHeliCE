package com.norwood.mcheli;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MCH_HBMUtil {
    private static Class<?> nukeExplosionMK5Class;
    private static Class<?> nukeTorexClass;
    private static Class<?> explosionChaosClass;
    private static Class<?> explosionCreatorClass;
    private static Class<?> explosionNT;


    //ah shit now I have to fucking figure out this shit for 1.12.2 hbm
    static {
        try {
            nukeExplosionMK5Class = Class.forName("com.hbm.entity.logic.EntityNukeExplosionMK5");
            nukeTorexClass = Class.forName("com.hbm.entity.effect.EntityNukeTorex");
            explosionChaosClass = Class.forName("com.hbm.explosion.ExplosionChaos");
            explosionCreatorClass = Class.forName("com.hbm.particle.helper.ExplosionCreator");
            explosionNT = Class.forName("com.hbm.explosion.ExplosionNT");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Object EntityNukeExplosionMK5_statFac(World world, int r, double posX, double posY, double posZ) {
        try {
            if (nukeExplosionMK5Class != null) {
                Method statFacMethod = nukeExplosionMK5Class.getMethod("statFac", World.class, int.class, double.class, double.class, double.class);
                return statFacMethod.invoke(null, world, r, posX, posY, posZ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void EntityNukeTorex_statFac(World world, double posX, double posY, double posZ, float nukeYield) {
        try {
            if (nukeTorexClass != null) {
                Method statFacMethod = nukeTorexClass.getMethod("statFac", World.class, double.class, double.class, double.class, float.class);
                statFacMethod.invoke(null, world, posX, posY, posZ, nukeYield);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ExplosionChaos_spawnChlorine(World world, double posX, double posY, double posZ, float chemYield, double chemSpeed, int chemType) {
        try {
            System.out.println("spawn chlorine method");
            if (explosionChaosClass != null) {
                Method spawnChlorineMethod = explosionChaosClass.getMethod("spawnChlorine",
                        World.class, double.class, double.class, double.class, int.class, double.class, int.class);
                spawnChlorineMethod.invoke(null, world, posX, posY, posZ, (int)chemYield, chemSpeed, chemType);
                System.out.println("Chlorine effect spawned successfully");
                //ExplosionChaos.spawnChlorine(worldObj, posX, posY, posZ, 50, 1.5, 0);

            }
        } catch (Exception e) {
            System.out.println("Failed to spawn chlorine effect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ExplosionCreator_composeEffectStandard(World world, double posX, double posY, double posZ, int explosionBlockSize) {
        try {
            if (explosionCreatorClass != null) {
                Method spawnChlorineMethod;
                if(explosionBlockSize<50) {
                    spawnChlorineMethod = explosionCreatorClass.getMethod("composeEffectSmall", World.class, double.class, double.class, double.class);
                } else if (explosionBlockSize<100) {
                    spawnChlorineMethod = explosionCreatorClass.getMethod("composeEffectStandard", World.class, double.class, double.class, double.class);
                }
                else {
                    spawnChlorineMethod = explosionCreatorClass.getMethod("composeEffectLarge", World.class, double.class, double.class, double.class);
                }
                spawnChlorineMethod.invoke(null, world, posX, posY, posZ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object ExplosionNT_instance_init(World world, Entity entity, double posX, double posY, double posZ, float explosionPower) {
        try {
            if (explosionNT != null) {
                Class<?>[] explosionNTParamTypes = {World.class, Entity.class, double.class, double.class, double.class, float.class};
                Constructor<?> explosionNTConstructor = explosionNT.getConstructor(explosionNTParamTypes);
                return explosionNTConstructor.newInstance(world, entity, posX, posY, posZ, explosionPower);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void ExplosionNT_instance_overrideResolutionAndExplode(Object explosionNTInstance, int resolution) {
        try {
            if (explosionNTInstance != null) {
                Method overrideResolutionMethod = explosionNTInstance.getClass().getMethod("overrideResolution", int.class);
                overrideResolutionMethod.invoke(explosionNTInstance, resolution);
                Method explodeMethod = explosionNTInstance.getClass().getMethod("explode");
                explodeMethod.invoke(explosionNTInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void ExplosionNT_instance_addAttrib(Object explosionNTInstance, String attrib) {
        try {
            if (explosionNTInstance != null) {
                Class<?> exAttribClass = Class.forName("com.hbm.explosion.ExplosionNT$ExAttrib");
                Object Attrib = Enum.valueOf((Class<Enum>) exAttribClass, attrib);
                Method addAttribMethod = explosionNT.getMethod("addAttrib", exAttribClass);
                addAttribMethod.invoke(explosionNTInstance,Attrib);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
