package com.norwood.mcheli.particles;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBlockDust;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class MCH_EntityBlockDustFX extends ParticleBlockDust {
   protected MCH_EntityBlockDustFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IBlockState state) {
      super(world, x, y, z, xSpeed, ySpeed, zSpeed, state);
   }

   public void setScale(float s) {
      this.field_70544_f = s;
   }

   public static class Factory implements IParticleFactory {
      @Nullable
      public Particle func_178902_a(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
         IBlockState iblockstate = Block.func_176220_d(p_178902_15_[0]);
         return iblockstate.func_185901_i() == EnumBlockRenderType.INVISIBLE ? null : (new MCH_EntityBlockDustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, iblockstate)).func_174845_l();
      }
   }
}
