package com.norwood.mcheli.throwable;

import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderThrowable extends W_Render<MCH_EntityThrowable> {
   public static final IRenderFactory<MCH_EntityThrowable> FACTORY = MCH_RenderThrowable::new;

   public MCH_RenderThrowable(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 0.0F;
   }

   public void doRender(MCH_EntityThrowable entity, double posX, double posY, double posZ, float par8, float tickTime) {
      MCH_ThrowableInfo info = entity.getInfo();
      if (info != null) {
         GL11.glPushMatrix();
         GL11.glTranslated(posX, posY, posZ);
         GL11.glRotatef(entity.field_70177_z, 0.0F, -1.0F, 0.0F);
         GL11.glRotatef(entity.field_70125_A, 1.0F, 0.0F, 0.0F);
         this.setCommonRenderParam(true, entity.func_70070_b());
         if (info.model != null) {
            this.bindTexture("textures/throwable/" + info.name + ".png");
            info.model.renderAll();
         }

         this.restoreCommonRenderParam();
         GL11.glPopMatrix();
      }
   }

   protected ResourceLocation getEntityTexture(MCH_EntityThrowable entity) {
      return TEX_DEFAULT;
   }
}
