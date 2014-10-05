package edu.nyu.cs.cs2580;

import java.util.List;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class PhraseFreq {
	
	public static HashMap<String, Integer> CalculateDocumentVector(Document doc)
	{
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		Vector<String> bv = doc.get_body_vector();
		//int N = Document.termFrequency();
		//double total_terms = tv.size() + bv.size();
		//double incr = 1.0/total_terms;
		for(int i=1; i<bv.size() ; i++)
		{
			String term = bv.get(i-1) + " " + bv.get(i);
			if(counts.containsKey(term))
			{
				counts.put(term, counts.get(term) + 1);
			}
			else
			{
				counts.put(term, 1);
			}
		}
		
		return counts;		
	}
	
	public static HashMap<String, Integer> CalculateQueryVector(String query)
	{
		Scanner s = new Scanner(query);
	 
		HashMap <String, Integer> counts = new HashMap<String, Integer>();
		String term1 = s.next();
	    while (s.hasNext()){
	      String term2 = s.next();
	      String term = term1 + " " + term2;
	      if(counts.containsKey(term))
	    	  counts.put(term, counts.get(term)+1);
	      else
	    	  counts.put(term, 1);
	      term1 = term2;
	    }
	    s.close();
		return counts;
	}
	
	public static HashMap<String, Integer> UniCalculateDocumentVector(Document doc)
	{
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		Vector<String> bv = doc.get_body_vector();
		//int N = Document.termFrequency();
		//double total_terms = tv.size() + bv.size();
		//double incr = 1.0/total_terms;
		for(int i=0; i<bv.size() ; i++)
		{
			String term = bv.get(i);
			if(counts.containsKey(term))
			{
				counts.put(term, counts.get(term) + 1);
			}
			else
			{
				counts.put(term, 1);
			}
		}
		
		return counts;		
	}
	
	public static HashMap<String, Integer> UniCalculateQueryVector(String query)
	{
		Scanner s = new Scanner(query);
	 
		HashMap <String, Integer> counts = new HashMap<String, Integer>();
	    while (s.hasNext()){
	      String term = s.next();
	      if(counts.containsKey(term))
	    	  counts.put(term, counts.get(term)+1);
	      else
	    	  counts.put(term, 1);
	    }
	    s.close();
		return counts;
	}
}