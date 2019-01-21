package com.app;

import sun.jvm.hotspot.debugger.posix.elf.ELFHashTable;

import java.security.Key;
import java.util.*;

/**
 * THIS CLASS IS BASED OFF OF M. SMUCKER'S RELEVANCE JUDGEMENTS CLASS
 */
public class RelevanceJudgements {

    private static class Tuple {
        private String queryID;
        private String docNo;
        private int judgement;

        public Tuple(String queryID, String docNo, int judgement) {
            this.queryID = queryID;
            this.docNo = docNo;
            this.judgement = judgement;
        }


        public int getJudgement() {
            return this.judgement;
        }

        public static String GenerateKey(String queryID, String docNo) {
            return queryID + "-" + docNo;
        }

        public String getKey() {
            return queryID + "-" + docNo;
        }
    }

    private HashMap<String, Tuple> tuples;
    private TreeMap<String, ArrayList<String>> queryToRelDocNos;

    public RelevanceJudgements() {
        this.tuples = new HashMap<>();
        this.queryToRelDocNos = new TreeMap<>();

    }

    public void AddJudgement(String queryID, String docNo, int judgement) {
        Tuple tuple = new Tuple(queryID, docNo, judgement);
        try {
            if (this.tuples.containsKey(tuple.getKey())) {
                throw new Exception("Cannot have duplicate queryID to docID data");
            }
            this.tuples.put(tuple.getKey(), tuple);
            if (tuple.judgement != 0) {
                ArrayList<String> tmpRelevantDocs = null;
                if (queryToRelDocNos.containsKey(queryID)) {
                    tmpRelevantDocs = (ArrayList) queryToRelDocNos.get(queryID);
                } else {
                    tmpRelevantDocs = new ArrayList<>();
                    queryToRelDocNos.put(queryID, tmpRelevantDocs);
                }
                if (!tmpRelevantDocs.contains(docNo)) {
                    tmpRelevantDocs.add(docNo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Determines if the document is relevant to the query
    public boolean IsRelevant(String queryID, String docNo) {
        return GetJudgement(queryID, docNo, true) != 0;
    }


    public int GetJudgement(String queryID, String docNo, boolean assumeNonRel) {
        try {
            if (!this.queryToRelDocNos.containsKey(queryID)) {
                throw new Exception("no relevance judgements for queryID = " + queryID);
            }
            String key = Tuple.GenerateKey(queryID, docNo);
            if(!tuples.containsKey(key)){
                if(assumeNonRel) {
                    return 0;
                }
                else {
                    throw new Exception("no relevance judgement for queryID and docNo");
                }
            } else {
                Tuple tuple = (Tuple)tuples.get(key);
                return tuple.getJudgement();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int NumRelevant(String queryID){
        if(this.queryToRelDocNos.containsKey(queryID)){
            return this.queryToRelDocNos.get(queryID).size();
        } else {
            try {
                throw new Exception("no relevance judgements for queryID =" + queryID);
            } catch (Exception ex){
                ex.printStackTrace();
                return 0;
            }
        }
    }

    public Set<String> getQueryIDs(){
        return this.queryToRelDocNos.keySet();
    }

}




