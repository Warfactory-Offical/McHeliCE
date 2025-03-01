package com.norwood.mcheli.eval.eval.exp;

public class BitOrExpression extends Col2Expression {
   public BitOrExpression() {
      this.setOperator("|");
   }

   protected BitOrExpression(BitOrExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new BitOrExpression(this, s);
   }

   protected long operateLong(long vl, long vr) {
      return vl | vr;
   }

   protected double operateDouble(double vl, double vr) {
      return (double)((long)vl | (long)vr);
   }

   protected Object operateObject(Object vl, Object vr) {
      return this.share.oper.bitOr(vl, vr);
   }
}
