package com.norwood.mcheli.__helper.info;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.norwood.mcheli.MCH_MOD;
import com.norwood.mcheli.__helper.MCH_Logger;
import com.norwood.mcheli.__helper.MCH_Utils;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ContentLoader {
    protected final String domain;
    protected final File dir;
    protected final String loaderVersion;
    private final Predicate<String> fileFilter;

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

        for (ContentLoader.ContentEntry entry : this.getEntries()) {
            map.put(entry.getType(), entry);
        }

        return map;
    }

    protected abstract List<ContentLoader.ContentEntry> getEntries();

    protected abstract InputStream getInputStreamByName(String var1) throws IOException;

    public <TYPE extends IContentData> List<TYPE> reloadAndParse(Class<TYPE> clazz, List<TYPE> oldContents, IContentFactory factory) {
        List<TYPE> list = Lists.newLinkedList();

        for (TYPE oldContent : oldContents) {
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
        List<String> lines;

        try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(this.getInputStreamByName(filepath), StandardCharsets.UTF_8))) {
            lines = bufferedreader.lines().collect(Collectors.toList());
        }

        return new ContentLoader.ContentEntry(filepath, this.domain, factory, lines, reload);
    }

    public static class ContentEntry {
        private final String filepath;
        private final String domain;
        private final IContentFactory factory;
        private final List<String> lines;
        private final boolean reload;

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

            IContentData var4;
            try {
                IContentData content = this.factory.create(location, this.filepath);
                if (content != null) {
                    content.parse(this.lines, Files.getFileExtension(this.filepath), this.reload);
                    if (!content.validate()) {
                        MCH_Logger.get().debug("Invalid content info: {}", this.filepath);
                    }
                }

                return content;
            } catch (Exception var8) {
                String msg = "An error occurred while file loading ";
                if (var8 instanceof ContentParseException) {
                    msg = msg + "at line:" + ((ContentParseException) var8).getLineNo() + ".";
                }

                MCH_Logger.get().error("{} file:{}, domain:{}", msg, location.getPath(), this.domain, var8);
                var4 = null;
            } finally {
                MCH_MOD.proxy.onParseFinishFile(location);
            }

            return var4;
        }

        public ContentType getType() {
            return this.factory.getType();
        }
    }
}
