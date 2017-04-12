package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import debug.Word;

public class FileUtil {
    
    public static List<String> readFile(String path) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));
        
        while (true) {
            String line = br.readLine();
            if (line == null)
                break;
            lines.add(line);
        }
        br.close();
        
        return lines;
    }
    
    public static void writeFile(List<String> lines, String path) throws IOException {
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), "UTF8"));
        for (String line : lines) {
            bw.write(line);
            bw.write("\n");
        }
        bw.close();
    }
    
    public static void writeFile(List<String> lines, String path, String charset) throws IOException {
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), charset));
        for (String line : lines) {
            bw.write(line);
            bw.write("\n");
        }
        bw.close();
    }
    
    public static List<String> getCorpusListFromFile(String path) throws IOException {
        List<String> corpusList = readFile(path);
        System.out.println("---Corpus loaded : " + corpusList.size());
        return corpusList;
    }
    
    public static List<Word> getWordListFromFile(String path) throws IOException {
        List<Word> wordList = new ArrayList<Word>();
        List<String> lines = readFile(path);
        
        for (String line : lines) {
            String temp[] = line.split(" ");
            wordList.add(new Word(temp[0], Integer.parseInt(temp[1])));
        }
        System.out.println("---word loaded : " + wordList.size());
        return wordList;
    }
}
