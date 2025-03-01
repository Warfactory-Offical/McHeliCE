package com.norwood.mcheli.eval.eval.exp;

import com.norwood.mcheli.eval.eval.EvalException;

public class ArrayExpression extends Col2OpeExpression {
   public ArrayExpression() {
      this.setOperator("[");
      this.setEndOperator("]");
   }

   protected ArrayExpression(ArrayExpression from, ShareExpValue s) {
      super(from, s);
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new ArrayExpression(this, s);
   }

   public long evalLong() {
      try {
         return this.share.var.evalLong(this.getVariable());
      } catch (EvalException var2) {
         throw var2;
      } catch (Exception var3) {
         throw new EvalException(2201, this.toString(), this.string, this.pos, var3);
      }
   }

   public double evalDouble() {
      try {
         return this.share.var.evalDouble(this.getVariable());
      } catch (EvalException var2) {
         throw var2;
      } catch (Exception var3) {
         throw new EvalException(2201, this.toString(), this.string, this.pos, var3);
      }
   }

   public Object evalObject() {
      return this.getVariable();
   }

   protected Object getVariable() {
      Object obj = this.expl.getVariable();
      if (obj == null) {
         throw new EvalException(2104, this.expl.toString(), this.string, this.pos, (Throwable)null);
      } else {
         int index = (int)this.expr.evalLong();

         try {
            return this.share.var.getObject(obj, index);
         } catch (EvalException var4) {
            throw var4;
         } catch (Exception var5) {
            throw new EvalException(2201, this.toString(), this.string, this.pos, var5);
         }
      }
   }

   protected void let(Object val, int pos) {
      Object obj = this.expl.getVariable();
      if (obj == null) {
         throw new EvalException(2104, this.expl.toString(), this.string, pos, (Throwable)null);
      } else {
         int index = (int)this.expr.evalLong();

         try {
            this.share.var.setValue(obj, index, val);
         } catch (EvalException var6) {
            throw var6;
         } catch (Exception var7) {
            throw new EvalException(2202, this.toString(), this.string, pos, var7);
         }
      }
   }

   protected AbstractExpression replaceVar() {
      this.expl = this.expl.replaceVar();
      this.expr = this.expr.replace();
      return this.share.repl.replaceVar2((Col2OpeExpression)this);
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.expl.toString());
      sb.append('[');
      sb.append(this.expr.toString());
      sb.append(']');
      return sb.toString();
   }
}
