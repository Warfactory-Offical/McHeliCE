package com.norwood.mcheli.__helper.addon;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import com.norwood.mcheli.__helper.MCH_Logger;
import com.norwood.mcheli.__helper.io.ResourceLoader;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;

public class AddonPack {
   private final String addonDomain;
   private final String addonName;
   private final String addonVersion;
   private File addonFile;
   private String credits;
   private List<String> authors;
   private String description;
   private String loaderVersion;
   protected ImmutableMap<String, JsonElement> packMetaMap;

   public AddonPack(String addonDomain, String addonName, String addonVersion, File addonFile, String credits, List<String> authors, String description, String loaderVersion, ImmutableMap<String, JsonElement> packMetaMap) {
      this.addonDomain = addonDomain;
      this.addonName = addonName;
      this.addonVersion = addonVersion;
      this.addonFile = addonFile;
      this.credits = credits;
      this.authors = authors;
      this.description = description;
      this.loaderVersion = loaderVersion;
      this.packMetaMap = packMetaMap;
   }

   public String getDomain() {
      return this.addonDomain;
   }

   public String getName() {
      return this.addonName;
   }

   public String getVersion() {
      return this.addonVersion;
   }

   public String getAuthorsString() {
      return Joiner.on(", ").join(this.authors);
   }

   public String getCredits() {
      return this.credits;
   }

   public String getDescription() {
      return this.description;
   }

   public String getLoaderVersion() {
      return this.loaderVersion;
   }

   public File getFile() {
      return this.addonFile;
   }

   public ImmutableMap<String, JsonElement> getPackMetaMap() {
      return this.packMetaMap;
   }

   public static AddonPack create(File addonFile) {
      JsonObject packMetaJson = loadPackMeta(addonFile);
      JsonObject packJson = JsonUtils.func_151218_a(packMetaJson, "pack", new JsonObject());
      JsonObject addonJson = JsonUtils.func_151218_a(packMetaJson, "addon", new JsonObject());
      String addonDomain = JsonUtils.func_151219_a(addonJson, "domain", (String)null);
      String packName = JsonUtils.func_151219_a(packJson, "description", addonFile.getName());
      String version = JsonUtils.func_151219_a(addonJson, "version", "0.0");
      if (addonDomain == null) {
         MCH_Logger.get().warn("A addon domain is not specified! file:{}", addonFile.getName());
         addonDomain = "<!mcheli_share_domain>";
      }

      String credits = JsonUtils.func_151219_a(addonJson, "credits", "");
      String description = JsonUtils.func_151219_a(addonJson, "description", "");
      String loaderVersion = JsonUtils.func_151219_a(addonJson, "loader_version", "1");
      List<String> authors = getAuthors(addonJson);
      return new AddonPack(addonDomain, packName, version, addonFile, credits, authors, description, loaderVersion, ImmutableMap.copyOf(packMetaJson.entrySet()));
   }

   private static List<String> getAuthors(JsonObject jsonObject) {
      List<String> list = Lists.newLinkedList();
      JsonElement jsonElement2;
      if (jsonObject.has("authors")) {
         jsonElement2 = jsonObject.get("authors");
         if (jsonElement2.isJsonArray()) {
            Iterator var3 = jsonElement2.getAsJsonArray().iterator();

            while(var3.hasNext()) {
               JsonElement jsonElement1 = (JsonElement)var3.next();
               list.add(jsonElement1.getAsString());
            }
         }
      } else if (jsonObject.has("author")) {
         jsonElement2 = jsonObject.get("author");
         if (jsonElement2.isJsonPrimitive()) {
            list.add(jsonElement2.getAsString());
         }
      }

      return list;
   }

   private static JsonObject loadPackMeta(File addonFile) {
      ResourceLoader loader = ResourceLoader.create(addonFile);
      BufferedReader bufferedReader = null;

      try {
         bufferedReader = new BufferedReader(new InputStreamReader(loader.getInputStream("pack.mcmeta"), StandardCharsets.UTF_8));
         JsonObject var3 = (new JsonParser()).parse(bufferedReader).getAsJsonObject();
         return var3;
      } catch (FileNotFoundException var8) {
         MCH_Logger.get().warn("'pack.mcmeta' does not found in '{}'", addonFile.getName());
      } catch (IOException var9) {
         var9.printStackTrace();
      } finally {
         IOUtils.closeQuietly(bufferedReader);
         IOUtils.closeQuietly(loader);
      }

      return new JsonObject();
   }
}
