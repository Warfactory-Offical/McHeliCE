package com.norwood.mcheli.debug._v1.model;

import java.util.List;
import com.norwood.mcheli.__helper.debug.DebugInfoObject;
import com.norwood.mcheli.debug._v1.PrintStreamWrapper;

public class MqoModel implements DebugInfoObject {
   private List<_GroupObject> groupObjects;
   private int vertexNum;
   private int faceNum;

   public MqoModel(List<_GroupObject> groupObjects, int verts, int faces) {
      this.groupObjects = groupObjects;
      this.vertexNum = verts;
      this.faceNum = faces;
   }

   @Override
   public String toString() {
      return "MqoModel[verts:" + this.vertexNum + ", face:" + this.faceNum + ", obj:" + this.groupObjects + "]";
   }

   @Override
   public void printInfo(PrintStreamWrapper stream) {
      stream.push("Mqo Model Info:");
      this.groupObjects.forEach(g -> g.printInfo(stream));
      stream.pop();
      stream.println();
   }
}
