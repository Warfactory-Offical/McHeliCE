package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.wrapper.W_MovingObjectPosition;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MCH_EntityBullet extends MCH_EntityBaseBullet {
    public MCH_EntityBullet(World par1World) {
        super(par1World);
    }

    public MCH_EntityBullet(
            World par1World, double pX, double pY, double pZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration
    ) {
        super(par1World, pX, pY, pZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.isDead && !this.world.isRemote && this.getCountOnUpdate() > 1 && this.getInfo() != null && this.explosionPower > 0) {
            float pDist = this.getInfo().proximityFuseDist;
            if (pDist > 0.1) {
                float rng = ++pDist + MathHelper.abs(this.getInfo().acceleration);
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(rng, rng, rng));

                for (Entity entity1 : list) {
                    if (this.canBeCollidedEntity(entity1) && entity1.getDistanceSq(this) < pDist * pDist) {
                        MCH_Lib.DbgLog(this.world, "MCH_EntityBullet.onUpdate:proximityFuse:" + entity1);
                        this.posX = (entity1.posX + this.posX) / 2.0;
                        this.posY = (entity1.posY + this.posY) / 2.0;
                        this.posZ = (entity1.posZ + this.posZ) / 2.0;
                        RayTraceResult mop = W_MovingObjectPosition.newMOP(
                                (int) this.posX, (int) this.posY, (int) this.posZ, 0, W_WorldFunc.getWorldVec3EntityPos(this), false
                        );
                        this.onImpact(mop, 1.0F);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onUpdateCollided() {
        double mx = this.motionX * this.accelerationFactor;
        double my = this.motionY * this.accelerationFactor;
        double mz = this.motionZ * this.accelerationFactor;
        float damageFactor = 1.0F;
        RayTraceResult m = null;

        for (int i = 0; i < 5; i++) {
            Vec3d vec3 = W_WorldFunc.getWorldVec3(this.world, this.posX, this.posY, this.posZ);
            Vec3d vec31 = W_WorldFunc.getWorldVec3(this.world, this.posX + mx, this.posY + my, this.posZ + mz);
            m = W_WorldFunc.clip(this.world, vec3, vec31);
            boolean continueClip = false;
            if (this.shootingEntity != null && W_MovingObjectPosition.isHitTypeTile(m)) {
                Block block = W_WorldFunc.getBlock(this.world, m.getBlockPos());
                if (MCH_Config.bulletBreakableBlocks.contains(block)) {
                    W_WorldFunc.destroyBlock(this.world, m.getBlockPos(), true);
                    continueClip = true;
                }
            }

            if (!continueClip) {
                break;
            }
        }

        Vec3d vec3x = W_WorldFunc.getWorldVec3(this.world, this.posX, this.posY, this.posZ);
        Vec3d vec31x = W_WorldFunc.getWorldVec3(this.world, this.posX + mx, this.posY + my, this.posZ + mz);
        if (this.getInfo().delayFuse > 0) {
            if (m != null) {
                this.boundBullet(m.sideHit);
                if (this.delayFuse == 0) {
                    this.delayFuse = this.getInfo().delayFuse;
                }
            }
        } else {
            if (m != null) {
                vec31x = W_WorldFunc.getWorldVec3(this.world, m.hitVec.x, m.hitVec.y, m.hitVec.z);
            }

            Entity entity = null;
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(mx, my, mz).grow(21.0, 21.0, 21.0));
            double d0 = 0.0;

            for (Entity entity1 : list) {
                if (this.canBeCollidedEntity(entity1)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(f, f, f);
                    RayTraceResult m1 = axisalignedbb.calculateIntercept(vec3x, vec31x);
                    if (m1 != null) {
                        double d1 = vec3x.distanceTo(m1.hitVec);
                        if (d1 < d0 || d0 == 0.0) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                m = new RayTraceResult(entity);
            }

            if (m != null) {
                this.onImpact(m, damageFactor);
            }
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.Bullet;
    }
}
