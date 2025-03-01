package com.norwood.mcheli.__helper;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import com.norwood.mcheli.MCH_ItemRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(
   modid = "mcheli"
)
public class MCH_Recipes {
   private static final Set<IRecipe> registryWrapper = Sets.newLinkedHashSet();

   @SubscribeEvent
   static void onRecipeRegisterEvent(Register<IRecipe> event) {
      MCH_ItemRecipe.registerItemRecipe(event.getRegistry());
      Iterator var1 = registryWrapper.iterator();

      while(var1.hasNext()) {
         IRecipe recipe = (IRecipe)var1.next();
         event.getRegistry().register(recipe);
      }

   }

   public static void register(String name, IRecipe recipe) {
      registryWrapper.add(recipe.setRegistryName(MCH_Utils.suffix(name)));
   }

   public static ShapedRecipes addShapedRecipe(String name, ItemStack output, Object... params) {
      ShapedPrimer primer = CraftingHelper.parseShaped(params);
      ShapedRecipes recipe = new ShapedRecipes("", primer.width, primer.height, primer.input, output);
      register(name, recipe);
      return recipe;
   }

   public static boolean canCraft(EntityPlayer player, IRecipe recipe) {
      Iterator var2 = recipe.func_192400_c().iterator();

      boolean flag;
      do {
         Ingredient ingredient;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            ingredient = (Ingredient)var2.next();
         } while(ingredient == Ingredient.field_193370_a);

         flag = false;
         Iterator var5 = player.field_71071_by.field_70462_a.iterator();

         while(var5.hasNext()) {
            ItemStack itemstack = (ItemStack)var5.next();
            if (ingredient.apply(itemstack)) {
               flag = true;
               break;
            }
         }
      } while(flag);

      return false;
   }

   public static boolean consumeInventory(EntityPlayer player, IRecipe recipe) {
      Iterator var2 = recipe.func_192400_c().iterator();

      boolean flag;
      do {
         Ingredient ingredient;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            ingredient = (Ingredient)var2.next();
         } while(ingredient == Ingredient.field_193370_a);

         int i = 0;
         flag = false;

         for(Iterator var6 = player.field_71071_by.field_70462_a.iterator(); var6.hasNext(); ++i) {
            ItemStack itemstack = (ItemStack)var6.next();
            if (ingredient.apply(itemstack)) {
               player.field_71071_by.func_70298_a(i, 1);
               flag = true;
               break;
            }
         }
      } while(flag);

      return false;
   }
}
