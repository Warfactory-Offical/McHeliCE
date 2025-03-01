package com.norwood.mcheli.eval.eval.exp;

public class LetXorExpression extends BitXorExpression {
   public LetXorExpression() {
      this.setOperator("^=");
   }

   protected LetXorExpression(LetXorExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new LetXorExpression(this, s);
   }

   public long evalLong() {
      long val = super.evalLong();
      this.expl.let(val, this.pos);
      return val;
   }

   public double evalDouble() {
      double val = super.evalDouble();
      this.expl.let(val, this.pos);
      return val;
   }

   public Object evalObject() {
      Object val = super.evalObject();
      this.expl.let(val, this.pos);
      return val;
   }

   protected AbstractExpression replace() {
      this.expl = this.expl.replaceVar();
      this.expr = this.expr.replace();
      return this.share.repl.replaceLet(this);
   }
}
