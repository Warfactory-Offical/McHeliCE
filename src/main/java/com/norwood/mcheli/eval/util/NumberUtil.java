package com.norwood.mcheli.eval.util;

public class NumberUtil {
   public static long parseLong(String str) {
      if (str == null) {
         return 0L;
      } else {
         str = str.trim();
         int len = str.length();
         if (len <= 0) {
            return 0L;
         } else {
            switch (str.charAt(len - 1)) {
               case '.':
               case 'L':
               case 'l':
                  len--;
               default:
                  if (len >= 3 && str.charAt(0) == '0') {
                     switch (str.charAt(1)) {
                        case 'B':
                        case 'b':
                           return parseLongBin(str, 2, len - 2);
                        case 'O':
                        case 'o':
                           return parseLongOct(str, 2, len - 2);
                        case 'X':
                        case 'x':
                           return parseLongHex(str, 2, len - 2);
                     }
                  }

                  return parseLongDec(str, 0, len);
            }
         }
      }
   }

   public static long parseLongBin(String str) {
      return str == null ? 0L : parseLongBin(str, 0, str.length());
   }

   public static long parseLongBin(String str, int pos, int len) {
      long ret = 0L;

      for (int i = 0; i < len; i++) {
         ret *= 2L;
         char c = str.charAt(pos++);
         switch (c) {
            case '1':
               ret++;
               break;
            case '0':
            default:
               throw new NumberFormatException(str.substring(pos, len));
         }
      }

      return ret;
   }

   public static long parseLongOct(String str) {
      return str == null ? 0L : parseLongOct(str, 0, str.length());
   }

   public static long parseLongOct(String str, int pos, int len) {
      long ret = 0L;

      for (int i = 0; i < len; i++) {
         ret *= 8L;
         char c = str.charAt(pos++);
         switch (c) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
               ret += c - '0';
               break;
            case '0':
            default:
               throw new NumberFormatException(str.substring(pos, len));
         }
      }

      return ret;
   }

   public static long parseLongDec(String str) {
      return str == null ? 0L : parseLongDec(str, 0, str.length());
   }

   public static long parseLongDec(String str, int pos, int len) {
      long ret = 0L;

      for (int i = 0; i < len; i++) {
         ret *= 10L;
         char c = str.charAt(pos++);
         switch (c) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
               ret += c - '0';
               break;
            case '0':
            default:
               throw new NumberFormatException(str.substring(pos, len));
         }
      }

      return ret;
   }

   public static long parseLongHex(String str) {
      return str == null ? 0L : parseLongHex(str, 0, str.length());
   }

   public static long parseLongHex(String str, int pos, int len) {
      long ret = 0L;

      for (int i = 0; i < len; i++) {
         ret *= 16L;
         char c = str.charAt(pos++);
         switch (c) {
            case '0':
               break;
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
               ret += c - '0';
               break;
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            default:
               throw new NumberFormatException(str.substring(pos, len));
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
               ret += c - 'A' + 10;
               break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
               ret += c - 'a' + 10;
         }
      }

      return ret;
   }
}
