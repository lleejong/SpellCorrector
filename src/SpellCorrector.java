import java.util.ArrayList;
import java.util.Scanner;

import dic.NounDic;
import dic.NounDic.Result;
import util.KeystrokeConvertor;

public class SpellCorrector {
	private final static SpellCorrector instance = new SpellCorrector();
	private final static int MAX_DISTANCE = 3;
	private NounDic dic;

	public static SpellCorrector getInstance() {
		return instance;
	}

	private SpellCorrector() {
		// load dictionary
		dic = NounDic.getInstance();
	}

	public void correct(String query, int maxDistance) {
		ArrayList<Result> result = dic.getSimilarWords(KeystrokeConvertor.convert(query), maxDistance, 1, 1);
		//ArrayList<Result> result = dic.getSimilarWords(KeystrokeConvertor.convert(query), maxDistance, 0.2, 0.2);
		//ArrayList<EditDistanceResult> result = dic.getResult(KeystrokeConvertor.convert(query), maxDistance);
			
		
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).noun.getWord() + "\t"+ result.get(i).noun.getKeystr() +"\t" + result.get(i).editDistance);
		}
	}

	public static void main(String args[]) {
		SpellCorrector corrector = SpellCorrector.getInstance();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Input>");
			String query = scanner.nextLine();
			System.out.println(KeystrokeConvertor.convert(query));
			long startTime = System.currentTimeMillis();
			corrector.correct(query, MAX_DISTANCE);
			long endTime = System.currentTimeMillis();
			System.out.println("\t\t\tElapsed Time : " + (endTime - startTime)/1000.0 + " sec.");
		}
	}
}
