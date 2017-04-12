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
	
}