package edu.nyu.cs.cs2580;

import java.util.Collections;
import java.util.Vector;

public class Numviews extends Ranker
{
	public Numviews(String index_source)
	{
		super(index_source);
		// TODO Auto-generated constructor stub
	}
	
	public Numviews(Index index) {
    super(index);
  }
	
	public Vector < ScoredDocument > runquery(String query){
	    Vector < ScoredDocument > retrieval_results = new Vector < ScoredDocument > ();
	    for (int i = 0; i < get_index().numDocs(); ++i){
	      retrieval_results.add(runquery(query, i));
	    }
	    Collections.sort(retrieval_results, new ScoredDocumentComparator());
	    return retrieval_results;
	}
	
	public ScoredDocument runquery(String query, int did){
		Document d = super.get_index().getDoc(did);
		double numviews = (double)d.get_numviews();
		double totalviews = (double)Document.getTotalViews();
		double score = numviews/totalviews;
		System.out.println(numviews + "" + totalviews);
		return new ScoredDocument(did, d.get_title_string(), score);
	}
}