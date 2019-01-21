package com.app;

import apple.laf.JRSUIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by hannahgautreau1 on 2017-03-06. This calculates time biased gain per the Smucker and Clarke Paper
 */
public class TimeBiasedGain {

    // constants from the paper
    private static final double a = 0.018;
    private static final double b = 7.8;
    private static final double summaryTime = 4.4;
    private static final double probClickRelevant = 0.64;
    private static final double probClickNotRelevant = 0.39;


    // gather all results for TBG
    public static TreeMap<String, Double> CalculateAllTopics(Results toTest, RelevanceJudgements judgements, WordCounts wordCounts) throws IOException {

        TreeMap<String, Double> resultSet = new TreeMap<>();
        Set<String> queryIDs = judgements.getQueryIDs();
        ArrayList<Results.Result> queryResults;
        for (String s: queryIDs){
            queryResults = toTest.QueryResults(s);
            double topicScore = calculateTopic(s, wordCounts, judgements, queryResults);
            if(topicScore == -1){
                return null;
            }
            resultSet.put(s, topicScore);
        }
        return resultSet;
    }

    // calculate TBG for 1 topic
    private static double calculateTopic(String queryID, WordCounts wordcounts, RelevanceJudgements judgements, ArrayList<Results.Result> results){
        double timeBiasedGain = 0;
        double timeAtK;
        for(int i = 0; i < results.size(); ++i){
            if(judgements.IsRelevant(queryID, results.get(i).getDocNo())){
                timeAtK = CalculateTimeToReachK(i, queryID, wordcounts, results, judgements);
                if(timeAtK == -1){
                    return -1;
                }
                timeBiasedGain += CalculateDecay(timeAtK);
            }
        }

        return timeBiasedGain;
    }

    // calculate the decay function
    private static double CalculateDecay(double time){
        return Math.exp((-1*time)*Math.log(2)/224);
    }


    // calculate the time to reach l for 1 k
    private static double CalculateTimeToReachK(int k, String queryID, WordCounts wordCounts, ArrayList<Results.Result> results, RelevanceJudgements judgements){
        double timeAtK=0;
        double probability;
        for (int i = 0; i < k; ++i) {
            if(judgements.IsRelevant(queryID, results.get(i).getDocNo())){
                probability = probClickRelevant;
            } else {
                probability = probClickNotRelevant;
            }
            try {
                timeAtK += summaryTime + (a * wordCounts.wordCounts.get(results.get(i).getDocNo()) + b) * probability;
            } catch (Exception ex) {
                return -1;
            }
        }
        return timeAtK;

    }
}
