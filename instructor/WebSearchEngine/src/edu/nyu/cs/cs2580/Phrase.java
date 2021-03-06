package edu.nyu.cs.cs2580;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class Phrase extends Ranker
{
  public Phrase(String index_source) {
    super(index_source);
    // TODO Auto-generated constructor stub
  }
  
  public Phrase(Index index) {
    super(index);
  }
  
  public Vector < ScoredDocument > runquery(String query){
      Vector < ScoredDocument > retrieval_results = new Vector < ScoredDocument > ();
      if(query.split(" ").length > 1)
      {
        for (int i = 0; i < get_index().numDocs(); ++i){
          retrieval_results.add(runquery(query, i));
        }
      }
      else
      {
        for (int i = 0; i < get_index().numDocs(); ++i){
          retrieval_results.add(runqueryUni(query, i));
        }
      }
      Collections.sort(retrieval_results, new ScoredDocumentComparator());
      return retrieval_results;
  }
  
  public ScoredDocument runquery(String query, int did) {
    if(query.split(" ").length > 1)
    {
      return runqueryMulti(query, did);
    }
    else
    {
      return runqueryUni(query, did);
    }
  }

  public ScoredDocument runqueryMulti(String query, int did){

    Document d = super.get_index().getDoc(did);
    HashMap<String, Integer> qv = PhraseFreq.CalculateQueryVector(query);
    HashMap<String, Integer> dv = PhraseFreq.CalculateDocumentVector(d);
    double score = CalculateScore(qv,dv);
      return new ScoredDocument(did, d.get_title_string(), score);
  }
  
  public ScoredDocument runqueryUni(String query, int did){

    Document d = super.get_index().getDoc(did);
    HashMap<String, Integer> qv = PhraseFreq.UniCalculateQueryVector(query);
    HashMap<String, Integer> dv = PhraseFreq.UniCalculateDocumentVector(d);
    double score = CalculateScore(qv,dv);
      return new ScoredDocument(did, d.get_title_string(), score);
  }
  
  public double CalculateScore(HashMap<String, Integer> qv, HashMap<String, Integer> dv)
  {
    Set<String> A = qv.keySet();
      Set<String> B = dv.keySet();
      
      Set<String> keys = qv.size() < dv.size() ? A : B;
    
      double dena = 0.0;
      for(String a : A)
      {
        dena+= qv.get(a)*qv.get(a);
      }
      double denb = 0.0;
      for(String b : B)
      {
        denb += dv.get(b)*dv.get(b);
      }
    
      double denominator = Math.sqrt(dena * denb);
    
      // Score the document. Here we have provided a very simple ranking model,
      // where a document is scored 1.0 if it gets hit by at least one query term.
      double score = 0.0;
      for (String key: keys){
        double temp1 = 0.0;
        if(qv.containsKey(key))
          temp1 = qv.get(key);
        double temp2 = 0.0;
        if(dv.containsKey(key))
          temp2 = dv.get(key);
      
        score += temp1*temp2;
      }
      score /= denominator;
      return score;
  }

}