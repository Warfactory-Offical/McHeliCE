package com.norwood.mcheli.debug._v1.model;

import com.norwood.mcheli.__helper.debug.DebugException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MqoParser {
    public static MqoModel parse(InputStream inputStream) throws DebugException {
        List<_Vertex> vertices = new ArrayList<>();
        List<_Face> faceList = new ArrayList<>();
        List<_GroupObject> groupObjects = new ArrayList<>();
        int vertexNum = 0;
        int faceNum = 0;
        BufferedReader reader = null;
        String currentLine;
        int lineCount = 0;

        MqoModel var44;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((currentLine = reader.readLine()) != null) {
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                if (isValidGroupObjectLine(currentLine)) {
                    _GroupObject.Builder group = parseGroupObject(currentLine, lineCount);
                    if (group != null) {
                        vertices.clear();
                        faceList.clear();
                        int vertexNum2 = 0;
                        boolean mirror = false;
                        double facet = Math.cos(0.785398163375);
                        boolean shading = false;

                        while ((currentLine = reader.readLine()) != null) {
                            lineCount++;
                            currentLine = currentLine.replaceAll("\\s+", " ").trim();
                            if (currentLine.equalsIgnoreCase("mirror 1")) {
                                mirror = true;
                            }

                            if (currentLine.equalsIgnoreCase("shading 1")) {
                                shading = true;
                            }

                            String[] s = currentLine.split(" ");
                            if (s.length == 2 && s[0].equalsIgnoreCase("facet")) {
                                facet = Math.cos(Double.parseDouble(s[1]) * 3.1415926535 / 180.0);
                            }

                            if (isValidVertexLine(currentLine)) {
                                vertexNum2 = Integer.parseInt(currentLine.split(" ")[1]);
                                break;
                            }
                        }

                        if (vertexNum2 > 0) {
                            while ((currentLine = reader.readLine()) != null) {
                                lineCount++;
                                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                                String[] sx = currentLine.split(" ");
                                if (sx.length == 3) {
                                    _Vertex v = new _Vertex(Float.parseFloat(sx[0]) / 100.0F, Float.parseFloat(sx[1]) / 100.0F, Float.parseFloat(sx[2]) / 100.0F);
                                    vertices.add(v);
                                    if (--vertexNum2 <= 0) {
                                        break;
                                    }
                                } else if (sx.length > 0) {
                                    throw new DebugException("format error : line=" + lineCount);
                                }
                            }

                            int faceNum2 = 0;

                            while ((currentLine = reader.readLine()) != null) {
                                lineCount++;
                                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                                if (isValidFaceLine(currentLine)) {
                                    faceNum2 = Integer.parseInt(currentLine.split(" ")[1]);
                                    break;
                                }
                            }

                            if (faceNum2 > 0) {
                                while ((currentLine = reader.readLine()) != null) {
                                    lineCount++;
                                    currentLine = currentLine.replaceAll("\\s+", " ").trim();
                                    String[] sx = currentLine.split(" ");
                                    if (sx.length <= 2) {
                                    } else {
                                        if (Integer.parseInt(sx[0]) >= 3) {
                                            _Face[] faces = parseFace(currentLine, lineCount, mirror, vertices);

                                            Collections.addAll(faceList, faces);
                                        }

                                        if (--faceNum2 <= 0) {
                                            break;
                                        }
                                    }
                                }

                                for (_Face face : faceList) {
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
                }
            }

            var44 = new MqoModel(groupObjects, vertexNum, faceNum);
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

        return var44;
    }

    private static _GroupObject.Builder parseGroupObject(String line, int lineCount) throws DebugException {
        _GroupObject.Builder group = null;
        if (isValidGroupObjectLine(line)) {
            String[] s = line.split(" ");
            String trimmedLine = s[1].substring(1, s[1].length() - 1);
            if (!trimmedLine.isEmpty()) {
                group = _GroupObject.builder().name(trimmedLine);
            }

            return group;
        } else {
            throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        }
    }

    private static _Face[] parseFace(String line, int lineCount, boolean mirror, List<_Vertex> vertices) {
        String[] s = line.split("[ VU)(M]+");
        int vnum = Integer.parseInt(s[0]);
        if (vnum != 3 && vnum != 4) {
            return new _Face[0];
        } else if (vnum == 3) {
            int[] verticesID = new int[]{Integer.parseInt(s[3]), Integer.parseInt(s[2]), Integer.parseInt(s[1])};
            _Vertex[] verts = new _Vertex[]{vertices.get(verticesID[0]), vertices.get(verticesID[1]), vertices.get(verticesID[2])};
            _TextureCoord[] texCoords;
            if (s.length >= 11) {
                texCoords = new _TextureCoord[]{
                        new _TextureCoord(Float.parseFloat(s[9]), Float.parseFloat(s[10])),
                        new _TextureCoord(Float.parseFloat(s[7]), Float.parseFloat(s[8])),
                        new _TextureCoord(Float.parseFloat(s[5]), Float.parseFloat(s[6]))
                };
            } else {
                texCoords = new _TextureCoord[]{new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F)};
            }

            return new _Face[]{new _Face(verticesID, verts, texCoords)};
        } else {
            int[] verticesID1 = new int[]{Integer.parseInt(s[3]), Integer.parseInt(s[2]), Integer.parseInt(s[1])};
            _Vertex[] verts1 = new _Vertex[]{vertices.get(verticesID1[0]), vertices.get(verticesID1[1]), vertices.get(verticesID1[2])};
            _TextureCoord[] texCoords1;
            if (s.length >= 12) {
                texCoords1 = new _TextureCoord[]{
                        new _TextureCoord(Float.parseFloat(s[10]), Float.parseFloat(s[11])),
                        new _TextureCoord(Float.parseFloat(s[8]), Float.parseFloat(s[9])),
                        new _TextureCoord(Float.parseFloat(s[6]), Float.parseFloat(s[7]))
                };
            } else {
                texCoords1 = new _TextureCoord[]{new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F)};
            }

            int[] verticesID2 = new int[]{Integer.parseInt(s[4]), Integer.parseInt(s[3]), Integer.parseInt(s[1])};
            _Vertex[] verts2 = new _Vertex[]{vertices.get(verticesID2[0]), vertices.get(verticesID2[1]), vertices.get(verticesID2[2])};
            _TextureCoord[] texCoords2;
            if (s.length >= 14) {
                texCoords2 = new _TextureCoord[]{
                        new _TextureCoord(Float.parseFloat(s[12]), Float.parseFloat(s[13])),
                        new _TextureCoord(Float.parseFloat(s[10]), Float.parseFloat(s[11])),
                        new _TextureCoord(Float.parseFloat(s[6]), Float.parseFloat(s[7]))
                };
            } else {
                texCoords2 = new _TextureCoord[]{new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F), new _TextureCoord(0.0F, 0.0F)};
            }

            return new _Face[]{new _Face(verticesID1, verts1, texCoords1), new _Face(verticesID2, verts2, texCoords2)};
        }
    }

    private static boolean isValidGroupObjectLine(String line) {
        String[] s = line.split(" ");
        return s.length >= 2 && s[0].equals("Object") && s[1].length() >= 4 && s[1].charAt(0) == '"';
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
