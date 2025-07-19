package com.norwood.mcheli.__helper.info;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.norwood.mcheli.MCH_BaseInfo;
import com.norwood.mcheli.__helper.MCH_Logger;

public class ContentRegistry<T extends MCH_BaseInfo> {
   private Class<T> contentClass;
   private String dir;
   private Map<String, T> registry;

   private ContentRegistry(Class<T> contentClass, String dir, Map<String, T> table) {
      this.contentClass = contentClass;
      this.dir = dir;
      this.registry = Maps.newHashMap(table);
   }

   @Nullable
   public T get(@Nullable String key) {
      return key == null ? null : this.registry.get(key);
   }

   @Nullable
   public T findFirst(Predicate<? super T> filter) {
      return this.registry.values().stream().filter(filter).findFirst().orElse(null);
   }

   public boolean reload(String key) {
      T content = this.get(key);
      if (content != null) {
         IContentData newContent = ContentRegistries.reparseContent(content, this.dir);
         if (this.contentClass.isInstance(newContent)) {
            T castedContent = this.contentClass.cast(newContent);
            this.registry.replace(key, castedContent);
            return true;
         }

         MCH_Logger.get().error("Content cast error, old dir:{}, new dir:{}", content.getClass(), newContent.getClass());
      }

      return false;
   }

   public void reloadAll() {
      for (T content : ContentRegistries.reloadAllAddonContents(this)) {
         this.registry.replace(content.getLoation().getPath(), content);
      }
   }

   public List<T> values() {
      return ImmutableList.copyOf(this.registry.values());
   }

   public Set<Entry<String, T>> entries() {
      return this.registry.entrySet();
   }

   public void forEachValue(Consumer<? super T> action) {
      this.registry.values().forEach(action);
   }

   public boolean contains(String key) {
      return this.registry.containsKey(key);
   }

   public int size() {
      return this.registry.size();
   }

   public Class<T> getType() {
      return this.contentClass;
   }

   public String getDirectoryName() {
      return this.dir;
   }

   private static <TYPE extends IContentData> void putTable(Map<String, TYPE> table, TYPE content) {
      table.put(content.getLoation().getPath(), content);
   }

   public static <TYPE extends MCH_BaseInfo> ContentRegistry.Builder<TYPE> builder(Class<TYPE> type, String dir) {
      return new ContentRegistry.Builder<>(type, dir);
   }

   public static class Builder<E extends MCH_BaseInfo> {
      private Class<E> clazz;
      private String dirName;
      private Map<String, E> map = Maps.newHashMap();

      Builder(Class<E> clazz, String dir) {
         this.clazz = clazz;
         this.dirName = dir;
      }

      public void put(E content) {
         ContentRegistry.putTable(this.map, content);
      }

      public ContentRegistry<E> build() {
         return new ContentRegistry<>(this.clazz, this.dirName, this.map);
      }
   }
}
