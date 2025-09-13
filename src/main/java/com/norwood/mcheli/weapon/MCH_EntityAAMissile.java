package com.norwood.mcheli.weapon;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.flare.MCH_EntityChaff;
import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.vecmath.Vector3f;
import java.util.List;

public class MCH_EntityAAMissile extends MCH_EntityBaseBullet {
    public MCH_EntityAAMissile(World par1World) {
        super(par1World);
        this.targetEntity = null;
    }

    public MCH_EntityAAMissile(
            World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration
    ) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.getCountOnUpdate() > 4 && this.getInfo() != null && !this.getInfo().disableSmoke) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 7.0F * this.getInfo().smokeSize * 0.5F);
        }

        if(!super.world.isRemote && this.getInfo() != null) {
            if(super.shootingEntity != null && super.targetEntity != null && !super.targetEntity.isDead) {
                double x = super.posX - super.targetEntity.posX;
                double y = super.posY - super.targetEntity.posY;
                double z = super.posZ - super.targetEntity.posZ;
                double d = x * x + y * y + z * z;
                if(d > 3422500.0D) {
                    this.setDead();
                } else if(this.getCountOnUpdate() > this.getInfo().rigidityTime) {
                    if(this.getInfo().proximityFuseDist >= 0.1F && d * d < (double)this.getInfo().proximityFuseDist) {
                        RayTraceResult  mop = new RayTraceResult(super.targetEntity);
                        super.posX = (super.targetEntity.posX + super.posX) / 2.0D;
                        super.posY = (super.targetEntity.posY + super.posY) / 2.0D;
                        super.posZ = (super.targetEntity.posZ + super.posZ) / 2.0D;
                        this.onImpact(mop, 1.0F);
                    } else {
                        this.guidanceToTarget(super.targetEntity.posX, super.targetEntity.posY, super.targetEntity.posZ);
                    }
                }
            } else {
                if(getInfo().activeRadar && ticksExisted % getInfo().scanInterval == 0) {
                    scanForTargets();
                }
            }
        }

    }


    private void scanForTargets() {
        // Build missile direction vector
        Vector3f missileDirection = new Vector3f((float) this.motionX, (float) this.motionY, (float) this.motionZ);
        double range = getInfo().maxLockOnRange;

        // Get all entities in a cubic search area around the missile
        List<Entity> list = world.getEntitiesWithinAABB(
                Entity.class,
                new AxisAlignedBB(
                        posX - range, posY - range, posZ - range,
                        posX + range, posY + range, posZ + range
                )
        );

        if (!list.isEmpty()) {
            double closestAngle = Double.MAX_VALUE;
            Entity closestTarget = null;

            for (Entity entity : list) {
                if (entity instanceof MCH_EntityAircraft || entity instanceof MCH_EntityChaff) {

                    // Don’t lock onto the shooter’s own aircraft
                    if (W_Entity.isEqual(entity, shootingAircraft)) {
                        continue;
                    }

                    // Skip grounded entities
                    boolean isTargetOnGround = MCH_WeaponGuidanceSystem.isEntityOnGround(entity);
                    if (isTargetOnGround) {
                        continue;
                    }

                    // Direction from missile to target
                    double dx = entity.posX - this.posX;
                    double dy = entity.posY - this.posY;
                    double dz = entity.posZ - this.posZ;
                    Vector3f targetDirection = new Vector3f((float) dx, (float) dy, (float) dz);

                    // Angle between missile trajectory and target
                    double angle = Math.abs(missileDirection.angle(targetDirection));

                    // Skip if outside lock-on cone
                    if (angle > Math.toRadians(getInfo().maxLockOnAngle)) {
                        continue;
                    }

                    // Pick closest (smallest angle) target
                    if (angle < closestAngle) {
                        closestAngle = angle;
                        closestTarget = entity;
                    }
                }
            }

            if (closestTarget != null) {
                this.targetEntity = closestTarget;
            }
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.AAMissile;
    }
}
