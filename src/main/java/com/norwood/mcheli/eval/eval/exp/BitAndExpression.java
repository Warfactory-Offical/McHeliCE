package com.norwood.mcheli.eval.eval.exp;

public class BitAndExpression extends Col2Expression {
   public BitAndExpression() {
      this.setOperator("&");
   }

   protected BitAndExpression(BitAndExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new BitAndExpression(this, s);
   }

   protected long operateLong(long vl, long vr) {
      return vl & vr;
   }

   protected double operateDouble(double vl, double vr) {
      return (double)((long)vl & (long)vr);
   }

   protected Object operateObject(Object vl, Object vr) {
      return this.share.oper.bitAnd(vl, vr);
   }
}
