package com.norwood.mcheli.hud;

public class MCH_HudItemConditional extends MCH_HudItem {
   private final boolean isEndif;
   private final String conditional;

   public MCH_HudItemConditional(int fileLine, boolean isEndif, String conditional) {
      super(fileLine);
      this.isEndif = isEndif;
      this.conditional = conditional;
   }

   public boolean canExecute() {
      return true;
   }

   public void execute() {
      if (!this.isEndif) {
         this.parent.isIfFalse = calc(this.conditional) == 0.0D;
      } else {
         this.parent.isIfFalse = false;
      }

   }
}
