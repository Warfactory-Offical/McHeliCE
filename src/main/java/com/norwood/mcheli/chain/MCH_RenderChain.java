package com.norwood.mcheli.chain;

import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderChain extends W_Render<MCH_EntityChain> {
   public static final IRenderFactory<MCH_EntityChain> FACTORY = MCH_RenderChain::new;

   public MCH_RenderChain(RenderManager renderManager) {
      super(renderManager);
   }

   public void doRender(MCH_EntityChain e, double posX, double posY, double posZ, float par8, float par9) {
      if (e instanceof MCH_EntityChain) {
         if (e.towedEntity != null && e.towEntity != null) {
            GL11.glPushMatrix();
            GL11.glEnable(2884);
            GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            GL11.glTranslated(
               e.towedEntity.lastTickPosX - TileEntityRendererDispatcher.staticPlayerX,
               e.towedEntity.lastTickPosY - TileEntityRendererDispatcher.staticPlayerY,
               e.towedEntity.lastTickPosZ - TileEntityRendererDispatcher.staticPlayerZ
            );
            this.bindTexture("textures/chain.png");
            double dx = e.towEntity.lastTickPosX - e.towedEntity.lastTickPosX;
            double dy = e.towEntity.lastTickPosY - e.towedEntity.lastTickPosY;
            double dz = e.towEntity.lastTickPosZ - e.towedEntity.lastTickPosZ;
            double diff = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double x = dx * 0.95F / diff;
            double y = dy * 0.95F / diff;

            for (double z = dz * 0.95F / diff; diff > 0.95F; diff -= 0.95F) {
               GL11.glTranslated(x, y, z);
               GL11.glPushMatrix();
               Vec3d v = MCH_Lib.getYawPitchFromVec(x, y, z);
               GL11.glRotatef((float)v.y, 0.0F, -1.0F, 0.0F);
               GL11.glRotatef((float)v.z, 0.0F, 0.0F, 1.0F);
               MCH_ModelManager.render("chain");
               GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
         }
      }
   }

   protected ResourceLocation getEntityTexture(MCH_EntityChain entity) {
      return TEX_DEFAULT;
   }
}
