package com.norwood.mcheli.eval.eval.exp;

public class IncAfterExpression extends Col1AfterExpression {
   public IncAfterExpression() {
      this.setOperator("++");
   }

   protected IncAfterExpression(IncAfterExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new IncAfterExpression(this, s);
   }

   protected long operateLong(long val) {
      this.exp.let(val + 1L, this.pos);
      return val;
   }

   protected double operateDouble(double val) {
      this.exp.let(val + 1.0D, this.pos);
      return val;
   }

   public Object evalObject() {
      Object val = this.exp.evalObject();
      this.exp.let(this.share.oper.inc(val, 1), this.pos);
      return val;
   }
}
