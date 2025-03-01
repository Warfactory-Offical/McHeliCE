package com.norwood.mcheli.eval.eval.exp;

public abstract class Col2OpeExpression extends Col2Expression {
   protected Col2OpeExpression() {
   }

   protected Col2OpeExpression(Col2Expression from, ShareExpValue s) {
      super(from, s);
   }

   protected final long operateLong(long vl, long vr) {
      throw new RuntimeException("この関数が呼ばれてはいけない");
   }

   protected final double operateDouble(double vl, double vr) {
      throw new RuntimeException("この関数が呼ばれてはいけない");
   }

   protected final Object operateObject(Object vl, Object vr) {
      throw new RuntimeException("この関数が呼ばれてはいけない");
   }

   protected AbstractExpression replace() {
      this.expl = this.expl.replace();
      this.expr = this.expr.replace();
      return this.share.repl.replace2(this);
   }

   protected AbstractExpression replaceVar() {
      this.expl = this.expl.replaceVar();
      this.expr = this.expr.replaceVar();
      return this.share.repl.replaceVar2(this);
   }
}
