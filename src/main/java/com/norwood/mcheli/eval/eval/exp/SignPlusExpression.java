package com.norwood.mcheli.eval.eval.exp;

public class SignPlusExpression extends Col1Expression {
   public SignPlusExpression() {
      this.setOperator("+");
   }

   protected SignPlusExpression(SignPlusExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new SignPlusExpression(this, s);
   }

   protected long operateLong(long val) {
      return val;
   }

   protected double operateDouble(double val) {
      return val;
   }

   public Object evalObject() {
      return this.share.oper.signPlus(this.exp.evalObject());
   }
}
