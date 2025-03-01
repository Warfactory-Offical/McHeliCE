package com.norwood.mcheli.__helper.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;

public interface IEntitySinglePassenger {
   @Nullable
   Entity getRiddenByEntity();
}
