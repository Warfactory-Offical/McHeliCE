package com.norwood.mcheli.eval.eval.exp;

import java.util.ArrayList;
import java.util.List;
import com.norwood.mcheli.eval.eval.EvalException;

public class FunctionExpression extends Col1Expression {
   protected AbstractExpression target;
   String name;

   public static AbstractExpression create(AbstractExpression x, AbstractExpression args, int prio, ShareExpValue share) {
      AbstractExpression obj;
      if (x instanceof VariableExpression) {
         obj = null;
      } else {
         if (!(x instanceof FieldExpression)) {
            throw new EvalException(1101, x.toString(), x.string, x.pos, (Throwable)null);
         }

         FieldExpression f = (FieldExpression)x;
         obj = f.expl;
         x = f.expr;
      }

      String name = x.getWord();
      FunctionExpression f = new FunctionExpression(obj, name);
      f.setExpression(args);
      f.setPos(x.string, x.pos);
      f.setPriority(prio);
      f.share = share;
      return f;
   }

   public FunctionExpression() {
      this.setOperator("(");
      this.setEndOperator(")");
   }

   public FunctionExpression(AbstractExpression obj, String word) {
      this();
      this.target = obj;
      this.name = word;
   }

   protected FunctionExpression(FunctionExpression from, ShareExpValue s) {
      super(from, s);
      if (from.target != null) {
         this.target = from.target.dup(s);
      }

      this.name = from.name;
   }

   public AbstractExpression dup(ShareExpValue s) {
      return new FunctionExpression(this, s);
   }

   public long evalLong() {
      Object obj = null;
      if (this.target != null) {
         obj = this.target.getVariable();
      }

      List args = this.evalArgsLong();

      try {
         Long[] arr = new Long[args.size()];
         return this.share.func.evalLong(obj, this.name, (Long[])args.toArray(arr));
      } catch (EvalException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new EvalException(2401, this.name, this.string, this.pos, var5);
      }
   }

   public double evalDouble() {
      Object obj = null;
      if (this.target != null) {
         obj = this.target.getVariable();
      }

      List args = this.evalArgsDouble();

      try {
         Double[] arr = new Double[args.size()];
         return this.share.func.evalDouble(obj, this.name, (Double[])args.toArray(arr));
      } catch (EvalException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new EvalException(2401, this.name, this.string, this.pos, var5);
      }
   }

   public Object evalObject() {
      Object obj = null;
      if (this.target != null) {
         obj = this.target.getVariable();
      }

      List args = this.evalArgsObject();

      try {
         Object[] arr = new Object[args.size()];
         return this.share.func.evalObject(obj, this.name, args.toArray(arr));
      } catch (EvalException var4) {
         throw var4;
      } catch (Throwable var5) {
         throw new EvalException(2401, this.name, this.string, this.pos, var5);
      }
   }

   private List<Long> evalArgsLong() {
      List<Long> args = new ArrayList();
      if (this.exp != null) {
         this.exp.evalArgsLong(args);
      }

      return args;
   }

   private List<Double> evalArgsDouble() {
      List<Double> args = new ArrayList();
      if (this.exp != null) {
         this.exp.evalArgsDouble(args);
      }

      return args;
   }

   private List<Object> evalArgsObject() {
      List<Object> args = new ArrayList();
      if (this.exp != null) {
         this.exp.evalArgsObject(args);
      }

      return args;
   }

   protected Object getVariable() {
      return this.evalObject();
   }

   protected long operateLong(long val) {
      throw new RuntimeException("この関数が呼ばれてはいけない。サブクラスで実装要");
   }

   protected double operateDouble(double val) {
      throw new RuntimeException("この関数が呼ばれてはいけない。サブクラスで実装要");
   }

   protected void search() {
      this.share.srch.search(this);
      if (!this.share.srch.end()) {
         if (!this.share.srch.searchFunc_begin(this)) {
            if (!this.share.srch.end()) {
               if (this.target != null) {
                  this.target.search();
                  if (this.share.srch.end()) {
                     return;
                  }
               }

               if (!this.share.srch.searchFunc_2(this)) {
                  if (!this.share.srch.end()) {
                     if (this.exp != null) {
                        this.exp.search();
                        if (this.share.srch.end()) {
                           return;
                        }
                     }

                     this.share.srch.searchFunc_end(this);
                  }
               }
            }
         }
      }
   }

   protected AbstractExpression replace() {
      if (this.target != null) {
         this.target = this.target.replace();
      }

      if (this.exp != null) {
         this.exp = this.exp.replace();
      }

      return this.share.repl.replaceFunc(this);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof FunctionExpression)) {
         return false;
      } else {
         FunctionExpression e = (FunctionExpression)obj;
         return this.name.equals(e.name) && equals(this.target, e.target) && equals(this.exp, e.exp);
      }
   }

   private static boolean equals(AbstractExpression e1, AbstractExpression e2) {
      if (e1 == null) {
         return e2 == null;
      } else {
         return e2 == null ? false : e1.equals(e2);
      }
   }

   public int hashCode() {
      int t = this.target != null ? this.target.hashCode() : 0;
      int a = this.exp != null ? this.exp.hashCode() : 0;
      return this.name.hashCode() ^ t ^ a * 2;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      if (this.target != null) {
         sb.append(this.target.toString());
         sb.append('.');
      }

      sb.append(this.name);
      sb.append('(');
      if (this.exp != null) {
         sb.append(this.exp.toString());
      }

      sb.append(')');
      return sb.toString();
   }
}
