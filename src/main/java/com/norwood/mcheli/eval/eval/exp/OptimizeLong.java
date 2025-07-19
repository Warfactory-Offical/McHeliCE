package com.norwood.mcheli.eval.eval.exp;

public class OptimizeLong extends OptimizeObject {
    @Override
    protected boolean isTrue(AbstractExpression x) {
        return x.evalLong() != 0L;
    }

    @Override
    protected AbstractExpression toConst(AbstractExpression exp) {
        try {
            long val = exp.evalLong();
            return NumberExpression.create(exp, Long.toString(val));
        } catch (Exception var4) {
            return exp;
        }
    }
}
