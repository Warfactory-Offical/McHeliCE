package com.norwood.mcheli.__helper.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.norwood.mcheli.MCH_IRecipeList;
import com.norwood.mcheli.MCH_ItemRecipe;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.helicopter.MCH_HeliInfoManager;
import com.norwood.mcheli.plane.MCP_PlaneInfoManager;
import com.norwood.mcheli.tank.MCH_TankInfoManager;
import com.norwood.mcheli.vehicle.MCH_VehicleInfoManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RecipeDescriptionManager {
   private static final Map<ResourceLocation, RecipeDescriptionManager.DescriptionInfo> INFO_TABLE = Maps.newHashMap();

   public static void registerDescriptionInfos(IResourceManager resourceManager) {
      INFO_TABLE.clear();
      registerDescriptions(resourceManager, MCH_ItemRecipe.getInstance());
      registerDescriptions(resourceManager, MCH_HeliInfoManager.getInstance());
      registerDescriptions(resourceManager, MCP_PlaneInfoManager.getInstance());
      registerDescriptions(resourceManager, MCH_TankInfoManager.getInstance());
      registerDescriptions(resourceManager, MCH_VehicleInfoManager.getInstance());
   }

   private static void registerDescriptions(IResourceManager resourceManager, MCH_IRecipeList recipeList) {
      for(int i = 0; i < recipeList.getRecipeListSize(); ++i) {
         IRecipe recipe = recipeList.getRecipe(i);
         RecipeDescriptionManager.DescriptionInfo info = createDescriptionInfo(resourceManager, recipe);
         ResourceLocation registryName = recipe.getRegistryName();
         INFO_TABLE.put(registryName, info);
      }

   }

   private static RecipeDescriptionManager.DescriptionInfo createDescriptionInfo(IResourceManager resourceManager, IRecipe recipe) {
      List<ResourceLocation> textures = Lists.newLinkedList();

      for(int i = 0; i < 20; ++i) {
         String itemName = recipe.func_77571_b().func_77977_a();
         if (itemName.startsWith("tile.")) {
            itemName = itemName.substring(5);
         }

         if (itemName.indexOf(":") >= 0) {
            itemName = itemName.substring(itemName.indexOf(":") + 1);
         }

         String filepath = "textures/drafting_table_desc/" + itemName + "#" + i + ".png";

         try {
            IResource resource = resourceManager.func_110536_a(MCH_Utils.suffix(filepath));
            Throwable var7 = null;

            try {
               textures.add(resource.func_177241_a());
            } catch (Throwable var18) {
               var7 = var18;
               throw var18;
            } finally {
               if (resource != null) {
                  if (var7 != null) {
                     try {
                        resource.close();
                     } catch (Throwable var17) {
                        var7.addSuppressed(var17);
                     }
                  } else {
                     resource.close();
                  }
               }

            }
         } catch (FileNotFoundException var20) {
         } catch (IOException var21) {
            var21.printStackTrace();
         }
      }

      return new RecipeDescriptionManager.DescriptionInfo(textures);
   }

   public static ImmutableList<ResourceLocation> getDescriptionTextures(ResourceLocation recipeRegistryName) {
      return ((RecipeDescriptionManager.DescriptionInfo)INFO_TABLE.getOrDefault(recipeRegistryName, new RecipeDescriptionManager.DescriptionInfo(Collections.emptyList()))).getTextures();
   }

   static class DescriptionInfo {
      private ImmutableList<ResourceLocation> textures;

      public DescriptionInfo(List<ResourceLocation> textures) {
         this.textures = ImmutableList.copyOf(textures);
      }

      public ImmutableList<ResourceLocation> getTextures() {
         return this.textures;
      }
   }
}
