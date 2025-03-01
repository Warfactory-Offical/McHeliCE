package com.norwood.mcheli.eval.eval.func;

import java.lang.reflect.Method;

public class MathFunction implements Function {
   public long evalLong(Object object, String name, Long[] args) throws Throwable {
      Class<?>[] types = new Class[args.length];

      for(int i = 0; i < types.length; ++i) {
         types[i] = Long.TYPE;
      }

      Method m = Math.class.getMethod(name, types);
      Object ret = m.invoke((Object)null, (Object[])args);
      return (Long)ret;
   }

   public double evalDouble(Object object, String name, Double[] args) throws Throwable {
      Class<?>[] types = new Class[args.length];

      for(int i = 0; i < types.length; ++i) {
         types[i] = Double.TYPE;
      }

      Method m = Math.class.getMethod(name, types);
      Object ret = m.invoke((Object)null, (Object[])args);
      return (Double)ret;
   }

   public Object evalObject(Object object, String name, Object[] args) throws Throwable {
      Class<?>[] types = new Class[args.length];

      for(int i = 0; i < types.length; ++i) {
         Class<?> c = args[i].getClass();
         if (Double.class.isAssignableFrom(c)) {
            c = Double.TYPE;
         } else if (Float.class.isAssignableFrom(c)) {
            c = Float.TYPE;
         } else if (Integer.class.isAssignableFrom(c)) {
            c = Integer.TYPE;
         } else if (Number.class.isAssignableFrom(c)) {
            c = Long.TYPE;
         }

         types[i] = c;
      }

      Method m = Math.class.getMethod(name, types);
      return m.invoke((Object)null, args);
   }
}
