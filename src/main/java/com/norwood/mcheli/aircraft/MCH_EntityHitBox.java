package com.norwood.mcheli.aircraft;

import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityHitBox extends W_Entity {
   public MCH_EntityAircraft parent;
   public int debugId;

   public MCH_EntityHitBox(World world) {
      super(world);
      this.func_70105_a(1.0F, 1.0F);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.parent = null;
      this.field_70158_ak = true;
      this.field_70178_ae = true;
   }

   public MCH_EntityHitBox(World world, MCH_EntityAircraft ac, float w, float h) {
      this(world);
      this.func_70107_b(ac.field_70165_t, ac.field_70163_u + 1.0D, ac.field_70161_v);
      this.field_70169_q = ac.field_70165_t;
      this.field_70167_r = ac.field_70163_u + 1.0D;
      this.field_70166_s = ac.field_70161_v;
      this.parent = ac;
      this.func_70105_a(w, h);
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public AxisAlignedBB func_70114_g(Entity par1Entity) {
      return par1Entity.func_174813_aQ();
   }

   public AxisAlignedBB func_70046_E() {
      return this.func_174813_aQ();
   }

   public boolean func_70104_M() {
      return false;
   }

   public double func_70042_X() {
      return -0.3D;
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      return this.parent != null ? this.parent.func_70097_a(par1DamageSource, par2) : false;
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   public void func_70106_y() {
      super.func_70106_y();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
   }

   protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
   }

   protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
      return this.parent != null ? this.parent.func_184230_a(player, hand) : false;
   }
}
