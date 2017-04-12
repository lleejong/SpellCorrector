import dic.NounDic;
import util.EditDistance;
import util.KeystrokeConvertor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SpellCorrector {
	private final static SpellCorrector instance = new SpellCorrector();
	private final static int MAX_DISTANCE = 3;
	private final static int OFFSET = 2;
	
	private final static double rw = 0.5;
	private final static double iw = 0.3;
	private final static double dw = 0.2;
	
	private final static double vw = 0.7;
	private final static double cw = 0.3;
	
	
	
	private NounDic dic;

	public static SpellCorrector getInstance() {
		return instance;
	}

	private SpellCorrector() {
		// load dictionary
		dic = NounDic.getInstance();
		EditDistance.cW = cw;
		EditDistance.vW = vw;
		EditDistance.iW = iw;
		EditDistance.dW = dw;
		EditDistance.rW = rw;
	}

	public String correct(String query, int maxDistance, int offset) {
		
		ArrayList<NounDic.Result> result = dic.getSimilarWords(KeystrokeConvertor.convert(query), maxDistance, offset);
		
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).getNoun().getWord() + "\t" + result.get(i).getNoun().getKeystr() + "\t"+ result.get(i).getEditDistance() + "\t" + result.get(i).getWeight());
		}
		
		if(result.size() == 0)
			return null;
		
		return result.get(0).getNoun().getWord();
	}

	public static void main(String args[]) {
		SpellCorrector corrector = SpellCorrector.getInstance();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Input>");
			String query = scanner.nextLine();
			System.out.println(KeystrokeConvertor.convert(query));
			long startTime = System.currentTimeMillis();
			corrector.correct(query, MAX_DISTANCE, OFFSET);
			long endTime = System.currentTimeMillis();
			System.out.println("\t\t\tElapsed Time : " + (endTime - startTime)/1000.0 + " sec.");
		}
	}
}
