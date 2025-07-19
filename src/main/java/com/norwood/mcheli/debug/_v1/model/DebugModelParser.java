package com.norwood.mcheli.debug._v1.model;

import com.norwood.mcheli.__helper.debug.DebugException;

import java.io.InputStream;

public interface DebugModelParser {
    void parse(InputStream var1) throws DebugException;
}
