package com.norwood.mcheli.aircraft;

import java.util.ArrayList;
import java.util.List;
import com.norwood.mcheli.MCH_IRecipeList;
import com.norwood.mcheli.MCH_InfoManagerBase;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

public abstract class MCH_AircraftInfoManager<T extends MCH_AircraftInfo> extends MCH_InfoManagerBase<T> implements MCH_IRecipeList {
   private List<IRecipe> listItemRecipe = new ArrayList();

   public int getRecipeListSize() {
      return this.listItemRecipe.size();
   }

   public IRecipe getRecipe(int index) {
      return (IRecipe)this.listItemRecipe.get(index);
   }

   public void addRecipe(IRecipe recipe, int count, String name, String recipeString) {
      if (recipe != null && recipe.func_77571_b() != null && recipe.func_77571_b().func_77973_b() != null) {
         this.listItemRecipe.add(recipe);
      } else {
         throw new RuntimeException("[mcheli]Recipe Parameter Error! recipe" + count + " : " + name + ".txt : " + recipe + " : " + recipeString);
      }
   }

   public abstract MCH_AircraftInfo getAcInfoFromItem(Item var1);

   public MCH_AircraftInfo getAcInfoFromItem(IRecipe recipe) {
      return recipe != null ? this.getAcInfoFromItem(recipe.func_77571_b().func_77973_b()) : null;
   }
}
