package debug;

import util.FileUtil;
import util.KeystrokeConvertor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by leejong on 2017-04-12.
 */
public class RandomWordExtractor {
    
    public static final int samplingSize1 = 7000;
    public static final int samplingSize2 = 3000;
    
    public static void randomSample() {
        try {
            List<String> list1 = FileUtil.readFile("src/dic/speller.kor.utf8");
            List<String> list2 = FileUtil.readFile("src/dic/noun");
            
            ArrayList<String> sampledList1 = new ArrayList<String>();
            ArrayList<String> sampledList2 = new ArrayList<String>();
            
            
            int size1 = list1.size();
            int size2 = list2.size();
            
            HashSet<Integer> set1 = new HashSet<Integer>();
            while (set1.size() < samplingSize1) {
                int rand = (int) (Math.random() * size1);
                if (set1.contains(rand))
                    continue;
                String line = list1.get(rand);
                sampledList1.add(KeystrokeConvertor.convert(line.split(" ")[0]));
                set1.add(rand);
            }
            HashSet<Integer> set2 = new HashSet<Integer>();
            while (set2.size() < samplingSize2) {
                int rand = (int) (Math.random() * size2);
                if (set2.contains(rand))
                    continue;
                String line = list2.get(rand);
                sampledList2.add(line.split("\t\t")[0]);
                set2.add(rand);
            }
            
            for (String str : sampledList2) {
                sampledList1.add(str);
            }
            
            FileUtil.writeFile(sampledList1, "src/dic/random_sampled_700_300_all");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void makeOta(){
        try {
            List<String> lines = FileUtil.readFile("src/dic/random_sampled_700_300_all");
            ArrayList<String> oV = new ArrayList<String>();
            ArrayList<String> oC = new ArrayList<String>();
            ArrayList<String> tVI = new ArrayList<String>();
            ArrayList<String> tVD = new ArrayList<String>();
            ArrayList<String> oCI = new ArrayList<String>();
            ArrayList<String> oCD = new ArrayList<String>();
            for(String line : lines){
                
                String ovstr;
                String ocstr;
                String tVIstr;
                String tVDstr;
                String oCIstr;
                String oCDstr;
                
                
                oV.add(ovstr);
                oC.add(ocstr);
                tVI.add(tVIstr);
                tVD.add(tVDstr);
                oCI.add(oCIstr);
                oCD.add(oCDstr);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) {
        //randomSample();
    }
}
