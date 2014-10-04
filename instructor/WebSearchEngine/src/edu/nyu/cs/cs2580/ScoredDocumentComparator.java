package edu.nyu.cs.cs2580;

import java.util.Comparator;

public class ScoredDocumentComparator implements Comparator<ScoredDocument>{

	@Override
	public int compare(ScoredDocument o1, ScoredDocument o2) {
		// TODO Auto-generated method stub
		double diff = o2._score - o1._score;
		return (int)((diff)/Math.abs(diff)) ;
	}
	
}
