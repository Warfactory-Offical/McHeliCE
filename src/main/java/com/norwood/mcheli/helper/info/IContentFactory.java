package com.norwood.mcheli.helper.info;

import com.norwood.mcheli.helper.addon.AddonResourceLocation;

import javax.annotation.Nullable;

public interface IContentFactory {
    @Nullable
    IContentData create(AddonResourceLocation var1, String var2);

    ContentType getType();
}
