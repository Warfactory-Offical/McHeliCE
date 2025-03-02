package com.norwood.mcheli;

import java.util.List;
import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.world.MCH_ExplosionV2;
import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MCH_Explosion {
   public static MCH_Explosion.ExplosionResult newExplosion(World w, @Nullable Entity entityExploded, @Nullable Entity player, double x, double y, double z, float size, float sizeBlock, boolean playSound, boolean isSmoking, boolean isFlaming, boolean isDestroyBlock, int countSetFireEntity) {
      return newExplosion(w, entityExploded, player, x, y, z, size, sizeBlock, playSound, isSmoking, isFlaming, isDestroyBlock, countSetFireEntity, (MCH_DamageFactor)null);
   }

   public static MCH_Explosion.ExplosionResult newExplosion(World w, @Nullable Entity entityExploded, @Nullable Entity player, double x, double y, double z, float size, float sizeBlock, boolean playSound, boolean isSmoking, boolean isFlaming, boolean isDestroyBlock, int countSetFireEntity, MCH_DamageFactor df) {
      if (w.isRemote) {
         return null;
      } else {
         MCH_ExplosionV2 exp = new MCH_ExplosionV2(w, entityExploded, player, x, y, z, size, isFlaming, w.func_82736_K().func_82766_b("mobGriefing"));
         exp.isDestroyBlock = isDestroyBlock;
         exp.explosionSizeBlock = sizeBlock;
         exp.countSetFireEntity = countSetFireEntity;
         exp.isPlaySound = playSound;
         exp.isInWater = false;
         exp.damageFactor = df;
         exp.func_77278_a();
         exp.func_77279_a(false);
         MCH_PacketEffectExplosion.ExplosionParam param = MCH_PacketEffectExplosion.create();
         param.exploderID = W_Entity.getEntityId(entityExploded);
         param.posX = x;
         param.posY = y;
         param.posZ = z;
         param.size = size;
         param.inWater = false;
         param.setAffectedPositions(exp.func_180343_e());
         MCH_PacketEffectExplosion.send(param);
         return exp.getResult();
      }
   }

   @Nullable
   public static MCH_Explosion.ExplosionResult newExplosionInWater(World w, @Nullable Entity entityExploded, @Nullable Entity player, double x, double y, double z, float size, float sizeBlock, boolean playSound, boolean isSmoking, boolean isFlaming, boolean isDestroyBlock, int countSetFireEntity, MCH_DamageFactor df) {
      if (w.isRemote) {
         return null;
      } else {
         MCH_ExplosionV2 exp = new MCH_ExplosionV2(w, entityExploded, player, x, y, z, size, isFlaming, w.func_82736_K().func_82766_b("mobGriefing"));
         exp.isDestroyBlock = isDestroyBlock;
         exp.explosionSizeBlock = sizeBlock;
         exp.countSetFireEntity = countSetFireEntity;
         exp.isPlaySound = playSound;
         exp.isInWater = true;
         exp.damageFactor = df;
         exp.func_77278_a();
         exp.func_77279_a(false);
         MCH_PacketEffectExplosion.ExplosionParam param = MCH_PacketEffectExplosion.create();
         param.exploderID = W_Entity.getEntityId(entityExploded);
         param.posX = x;
         param.posY = y;
         param.posZ = z;
         param.size = size;
         param.inWater = true;
         param.setAffectedPositions(exp.func_180343_e());
         MCH_PacketEffectExplosion.send(param);
         return exp.getResult();
      }
   }

   public static void playExplosionSound(World w, double x, double y, double z) {
      MCH_ExplosionV2.playExplosionSound(w, x, y, z);
   }

   public static void effectExplosion(World world, Entity exploder, double explosionX, double explosionY, double explosionZ, float explosionSize, boolean isSmoking, List<BlockPos> affectedPositions) {
      MCH_ExplosionV2.effectMODExplosion(world, explosionX, explosionY, explosionZ, explosionSize, affectedPositions);
   }

   public static void DEF_effectExplosion(World world, Entity exploder, double explosionX, double explosionY, double explosionZ, float explosionSize, boolean isSmoking, List<BlockPos> affectedPositions) {
      MCH_ExplosionV2.effectVanillaExplosion(world, explosionX, explosionY, explosionZ, explosionSize, affectedPositions);
   }

   public static void effectExplosionInWater(World world, Entity exploder, double explosionX, double explosionY, double explosionZ, float explosionSize, boolean isSmoking) {
      MCH_ExplosionV2.effectExplosionInWater(world, explosionX, explosionY, explosionZ, explosionSize);
   }

   public static class ExplosionResult {
      public boolean hitEntity = false;
   }
}
