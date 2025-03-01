package com.norwood.mcheli.eval.eval.exp;

public class ShiftLeftExpression extends Col2Expression {
   public ShiftLeftExpression() {
      this.setOperator("<<");
   }

   protected ShiftLeftExpression(ShiftLeftExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new ShiftLeftExpression(this, s);
   }

   protected long operateLong(long vl, long vr) {
      return vl << (int)vr;
   }

   protected double operateDouble(double vl, double vr) {
      return vl * Math.pow(2.0D, vr);
   }

   protected Object operateObject(Object vl, Object vr) {
      return this.share.oper.shiftLeft(vl, vr);
   }
}
