package com.norwood.mcheli.particles;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.__helper.entity.ITargetMarkerObject;
import com.norwood.mcheli.multiplay.MCH_GuiTargetMarker;
import com.norwood.mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class MCH_EntityParticleMarkPoint extends MCH_EntityParticleBase implements ITargetMarkerObject {
   final Team taem;

   public MCH_EntityParticleMarkPoint(World par1World, double x, double y, double z, Team team) {
      super(par1World, x, y, z, 0.0D, 0.0D, 0.0D);
      this.setParticleMaxAge(30);
      this.taem = team;
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      if (player == null) {
         this.func_187112_i();
      } else if (player.func_96124_cp() == null && this.taem != null) {
         this.func_187112_i();
      } else if (player.func_96124_cp() != null && !player.func_184194_a(this.taem)) {
         this.func_187112_i();
      }

   }

   public void func_187112_i() {
      super.func_187112_i();
      MCH_Lib.DbgLog(true, "MCH_EntityParticleMarkPoint.setExpired : " + this);
   }

   public int func_70537_b() {
      return 3;
   }

   public void func_180434_a(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
      GL11.glPushMatrix();
      Minecraft mc = Minecraft.func_71410_x();
      EntityPlayer player = mc.field_71439_g;
      if (player != null) {
         double ix = field_70556_an;
         double iy = field_70554_ao;
         double iz = field_70555_ap;
         if (mc.field_71474_y.field_74320_O > 0 && entityIn != null) {
            double dist = (double)W_Reflection.getThirdPersonDistance();
            float yaw = mc.field_71474_y.field_74320_O != 2 ? -entityIn.field_70177_z : -entityIn.field_70177_z;
            float pitch = mc.field_71474_y.field_74320_O != 2 ? -entityIn.field_70125_A : -entityIn.field_70125_A;
            Vec3d v = MCH_Lib.RotVec3(0.0D, 0.0D, -dist, yaw, pitch);
            if (mc.field_71474_y.field_74320_O == 2) {
               v = new Vec3d(-v.x, -v.y, -v.z);
            }

            Vec3d vs = new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.func_70047_e(), entityIn.posZ);
            RayTraceResult mop = entityIn.world.func_72933_a(vs.func_72441_c(0.0D, 0.0D, 0.0D), vs.func_72441_c(v.x, v.y, v.z));
            double block_dist = dist;
            if (mop != null && mop.field_72313_a == Type.BLOCK) {
               block_dist = vs.func_72438_d(mop.field_72307_f) - 0.4D;
               if (block_dist < 0.0D) {
                  block_dist = 0.0D;
               }
            }

            GL11.glTranslated(v.x * (block_dist / dist), v.y * (block_dist / dist), v.z * (block_dist / dist));
            ix += v.x * (block_dist / dist);
            iy += v.y * (block_dist / dist);
            iz += v.z * (block_dist / dist);
         }

         double px = (double)((float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)partialTicks - ix));
         double py = (double)((float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)partialTicks - iy));
         double pz = (double)((float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)partialTicks - iz));
         double scale = Math.sqrt(px * px + py * py + pz * pz) / 100.0D;
         if (scale < 1.0D) {
            scale = 1.0D;
         }

         MCH_GuiTargetMarker.addMarkEntityPos(100, this, px / scale, py / scale, pz / scale, false);
         GL11.glPopMatrix();
      }
   }

   public double getX() {
      return this.field_187126_f;
   }

   public double getY() {
      return this.field_187127_g;
   }

   public double getZ() {
      return this.field_187128_h;
   }
}
