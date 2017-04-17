import dic.NounDic;
import util.EditDistance;
import util.FileUtil;
import util.KeystrokeConvertor;

import java.io.IOException;
import java.security.Key;
import java.util.*;

public class SpellCorrector {
    private final static SpellCorrector instance = new SpellCorrector();
    private final static int MAX_DISTANCE = 2;
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

//    public String correct(String query, int maxDistance, int offset) {
//
//        //ArrayList<NounDic.Result> result = dic.getSimilarWords(KeystrokeConvertor.convert(query), maxDistance, offset);
//        ArrayList<NounDic.Result> result = dic.getSimilarWords(query, maxDistance, offset);
//
//
////		for (int i = 0; i < result.size(); i++) {
////			System.out.println(result.get(i).getNoun().getWord() + "\t" + result.get(i).getNoun().getKeystr() + "\t"+ result.get(i).getEditDistance() + "\t" + result.get(i).getWeight());
////		}
//
//        if (result.size() == 0)
//            return null;
//
//        return result.get(0).getNoun().getKeystr();
//    }
    
    public NounDic.Result correct(String query, int maxDistance, int offset) {
        
        ArrayList<NounDic.Result> result = dic.getSimilarWords(KeystrokeConvertor.convert(query), maxDistance, offset);
        //ArrayList<NounDic.Result> result = dic.getSimilarWords(query, maxDistance, offset);


//		for (int i = 0; i < result.size(); i++) {
//			System.out.println(result.get(i).getNoun().getWord() + "\t" + result.get(i).getNoun().getKeystr() + "\t"+ result.get(i).getEditDistance() + "\t" + result.get(i).getWeight());
//		}
//
        if (result.size() == 0)
            return null;
        
        return result.get(0);
    }
    public void evaluateTestSet() throws IOException {
        String path = "src/dic/";
        String[] filenames = {"result_oC", "result_tCD", "result_tCI", "result_oV", "result_tVD", "result_tVI"};;
        ArrayList<String> output = new ArrayList<String>();
        HashSet<String> set = new HashSet<String>();
        
        List<String> dicLines = FileUtil.readFile("src/dic/speller.kor.utf8");
        for(String line : dicLines){
            set.add(KeystrokeConvertor.convert(line.split("\t")[0]));
        }
        
        for(int i = 0; i < filenames.length; i++){
            int cnt = 0;
            List<String> lines = FileUtil.readFile(path+filenames[i]+".txt");
            for(String line:lines){
                String[] spl =line.split("\t");
                String query = spl[1];
                if(set.contains(query)){
                    cnt++;
                }
            }
            System.out.println(cnt);
        }
    }
    
    public void doConsoleTest() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Input>");
            String query = scanner.nextLine();
            System.out.println(KeystrokeConvertor.convert(query));
            long startTime = System.currentTimeMillis();
            correct(query, MAX_DISTANCE, OFFSET);
            long endTime = System.currentTimeMillis();
            System.out.println("\t\t\tElapsed Time : " + (endTime - startTime) / 1000.0 + " sec.");
        }
    }
    
    public void doTest() throws IOException {
        String expName = "default";
        String path = "src/dic/";
        ArrayList<String> output = new ArrayList<String>();
        output.add("Config===");
        output.add("MAX_DISTANCE: " + MAX_DISTANCE + "  OFFSET : " + OFFSET);
        output.add("Replace Weight : " + rw + " Insert Weight : " + iw + " Delete Weight : " + dw + " // Vowel Weight : " + vw + " Consonant Weight : " + cw);
        String[] filenames = {"result_oC", "result_tCD", "result_tCI", "result_oV", "result_tVD", "result_tVI"};
        double time = 0.0;
        for (String filename : filenames) {
            long startTime = System.currentTimeMillis();
            List<String> lines = FileUtil.readFile(path + filename + ".txt");
            int totalCnt = 0;
            int corrCnt = 0;
            System.out.println(filename + " start...size=" + lines.size());
            output.add(filename);
            for (String line : lines) {
                String[] spl = line.split("\t");
                String label = spl[0];
                String query = spl[1];
                //String ans = correct(query, MAX_DISTANCE, OFFSET);
                NounDic.Result res = correct(query, MAX_DISTANCE, OFFSET);
                //String ans = res.getNoun().getKeystr();
                //if (ans == null) {
                if (res == null) {
                
                } else {
                    String ans = res.getNoun().getKeystr();
                    if (label.equals(ans)) {
                        corrCnt++;
                    }
                }
                totalCnt++;
                if (totalCnt % 1000 == 0) {
                    System.out.print(totalCnt + "...");
                }
            }
            long endTime = System.currentTimeMillis();
            double elapsedTime = (endTime - startTime) / 1000.0;
            time += elapsedTime;
            System.out.println("\nElapsed Time : " + elapsedTime + " sec.");
            output.add("====================");
            output.add("Total : " + totalCnt);
            output.add("Correct : " + corrCnt);
            output.add("Accuracy : " + (double) corrCnt / totalCnt * 100);
            output.add("Elapsed Time : " + elapsedTime);
            output.add("====================");
        }
        output.add("Total Elapsed Time : " + time);
        FileUtil.writeFile(output, path + expName + "_result.txt");
        System.out.println("End Experiment...Total Elapsed Time : " + time);
    }
    
    public static void main(String args[]) {
        SpellCorrector corrector = SpellCorrector.getInstance();
        //corrector.doConsoleTest();
        try {
            corrector.doTest();
            //corrector.evaluateTestSet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
