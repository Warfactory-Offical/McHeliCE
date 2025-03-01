package com.norwood.mcheli.block;

import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_ModelManager;
import com.norwood.mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class MCH_DraftingTableRenderer extends TileEntitySpecialRenderer<MCH_DraftingTableTileEntity> {
   @SideOnly(Side.CLIENT)
   private static MCH_DraftingTableRenderer.DraftingTableStackRenderer stackRenderer;

   @SideOnly(Side.CLIENT)
   public static MCH_DraftingTableRenderer.DraftingTableStackRenderer getStackRenderer() {
      if (stackRenderer == null) {
         stackRenderer = new MCH_DraftingTableRenderer.DraftingTableStackRenderer();
      }

      return stackRenderer;
   }

   public void render(MCH_DraftingTableTileEntity tile, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
      GL11.glPushMatrix();
      GL11.glEnable(2884);
      GL11.glTranslated(posX + 0.5D, posY, posZ + 0.5D);
      float yaw = this.getYawAngle(tile);
      GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
      RenderHelper.func_74519_b();
      GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
      GL11.glEnable(3042);
      int srcBlend = GL11.glGetInteger(3041);
      int dstBlend = GL11.glGetInteger(3040);
      GL11.glBlendFunc(770, 771);
      if (MCH_Config.SmoothShading.prmBool) {
         GL11.glShadeModel(7425);
      }

      W_McClient.MOD_bindTexture("textures/blocks/drafting_table.png");
      MCH_ModelManager.render("blocks", "drafting_table");
      GL11.glBlendFunc(srcBlend, dstBlend);
      GL11.glDisable(3042);
      GL11.glShadeModel(7424);
      GL11.glPopMatrix();
   }

   private float getYawAngle(MCH_DraftingTableTileEntity tile) {
      return tile.func_145830_o() ? (float)(-tile.func_145832_p()) * 45.0F + 180.0F : 0.0F;
   }

   @SideOnly(Side.CLIENT)
   private static class DraftingTableStackRenderer extends TileEntityItemStackRenderer {
      private MCH_DraftingTableTileEntity draftingTable;

      private DraftingTableStackRenderer() {
         this.draftingTable = new MCH_DraftingTableTileEntity();
      }

      public void func_192838_a(ItemStack p_192838_1_, float partialTicks) {
         TileEntityRendererDispatcher.field_147556_a.func_192855_a(this.draftingTable, 0.0D, 0.0D, 0.0D, partialTicks, 0.0F);
      }

      // $FF: synthetic method
      DraftingTableStackRenderer(Object x0) {
         this();
      }
   }
}
