package com.norwood.mcheli.eval.eval.repl;

import com.norwood.mcheli.eval.eval.exp.AbstractExpression;
import com.norwood.mcheli.eval.eval.exp.Col1Expression;
import com.norwood.mcheli.eval.eval.exp.Col2Expression;
import com.norwood.mcheli.eval.eval.exp.Col2OpeExpression;
import com.norwood.mcheli.eval.eval.exp.Col3Expression;
import com.norwood.mcheli.eval.eval.exp.FunctionExpression;
import com.norwood.mcheli.eval.eval.exp.WordExpression;

public class ReplaceAdapter implements Replace {
   @Override
   public AbstractExpression replace0(WordExpression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replace1(Col1Expression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replace2(Col2Expression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replace2(Col2OpeExpression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replace3(Col3Expression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replaceVar0(WordExpression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replaceVar1(Col1Expression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replaceVar2(Col2Expression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replaceVar2(Col2OpeExpression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replaceVar3(Col3Expression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replaceFunc(FunctionExpression exp) {
      return exp;
   }

   @Override
   public AbstractExpression replaceLet(Col2Expression exp) {
      return exp;
   }
}
