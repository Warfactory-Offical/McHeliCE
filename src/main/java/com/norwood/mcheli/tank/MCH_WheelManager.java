package com.norwood.mcheli.tank;

import java.util.List;
import java.util.Random;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Blocks;
import com.norwood.mcheli.wrapper.W_Lib;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WheelManager {
   public final MCH_EntityAircraft parent;
   public MCH_EntityWheel[] wheels;
   private double minZ;
   private double maxZ;
   private double avgZ;
   public Vec3d weightedCenter;
   public float targetPitch;
   public float targetRoll;
   public float prevYaw;
   private static Random rand = new Random();

   public MCH_WheelManager(MCH_EntityAircraft ac) {
      this.parent = ac;
      this.wheels = new MCH_EntityWheel[0];
      this.weightedCenter = Vec3d.field_186680_a;
   }

   public void createWheels(World w, List<MCH_AircraftInfo.Wheel> list, Vec3d weightedCenter) {
      this.wheels = new MCH_EntityWheel[list.size() * 2];
      this.minZ = 999999.0D;
      this.maxZ = -999999.0D;
      this.weightedCenter = weightedCenter;

      for(int i = 0; i < this.wheels.length; ++i) {
         MCH_EntityWheel wheel = new MCH_EntityWheel(w);
         wheel.setParents(this.parent);
         Vec3d wp = ((MCH_AircraftInfo.Wheel)list.get(i / 2)).pos;
         wheel.setWheelPos(new Vec3d(i % 2 == 0 ? wp.field_72450_a : -wp.field_72450_a, wp.field_72448_b, wp.field_72449_c), this.weightedCenter);
         Vec3d v = this.parent.getTransformedPosition(wheel.pos.field_72450_a, wheel.pos.field_72448_b, wheel.pos.field_72449_c);
         wheel.func_70012_b(v.field_72450_a, v.field_72448_b + 1.0D, v.field_72449_c, 0.0F, 0.0F);
         this.wheels[i] = wheel;
         if (wheel.pos.field_72449_c <= this.minZ) {
            this.minZ = wheel.pos.field_72449_c;
         }

         if (wheel.pos.field_72449_c >= this.maxZ) {
            this.maxZ = wheel.pos.field_72449_c;
         }
      }

      this.avgZ = this.maxZ - this.minZ;
   }

   public void move(double x, double y, double z) {
      MCH_EntityAircraft ac = this.parent;
      if (ac.getAcInfo() != null) {
         boolean showLog = ac.field_70173_aa % 1 == 1;
         if (showLog) {
            MCH_Lib.DbgLog(ac.field_70170_p, "[" + (ac.field_70170_p.field_72995_K ? "Client" : "Server") + "] ==============================");
         }

         MCH_EntityWheel[] var9 = this.wheels;
         int i = var9.length;

         int var11;
         MCH_EntityWheel w2;
         for(var11 = 0; var11 < i; ++var11) {
            w2 = var9[var11];
            w2.field_70169_q = w2.field_70165_t;
            w2.field_70167_r = w2.field_70163_u;
            w2.field_70166_s = w2.field_70161_v;
            Vec3d v = ac.getTransformedPosition(w2.pos.field_72450_a, w2.pos.field_72448_b, w2.pos.field_72449_c);
            w2.field_70159_w = v.field_72450_a - w2.field_70165_t + x;
            w2.field_70181_x = v.field_72448_b - w2.field_70163_u;
            w2.field_70179_y = v.field_72449_c - w2.field_70161_v + z;
         }

         var9 = this.wheels;
         i = var9.length;

         for(var11 = 0; var11 < i; ++var11) {
            w2 = var9[var11];
            w2.field_70181_x *= 0.15D;
            w2.func_70091_d(MoverType.SELF, w2.field_70159_w, w2.field_70181_x, w2.field_70179_y);
            double f = 1.0D;
            w2.func_70091_d(MoverType.SELF, 0.0D, -0.1D * f, 0.0D);
         }

         int zmog = -1;

         MCH_EntityWheel w1;
         for(i = 0; i < this.wheels.length / 2; ++i) {
            zmog = i;
            w1 = this.wheels[i * 2 + 0];
            w2 = this.wheels[i * 2 + 1];
            if (!w1.isPlus && (w1.field_70122_E || w2.field_70122_E)) {
               zmog = -1;
               break;
            }
         }

         if (zmog >= 0) {
            this.wheels[zmog * 2 + 0].field_70122_E = true;
            this.wheels[zmog * 2 + 1].field_70122_E = true;
         }

         zmog = -1;

         for(i = this.wheels.length / 2 - 1; i >= 0; --i) {
            zmog = i;
            w1 = this.wheels[i * 2 + 0];
            w2 = this.wheels[i * 2 + 1];
            if (w1.isPlus && (w1.field_70122_E || w2.field_70122_E)) {
               zmog = -1;
               break;
            }
         }

         if (zmog >= 0) {
            this.wheels[zmog * 2 + 0].field_70122_E = true;
            this.wheels[zmog * 2 + 1].field_70122_E = true;
         }

         Vec3d rv = Vec3d.field_186680_a;
         Vec3d wc = ac.getTransformedPosition(this.weightedCenter);
         wc = new Vec3d(wc.field_72450_a - ac.field_70165_t, this.weightedCenter.field_72448_b, wc.field_72449_c - ac.field_70161_v);

         for(int i = 0; i < this.wheels.length / 2; ++i) {
            MCH_EntityWheel w1 = this.wheels[i * 2 + 0];
            MCH_EntityWheel w2 = this.wheels[i * 2 + 1];
            Vec3d v1 = new Vec3d(w1.field_70165_t - (ac.field_70165_t + wc.field_72450_a), w1.field_70163_u - (ac.field_70163_u + wc.field_72448_b), w1.field_70161_v - (ac.field_70161_v + wc.field_72449_c));
            Vec3d v2 = new Vec3d(w2.field_70165_t - (ac.field_70165_t + wc.field_72450_a), w2.field_70163_u - (ac.field_70163_u + wc.field_72448_b), w2.field_70161_v - (ac.field_70161_v + wc.field_72449_c));
            Vec3d v = w1.pos.field_72449_c >= 0.0D ? v2.func_72431_c(v1) : v1.func_72431_c(v2);
            v = v.func_72432_b();
            double f = Math.abs(w1.pos.field_72449_c / this.avgZ);
            if (!w1.field_70122_E && !w2.field_70122_E) {
               f = 0.0D;
            }

            rv = rv.func_72441_c(v.field_72450_a * f, v.field_72448_b * f, v.field_72449_c * f);
            if (showLog) {
               v = v.func_178785_b((float)((double)ac.getRotYaw() * 3.141592653589793D / 180.0D));
               MCH_Lib.DbgLog(ac.field_70170_p, "%2d : %.2f :[%+.1f, %+.1f, %+.1f][%s %d %d][%+.2f(%+.2f), %+.2f(%+.2f)][%+.1f, %+.1f, %+.1f]", i, f, v.field_72450_a, v.field_72448_b, v.field_72449_c, w1.isPlus ? "+" : "-", w1.field_70122_E ? 1 : 0, w2.field_70122_E ? 1 : 0, w1.field_70163_u - w1.field_70167_r, w1.field_70181_x, w2.field_70163_u - w2.field_70167_r, w2.field_70181_x, v.field_72450_a, v.field_72448_b, v.field_72449_c);
            }
         }

         rv = rv.func_72432_b();
         if (rv.field_72448_b > 0.01D && rv.field_72448_b < 0.7D) {
            ac.field_70159_w += rv.field_72450_a / 50.0D;
            ac.field_70179_y += rv.field_72449_c / 50.0D;
         }

         rv = rv.func_178785_b((float)((double)ac.getRotYaw() * 3.141592653589793D / 180.0D));
         float pitch = (float)(90.0D - Math.atan2(rv.field_72448_b, rv.field_72449_c) * 180.0D / 3.141592653589793D);
         float roll = -((float)(90.0D - Math.atan2(rv.field_72448_b, rv.field_72450_a) * 180.0D / 3.141592653589793D));
         float ogpf = ac.getAcInfo().onGroundPitchFactor;
         if (pitch - ac.getRotPitch() > ogpf) {
            pitch = ac.getRotPitch() + ogpf;
         }

         if (pitch - ac.getRotPitch() < -ogpf) {
            pitch = ac.getRotPitch() - ogpf;
         }

         float ogrf = ac.getAcInfo().onGroundRollFactor;
         if (roll - ac.getRotRoll() > ogrf) {
            roll = ac.getRotRoll() + ogrf;
         }

         if (roll - ac.getRotRoll() < -ogrf) {
            roll = ac.getRotRoll() - ogrf;
         }

         this.targetPitch = pitch;
         this.targetRoll = roll;
         if (!W_Lib.isClientPlayer(ac.getRiddenByEntity())) {
            ac.setRotPitch(pitch);
            ac.setRotRoll(roll);
         }

         if (showLog) {
            MCH_Lib.DbgLog(ac.field_70170_p, "%+03d, %+03d :[%.2f, %.2f, %.2f] yaw=%.2f, pitch=%.2f, roll=%.2f", (int)pitch, (int)roll, rv.field_72450_a, rv.field_72448_b, rv.field_72449_c, ac.getRotYaw(), this.targetPitch, this.targetRoll);
         }

         MCH_EntityWheel[] var36 = this.wheels;
         int var37 = var36.length;

         for(int var38 = 0; var38 < var37; ++var38) {
            MCH_EntityWheel wheel = var36[var38];
            Vec3d v = this.getTransformedPosition(wheel.pos.field_72450_a, wheel.pos.field_72448_b, wheel.pos.field_72449_c, ac, ac.getRotYaw(), this.targetPitch, this.targetRoll);
            double rangeH = 2.0D;
            double poy = (double)(wheel.field_70138_W / 2.0F);
            if (wheel.field_70165_t > v.field_72450_a + rangeH) {
               wheel.field_70165_t = v.field_72450_a + rangeH;
               wheel.field_70163_u = v.field_72448_b + poy;
            }

            if (wheel.field_70165_t < v.field_72450_a - rangeH) {
               wheel.field_70165_t = v.field_72450_a - rangeH;
               wheel.field_70163_u = v.field_72448_b + poy;
            }

            if (wheel.field_70161_v > v.field_72449_c + rangeH) {
               wheel.field_70161_v = v.field_72449_c + rangeH;
               wheel.field_70163_u = v.field_72448_b + poy;
            }

            if (wheel.field_70161_v < v.field_72449_c - rangeH) {
               wheel.field_70161_v = v.field_72449_c - rangeH;
               wheel.field_70163_u = v.field_72448_b + poy;
            }

            wheel.func_70080_a(wheel.field_70165_t, wheel.field_70163_u, wheel.field_70161_v, 0.0F, 0.0F);
         }

      }
   }

   public Vec3d getTransformedPosition(double x, double y, double z, MCH_EntityAircraft ac, float yaw, float pitch, float roll) {
      Vec3d v = MCH_Lib.RotVec3(x, y, z, -yaw, -pitch, -roll);
      return v.func_72441_c(ac.field_70165_t, ac.field_70163_u, ac.field_70161_v);
   }

   public void updateBlock() {
      if (MCH_Config.Collision_DestroyBlock.prmBool) {
         MCH_EntityAircraft ac = this.parent;
         MCH_EntityWheel[] var2 = this.wheels;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            MCH_EntityWheel w = var2[var4];
            Vec3d v = ac.getTransformedPosition(w.pos);
            int x = (int)(v.field_72450_a + 0.5D);
            int y = (int)(v.field_72448_b + 0.5D);
            int z = (int)(v.field_72449_c + 0.5D);
            BlockPos blockpos = new BlockPos(x, y, z);
            IBlockState iblockstate = ac.field_70170_p.func_180495_p(blockpos);
            if (iblockstate.func_177230_c() == W_Block.getSnowLayer()) {
               ac.field_70170_p.func_175698_g(blockpos);
            }

            if (iblockstate.func_177230_c() == W_Blocks.field_150392_bi || iblockstate.func_177230_c() == W_Blocks.field_150414_aQ) {
               W_WorldFunc.destroyBlock(ac.field_70170_p, x, y, z, false);
            }
         }

      }
   }

   public void particleLandingGear() {
      if (this.wheels.length > 0) {
         MCH_EntityAircraft ac = this.parent;
         double d = ac.field_70159_w * ac.field_70159_w + ac.field_70179_y * ac.field_70179_y + (double)Math.abs(this.prevYaw - ac.getRotYaw());
         this.prevYaw = ac.getRotYaw();
         if (d > 0.001D) {
            for(int i = 0; i < 2; ++i) {
               MCH_EntityWheel w = this.wheels[rand.nextInt(this.wheels.length)];
               Vec3d v = ac.getTransformedPosition(w.pos);
               int x = MathHelper.func_76128_c(v.field_72450_a + 0.5D);
               int y = MathHelper.func_76128_c(v.field_72448_b - 0.5D);
               int z = MathHelper.func_76128_c(v.field_72449_c + 0.5D);
               BlockPos blockpos = new BlockPos(x, y, z);
               IBlockState iblockstate = ac.field_70170_p.func_180495_p(blockpos);
               if (Block.func_149680_a(iblockstate.func_177230_c(), Blocks.field_150350_a)) {
                  y = MathHelper.func_76128_c(v.field_72448_b + 0.5D);
                  blockpos = new BlockPos(x, y, z);
                  iblockstate = ac.field_70170_p.func_180495_p(blockpos);
               }

               if (!Block.func_149680_a(iblockstate.func_177230_c(), Blocks.field_150350_a)) {
                  MCH_ParticlesUtil.spawnParticleTileCrack(ac.field_70170_p, x, y, z, v.field_72450_a + ((double)rand.nextFloat() - 0.5D), v.field_72448_b + 0.1D, v.field_72449_c + ((double)rand.nextFloat() - 0.5D), -ac.field_70159_w * 4.0D + ((double)rand.nextFloat() - 0.5D) * 0.1D, (double)rand.nextFloat() * 0.5D, -ac.field_70179_y * 4.0D + ((double)rand.nextFloat() - 0.5D) * 0.1D);
               }
            }
         }

      }
   }
}
