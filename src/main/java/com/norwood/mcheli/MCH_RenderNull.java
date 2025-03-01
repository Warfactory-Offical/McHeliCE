package com.norwood.mcheli;

import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MCH_RenderNull extends W_Render<Entity> {
   public static final IRenderFactory<Entity> FACTORY = MCH_RenderNull::new;

   public MCH_RenderNull(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 0.0F;
   }

   public void func_76986_a(Entity entity, double posX, double posY, double posZ, float par8, float tickTime) {
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return TEX_DEFAULT;
   }
}
