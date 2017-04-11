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

			if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00: �� ~ 0xD7A3: �R"
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

			if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00: �� ~ 0xD7A3: �R"
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
		String jaso = emjeolToJasoWithDivider("�ȳ��ϼ���");
		System.out.println(jaso);
		System.out.println(jasoToEmjeolWithDivider(jaso));
	}

	// public static String convertToWord(String str){
	//
	// }
	// public class TempTest {
//  // �ڵ�Ÿ�� - �ʼ�, �߼�, ����
//  enum CodeType{chosung, jungsung, jongsung}
//
//  public static void main(String[] args) throws UnsupportedEncodingException {
//   TempTest ts1 = new TempTest();
//   String result = ts1.engToKor("zjavbxjtkTek");
//   System.out.println(result);
//  }
//
//  /**
//    * ��� �ѱ۷�...
//    */
//  public String engToKor(String eng){
//   StringBuffer sb = new StringBuffer();
//   int initialCode = 0, medialCode = 0, finalCode = 0;
//   int tempMedialCode, tempFinalCode;
//
//   for(int i = 0; i < eng.length(); i++){
//    // �ʼ��ڵ� ����
//    initialCode = getCode(CodeType.chosung, eng.substring(i, i+1));
//    i++; // �������ڷ�
//
//    // �߼��ڵ� ����
//    tempMedialCode = getDoubleMedial(i, eng);   // �� �ڷ� �̷���� �߼��ڵ� ����
//
//    if(tempMedialCode != -1){
//     medialCode = tempMedialCode;
//     i += 2;
//    }else{            // ���ٸ�,
//     medialCode = getSingleMedial(i, eng);   // �� �ڷ� �̷���� �߼��ڵ� ����
//     i++;
//    }
//
//    // �����ڵ� ����
//    tempFinalCode = getDoubleFinal(i, eng);    // �� �ڷ� �̷���� �����ڵ� ����
//    if(tempFinalCode != -1){
//     finalCode = tempFinalCode;
//     // �� ������ �߼� ���ڿ� ���� �ڵ带 �����Ѵ�.
//     tempMedialCode = getSingleMedial(i+2, eng);
//     if( tempMedialCode != -1 ){      // �ڵ� ���� ���� ���
//      finalCode = getSingleFinal(i, eng);   // ���� �ڵ� ���� �����Ѵ�.
//              }else{
//               i++;
//              }
//    }else{            // �ڵ� ���� ���� ��� ,
//     tempMedialCode = getSingleMedial(i+1, eng);  // �� ������ �߼� ���ڿ� ���� �ڵ� ����.
//     if(tempMedialCode != -1){      // �� ������ �߼� ���ڰ� ������ ���,
//      finalCode = 0;        // ���� ���ڴ� ����.
//      i--;
//     }else{
//      finalCode = getSingleFinal(i, eng);   // ���� ���� ����
//      if( finalCode == -1 )
//       finalCode = 0;
//     }
//    }
//    // ������ �ʼ� ���� �ڵ�, �߼� ���� �ڵ�, ���� ���� �ڵ带 ���� �� ��ȯ�Ͽ� ��Ʈ�����ۿ� �ѱ�
//    sb.append((char)(0xAC00 + initialCode + medialCode + finalCode));
//   }
//   return sb.toString();
//  }
//
//  /**
//    * �ش� ���ڿ� ���� �ڵ带 �����Ѵ�.
//    * @param type �ʼ� : chosung, �߼� : jungsung, ���� : jongsung ����
//    * @param char �ش� ����
//    */
//  private int getCode(CodeType type, String c){
//   // �ʼ�
//   String init = "rRseEfaqQtTdwWczxvg";
//   // �߼�
//   String[] mid = {"k","o","i","O","j","p","u","P","h","hk", "ho","hl","y","n","nj","np", "nl", "b", "m", "ml", "l"};
//   // ����
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
//     System.out.println("�߸��� Ÿ�� �Դϴ�");
//   }
//
//   return -1;
//  }
//
//  // �� �ڷ� �� �߼����� �����Ѵ�
//  // �ε����� ����ٸ� -1�� ����
//  private int getSingleMedial(int i, String eng){
//   if((i+1) <= eng.length()){
//    return getCode(CodeType.jungsung, eng.substring(i, i+1));
//   }else{
//    return -1;
//   }
//  }
//
//  // �� �ڷ� �� �߼��� üũ�ϰ�, �ִٸ� ���� �����Ѵ�.
//  // ������ ���ϰ��� -1
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
//  // �� �ڷε� �������� �����Ѵ�
//  // �ε����� ����ٸ� -1�� ����
//  private int getSingleFinal(int i, String eng){
//   if((i+1) <= eng.length()){
//    return getCode(CodeType.jongsung, eng.substring(i, i+1));
//   }else{
//    return -1;
//   }
//  }
//
//  // �� �ڷε� ������ üũ�ϰ�, �ִٸ� ���� �����Ѵ�.
//  // ������ ���ϰ��� -1
//  private int getDoubleFinal(int i, String eng){
//   if((i+2) > eng.length()){
//    return -1;
//   }else{
//    return getCode(CodeType.jongsung, eng.substring(i, i+2));
//   }
//  }
// }
}