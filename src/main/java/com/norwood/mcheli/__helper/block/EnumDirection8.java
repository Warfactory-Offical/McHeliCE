package com.norwood.mcheli.__helper.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public enum EnumDirection8 implements IStringSerializable {
   SOUTH(0, 4, "south", new Vec3i(0, 0, 1)),
   SOUTHWEST(1, 5, "southwest", new Vec3i(-1, 0, 1)),
   WEST(2, 6, "west", new Vec3i(-1, 0, 0)),
   NORTHWEST(3, 7, "northwest", new Vec3i(-1, 0, -1)),
   NORTH(4, 0, "north", new Vec3i(0, 0, -1)),
   NORTHEAST(5, 1, "northeast", new Vec3i(1, 0, -1)),
   EAST(6, 2, "east", new Vec3i(1, 0, 0)),
   SOUTHEAST(7, 3, "southeast", new Vec3i(1, 0, 1));

   private final int index;
   private final int opposite;
   private final String name;
   private final Vec3i directionVec;
   public static final EnumDirection8[] VALUES = new EnumDirection8[8];

   private EnumDirection8(int index, int opposite, String name, Vec3i dirVec) {
      this.index = index;
      this.opposite = opposite;
      this.name = name;
      this.directionVec = dirVec;
   }

   public int getIndex() {
      return this.index;
   }

   public String func_176610_l() {
      return this.name;
   }

   public EnumDirection8 opposite() {
      return getFront(this.opposite);
   }

   public Vec3i getDirectionVec() {
      return this.directionVec;
   }

   public float getAngle() {
      return (float)((this.index & 7) * 45);
   }

   public static EnumDirection8 getFront(int index) {
      return VALUES[MathHelper.func_76130_a(index % VALUES.length)];
   }

   public static EnumDirection8 fromAngle(double angle) {
      return getFront(MathHelper.func_76128_c(angle / 45.0D + 0.5D) & 7);
   }

   static {
      EnumDirection8[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumDirection8 value = var0[var2];
         VALUES[value.index] = value;
      }

   }
}
