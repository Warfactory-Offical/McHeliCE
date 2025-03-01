package com.norwood.mcheli.__helper.debug;

public class DebugTrace {
   public static void printOutTraceback() {
      (new RuntimeException()).printStackTrace(System.out);
   }
}
