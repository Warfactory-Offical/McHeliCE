package com.norwood.mcheli.__helper.criterion;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.ICriterionTrigger.Listener;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;

public class MCH_SimpleListeners {
   private final Set<Listener<MCH_SimpleListeners.SimpleInstance>> listeners = Sets.newHashSet();
   private final PlayerAdvancements playerAdvancements;

   public MCH_SimpleListeners(PlayerAdvancements playerAdvancements) {
      this.playerAdvancements = playerAdvancements;
   }

   public boolean isEmpty() {
      return this.listeners.isEmpty();
   }

   public void add(Listener<MCH_SimpleListeners.SimpleInstance> listener) {
      this.listeners.add(listener);
   }

   public void remove(Listener<MCH_SimpleListeners.SimpleInstance> listener) {
      this.listeners.remove(listener);
   }

   public void trigger() {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         Listener<MCH_SimpleListeners.SimpleInstance> listener = (Listener)var1.next();
         listener.func_192159_a(this.playerAdvancements);
      }

   }

   static class SimpleInstance extends AbstractCriterionInstance {
      public SimpleInstance(ResourceLocation criterionIn) {
         super(criterionIn);
      }
   }
}
