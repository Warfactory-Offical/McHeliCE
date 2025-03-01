package com.norwood.mcheli.eval.eval.oper;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JavaExOperator implements Operator {
   boolean inLong(Object x) {
      if (x instanceof Long) {
         return true;
      } else if (x instanceof Integer) {
         return true;
      } else if (x instanceof Short) {
         return true;
      } else if (x instanceof Byte) {
         return true;
      } else if (x instanceof BigInteger) {
         return true;
      } else {
         return x instanceof BigDecimal;
      }
   }

   long l(Object x) {
      return ((Number)x).longValue();
   }

   boolean inDouble(Object x) {
      if (x instanceof Double) {
         return true;
      } else {
         return x instanceof Float;
      }
   }

   double d(Object x) {
      return ((Number)x).doubleValue();
   }

   Object n(long n, Object x) {
      if (x instanceof Long) {
         return new Long(n);
      } else if (x instanceof Double) {
         return new Double((double)n);
      } else if (x instanceof Integer) {
         return new Integer((int)n);
      } else if (x instanceof Short) {
         return new Short((short)((int)n));
      } else if (x instanceof Byte) {
         return new Byte((byte)((int)n));
      } else if (x instanceof Float) {
         return new Float((float)n);
      } else if (x instanceof BigInteger) {
         return BigInteger.valueOf(n);
      } else if (x instanceof BigDecimal) {
         return BigDecimal.valueOf(n);
      } else {
         return x instanceof String ? String.valueOf(n) : new Long(n);
      }
   }

   Object n(long n, Object x, Object y) {
      if (!(x instanceof Byte) && !(y instanceof Byte)) {
         if (!(x instanceof Short) && !(y instanceof Short)) {
            if (!(x instanceof Integer) && !(y instanceof Integer)) {
               if (!(x instanceof Long) && !(y instanceof Long)) {
                  if (!(x instanceof BigInteger) && !(y instanceof BigInteger)) {
                     if (!(x instanceof BigDecimal) && !(y instanceof BigDecimal)) {
                        if (!(x instanceof Float) && !(y instanceof Float)) {
                           if (!(x instanceof Double) && !(y instanceof Double)) {
                              return !(x instanceof String) && !(y instanceof String) ? new Long(n) : String.valueOf(n);
                           } else {
                              return new Double((double)n);
                           }
                        } else {
                           return new Float((float)n);
                        }
                     } else {
                        return BigDecimal.valueOf(n);
                     }
                  } else {
                     return BigInteger.valueOf(n);
                  }
               } else {
                  return new Long(n);
               }
            } else {
               return new Integer((int)n);
            }
         } else {
            return new Short((short)((int)n));
         }
      } else {
         return new Byte((byte)((int)n));
      }
   }

   Object n(double n, Object x) {
      if (x instanceof Float) {
         return new Float(n);
      } else {
         return x instanceof String ? String.valueOf(n) : new Double(n);
      }
   }

   Object n(double n, Object x, Object y) {
      if (!(x instanceof Float) && !(y instanceof Float)) {
         if (!(x instanceof Number) && !(y instanceof Number)) {
            return !(x instanceof String) && !(y instanceof String) ? new Double(n) : String.valueOf(n);
         } else {
            return new Double(n);
         }
      } else {
         return new Float(n);
      }
   }

   Object nn(long n, Object x) {
      if (x instanceof BigDecimal) {
         return BigDecimal.valueOf(n);
      } else {
         return x instanceof BigInteger ? BigInteger.valueOf(n) : new Long(n);
      }
   }

   Object nn(long n, Object x, Object y) {
      if (!(x instanceof BigDecimal) && !(y instanceof BigDecimal)) {
         return !(x instanceof BigInteger) && !(y instanceof BigInteger) ? new Long(n) : BigInteger.valueOf(n);
      } else {
         return BigDecimal.valueOf(n);
      }
   }

   Object nn(double n, Object x) {
      return this.inLong(x) ? new Long((long)n) : new Double(n);
   }

   Object nn(double n, Object x, Object y) {
      return this.inLong(x) && this.inLong(y) ? new Long((long)n) : new Double(n);
   }

   RuntimeException undefined(Object x) {
      String c = null;
      if (x != null) {
         c = x.getClass().getName();
      }

      return new RuntimeException("未定義単項演算：" + c);
   }

   RuntimeException undefined(Object x, Object y) {
      String c1 = null;
      String c2 = null;
      if (x != null) {
         c1 = x.getClass().getName();
      }

      if (y != null) {
         c2 = y.getClass().getName();
      }

      return new RuntimeException("未定義二項演算：" + c1 + " , " + c2);
   }

   public Object power(Object x, Object y) {
      return x == null && y == null ? null : this.nn(Math.pow(this.d(x), this.d(y)), x, y);
   }

   public Object signPlus(Object x) {
      return x;
   }

   public Object signMinus(Object x) {
      if (x == null) {
         return null;
      } else if (this.inLong(x)) {
         return this.n(-this.l(x), x);
      } else if (this.inDouble(x)) {
         return this.n(-this.d(x), x);
      } else if (x instanceof Boolean) {
         return x;
      } else {
         throw this.undefined(x);
      }
   }

   public Object plus(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (this.inLong(x) && this.inLong(y)) {
         return this.nn(this.l(x) + this.l(y), x, y);
      } else if (this.inDouble(x) && this.inDouble(y)) {
         return this.nn(this.d(x) + this.d(y), x, y);
      } else if (!(x instanceof String) && !(y instanceof String)) {
         if (!(x instanceof Character) && !(y instanceof Character)) {
            throw this.undefined(x, y);
         } else {
            return x + String.valueOf(y);
         }
      } else {
         return x + String.valueOf(y);
      }
   }

   public Object minus(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (this.inLong(x) && this.inLong(y)) {
         return this.nn(this.l(x) - this.l(y), x, y);
      } else if (this.inDouble(x) && this.inDouble(y)) {
         return this.nn(this.d(x) - this.d(y), x, y);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object mult(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (this.inLong(x) && this.inLong(y)) {
         return this.nn(this.l(x) * this.l(y), x, y);
      } else if (this.inDouble(x) && this.inDouble(y)) {
         return this.nn(this.d(x) * this.d(y), x, y);
      } else {
         String s = null;
         int ct = 0;
         boolean str = false;
         if (x instanceof String && y instanceof Number) {
            s = (String)x;
            ct = (int)this.l(y);
            str = true;
         } else if (y instanceof String && x instanceof Number) {
            s = (String)y;
            ct = (int)this.l(x);
            str = true;
         }

         if (!str) {
            throw this.undefined(x, y);
         } else {
            StringBuffer sb = new StringBuffer(s.length() * ct);

            for(int i = 0; i < ct; ++i) {
               sb.append(s);
            }

            return sb.toString();
         }
      }
   }

   public Object div(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (this.inLong(x) && this.inLong(y)) {
         return this.nn(this.l(x) / this.l(y), x);
      } else if (this.inDouble(x) && this.inDouble(y)) {
         return this.nn(this.d(x) / this.d(y), x);
      } else if (x instanceof String && y instanceof String) {
         String s = (String)x;
         String r = (String)y;
         return s.split(r);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object mod(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (this.inLong(x) && this.inLong(y)) {
         return this.nn(this.l(x) % this.l(y), x);
      } else if (this.inDouble(x) && this.inDouble(y)) {
         return this.nn(this.d(x) % this.d(y), x);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object bitNot(Object x) {
      if (x == null) {
         return null;
      } else if (x instanceof Number) {
         return this.n(~this.l(x), x);
      } else {
         throw this.undefined(x);
      }
   }

   public Object shiftLeft(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (this.inLong(x) && this.inLong(y)) {
         return this.n(this.l(x) << (int)this.l(y), x);
      } else if (this.inDouble(x) && this.inDouble(y)) {
         return this.n(this.d(x) * Math.pow(2.0D, this.d(y)), x);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object shiftRight(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (this.inLong(x) && this.inLong(y)) {
         return this.n(this.l(x) >> (int)this.l(y), x);
      } else if (this.inDouble(x) && this.inDouble(y)) {
         return this.n(this.d(x) / Math.pow(2.0D, this.d(y)), x);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object shiftRightLogical(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (x instanceof Byte && y instanceof Number) {
         return this.n((this.l(x) & 255L) >>> (int)this.l(y), x);
      } else if (x instanceof Short && y instanceof Number) {
         return this.n((this.l(x) & 65535L) >>> (int)this.l(y), x);
      } else if (x instanceof Integer && y instanceof Number) {
         return this.n((this.l(x) & -1L) >>> (int)this.l(y), x);
      } else if (this.inLong(x) && y instanceof Number) {
         return this.n(this.l(x) >>> (int)this.l(y), x);
      } else if (this.inDouble(x) && y instanceof Number) {
         double t = this.d(x);
         if (t < 0.0D) {
            t = -t;
         }

         return this.n(t / Math.pow(2.0D, this.d(y)), x);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object bitAnd(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (x instanceof Number && y instanceof Number) {
         return this.n(this.l(x) & this.l(y), x);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object bitOr(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (x instanceof Number && y instanceof Number) {
         return this.n(this.l(x) | this.l(y), x);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object bitXor(Object x, Object y) {
      if (x == null && y == null) {
         return null;
      } else if (x instanceof Number && y instanceof Number) {
         return this.n(this.l(x) ^ this.l(y), x);
      } else {
         throw this.undefined(x, y);
      }
   }

   public Object not(Object x) {
      if (x == null) {
         return null;
      } else if (x instanceof Boolean) {
         return (Boolean)x ? Boolean.FALSE : Boolean.TRUE;
      } else if (x instanceof Number) {
         return this.l(x) == 0L ? Boolean.TRUE : Boolean.FALSE;
      } else {
         throw this.undefined(x);
      }
   }

   int compare(Object x, Object y) {
      if (x == null && y == null) {
         return 0;
      } else if (x == null && y != null) {
         return -1;
      } else if (x != null && y == null) {
         return 1;
      } else if (this.inLong(x) && this.inLong(y)) {
         long c = this.l(x) - this.l(y);
         if (c == 0L) {
            return 0;
         } else {
            return c < 0L ? -1 : 1;
         }
      } else if (x instanceof Number && y instanceof Number) {
         double n = this.d(x) - this.d(y);
         if (n == 0.0D) {
            return 0;
         } else {
            return n < 0.0D ? -1 : 1;
         }
      } else {
         Class<?> xc = x.getClass();
         Class<?> yc = y.getClass();
         if (xc.isAssignableFrom(yc) && x instanceof Comparable) {
            return ((Comparable)x).compareTo(y);
         } else if (yc.isAssignableFrom(xc) && y instanceof Comparable) {
            return -((Comparable)y).compareTo(x);
         } else if (x.equals(y)) {
            return 0;
         } else {
            throw this.undefined(x, y);
         }
      }
   }

   public Object equal(Object x, Object y) {
      return this.compare(x, y) == 0 ? Boolean.TRUE : Boolean.FALSE;
   }

   public Object notEqual(Object x, Object y) {
      return this.compare(x, y) != 0 ? Boolean.TRUE : Boolean.FALSE;
   }

   public Object lessThan(Object x, Object y) {
      return this.compare(x, y) < 0 ? Boolean.TRUE : Boolean.FALSE;
   }

   public Object lessEqual(Object x, Object y) {
      return this.compare(x, y) <= 0 ? Boolean.TRUE : Boolean.FALSE;
   }

   public Object greaterThan(Object x, Object y) {
      return this.compare(x, y) > 0 ? Boolean.TRUE : Boolean.FALSE;
   }

   public Object greaterEqual(Object x, Object y) {
      return this.compare(x, y) >= 0 ? Boolean.TRUE : Boolean.FALSE;
   }

   public boolean bool(Object x) {
      if (x == null) {
         return false;
      } else if (x instanceof Boolean) {
         return (Boolean)x;
      } else if (x instanceof Number) {
         return ((Number)x).longValue() != 0L;
      } else {
         return Boolean.valueOf(x.toString());
      }
   }

   public Object inc(Object x, int inc) {
      if (x == null) {
         return null;
      } else if (this.inLong(x)) {
         return this.n(this.l(x) + (long)inc, x);
      } else if (this.inDouble(x)) {
         return this.n(this.d(x) + (double)inc, x);
      } else {
         throw this.undefined(x);
      }
   }
}
