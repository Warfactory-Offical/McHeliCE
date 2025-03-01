package com.norwood.mcheli.eval.eval.exp;

public abstract class WordExpression extends AbstractExpression {
   protected String word;

   protected WordExpression(String str) {
      this.word = str;
   }

   protected WordExpression(WordExpression from, ShareExpValue s) {
      super(from, s);
      this.word = from.word;
   }

   protected String getWord() {
      return this.word;
   }

   protected void setWord(String word) {
      this.word = word;
   }

   protected int getCols() {
      return 0;
   }

   protected int getFirstPos() {
      return this.pos;
   }

   protected void search() {
      this.share.srch.search(this);
      if (!this.share.srch.end()) {
         this.share.srch.search0(this);
      }
   }

   protected AbstractExpression replace() {
      return this.share.repl.replace0(this);
   }

   protected AbstractExpression replaceVar() {
      return this.share.repl.replaceVar0(this);
   }

   public boolean equals(Object obj) {
      if (obj instanceof WordExpression) {
         WordExpression e = (WordExpression)obj;
         if (this.getClass() == e.getClass()) {
            return this.word.equals(e.word);
         }
      }

      return false;
   }

   public int hashCode() {
      return this.word.hashCode();
   }

   public void dump(int n) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < n; ++i) {
         sb.append(' ');
      }

      sb.append(this.word);
      System.out.println(sb.toString());
   }

   public String toString() {
      return this.word;
   }
}
