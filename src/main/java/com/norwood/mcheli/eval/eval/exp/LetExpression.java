package com.norwood.mcheli.eval.eval.exp;

public class LetExpression extends Col2OpeExpression {
   public LetExpression() {
      this.setOperator("=");
   }

   protected LetExpression(LetExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new LetExpression(this, s);
   }

   public long evalLong() {
      long val = this.expr.evalLong();
      this.expl.let(val, this.pos);
      return val;
   }

   public double evalDouble() {
      double val = this.expr.evalDouble();
      this.expl.let(val, this.pos);
      return val;
   }

   public Object evalObject() {
      Object val = this.expr.evalObject();
      this.expl.let(val, this.pos);
      return val;
   }

   protected AbstractExpression replace() {
      this.expl = this.expl.replaceVar();
      this.expr = this.expr.replace();
      return this.share.repl.replaceLet(this);
   }
}
