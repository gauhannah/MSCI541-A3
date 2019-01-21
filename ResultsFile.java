package com.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * THIS CLASS IS BASED OFF OF M. SMUCKER'S RESULTS CLASS
 */
public class ResultsFile {

    public Results results = new Results();
    public String runId;

    public ResultsFile(String path) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            boolean firstLine = true;
            String line;
            while((line = br.readLine()) != null){
                String [] fields = line.split("\\s+");
                if(fields.length != 6){
                    throw new Exception("input should have 6 columns");
                }

                String queryID = fields[0];
                String docID = fields[2];
                try {
                    int rank = Integer.parseInt(fields[3]);
                    double score = Double.parseDouble(fields[4]);
                    results.AddResult(queryID, docID, score, rank);
                } catch (Exception ex){
                    results = null;
                    return;
                }

                if(firstLine){
                    this.runId = fields[5];
                    firstLine = false;
                } else if (!this.runId.equals(fields[5])) {
                    throw new Exception("mismatching runIDs in file");
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            br.close();
        }
    }
}


