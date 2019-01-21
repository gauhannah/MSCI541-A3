package com.app;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by hannahgautreau1 on 2017-03-06.
 */
public class PrecisionAtN {

    // Collect all precision at N results
    public static TreeMap<String, Double> CalculateAllTopics(Results toTest, RelevanceJudgements judgements, int n){
        TreeMap<String, Double> resultSet = new TreeMap<>();
        Set<String> queryIDs = judgements.getQueryIDs();
        ArrayList<Results.Result> queryResults;
        for (String s: queryIDs){
            queryResults = toTest.QueryResults(s);
            double topicResult = calculateTopic(s, judgements, queryResults, n);
            if(topicResult == -1){
                return null;
            }
            resultSet.put(s, topicResult);
        }
        return resultSet;

    }


    // Calculate precision at N for 1 topic
    private static double calculateTopic(String queryID, RelevanceJudgements judgements, ArrayList<Results.Result> queryResults, int n){
        double relevantDocsReturned = 0;
        double precisionAtN;
        for(int i = 0; i < n && i < queryResults.size(); ++i){
            try {
                if (judgements.IsRelevant(queryID, queryResults.get(i).getDocNo())) {
                    relevantDocsReturned += 1;
                }
            } catch (Exception ex){
                return -1;
            }
        }
        precisionAtN = relevantDocsReturned/n;
        return precisionAtN;
    }
}
