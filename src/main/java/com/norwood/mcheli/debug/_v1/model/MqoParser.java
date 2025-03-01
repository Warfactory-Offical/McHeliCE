package com.norwood.mcheli.debug._v1.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.__helper.debug.DebugException;

public class MqoParser {
   public static MqoModel parse(InputStream inputStream) throws DebugException {
      List<_Vertex> vertices = new ArrayList();
      List<_Face> faceList = new ArrayList();
      List<_GroupObject> groupObjects = new ArrayList();
      int vertexNum = 0;
      int faceNum = 0;
      BufferedReader reader = null;
      String currentLine = null;
      int lineCount = 0;

      try {
         reader = new BufferedReader(new InputStreamReader(inputStream));

         while(true) {
            _GroupObject.Builder group;
            do {
               do {
                  if ((currentLine = reader.readLine()) == null) {
                     MqoModel var34 = new MqoModel(groupObjects, vertexNum, faceNum);
                     return var34;
                  }

                  ++lineCount;
                  currentLine = currentLine.replaceAll("\\s+", " ").trim();
               } while(!isValidGroupObjectLine(currentLine));

               group = parseGroupObject(currentLine, lineCount);
            } while(group == null);

            vertices.clear();
            faceList.clear();
            int vertexNum2 = 0;
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
                  vertexNum2 = Integer.valueOf(currentLine.split(" ")[1]);
                  break;
               }
            }

            if (vertexNum2 > 0) {
               while((currentLine = reader.readLine()) != null) {
                  ++lineCount;
                  currentLine = currentLine.replaceAll("\\s+", " ").trim();
                  s = currentLine.split(" ");
                  if (s.length == 3) {
                     _Vertex v = new _Vertex(Float.valueOf(s[0]) / 100.0F, Float.valueOf(s[1]) / 100.0F, Float.valueOf(s[2]) / 100.0F);
                     vertices.add(v);
                     --vertexNum2;
                     if (vertexNum2 <= 0) {
                        break;
                     }
                  } else if (s.length > 0) {
                     throw new DebugException("format error : line=" + lineCount);
                  }
               }

               int faceNum2 = 0;

               while((currentLine = reader.readLine()) != null) {
                  ++lineCount;
                  currentLine = currentLine.replaceAll("\\s+", " ").trim();
                  if (isValidFaceLine(currentLine)) {
                     faceNum2 = Integer.valueOf(currentLine.split(" ")[1]);
                     break;
                  }
               }

               if (faceNum2 > 0) {
                  while((currentLine = reader.readLine()) != null) {
                     ++lineCount;
                     currentLine = currentLine.replaceAll("\\s+", " ").trim();
                     String[] s = currentLine.split(" ");
                     if (s.length <= 2) {
                        if (s.length > 2 && Integer.valueOf(s[0]) != 3) {
                           throw new DebugException("found face is not triangle : line=" + lineCount);
                        }
                     } else {
                        if (Integer.valueOf(s[0]) >= 3) {
                           _Face[] faces = parseFace(currentLine, lineCount, mirror, vertices);
                           _Face[] var18 = faces;
                           int var19 = faces.length;

                           for(int var20 = 0; var20 < var19; ++var20) {
                              _Face face = var18[var20];
                              faceList.add(face);
                           }
                        }

                        --faceNum2;
                        if (faceNum2 <= 0) {
                           break;
                        }
                     }
                  }

                  Iterator var37 = faceList.iterator();

                  while(var37.hasNext()) {
                     _Face face = (_Face)var37.next();
                     group.addFace(face.calcVerticesNormal(faceList, shading, facet));
                  }
               }
            }

            vertexNum += vertices.size();
            faceNum += group.faceSize();
            vertices.clear();
            faceList.clear();
            groupObjects.add(group.build());
         }
      } catch (IOException var32) {
         throw new DebugException("IO Exception reading model format.", var32);
      } finally {
         try {
            reader.close();
         } catch (IOException var31) {
         }

         try {
            inputStream.close();
         } catch (IOException var30) {
         }

      }
   }

   private static _GroupObject.Builder parseGroupObject(String line, int lineCount) throws DebugException {
      _GroupObject.Builder group = null;
      if (isValidGroupObjectLine(line)) {
         String[] s = line.split(" ");
         String trimmedLine = s[1].substring(1, s[1].length() - 1);
         if (trimmedLine.length() > 0) {
            group = _GroupObject.builder().name(trimmedLine);
         }

         return group;
      } else {
         throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
      }
   }

   private static _Face[] parseFace(String line, int lineCount, boolean mirror, List<_Vertex> vertices) {
      String[] s = line.split("[ VU)(M]+");
      int vnum = Integer.valueOf(s[0]);
      if (vnum != 3 && vnum != 4) {
         return new _Face[0];
      } else {
         int[] verticesID1;
         _Vertex[] verts1;
         _TextureCoord[] texCoords1;
         if (vnum == 3) {
            verticesID1 = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
            verts1 = new _Vertex[]{(_Vertex)vertices.get(verticesID1[0]), (_Vertex)vertices.get(verticesID1[1]), (_Vertex)vertices.get(verticesID1[2])};
            if (s.length >= 11) {
               texCoords1 = new _TextureCoord[]{new _TextureCoord(Float.valueOf(s[9]), Float.valueOf(s[10])), new _TextureCoord(Float.valueOf(s[7]), Float.valueOf(s[8])), new _TextureCoord(Float.valueOf(s[5]), Float.valueOf(s[6]))};
            } else {
               texCoords1 = new _TextureCoord[]{new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F)};
            }

            return new _Face[]{new _Face(verticesID1, verts1, texCoords1)};
         } else {
            verticesID1 = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
            verts1 = new _Vertex[]{(_Vertex)vertices.get(verticesID1[0]), (_Vertex)vertices.get(verticesID1[1]), (_Vertex)vertices.get(verticesID1[2])};
            if (s.length >= 12) {
               texCoords1 = new _TextureCoord[]{new _TextureCoord(Float.valueOf(s[10]), Float.valueOf(s[11])), new _TextureCoord(Float.valueOf(s[8]), Float.valueOf(s[9])), new _TextureCoord(Float.valueOf(s[6]), Float.valueOf(s[7]))};
            } else {
               texCoords1 = new _TextureCoord[]{new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F)};
            }

            int[] verticesID2 = new int[]{Integer.valueOf(s[4]), Integer.valueOf(s[3]), Integer.valueOf(s[1])};
            _Vertex[] verts2 = new _Vertex[]{(_Vertex)vertices.get(verticesID2[0]), (_Vertex)vertices.get(verticesID2[1]), (_Vertex)vertices.get(verticesID2[2])};
            _TextureCoord[] texCoords2;
            if (s.length >= 14) {
               texCoords2 = new _TextureCoord[]{new _TextureCoord(Float.valueOf(s[12]), Float.valueOf(s[13])), new _TextureCoord(Float.valueOf(s[10]), Float.valueOf(s[11])), new _TextureCoord(Float.valueOf(s[6]), Float.valueOf(s[7]))};
            } else {
               texCoords2 = new _TextureCoord[]{new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F)};
            }

            return new _Face[]{new _Face(verticesID1, verts1, texCoords1), new _Face(verticesID2, verts2, texCoords2)};
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

   private static boolean isValidVertexLine(String line) {
      String[] s = line.split(" ");
      return s[0].equals("vertex");
   }

   private static boolean isValidFaceLine(String line) {
      String[] s = line.split(" ");
      return s[0].equals("face");
   }
}
