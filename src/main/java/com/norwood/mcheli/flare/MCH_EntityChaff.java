package com.norwood.mcheli.flare;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.norwood.mcheli.particles.MCH_ParticleParam;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.weapon.MCH_IEntityLockChecker;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_WorldFunc;

public class MCH_EntityChaff extends W_Entity implements MCH_IEntityLockChecker {

    public double gravity;
    public double airResistance;
    public static final int MAX_TICK_EXISTED = 200;

    public MCH_EntityChaff(World worldIn) {
        super(worldIn);
        this.gravity = -0.001D;
        this.airResistance = 0.99D;
        this.setSize(1.0F, 1.0F);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public MCH_EntityChaff(World worldIn, double pX, double pY, double pZ, double mX, double mY, double mZ) {
        this(worldIn);
        this.setLocationAndAngles(pX, pY, pZ, 0.0F, 0.0F);
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
    }

    @Override
    public void onUpdate() {
        if (world.isRemote) {
            this.setRenderDistanceWeight(500);
        }

        if (this.ticksExisted > MAX_TICK_EXISTED) {
            this.setDead();
            return;
        }

        if (!world.isRemote && !world.isBlockLoaded(new BlockPos(this.posX, this.posY, this.posZ))) {
            this.setDead();
            return;
        }

        super.onUpdate();

        if (!world.isRemote) {
            this.onUpdateCollided();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        this.motionY += this.gravity;
        this.motionX *= this.airResistance;
        this.motionZ *= this.airResistance;

        if (this.isInWater() && !world.isRemote) {
            this.setDead();
        }

        if (this.onGround && !world.isRemote) {
            this.setDead();
        }

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
        d1 *= 64.0D;
        return distance < d1 * d1;
    }

    @Override
    public boolean canLockEntity(Entity var1) {
        return false;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public float getCollisionBorderSize() {
        return 1.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    protected void onUpdateCollided() {
        Vec3d vec3 = new Vec3d(posX, posY, posZ);
        Vec3d vec31 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

        RayTraceResult mop = world.rayTraceBlocks(vec3, vec31, false, true, false);

        if (mop != null) {
            Vec3d hitVec = mop.hitVec;
            this.onImpact(mop);
        }
    }

    private void onImpact(RayTraceResult mop) {
        if (!world.isRemote) {
            this.setDead();
        }
    }
}
