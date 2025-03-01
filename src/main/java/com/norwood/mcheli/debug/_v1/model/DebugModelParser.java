package com.norwood.mcheli.debug._v1.model;

import java.io.InputStream;
import com.norwood.mcheli.__helper.debug.DebugException;

public interface DebugModelParser {
   void parse(InputStream var1) throws DebugException;
}
