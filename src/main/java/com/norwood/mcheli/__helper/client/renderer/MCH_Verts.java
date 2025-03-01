package com.norwood.mcheli.__helper.client.renderer;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;

public class MCH_Verts {
   public static final VertexFormatElement TEX_2S;
   public static final VertexFormat POS_COLOR_LMAP;

   static {
      TEX_2S = new VertexFormatElement(0, EnumType.SHORT, EnumUsage.UV, 2);
      POS_COLOR_LMAP = (new VertexFormat()).func_181721_a(DefaultVertexFormats.field_181713_m).func_181721_a(DefaultVertexFormats.field_181714_n).func_181721_a(TEX_2S);
   }
}
