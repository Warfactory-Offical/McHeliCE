package com.norwood.mcheli.eval.eval.exp;

public class OrExpression extends Col2OpeExpression {
   public OrExpression() {
      this.setOperator("||");
   }

   protected OrExpression(OrExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new OrExpression(this, s);
   }

   public long evalLong() {
      long val = this.expl.evalLong();
      return val != 0L ? val : this.expr.evalLong();
   }

   public double evalDouble() {
      double val = this.expl.evalDouble();
      return val != 0.0D ? val : this.expr.evalDouble();
   }

   public Object evalObject() {
      Object val = this.expl.evalObject();
      return this.share.oper.bool(val) ? val : this.expr.evalObject();
   }
}
