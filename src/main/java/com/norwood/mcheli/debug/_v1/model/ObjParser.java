package com.norwood.mcheli.debug._v1.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.norwood.mcheli.__helper.debug.DebugException;

public class ObjParser {
   private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
   private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
   private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
   private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
   private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
   private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
   private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
   private static Pattern groupObjectPattern = Pattern.compile("([go]( [-\\$\\w\\d]+) *\\n)|([go]( [-\\$\\w\\d]+) *$)");

   public static ObjModel parse(InputStream inputStream) throws DebugException {
      List<_Vertex> vertices = new ArrayList<>();
      List<_TextureCoord> textureCoordinates = new ArrayList<>();
      List<_Vertex> vertexNormals = new ArrayList<>();
      List<_GroupObject> groupObjects = new ArrayList<>();
      _GroupObject.Builder group = null;
      int vertexNum = 0;
      int faceNum = 0;
      BufferedReader reader = null;
      String currentLine = null;
      int lineCount = 0;

      ObjModel var31;
      try {
         reader = new BufferedReader(new InputStreamReader(inputStream));

         while ((currentLine = reader.readLine()) != null) {
            lineCount++;
            currentLine = currentLine.replaceAll("\\s+", " ").trim();
            if (!currentLine.startsWith("#") && currentLine.length() != 0) {
               if (currentLine.startsWith("v ")) {
                  _Vertex vertex = parseVertex(currentLine, lineCount);
                  if (vertex != null) {
                     vertices.add(vertex);
                  }
               } else if (currentLine.startsWith("vn ")) {
                  _Vertex vertex = parseVertexNormal(currentLine, lineCount);
                  if (vertex != null) {
                     vertexNormals.add(vertex);
                  }
               } else if (currentLine.startsWith("vt ")) {
                  _TextureCoord textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
                  if (textureCoordinate != null) {
                     textureCoordinates.add(textureCoordinate);
                  }
               } else if (currentLine.startsWith("f ")) {
                  if (group == null) {
                     group = _GroupObject.builder().name("Default");
                  }

                  _Face face = parseFace(currentLine, lineCount, vertices, textureCoordinates, vertexNormals);
                  if (face != null) {
                     group.addFace(face);
                  }
               } else if (currentLine.startsWith("g ") | currentLine.startsWith("o ") && currentLine.charAt(2) == '$') {
                  _GroupObject.Builder group2 = parseGroupObject(currentLine, lineCount);
                  if (group2 != null && group != null) {
                     groupObjects.add(group.build());
                  }

                  group = group2;
               }
            }
         }

         groupObjects.add(group.build());
         var31 = new ObjModel(groupObjects, vertexNum, faceNum);
      } catch (IOException var23) {
         throw new DebugException("IO Exception reading model format", var23);
      } finally {
         try {
            reader.close();
         } catch (IOException var22) {
         }

         try {
            inputStream.close();
         } catch (IOException var21) {
         }
      }

      return var31;
   }

   private static _Vertex parseVertex(String line, int lineCount) throws DebugException {
      _Vertex vertex = null;
      if (isValidVertexLine(line)) {
         line = line.substring(line.indexOf(" ") + 1);
         String[] tokens = line.split(" ");

         try {
            if (tokens.length == 2) {
               return new _Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
            } else {
               return tokens.length == 3 ? new _Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : vertex;
            }
         } catch (NumberFormatException var5) {
            throw new DebugException(String.format("Number formatting error at line %d", lineCount), var5);
         }
      } else {
         throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
      }
   }

   private static _Vertex parseVertexNormal(String line, int lineCount) throws DebugException {
      _Vertex vertexNormal = null;
      if (isValidVertexNormalLine(line)) {
         line = line.substring(line.indexOf(" ") + 1);
         String[] tokens = line.split(" ");

         try {
            return tokens.length == 3 ? new _Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : vertexNormal;
         } catch (NumberFormatException var5) {
            throw new DebugException(String.format("Number formatting error at line %d", lineCount), var5);
         }
      } else {
         throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
      }
   }

   private static _TextureCoord parseTextureCoordinate(String line, int lineCount) throws DebugException {
      _TextureCoord textureCoordinate = null;
      if (isValidTextureCoordinateLine(line)) {
         line = line.substring(line.indexOf(" ") + 1);
         String[] tokens = line.split(" ");

         try {
            if (tokens.length == 2) {
               return new _TextureCoord(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]));
            } else {
               return tokens.length == 3
                  ? new _TextureCoord(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]))
                  : textureCoordinate;
            }
         } catch (NumberFormatException var5) {
            throw new DebugException(String.format("Number formatting error at line %d", lineCount), var5);
         }
      } else {
         throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
      }
   }

   private static _Face parseFace(String line, int lineCount, List<_Vertex> vertices, List<_TextureCoord> textureCoordinates, List<_Vertex> vertexNormals) throws DebugException {
      _Face face = null;
      if (!isValidFaceLine(line)) {
         throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
      } else {
         String trimmedLine = line.substring(line.indexOf(" ") + 1);
         String[] tokens = trimmedLine.split(" ");
         String[] subTokens = null;
         int[] verticesID = new int[tokens.length];
         _Vertex[] verts = new _Vertex[tokens.length];
         _TextureCoord[] texCoords = new _TextureCoord[tokens.length];
         _Vertex[] normals = new _Vertex[tokens.length];
         if (isValidFace_V_VT_VN_Line(line)) {
            for (int i = 0; i < tokens.length; i++) {
               subTokens = tokens[i].split("/");
               verticesID[i] = Integer.parseInt(subTokens[0]) - 1;
               verts[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
               texCoords[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
               normals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
            }

            face = new _Face(verticesID, verts, normals, texCoords);
         } else if (isValidFace_V_VT_Line(line)) {
            for (int i = 0; i < tokens.length; i++) {
               subTokens = tokens[i].split("/");
               verticesID[i] = Integer.parseInt(subTokens[0]) - 1;
               verts[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
               texCoords[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
            }

            face = new _Face(verticesID, verts, new _Vertex[0], texCoords);
         } else if (isValidFace_V_VN_Line(line)) {
            for (int i = 0; i < tokens.length; i++) {
               subTokens = tokens[i].split("//");
               verticesID[i] = Integer.parseInt(subTokens[0]) - 1;
               verts[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
               normals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
            }

            face = new _Face(verticesID, verts, normals, new _TextureCoord[0]);
         } else {
            if (!isValidFace_V_Line(line)) {
               throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
            }

            for (int i = 0; i < tokens.length; i++) {
               verticesID[i] = Integer.parseInt(tokens[0]) - 1;
               verts[i] = vertices.get(Integer.parseInt(tokens[0]) - 1);
            }

            face = new _Face(verticesID, verts, new _Vertex[0], new _TextureCoord[0]);
         }

         return face;
      }
   }

   private static _GroupObject.Builder parseGroupObject(String line, int lineCount) throws DebugException {
      _GroupObject.Builder group = null;
      if (isValidGroupObjectLine(line)) {
         String trimmedLine = line.substring(line.indexOf(" ") + 1);
         if (trimmedLine.length() > 0) {
            group = _GroupObject.builder().name(trimmedLine);
         }

         return group;
      } else {
         throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
      }
   }

   private static boolean isValidVertexLine(String line) {
      return vertexPattern.matcher(line).matches();
   }

   private static boolean isValidVertexNormalLine(String line) {
      return vertexNormalPattern.matcher(line).matches();
   }

   private static boolean isValidTextureCoordinateLine(String line) {
      return textureCoordinatePattern.matcher(line).matches();
   }

   private static boolean isValidFace_V_VT_VN_Line(String line) {
      return face_V_VT_VN_Pattern.matcher(line).matches();
   }

   private static boolean isValidFace_V_VT_Line(String line) {
      return face_V_VT_Pattern.matcher(line).matches();
   }

   private static boolean isValidFace_V_VN_Line(String line) {
      return face_V_VN_Pattern.matcher(line).matches();
   }

   private static boolean isValidFace_V_Line(String line) {
      return face_V_Pattern.matcher(line).matches();
   }

   private static boolean isValidFaceLine(String line) {
      return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
   }

   private static boolean isValidGroupObjectLine(String line) {
      return groupObjectPattern.matcher(line).matches();
   }
}
