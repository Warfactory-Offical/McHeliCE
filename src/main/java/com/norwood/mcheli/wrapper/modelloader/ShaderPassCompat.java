package com.norwood.mcheli.wrapper.modelloader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

final class ShaderPassCompat {

    private static final Method OPTIFINE_IS_SHADERS = resolveMethod("net.optifine.Config", "isShaders");
    private static final Field OPTIFINE_SHADER_PACK_LOADED =
            OPTIFINE_IS_SHADERS == null ? resolveField("net.optifine.shaders.Shaders", "shaderPackLoaded") : null;
    private static final Field NTM_RENDERING_CONSTANT = resolveField("com.hbm.main.ClientProxy", "renderingConstant");

    private ShaderPassCompat() {
    }

    static boolean needsClientArrayFallback() {
        return optifineShadersActive() || readStaticBoolean(NTM_RENDERING_CONSTANT);
    }

    private static boolean optifineShadersActive() {
        if (OPTIFINE_IS_SHADERS != null) {
            try {
                return Boolean.TRUE.equals(OPTIFINE_IS_SHADERS.invoke(null));
            } catch (Throwable ignored) {
                return false;
            }
        }
        return readStaticBoolean(OPTIFINE_SHADER_PACK_LOADED);
    }

    private static boolean readStaticBoolean(Field field) {
        if (field == null) {
            return false;
        }
        try {
            return field.getBoolean(null);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static Method resolveMethod(String className, String methodName) {
        try {
            Method method = Class.forName(className).getMethod(methodName);
            method.setAccessible(true);
            return method;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Field resolveField(String className, String fieldName) {
        try {
            Field field = Class.forName(className).getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Throwable ignored) {
            return null;
        }
    }
}
