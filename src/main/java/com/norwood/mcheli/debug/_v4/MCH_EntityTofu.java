package com.norwood.mcheli.debug._v4;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MCH_EntityTofu extends Entity {
   public MCH_EntityTofu(World worldIn) {
      super(worldIn);
   }

   public MCH_EntityTofu(World world, double x, double y, double z) {
      this(world);
      this.func_70107_b(x, y, z);
   }

   protected void entityInit() {
      this.func_189654_d(true);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      this.func_70091_d(MoverType.SELF, 1.0D, 0.0D, 0.0D);
      if (!this.world.isRemote && this.field_70173_aa > 100) {
         this.func_70106_y();
      }

   }

   public boolean func_70067_L() {
      return false;
   }

   public void func_70108_f(Entity entityIn) {
   }

   protected void func_70037_a(NBTTagCompound compound) {
   }

   protected void func_70014_b(NBTTagCompound compound) {
   }
}
