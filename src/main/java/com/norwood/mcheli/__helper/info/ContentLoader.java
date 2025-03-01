package com.norwood.mcheli.__helper.info;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.MCH_Logger;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;

public abstract class ContentLoader {
   protected final String domain;
   protected final File dir;
   protected final String loaderVersion;
   private Predicate<String> fileFilter;

   public ContentLoader(String domain, File addonDir, String loaderVersion, Predicate<String> fileFilter) {
      this.domain = domain;
      this.dir = addonDir;
      this.loaderVersion = loaderVersion;
      this.fileFilter = fileFilter;
   }

   public boolean isReadable(String path) {
      return this.fileFilter.test(path);
   }

   @Nullable
   public IContentFactory getFactory(@Nullable String dirName) {
      return ContentFactories.getFactory(dirName);
   }

   public Multimap<ContentType, ContentLoader.ContentEntry> load() {
      Multimap<ContentType, ContentLoader.ContentEntry> map = LinkedHashMultimap.create();
      List<ContentLoader.ContentEntry> list = this.getEntries();
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         ContentLoader.ContentEntry entry = (ContentLoader.ContentEntry)var3.next();
         map.put(entry.getType(), entry);
      }

      return map;
   }

   protected abstract List<ContentLoader.ContentEntry> getEntries();

   protected abstract InputStream getInputStreamByName(String var1) throws IOException;

   public <TYPE extends IContentData> List<TYPE> reloadAndParse(Class<TYPE> clazz, List<TYPE> oldContents, IContentFactory factory) {
      List<TYPE> list = Lists.newLinkedList();
      Iterator var5 = oldContents.iterator();

      while(var5.hasNext()) {
         IContentData oldContent = (IContentData)var5.next();

         try {
            ContentLoader.ContentEntry entry = this.makeEntry(oldContent.getContentPath(), factory, true);
            IContentData content = entry.parse();
            if (content != null) {
               content.onPostReload();
            } else {
               content = oldContent;
            }

            if (clazz.isInstance(content)) {
               list.add(clazz.cast(content));
            }
         } catch (IOException var9) {
            MCH_Logger.get().error("IO Error from file loader!", var9);
         }
      }

      return list;
   }

   public IContentData reloadAndParseSingle(IContentData oldData, String dir) {
      IContentData content = oldData;

      try {
         ContentLoader.ContentEntry entry = this.makeEntry(oldData.getContentPath(), this.getFactory(dir), true);
         content = entry.parse();
         if (content != null) {
            content.onPostReload();
         } else {
            content = oldData;
         }
      } catch (IOException var5) {
         MCH_Logger.get().error("IO Error from file loader!", var5);
      }

      return content;
   }

   protected ContentLoader.ContentEntry makeEntry(String filepath, @Nullable IContentFactory factory, boolean reload) throws IOException {
      List<String> lines = null;
      BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(this.getInputStreamByName(filepath), StandardCharsets.UTF_8));
      Throwable var6 = null;

      try {
         lines = (List)bufferedreader.lines().collect(Collectors.toList());
      } catch (Throwable var15) {
         var6 = var15;
         throw var15;
      } finally {
         if (bufferedreader != null) {
            if (var6 != null) {
               try {
                  bufferedreader.close();
               } catch (Throwable var14) {
                  var6.addSuppressed(var14);
               }
            } else {
               bufferedreader.close();
            }
         }

      }

      return new ContentLoader.ContentEntry(filepath, this.domain, factory, lines, reload);
   }

   static class ContentEntry {
      private String filepath;
      private String domain;
      private IContentFactory factory;
      private List<String> lines;
      private boolean reload;

      private ContentEntry(String filepath, String domain, @Nullable IContentFactory factory, List<String> lines, boolean reload) {
         this.filepath = filepath;
         this.domain = domain;
         this.factory = factory;
         this.lines = lines;
         this.reload = reload;
      }

      @Nullable
      public IContentData parse() {
         AddonResourceLocation location = MCH_Utils.addon(this.domain, Files.getNameWithoutExtension(this.filepath));
         if (!this.reload) {
            MCH_MOD.proxy.onParseStartFile(location);
         }

         Object var4;
         try {
            IContentData content = this.factory.create(location, this.filepath);
            if (content != null) {
               content.parse(this.lines, Files.getFileExtension(this.filepath), this.reload);
               if (!content.validate()) {
                  MCH_Logger.get().debug("Invalid content info: " + this.filepath);
               }
            }

            IContentData var10 = content;
            return var10;
         } catch (Exception var8) {
            String msg = "An error occurred while file loading ";
            if (var8 instanceof ContentParseException) {
               msg = msg + "at line:" + ((ContentParseException)var8).getLineNo() + ".";
            }

            MCH_Logger.get().error(msg + " file:{}, domain:{}", location.func_110623_a(), this.domain, var8);
            var4 = null;
         } finally {
            MCH_MOD.proxy.onParseFinishFile(location);
         }

         return (IContentData)var4;
      }

      public ContentType getType() {
         return this.factory.getType();
      }

      // $FF: synthetic method
      ContentEntry(String x0, String x1, IContentFactory x2, List x3, boolean x4, Object x5) {
         this(x0, x1, x2, x3, x4);
      }
   }
}
