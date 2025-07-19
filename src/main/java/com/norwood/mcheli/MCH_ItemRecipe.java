package com.norwood.mcheli;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.MCH_Recipes;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.info.ContentRegistries;
import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_AircraftInfoManager;
import com.norwood.mcheli.helicopter.MCH_HeliInfo;
import com.norwood.mcheli.helicopter.MCH_HeliInfoManager;
import com.norwood.mcheli.plane.MCP_PlaneInfo;
import com.norwood.mcheli.plane.MCP_PlaneInfoManager;
import com.norwood.mcheli.tank.MCH_TankInfo;
import com.norwood.mcheli.tank.MCH_TankInfoManager;
import com.norwood.mcheli.throwable.MCH_ThrowableInfo;
import com.norwood.mcheli.vehicle.MCH_VehicleInfo;
import com.norwood.mcheli.vehicle.MCH_VehicleInfoManager;
import com.norwood.mcheli.wrapper.W_Block;
import com.norwood.mcheli.wrapper.W_Item;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.IForgeRegistry;

public class MCH_ItemRecipe implements MCH_IRecipeList {
   private static final MCH_ItemRecipe instance = new MCH_ItemRecipe();
   private static List<IRecipe> commonItemRecipe = new ArrayList<>();

   public static MCH_ItemRecipe getInstance() {
      return instance;
   }

   @Override
   public int getRecipeListSize() {
      return commonItemRecipe.size();
   }

   @Override
   public IRecipe getRecipe(int index) {
      return commonItemRecipe.get(index);
   }

   private static void addRecipeList(IRecipe recipe) {
      if (recipe != null) {
         commonItemRecipe.add(recipe);
      }
   }

   private static void registerCommonItemRecipe(IForgeRegistry<IRecipe> registry) {
      commonItemRecipe.clear();
      MCH_Recipes.register("charge_fuel", new MCH_RecipeFuel());
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fuel")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("gltd")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("chain")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("parachute")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("container")));

      for (int i = 0; i < MCH_MOD.itemUavStation.length; i++) {
         addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("uav_station" + (i > 0 ? "" + (i + 1) : ""))));
      }

      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("wrench")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("range_finder")));
      MCH_Recipes.register("charge_power_range_finder", new MCH_RecipeReloadRangeFinder());
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fim92")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fim92_bullet")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fgm148")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fgm148_bullet")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("spawn_gunner_vs_monster")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("spawn_gunner_vs_player")));
      addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("drafting_table")));
   }

   public static void registerItemRecipe(IForgeRegistry<IRecipe> registry) {
      registerCommonItemRecipe(registry);

      for (MCH_HeliInfo info : ContentRegistries.heli().values()) {
         addRecipeAndRegisterList(info, info.item, MCH_HeliInfoManager.getInstance());
      }

      for (MCP_PlaneInfo info : ContentRegistries.plane().values()) {
         addRecipeAndRegisterList(info, info.item, MCP_PlaneInfoManager.getInstance());
      }

      for (MCH_TankInfo info : ContentRegistries.tank().values()) {
         addRecipeAndRegisterList(info, info.item, MCH_TankInfoManager.getInstance());
      }

      for (MCH_VehicleInfo info : ContentRegistries.vehicle().values()) {
         addRecipeAndRegisterList(info, info.item, MCH_VehicleInfoManager.getInstance());
      }

      for (MCH_ThrowableInfo info : ContentRegistries.throwable().values()) {
         for (String s : info.recipeString) {
            if (s.length() >= 3) {
               IRecipe recipe = addRecipe(info.name, info.item, s, info.isShapedRecipe);
               if (recipe != null) {
                  info.recipe.add(recipe);
                  addRecipeList(recipe);
               }
            }
         }

         info.recipeString = null;
      }
   }

   private static <T extends MCH_AircraftInfo> void addRecipeAndRegisterList(MCH_AircraftInfo info, Item item, MCH_AircraftInfoManager<T> im) {
      int count = 0;

      for (String s : info.recipeString) {
         count++;
         if (s.length() >= 3) {
            IRecipe recipe = addRecipe(info.name, item, s, info.isShapedRecipe);
            if (recipe != null) {
               info.recipe.add(recipe);
               im.addRecipe(recipe, count, info.name, s);
            }
         }
      }

      info.recipeString = null;
   }

   public static IRecipe addRecipe(String name, Item item, String data) {
      return addShapedRecipe(name, item, data);
   }

   @Nullable
   public static IRecipe addRecipe(String name, Item item, String data, boolean isShaped) {
      return isShaped ? addShapedRecipe(name, item, data) : addShapelessRecipe(name, item, data);
   }

   @Nullable
   public static IRecipe addShapedRecipe(String name, Item item, String data) {
      ArrayList<Object> rcp = new ArrayList<>();
      String[] s = data.split("\\s*,\\s*");
      if (s.length < 3) {
         return null;
      } else {
         int start = 0;
         int createNum = 1;
         if (isNumber(s[0])) {
            start = 1;
            createNum = Integer.valueOf(s[0]);
            if (createNum <= 0) {
               createNum = 1;
            }
         }

         Set<Integer> needShortChars = Sets.newHashSet();
         int idx = start;

         for (int i = start; i < 3 + start; i++) {
            if (s[idx].length() > 0 && s[idx].charAt(0) == '"' && s[idx].charAt(s[idx].length() - 1) == '"') {
               String ingredientStr = s[idx].substring(1, s[idx].length() - 1);
               ingredientStr.toUpperCase().chars().forEach(needShortChars::add);
               rcp.add(s[idx].subSequence(1, s[idx].length() - 1));
               idx++;
            }
         }

         if (idx == 0) {
            return null;
         } else {
            boolean isChar = true;

            for (boolean flag = false; idx < s.length; idx++) {
               if (s[idx].length() <= 0) {
                  return null;
               }

               if (isChar) {
                  if (s[idx].length() != 1) {
                     return null;
                  }

                  char c = s[idx].toUpperCase().charAt(0);
                  if (c < 'A' || c > 'Z') {
                     return null;
                  }

                  if (!needShortChars.contains(Integer.valueOf(c))) {
                     MCH_Utils.logger().warn("Key defines symbols that aren't used in pattern: [" + c + "], item:" + name);
                     flag = true;
                  }

                  if (!flag) {
                     rcp.add(c);
                  }
               } else {
                  if (!flag) {
                     String nm = s[idx].trim().toLowerCase();
                     int dmg = 0;
                     if (idx + 1 < s.length && isNumber(s[idx + 1])) {
                        dmg = Integer.parseInt(s[++idx]);
                     }

                     if (isNumber(nm)) {
                        return null;
                     }

                     rcp.add(new ItemStack(W_Item.getItemByName(nm), 1, dmg));
                  }

                  flag = false;
               }

               isChar = !isChar;
            }

            Object[] recipe = new Object[rcp.size()];

            for (int ix = 0; ix < recipe.length; ix++) {
               recipe[ix] = rcp.get(ix);
            }

            ShapedRecipes r;
            try {
               r = MCH_Recipes.addShapedRecipe(name, new ItemStack(item, createNum), recipe);
            } catch (Exception var14) {
               MCH_Utils.logger().warn(var14.getMessage() + ", name:" + name);
               return null;
            }

            for (int ix = 0; ix < r.recipeItems.size(); ix++) {
               if (r.recipeItems.get(ix) != Ingredient.EMPTY
                  && Arrays.stream(((Ingredient)r.recipeItems.get(ix)).getMatchingStacks()).anyMatch(stack -> stack.getItem() == null)) {
                  throw new RuntimeException("Error: Invalid ShapedRecipes! " + item + " : " + data);
               }
            }

            return r;
         }
      }
   }

   @Nullable
   public static IRecipe addShapelessRecipe(String name, Item item, String data) {
      ArrayList<Object> rcp = new ArrayList<>();
      String[] s = data.split("\\s*,\\s*");
      if (s.length < 1) {
         return null;
      } else {
         int start = 0;
         int createNum = 1;
         if (isNumber(s[0]) && createNum <= 0) {
            createNum = 1;
         }

         for (int idx = start; idx < s.length; idx++) {
            if (s[idx].length() <= 0) {
               return null;
            }

            String nm = s[idx].trim().toLowerCase();
            int dmg = 0;
            if (idx + 1 < s.length && isNumber(s[idx + 1])) {
               dmg = Integer.parseInt(s[++idx]);
            }

            if (isNumber(nm)) {
               int n = Integer.parseInt(nm);
               if (n <= 255) {
                  rcp.add(new ItemStack(W_Block.getBlockById(n), 1, dmg));
               } else if (n <= 511) {
                  rcp.add(new ItemStack(W_Item.getItemById(n), 1, dmg));
               } else if (n <= 2255) {
                  rcp.add(new ItemStack(W_Block.getBlockById(n), 1, dmg));
               } else if (n <= 2267) {
                  rcp.add(new ItemStack(W_Item.getItemById(n), 1, dmg));
               } else if (n <= 4095) {
                  rcp.add(new ItemStack(W_Block.getBlockById(n), 1, dmg));
               } else if (n <= 31999) {
                  rcp.add(new ItemStack(W_Item.getItemById(n + 256), 1, dmg));
               }
            } else {
               rcp.add(new ItemStack(W_Item.getItemByName(nm), 1, dmg));
            }
         }

         Object[] recipe = new Object[rcp.size()];

         for (int i = 0; i < recipe.length; i++) {
            recipe[i] = rcp.get(i);
         }

         ShapelessRecipes r = getShapelessRecipe(new ItemStack(item, createNum), recipe);

         for (int i = 0; i < r.recipeItems.size(); i++) {
            Ingredient ingredient = (Ingredient)r.recipeItems.get(i);
            if (Arrays.stream(ingredient.getMatchingStacks()).anyMatch(stack -> stack.getItem() == null)) {
               throw new RuntimeException("Error: Invalid ShapelessRecipes! " + item + " : " + data);
            }
         }

         MCH_Recipes.register(name, r);
         return r;
      }
   }

   public static ShapelessRecipes getShapelessRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj) {
      NonNullList<Ingredient> list = NonNullList.create();

      for (Object object1 : par2ArrayOfObj) {
         if (object1 instanceof ItemStack) {
            list.add(Ingredient.fromStacks(new ItemStack[]{((ItemStack)object1).copy()}));
         } else if (object1 instanceof Item) {
            list.add(Ingredient.fromStacks(new ItemStack[]{new ItemStack((Item)object1)}));
         } else {
            if (!(object1 instanceof Block)) {
               throw new RuntimeException("Invalid shapeless recipy!");
            }

            list.add(Ingredient.fromStacks(new ItemStack[]{new ItemStack((Block)object1)}));
         }
      }

      return new ShapelessRecipes("", par1ItemStack, list);
   }

   public static boolean isNumber(@Nullable String s) {
      if (s != null && !s.isEmpty()) {
         byte[] buf = s.getBytes();

         for (byte b : buf) {
            if (b < 48 || b > 57) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
