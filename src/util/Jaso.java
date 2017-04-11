package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Jaso {

	private final static char[] CHOSUNG = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143,
			0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
	private final static char[] JUNGSUNG = { 0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157,
			0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163 };
	private final static char[] JONGSUNG = { 0, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a,
			0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148,
			0x314a, 0x314b, 0x314c, 0x314d, 0x314e };

	private final static String DIVIDER = "~";
	private final static int[] EMJEOL_CONVERT_MAP = { 28 * 21, 28, 1 };

	private final static ArrayList<HashMap<Character, Integer>> JASO_IDX = new ArrayList<HashMap<Character, Integer>>();
	private final static HashSet<Character> MOEMSET = new HashSet<Character>();

	static {
		HashMap<Character, Integer> choIdx = new HashMap<Character, Integer>();
		for (int i = 0; i < CHOSUNG.length; i++)
			choIdx.put(CHOSUNG[i], i);
		JASO_IDX.add(choIdx);

		HashMap<Character, Integer> jungIdx = new HashMap<Character, Integer>();
		for (int i = 0; i < JUNGSUNG.length; i++)
			jungIdx.put(JUNGSUNG[i], i);
		JASO_IDX.add(jungIdx);

		HashMap<Character, Integer> jongIdx = new HashMap<Character, Integer>();
		for (int i = 0; i < JONGSUNG.length; i++)
			jongIdx.put(JONGSUNG[i], i);
		JASO_IDX.add(jongIdx);
		
	}

	public static String emjeolToJaso(String str) {
		int a, b, c;
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00: °¡ ~ 0xD7A3: ÆR"
				c = ch - 0xAC00;
				a = c / (21 * 28);
				c = c % (21 * 28);
				b = c / 28;
				c = c % 28;

				result.append(CHOSUNG[a]);
				result.append(JUNGSUNG[b]);

				if (c != 0)
					result.append(JONGSUNG[c]);
			} else
				result.append(ch);

		}
		return result.toString();
	}

	public static String emjeolToJasoWithDivider(String str) {
		int a, b, c;
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00: °¡ ~ 0xD7A3: ÆR"
				c = ch - 0xAC00;
				a = c / (21 * 28);
				c = c % (21 * 28);
				b = c / 28;
				c = c % 28;

				result.append(CHOSUNG[a]);
				result.append(JUNGSUNG[b]);

				if (c != 0)
					result.append(JONGSUNG[c]);
			} else
				result.append(ch);

			result.append(DIVIDER);
		}
		return result.toString();
	}

	public static String jasoToEmjeolWithDivider(String str) {
		StringBuffer result = new StringBuffer();
		String[] spl = str.split(DIVIDER);
		System.out.println(spl.length);

		for (String emjeol : spl) {
			int emLen = emjeol.length();
			if (emLen == 1)
				result.append(emjeol);
			else {
				char temp = 0xAC00;

				for (int i = 0; i < emLen; i++)
					temp += (char) (JASO_IDX.get(i).get(emjeol.charAt(i)) * EMJEOL_CONVERT_MAP[i]);

				result.append(temp);
			}
		}
		return result.toString();
	}

	public static void main(String args[]) {
		String jaso = emjeolToJasoWithDivider("¾È³çÇÏ¼¼¿ä");
		System.out.println(jaso);
		System.out.println(jasoToEmjeolWithDivider(jaso));
	}

	// public static String convertToWord(String str){
	//
	// }
	// public class TempTest {
//  // ÄÚµåÅ¸ÀÔ - ÃÊ¼º, Áß¼º, Á¾¼º
//  enum CodeType{chosung, jungsung, jongsung}
//
//  public static void main(String[] args) throws UnsupportedEncodingException {
//   TempTest ts1 = new TempTest();
//   String result = ts1.engToKor("zjavbxjtkTek");
//   System.out.println(result);
//  }
//
//  /**
//    * ¿µ¾î¸¦ ÇÑ±Û·Î...
//    */
//  public String engToKor(String eng){
//   StringBuffer sb = new StringBuffer();
//   int initialCode = 0, medialCode = 0, finalCode = 0;
//   int tempMedialCode, tempFinalCode;
//
//   for(int i = 0; i < eng.length(); i++){
//    // ÃÊ¼ºÄÚµå ÃßÃâ
//    initialCode = getCode(CodeType.chosung, eng.substring(i, i+1));
//    i++; // ´ÙÀ½¹®ÀÚ·Î
//
//    // Áß¼ºÄÚµå ÃßÃâ
//    tempMedialCode = getDoubleMedial(i, eng);   // µÎ ÀÚ·Î ÀÌ·ç¾îÁø Áß¼ºÄÚµå ÃßÃâ
//
//    if(tempMedialCode != -1){
//     medialCode = tempMedialCode;
//     i += 2;
//    }else{            // ¾ø´Ù¸é,
//     medialCode = getSingleMedial(i, eng);   // ÇÑ ÀÚ·Î ÀÌ·ç¾îÁø Áß¼ºÄÚµå ÃßÃâ
//     i++;
//    }
//
//    // Á¾¼ºÄÚµå ÃßÃâ
//    tempFinalCode = getDoubleFinal(i, eng);    // µÎ ÀÚ·Î ÀÌ·ç¾îÁø Á¾¼ºÄÚµå ÃßÃâ
//    if(tempFinalCode != -1){
//     finalCode = tempFinalCode;
//     // ±× ´ÙÀ½ÀÇ Áß¼º ¹®ÀÚ¿¡ ´ëÇÑ ÄÚµå¸¦ ÃßÃâÇÑ´Ù.
//     tempMedialCode = getSingleMedial(i+2, eng);
//     if( tempMedialCode != -1 ){      // ÄÚµå °ªÀÌ ÀÖÀ» °æ¿ì
//      finalCode = getSingleFinal(i, eng);   // Á¾¼º ÄÚµå °ªÀ» ÀúÀåÇÑ´Ù.
//              }else{
//               i++;
//              }
//    }else{            // ÄÚµå °ªÀÌ ¾øÀ» °æ¿ì ,
//     tempMedialCode = getSingleMedial(i+1, eng);  // ±× ´ÙÀ½ÀÇ Áß¼º ¹®ÀÚ¿¡ ´ëÇÑ ÄÚµå ÃßÃâ.
//     if(tempMedialCode != -1){      // ±× ´ÙÀ½¿¡ Áß¼º ¹®ÀÚ°¡ Á¸ÀçÇÒ °æ¿ì,
//      finalCode = 0;        // Á¾¼º ¹®ÀÚ´Â ¾øÀ½.
//      i--;
//     }else{
//      finalCode = getSingleFinal(i, eng);   // Á¾¼º ¹®ÀÚ ÃßÃâ
//      if( finalCode == -1 )
//       finalCode = 0;
//     }
//    }
//    // ÃßÃâÇÑ ÃÊ¼º ¹®ÀÚ ÄÚµå, Áß¼º ¹®ÀÚ ÄÚµå, Á¾¼º ¹®ÀÚ ÄÚµå¸¦ ÇÕÇÑ ÈÄ º¯È¯ÇÏ¿© ½ºÆ®¸µ¹öÆÛ¿¡ ³Ñ±è
//    sb.append((char)(0xAC00 + initialCode + medialCode + finalCode));
//   }
//   return sb.toString();
//  }
//
//  /**
//    * ÇØ´ç ¹®ÀÚ¿¡ µû¸¥ ÄÚµå¸¦ ÃßÃâÇÑ´Ù.
//    * @param type ÃÊ¼º : chosung, Áß¼º : jungsung, Á¾¼º : jongsung ±¸ºÐ
//    * @param char ÇØ´ç ¹®ÀÚ
//    */
//  private int getCode(CodeType type, String c){
//   // ÃÊ¼º
//   String init = "rRseEfaqQtTdwWczxvg";
//   // Áß¼º
//   String[] mid = {"k","o","i","O","j","p","u","P","h","hk", "ho","hl","y","n","nj","np", "nl", "b", "m", "ml", "l"};
//   // Á¾¼º
//   String[] fin = {"r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};
//
//   switch(type){
//    case chosung :
//     int index = init.indexOf(c);
//     if( index != -1 ){
//      return index * 21 * 28;
//     }
//     break;
//    case jungsung :
//
//     for(int i = 0; i < mid.length; i++){
//      if(mid[i].equals(c)){
//        return i * 28;
//       }
//     }
//     break;
//    case jongsung :
//     for(int i = 0; i < fin.length; i++){
//      if(fin[i].equals(c)){
//        return i + 1;
//       }
//     }
//     break;
//    default:
//     System.out.println("Àß¸øµÈ Å¸ÀÔ ÀÔ´Ï´Ù");
//   }
//
//   return -1;
//  }
//
//  // ÇÑ ÀÚ·Î µÈ Áß¼º°ªÀ» ¸®ÅÏÇÑ´Ù
//  // ÀÎµ¦½º¸¦ ¹þ¾î³´´Ù¸é -1À» ¸®ÅÏ
//  private int getSingleMedial(int i, String eng){
//   if((i+1) <= eng.length()){
//    return getCode(CodeType.jungsung, eng.substring(i, i+1));
//   }else{
//    return -1;
//   }
//  }
//
//  // µÎ ÀÚ·Î µÈ Áß¼ºÀ» Ã¼Å©ÇÏ°í, ÀÖ´Ù¸é °ªÀ» ¸®ÅÏÇÑ´Ù.
//  // ¾øÀ¸¸é ¸®ÅÏ°ªÀº -1
//  private int getDoubleMedial(int i, String eng){
//   int result;
//   if((i+2) > eng.length()){
//    return -1;
//   }else{
//    result = getCode(CodeType.jungsung, eng.substring(i, i+2));
//    if(result != -1){
//     return result;
//    }else{
//     return -1;
//    }
//   }
//  }
//
//  // ÇÑ ÀÚ·ÎµÈ Á¾¼º°ªÀ» ¸®ÅÏÇÑ´Ù
//  // ÀÎµ¦½º¸¦ ¹þ¾î³´´Ù¸é -1À» ¸®ÅÏ
//  private int getSingleFinal(int i, String eng){
//   if((i+1) <= eng.length()){
//    return getCode(CodeType.jongsung, eng.substring(i, i+1));
//   }else{
//    return -1;
//   }
//  }
//
//  // µÎ ÀÚ·ÎµÈ Á¾¼ºÀ» Ã¼Å©ÇÏ°í, ÀÖ´Ù¸é °ªÀ» ¸®ÅÏÇÑ´Ù.
//  // ¾øÀ¸¸é ¸®ÅÏ°ªÀº -1
//  private int getDoubleFinal(int i, String eng){
//   if((i+2) > eng.length()){
//    return -1;
//   }else{
//    return getCode(CodeType.jongsung, eng.substring(i, i+2));
//   }
//  }
// }
}