package com.norwood.mcheli.debug._v4;

import com.norwood.mcheli.weapon.MCH_EntityRocket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySampleBullet extends MCH_EntityRocket {
   private Vec3d firstPos;

   public EntitySampleBullet(World par1World) {
      super(par1World, 0.0D, 0.0D, 0.0D, 2.0D, 0.0D, 0.0D, 0.0F, 0.0F, 4.0D);
      this.setName("hydra70");
      this.func_70105_a(1.0F, 1.0F);
      this.explosionPower = 3;
      this.setPower(22);
      this.firstPos = Vec3d.field_186680_a;
      this.accelerationFactor = 1.0D;
      this.delayFuse = 100;
   }

   public void func_70037_a(NBTTagCompound par1nbtTagCompound) {
      this.field_70159_w = 1.0D;
   }

   public boolean checkValid() {
      double x = this.field_70165_t - this.firstPos.field_72450_a;
      double z = this.field_70161_v - this.firstPos.field_72449_c;
      return x * x + z * z < 3.38724E7D && this.field_70163_u > -10.0D;
   }
}
