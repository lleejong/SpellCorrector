package kma;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import kr.co.wisenut.wisekmaorange.WK_Analyzer;
import kr.co.wisenut.wisekmaorange.WK_Eojul;
import kr.co.wisenut.wisekmaorange.WK_Global;
import kr.co.wisenut.wisekmaorange.WK_Knowledge;

public class KMAWrapper {
    private static final String KNOW_PATH = "src/knowledge";
    private static KMAWrapper instance = null;
    public static KMAWrapper getInstance() {
        if(instance == null)
            instance = new KMAWrapper();
        return instance;
    }
    
    private WK_Knowledge knowledge;
    private WK_Analyzer analyzer;
    private WK_Eojul eojul;
    private HashSet<String> tagFilter;
    private String[] targetTag = {"NNG", "NNP", "NNU", "NNR", "NFU", "NFG"};
    
    public class KMAResult {
        public List<String> morph;
        public List<String> tag;
        public String result;
        
        public KMAResult() {
            morph = new ArrayList<String>();
            tag = new ArrayList<String>();
        }
    }
    
    private KMAWrapper() {
        this.knowledge = new WK_Knowledge();
        knowledge.createObject();
        int ret = knowledge.loadKnowledge(KNOW_PATH, true, false);
        if (ret < 0) {
            System.out.println("Knowledge loading fail");
            return;
        }
        ret = knowledge.loadUserNoun(KNOW_PATH, "usernoun.txt", true);
        if (ret < 0) {
            System.out.println("usernoun loading fail");
            return;
        }
//		ret = knowledge.loadUserNoun("src/test", "poi-nnr-spacing-bigram.txt", true);
//		if (ret < 0) {
//			System.out.println("usernoun loading fail");
//			return;
//		}
        ret = knowledge.loadUserNoun("src/test", "poi-nnr-spacing-unigram-update2.txt", true);
        if (ret < 0) {
            System.out.println("usernoun loading fail");
            return;
        }
        analyzer = new WK_Analyzer(knowledge);
        eojul = new WK_Eojul();
        eojul.createObject();
        
        analyzer.setEojul(eojul);
        
        analyzer.setOption(WK_Global.WKO_OPTION_N_BEST, 1);
        analyzer.setOption(WK_Global.WKO_OPTION_EXTRACT_ALPHA, 1);
        analyzer.setOption(WK_Global.WKO_OPTION_EXTRACT_NUM, 1);
        
        // filter init
        tagFilter = new HashSet<String>();
        for (int i = 0; i < targetTag.length; i++)
            tagFilter.add(targetTag[i]);
        
    }
    
    public KMAResult getResultWithFilter(String query, HashSet<String> filter) {
        
        StringTokenizer tokens = new StringTokenizer(query, "  \t\r\n?!.-,\"'()[]<>~\\:;`_/|+=*&^%$#@{}");
        
        if (tokens.countTokens() == 0)
            return null;
        
        boolean flag = false;
        KMAResult result = new KMAResult();
        StringBuffer output = new StringBuffer();
        
        for (int posOfToken = 0; tokens.hasMoreElements(); posOfToken++) {
            String eojulStr = tokens.nextToken();
            
            // Eojul Initialize
            eojul.eojulInitialize();
            
            // Set String for analyzing
            eojul.setString(eojulStr);
            
            // analyzing
            int ret = analyzer.runWithEojul();
            if (ret < 0)
                break;
            
            for (int posOfList = 0; posOfList < eojul.getListSize(); posOfList++) {
                for (int posOfMorph = 0; posOfMorph < eojul.getCount(posOfList); posOfMorph++) {
                    if (posOfMorph != 0)
                        output.append(" + ");
                    else
                        output.append("\t" + posOfList + " : ");
                    
                    String morph = eojul.getLexicon(posOfList, posOfMorph);
                    String tag = eojul.getStrPOS(posOfList, posOfMorph);
                    result.morph.add(morph);
                    result.tag.add(tag);
                    
                    output.append(morph + "/" + tag);
                    
                    if (filter.contains(tag))
                        flag = true;
                    
                    if (eojul.isIndexWord(posOfList, posOfMorph) == true)
                        output.append(" [1]");
                    else
                        output.append(" [0]");
                }
            }
        }
        result.result = output.toString();
        
        if (flag)
            return result;
        else
            return null;
    }
    
    public List<String> getNouns(String str) {
        List<String> nounList = new ArrayList<String>();
        
        StringTokenizer tokens = new StringTokenizer(str, "  \t\r\n?!.-,\"'()[]<>~\\:;`_/|+=*&^%$#@{}");
        
        if (tokens.countTokens() == 0)
            return null;
        
        for (int posOfToken = 0; tokens.hasMoreElements(); posOfToken++) {
            String eojulStr = tokens.nextToken();
            
            // Eojul Initialize
            eojul.eojulInitialize();
            
            // Set String for analyzing
            eojul.setString(eojulStr);
            
            // analyzing
            int ret = analyzer.runWithEojul();
            if (ret < 0)
                break;
            
            for (int posOfList = 0; posOfList < eojul.getListSize(); posOfList++) {
                for (int posOfMorph = 0; posOfMorph < eojul.getCount(posOfList); posOfMorph++) {
                    String morph = eojul.getLexicon(posOfList, posOfMorph);
                    String tag = eojul.getStrPOS(posOfList, posOfMorph);
                    
                    if (tagFilter.contains(tag))
                        nounList.add(morph);
                }
                
            }
        }
        return nounList;
    }
    
    public void test(String str) {
        StringTokenizer tokens = new StringTokenizer(str, "  \t\r\n?!.-,\"'()[]<>~\\:;`_/|+=*&^%$#@{}");
        
        if (tokens.countTokens() == 0) {
            return;
        }
        
        for (int posOfToken = 0; tokens.hasMoreElements(); posOfToken++) {
            String eojulStr = tokens.nextToken();
            
            // Eojul Initialize
            eojul.eojulInitialize();
            
            // Set String for analyzing
            eojul.setString(eojulStr);
            
            // analyzing
            int ret = analyzer.runWithEojul();
            if (ret < 0) {
                System.out.println("Analyze Faild: " + eojul.getString());
                
                break;
            }
            
            // Print Result
            System.out.println("\n> [" + eojul.getString() + "]");
            
            for (int posOfList = 0; posOfList < eojul.getListSize(); posOfList++) {
                for (int posOfMorph = 0; posOfMorph < eojul.getCount(posOfList); posOfMorph++) {
                    if (posOfMorph != 0)
                        System.out.print(" + ");
                    else
                        System.out.print("\t" + posOfList + " : ");
                    
                    String morph = eojul.getLexicon(posOfList, posOfMorph);
                    String tag = eojul.getStrPOS(posOfList, posOfMorph);
                    
                    System.out.print(morph + "/" + tag);
                    
                    if (eojul.isIndexWord(posOfList, posOfMorph) == true)
                        System.out.print(" [1]");
                    else
                        System.out.print(" [0]");
                }
                
                System.out.println("");
            }
        }
        
    }
    
    public void finalize() {
        eojul.finalize();
        analyzer.finalize();
        knowledge.finalize();
    }
    
    public static void main(String args[]) {
        KMAWrapper kma = KMAWrapper.getInstance();
        kma.test("알콜 - 약물 중독에서 벗어난 사람이 아직 여기에 빠져 있는 사람을 돕는 등 비슷한 상황에 있거나 겪은 사람이 가장 좋은 상담자가 될 수 있다는 것이죠.");
    }
}
