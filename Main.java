package com.app;

import apple.laf.JRSUIUtils;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    //https://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
    // This gathers all of the results for all performance metrics.
    public static void main(String[] args) throws IOException {
        String indexPath;
        String qRelsPath;
        String pathForResults;
        String pathForEvaluations;
        try{
            indexPath = args[0];
            qRelsPath = args[1];
            pathForResults =  args[2];
            pathForEvaluations = args[3];
        } catch (Exception ex){
            System.out.println("One of your directories was not found. Please try again.");
            return;
        }
        QRels qrels = new QRels(qRelsPath);
        WordCounts wordCounts = new WordCounts(indexPath);
        RelevanceJudgements judgements = qrels.judgements;
        for(int i = 1; i < 15 ; ++i){
            String runID = "student" + Integer.toString(i);
            String resultsPath = pathForResults + runID + ".results";
            ResultsFile resultsFile = new ResultsFile(resultsPath);

            Results results = resultsFile.results;
            if(results != null) {
                TreeMap<String, Double> averagePrecisionResults = AveragePrecision.CalculateAllTopics(results, judgements);
                TreeMap<String, Double> precisionAt10Results = PrecisionAtN.CalculateAllTopics(results, judgements, 10);
                TreeMap<String, Double> nDCGAt10 = NDCG.CalculateAllTopics(results, judgements, 10);
                TreeMap<String, Double> nDCGAt1000 = NDCG.CalculateAllTopics(results, judgements, 1000);
                TreeMap<String, Double> timeBiasedGain = TimeBiasedGain.CalculateAllTopics(results, judgements, wordCounts);
                if(timeBiasedGain != null && nDCGAt10 != null && nDCGAt10 != null && precisionAt10Results != null && averagePrecisionResults != null) {

                    printToFile(averagePrecisionResults, precisionAt10Results, nDCGAt10, nDCGAt1000, timeBiasedGain, runID, pathForEvaluations);
                } else {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(pathForEvaluations + "/" +runID+  ".csv"));
                    bw.write("bad run");
                    bw.close();
                }
            }
            else {
                BufferedWriter bw = new BufferedWriter(new FileWriter(pathForEvaluations + "/" +runID+  ".csv"));
                bw.write("bad run");
                bw.close();
            }
        }

    }

    // write all of the results to a file
    public static void printToFile(TreeMap<String, Double> averagePrecisionResults, TreeMap<String, Double> precisionAt10Results,
                                   TreeMap<String, Double> nDCGAt10, TreeMap<String, Double> nDCGAt1000, TreeMap<String, Double> timeBiasedGain,
                                   String runID, String pathForEvaluations){
        try {

            BufferedWriter bw = null;
            String path=pathForEvaluations;
            bw = new BufferedWriter(new FileWriter(path + "/" +runID+  ".csv"));
            bw.write("RUNID,TOPIC ID,Average Precision,P@10,nDCG@10,nDCG@1000,TBG" + System.lineSeparator());
            for(Map.Entry<String,Double> entry : averagePrecisionResults.entrySet()) {
                bw.write(runID + "," + entry.getKey() + ","
                    + entry.getValue() + "," + precisionAt10Results.get(entry.getKey())
                    + "," + nDCGAt10.get(entry.getKey())
                    + "," + nDCGAt1000.get(entry.getKey())
                    + "," + timeBiasedGain.get(entry.getKey())
                    + System.lineSeparator());
            }
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
