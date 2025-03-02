package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Color;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Entity;
import com.norwood.mcheli.wrapper.W_Render;
import com.norwood.mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

public abstract class MCH_RenderBulletBase<T extends W_Entity> extends W_Render<T> {
   protected MCH_RenderBulletBase(RenderManager renderManager) {
      super(renderManager);
   }

   public void doRender(T e, double var2, double var4, double var6, float var8, float var9) {
      int y;
      if (e instanceof MCH_EntityBaseBullet && ((MCH_EntityBaseBullet)e).getInfo() != null) {
         MCH_Color c = ((MCH_EntityBaseBullet)e).getInfo().color;

         for(y = 0; y < 3; ++y) {
            Block b = W_WorldFunc.getBlock(e.world, (int)(e.posX + 0.5D), (int)(e.posY + 1.5D - (double)y), (int)(e.posZ + 0.5D));
            if (b != null && b == W_Block.getWater()) {
               c = ((MCH_EntityBaseBullet)e).getInfo().colorInWater;
               break;
            }
         }

         GL11.glColor4f(c.r, c.g, c.b, c.a);
      } else {
         GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
      }

      GL11.glAlphaFunc(516, 0.001F);
      GL11.glEnable(2884);
      GL11.glEnable(3042);
      int srcBlend = GL11.glGetInteger(3041);
      y = GL11.glGetInteger(3040);
      GL11.glBlendFunc(770, 771);
      this.renderBullet(e, var2, var4, var6, var8, var9);
      GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
      GL11.glBlendFunc(srcBlend, y);
      GL11.glDisable(3042);
   }

   public void renderModel(MCH_EntityBaseBullet e) {
      MCH_BulletModel model = e.getBulletModel();
      if (model != null) {
         this.bindTexture("textures/bullets/" + model.name + ".png");
         model.model.renderAll();
      }

   }

   public abstract void renderBullet(T var1, double var2, double var4, double var6, float var8, float var9);
}
