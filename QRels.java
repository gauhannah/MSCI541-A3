package com.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * THIS CLASS IS BASED OFF OF M. SMUCKER'S QRELS CLASS
 */
public class QRels {
    public RelevanceJudgements judgements = new RelevanceJudgements();

    public QRels(String path) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            String[] fields;
            String queryID;
            String docNo;
            int judgement;
            while ((line = br.readLine()) != null) {
                 fields = line.split("\\s+");
                 if(fields.length != 4) {
                     throw new Exception("Incorrect input format. The input should be formatted as: " +
                        "topicID ignore docno judgement");
                 }
                 queryID = fields[0];
                 docNo = fields[2];
                 judgement = Integer.parseInt(fields[3]);
                 judgements.AddJudgement(queryID, docNo, judgement);
            }
            System.out.println("done");

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            br.close();
        }

    }

}
