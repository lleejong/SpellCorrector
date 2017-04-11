package dic;

import java.text.Normalizer;

public class HangulTokenizer {
	private static void printIt(String string){
		System.out.println();
		for(int i = 0; i < string.length(); i ++){
			System.out.println(string.codePointAt(i));
		}
	}
	
	public static void main(String args[]){
		String han = "วั";
		printIt(han);
		
		String nfd = Normalizer.normalize(han, Normalizer.Form.NFD);
		printIt(nfd);
	}
}
