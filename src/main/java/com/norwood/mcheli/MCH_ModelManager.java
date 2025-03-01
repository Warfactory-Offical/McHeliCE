package com.norwood.mcheli;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.client.MCH_Models;
import com.norwood.mcheli.__helper.client._IModelCustom;
import com.norwood.mcheli.wrapper.W_ModelBase;
import com.norwood.mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MCH_ModelManager extends W_ModelBase {
   private static MCH_ModelManager instance = new MCH_ModelManager();
   private static HashMap<String, _IModelCustom> map = new HashMap();
   private static ModelRenderer defaultModel = null;
   private static boolean forceReloadMode = false;
   private static Random rand;

   private MCH_ModelManager() {
   }

   public static void setForceReloadMode(boolean b) {
      forceReloadMode = b;
   }

   @Nullable
   public static _IModelCustom load(String path, String name) {
      return name != null && !name.isEmpty() ? load(path + "/" + name) : null;
   }

   @Nullable
   public static _IModelCustom load(String name) {
      if (name != null && !name.isEmpty()) {
         _IModelCustom obj = (_IModelCustom)map.get(name);
         if (obj != null) {
            if (!forceReloadMode) {
               return obj;
            }

            map.remove(name);
         }

         _IModelCustom model = null;

         try {
            model = MCH_Models.loadModel(name);
         } catch (Exception var4) {
            var4.printStackTrace();
            model = null;
         }

         if (model != null) {
            map.put(name, model);
            return model;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void render(String path, String name) {
      render(path + "/" + name);
   }

   public static void render(String name) {
      _IModelCustom model = (_IModelCustom)map.get(name);
      if (model != null) {
         model.renderAll();
      } else if (defaultModel == null) {
      }

   }

   public static void renderPart(String name, String partName) {
      _IModelCustom model = (_IModelCustom)map.get(name);
      if (model != null) {
         model.renderPart(partName);
      }

   }

   public static void renderLine(String path, String name, int startLine, int maxLine) {
      _IModelCustom model = (_IModelCustom)map.get(path + "/" + name);
      if (model instanceof W_ModelCustom) {
         ((W_ModelCustom)model).renderAllLine(startLine, maxLine);
      }

   }

   public static void render(String path, String name, int startFace, int maxFace) {
      _IModelCustom model = (_IModelCustom)map.get(path + "/" + name);
      if (model instanceof W_ModelCustom) {
         ((W_ModelCustom)model).renderAll(startFace, maxFace);
      }

   }

   public static int getVertexNum(String path, String name) {
      _IModelCustom model = (_IModelCustom)map.get(path + "/" + name);
      return model instanceof W_ModelCustom ? ((W_ModelCustom)model).getVertexNum() : 0;
   }

   public static W_ModelCustom get(String path, String name) {
      _IModelCustom model = (_IModelCustom)map.get(path + "/" + name);
      return model instanceof W_ModelCustom ? (W_ModelCustom)model : null;
   }

   public static W_ModelCustom getRandome() {
      int size = map.size();

      for(int i = 0; i < 10; ++i) {
         int idx = 0;
         int index = rand.nextInt(size);

         for(Iterator var4 = map.values().iterator(); var4.hasNext(); ++idx) {
            _IModelCustom model = (_IModelCustom)var4.next();
            if (idx >= index && model instanceof W_ModelCustom) {
               return (W_ModelCustom)model;
            }
         }
      }

      return null;
   }

   public static boolean containsModel(String path, String name) {
      return containsModel(path + "/" + name);
   }

   public static boolean containsModel(String name) {
      return map.containsKey(name);
   }

   static {
      defaultModel = new ModelRenderer(instance, 0, 0);
      defaultModel.func_78790_a(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
      rand = new Random();
   }
}
