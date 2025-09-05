package com.norwood.mcheli.helper.info;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import org.jline.utils.OSUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Predicate;

public class FolderContentLoader extends ContentLoader {
    private static final boolean ON_WINDOWS = OSUtils.IS_WINDOWS;
    private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is('\\');
    private final File addonFolder;

    public FolderContentLoader(String domain, File addonDir, String loaderVersion, Predicate<String> fileFilter) {
        super(domain, addonDir, loaderVersion, fileFilter);
        this.addonFolder = addonDir.getParentFile();
    }

    protected static boolean validatePath(File file, String filepath) throws IOException {
        String s = file.getCanonicalPath();
//        if (ON_WINDOWS) {
//            s = BACKSLASH_MATCHER.replaceFrom(s, '/');
//        }

        return s.endsWith(filepath);
    }

    @Override
    protected List<ContentLoader.ContentEntry> getEntries() {
        return this.walkDir(this.dir, null, this.loaderVersion.equals("2"), 0);
    }

    private List<ContentLoader.ContentEntry> walkDir(File dir, @Nullable IContentFactory factory, boolean loadDeep, int depth) {
        List<ContentLoader.ContentEntry> list = Lists.newArrayList();
        if (dir != null && dir.exists()) {
            if (dir.isDirectory()) {
                if (loadDeep || depth <= 1) {
                    for (File f : dir.listFiles()) {
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
//        if (ON_WINDOWS) {
//            s1 = BACKSLASH_MATCHER.replaceFrom(s1, '/');
//        }

        String[] split = s1.split(this.addonFolder.getName() + "/" + s + "/", 2);
        if (split.length < 2) {
            throw new FileNotFoundException(String.format("'%s' in AddonPack '%s'", this.dir, s));
        } else {
            return split[1];
        }
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        File file1 = this.getFile(name);
        if (file1 == null) {
            throw new FileNotFoundException(String.format("'%s' in AddonPack '%s'", this.dir, name));
        } else {
            return new BufferedInputStream(Files.newInputStream(file1.toPath()));
        }
    }

    @Nullable
    private File getFile(String filepath) throws IOException {
        try {
            File file1 = new File(this.dir, filepath);
            if (file1.isFile() && validatePath(file1, filepath)) {
                return file1;
            }
        } catch (IOException var3) {
            throw var3;
        }

        return null;
    }
}
