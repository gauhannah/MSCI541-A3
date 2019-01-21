package com.app;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

/**
 http://stackoverflow.com/questions/30132776/calculating-log2-logarithm-in-java
 */

public class NDCG {

    // Gather NDCG for all topics
    public static TreeMap<String, Double> CalculateAllTopics(Results toTest, RelevanceJudgements judgements, int n){
        TreeMap<String, Double> resultSet = new TreeMap<>();
        Set<String> queryIDs = judgements.getQueryIDs();
        ArrayList<Results.Result> queryResults;

        for (String s: queryIDs){
            queryResults = toTest.QueryResults(s);
            double relevantDocsReturned = 0;
            for(int i = 0; i < queryResults.size(); ++i){
                if(judgements.IsRelevant(s, queryResults.get(i).getDocNo())){
                    relevantDocsReturned += 1;
                }
            }
            resultSet.put(s, calculateNDCG(s, relevantDocsReturned, judgements, queryResults, n));
        }
        return resultSet;

    }

    // Calculate NDCG for 1 topic
    private static double calculateNDCG(String queryID, double relevantDocsReturned, RelevanceJudgements judgements, ArrayList<Results.Result> queryResults, int n){
        double dcg = 0;
        double ndcg = 0;
        double idcg = calculateIDCG(queryID, relevantDocsReturned, n);
        for(int i = 0; i < n && i < queryResults.size(); ++i) {
            if (judgements.IsRelevant(queryID, queryResults.get(i).getDocNo())) {
                dcg += 1/(Math.log(i+2.0)/Math.log(2));
            }
        }
        ndcg = dcg/idcg;
        if(Double.isNaN(ndcg)){
            return 0;
        }
        return ndcg;
    }

    // Calculate IDCG
    private static double calculateIDCG(String queryID, double relevantDocsReturned , int n) {
        double idcg = 0;
        for (int i = 0; i < n && i < relevantDocsReturned; ++ i){
            idcg += 1/(Math.log(i+2.0)/Math.log(2));
        }

        return idcg;
    }

}
