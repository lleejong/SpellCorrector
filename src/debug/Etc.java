package debug;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kma.KMAWrapper;
import util.FileUtil;
import util.KeystrokeConvertor;

public class Etc {

	public static ArrayList<Word> sortByWord(HashMap<String, Word> map) {
		ArrayList<Word> list = new ArrayList<Word>();
		list.addAll(map.values());
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}

	public static void writeToFile(ArrayList<Word> list, String filename) throws IOException {
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("C:\\Users\\leejong\\Desktop\\" + filename), "UTF8"));
		for (Word word : list) {
			bw.write(word.toString());
			bw.write("\n");
		}
		bw.close();
	}

	public static void extractFromCorpus(KMAWrapper kma) {
		try {
			HashMap<String, Word> map = new HashMap<String, Word>();

			List<String> corpusList = FileUtil
					.getCorpusListFromFile("C:\\Users\\leejong\\Documents\\3. corpus\\raw_corpus_all.txt");
			int linenum = 0;
			for (String sentence : corpusList) {
				List<String> nounList = kma.getNouns(sentence);
				linenum++;
				if (linenum % 10000 == 0)
					System.out.println("-----" + linenum);
				if (nounList == null) {
					continue;
				}
				for (String noun : nounList) {
					if (!map.containsKey(noun))
						map.put(noun, new Word(noun));
					else
						map.get(noun).increaseFreq();
				}
			}
			System.out.println("---end extract noun");

			ArrayList<Word> sortedList = sortByWord(map);
			writeToFile(sortedList, "result.txt");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void extractFromWordList(KMAWrapper kma) {
//		try {
//			List<Word> wordList = FileUtil.getWordListFromFile("C:\\Users\\leejong\\Desktop\\result.txt");
//			HashMap<String, Word> map = new HashMap<String, Word>();
//			for (Word word : wordList) {
//				List<String> list = kma.getNouns(word.getWord());
//				if (list == null) {
//					System.out.println(word.getWord());
//					continue;
//				} else if (list.size() < 1) {
//					
//					continue;
//					
//				} else if (list.size() == 1) {
//					if (!map.containsKey(word.getWord())) {
//						map.put(word.getWord(), word);
//
//					} else {
//						map.get(word.getWord()).sum(word.getFreq());
//						// System.out.println(word.getWord());
//					}
//				} else {
//					System.out.print(word.getWord() + " ===>");
//					for (String noun : list) {
//						System.out.print(noun + " , ");
//						if (!map.containsKey(noun)) {
//							map.put(noun, new Word(noun, word.getFreq()));
//						} else {
//							map.get(noun).sum(word.getFreq());
//						}
//					}
//					System.out.println("");
//				}
//			}
//			ArrayList<Word> sortedList = sortByWord(map);
//			System.out.println("---start to write");
//
//			writeToFile(sortedList, "result2.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static void main(String args[]) {
		long startTime = System.currentTimeMillis();
		System.out.println("Start..");
//		KMAWrapper kma = KMAWrapper.getInstance();
//		// extractFromCorpus(kma);
//		//extractFromWordList(kma);
		
		
		try {
			
			List<String> lines = FileUtil.readFile("C:\\Users\\leejong\\Desktop\\result.txt");
			List<String> kss = new ArrayList<String>();
			List<Integer> fre = new ArrayList<Integer>();
			List<String> newLines = new ArrayList<String>();
			int total = 0;
			for(String line: lines){
				String[] temp = line.split(" ");
				
				String ks = KeystrokeConvertor.convert(temp[0]);
				kss.add(ks);
				int freq = Integer.parseInt(temp[1]);
				total += freq;
				fre.add(freq);
			}
			
			for(int i = 0; i < kss.size(); i ++){
				String ks = kss.get(i);
				int freq = fre.get(i);
				
				String newLine = ks + "\t\t" + (String.format("%.8f", (double)freq/total));
				newLines.add(newLine);
			}
			FileUtil.writeFile(newLines, "src/dic/noun");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		long endTime = System.currentTimeMillis();
		System.out.println("End..");
		System.out.println("Elapsed Time : " + ((endTime - startTime) / 1000.0) + " sec.");
//		kma.finalize();

	}
}
