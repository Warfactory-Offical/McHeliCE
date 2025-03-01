package com.norwood.mcheli.eval.eval.repl;

import com.norwood.mcheli.eval.eval.exp.AbstractExpression;
import com.norwood.mcheli.eval.eval.exp.Col1Expression;
import com.norwood.mcheli.eval.eval.exp.Col2Expression;
import com.norwood.mcheli.eval.eval.exp.Col2OpeExpression;
import com.norwood.mcheli.eval.eval.exp.Col3Expression;
import com.norwood.mcheli.eval.eval.exp.FunctionExpression;
import com.norwood.mcheli.eval.eval.exp.WordExpression;

public interface Replace {
   AbstractExpression replace0(WordExpression var1);

   AbstractExpression replace1(Col1Expression var1);

   AbstractExpression replace2(Col2Expression var1);

   AbstractExpression replace2(Col2OpeExpression var1);

   AbstractExpression replace3(Col3Expression var1);

   AbstractExpression replaceVar0(WordExpression var1);

   AbstractExpression replaceVar1(Col1Expression var1);

   AbstractExpression replaceVar2(Col2Expression var1);

   AbstractExpression replaceVar2(Col2OpeExpression var1);

   AbstractExpression replaceVar3(Col3Expression var1);

   AbstractExpression replaceFunc(FunctionExpression var1);

   AbstractExpression replaceLet(Col2Expression var1);
}
