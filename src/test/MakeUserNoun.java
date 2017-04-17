package test;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Saebyeok on 2017-04-11.
 */
public class MakeUserNoun {

    public static void main(String [] args){
        String s, str, strNum, key;
        String[] tempArray = {};
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        String InputFilePath = "src/test/poi-modified.txt";
        String OutputFilePath = "src/test/poi-modified-bigram.txt";

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(InputFilePath), "utf-8"));

            while ((s = input.readLine()) != null) {
                tempArray = s.split(" ");


                if (tempArray.length == 1) {
                    str = "";
                    strNum = "0";
                    str = tempArray[0];
                    key = str + "\t" + strNum;

                    if (map.get(key) == null) {
                        map.put(key, 1);
                    } else {
                        map.put(key, map.get(key) + 1);
                    }


                }
                else if (tempArray.length > 1) {
                    for (int num = 0; num < tempArray.length - 1; num++) {
                        str = "";
                        strNum = " ";
                        for (int i = num; i < num + 2; i++) {
                            str += tempArray[i];
                            int len = tempArray[i].length();
                            if(len >= 10)
                                strNum += (char)('a' + (len - 10));
                            else
                                strNum += Integer.toString(len);
                        }
                        //  System.out.println(str);
                        //  System.out.println(strNum);
                        key = str + "\t" + strNum;
                        //   System.out.println(key);

                        if (map.get(key) == null) {
                            map.put(key, 1);
                        } else {
                            map.put(key, map.get(key) + 1);
                        }
                    }

                }

            }

            BufferedWriter output = new BufferedWriter(new FileWriter(OutputFilePath, true));
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                // System.out.print(key);
                output.write(key);
                // System.out.println("\t" + map.get(key));
                output.write("\t" + map.get(key));
                output.newLine();
            }
            input.close();
            output.flush();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
