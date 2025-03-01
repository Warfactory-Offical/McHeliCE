package com.norwood.mcheli.eval.eval.exp;

public class IfExpression extends Col3Expression {
   public IfExpression() {
      this.setOperator("?");
      this.setEndOperator(":");
   }

   protected IfExpression(IfExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new IfExpression(this, s);
   }

   public long evalLong() {
      return this.exp1.evalLong() != 0L ? this.exp2.evalLong() : this.exp3.evalLong();
   }

   public double evalDouble() {
      return this.exp1.evalDouble() != 0.0D ? this.exp2.evalDouble() : this.exp3.evalDouble();
   }

   public Object evalObject() {
      return this.share.oper.bool(this.exp1.evalObject()) ? this.exp2.evalObject() : this.exp3.evalObject();
   }
}
