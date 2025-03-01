package com.norwood.mcheli.eval.eval.exp;

public class CommaExpression extends Col2OpeExpression {
   public CommaExpression() {
      this.setOperator(",");
   }

   protected CommaExpression(CommaExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new CommaExpression(this, s);
   }

   public long evalLong() {
      this.expl.evalLong();
      return this.expr.evalLong();
   }

   public double evalDouble() {
      this.expl.evalDouble();
      return this.expr.evalDouble();
   }

   public Object evalObject() {
      this.expl.evalObject();
      return this.expr.evalObject();
   }

   protected String toStringLeftSpace() {
      return "";
   }
}
