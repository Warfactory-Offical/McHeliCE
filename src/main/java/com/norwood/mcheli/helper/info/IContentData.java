package com.norwood.mcheli.helper.info;

import com.norwood.mcheli.helper.addon.AddonResourceLocation;

import java.util.List;

public interface IContentData {
    void parse(List<String> var1, String var2, boolean var3);

    boolean validate() throws Exception;

    void onPostReload();

    AddonResourceLocation getLoation();

    String getContentPath();
}
