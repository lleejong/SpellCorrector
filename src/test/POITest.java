package test;

import kma.KMAWrapper;
import kma.KMAWrapper.KMAResult;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class POITest {
    //    public static void poitest() {
//        try {
//            KMAWrapper kma = KMAWrapper.getInstance();
//            List<String> lines = FileUtil.readFile("src/test/poi-modified.txt");
//
//            HashSet<String> filter = new HashSet<String>();
//            filter.add("NNR");
//
//            System.out.println("TOTAL : " + lines.size());
//            List<String> output = new ArrayList<String>();
//            int cnt = 0;
//            for (String line : lines) {
//                cnt++;
//                if (cnt % 100000 == 0) {
//                    System.out.println(cnt);
//                }
//                int spacecnt = 0;
//                for (int i = 0; i < line.length(); i++) {
//                    if (line.charAt(i) == ' ')
//                        spacecnt++;
//                }
//                String trimmedLine = line.replaceAll(" ", "");
//                KMAResult result = kma.getResultWithFilter(trimmedLine, filter);
//                if (result == null) {
//                    continue;
//                } else if (spacecnt <= (result.morph.size() - 1)) {
//                    continue;
//                } else {
//                    int size  = result.morph.size();
//
//                    for(int i = 0; i < size; i++){
//                        String morph = result.morph.get(i);
//                        String tag = result.tag.get(i);
//
//                        if(filter.contains(tag)){
//                            String pattern = findPattern(line, morph);
//                            if(pattern == null){
//                                System.out.println(line);
//                                System.out.println(morph);
//                                System.out.println("NULL");
//                                System.exit(0);
//                            }
//                            String[] tmp = pattern.split(" ");
//                            StringBuffer bf = new StringBuffer(morph);
//                            bf.append("\t");
//                            for(String str : tmp) {
//                                bf.append(str.length());
//                            }
//                            output.add(bf.toString());
//                        }
//                    }
//                }
//
//            }
//
//            FileUtil.writeFile(output, "src/test/poi-nnr-spacing-bigram.txt");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static String findPattern(String org, String query) {
        int lenA = org.length();
        int lenB = query.length();
        
        int i;
        int j;
        int idxStart = 0;
        int idxEnd = 0;
        boolean flag = false;
        for (i = 0; i < lenA; i++) {
            if (org.charAt(i) == ' ')
                continue;
            idxStart = i;
            for (j = 0; j < lenB; j++) {
                if (org.charAt(i) == ' ') {
                    i++;
                }
                if (org.charAt(i) != query.charAt(j)) {
                    break;
                } else
                    i++;
            }
            
            if (j == lenB) {
                flag = true;
                idxEnd = i;
                break;
            }
            i = idxStart;
        }
        if (flag)
            return org.substring(idxStart, idxEnd);
        
        return null;
    }
    
    private static String toSpacingTag(String input, String trimmedLine) {
        String[] spl = input.split(" ");
        StringBuffer bf = new StringBuffer(trimmedLine);
        bf.append("\t");
        if (spl.length == 1) {
            bf.append(0);
            return bf.toString();
        }
        for (String str : spl) {
            int len = str.length();
            if (len == 0)
                continue;
            if (len < 10)
                bf.append(len);
            else
                bf.append((char) (len - 10 + 'a'));
        }
        return bf.toString();
    }
    
    public static void makeUnigramDic() {
        try {
            KMAWrapper kma = KMAWrapper.getInstance();
            List<String> poilist = FileUtil.readFile("src/test/poi-modified.txt");
            List<String> output = new ArrayList<String>();
            
            HashSet<String> filter = new HashSet<String>();
            filter.add("NNR");

//            HashSet<String> filter2 = new HashSet<String>();
//            String[] targetTag = {"NNG", "NNP", "NFU", "NFG", "NNR"};
//            filter2.addAll(Arrays.asList(targetTag));
            
            HashSet<String> uniqueWord = new HashSet<String>();
            
            for (String line : poilist) {
                String trimmedLine = line.replaceAll(" ", "");
                KMAResult result = kma.getResultWithFilter(trimmedLine, filter);
                if (result == null)
                    continue;
                else {
//                    if (result.tag.size() == 1) {
//                        if (trimmedLine.length() != 1 && !uniqueWord.contains(trimmedLine)) {
//                            output.add(toSpacingTag(line, trimmedLine));
//                            uniqueWord.add(trimmedLine);
//                        }
//                    } else {
                    int size = result.tag.size();
                    for (int i = 0; i < size; i++) {
                        String morph = result.morph.get(i);
                        String tag = result.tag.get(i);
                        
                        if (filter.contains(tag) && morph.length() != 1) {
                            String query = findPattern(line, morph);
                            if (query.length() != morph.length()) {
                                String spl[] = query.split(" ");
                                for (String str : spl) {
                                    KMAResult result2 = kma.getResultWithFilter(str, filter);
                                    if (result2 == null) {
                                    } else {
                                        int size2 = result2.morph.size();
                                        for (int j = 0; j < size2; j++) {
                                            String morph2 = result2.morph.get(j);
                                            String tag2 = result2.tag.get(j);
                                            if (filter.contains(tag2)) {
                                                if (morph2.length() != 1 && !uniqueWord.contains(morph2)) {
                                                    output.add(morph2 + "\t" + 0);
                                                    uniqueWord.add(morph2);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (!uniqueWord.contains(morph)) {
                                    output.add(morph + "\t" + 0);
                                    uniqueWord.add(morph);
                                }
                            }
                        }
                    }
                }
//                }
                
            }
            
            FileUtil.writeFile(output, "src/test/poi-nnr-spacing-unigram-update2.txt", "cp949");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void poitest() {
        try {
            KMAWrapper kma = KMAWrapper.getInstance();
            HashSet<String> filter = new HashSet<String>();
            filter.add("NNR");
            List<String> lines = FileUtil.readFile("src/test/poi-modified-bigram.txt");
            List<String> output = new ArrayList<String>();
            HashSet<String> set = new HashSet<String>();
            for (String line : lines) {
                String[] tmp = line.split("\t");
                String query = tmp[0];
                String spacing = tmp[1];
                String frqStr = tmp[2];
                
                KMAResult result = kma.getResultWithFilter(query, filter);
                if (result == null) {
                    continue;
                } else {
                    int nnrCnt = 0;
                    for (String tag : result.tag) {
                        if (tag.equals("NNR"))
                            nnrCnt++;
                    }
                    
                    System.out.println(result.result + "\t" + spacing);

//                    int size = result.morph.size();
//                    for(int i = 0; i < size; i ++){
//                        String morph = result.morph.get(i);
//                        String tag = result.tag.get(i);
//
//                    }
                    
                }
            }
            //FileUtil.writeFile(output, "src/test/poi-nnr-spacing-bigram.txt", "cp949");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void poiTestNNRFreq() {
        try {
            long dicstart = System.currentTimeMillis();
            KMAWrapper kma = KMAWrapper.getInstance();
            long dicend = System.currentTimeMillis();
            HashSet<String> filter = new HashSet<String>();
            filter.add("NNR");
            List<String> lines = FileUtil.readFile("src/test/poi-all.txt");
            List<String> output = new ArrayList<String>();
            List<String> output2 = new ArrayList<String>();
            HashSet<String> uniqueMorph = new HashSet<String>();
            int nnrcnt = 0;
            int nullcnt = 0;
            long time = 0;
            for (String line : lines) {
                long start = System.currentTimeMillis();
                KMAResult result = kma.getResultWithFilter(line, filter);
                if (result == null) {
                    nullcnt++;
                } else {
                    int size = result.morph.size();
                    for (int i = 0; i < size; i++) {
                        String tag = result.tag.get(i);
                        String morph = result.morph.get(i);
                        
                        if (filter.contains(tag)) {
                            if (!uniqueMorph.contains(morph)) {
                                nnrcnt++;
                                output2.add(morph);
                                uniqueMorph.add(morph);
                            }
                        }
                    }
                }
                long end = System.currentTimeMillis();
                time += (end - start);
            }
            System.out.println("Total : " + lines.size());
            System.out.println("NullCnt : " + nullcnt);
            System.out.println("NNR : " + nnrcnt);
            output.add("NullCnt : " + nullcnt);
            output.add("NNR : " + nnrcnt);
            output.add("Total Elapsed Time : " + ((dicend - dicstart) / 1000.0 + (double) time / 1000.0) + " secs.");
            output.add("---Dic loading time : " + (dicend - dicstart) / 1000.0 + " secs.");
            FileUtil.writeFile(output, "src/test/poi-all-nnr-withUnigramDic-update-summary.txt");
            FileUtil.writeFile(output2, "src/test/poi-all-nnr-withUnigramDic-update-list.txt");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) {
        poiTestNNRFreq();
        //poitest();
        //makeUnigramDic();
        //System.out.println(findPattern("강화 화도점", "화도점"));
    }
}
    
