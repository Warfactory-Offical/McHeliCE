package com.norwood.mcheli.wrapper.modelloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import com.norwood.mcheli.__helper.client._ModelFormatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class W_MetasequoiaObject extends W_ModelCustom {
   public ArrayList<W_Vertex> vertices = new ArrayList();
   public ArrayList<W_GroupObject> groupObjects = new ArrayList();
   private W_GroupObject currentGroupObject = null;
   private String fileName;
   private int vertexNum = 0;
   private int faceNum = 0;

   public W_MetasequoiaObject(ResourceLocation location, IResource resource) throws _ModelFormatException {
      this.fileName = resource.toString();
      this.loadObjModel(resource.func_110527_b());
   }

   public W_MetasequoiaObject(ResourceLocation resource) throws _ModelFormatException {
      this.fileName = resource.toString();

      try {
         IResource res = Minecraft.func_71410_x().func_110442_L().func_110536_a(resource);
         this.loadObjModel(res.func_110527_b());
      } catch (IOException var3) {
         throw new _ModelFormatException("IO Exception reading model format:" + this.fileName, var3);
      }
   }

   public W_MetasequoiaObject(String fileName, URL resource) throws _ModelFormatException {
      this.fileName = fileName;

      try {
         this.loadObjModel(resource.openStream());
      } catch (IOException var4) {
         throw new _ModelFormatException("IO Exception reading model format:" + this.fileName, var4);
      }
   }

   public W_MetasequoiaObject(String filename, InputStream inputStream) throws _ModelFormatException {
      this.fileName = filename;
      this.loadObjModel(inputStream);
   }

   public boolean containsPart(String partName) {
      Iterator var2 = this.groupObjects.iterator();

      W_GroupObject groupObject;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         groupObject = (W_GroupObject)var2.next();
      } while(!partName.equalsIgnoreCase(groupObject.name));

      return true;
   }

   private void loadObjModel(InputStream inputStream) throws _ModelFormatException {
      BufferedReader reader = null;
      String currentLine = null;
      int lineCount = 0;

      try {
         reader = new BufferedReader(new InputStreamReader(inputStream));

         while((currentLine = reader.readLine()) != null) {
            ++lineCount;
            currentLine = currentLine.replaceAll("\\s+", " ").trim();
            if (isValidGroupObjectLine(currentLine)) {
               W_GroupObject group = this.parseGroupObject(currentLine, lineCount);
               if (group != null) {
                  group.glDrawingMode = 4;
                  this.vertices.clear();
                  int vertexNum = 0;
                  boolean mirror = false;
                  double facet = Math.cos(0.785398163375D);
                  boolean shading = false;

                  String[] s;
                  while((currentLine = reader.readLine()) != null) {
                     ++lineCount;
                     currentLine = currentLine.replaceAll("\\s+", " ").trim();
                     if (currentLine.equalsIgnoreCase("mirror 1")) {
                        mirror = true;
                     }

                     if (currentLine.equalsIgnoreCase("shading 1")) {
                        shading = true;
                     }

                     s = currentLine.split(" ");
                     if (s.length == 2 && s[0].equalsIgnoreCase("facet")) {
                        facet = Math.cos(Double.parseDouble(s[1]) * 3.1415926535D / 180.0D);
                     }

                     if (isValidVertexLine(currentLine)) {
                        vertexNum = Integer.valueOf(currentLine.split(" ")[1]);
                        break;
                     }
                  }

                  if (vertexNum > 0) {
                     while((currentLine = reader.readLine()) != null) {
                        ++lineCount;
                        currentLine = currentLine.replaceAll("\\s+", " ").trim();
                        s = currentLine.split(" ");
                        if (s.length == 3) {
                           W_Vertex v = new W_Vertex(Float.valueOf(s[0]) / 100.0F, Float.valueOf(s[1]) / 100.0F, Float.valueOf(s[2]) / 100.0F);
                           this.checkMinMax(v);
                           this.vertices.add(v);
                           --vertexNum;
                           if (vertexNum <= 0) {
                              break;
                           }
                        } else if (s.length > 0) {
                           throw new _ModelFormatException("format error : " + this.fileName + " : line=" + lineCount);
                        }
                     }

                     int faceNum = 0;

                     while((currentLine = reader.readLine()) != null) {
                        ++lineCount;
                        currentLine = currentLine.replaceAll("\\s+", " ").trim();
                        if (isValidFaceLine(currentLine)) {
                           faceNum = Integer.valueOf(currentLine.split(" ")[1]);
                           break;
                        }
                     }

                     if (faceNum > 0) {
                        while((currentLine = reader.readLine()) != null) {
                           ++lineCount;
                           currentLine = currentLine.replaceAll("\\s+", " ").trim();
                           String[] s = currentLine.split(" ");
                           if (s.length <= 2) {
                              if (s.length > 2 && Integer.valueOf(s[0]) != 3) {
                                 throw new _ModelFormatException("found face is not triangle : " + this.fileName + " : line=" + lineCount);
                              }
                           } else {
                              if (Integer.valueOf(s[0]) >= 3) {
                                 W_Face[] faces = this.parseFace(currentLine, lineCount, mirror);
                                 W_Face[] var14 = faces;
                                 int var15 = faces.length;

                                 for(int var16 = 0; var16 < var15; ++var16) {
                                    W_Face face = var14[var16];
                                    group.faces.add(face);
                                 }
                              }

                              --faceNum;
                              if (faceNum <= 0) {
                                 break;
                              }
                           }
                        }

                        this.calcVerticesNormal(group, shading, facet);
                     }
                  }

                  this.vertexNum += this.vertices.size();
                  this.faceNum += group.faces.size();
                  this.vertices.clear();
                  this.groupObjects.add(group);
               }
            }
         }
      } catch (IOException var28) {
         throw new _ModelFormatException("IO Exception reading model format : " + this.fileName, var28);
      } finally {
         this.checkMinMaxFinal();
         this.vertices = null;

         try {
            reader.close();
         } catch (IOException var27) {
         }

         try {
            inputStream.close();
         } catch (IOException var26) {
         }

      }

   }

   public void calcVerticesNormal(W_GroupObject group, boolean shading, double facet) {
      Iterator var5 = group.faces.iterator();

      while(var5.hasNext()) {
         W_Face f = (W_Face)var5.next();
         f.vertexNormals = new W_Vertex[f.verticesID.length];

         for(int i = 0; i < f.verticesID.length; ++i) {
            W_Vertex vn = this.getVerticesNormalFromFace(f.faceNormal, f.verticesID[i], group, (float)facet);
            vn.normalize();
            if (shading) {
               if ((double)(f.faceNormal.x * vn.x + f.faceNormal.y * vn.y + f.faceNormal.z * vn.z) >= facet) {
                  f.vertexNormals[i] = vn;
               } else {
                  f.vertexNormals[i] = f.faceNormal;
               }
            } else {
               f.vertexNormals[i] = f.faceNormal;
            }
         }
      }

   }

   public W_Vertex getVerticesNormalFromFace(W_Vertex faceNormal, int verticesID, W_GroupObject group, float facet) {
      W_Vertex v = new W_Vertex(0.0F, 0.0F, 0.0F);
      Iterator var6 = group.faces.iterator();

      while(true) {
         while(var6.hasNext()) {
            W_Face f = (W_Face)var6.next();
            int[] var8 = f.verticesID;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               int id = var8[var10];
               if (id == verticesID) {
                  if (!(f.faceNormal.x * faceNormal.x + f.faceNormal.y * faceNormal.y + f.faceNormal.z * faceNormal.z < facet)) {
                     v.add(f.faceNormal);
                  }
                  break;
               }
            }
         }

         v.normalize();
         return v;
      }
   }

   public void renderAll() {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder builder = tessellator.func_178180_c();
      if (this.currentGroupObject != null) {
         builder.func_181668_a(this.currentGroupObject.glDrawingMode, DefaultVertexFormats.field_181710_j);
      } else {
         builder.func_181668_a(4, DefaultVertexFormats.field_181710_j);
      }

      this.tessellateAll(tessellator);
      tessellator.func_78381_a();
   }

   public void tessellateAll(Tessellator tessellator) {
      Iterator var2 = this.groupObjects.iterator();

      while(var2.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var2.next();
         groupObject.render(tessellator);
      }

   }

   public void renderOnly(String... groupNames) {
      Iterator var2 = this.groupObjects.iterator();

      while(var2.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var2.next();
         String[] var4 = groupNames;
         int var5 = groupNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String groupName = var4[var6];
            if (groupName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render();
            }
         }
      }

   }

   public void tessellateOnly(Tessellator tessellator, String... groupNames) {
      Iterator var3 = this.groupObjects.iterator();

      while(var3.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var3.next();
         String[] var5 = groupNames;
         int var6 = groupNames.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String groupName = var5[var7];
            if (groupName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render(tessellator);
            }
         }
      }

   }

   public void renderPart(String partName) {
      W_GroupObject groupObject;
      if (partName.charAt(0) == '$') {
         for(int i = 0; i < this.groupObjects.size(); ++i) {
            groupObject = (W_GroupObject)this.groupObjects.get(i);
            if (partName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render();
               ++i;

               while(i < this.groupObjects.size()) {
                  groupObject = (W_GroupObject)this.groupObjects.get(i);
                  if (groupObject.name.charAt(0) == '$') {
                     break;
                  }

                  groupObject.render();
                  ++i;
               }
            }
         }
      } else {
         Iterator var4 = this.groupObjects.iterator();

         while(var4.hasNext()) {
            groupObject = (W_GroupObject)var4.next();
            if (partName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render();
            }
         }
      }

   }

   public void tessellatePart(Tessellator tessellator, String partName) {
      Iterator var3 = this.groupObjects.iterator();

      while(var3.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var3.next();
         if (partName.equalsIgnoreCase(groupObject.name)) {
            groupObject.render(tessellator);
         }
      }

   }

   public void renderAllExcept(String... excludedGroupNames) {
      Iterator var2 = this.groupObjects.iterator();

      while(var2.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var2.next();
         boolean skipPart = false;
         String[] var5 = excludedGroupNames;
         int var6 = excludedGroupNames.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String excludedGroupName = var5[var7];
            if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
               skipPart = true;
            }
         }

         if (!skipPart) {
            groupObject.render();
         }
      }

   }

   public void tessellateAllExcept(Tessellator tessellator, String... excludedGroupNames) {
      Iterator var3 = this.groupObjects.iterator();

      while(var3.hasNext()) {
         W_GroupObject groupObject = (W_GroupObject)var3.next();
         boolean exclude = false;
         String[] var6 = excludedGroupNames;
         int var7 = excludedGroupNames.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String excludedGroupName = var6[var8];
            if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
               exclude = true;
            }
         }

         if (!exclude) {
            groupObject.render(tessellator);
         }
      }

   }

   private W_Face[] parseFace(String line, int lineCount, boolean mirror) {
      String[] s = line.split("[ VU)(M]+");
      int vnum = Integer.valueOf(s[0]);
      if (vnum != 3 && vnum != 4) {
         return new W_Face[0];
      } else {
         W_Face face1;
         if (vnum == 3) {
            face1 = new W_Face();
            face1.verticesID = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
            face1.vertices = new W_Vertex[]{(W_Vertex)this.vertices.get(face1.verticesID[0]), (W_Vertex)this.vertices.get(face1.verticesID[1]), (W_Vertex)this.vertices.get(face1.verticesID[2])};
            if (s.length >= 11) {
               face1.textureCoordinates = new W_TextureCoordinate[]{new W_TextureCoordinate(Float.valueOf(s[9]), Float.valueOf(s[10])), new W_TextureCoordinate(Float.valueOf(s[7]), Float.valueOf(s[8])), new W_TextureCoordinate(Float.valueOf(s[5]), Float.valueOf(s[6]))};
            } else {
               face1.textureCoordinates = new W_TextureCoordinate[]{new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F)};
            }

            face1.faceNormal = face1.calculateFaceNormal();
            return new W_Face[]{face1};
         } else {
            face1 = new W_Face();
            face1.verticesID = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
            face1.vertices = new W_Vertex[]{(W_Vertex)this.vertices.get(face1.verticesID[0]), (W_Vertex)this.vertices.get(face1.verticesID[1]), (W_Vertex)this.vertices.get(face1.verticesID[2])};
            if (s.length >= 12) {
               face1.textureCoordinates = new W_TextureCoordinate[]{new W_TextureCoordinate(Float.valueOf(s[10]), Float.valueOf(s[11])), new W_TextureCoordinate(Float.valueOf(s[8]), Float.valueOf(s[9])), new W_TextureCoordinate(Float.valueOf(s[6]), Float.valueOf(s[7]))};
            } else {
               face1.textureCoordinates = new W_TextureCoordinate[]{new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F)};
            }

            face1.faceNormal = face1.calculateFaceNormal();
            W_Face face2 = new W_Face();
            face2.verticesID = new int[]{Integer.valueOf(s[4]), Integer.valueOf(s[3]), Integer.valueOf(s[1])};
            face2.vertices = new W_Vertex[]{(W_Vertex)this.vertices.get(face2.verticesID[0]), (W_Vertex)this.vertices.get(face2.verticesID[1]), (W_Vertex)this.vertices.get(face2.verticesID[2])};
            if (s.length >= 14) {
               face2.textureCoordinates = new W_TextureCoordinate[]{new W_TextureCoordinate(Float.valueOf(s[12]), Float.valueOf(s[13])), new W_TextureCoordinate(Float.valueOf(s[10]), Float.valueOf(s[11])), new W_TextureCoordinate(Float.valueOf(s[6]), Float.valueOf(s[7]))};
            } else {
               face2.textureCoordinates = new W_TextureCoordinate[]{new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F), new W_TextureCoordinate(0.0F, 0.0F)};
            }

            face2.faceNormal = face2.calculateFaceNormal();
            return new W_Face[]{face1, face2};
         }
      }
   }

   private static boolean isValidGroupObjectLine(String line) {
      String[] s = line.split(" ");
      if (s.length >= 2 && s[0].equals("Object")) {
         return s[1].length() >= 4 && s[1].charAt(0) == '"';
      } else {
         return false;
      }
   }

   private W_GroupObject parseGroupObject(String line, int lineCount) throws _ModelFormatException {
      W_GroupObject group = null;
      if (isValidGroupObjectLine(line)) {
         String[] s = line.split(" ");
         String trimmedLine = s[1].substring(1, s[1].length() - 1);
         if (trimmedLine.length() > 0) {
            group = new W_GroupObject(trimmedLine);
         }

         return group;
      } else {
         throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
   }

   private static boolean isValidVertexLine(String line) {
      String[] s = line.split(" ");
      return s[0].equals("vertex");
   }

   private static boolean isValidFaceLine(String line) {
      String[] s = line.split(" ");
      return s[0].equals("face");
   }

   public String getType() {
      return "mqo";
   }

   public void renderAllLine(int startLine, int maxLine) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder builder = tessellator.func_178180_c();
      builder.func_181668_a(1, DefaultVertexFormats.field_181705_e);
      this.renderAllLine(tessellator, startLine, maxLine);
      tessellator.func_78381_a();
   }

   public void renderAllLine(Tessellator tessellator, int startLine, int maxLine) {
      int lineCnt = 0;
      BufferBuilder builder = tessellator.func_178180_c();
      Iterator var6 = this.groupObjects.iterator();

      while(true) {
         W_GroupObject groupObject;
         do {
            if (!var6.hasNext()) {
               return;
            }

            groupObject = (W_GroupObject)var6.next();
         } while(groupObject.faces.size() <= 0);

         Iterator var8 = groupObject.faces.iterator();

         while(var8.hasNext()) {
            W_Face face = (W_Face)var8.next();

            for(int i = 0; i < face.vertices.length / 3; ++i) {
               W_Vertex v1 = face.vertices[i * 3 + 0];
               W_Vertex v2 = face.vertices[i * 3 + 1];
               W_Vertex v3 = face.vertices[i * 3 + 2];
               ++lineCnt;
               if (lineCnt > maxLine) {
                  return;
               }

               builder.func_181662_b((double)v1.x, (double)v1.y, (double)v1.z).func_181675_d();
               builder.func_181662_b((double)v2.x, (double)v2.y, (double)v2.z).func_181675_d();
               ++lineCnt;
               if (lineCnt > maxLine) {
                  return;
               }

               builder.func_181662_b((double)v2.x, (double)v2.y, (double)v2.z).func_181675_d();
               builder.func_181662_b((double)v3.x, (double)v3.y, (double)v3.z).func_181675_d();
               ++lineCnt;
               if (lineCnt > maxLine) {
                  return;
               }

               builder.func_181662_b((double)v3.x, (double)v3.y, (double)v3.z).func_181675_d();
               builder.func_181662_b((double)v1.x, (double)v1.y, (double)v1.z).func_181675_d();
            }
         }
      }
   }

   public int getVertexNum() {
      return this.vertexNum;
   }

   public int getFaceNum() {
      return this.faceNum;
   }

   public void renderAll(int startFace, int maxFace) {
      if (startFace < 0) {
         startFace = 0;
      }

      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder builder = tessellator.func_178180_c();
      builder.func_181668_a(4, DefaultVertexFormats.field_181710_j);
      this.renderAll(tessellator, startFace, maxFace);
      tessellator.func_78381_a();
   }

   public void renderAll(Tessellator tessellator, int startFace, int maxLine) {
      int faceCnt = 0;
      Iterator var5 = this.groupObjects.iterator();

      while(true) {
         W_GroupObject groupObject;
         do {
            if (!var5.hasNext()) {
               return;
            }

            groupObject = (W_GroupObject)var5.next();
         } while(groupObject.faces.size() <= 0);

         Iterator var7 = groupObject.faces.iterator();

         while(var7.hasNext()) {
            W_Face face = (W_Face)var7.next();
            ++faceCnt;
            if (faceCnt >= startFace) {
               if (faceCnt > maxLine) {
                  return;
               }

               face.addFaceForRender(tessellator);
            }
         }
      }
   }
}
