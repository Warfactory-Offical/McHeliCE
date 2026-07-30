package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Explosion;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponBomb extends MCH_WeaponBase {

    public MCH_WeaponBomb(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.acceleration = 0.5F;
        this.explosionPower = 9;
        this.power = 35;
        this.interval = -90;
        if (w.isRemote) {
            this.interval -= 10;
        }
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        if (this.getInfo() != null && this.getInfo().destruct) {
            if (prm.entity instanceof MCH_EntityAircraft ac) {
                if (ac.isUAV()) {
                    if (!this.world.isRemote) {
                        MCH_WeaponInfo info = this.getInfo();
                        float size = info.explosion;
                        float sizeBlock = size;
                        float damagePower = info.explosion;
                        float blockPower = info.explosionBlock;
                        float damageRadius = size;

                        if (info.isNewExplosionBreak) {
                            size = info.explosionRadius > 0.0F ? info.explosionRadius : size;
                            sizeBlock = size;
                            blockPower = info.explosionBlock >= 0 ? info.explosionBlock : sizeBlock;
                        }

                        if (info.isNewExplosionBreak && info.explosionDamageRadius > 0.0F) {
                            damageRadius = info.explosionDamageRadius;
                        } else {
                            damageRadius = size;
                        }

                        MCH_Explosion.newExplosion(
                                this.world,
                                null,
                                prm.user,
                                ac.posX,
                                ac.posY,
                                ac.posZ,
                                size,
                                sizeBlock,
                                damagePower,
                                blockPower,
                                damageRadius,
                                true,
                                true,
                                info.flaming,
                                true,
                                0);
                        this.playSound(prm.entity);
                    }

                    ac.destruct();
                }
            }
        } else if (!this.world.isRemote) {
            this.playSound(prm.entity);
            MCH_EntityBomb e = new MCH_EntityBomb(
                    this.world,
                    prm.posX,
                    prm.posY,
                    prm.posZ,
                    prm.entity.motionX,
                    prm.entity.motionY,
                    prm.entity.motionZ,
                    prm.entity.rotationYaw,
                    0.0F,
                    this.acceleration);
            e.setName(this.name);
            e.setParameterFromWeapon(this, prm.entity, prm.user);
            e.motionX = prm.entity.motionX;
            e.motionY = prm.entity.motionY;
            e.motionZ = prm.entity.motionZ;
            this.world.spawnEntity(e);
        }

        return true;
    }
}
