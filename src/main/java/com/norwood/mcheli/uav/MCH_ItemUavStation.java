package com.norwood.mcheli.uav;

import java.util.List;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.wrapper.W_Item;
import com.norwood.mcheli.wrapper.W_MovingObjectPosition;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_ItemUavStation extends W_Item {
   public static int UAV_STATION_KIND_NUM = 2;
   public final int UavStationKind;

   public MCH_ItemUavStation(int par1, int kind) {
      super(par1);
      this.field_77777_bU = 1;
      this.UavStationKind = kind;
   }

   public MCH_EntityUavStation createUavStation(World world, double x, double y, double z, int kind) {
      MCH_EntityUavStation uavst = new MCH_EntityUavStation(world);
      uavst.func_70107_b(x, y, z);
      uavst.field_70169_q = x;
      uavst.field_70167_r = y;
      uavst.field_70166_s = z;
      uavst.setKind(kind);
      return uavst;
   }

   public ActionResult<ItemStack> func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
      ItemStack itemstack = playerIn.func_184586_b(handIn);
      float f = 1.0F;
      float f1 = playerIn.field_70127_C + (playerIn.field_70125_A - playerIn.field_70127_C) * f;
      float f2 = playerIn.field_70126_B + (playerIn.field_70177_z - playerIn.field_70126_B) * f;
      double d0 = playerIn.field_70169_q + (playerIn.field_70165_t - playerIn.field_70169_q) * (double)f;
      double d1 = playerIn.field_70167_r + (playerIn.field_70163_u - playerIn.field_70167_r) * (double)f + 1.62D;
      double d2 = playerIn.field_70166_s + (playerIn.field_70161_v - playerIn.field_70166_s) * (double)f;
      Vec3d vec3 = W_WorldFunc.getWorldVec3(worldIn, d0, d1, d2);
      float f3 = MathHelper.func_76134_b(-f2 * 0.017453292F - 3.1415927F);
      float f4 = MathHelper.func_76126_a(-f2 * 0.017453292F - 3.1415927F);
      float f5 = -MathHelper.func_76134_b(-f1 * 0.017453292F);
      float f6 = MathHelper.func_76126_a(-f1 * 0.017453292F);
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = 5.0D;
      Vec3d vec31 = vec3.func_72441_c((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
      RayTraceResult movingobjectposition = W_WorldFunc.clip(worldIn, vec3, vec31, true);
      if (movingobjectposition == null) {
         return ActionResult.newResult(EnumActionResult.PASS, itemstack);
      } else {
         Vec3d vec32 = playerIn.func_70676_i(f);
         boolean flag = false;
         float f9 = 1.0F;
         List<Entity> list = worldIn.func_72839_b(playerIn, playerIn.func_174813_aQ().func_72321_a(vec32.field_72450_a * d3, vec32.field_72448_b * d3, vec32.field_72449_c * d3).func_72314_b((double)f9, (double)f9, (double)f9));

         int i;
         for(i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (entity.func_70067_L()) {
               float f10 = entity.func_70111_Y();
               AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)f10, (double)f10, (double)f10);
               if (axisalignedbb.func_72318_a(vec3)) {
                  flag = true;
               }
            }
         }

         if (flag) {
            return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
         } else {
            if (W_MovingObjectPosition.isHitTypeTile(movingobjectposition)) {
               i = movingobjectposition.func_178782_a().func_177958_n();
               int j = movingobjectposition.func_178782_a().func_177956_o();
               int k = movingobjectposition.func_178782_a().func_177952_p();
               MCH_EntityUavStation entityUavSt = this.createUavStation(worldIn, (double)((float)i + 0.5F), (double)((float)j + 1.0F), (double)((float)k + 0.5F), this.UavStationKind);
               int rot = (int)(MCH_Lib.getRotate360((double)playerIn.field_70177_z) + 45.0D);
               entityUavSt.field_70177_z = (float)(rot / 90 * 90 - 180);
               entityUavSt.initUavPostion();
               if (!worldIn.func_184144_a(entityUavSt, entityUavSt.func_174813_aQ().func_72314_b(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                  return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
               }

               if (!worldIn.field_72995_K) {
                  worldIn.func_72838_d(entityUavSt);
               }

               if (!playerIn.field_71075_bZ.field_75098_d) {
                  itemstack.func_190918_g(1);
               }
            }

            return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
         }
      }
   }
}
