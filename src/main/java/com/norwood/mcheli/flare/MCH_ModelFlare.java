package com.norwood.mcheli.flare;

import com.norwood.mcheli.wrapper.W_ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MCH_ModelFlare extends W_ModelBase {
   public ModelRenderer model = (new ModelRenderer(this, 0, 0)).func_78787_b(4, 4);

   public MCH_ModelFlare() {
      this.model.func_78790_a(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
   }

   public void renderModel(double yaw, double pitch, float par7) {
      this.model.func_78785_a(par7);
   }
}
