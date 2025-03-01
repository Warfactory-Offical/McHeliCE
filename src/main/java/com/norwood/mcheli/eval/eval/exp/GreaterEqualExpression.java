package com.norwood.mcheli.eval.eval.exp;

public class GreaterEqualExpression extends Col2Expression {
   public GreaterEqualExpression() {
      this.setOperator(">=");
   }

   protected GreaterEqualExpression(GreaterEqualExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new GreaterEqualExpression(this, s);
   }

   protected long operateLong(long vl, long vr) {
      return vl >= vr ? 1L : 0L;
   }

   protected double operateDouble(double vl, double vr) {
      return vl >= vr ? 1.0D : 0.0D;
   }

   protected Object operateObject(Object vl, Object vr) {
      return this.share.oper.greaterEqual(vl, vr);
   }
}
