package com.norwood.mcheli.eval.eval.exp;

public class ParenExpression extends Col1Expression {
   public ParenExpression() {
      this.setOperator("(");
      this.setEndOperator(")");
   }

   protected ParenExpression(ParenExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new ParenExpression(this, s);
   }

   protected long operateLong(long val) {
      return val;
   }

   protected double operateDouble(double val) {
      return val;
   }

   public Object evalObject() {
      return this.exp.evalObject();
   }

   public String toString() {
      return this.exp == null ? "" : this.getOperator() + this.exp.toString() + this.getEndOperator();
   }
}
