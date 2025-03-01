package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_ModelManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderA10 extends MCH_RenderBulletBase<MCH_EntityA10> {
   public static final IRenderFactory<MCH_EntityA10> FACTORY = MCH_RenderA10::new;

   public MCH_RenderA10(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 10.5F;
   }

   public void renderBullet(MCH_EntityA10 e, double posX, double posY, double posZ, float par8, float tickTime) {
      if (e instanceof MCH_EntityA10) {
         if (e.isRender()) {
            GL11.glPushMatrix();
            GL11.glTranslated(posX, posY, posZ);
            float yaw = -(e.field_70126_B + (e.field_70177_z - e.field_70126_B) * tickTime);
            float pitch = -(e.field_70127_C + (e.field_70125_A - e.field_70127_C) * tickTime);
            GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
            this.bindTexture("textures/bullets/a10.png");
            MCH_ModelManager.render("a-10");
            GL11.glPopMatrix();
         }
      }
   }

   protected ResourceLocation getEntityTexture(MCH_EntityA10 entity) {
      return TEX_DEFAULT;
   }
}
