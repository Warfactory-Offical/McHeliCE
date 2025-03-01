package com.norwood.mcheli.eval.eval.func;

public class VoidFunction implements Function {
   public long evalLong(Object object, String name, Long[] args) throws Throwable {
      System.out.println(object + "." + name + "関数が呼ばれた(long)");

      for(int i = 0; i < args.length; ++i) {
         System.out.println("arg[" + i + "] " + args[i]);
      }

      return 0L;
   }

   public double evalDouble(Object object, String name, Double[] args) throws Throwable {
      System.out.println(object + "." + name + "関数が呼ばれた(double)");

      for(int i = 0; i < args.length; ++i) {
         System.out.println("arg[" + i + "] " + args[i]);
      }

      return 0.0D;
   }

   public Object evalObject(Object object, String name, Object[] args) throws Throwable {
      System.out.println(object + "." + name + "関数が呼ばれた(Object)");

      for(int i = 0; i < args.length; ++i) {
         System.out.println("arg[" + i + "] " + args[i]);
      }

      return null;
   }
}
