package com.norwood.mcheli.__helper.info;

import java.util.List;
import com.norwood.mcheli.__helper.addon.AddonResourceLocation;

public interface IContentData {
   void parse(List<String> var1, String var2, boolean var3) throws Exception;

   boolean validate() throws Exception;

   void onPostReload();

   AddonResourceLocation getLoation();

   String getContentPath();
}
