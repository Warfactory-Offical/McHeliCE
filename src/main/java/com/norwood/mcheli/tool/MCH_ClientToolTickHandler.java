package com.norwood.mcheli.tool;

import com.norwood.mcheli.MCH_ClientTickHandlerBase;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_Key;
import com.norwood.mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import com.norwood.mcheli.wrapper.W_McClient;
import com.norwood.mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MCH_ClientToolTickHandler extends MCH_ClientTickHandlerBase {
   public MCH_Key KeyUseItem;
   public MCH_Key KeyZoomIn;
   public MCH_Key KeyZoomOut;
   public MCH_Key KeySwitchMode;
   public MCH_Key[] Keys;

   public MCH_ClientToolTickHandler(Minecraft minecraft, MCH_Config config) {
      super(minecraft);
      this.updateKeybind(config);
   }

   public void updateKeybind(MCH_Config config) {
      this.KeyUseItem = new MCH_Key(MCH_Config.KeyAttack.prmInt);
      this.KeyZoomIn = new MCH_Key(MCH_Config.KeyZoom.prmInt);
      this.KeyZoomOut = new MCH_Key(MCH_Config.KeySwWeaponMode.prmInt);
      this.KeySwitchMode = new MCH_Key(MCH_Config.KeyFlare.prmInt);
      this.Keys = new MCH_Key[]{this.KeyUseItem, this.KeyZoomIn, this.KeyZoomOut, this.KeySwitchMode};
   }

   protected void onTick(boolean inGUI) {
      MCH_Key[] var2 = this.Keys;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MCH_Key k = var2[var4];
         k.update();
      }

      this.onTick_ItemWrench(inGUI, this.mc.field_71439_g);
      this.onTick_ItemRangeFinder(inGUI, this.mc.field_71439_g);
   }

   private void onTick_ItemRangeFinder(boolean inGUI, EntityPlayer player) {
      if (MCH_ItemRangeFinder.rangeFinderUseCooldown > 0) {
         --MCH_ItemRangeFinder.rangeFinderUseCooldown;
      }

      ItemStack itemStack = ItemStack.field_190927_a;
      if (player != null) {
         itemStack = player.func_184614_ca();
         if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemRangeFinder) {
            boolean usingItem = player.func_184612_cw() > 8 && MCH_ItemRangeFinder.canUse(player);
            if (!MCH_ItemRangeFinder.continueUsingItem && usingItem) {
               MCH_ItemRangeFinder.onStartUseItem();
            }

            if (usingItem) {
               if (this.KeyUseItem.isKeyDown()) {
                  ((MCH_ItemRangeFinder)itemStack.func_77973_b()).spotEntity(player, itemStack);
               }

               if (this.KeyZoomIn.isKeyPress() && MCH_ItemRangeFinder.zoom < 10.0F) {
                  MCH_ItemRangeFinder.zoom += MCH_ItemRangeFinder.zoom / 10.0F;
                  if (MCH_ItemRangeFinder.zoom > 10.0F) {
                     MCH_ItemRangeFinder.zoom = 10.0F;
                  }

                  W_McClient.MOD_playSoundFX("zoom", 0.05F, 1.0F);
                  W_Reflection.setCameraZoom(MCH_ItemRangeFinder.zoom);
               }

               if (this.KeyZoomOut.isKeyPress() && MCH_ItemRangeFinder.zoom > 1.2F) {
                  MCH_ItemRangeFinder.zoom -= MCH_ItemRangeFinder.zoom / 10.0F;
                  if (MCH_ItemRangeFinder.zoom < 1.2F) {
                     MCH_ItemRangeFinder.zoom = 1.2F;
                  }

                  W_McClient.MOD_playSoundFX("zoom", 0.05F, 0.9F);
                  W_Reflection.setCameraZoom(MCH_ItemRangeFinder.zoom);
               }

               if (this.KeySwitchMode.isKeyDown()) {
                  W_McClient.MOD_playSoundFX("lockon", 1.0F, 0.9F);
                  MCH_ItemRangeFinder.mode = (MCH_ItemRangeFinder.mode + 1) % 3;
                  if (this.mc.func_71356_B() && MCH_ItemRangeFinder.mode == 0) {
                     MCH_ItemRangeFinder.mode = 1;
                  }
               }
            }
         }
      }

      if (MCH_ItemRangeFinder.continueUsingItem && (itemStack.func_190926_b() || !(itemStack.func_77973_b() instanceof MCH_ItemRangeFinder))) {
         MCH_ItemRangeFinder.onStopUseItem();
      }

   }

   private void onTick_ItemWrench(boolean inGUI, EntityPlayer player) {
      if (player != null) {
         ItemStack itemStack = player.func_184614_ca();
         if (!itemStack.func_190926_b() && itemStack.func_77973_b() instanceof MCH_ItemWrench) {
            int maxdm = itemStack.func_77958_k();
            int dm = itemStack.func_77960_j();
            if (dm <= maxdm) {
               ItemStack renderItemstack = W_Reflection.getItemRendererMainHand();
               if (renderItemstack.func_190926_b() || itemStack.func_77973_b() == renderItemstack.func_77973_b()) {
                  W_Reflection.setItemRendererMainHand(player.field_71071_by.func_70448_g());
               }
            }
         }

      }
   }
}
