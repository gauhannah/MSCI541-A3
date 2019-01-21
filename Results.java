package com.app;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.reflect.Array;
import java.util.*;

/**
 THIS CLASS IS BASED OFF OF M. SMUCKER'S RESULTS CLASS
 https://www.java-tips.org/java-se-tips-100019/24-java-lang/1997-how-to-use-comparable-interface.html
 */
public class Results{

    public class Result implements Comparable {
        private String docNo;
        private double score;
        private int rank;

        public Result(String docNo, double score, int rank) {
            this.docNo = docNo;
            this.rank = rank;
            this.score = score;
        }

        public String getDocNo() {
            return this.docNo;
        }

        public double getScore() {
            return this.score;
        }

        public int getRank() {
            return this.rank;
        }

        //Sort by rank ascending
        public int compareTo(Object r) {
            if(this.rank < ((Result) r).getRank()) {
                return -1;
            } else if(this.rank > ((Result) r).getRank()){
                return 1;
            } else if (this.docNo.compareTo(((Result) r).getDocNo()) == 1){
                return -1;
            }else{
                return 1;
            }
        }

    }

    private Set<String> tupleKeys;
    private HashMap<String, ArrayList> queryToResults;
    private HashMap<String, Boolean> queryToResultIsSorted;

    public Results() {
        this.tupleKeys = new HashSet<>();
        this.queryToResults = new HashMap<>();
        this.queryToResultIsSorted = new HashMap<>();
    }

    public void AddResult(String queryID, String docID, double score, int rank){
        String key = this.GenerateTupleKey(queryID, docID);
        if(this.tupleKeys.contains(key)) {
            try {
                throw new Exception("Cannot have duplicate queryID and docID data points");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        tupleKeys.add(key);

        ArrayList results;
        if (this.queryToResults.containsKey(queryID)){
            results = this.queryToResults.get(queryID);
        }
        else {
            results = new ArrayList();
            this.queryToResults.put(queryID, results);
            this.queryToResultIsSorted.put(queryID, false);
        }
        Result result = new Result(docID, score, rank);
        results.add(result);

    }

    public String GenerateTupleKey( String queryID, String docID )
    {
        return queryID + "-" + docID ;
    }

    public ArrayList<Result> QueryResults(String queryID) {
        if(!this.queryToResults.containsKey(queryID)){
            try {
                throw new Exception("no such query in results");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ArrayList<Result> results = this.queryToResults.get(queryID);
        if(!queryToResultIsSorted.get(queryID)){
            Collections.sort(results);
            this.queryToResultIsSorted.put(queryID, true);
        }

        return results;
    }

    public Set<String> getQueryIDs(){
        return this.queryToResults.keySet();
    }

    public boolean QueryIDExists(String queryID) {
        return this.queryToResults.containsKey(queryID);
    }


}
