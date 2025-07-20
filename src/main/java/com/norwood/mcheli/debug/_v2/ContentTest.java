package com.norwood.mcheli.debug._v2;

import com.google.common.collect.Lists;
import com.norwood.mcheli.helper.debug.DebugBootstrap;
import com.norwood.mcheli.helper.info.ContentRegistries;
import com.norwood.mcheli.helper.info.IContentData;

import java.io.File;
import java.util.List;

public class ContentTest {
    public static void main(String[] args) {
        DebugBootstrap.init();
        File debugDir = new File("./run/");
        List<IContentData> contents = Lists.newLinkedList();
        System.out.println(debugDir.getAbsolutePath());
        ContentRegistries.loadContents(debugDir);
    }
}
