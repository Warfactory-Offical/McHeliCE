package com.norwood.mcheli;

import com.norwood.mcheli.helper.MCH_Utils;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;

import java.io.File;

public abstract class MCH_InfoManagerBase<T extends MCH_BaseInfo> {
    public abstract T newInfo(AddonResourceLocation var1, String var2);

    protected void put(String name, T info) {
    }

    protected abstract boolean contains(String var1);

    protected abstract int size();

    public boolean load(String path, String type) {
        path = path.replace('\\', '/');
        File dir = new File(path + type);
        File[] files = dir.listFiles(pathname -> {
            String s = pathname.getName().toLowerCase();
            return pathname.isFile() && s.length() >= 5 && s.substring(s.length() - 4).compareTo(".txt") == 0;
        });
        if (files != null && files.length > 0) {
            for (File f : files) {
                int line = 0;
                MCH_InputFile inFile = new MCH_InputFile();

                try {
                    String name = f.getName().toLowerCase();
                    name = name.substring(0, name.length() - 4);
                    if (this.contains(name)) {
                        inFile.close();
                    } else if (inFile.openUTF8(f)) {
                        T info = this.newInfo(MCH_Utils.buildinAddon(name), f.getCanonicalPath());

                        String str;
                        while ((str = inFile.br.readLine()) != null) {
                            line++;
                            str = str.trim();
                            int eqIdx = str.indexOf(61);
                            if (eqIdx >= 0 && str.length() > eqIdx + 1) {
                                info.loadItemData(str.substring(0, eqIdx).trim().toLowerCase(), str.substring(eqIdx + 1).trim());
                            }
                        }

                        line = 0;
                        if (info.validate()) {
                            this.put(name, info);
                        }
                    }
                } catch (Exception var18) {
                    if (line > 0) {
                        MCH_Lib.Log("### Load failed %s : line=%d", f.getName(), line);
                    } else {
                        MCH_Lib.Log("### Load failed %s", f.getName());
                    }

                    var18.printStackTrace();
                } finally {
                    inFile.close();
                }
            }

            MCH_Lib.Log("Read %d %s", this.size(), type);
            return this.size() > 0;
        } else {
            return false;
        }
    }
}
