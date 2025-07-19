package com.norwood.mcheli;

import com.norwood.mcheli.wrapper.W_KeyBinding;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class MCH_Key {
   public int key;
   private boolean isPress;
   private boolean isBeforePress;

   public MCH_Key(int k) {
      this.key = k;
      this.isPress = false;
      this.isBeforePress = false;
   }

   public boolean isKeyDown() {
      return !this.isBeforePress && this.isPress;
   }

   public boolean isKeyPress() {
      return this.isPress;
   }

   public boolean isKeyUp() {
      return this.isBeforePress && !this.isPress;
   }

   public void update() {
      if (this.key != 0) {
         this.isBeforePress = this.isPress;
         if (this.key >= 0) {
            this.isPress = Keyboard.isKeyDown(this.key);
         } else {
            this.isPress = Mouse.isButtonDown(this.key + 100);
         }
      }
   }

   public static boolean isKeyDown(int key) {
      if (key > 0) {
         return Keyboard.isKeyDown(key);
      } else {
         return key < 0 ? Mouse.isButtonDown(key + 100) : false;
      }
   }

   public static boolean isKeyDown(KeyBinding keyBind) {
      return isKeyDown(W_KeyBinding.getKeyCode(keyBind));
   }
}
