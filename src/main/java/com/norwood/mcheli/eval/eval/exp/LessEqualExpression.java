package com.norwood.mcheli.eval.eval.exp;

public class LessEqualExpression extends Col2Expression {
   public LessEqualExpression() {
      this.setOperator("<=");
   }

   protected LessEqualExpression(LessEqualExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new LessEqualExpression(this, s);
   }

   protected long operateLong(long vl, long vr) {
      return vl <= vr ? 1L : 0L;
   }

   protected double operateDouble(double vl, double vr) {
      return vl <= vr ? 1.0D : 0.0D;
   }

   protected Object operateObject(Object vl, Object vr) {
      return this.share.oper.lessEqual(vl, vr);
   }
}
