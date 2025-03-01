package com.norwood.mcheli;

import java.nio.FloatBuffer;
import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.entity.ITargetMarkerObject;
import org.lwjgl.BufferUtils;

public class MCH_MarkEntityPos {
   public FloatBuffer pos;
   public int type;
   private ITargetMarkerObject target;

   public MCH_MarkEntityPos(int type, ITargetMarkerObject target) {
      this.type = type;
      this.pos = BufferUtils.createFloatBuffer(3);
      this.target = target;
   }

   public MCH_MarkEntityPos(int type) {
      this(type, (ITargetMarkerObject)null);
   }

   @Nullable
   public ITargetMarkerObject getTarget() {
      return this.target;
   }
}
