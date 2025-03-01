package com.norwood.mcheli.__helper.client.renderer;

import com.google.common.collect.Queues;
import java.util.Deque;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

@SideOnly(Side.CLIENT)
public class GlUtil extends GlStateManager {
   private static final GlUtil.StencilState stencilState = new GlUtil.StencilState();
   private static final GlUtil.ClearState clearState = new GlUtil.ClearState();
   private static final GlUtil.BooleanState scissorState = new GlUtil.BooleanState(3089);
   private static final GlUtil.BooleanState stippleState = new GlUtil.BooleanState(2852);
   private static final Deque<Float> lineWidthState = Queues.newArrayDeque();
   private static final Deque<Float> pointSizeState = Queues.newArrayDeque();
   private static final Deque<Pair<Integer, Integer>> blendFancState = Queues.newArrayDeque();

   public static float getFloat(int pname) {
      return GL11.glGetFloat(pname);
   }

   public static float getLineWidth() {
      return GL11.glGetFloat(2849);
   }

   public static float getPointSize() {
      return GL11.glGetFloat(2833);
   }

   public static int getBlendSrcFactor() {
      return GL11.glGetInteger(3041);
   }

   public static int getBlendDstFactor() {
      return GL11.glGetInteger(3040);
   }

   public static void pointSize(float size) {
      GL11.glPointSize(size);
   }

   public static void pushLineWidth(float width) {
      lineWidthState.push(getLineWidth());
      func_187441_d(width);
   }

   public static float popLineWidth() {
      float f = (Float)lineWidthState.pop();
      func_187441_d(f);
      return f;
   }

   public static void pushPointSize(float size) {
      pointSizeState.push(getPointSize());
      pointSize(size);
   }

   public static float popPointSize() {
      float f = (Float)pointSizeState.pop();
      pointSize(f);
      return f;
   }

   public static void pushBlendFunc(SourceFactor srcFactor, DestFactor dstFactor) {
      pushBlendFunc(srcFactor.field_187395_p, dstFactor.field_187345_o);
   }

   public static void pushBlendFunc(int srcFactor, int dstFactor) {
      blendFancState.push(Pair.of(getBlendSrcFactor(), getBlendDstFactor()));
      func_179112_b(srcFactor, dstFactor);
   }

   public static void popBlendFunc() {
      Pair<Integer, Integer> func = (Pair)blendFancState.pop();
      func_179112_b((Integer)func.getLeft(), (Integer)func.getRight());
   }

   public static void polygonMode(CullFace face, GlUtil.RasterizeType mode) {
      func_187409_d(face.field_187328_d, mode.mode);
   }

   public static void depthFunc(GlUtil.Function depthFunc) {
      func_179143_c(depthFunc.func);
   }

   public static void clearStencilBufferBit() {
      func_179086_m(1024);
   }

   public static void clearStencil(int stencil) {
      if (stencil != clearState.stencil) {
         clearState.stencil = stencil;
         GL11.glClearStencil(stencil);
      }

   }

   public static void enableStencil() {
      stencilState.stencilTest.setEnabled();
   }

   public static void disableStencil() {
      stencilState.stencilTest.setDisabled();
   }

   public static void stencilFunc(GlUtil.Function stencilFunc, int ref, int mask) {
      stencilFunc(stencilFunc.func, ref, mask);
   }

   public static void stencilFunc(int stencilFunc, int ref, int mask) {
      if (stencilFunc != stencilState.func.func || ref != stencilState.func.ref || mask != stencilState.func.mask) {
         stencilState.func.func = stencilFunc;
         stencilState.func.ref = ref;
         stencilState.func.mask = mask;
         GL11.glStencilFunc(stencilFunc, ref, mask);
      }

   }

   public static void stencilMask(int mask) {
      if (mask != stencilState.mask) {
         stencilState.mask = mask;
         GL11.glStencilMask(mask);
      }

   }

   public static void stencilOp(GlUtil.StencilAction fail, GlUtil.StencilAction zfail, GlUtil.StencilAction zpass) {
      stencilOp(fail.action, zfail.action, zpass.action);
   }

   public static void stencilOp(int fail, int zfail, int zpass) {
      if (fail != stencilState.fail || zfail != stencilState.zfail || zpass != stencilState.zpass) {
         stencilState.fail = fail;
         stencilState.zfail = zfail;
         stencilState.zpass = zpass;
         GL11.glStencilOp(fail, zfail, zpass);
      }

   }

   public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
      Sphere s = new Sphere();
      GL11.glPushMatrix();
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(1.2F);
      GL11.glDisable(3553);
      s.setDrawStyle(100013);
      GL11.glTranslatef((float)x, (float)y, (float)z);
      s.draw(size, slices, stacks);
      GL11.glLineWidth(2.0F);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void enableScissor() {
      scissorState.setEnabled();
   }

   public static void disableScissor() {
      scissorState.setDisabled();
   }

   public static void scissor(int x, int y, int width, int height) {
      GL11.glScissor(x, y, width, height);
   }

   public static void scissorGui(Minecraft mc, int x, int y, int width, int height) {
      ScaledResolution res = new ScaledResolution(mc);
      double scaleW = (double)mc.field_71443_c / res.func_78327_c();
      double scaleH = (double)mc.field_71440_d / res.func_78324_d();
      scissor((int)((double)x * scaleW), (int)((double)mc.field_71440_d - (double)(y + height) * scaleH), (int)((double)width * scaleW), (int)((double)height * scaleH));
   }

   public static void enableStipple() {
      stippleState.setEnabled();
   }

   public static void disableStipple() {
      stippleState.setDisabled();
   }

   public static void lineStipple(int factor, short pattern) {
      GL11.glLineStipple(factor, pattern);
   }

   public static boolean enableStencilBuffer() {
      Framebuffer framebuffer = Minecraft.func_71410_x().func_147110_a();
      if (!framebuffer.isStencilEnabled() && OpenGlHelper.func_148822_b()) {
         framebuffer.enableStencil();
         return true;
      } else {
         return false;
      }
   }

   public static boolean isEnableStencilBuffer() {
      return Minecraft.func_71410_x().func_147110_a().isStencilEnabled();
   }

   public static Matrix4f translateAsMatrix(float x, float y, float z) {
      return TRSRTransformation.mul(new Vector3f(x, y, z), (Quat4f)null, (Vector3f)null, (Quat4f)null);
   }

   public static Matrix4f rotateAsMatrix(float angle, float x, float y, float z) {
      Quat4f quat = new Quat4f(0.0F, 0.0F, 0.0F, 1.0F);
      Quat4f t = new Quat4f();
      t.set(new AxisAngle4f(x, y, z, angle * 0.017453292F));
      quat.mul(t);
      return TRSRTransformation.mul((Vector3f)null, quat, (Vector3f)null, (Quat4f)null);
   }

   public static Matrix4f scaleAsMatrix(float x, float y, float z) {
      return TRSRTransformation.mul((Vector3f)null, (Quat4f)null, new Vector3f(x, y, z), (Quat4f)null);
   }

   @SideOnly(Side.CLIENT)
   protected static class BooleanState {
      private final int capability;
      private boolean currentState;

      public BooleanState(int capabilityIn) {
         this.capability = capabilityIn;
      }

      public void setDisabled() {
         this.setState(false);
      }

      public void setEnabled() {
         this.setState(true);
      }

      public void setState(boolean state) {
         if (state != this.currentState) {
            this.currentState = state;
            if (state) {
               GL11.glEnable(this.capability);
            } else {
               GL11.glDisable(this.capability);
            }
         }

      }
   }

   @SideOnly(Side.CLIENT)
   public static enum RasterizeType {
      POINT(6912),
      LINE(6913),
      FILL(6914);

      public final int mode;

      private RasterizeType(int mode) {
         this.mode = mode;
      }
   }

   @SideOnly(Side.CLIENT)
   static class StencilState {
      public GlUtil.BooleanState stencilTest;
      public GlUtil.StencilFunc func;
      public int mask;
      public int fail;
      public int zfail;
      public int zpass;

      private StencilState() {
         this.stencilTest = new GlUtil.BooleanState(2960);
         this.func = new GlUtil.StencilFunc();
         this.mask = -1;
         this.fail = 7680;
         this.zfail = 7680;
         this.zpass = 7680;
      }

      // $FF: synthetic method
      StencilState(Object x0) {
         this();
      }
   }

   @SideOnly(Side.CLIENT)
   static class StencilFunc {
      public int func;
      public int ref;
      public int mask;

      private StencilFunc() {
         this.func = 519;
         this.ref = 0;
         this.mask = -1;
      }

      // $FF: synthetic method
      StencilFunc(Object x0) {
         this();
      }
   }

   @SideOnly(Side.CLIENT)
   public static enum StencilAction {
      KEEP(7680),
      ZERO(0),
      INCR(7682),
      INCR_WRAP(34055),
      DECR(7683),
      DECR_WRAP(34056),
      REPLACE(7681),
      INVERT(5386);

      public final int action;

      private StencilAction(int action) {
         this.action = action;
      }
   }

   @SideOnly(Side.CLIENT)
   public static enum Function {
      NEVER(512),
      LESS(513),
      EQUAL(514),
      LEQUAL(515),
      GREATER(516),
      NOTEQUAL(517),
      GEQUAL(518),
      ALWAYS(519);

      public final int func;

      private Function(int func) {
         this.func = func;
      }
   }

   @SideOnly(Side.CLIENT)
   static class ClearState {
      public int stencil;

      private ClearState() {
         this.stencil = 0;
      }

      // $FF: synthetic method
      ClearState(Object x0) {
         this();
      }
   }
}
