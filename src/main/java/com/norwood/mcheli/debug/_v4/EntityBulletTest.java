package com.norwood.mcheli.debug._v4;

import com.norwood.mcheli.weapon.MCH_EntityBullet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBulletTest extends MCH_EntityBullet {
   private Vec3d firstPos;

   public EntityBulletTest(World par1World) {
      super(par1World);
      this.setName("m230");
      this.func_70105_a(1.0F, 1.0F);
      this.explosionPower = 3;
      this.setPower(22);
      this.firstPos = Vec3d.ZERO;
      this.acceleration = 4.0D;
      this.explosionPower = 1;
      this.delayFuse = 100;
   }

   public void func_70037_a(NBTTagCompound par1nbtTagCompound) {
      this.field_70159_w = 1.0D;
   }

   public boolean checkValid() {
      double x = this.posX - this.firstPos.x;
      double z = this.posZ - this.firstPos.z;
      return x * x + z * z < 3.38724E7D && this.posY > -10.0D;
   }
}
