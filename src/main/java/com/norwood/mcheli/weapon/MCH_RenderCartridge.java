package com.norwood.mcheli.weapon;

import com.norwood.mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MCH_RenderCartridge extends W_Render<MCH_EntityCartridge> {
   public static final IRenderFactory<MCH_EntityCartridge> FACTORY = MCH_RenderCartridge::new;

   public MCH_RenderCartridge(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 0.0F;
   }

   public void doRender(MCH_EntityCartridge entity, double posX, double posY, double posZ, float par8, float tickTime) {
      MCH_EntityCartridge cartridge = null;
      if (entity.model != null && !entity.texture_name.isEmpty()) {
         GL11.glPushMatrix();
         GL11.glTranslated(posX, posY, posZ);
         GL11.glScalef(entity.getScale(), entity.getScale(), entity.getScale());
         float prevYaw = entity.field_70126_B;
         if (entity.field_70177_z - prevYaw < -180.0F) {
            prevYaw -= 360.0F;
         } else if (prevYaw - entity.field_70177_z < -180.0F) {
            prevYaw += 360.0F;
         }

         float yaw = -(prevYaw + (entity.field_70177_z - prevYaw) * tickTime);
         float pitch = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * tickTime;
         GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
         this.bindTexture("textures/bullets/" + entity.texture_name + ".png");
         entity.model.renderAll();
         GL11.glPopMatrix();
      }

   }

   protected ResourceLocation getEntityTexture(MCH_EntityCartridge entity) {
      return TEX_DEFAULT;
   }
}
