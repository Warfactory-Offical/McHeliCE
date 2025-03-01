package com.norwood.mcheli.throwable;

import com.norwood.mcheli.MCH_Lib;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

public class MCH_ItemThrowableDispenseBehavior extends BehaviorDefaultDispenseItem {
   public ItemStack func_82487_b(IBlockSource bs, ItemStack itemStack) {
      EnumFacing enumfacing = (EnumFacing)bs.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
      double x = bs.func_82615_a() + (double)enumfacing.func_82601_c() * 2.0D;
      double y = bs.func_82617_b() + (double)enumfacing.func_96559_d() * 2.0D;
      double z = bs.func_82616_c() + (double)enumfacing.func_82599_e() * 2.0D;
      if (itemStack.func_77973_b() instanceof MCH_ItemThrowable) {
         MCH_ThrowableInfo info = MCH_ThrowableInfoManager.get(itemStack.func_77973_b());
         if (info != null) {
            bs.func_82618_k().func_184134_a(x, y, z, SoundEvents.field_187737_v, SoundCategory.BLOCKS, 0.5F, 0.4F / (bs.func_82618_k().field_73012_v.nextFloat() * 0.4F + 0.8F), false);
            if (!bs.func_82618_k().field_72995_K) {
               MCH_Lib.DbgLog(bs.func_82618_k(), "MCH_ItemThrowableDispenseBehavior.dispenseStack(%s)", info.name);
               MCH_EntityThrowable entity = new MCH_EntityThrowable(bs.func_82618_k(), x, y, z);
               entity.field_70159_w = (double)((float)enumfacing.func_82601_c() * info.dispenseAcceleration);
               entity.field_70181_x = (double)((float)enumfacing.func_96559_d() * info.dispenseAcceleration);
               entity.field_70179_y = (double)((float)enumfacing.func_82599_e() * info.dispenseAcceleration);
               entity.setInfo(info);
               bs.func_82618_k().func_72838_d(entity);
               itemStack.func_77979_a(1);
            }
         }
      }

      return itemStack;
   }
}
