package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Lib;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderAAMissile extends MCH_RenderBulletBase<MCH_EntityAAMissile> {
   public static final IRenderFactory<MCH_EntityAAMissile> FACTORY = MCH_RenderAAMissile::new;

   public MCH_RenderAAMissile(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 0.5F;
   }

   public void renderBullet(MCH_EntityAAMissile entity, double posX, double posY, double posZ, float par8, float par9) {
      if (entity instanceof MCH_EntityAAMissile) {
         double mx = entity.prevMotionX + (entity.field_70159_w - entity.prevMotionX) * (double)par9;
         double my = entity.prevMotionY + (entity.field_70181_x - entity.prevMotionY) * (double)par9;
         double mz = entity.prevMotionZ + (entity.field_70179_y - entity.prevMotionZ) * (double)par9;
         GL11.glPushMatrix();
         GL11.glTranslated(posX, posY, posZ);
         Vec3d v = MCH_Lib.getYawPitchFromVec(mx, my, mz);
         GL11.glRotatef((float)v.y - 90.0F, 0.0F, -1.0F, 0.0F);
         GL11.glRotatef((float)v.z, -1.0F, 0.0F, 0.0F);
         this.renderModel(entity);
         GL11.glPopMatrix();
      }
   }

   protected ResourceLocation getEntityTexture(MCH_EntityAAMissile entity) {
      return TEX_DEFAULT;
   }
}
