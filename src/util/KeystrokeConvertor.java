package util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class KeystrokeConvertor {
	
	private static HashMap<Character, String> hanToEng = new HashMap<Character, String>();
	private static HashSet<Character> vowelSet = new HashSet<Character>();
	private static HashSet<Character> consonantSet = new HashSet<Character>();
	static{
		loadKeymap();
	}
	
	private static void loadKeymap() {
		try {
			List<String> lines = FileUtil.readFile("src/util/keymap");

			for (String line : lines) {
				String temp[] = line.split("\t");
				// 0: hangul tag, 1: eng keystroke, 2: hangul unicode
				String eng = temp[1];
				char hanUni = (char)Integer.parseInt(temp[2],16);
				hanToEng.put(hanUni, eng);
				
				if(hanUni >= 0x314f && hanUni <= 0x3163){
					if(eng.length() == 1)
						vowelSet.add(eng.charAt(0));
				}
				else{
					if(eng.length() == 1)
						consonantSet.add(eng.charAt(0));
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String convert(String str){
		String jaso = Jaso.emjeolToJaso(str);
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < jaso.length(); i++){
			Object ch = hanToEng.get(jaso.charAt(i));
			if(ch == null)
				sb.append(jaso.charAt(i));
			else
				sb.append(ch.toString());
		}
		return sb.toString();
	}
	
	//vowel
	public static boolean isVowel(char ch){
		return vowelSet.contains(ch);
	}

	private static void test(String str) {
		String test = Jaso.emjeolToJaso(str);
		convert(test);
	}

	public static void main(String args[]) {
		loadKeymap();
		test("照括しししAAA馬室しに");
	}

	
}
