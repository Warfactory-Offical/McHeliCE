package com.norwood.mcheli.gltd;

import java.util.List;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_ItemGLTD extends W_Item {
   public MCH_ItemGLTD(int par1) {
      super(par1);
      this.field_77777_bU = 1;
   }

   public ActionResult<ItemStack> func_77659_a(World par2World, EntityPlayer par3EntityPlayer, EnumHand handIn) {
      ItemStack itemstack = par3EntityPlayer.func_184586_b(handIn);
      float f = 1.0F;
      float f1 = par3EntityPlayer.field_70127_C + (par3EntityPlayer.field_70125_A - par3EntityPlayer.field_70127_C) * f;
      float f2 = par3EntityPlayer.field_70126_B + (par3EntityPlayer.field_70177_z - par3EntityPlayer.field_70126_B) * f;
      double d0 = par3EntityPlayer.field_70169_q + (par3EntityPlayer.posX - par3EntityPlayer.field_70169_q) * (double)f;
      double d1 = par3EntityPlayer.field_70167_r + (par3EntityPlayer.posY - par3EntityPlayer.field_70167_r) * (double)f + (double)par3EntityPlayer.func_70047_e();
      double d2 = par3EntityPlayer.field_70166_s + (par3EntityPlayer.posZ - par3EntityPlayer.field_70166_s) * (double)f;
      Vec3d vec3 = W_WorldFunc.getWorldVec3(par2World, d0, d1, d2);
      float f3 = MathHelper.func_76134_b(-f2 * 0.017453292F - 3.1415927F);
      float f4 = MathHelper.func_76126_a(-f2 * 0.017453292F - 3.1415927F);
      float f5 = -MathHelper.func_76134_b(-f1 * 0.017453292F);
      float f6 = MathHelper.func_76126_a(-f1 * 0.017453292F);
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = 5.0D;
      Vec3d vec31 = vec3.func_72441_c((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
      RayTraceResult movingobjectposition = W_WorldFunc.clip(par2World, vec3, vec31, true);
      if (movingobjectposition == null) {
         return ActionResult.newResult(EnumActionResult.PASS, itemstack);
      } else {
         Vec3d vec32 = par3EntityPlayer.func_70676_i(f);
         boolean flag = false;
         float f9 = 1.0F;
         List<Entity> list = par2World.func_72839_b(par3EntityPlayer, par3EntityPlayer.func_174813_aQ().func_72321_a(vec32.x * d3, vec32.y * d3, vec32.z * d3).func_72314_b((double)f9, (double)f9, (double)f9));

         for(int i = 0; i < list.size(); ++i) {
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
               BlockPos blockpos = movingobjectposition.func_178782_a();
               int i = blockpos.func_177958_n();
               int j = blockpos.func_177956_o();
               int k = blockpos.func_177952_p();
               MCH_EntityGLTD entityboat = new MCH_EntityGLTD(par2World, (double)((float)i + 0.5F), (double)((float)j + 1.0F), (double)((float)k + 0.5F));
               entityboat.field_70177_z = par3EntityPlayer.field_70177_z;
               if (!par2World.func_184144_a(entityboat, entityboat.func_174813_aQ().func_72314_b(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                  return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
               }

               if (!par2World.isRemote) {
                  par2World.func_72838_d(entityboat);
               }

               if (!par3EntityPlayer.field_71075_bZ.field_75098_d) {
                  itemstack.func_190918_g(1);
               }
            }

            return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
         }
      }
   }
}
