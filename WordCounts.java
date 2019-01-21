package com.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hannahgautreau1 on 2017-03-07.
 * Gathers all of the word counts from the documents provided in homework 1
 */
public class WordCounts {
    public HashMap<String, Integer> wordCounts;

    public WordCounts(String path) throws IOException {
        ArrayList<String> docNos = GetDocNos(path);
        wordCounts = new HashMap<>();
        for(int i = 0; i < docNos.size(); ++i) {
            this.wordCounts.put(docNos.get(i), getWordCount(docNos.get(i), path));
            System.out.println(i);
        }
    }

    public static int getWordCount(String docNo, String indexPath) throws IOException {
        String path = indexPath + "/" + docNo.substring(6,8) + "/" +
                docNo.substring(2,4) + "/" + docNo.substring(4,6) + "/" + docNo + ".txt";
        BufferedReader br=null;
        int wordCount = 0;
        File test = new File(path);
        if(!test.exists()) {
            System.out.println("File not found, please try again");
            return wordCount;
        }
        try {

            br = new BufferedReader(new FileReader(path));
            String line;
            boolean foundCount=false;
            while ((line = br.readLine()) != null && !foundCount) {
                String[] tmp = line.split(":") ;
                if(tmp[0].equals("word count")){
                   wordCount = Integer.parseInt(tmp[1].trim());
                   foundCount = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return wordCount;
    }

    private ArrayList<String> GetDocNos(String path) {
        ArrayList<String> docNos = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path + "/index.txt"));
            String line;
            while ((line = br.readLine()) != null){
                String[] tmp = line.split(",");
                docNos.add(tmp[1]);
            }
            return docNos;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return docNos;
    }



}
