package com.norwood.mcheli;

import com.norwood.mcheli.helper.world.MCH_ExplosionV2;
import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MCH_Explosion {
    public static MCH_Explosion.ExplosionResult newExplosion(
            World w,
            @Nullable Entity entityExploded,
            @Nullable Entity player,
            double x,
            double y,
            double z,
            float size,
            float sizeBlock,
            boolean playSound,
            boolean isSmoking,
            boolean isFlaming,
            boolean isDestroyBlock,
            int countSetFireEntity
    ) {
        return newExplosion(w, entityExploded, player, x, y, z, size, sizeBlock, playSound, isSmoking, isFlaming, isDestroyBlock, countSetFireEntity, null);
    }

    public static MCH_Explosion.ExplosionResult newExplosion(
            World w,
            @Nullable Entity entityExploded,
            @Nullable Entity player,
            double x,
            double y,
            double z,
            float size,
            float sizeBlock,
            boolean playSound,
            boolean isSmoking,
            boolean isFlaming,
            boolean isDestroyBlock,
            int countSetFireEntity,
            MCH_DamageFactor df
    ) {
        if (w.isRemote) {
            return null;
        } else {
            MCH_ExplosionV2 exp = new MCH_ExplosionV2(w, entityExploded, player, x, y, z, size, isFlaming, w.getGameRules().getBoolean("mobGriefing"));
            exp.isDestroyBlock = isDestroyBlock;
            exp.explosionSizeBlock = sizeBlock;
            exp.countSetFireEntity = countSetFireEntity;
            exp.isPlaySound = playSound;
            exp.isInWater = false;
            exp.damageFactor = df;
            exp.doExplosionA();
            exp.doExplosionB(false);
            MCH_PacketEffectExplosion.ExplosionParam param = MCH_PacketEffectExplosion.create();
            param.exploderID = W_Entity.getEntityId(entityExploded);
            param.posX = x;
            param.posY = y;
            param.posZ = z;
            param.size = size;
            param.inWater = false;
            param.setAffectedPositions(exp.getAffectedBlockPositions());
            MCH_PacketEffectExplosion.send(param);
            return exp.getResult();
        }
    }

    @Nullable
    public static MCH_Explosion.ExplosionResult newExplosionInWater(
            World w,
            @Nullable Entity entityExploded,
            @Nullable Entity player,
            double x,
            double y,
            double z,
            float size,
            float sizeBlock,
            boolean playSound,
            boolean isSmoking,
            boolean isFlaming,
            boolean isDestroyBlock,
            int countSetFireEntity,
            MCH_DamageFactor df
    ) {
        if (w.isRemote) {
            return null;
        } else {
            MCH_ExplosionV2 exp = new MCH_ExplosionV2(w, entityExploded, player, x, y, z, size, isFlaming, w.getGameRules().getBoolean("mobGriefing"));
            exp.isDestroyBlock = isDestroyBlock;
            exp.explosionSizeBlock = sizeBlock;
            exp.countSetFireEntity = countSetFireEntity;
            exp.isPlaySound = playSound;
            exp.isInWater = true;
            exp.damageFactor = df;
            exp.doExplosionA();
            exp.doExplosionB(false);
            MCH_PacketEffectExplosion.ExplosionParam param = MCH_PacketEffectExplosion.create();
            param.exploderID = W_Entity.getEntityId(entityExploded);
            param.posX = x;
            param.posY = y;
            param.posZ = z;
            param.size = size;
            param.inWater = true;
            param.setAffectedPositions(exp.getAffectedBlockPositions());
            MCH_PacketEffectExplosion.send(param);
            return exp.getResult();
        }
    }

    public static class ExplosionResult {
        public boolean hitEntity = false;
    }
}
