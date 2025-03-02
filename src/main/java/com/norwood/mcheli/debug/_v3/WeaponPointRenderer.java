package com.norwood.mcheli.debug._v3;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import javax.vecmath.Color4f;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.weapon.MCH_WeaponBase;
import com.norwood.mcheli.weapon.MCH_WeaponSet;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class WeaponPointRenderer {
   private static final Color4f[] C = new Color4f[]{new Color4f(1.0F, 0.0F, 0.0F, 1.0F), new Color4f(0.0F, 1.0F, 0.0F, 1.0F), new Color4f(0.0F, 0.0F, 1.0F, 1.0F), new Color4f(1.0F, 1.0F, 0.0F, 1.0F), new Color4f(1.0F, 0.0F, 1.0F, 1.0F), new Color4f(0.0F, 1.0F, 1.0F, 1.0F), new Color4f(0.95686275F, 0.6431373F, 0.3764706F, 1.0F), new Color4f(0.5411765F, 0.16862746F, 0.42477876F, 1.0F)};

   public static void renderWeaponPoints(MCH_EntityAircraft ac, MCH_AircraftInfo info, double x, double y, double z) {
      int prevPointSize = GlStateManager.func_187397_v(2833);
      int id = 0;
      int prevFunc = GlStateManager.func_187397_v(2932);
      Map<Vec3d, Integer> poses = Maps.newHashMap();
      GlStateManager.func_179090_x();
      GlStateManager.func_179147_l();
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.func_179145_e();
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179143_c(519);
      GL11.glPointSize(20.0F);
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(x, y, z);
      Iterator var12 = info.weaponSetList.iterator();

      while(true) {
         MCH_WeaponSet ws;
         do {
            if (!var12.hasNext()) {
               GlStateManager.func_179121_F();
               GL11.glPointSize((float)prevPointSize);
               GlStateManager.func_179143_c(prevFunc);
               GlStateManager.func_179132_a(true);
               GlStateManager.func_179098_w();
               GlStateManager.func_179084_k();
               return;
            }

            MCH_AircraftInfo.WeaponSet wsInfo = (MCH_AircraftInfo.WeaponSet)var12.next();
            ws = ac.getWeaponByName(wsInfo.type);
         } while(ws == null);

         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder builder = tessellator.getBuffer();
         builder.begin(0, DefaultVertexFormats.field_181706_f);

         for(int i = 0; i < ws.getWeaponNum(); ++i) {
            MCH_WeaponBase weapon = ws.getWeapon(i);
            if (weapon != null) {
               int j = 0;
               if (poses.containsKey(weapon.position)) {
                  j = (Integer)poses.get(weapon.position);
                  ++j;
               }

               poses.put(weapon.position, j);
               Vec3d vec3d = weapon.getShotPos(ac);
               Color4f c = C[id % C.length];
               float f = (float)i * 0.1F;
               double d = (double)j * 0.04D;
               builder.pos(vec3d.x, vec3d.y + d, vec3d.z).func_181666_a(in(c.x + f), in(c.y + f), in(c.z + f), c.w).func_181675_d();
            }
         }

         tessellator.draw();
         ++id;
      }
   }

   static float in(float value) {
      return MathHelper.func_76131_a(value, 0.0F, 1.0F);
   }
}
