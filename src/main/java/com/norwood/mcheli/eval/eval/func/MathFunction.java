package com.norwood.mcheli.eval.eval.func;

import java.lang.reflect.Method;

public class MathFunction implements Function {
    @Override
    public long evalLong(Object object, String name, Long[] args) throws Throwable {
        Class<?>[] types = new Class[args.length];

        for (int i = 0; i < types.length; i++) {
            types[i] = long.class;
        }

        Method m = Math.class.getMethod(name, types);
        Object ret = m.invoke(null, args);
        return (Long) ret;
    }

    @Override
    public double evalDouble(Object object, String name, Double[] args) throws Throwable {
        Class<?>[] types = new Class[args.length];

        for (int i = 0; i < types.length; i++) {
            types[i] = double.class;
        }

        Method m = Math.class.getMethod(name, types);
        Object ret = m.invoke(null, args);
        return (Double) ret;
    }

    @Override
    public Object evalObject(Object object, String name, Object[] args) throws Throwable {
        Class<?>[] types = new Class[args.length];

        for (int i = 0; i < types.length; i++) {
            Class<?> c = args[i].getClass();
            if (Double.class.isAssignableFrom(c)) {
                c = double.class;
            } else if (Float.class.isAssignableFrom(c)) {
                c = float.class;
            } else if (Integer.class.isAssignableFrom(c)) {
                c = int.class;
            } else if (Number.class.isAssignableFrom(c)) {
                c = long.class;
            }

            types[i] = c;
        }

        Method m = Math.class.getMethod(name, types);
        return m.invoke(null, args);
    }
}
