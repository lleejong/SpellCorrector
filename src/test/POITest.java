package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import kma.KMAWrapper;
import kma.KMAWrapper.KMAResult;
import util.FileUtil;

public class POITest {
	public static void poitest(){
		try {
			KMAWrapper kma = KMAWrapper.getInstance();
			List<String> lines = FileUtil.readFile("src/test/poi-modified.txt");
			
			HashSet<String> filter = new HashSet<String>();
			filter.add("NNR");
			
			System.out.println("TOTAL : " + lines.size());
			List<String> output = new ArrayList<String>();
			int cnt = 0;
			for(String line : lines){
				cnt++;
				if(cnt % 100000 == 0){
					System.out.println(cnt);
				}
				int spacecnt = 0;
				for(int i = 0; i < line.length(); i++){
					if(line.charAt(i) == ' ')
						spacecnt++;
				}
				String trimmedLine = line.replaceAll(" ", "");
				KMAResult result = kma.getResultWithFilter(trimmedLine, filter);
				if(result == null){
					continue;
				}
				else if(spacecnt <= (result.morph.size() - 1)){
					continue;
				}else{
					output.add(line);
					output.add(result.result);
//					StringBuffer outLine = new StringBuffer(line);
//					for(KMAResult kmaResult : result){
//						outLine.append(kmaResult.morph);
//						outLine.append("/");
//						outLine.append(kmaResult.tag+"\t");
//					}
//					output.add(outLine.toString());
				}
				
			}
			
			FileUtil.writeFile(output, "src/test/poi-test-result2.txt");
			System.out.println("NNR :" + output.size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		poitest();
	}
}
