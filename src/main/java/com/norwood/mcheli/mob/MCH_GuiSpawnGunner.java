package com.norwood.mcheli.mob;

import java.util.List;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.gui.MCH_Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_GuiSpawnGunner extends MCH_Gui {
   public MCH_GuiSpawnGunner(Minecraft minecraft) {
      super(minecraft);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public boolean func_73868_f() {
      return false;
   }

   public boolean isDrawGui(EntityPlayer player) {
      return player != null && player.world != null && !player.func_184614_ca().func_190926_b() && player.func_184614_ca().func_77973_b() instanceof MCH_ItemSpawnGunner;
   }

   public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
      if (!isThirdPersonView) {
         if (this.isDrawGui(player)) {
            GL11.glLineWidth((float)scaleFactor);
            GL11.glDisable(3042);
            this.draw(player, this.searchTarget(player));
         }
      }
   }

   private Entity searchTarget(EntityPlayer player) {
      float f = 1.0F;
      float pitch = player.field_70127_C + (player.field_70125_A - player.field_70127_C) * f;
      float yaw = player.field_70126_B + (player.field_70177_z - player.field_70126_B) * f;
      double dx = player.field_70169_q + (player.posX - player.field_70169_q) * (double)f;
      double dy = player.field_70167_r + (player.posY - player.field_70167_r) * (double)f + (double)player.func_70047_e();
      double dz = player.field_70166_s + (player.posZ - player.field_70166_s) * (double)f;
      Vec3d vec3 = new Vec3d(dx, dy, dz);
      float f3 = MathHelper.func_76134_b(-yaw * 0.017453292F - 3.1415927F);
      float f4 = MathHelper.func_76126_a(-yaw * 0.017453292F - 3.1415927F);
      float f5 = -MathHelper.func_76134_b(-pitch * 0.017453292F);
      float f6 = MathHelper.func_76126_a(-pitch * 0.017453292F);
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = 5.0D;
      Vec3d vec31 = vec3.func_72441_c((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
      Entity target = null;
      List<MCH_EntityGunner> list = player.world.func_72872_a(MCH_EntityGunner.class, player.func_174813_aQ().func_72314_b(5.0D, 5.0D, 5.0D));

      for(int i = 0; i < list.size(); ++i) {
         MCH_EntityGunner gunner = (MCH_EntityGunner)list.get(i);
         if (gunner.func_174813_aQ().func_72327_a(vec3, vec31) != null && (target == null || player.func_70068_e(gunner) < player.func_70068_e((Entity)target))) {
            target = gunner;
         }
      }

      if (target != null) {
         return (Entity)target;
      } else {
         MCH_ItemSpawnGunner item = (MCH_ItemSpawnGunner)player.func_184614_ca().func_77973_b();
         if (item.targetType == 1 && !player.world.isRemote && player.func_96124_cp() == null) {
            return null;
         } else {
            List<MCH_EntitySeat> list1 = player.world.func_72872_a(MCH_EntitySeat.class, player.func_174813_aQ().func_72314_b(5.0D, 5.0D, 5.0D));

            for(int i = 0; i < list1.size(); ++i) {
               MCH_EntitySeat seat = (MCH_EntitySeat)list1.get(i);
               if (seat.getParent() != null && seat.getParent().getAcInfo() != null && seat.func_174813_aQ().func_72327_a(vec3, vec31) != null && (target == null || player.func_70068_e(seat) < player.func_70068_e((Entity)target))) {
                  if (seat.getRiddenByEntity() instanceof MCH_EntityGunner) {
                     target = seat.getRiddenByEntity();
                  } else {
                     target = seat;
                  }
               }
            }

            if (target == null) {
               List<MCH_EntityAircraft> list2 = player.world.func_72872_a(MCH_EntityAircraft.class, player.func_174813_aQ().func_72314_b(5.0D, 5.0D, 5.0D));

               for(int i = 0; i < list2.size(); ++i) {
                  MCH_EntityAircraft ac = (MCH_EntityAircraft)list2.get(i);
                  if (!ac.isUAV() && ac.getAcInfo() != null && ac.func_174813_aQ().func_72327_a(vec3, vec31) != null && (target == null || player.func_70068_e(ac) < player.func_70068_e((Entity)target))) {
                     if (ac.getRiddenByEntity() instanceof MCH_EntityGunner) {
                        target = ac.getRiddenByEntity();
                     } else {
                        target = ac;
                     }
                  }
               }
            }

            return (Entity)target;
         }
      }
   }

   void draw(EntityPlayer player, Entity entity) {
      if (entity != null) {
         GL11.glEnable(3042);
         GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
         int srcBlend = GL11.glGetInteger(3041);
         int dstBlend = GL11.glGetInteger(3040);
         GL11.glBlendFunc(770, 771);

         double size;
         for(size = 512.0D; size < (double)this.field_146294_l || size < (double)this.field_146295_m; size *= 2.0D) {
         }

         GL11.glBlendFunc(srcBlend, dstBlend);
         GL11.glDisable(3042);
         double factor = size / 512.0D;
         double SCALE_FACTOR = (double)scaleFactor * factor;
         double CX = (double)(this.field_146297_k.field_71443_c / 2);
         double CY = (double)(this.field_146297_k.field_71440_d / 2);
         double px = (CX - 0.0D) / SCALE_FACTOR;
         double py = (CY + 0.0D) / SCALE_FACTOR;
         GL11.glPushMatrix();
         if (entity instanceof MCH_EntityGunner) {
            MCH_EntityGunner gunner = (MCH_EntityGunner)entity;
            String seatName = "";
            if (gunner.func_184187_bx() instanceof MCH_EntitySeat) {
               seatName = "(seat " + (((MCH_EntitySeat)gunner.func_184187_bx()).seatID + 2) + ")";
            } else if (gunner.func_184187_bx() instanceof MCH_EntityAircraft) {
               seatName = "(seat 1)";
            }

            String name = MCH_MOD.isTodaySep01() ? " EMB4 " : " Gunner ";
            this.drawCenteredString(gunner.getTeamName() + name + seatName, (int)px, (int)py + 20, -8355840);
            int S = 10;
            this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -8355840, 2);
         } else {
            byte S;
            if (entity instanceof MCH_EntitySeat) {
               MCH_EntitySeat seat = (MCH_EntitySeat)entity;
               if (seat.getRiddenByEntity() == null) {
                  this.drawCenteredString("seat " + (seat.seatID + 2), (int)px, (int)py + 20, -16711681);
                  S = 10;
                  this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -16711681, 2);
               } else {
                  this.drawCenteredString("seat " + (seat.seatID + 2), (int)px, (int)py + 20, -65536);
                  S = 10;
                  this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -65536, 2);
                  this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py + (double)S}, -65536);
                  this.drawLine(new double[]{px + (double)S, py - (double)S, px - (double)S, py + (double)S}, -65536);
               }
            } else if (entity instanceof MCH_EntityAircraft) {
               MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
               if (ac.getRiddenByEntity() == null) {
                  this.drawCenteredString("seat 1", (int)px, (int)py + 20, -16711681);
                  S = 10;
                  this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -16711681, 2);
               } else {
                  this.drawCenteredString("seat 1", (int)px, (int)py + 20, -65536);
                  S = 10;
                  this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py - (double)S, px + (double)S, py + (double)S, px - (double)S, py + (double)S}, -65536, 2);
                  this.drawLine(new double[]{px - (double)S, py - (double)S, px + (double)S, py + (double)S}, -65536);
                  this.drawLine(new double[]{px + (double)S, py - (double)S, px - (double)S, py + (double)S}, -65536);
               }
            }
         }

         GL11.glPopMatrix();
      }
   }
}
