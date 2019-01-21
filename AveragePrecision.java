package com.app;

import java.util.*;

/**
 * Created by hannahgautreau1 on 2017-03-06.
 */



public class AveragePrecision {

    public static TreeMap<String, Double> CalculateAllTopics(Results toTest, RelevanceJudgements judgements){
        TreeMap<String, Double> resultSet = new TreeMap<>();
        Set<String> queryIDs = judgements.getQueryIDs();
        ArrayList<Results.Result> queryResults;
        for (String s: queryIDs){
            queryResults = toTest.QueryResults(s);
            resultSet.put(s, calculateTopic(s, judgements, queryResults));
        }
        return resultSet;

    }

    private static double calculateTopic(String queryID, RelevanceJudgements judgements, ArrayList<Results.Result> queryResults){
        double relevantDocsReturned = 0;
        double sum = 0;
        double avgPrecision;
        for(int i = 0; i < queryResults.size(); ++i){
            if(judgements.IsRelevant(queryID, queryResults.get(i).getDocNo())){
                relevantDocsReturned += 1;
                sum += relevantDocsReturned/(i+1);
            }
        }
        avgPrecision = sum/relevantDocsReturned;
        if(Double.isNaN(avgPrecision)){
            return 0;
        }
        return avgPrecision;
    }
}
