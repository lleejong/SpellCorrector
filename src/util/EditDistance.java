package util;

public class EditDistance {

	// 공간복잡도 N으로 끝낸 소스
	// https://commons.apache.org/sandbox/commons-text/jacoco/org.apache.commons.text.similarity/LevenshteinDistance.java.html)
	public static int editDistance(String str1, String str2) {

		int len1 = str1.length();
		int len2 = str2.length();

		if (len1 == 0)
			return len2;
		else if (len2 == 0)
			return len1;

		if (len1 > len2) {
			final String tmp = str1;
			str1 = str2;
			str2 = tmp;
			len1 = len2;
			len2 = str2.length();
		}

		int[] dp = new int[len1 + 1];

		for (int i = 0; i <= len1; i++)
			dp[i] = i;

		for (int j = 1; j <= len2; j++) {
			int upperLeft = dp[0];
			char ch = str2.charAt(j - 1);

			dp[0] = j;

			for (int i = 1; i <= len1; i++) {
				int upper = dp[i];
				int cost = str1.charAt(i - 1) == ch ? 0 : 1;

				int insert = dp[i - 1] + 1;
				int delete = dp[i] + 1;
				int replace = upperLeft + cost;

				dp[i] = Math.min(Math.min(insert, delete), replace);
				upperLeft = upper;
			}
		}
		return dp[len1];
	}

	public static double weightedEditDistance(String str1, String str2, double weight1, double weight2) {

		int len1 = str1.length();
		int len2 = str2.length();

		if (len1 == 0)
			return len2;
		else if (len2 == 0)
			return len1;

		if (len1 > len2) {
			final String tmp = str1;
			str1 = str2;
			str2 = tmp;
			len1 = len2;
			len2 = str2.length();
		}

		double[] dp = new double[len1 + 1];

		for (int i = 0; i <= len1; i++)
			dp[i] = i;

		for (int j = 1; j <= len2; j++) {
			double upperLeft = dp[0];
			char ch = str2.charAt(j - 1);

			dp[0] = j;

			for (int i = 1; i <= len1; i++) {
				double upper = dp[i];
				int cost = str1.charAt(i - 1) == ch ? 0 : 1;

				double insert = dp[i - 1] + (1 + weight1);
				double delete = dp[i] + (1 + weight1);
				double replace = upperLeft + cost;

				dp[i] = Math.min(Math.min(insert, delete), replace);
				if (cost == 1) {
					if (!KeystrokeConvertor.isVowel(ch))
						dp[i] += weight2;
				}
				upperLeft = upper;
			}
		}
		return dp[len1];
	}

	public static int weightedEditDistance(String str1, String str2, int weight1, int weight2) {

		int len1 = str1.length();
		int len2 = str2.length();

		if (len1 == 0)
			return len2;
		else if (len2 == 0)
			return len1;

		if (len1 > len2) {
			final String tmp = str1;
			str1 = str2;
			str2 = tmp;
			len1 = len2;
			len2 = str2.length();
		}

		int[] dp = new int[len1 + 1];

		for (int i = 0; i <= len1; i++)
			dp[i] = i;

		for (int j = 1; j <= len2; j++) {
			int upperLeft = dp[0];
			char ch = str2.charAt(j - 1);

			dp[0] = j;

			for (int i = 1; i <= len1; i++) {
				int upper = dp[i];
				int cost = str1.charAt(i - 1) == ch ? 0 : 1;

				int insert = dp[i - 1] + (1 + weight1);
				int delete = dp[i] + (1 + weight1);
				int replace = upperLeft + cost;

				dp[i] = Math.min(Math.min(insert, delete), replace);
				if (cost == 1) {
					if (!KeystrokeConvertor.isVowel(ch))
						dp[i] += weight2;
				}
				upperLeft = upper;
			}
		}
		return dp[len1];
	}

	public static void main(String args[]) {
		String str1 = "현대바둑";
		String str2 = "현대바ㅑ둑";
		String str3 = "현대뱌둑";
		System.out.println(KeystrokeConvertor.convert(str1) + "\t" + KeystrokeConvertor.convert(str2));
		System.out.println(
				"Result : " + editDistance(KeystrokeConvertor.convert(str1), KeystrokeConvertor.convert(str2)));
		System.out.println("Result : "
				+ weightedEditDistance(KeystrokeConvertor.convert(str1), KeystrokeConvertor.convert(str2), 0.5, 0.5));
		System.out.println("Result : "
				+ weightedEditDistance(KeystrokeConvertor.convert(str1), KeystrokeConvertor.convert(str3), 0.5, 0.5));
	}

}
