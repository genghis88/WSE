package edu.nyu.cs.cs2580;

import java.util.Scanner;
import java.util.Vector;

public class LinearRanker extends Ranker {
  private double beta_cos = 0.33;
  private double beta_ql = 0.33;
  private double beta_phrase = 0.33;
  private double beta_numviews = 0.01;
  
  public LinearRanker(String index_source) {
    super(index_source);
  }
  
  public LinearRanker(Index index) {
    super(index);
  }
  
  public ScoredDocument runquery(String query, int did){
    // Build query vector
    Ranker cos = new Cosine(get_index());
    Ranker ql = new QLRanker(get_index());
    Ranker phrase = new Phrase(get_index());
    Ranker numviews = new Numviews(get_index());
    double score = beta_cos * cos.runquery(query, did)._score + beta_ql * ql.runquery(query, did)._score 
         + beta_phrase * phrase.runquery(query,did)._score + beta_numviews * numviews.runquery(query, did)._score;
    Document d = super.get_index().getDoc(did);
    return new ScoredDocument(did, d.get_title_string(), score);
  }
}
