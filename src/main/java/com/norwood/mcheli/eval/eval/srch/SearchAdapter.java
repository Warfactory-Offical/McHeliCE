package com.norwood.mcheli.eval.eval.srch;

import com.norwood.mcheli.eval.eval.exp.AbstractExpression;
import com.norwood.mcheli.eval.eval.exp.Col1Expression;
import com.norwood.mcheli.eval.eval.exp.Col2Expression;
import com.norwood.mcheli.eval.eval.exp.Col3Expression;
import com.norwood.mcheli.eval.eval.exp.FunctionExpression;
import com.norwood.mcheli.eval.eval.exp.WordExpression;

public class SearchAdapter implements Search {
   protected boolean end = false;

   public boolean end() {
      return this.end;
   }

   protected void setEnd() {
      this.end = true;
   }

   public void search(AbstractExpression exp) {
   }

   public void search0(WordExpression exp) {
   }

   public boolean search1_begin(Col1Expression exp) {
      return false;
   }

   public void search1_end(Col1Expression exp) {
   }

   public boolean search2_begin(Col2Expression exp) {
      return false;
   }

   public boolean search2_2(Col2Expression exp) {
      return false;
   }

   public void search2_end(Col2Expression exp) {
   }

   public boolean search3_begin(Col3Expression exp) {
      return false;
   }

   public boolean search3_2(Col3Expression exp3) {
      return false;
   }

   public boolean search3_3(Col3Expression exp) {
      return false;
   }

   public void search3_end(Col3Expression exp) {
   }

   public boolean searchFunc_begin(FunctionExpression exp) {
      return false;
   }

   public boolean searchFunc_2(FunctionExpression exp) {
      return false;
   }

   public void searchFunc_end(FunctionExpression exp) {
   }
}
