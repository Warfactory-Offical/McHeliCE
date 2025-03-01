package com.norwood.mcheli.wrapper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class W_Item extends Item {
   public W_Item(int par1) {
   }

   public W_Item() {
   }

   public static int getIdFromItem(Item i) {
      return i == null ? 0 : field_150901_e.func_148757_b(i);
   }

   public static Item getItemById(int i) {
      return Item.func_150899_d(i);
   }

   public static Item getItemByName(String nm) {
      return (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(nm));
   }

   public static String getNameForItem(Item item) {
      return ForgeRegistries.ITEMS.getKey(item).toString();
   }

   public static Item getItemFromBlock(Block block) {
      return Item.func_150898_a(block);
   }
}
