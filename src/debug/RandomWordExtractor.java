package debug;

import util.FileUtil;
import util.KeystrokeConvertor;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by leejong on 2017-04-12.
 */
public class RandomWordExtractor {
    
    public static final int samplingSize1 = 10000;
    public static final int samplingSize2 = 3000;
    
    public static void randomSample() {
        try {
            List<String> list1 = FileUtil.readFile("src/dic/speller.kor.utf8");
            //List<String> list2 = FileUtil.readFile("src/dic/noun");
            
            ArrayList<String> sampledList1 = new ArrayList<String>();
            //ArrayList<String> sampledList2 = new ArrayList<String>();
            
            
            int size1 = list1.size();
            //int size2 = list2.size();
            
            HashSet<Integer> set1 = new HashSet<Integer>();
            while (set1.size() < samplingSize1) {
                int rand = (int) (Math.random() * size1);
                if (set1.contains(rand)) continue;
                
                String line = list1.get(rand);
                String word = line.split(" ")[0];
                if(word.length() <= 3)
                    continue;
                sampledList1.add(KeystrokeConvertor.convert(word));
                set1.add(rand);
            }
//            HashSet<Integer> set2 = new HashSet<Integer>();
//            while (set2.size() < samplingSize2) {
//                int rand = (int) (Math.random() * size2);
//                if (set2.contains(rand))
//                    continue;
//                String line = list2.get(rand);
//                sampledList2.add(line.split("\t\t")[0]);
//                set2.add(rand);
//            }
//
//            for (String str : sampledList2) {
//                sampledList1.add(str);
//            }
            
            FileUtil.writeFile(sampledList1, "src/dic/random_sampled_10000_all");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static ArrayList<ArrayList<Integer>> getVCList(String str) {
        int len = str.length();
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vList = new ArrayList<Integer>();
        ArrayList<Integer> cList = new ArrayList<Integer>();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (KeystrokeConvertor.isVowel(ch))
                vList.add(i);
            else
                cList.add(i);
        }
        result.add(vList);
        result.add(cList);
        return result;
    }
    
    private static int randIdx(ArrayList<Integer> list) {
        int size = list.size();
        int rand = (int) (Math.random() * size);
        
        return list.get(rand);
    }
    
    private static int randIdx(String str) {
        int size = str.length();
        int rand = (int) (Math.random() * size);
        return rand;
    }
    
    private static int randIdx(int i) {
        int rand = (int) (Math.random() * i);
        return rand;
    }
    
    public static void makeOta() {
        try {
            List<String> lines = FileUtil.readFile("src/dic/random_sampled_10000_all");
            ArrayList<String> oV = new ArrayList<String>();
            ArrayList<String> oC = new ArrayList<String>();
            ArrayList<String> tVI = new ArrayList<String>();
            ArrayList<String> tVD = new ArrayList<String>();
            ArrayList<String> tCI = new ArrayList<String>();
            ArrayList<String> tCD = new ArrayList<String>();
            for (String line : lines) {
                System.out.println(line);
                ArrayList<ArrayList<Integer>> vcList = getVCList(line);
                int randIdx = 0;
                int len = line.length();
                int iter;
                char c;
                StringBuffer buffer;
                
                buffer = new StringBuffer(line);
                iter = randIdx(2) + 1;
                for (int i = 0; i < iter; i++) {
                    randIdx = randIdx(vcList.get(0));
                    c = KeystrokeConvertor.randVowel(line.charAt(randIdx));
                    buffer.setCharAt(randIdx, c);
                }
                String ovstr = line + "\t" + buffer.toString();
                
                buffer = new StringBuffer(line);
                iter = randIdx(2) + 1;
                for (int i = 0; i < iter; i++) {
                    randIdx = randIdx(vcList.get(1));
                    c = KeystrokeConvertor.randConsonant(line.charAt(randIdx));
                    buffer.setCharAt(randIdx, c);
                }
                String ocstr = line + "\t" + buffer.toString();
                
                buffer = new StringBuffer(line);
                iter = randIdx(2) + 1;
                for (int i = 0; i < iter; i++) {
                    c = KeystrokeConvertor.randVowel();
                    randIdx = randIdx(line);
                    buffer.insert(randIdx, c);
                }
                String tVIstr = line + "\t" + buffer.toString();
                
                buffer = new StringBuffer(line);
                iter = randIdx(2) + 1;
                while(line.length() - iter < 2){
                    iter = randIdx(2) + 1;
                }
                for(int i = 0; i < iter; i++) {
                    randIdx = randIdx(vcList.get(0));
                    try {
                        buffer.deleteCharAt(randIdx - iter);
                    }catch(Exception e){
                        break;
                    }
                }
                String tVDstr = line + "\t" + buffer.toString();
                
                buffer = new StringBuffer(line);
                iter = randIdx(2) + 1;
                for (int i = 0; i < iter; i++) {
                    c = KeystrokeConvertor.randConsonant();
                    randIdx = randIdx(line);
                    buffer.insert(randIdx, c);
                }
                String tCIstr = line + "\t" + buffer.toString();
                
                buffer = new StringBuffer(line);
                iter = randIdx(2) + 1;
                while(line.length() - iter < 2) {
                    iter = randIdx(2) + 1;
                }
                for(int i = 0; i < iter; i++) {
                    randIdx = randIdx(vcList.get(1));
                    try {
                        buffer.deleteCharAt(randIdx - iter);
                    }catch (Exception e){
                        break;
                    }
                }
                String tCDstr = line + "\t" + buffer.toString();
                
                
                oV.add(ovstr);
                oC.add(ocstr);
                tVI.add(tVIstr);
                tVD.add(tVDstr);
                tCI.add(tCIstr);
                tCD.add(tCDstr);
                
            }
            
            FileUtil.writeFile(oV, "src/dic/result_oV.txt");
            FileUtil.writeFile(oC, "src/dic/result_oC.txt");
            FileUtil.writeFile(tVI, "src/dic/result_tVI.txt");
            FileUtil.writeFile(tVD, "src/dic/result_tVD.txt");
            FileUtil.writeFile(tCI, "src/dic/result_tCI.txt");
            FileUtil.writeFile(tCD, "src/dic/result_tCD.txt");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) {
        //randomSample();
        makeOta();
    }
}
