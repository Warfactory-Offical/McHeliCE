package com.norwood.mcheli;

import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import com.norwood.mcheli.gltd.MCH_EntityGLTD;
import com.norwood.mcheli.uav.MCH_EntityUavStation;
import com.norwood.mcheli.wrapper.W_EntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MCH_ItemRendererDummy extends ItemRenderer {
   protected static Minecraft mc;
   protected static ItemRenderer backupItemRenderer;
   protected static MCH_ItemRendererDummy instance;

   public MCH_ItemRendererDummy(Minecraft par1Minecraft) {
      super(par1Minecraft);
      mc = par1Minecraft;
   }

   public void func_78440_a(float par1) {
      if (mc.field_71439_g == null) {
         super.func_78440_a(par1);
      } else if (!(mc.field_71439_g.func_184187_bx() instanceof MCH_EntityAircraft) && !(mc.field_71439_g.func_184187_bx() instanceof MCH_EntityUavStation) && !(mc.field_71439_g.func_184187_bx() instanceof MCH_EntityGLTD)) {
         super.func_78440_a(par1);
      }

   }

   public static void enableDummyItemRenderer() {
      if (instance == null) {
         instance = new MCH_ItemRendererDummy(Minecraft.func_71410_x());
      }

      if (!(mc.field_71460_t.field_78516_c instanceof MCH_ItemRendererDummy)) {
         backupItemRenderer = mc.field_71460_t.field_78516_c;
      }

      W_EntityRenderer.setItemRenderer(mc, instance);
   }

   public static void disableDummyItemRenderer() {
      if (backupItemRenderer != null) {
         W_EntityRenderer.setItemRenderer(mc, backupItemRenderer);
      }

   }
}
