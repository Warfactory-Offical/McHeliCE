package com.norwood.com.norwood.mcheli.multiplay;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_MarkEntityPos;
import com.norwood.mcheli.MCH_ServerSettings;
import com.norwood.mcheli.__helper.entity.ITargetMarkerObject;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.aircraft.MCH_EntitySeat;
import com.norwood.mcheli.gui.MCH_Gui;
import com.norwood.mcheli.particles.MCH_ParticlesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@SideOnly(Side.CLIENT)
public class MCH_GuiTargetMarker extends MCH_Gui {
   private static FloatBuffer matModel = BufferUtils.createFloatBuffer(16);
   private static FloatBuffer matProjection = BufferUtils.createFloatBuffer(16);
   private static IntBuffer matViewport = BufferUtils.createIntBuffer(16);
   private static ArrayList<MCH_MarkEntityPos> entityPos = new ArrayList();
   private static HashMap<Integer, Integer> spotedEntity = new HashMap();
   private static Minecraft s_minecraft;
   private static int spotedEntityCountdown = 0;

   public MCH_GuiTargetMarker(Minecraft minecraft) {
      super(minecraft);
      s_minecraft = minecraft;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public boolean func_73868_f() {
      return false;
   }

   public boolean isDrawGui(EntityPlayer player) {
      return player != null && player.field_70170_p != null;
   }

   public static void onClientTick() {
      if (!Minecraft.func_71410_x().func_147113_T()) {
         ++spotedEntityCountdown;
      }

      if (spotedEntityCountdown >= 20) {
         spotedEntityCountdown = 0;
         Iterator var1 = spotedEntity.keySet().iterator();

         while(var1.hasNext()) {
            Integer key = (Integer)var1.next();
            int count = (Integer)spotedEntity.get(key);
            if (count > 0) {
               spotedEntity.put(key, count - 1);
            }
         }

         Iterator i = spotedEntity.values().iterator();

         while(i.hasNext()) {
            if ((Integer)i.next() <= 0) {
               i.remove();
            }
         }
      }

   }

   public static boolean isSpotedEntity(@Nullable Entity entity) {
      if (entity == null) {
         return false;
      } else {
         int entityId = entity.func_145782_y();
         Iterator i$ = spotedEntity.keySet().iterator();

         int key;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            key = (Integer)i$.next();
         } while(key != entityId);

         return true;
      }
   }

   public static void addSpotedEntity(int entityId, int count) {
      if (spotedEntity.containsKey(entityId)) {
         int now = (Integer)spotedEntity.get(entityId);
         if (count > now) {
            spotedEntity.put(entityId, count);
         }
      } else {
         spotedEntity.put(entityId, count);
      }

   }

   public static void addMarkEntityPos(int reserve, ITargetMarkerObject target, double x, double y, double z) {
      addMarkEntityPos(reserve, target, x, y, z, false);
   }

   public static void addMarkEntityPos(int reserve, ITargetMarkerObject target, double x, double y, double z, boolean nazo) {
      if (isEnableEntityMarker()) {
         com.norwood.mcheli.multiplay.MCH_TargetType spotType = MCH_TargetType.NONE;
         EntityPlayer clientPlayer = s_minecraft.field_71439_g;
         Entity entity = target.getEntity();
         if (entity instanceof MCH_EntityAircraft) {
            MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
            if (ac.isMountedEntity(clientPlayer)) {
               return;
            }

            if (ac.isMountedSameTeamEntity(clientPlayer)) {
               spotType = MCH_TargetType.SAME_TEAM_PLAYER;
            }
         } else if (entity instanceof EntityPlayer) {
            if (entity == clientPlayer || entity.func_184187_bx() instanceof MCH_EntitySeat || entity.func_184187_bx() instanceof MCH_EntityAircraft) {
               return;
            }

            if (clientPlayer.func_96124_cp() != null && clientPlayer.func_184191_r(entity)) {
               spotType = MCH_TargetType.SAME_TEAM_PLAYER;
            }
         }

         if (spotType == MCH_TargetType.NONE && isSpotedEntity(entity)) {
            spotType = MCH_Multiplay.canSpotEntity(clientPlayer, clientPlayer.field_70165_t, clientPlayer.field_70163_u + (double)clientPlayer.func_70047_e(), clientPlayer.field_70161_v, entity, false);
         }

         if (reserve == 100) {
            spotType = MCH_TargetType.POINT;
         }

         if (spotType != MCH_TargetType.NONE) {
            MCH_MarkEntityPos e = new MCH_MarkEntityPos(spotType.ordinal(), target);
            GL11.glGetFloat(2982, matModel);
            GL11.glGetFloat(2983, matProjection);
            GL11.glGetInteger(2978, matViewport);
            if (nazo) {
               GLU.gluProject((float)z, (float)y, (float)x, matModel, matProjection, matViewport, e.pos);
               float yy = e.pos.get(1);
               GLU.gluProject((float)x, (float)y, (float)z, matModel, matProjection, matViewport, e.pos);
               e.pos.put(1, yy);
            } else {
               GLU.gluProject((float)x, (float)y, (float)z, matModel, matProjection, matViewport, e.pos);
            }

            entityPos.add(e);
         }

      }
   }

   public static void clearMarkEntityPos() {
      entityPos.clear();
   }

   public static boolean isEnableEntityMarker() {
      return MCH_Config.DisplayEntityMarker.prmBool && (Minecraft.func_71410_x().func_71356_B() || MCH_ServerSettings.enableEntityMarker) && MCH_Config.EntityMarkerSize.prmDouble > 0.0D;
   }

   public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
      GL11.glLineWidth((float)(scaleFactor * 2));
      if (this.isDrawGui(player)) {
         GL11.glDisable(3042);
         if (isEnableEntityMarker()) {
            this.drawMark();
         }

      }
   }

   void drawMark() {
      int[] COLOR_TABLE = new int[]{0, -808464433, -805371904, -805306624, -822018049, -805351649, -65536, 0};
      int scale = scaleFactor > 0 ? scaleFactor : 2;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
      GL11.glDepthMask(false);
      int DW = this.field_146297_k.field_71443_c;
      int DSW = this.field_146297_k.field_71443_c / scale;
      int DSH = this.field_146297_k.field_71440_d / scale;
      double x = 9999.0D;
      double z = 9999.0D;
      double y = 9999.0D;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder builder = tessellator.func_178180_c();

      for(int i = 0; i < 2; ++i) {
         if (i == 0) {
            builder.func_181668_a(i == 0 ? 4 : 1, DefaultVertexFormats.field_181706_f);
         }

         Iterator var15 = entityPos.iterator();

         while(true) {
            while(var15.hasNext()) {
               MCH_MarkEntityPos e = (MCH_MarkEntityPos)var15.next();
               int color = COLOR_TABLE[e.type];
               x = (double)(e.pos.get(0) / (float)scale);
               z = (double)e.pos.get(2);
               y = (double)(e.pos.get(1) / (float)scale);
               if (z < 1.0D) {
                  y = (double)DSH - y;
               } else if (x < (double)(DW / 2)) {
                  x = 10000.0D;
               } else if (x >= (double)(DW / 2)) {
                  x = -10000.0D;
               }

               if (i == 0) {
                  double size = MCH_Config.EntityMarkerSize.prmDouble;
                  if (e.type < MCH_TargetType.POINT.ordinal() && z < 1.0D && x >= 0.0D && x <= (double)DSW && y >= 0.0D && y <= (double)DSH) {
                     this.drawTriangle1(builder, x, y, size, color);
                  }
               } else if (e.type == MCH_TargetType.POINT.ordinal() && e.getTarget() != null) {
                  ITargetMarkerObject target = e.getTarget();
                  double MARK_SIZE = MCH_Config.BlockMarkerSize.prmDouble;
                  double S;
                  if (z < 1.0D && x >= 0.0D && x <= (double)(DSW - 20) && y >= 0.0D && y <= (double)(DSH - 40)) {
                     S = this.field_146297_k.field_71439_g.func_70011_f(target.getX(), target.getY(), target.getZ());
                     GL11.glEnable(3553);
                     this.drawCenteredString(String.format("%.0fm", S), (int)x, (int)(y + MARK_SIZE * 1.1D + 16.0D), color);
                     if (x >= (double)(DSW / 2 - 20) && x <= (double)(DSW / 2 + 20) && y >= (double)(DSH / 2 - 20) && y <= (double)(DSH / 2 + 20)) {
                        this.drawString(String.format("x : %.0f", target.getX()), (int)(x + MARK_SIZE + 18.0D), (int)y - 12, color);
                        this.drawString(String.format("y : %.0f", target.getY()), (int)(x + MARK_SIZE + 18.0D), (int)y - 4, color);
                        this.drawString(String.format("z : %.0f", target.getZ()), (int)(x + MARK_SIZE + 18.0D), (int)y + 4, color);
                     }

                     GL11.glDisable(3553);
                     builder.func_181668_a(1, DefaultVertexFormats.field_181706_f);
                     drawRhombus(builder, 15, x, y, (double)this.field_73735_i, MARK_SIZE, color);
                  } else {
                     builder.func_181668_a(1, DefaultVertexFormats.field_181706_f);
                     S = 30.0D;
                     if (x < S) {
                        drawRhombus(builder, 1, S, (double)(DSH / 2), (double)this.field_73735_i, MARK_SIZE, color);
                     } else if (x > (double)DSW - S) {
                        drawRhombus(builder, 4, (double)DSW - S, (double)(DSH / 2), (double)this.field_73735_i, MARK_SIZE, color);
                     }

                     if (y < S) {
                        drawRhombus(builder, 8, (double)(DSW / 2), S, (double)this.field_73735_i, MARK_SIZE, color);
                     } else if (y > (double)DSH - S * 2.0D) {
                        drawRhombus(builder, 2, (double)(DSW / 2), (double)DSH - S * 2.0D, (double)this.field_73735_i, MARK_SIZE, color);
                     }
                  }

                  tessellator.func_78381_a();
               }
            }

            if (i == 0) {
               tessellator.func_78381_a();
            }
            break;
         }
      }

      GL11.glDepthMask(true);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void drawRhombus(BufferBuilder builder, int dir, double x, double y, double z, double size, int color) {
      size *= 2.0D;
      int red = color >> 16 & 255;
      int green = color >> 8 & 255;
      int blue = color >> 0 & 255;
      int alpha = color >> 24 & 255;
      double M = size / 3.0D;
      if ((dir & 1) != 0) {
         builder.func_181662_b(x - size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x - size + M, y - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x - size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x - size + M, y + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
      }

      if ((dir & 4) != 0) {
         builder.func_181662_b(x + size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x + size - M, y - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x + size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x + size - M, y + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
      }

      if ((dir & 8) != 0) {
         builder.func_181662_b(x, y - size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x + M, y - size + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x, y - size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x - M, y - size + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
      }

      if ((dir & 2) != 0) {
         builder.func_181662_b(x, y + size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x + M, y + size - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x, y + size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
         builder.func_181662_b(x - M, y + size - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
      }

   }

   public void drawTriangle1(BufferBuilder builder, double x, double y, double size, int color) {
      int red = color >> 16 & 255;
      int green = color >> 8 & 255;
      int blue = color >> 0 & 255;
      int alpha = color >> 24 & 255;
      builder.func_181662_b(x + size / 2.0D, y - 10.0D - size, (double)this.field_73735_i).func_181669_b(red, green, blue, alpha).func_181675_d();
      builder.func_181662_b(x - size / 2.0D, y - 10.0D - size, (double)this.field_73735_i).func_181669_b(red, green, blue, alpha).func_181675_d();
      builder.func_181662_b(x + 0.0D, y - 10.0D, (double)this.field_73735_i).func_181669_b(red, green, blue, alpha).func_181675_d();
   }

   public static void markPoint(int px, int py, int pz) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      if (player != null && player.field_70170_p != null) {
         if (py < 1000) {
            MCH_ParticlesUtil.spawnMarkPoint(player, 0.5D + (double)px, 1.0D + (double)py, 0.5D + (double)pz);
         } else {
            MCH_ParticlesUtil.clearMarkPoint();
         }
      }

   }
}
