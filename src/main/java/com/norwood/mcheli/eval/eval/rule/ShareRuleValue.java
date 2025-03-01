package com.norwood.mcheli.eval.eval.rule;

import java.util.List;
import com.norwood.mcheli.eval.eval.EvalException;
import com.norwood.mcheli.eval.eval.Expression;
import com.norwood.mcheli.eval.eval.Rule;
import com.norwood.mcheli.eval.eval.exp.AbstractExpression;
import com.norwood.mcheli.eval.eval.exp.ShareExpValue;
import com.norwood.mcheli.eval.eval.lex.Lex;
import com.norwood.mcheli.eval.eval.lex.LexFactory;
import com.norwood.mcheli.eval.eval.oper.Operator;
import com.norwood.mcheli.eval.eval.ref.Refactor;
import com.norwood.mcheli.eval.eval.srch.Search;
import com.norwood.mcheli.eval.eval.var.Variable;

public class ShareRuleValue extends Rule {
   public AbstractRule topRule;
   public AbstractRule funcArgRule;
   public LexFactory lexFactory;
   protected List<String>[] opeList = new List[4];
   public AbstractExpression paren;

   public Expression parse(String str) {
      if (str == null) {
         return null;
      } else if (str.trim().length() <= 0) {
         return new ShareRuleValue.EmptyExpression();
      } else {
         ShareExpValue exp = new ShareExpValue();
         AbstractExpression x = this.parse(str, exp);
         exp.setAbstractExpression(x);
         return exp;
      }
   }

   public AbstractExpression parse(String str, ShareExpValue exp) {
      if (str == null) {
         return null;
      } else {
         Lex lex = this.lexFactory.create(str, this.opeList, this, exp);
         lex.check();
         AbstractExpression x = this.topRule.parse(lex);
         if (lex.getType() != Integer.MAX_VALUE) {
            throw new EvalException(1005, lex);
         } else {
            return x;
         }
      }
   }

   class EmptyExpression extends Expression {
      public long evalLong() {
         return 0L;
      }

      public double evalDouble() {
         return 0.0D;
      }

      public Object eval() {
         return null;
      }

      public void optimizeLong(Variable var) {
      }

      public void optimizeDouble(Variable var) {
      }

      public void optimize(Variable var, Operator oper) {
      }

      public void search(Search srch) {
      }

      public void refactorName(Refactor ref) {
      }

      public void refactorFunc(Refactor ref, Rule rule) {
      }

      public Expression dup() {
         return ShareRuleValue.this.new EmptyExpression();
      }

      public boolean same(Expression obj) {
         return obj instanceof ShareRuleValue.EmptyExpression;
      }

      public String toString() {
         return "";
      }
   }
}
