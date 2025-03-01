package com.norwood.mcheli.__helper.info;

import javax.annotation.Nullable;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;

public interface IContentFactory {
   @Nullable
   IContentData create(AddonResourceLocation var1, String var2);

   ContentType getType();
}
