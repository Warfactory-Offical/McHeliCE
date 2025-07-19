package com.norwood.mcheli.eval.eval.lex;

import com.norwood.mcheli.eval.eval.exp.ShareExpValue;
import com.norwood.mcheli.eval.eval.rule.ShareRuleValue;

import java.util.List;

public class LexFactory {
    public Lex create(String str, List<String>[] opeList, ShareRuleValue share, ShareExpValue exp) {
        return new Lex(str, opeList, share.paren, exp);
    }
}
