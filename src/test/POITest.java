package test;

import kma.KMAWrapper;
import kma.KMAWrapper.KMAResult;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
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
//            FileUtil.writeFile(output, "src/test/poi-nnr-spacing.txt");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static String findPattern(String org, String query){
//        int lenA = org.length();
//        int lenB = query.length();
//
//        int i;
//        int j;
//        int idxStart = 0;
//        int idxEnd = 0;
//        boolean flag = false;
//        for (i = 0; i < lenA; i++) {
//            if (org.charAt(i) == ' ')
//                continue;
//            idxStart = i;
//            for (j = 0; j < lenB; j++) {
//                if (org.charAt(i) == ' ') {
//                    i++;
//                }
//                if (org.charAt(i) != query.charAt(j))
//                    break;
//                else
//                    i++;
//            }
//            if (j == lenB) {
//                flag = true;
//                idxEnd = i;
//                break;
//            }
//        }
//        if (flag)
//            return org.substring(idxStart, idxEnd);
//
//        return null;
//    }
    public static void poitest() {
        try {
            KMAWrapper kma = KMAWrapper.getInstance();
            HashSet<String> filter = new HashSet<String>();
            filter.add("NNR");
            List<String> lines = FileUtil.readFile("src/test/poi-modified-bigram.txt");
            List<String> output = new ArrayList<String>();
            for (String line : lines) {
                String[] tmp = line.split("\t");
                String query = tmp[0];
                String spacing = tmp[1];
                String frqStr = tmp[2];
                
                KMAResult result = kma.getResultWithFilter(query, filter);
                if (result == null) {
                    continue;
                } else {
                    if(spacing.equals("00"))
                        spacing="0";
                    output.add(query + "\t" + spacing);
                }
            }
            FileUtil.writeFile(output, "src/test/poi-nnr-spacing.txt", "cp949");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void poiTestNNRFreq(){
        try {
            KMAWrapper kma = KMAWrapper.getInstance();
            HashSet<String> filter = new HashSet<String>();
            filter.add("NNR");
            List<String> lines = FileUtil.readFile("src/test/poi-all.txt");
            List<String> output = new ArrayList<String>();
            List<String> output2 = new ArrayList<String>();
            HashSet<String> uniqueMorph = new HashSet<String>();
            int nnrcnt = 0;
            int nullcnt = 0;
            for (String line : lines) {
                KMAResult result = kma.getResultWithFilter(line, filter);
                if (result == null) {
                    nullcnt++;
                    continue;
                } else {
                    int size = result.morph.size();
                    for(int i = 0; i < size; i++){
                        String tag = result.tag.get(i);
                        String morph = result.morph.get(i);
                        
                        if(filter.contains(tag)) {
                            if(!uniqueMorph.contains(morph)){
                                nnrcnt++;
                                output2.add(morph);
                                uniqueMorph.add(morph);
                            }
                        }
                    }
                }
            }
            System.out.println("Total : " + lines.size());
            System.out.println("NullCnt : " + nullcnt);
            System.out.println("NNR : " + nnrcnt);
            output.add("NullCnt : " + nullcnt);
            output.add("NNR : " + nnrcnt);
            FileUtil.writeFile(output, "src/test/poi-all-nnr-withDic-summary.txt");
            FileUtil.writeFile(output2, "src/test/poi-all-nnr-withDic-list.txt");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) {
        poiTestNNRFreq();
        //poitest();
    }
}
    
