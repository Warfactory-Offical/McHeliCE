package com.norwood.mcheli.__helper.info;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.jline.utils.OSUtils;

public class FolderContentLoader extends ContentLoader {
   private static final boolean ON_WINDOWS;
   private static final CharMatcher BACKSLASH_MATCHER;
   private File addonFolder;

   public FolderContentLoader(String domain, File addonDir, String loaderVersion, Predicate<String> fileFilter) {
      super(domain, addonDir, loaderVersion, fileFilter);
      this.addonFolder = addonDir.getParentFile();
   }

   protected List<ContentLoader.ContentEntry> getEntries() {
      return this.walkDir(this.dir, (IContentFactory)null, this.loaderVersion.equals("2"), 0);
   }

   private List<ContentLoader.ContentEntry> walkDir(File dir, @Nullable IContentFactory factory, boolean loadDeep, int depth) {
      List<ContentLoader.ContentEntry> list = Lists.newArrayList();
      if (dir != null && dir.exists()) {
         if (dir.isDirectory()) {
            if (loadDeep || depth <= 1) {
               File[] var6 = dir.listFiles();
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  File f = var6[var8];
                  IContentFactory contentFactory = factory;
                  boolean flag = loadDeep || depth == 0 && "assets".equals(f.getName());
                  if (factory == null) {
                     contentFactory = this.getFactory(f.getName());
                  }

                  list.addAll(this.walkDir(f, contentFactory, flag, depth + 1));
               }
            }
         } else {
            try {
               String s = this.getDirPath(dir);
               if (this.isReadable(s) && factory != null) {
                  list.add(this.makeEntry(s, factory, false));
               }
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

         return list;
      } else {
         return Lists.newArrayList();
      }
   }

   private String getDirPath(File file) throws IOException {
      String s = this.dir.getName();
      String s1 = file.getCanonicalPath();
      if (ON_WINDOWS) {
         s1 = BACKSLASH_MATCHER.replaceFrom(s1, '/');
      }

      String[] split = s1.split(this.addonFolder.getName() + "/" + s + "/", 2);
      if (split.length < 2) {
         throw new FileNotFoundException(String.format("'%s' in AddonPack '%s'", this.dir, s));
      } else {
         return split[1];
      }
   }

   protected InputStream getInputStreamByName(String name) throws IOException {
      File file1 = this.getFile(name);
      if (file1 == null) {
         throw new FileNotFoundException(String.format("'%s' in AddonPack '%s'", this.dir, name));
      } else {
         return new BufferedInputStream(new FileInputStream(file1));
      }
   }

   @Nullable
   private File getFile(String filepath) {
      try {
         File file1 = new File(this.dir, filepath);
         if (file1.isFile() && validatePath(file1, filepath)) {
            return file1;
         }
      } catch (IOException var3) {
      }

      return null;
   }

   protected static boolean validatePath(File file, String filepath) throws IOException {
      String s = file.getCanonicalPath();
      if (ON_WINDOWS) {
         s = BACKSLASH_MATCHER.replaceFrom(s, '/');
      }

      return s.endsWith(filepath);
   }

   static {
      ON_WINDOWS = OSUtils.IS_WINDOWS;
      BACKSLASH_MATCHER = CharMatcher.is('\\');
   }
}
