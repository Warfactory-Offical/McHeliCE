package com.norwood.mcheli.wrapper;

import net.minecraft.util.math.BlockPos;

public class W_ChunkPosition {
   public static int getChunkPosX(BlockPos c) {
      return c.func_177958_n();
   }

   public static int getChunkPosY(BlockPos c) {
      return c.func_177956_o();
   }

   public static int getChunkPosZ(BlockPos c) {
      return c.func_177952_p();
   }
}
