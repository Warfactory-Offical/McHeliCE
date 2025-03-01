package com.norwood.mcheli.multiplay;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.imageio.ImageIO;
import com.norwood.mcheli.MCH_Config;
import com.norwood.mcheli.MCH_FileSearch;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.MCH_OStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class MCH_MultiplayClient {
   private static IntBuffer pixelBuffer;
   private static int[] pixelValues;
   private static MCH_OStream dataOutputStream;
   private static List<String> modList = new ArrayList();

   public static void startSendImageData() {
      Minecraft mc = Minecraft.func_71410_x();
      sendScreenShot(mc.field_71443_c, mc.field_71440_d, mc.func_147110_a());
   }

   public static void sendScreenShot(int displayWidth, int displayHeight, Framebuffer framebufferMc) {
      try {
         if (OpenGlHelper.func_148822_b()) {
            displayWidth = framebufferMc.field_147622_a;
            displayHeight = framebufferMc.field_147620_b;
         }

         int k = displayWidth * displayHeight;
         if (pixelBuffer == null || pixelBuffer.capacity() < k) {
            pixelBuffer = BufferUtils.createIntBuffer(k);
            pixelValues = new int[k];
         }

         GL11.glPixelStorei(3333, 1);
         GL11.glPixelStorei(3317, 1);
         pixelBuffer.clear();
         if (OpenGlHelper.func_148822_b()) {
            GL11.glBindTexture(3553, framebufferMc.field_147617_g);
            GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
         } else {
            GL11.glReadPixels(0, 0, displayWidth, displayHeight, 32993, 33639, pixelBuffer);
         }

         pixelBuffer.get(pixelValues);
         TextureUtil.func_147953_a(pixelValues, displayWidth, displayHeight);
         BufferedImage bufferedimage = null;
         if (!OpenGlHelper.func_148822_b()) {
            bufferedimage = new BufferedImage(displayWidth, displayHeight, 1);
            bufferedimage.setRGB(0, 0, displayWidth, displayHeight, pixelValues, 0, displayWidth);
         } else {
            bufferedimage = new BufferedImage(framebufferMc.field_147621_c, framebufferMc.field_147618_d, 1);
            int l = framebufferMc.field_147620_b - framebufferMc.field_147618_d;

            for(int i1 = l; i1 < framebufferMc.field_147620_b; ++i1) {
               for(int j1 = 0; j1 < framebufferMc.field_147621_c; ++j1) {
                  bufferedimage.setRGB(j1, i1 - l, pixelValues[i1 * framebufferMc.field_147622_a + j1]);
               }
            }
         }

         dataOutputStream = new MCH_OStream();
         ImageIO.write(bufferedimage, "png", dataOutputStream);
      } catch (Exception var8) {
      }

   }

   public static void readImageData(DataOutputStream dos) throws IOException {
      dataOutputStream.write(dos);
   }

   public static void sendImageData() {
      if (dataOutputStream != null) {
         MCH_PacketLargeData.send();
         if (dataOutputStream.isDataEnd()) {
            dataOutputStream = null;
         }
      }

   }

   public static double getPerData() {
      return dataOutputStream == null ? -1.0D : (double)(dataOutputStream.index / dataOutputStream.size());
   }

   public static void readModList(String playerName, String commandSenderName) {
      modList = new ArrayList();
      modList.add(TextFormatting.RED + "###### Name:" + commandSenderName + " ######");
      modList.add(TextFormatting.RED + "###### ID  :" + playerName + " ######");
      String[] classFileNameList = System.getProperty("java.class.path").split(File.pathSeparator);
      String[] var3 = classFileNameList;
      int var4 = classFileNameList.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String classFileName = var3[var5];
         MCH_Lib.DbgLog(true, "java.class.path=" + classFileName);
         if (classFileName.length() > 1) {
            File javaClassFile = new File(classFileName);
            if (javaClassFile.getAbsolutePath().toLowerCase().indexOf("versions") >= 0) {
               modList.add(TextFormatting.AQUA + "# Client class=" + javaClassFile.getName() + " : file size= " + javaClassFile.length());
            }
         }
      }

      modList.add(TextFormatting.YELLOW + "=== ActiveModList ===");
      Iterator var21 = Loader.instance().getActiveModList().iterator();

      while(var21.hasNext()) {
         ModContainer mod = (ModContainer)var21.next();
         modList.add("" + mod + "  [" + mod.getModId() + "]  " + mod.getName() + "[" + mod.getDisplayVersion() + "]  " + mod.getSource().getName());
      }

      String s;
      if (CoreModManager.getAccessTransformers().size() > 0) {
         modList.add(TextFormatting.YELLOW + "=== AccessTransformers ===");
         var21 = CoreModManager.getAccessTransformers().iterator();

         while(var21.hasNext()) {
            s = (String)var21.next();
            modList.add(s);
         }
      }

      if (CoreModManager.getIgnoredMods().size() > 0) {
         modList.add(TextFormatting.YELLOW + "=== LoadedCoremods ===");
         var21 = CoreModManager.getIgnoredMods().iterator();

         while(var21.hasNext()) {
            s = (String)var21.next();
            modList.add(s);
         }
      }

      if (CoreModManager.getReparseableCoremods().size() > 0) {
         modList.add(TextFormatting.YELLOW + "=== ReparseableCoremods ===");
         var21 = CoreModManager.getReparseableCoremods().iterator();

         while(var21.hasNext()) {
            s = (String)var21.next();
            modList.add(s);
         }
      }

      Minecraft mc = Minecraft.func_71410_x();
      MCH_FileSearch search = new MCH_FileSearch();
      File[] files = search.listFiles((new File(mc.field_71412_D, "mods")).getAbsolutePath(), "*.jar");
      modList.add(TextFormatting.YELLOW + "=== Manifest ===");
      File[] var27 = files;
      int var28 = files.length;

      int var8;
      File file;
      String jarPath;
      JarFile jarFile;
      Enumeration jarEntries;
      String litemod_json;
      ZipEntry zipEntry;
      for(var8 = 0; var8 < var28; ++var8) {
         file = var27[var8];

         try {
            jarPath = file.getCanonicalPath();
            jarFile = new JarFile(jarPath);
            jarEntries = jarFile.entries();
            litemod_json = "";

            while(jarEntries.hasMoreElements()) {
               zipEntry = (ZipEntry)jarEntries.nextElement();
               if (zipEntry.getName().equalsIgnoreCase("META-INF/MANIFEST.MF") && !zipEntry.isDirectory()) {
                  InputStream is = jarFile.getInputStream(zipEntry);
                  BufferedReader br = new BufferedReader(new InputStreamReader(is));

                  for(String line = br.readLine(); line != null; line = br.readLine()) {
                     line = line.replace(" ", "").trim();
                     if (!line.isEmpty()) {
                        litemod_json = litemod_json + " [" + line + "]";
                     }
                  }

                  is.close();
                  break;
               }
            }

            jarFile.close();
            if (!litemod_json.isEmpty()) {
               modList.add(file.getName() + litemod_json);
            }
         } catch (Exception var20) {
            modList.add(file.getName() + " : Read Manifest failed.");
         }
      }

      search = new MCH_FileSearch();
      files = search.listFiles((new File(mc.field_71412_D, "mods")).getAbsolutePath(), "*.litemod");
      modList.add(TextFormatting.LIGHT_PURPLE + "=== LiteLoader ===");
      var27 = files;
      var28 = files.length;

      label116:
      for(var8 = 0; var8 < var28; ++var8) {
         file = var27[var8];

         try {
            jarPath = file.getCanonicalPath();
            jarFile = new JarFile(jarPath);
            jarEntries = jarFile.entries();
            litemod_json = "";

            while(true) {
               while(true) {
                  String fname;
                  do {
                     if (!jarEntries.hasMoreElements()) {
                        jarFile.close();
                        if (!litemod_json.isEmpty()) {
                           modList.add(file.getName() + litemod_json);
                        }
                        continue label116;
                     }

                     zipEntry = (ZipEntry)jarEntries.nextElement();
                     fname = zipEntry.getName().toLowerCase();
                  } while(zipEntry.isDirectory());

                  if (fname.equals("litemod.json")) {
                     InputStream is = jarFile.getInputStream(zipEntry);
                     BufferedReader br = new BufferedReader(new InputStreamReader(is));

                     for(String line = br.readLine(); line != null; line = br.readLine()) {
                        line = line.replace(" ", "").trim();
                        if (line.toLowerCase().indexOf("name") >= 0) {
                           litemod_json = litemod_json + " [" + line + "]";
                           break;
                        }
                     }

                     is.close();
                  } else {
                     int index = fname.lastIndexOf("/");
                     if (index >= 0) {
                        fname = fname.substring(index + 1);
                     }

                     if (fname.indexOf("litemod") >= 0 && fname.endsWith("class")) {
                        fname = zipEntry.getName();
                        if (index >= 0) {
                           fname = fname.substring(index + 1);
                        }

                        litemod_json = litemod_json + " [" + fname + "]";
                     }
                  }
               }
            }
         } catch (Exception var19) {
            modList.add(file.getName() + " : Read LiteLoader failed.");
         }
      }

   }

   public static void sendModsInfo(String playerName, String commandSenderName, int id) {
      if (MCH_Config.DebugLog) {
         modList.clear();
         readModList(playerName, commandSenderName);
      }

      MCH_PacketModList.send(modList, id);
   }
}
