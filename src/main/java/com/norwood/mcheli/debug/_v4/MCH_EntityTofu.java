package com.norwood.mcheli.debug._v4;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MCH_EntityTofu extends Entity {
    public MCH_EntityTofu(World worldIn) {
        super(worldIn);
    }

    public MCH_EntityTofu(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y, z);
    }

    protected void entityInit() {
        this.setNoGravity(true);
    }

    public void onUpdate() {
        super.onUpdate();
        this.move(MoverType.SELF, 1.0, 0.0, 0.0);
        if (!this.world.isRemote && this.ticksExisted > 100) {
            this.setDead();
        }
    }

    public void applyEntityCollision(@NotNull Entity entityIn) {
    }

    protected void readEntityFromNBT(@NotNull NBTTagCompound compound) {
    }

    protected void writeEntityToNBT(@NotNull NBTTagCompound compound) {
    }
}
