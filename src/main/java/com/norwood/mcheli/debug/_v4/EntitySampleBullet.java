package com.norwood.mcheli.debug._v4;

import com.norwood.mcheli.weapon.MCH_EntityRocket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySampleBullet extends MCH_EntityRocket {
   private Vec3d firstPos;

   public EntitySampleBullet(World par1World) {
      super(par1World, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0F, 0.0F, 4.0);
      this.setName("hydra70");
      this.setSize(1.0F, 1.0F);
      this.explosionPower = 3;
      this.setPower(22);
      this.firstPos = Vec3d.ZERO;
      this.accelerationFactor = 1.0;
      this.delayFuse = 100;
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
      this.motionX = 1.0;
   }

   @Override
   public boolean checkValid() {
      double x = this.posX - this.firstPos.x;
      double z = this.posZ - this.firstPos.z;
      return x * x + z * z < 3.38724E7 && this.posY > -10.0;
   }
}
