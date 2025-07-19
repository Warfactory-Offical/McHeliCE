package com.norwood.mcheli.particles;

import com.norwood.mcheli.wrapper.W_Particle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBreaking.SlimeFactory;
import net.minecraft.client.particle.ParticleBreaking.SnowballFactory;
import net.minecraft.client.particle.ParticleCrit.MagicFactory;
import net.minecraft.client.particle.ParticleDrip.LavaFactory;
import net.minecraft.client.particle.ParticleDrip.WaterFactory;
import net.minecraft.client.particle.ParticleEnchantmentTable.EnchantmentTable;
import net.minecraft.client.particle.ParticleExplosionHuge.Factory;
import net.minecraft.client.particle.ParticleHeart.AngryVillagerFactory;
import net.minecraft.client.particle.ParticleSpell.AmbientMobFactory;
import net.minecraft.client.particle.ParticleSpell.InstantFactory;
import net.minecraft.client.particle.ParticleSpell.MobFactory;
import net.minecraft.client.particle.ParticleSpell.WitchFactory;
import net.minecraft.client.particle.ParticleSuspendedTown.HappyVillagerFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.function.Supplier;

public class MCH_ParticlesUtil {
    public static MCH_EntityParticleMarkPoint markPoint = null;

    public static void spawnParticleExplode(World w, double x, double y, double z, float size, float r, float g, float b, float a, int age) {
        MCH_EntityParticleExplode epe = new MCH_EntityParticleExplode(w, x, y, z, size, age, 0.0);
        epe.setParticleMaxAge(age);
        epe.setRBGColorF(r, g, b);
        epe.setAlphaF(a);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(epe);
    }

    public static void spawnParticleTileCrack(World w, int blockX, int blockY, int blockZ, double x, double y, double z, double mx, double my, double mz) {
        W_Particle.BlockParticleParam name = W_Particle.getParticleTileCrackName(w, blockX, blockY, blockZ);
        if (!name.isEmpty()) {
            DEF_spawnParticle(name.name, x, y, z, mx, my, mz, 20.0F, name.stateId);
        }
    }

    public static boolean spawnParticleTileDust(
            World w, int blockX, int blockY, int blockZ, double x, double y, double z, double mx, double my, double mz, float scale
    ) {
        boolean ret = false;
        int[][] offset = new int[][]{{0, 0, 0}, {0, 0, -1}, {0, 0, 1}, {1, 0, 0}, {-1, 0, 0}};
        int len = offset.length;

        for (int[] ints : offset) {
            W_Particle.BlockParticleParam name = W_Particle.getParticleTileDustName(w, blockX + ints[0], blockY + ints[1], blockZ + ints[2]);
            if (!name.isEmpty()) {
                Particle e = DEF_spawnParticle(name.name, x, y, z, mx, my, mz, 20.0F, name.stateId);
                if (e instanceof MCH_EntityBlockDustFX) {
                    ((MCH_EntityBlockDustFX) e).setScale(scale * 2.0F);
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public static Particle DEF_spawnParticle(String s, double x, double y, double z, double mx, double my, double mz, float dist, int... params) {
        Particle e = doSpawnParticle(s, x, y, z, mx, my, mz, params);
        if (e != null) {
        }

        return e;
    }

    public static Particle doSpawnParticle(String type, double x, double y, double z, double mx, double my, double mz, int... params) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
            int i = mc.gameSettings.particleSetting;
            if (i == 1 && mc.world.rand.nextInt(3) == 0) {
                i = 2;
            }

            double d6 = mc.getRenderViewEntity().posX - x;
            double d7 = mc.getRenderViewEntity().posY - y;
            double d8 = mc.getRenderViewEntity().posZ - z;
            Particle entityfx = null;
            if (type.equalsIgnoreCase("hugeexplosion")) {
                entityfx = create(Factory::new, mc.world, x, y, z, mx, my, mz);
                mc.effectRenderer.addEffect(entityfx);
            } else if (type.equalsIgnoreCase("largeexplode")) {
                entityfx = create(net.minecraft.client.particle.ParticleExplosionLarge.Factory::new, mc.world, x, y, z, mx, my, mz);
                mc.effectRenderer.addEffect(entityfx);
            } else if (type.equalsIgnoreCase("fireworksSpark")) {
                entityfx = create(net.minecraft.client.particle.ParticleFirework.Factory::new, mc.world, x, y, z, mx, my, mz);
                mc.effectRenderer.addEffect(entityfx);
            }

            if (entityfx != null) {
                return entityfx;
            } else {
                double d9 = 300.0;
                if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9) {
                    return null;
                } else if (i > 1) {
                    return null;
                } else {
                    if (type.equalsIgnoreCase("bubble")) {
                        entityfx = create(net.minecraft.client.particle.ParticleBubble.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("suspended")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSuspend.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("depthsuspend")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSuspendedTown.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("townaura")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSuspendedTown.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("crit")) {
                        entityfx = create(net.minecraft.client.particle.ParticleCrit.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("magicCrit")) {
                        entityfx = create(MagicFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("smoke")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSmokeNormal.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("mobSpell")) {
                        entityfx = create(MobFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("mobSpellAmbient")) {
                        entityfx = create(AmbientMobFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("spell")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSpell.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("instantSpell")) {
                        entityfx = create(InstantFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("witchMagic")) {
                        entityfx = create(WitchFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("note")) {
                        entityfx = create(net.minecraft.client.particle.ParticleNote.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("portal")) {
                        entityfx = create(net.minecraft.client.particle.ParticlePortal.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("enchantmenttable")) {
                        entityfx = create(EnchantmentTable::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("explode")) {
                        entityfx = create(net.minecraft.client.particle.ParticleExplosion.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("flame")) {
                        entityfx = create(net.minecraft.client.particle.ParticleFlame.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("lava")) {
                        entityfx = create(net.minecraft.client.particle.ParticleLava.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("footstep")) {
                        entityfx = create(net.minecraft.client.particle.ParticleFootStep.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("splash")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSplash.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("wake")) {
                        entityfx = create(net.minecraft.client.particle.ParticleWaterWake.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("largesmoke")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSmokeLarge.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("cloud")) {
                        entityfx = create(net.minecraft.client.particle.ParticleCloud.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("reddust")) {
                        entityfx = create(net.minecraft.client.particle.ParticleRedstone.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("snowballpoof")) {
                        entityfx = create(SnowballFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("dripWater")) {
                        entityfx = create(WaterFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("dripLava")) {
                        entityfx = create(LavaFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("snowshovel")) {
                        entityfx = create(net.minecraft.client.particle.ParticleSnowShovel.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("slime")) {
                        entityfx = create(SlimeFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("heart")) {
                        entityfx = create(net.minecraft.client.particle.ParticleHeart.Factory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("angryVillager")) {
                        entityfx = create(AngryVillagerFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.equalsIgnoreCase("happyVillager")) {
                        entityfx = create(HappyVillagerFactory::new, mc.world, x, y, z, mx, my, mz);
                    } else if (type.startsWith("iconcrack")) {
                        entityfx = create(net.minecraft.client.particle.ParticleBreaking.Factory::new, mc.world, x, y, z, mx, my, mz, params);
                    } else if (type.startsWith("blockcrack")) {
                        entityfx = create(net.minecraft.client.particle.ParticleDigging.Factory::new, mc.world, x, y, z, mx, my, mz, params);
                    } else if (type.startsWith("blockdust")) {
                        entityfx = create(MCH_EntityBlockDustFX.Factory::new, mc.world, x, y, z, mx, my, mz, params);
                    }

                    if (entityfx != null) {
                        mc.effectRenderer.addEffect(entityfx);
                    }

                    return entityfx;
                }
            }
        } else {
            return null;
        }
    }

    public static void spawnParticle(MCH_ParticleParam p) {
        if (p.world.isRemote) {
            MCH_EntityParticleBase entityFX;
            if (p.name.equalsIgnoreCase("Splash")) {
                entityFX = new MCH_EntityParticleSplash(p.world, p.posX, p.posY, p.posZ, p.motionX, p.motionY, p.motionZ);
            } else {
                entityFX = new MCH_EntityParticleSmoke(p.world, p.posX, p.posY, p.posZ, p.motionX, p.motionY, p.motionZ);
            }

            entityFX.setRBGColorF(p.r, p.g, p.b);
            entityFX.setAlphaF(p.a);
            if (p.age > 0) {
                entityFX.setParticleMaxAge(p.age);
            }

            entityFX.moutionYUpAge = p.motionYUpAge;
            entityFX.gravity = p.gravity;
            entityFX.isEffectedWind = p.isEffectWind;
            entityFX.diffusible = p.diffusible;
            entityFX.toWhite = p.toWhite;
            if (p.diffusible) {
                entityFX.setParticleScale(p.size * 0.2F);
                entityFX.particleMaxScale = p.size * 2.0F;
            } else {
                entityFX.setParticleScale(p.size);
            }

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(entityFX);
        }
    }

    public static void spawnMarkPoint(EntityPlayer player, double x, double y, double z) {
        clearMarkPoint();
        markPoint = new MCH_EntityParticleMarkPoint(player.world, x, y, z, player.getTeam());
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(markPoint);
    }

    public static void clearMarkPoint() {
        if (markPoint != null) {
            markPoint.setExpired();
            markPoint = null;
        }
    }

    private static Particle create(
            Supplier<IParticleFactory> factoryFunc, World world, double x, double y, double z, double mx, double my, double mz, int... param
    ) {
        return factoryFunc.get().createParticle(-1, world, x, y, z, mx, my, mz, param);
    }
}
