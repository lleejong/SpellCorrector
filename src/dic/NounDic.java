package dic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import util.EditDistance;
import util.FileUtil;
import util.KeystrokeConvertor;

public class NounDic {
	private final static NounDic instance = new NounDic();
	// private HashMap<Integer, HashMap<String, Double>> map = new
	// HashMap<Integer,HashMap<String, Double>>();
	private HashMap<Integer, HashMap<String, Noun>> map = new HashMap<Integer, HashMap<String, Noun>>();

	public static NounDic getInstance() {
		return instance;
	}

	private NounDic() {
		try {
			loadDic();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadDic() throws IOException {
		// List<String> lines = FileUtil.readFile("src/dic/noun");
		List<String> lines = FileUtil.readFile("src/dic/speller.kor.utf8");
		for (String line : lines) {
			// String[] spl = line.split("\t\t");
			String[] spl = line.split(" ");

			String keystr = KeystrokeConvertor.convert(spl[0]);
			int len = keystr.length();
			if (!map.containsKey(len)) {
				map.put(len, new HashMap<String, Noun>());
			}
			HashMap<String, Noun> targetMap = map.get(len);
			targetMap.put(spl[0], new Noun(spl[0], KeystrokeConvertor.convert(spl[0]), Integer.parseInt(spl[1])));
		}
	}

	public ArrayList<Result> getSimilarWords(String query, int maxDistance) {
		ArrayList<Result> result = new ArrayList<Result>();

		int minLen = query.length() - 2 > 0 ? query.length() : 1;
		int maxLen = query.length() + 2;

		for (int i = minLen; i <= maxLen; i++) {
			HashMap<String, Noun> targetMap = map.get(i);
			Iterator<String> iter = targetMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				Noun noun = targetMap.get(key);
				int edits = EditDistance.editDistance(query, noun.keystr);
				if (edits <= maxDistance) {
					result.add(new Result(noun, edits));
				}
			}
		}

		Collections.sort(result);

		return result;
	}

	public ArrayList<Result> getSimilarWords(String query, int maxDistance, double weight1, double weight2) {
		ArrayList<Result> result = new ArrayList<Result>();

		int minLen = query.length() - 2 > 0 ? query.length() - 2 : 1;
		int maxLen = query.length() + 2;

		for (int i = minLen; i <= maxLen; i++) {
			HashMap<String, Noun> targetMap = map.get(i);
			Iterator<String> iter = targetMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				Noun noun = targetMap.get(key);
				double edits = EditDistance.weightedEditDistance(query, noun.keystr, weight1, weight2);
				if (edits <= maxDistance) {
					//result.add(new Result(noun, edits));
				}
			}
		}

		Collections.sort(result);

		return result;
	}

	public ArrayList<Result> getSimilarWords(String query, int maxDistance, int weight1, int weight2) {
		ArrayList<Result> result = new ArrayList<Result>();

		int minLen = query.length() - 2 > 0 ? query.length() - 2 : 1;
		int maxLen = query.length() + 2;

		for (int i = minLen; i <= maxLen; i++) {
			HashMap<String, Noun> targetMap = map.get(i);
			Iterator<String> iter = targetMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				Noun noun = targetMap.get(key);
				int edits = EditDistance.weightedEditDistance(query, noun.keystr, weight1, weight2);
				if (edits <= maxDistance) {
					result.add(new Result(noun, edits));
				}
			}
		}

		Collections.sort(result);

		return result;
	}

	public class Noun implements Comparable<Noun> {
		private String word;
		private String keystr;
		private int weight;

		private Noun(String word, String keystr, int weight) {
			this.word = word;
			this.keystr = keystr;
			this.weight = weight;
		}

		public String getWord() {
			return word;
		}

		public String getKeystr() {
			return keystr;
		}

		public int getWeight() {
			return weight;
		}

		@Override
		public int compareTo(Noun other) {
			return other.weight - this.weight;
		}
	}

	public class Result implements Comparable<Result> {
		public Noun noun;
		public int editDistance;

		public Result(Noun noun, int editDistance) {
			this.noun = noun;
			this.editDistance = editDistance;
		}

		@Override
		public int compareTo(Result o) {
			if (this.editDistance == o.editDistance) {
				//ed가 같으면 단어의 가중치가 높은 순
				return o.noun.getWeight() - this.noun.getWeight();

			} else {
				//ed가 다르다면, ed가 낮은 순
				return this.editDistance - o.editDistance;
			}
		}

	}
}
