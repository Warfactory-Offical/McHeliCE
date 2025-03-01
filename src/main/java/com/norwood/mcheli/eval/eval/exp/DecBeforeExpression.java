package com.norwood.mcheli.eval.eval.exp;

public class DecBeforeExpression extends Col1Expression {
   public DecBeforeExpression() {
      this.setOperator("--");
   }

   protected DecBeforeExpression(DecBeforeExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new DecBeforeExpression(this, s);
   }

   protected long operateLong(long val) {
      --val;
      this.exp.let(val, this.pos);
      return val;
   }

   protected double operateDouble(double val) {
      --val;
      this.exp.let(val, this.pos);
      return val;
   }

   public Object evalObject() {
      Object val = this.exp.evalObject();
      val = this.share.oper.inc(val, -1);
      this.exp.let(val, this.pos);
      return val;
   }

   protected AbstractExpression replace() {
      this.exp = this.exp.replaceVar();
      return this.share.repl.replaceVar1(this);
   }
}
