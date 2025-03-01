package com.norwood.mcheli.eval.eval.exp;

import com.norwood.mcheli.eval.eval.EvalException;
import com.norwood.mcheli.eval.eval.lex.Lex;
import com.norwood.mcheli.eval.util.CharUtil;
import com.norwood.mcheli.eval.util.NumberUtil;

public class StringExpression extends WordExpression {
   public static AbstractExpression create(Lex lex, int prio) {
      String str = lex.getWord();
      str = CharUtil.escapeString(str, 1, str.length() - 2);
      AbstractExpression exp = new StringExpression(str);
      exp.setPos(lex.getString(), lex.getPos());
      exp.setPriority(prio);
      exp.share = lex.getShare();
      return exp;
   }

   public StringExpression(String str) {
      super(str);
      this.setOperator("\"");
      this.setEndOperator("\"");
   }

   protected StringExpression(StringExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new StringExpression(this, s);
   }

   public static StringExpression create(AbstractExpression from, String word) {
      StringExpression n = new StringExpression(word);
      n.string = from.string;
      n.pos = from.pos;
      n.prio = from.prio;
      n.share = from.share;
      return n;
   }

   public long evalLong() {
      try {
         return NumberUtil.parseLong(this.word);
      } catch (Exception var6) {
         try {
            return Long.parseLong(this.word);
         } catch (Exception var5) {
            try {
               return (long)Double.parseDouble(this.word);
            } catch (Exception var4) {
               throw new EvalException(2003, this.word, this.string, this.pos, var4);
            }
         }
      }
   }

   public double evalDouble() {
      try {
         return Double.parseDouble(this.word);
      } catch (Exception var4) {
         try {
            return (double)NumberUtil.parseLong(this.word);
         } catch (Exception var3) {
            throw new EvalException(2003, this.word, this.string, this.pos, var4);
         }
      }
   }

   public Object evalObject() {
      return this.word;
   }

   public boolean equals(Object obj) {
      if (obj instanceof StringExpression) {
         StringExpression e = (StringExpression)obj;
         return this.word.equals(e.word);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.word.hashCode();
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.getOperator());
      sb.append(this.word);
      sb.append(this.getEndOperator());
      return sb.toString();
   }
}
