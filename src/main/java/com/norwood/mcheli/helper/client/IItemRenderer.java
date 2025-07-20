package com.norwood.mcheli.helper.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated
@SideOnly(Side.CLIENT)
public interface IItemRenderer {
    boolean handleRenderType(ItemStack var1, IItemRenderer.ItemRenderType var2);

    boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType var1, ItemStack var2, IItemRenderer.ItemRendererHelper var3);

    void renderItem(IItemRenderer.ItemRenderType var1, ItemStack var2, Object... var3);

    @Deprecated
    enum ItemRenderType {
        ENTITY,
        EQUIPPED,
        EQUIPPED_FIRST_PERSON,
        INVENTORY,
        FIRST_PERSON_MAP
    }

    @Deprecated
    enum ItemRendererHelper {
        ENTITY_ROTATION,
        ENTITY_BOBBING,
        EQUIPPED_BLOCK,
        BLOCK_3D,
        INVENTORY_BLOCK
    }
}
