package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.HashMap;
import java.util.Scanner;

import sun.reflect.generics.visitor.Reifier;

class Evaluator {
  
  private static HashMap<String,HashMap<Integer,Double>> relevance_for_ndcg = new HashMap<String,HashMap<Integer,Double>>();  

 public static void main(String[] args) throws IOException {
    HashMap < String , HashMap < Integer , Double > > relevance_judgments =
      new HashMap < String , HashMap < Integer , Double > >();
    if (args.length < 1){
      System.out.println("need to provide relevance_judgments");
      return;
    }
    String p = args[0];
    // first read the relevance judgments into the HashMap
    readRelevanceJudgments(p,relevance_judgments);
    // now evaluate the results from stdin
    evaluateStdin(relevance_judgments);
  }

  public static void readRelevanceJudgments(
    String p,HashMap < String , HashMap < Integer , Double > > relevance_judgments){
    try {
      BufferedReader reader = new BufferedReader(new FileReader(p));
      try {
        String line = null;
        int counter = 0;
        double relevance = 0.0;
        while ((line = reader.readLine()) != null){
          // parse the query,did,relevance line
          Scanner s = new Scanner(line).useDelimiter("\t");
          String query = s.next();
          int did = Integer.parseInt(s.next());
          String grade = s.next();
          double rel = 0.0;
          // convert to binary relevance
          if ((grade.equals("Perfect")) ||
            (grade.equals("Excellent")) ||
            (grade.equals("Good"))){
            rel = 1.0;
          }
          if ((grade.equals("Perfect"))) {
            relevance = 10.0;
          }
          else if ((grade.equals("Excellent"))) {
            relevance = 7.0;
          }
          else if ((grade.equals("Good"))) {
            relevance = 5.0;
          }
          else if ((grade.equals("Fair"))) {
            relevance = 1.0;
          }
          else if ((grade.equals("Bad"))) {
            relevance = 0.0;
          }
          if (relevance_judgments.containsKey(query) == false){
            HashMap < Integer , Double > qr = new HashMap < Integer , Double >();
            relevance_judgments.put(query,qr);
          }
          if (relevance_for_ndcg.containsKey(query) == false){
            HashMap < Integer , Double > gainMap = new HashMap < Integer , Double >();
            relevance_for_ndcg.put(query,gainMap);
            counter = 0;
          }
          HashMap < Integer , Double > qr = relevance_judgments.get(query);
          qr.put(did,rel);
          
          HashMap < Integer , Double > gainMap = relevance_for_ndcg.get(query);
          gainMap.put(did,relevance);
          counter++;
        }
      } finally {
        reader.close();
      }
    } catch (IOException ioe){
      System.err.println("Oops " + ioe.getMessage());
    }
  }
  
  private static List<Integer> getCountWithGain(HashMap<Integer,Double> gainMap) {
    List<Integer> gainCounts = new LinkedList<Integer>();
    List<Double> gains = new LinkedList<Double>();
    gains.add(10.0);
    gains.add(7.0);
    gains.add(5.0);
    gains.add(1.0);
    for(double gain:gains) {
      int counter = 0;
      for(double g:gainMap.values()) {
        if(g == gain) {
          counter++;
        }
      }
      gainCounts.add(counter);
    }
    return gainCounts;
  }
  
  public static double max(ArrayList<Double> arr, int i)
  {
      if(i < 0)
        i = 0;
      double max = arr.get(i);
      i++;
      for(; i < arr.size(); i++)
      {
          if(max < arr.get(i))
             max = arr.get(i);
      }
      return max;
  }
  
  private static void printMetrics(double RR,ArrayList<Double> rr,String q,List<Integer> docIdList) {
    List<Integer> l = new LinkedList<Integer>(); 
    l.add(1);
    l.add(5);
    l.add(10);
    StringBuilder sb = new StringBuilder();
    sb.append(q);
    sb.append('\t');
    
    List<Double> precision = new LinkedList<Double>();
    List<Double> recall = new LinkedList<Double>();
    List<Double> F = new LinkedList<Double>();
    double alfa = 0.50;
    for(int val : l)
    {
      Double x = rr.get(val-1);      
      precision.add(x/val);
      if(RR != 0) {
        recall.add(x/RR);
      }
      else {
        recall.add(0.0);
      }
      if(x != 0) {
        F.add(1.0/(alfa*val/x + (1.0-alfa)*RR/x));
      }
      else {
        F.add(0.0);
      }
    }
    
    for(double d : precision)
    {
      sb.append(d);
      sb.append('\t');
    }
    
    for(double d : recall)
    {
      sb.append(d);
      sb.append('\t');
    }
    
    for(double d : F)
    {
      sb.append(d);
      sb.append('\t');
    }
    
    //Average precision and reciprocal rank calculation
    double ap = rr.get(0);
    
    //List<Double> recallprecision = new LinkedList<Double>();
    
    double reciprocal_rank = 0.0;
    if(rr.get(0) != 0) {
      reciprocal_rank = 1.0;
    }
    
    /*for(int z=0;z<11;z++) {
      recallprecision.add(0.0);
    }*/
    
    double recallOnePoint = 0;
    
    for(int j=1;j<rr.size();j++) {
      double r = rr.get(j);
      if(rr.get(j-1) == r) {
        //we do nothing
        //average precision need not be calculated at non relevant documents
      }
      else {
        recallOnePoint = j;
        ap += r/(j+1);
        if(reciprocal_rank == 0) {
          reciprocal_rank = 1.0 / (j+1);
        }
      }
    }
    if(RR != 0) {
      ap = (ap/RR);
    }
    else {
      ap = 0.0;
    }
    
    /*boolean flag = true;
    int k = 0;
    for(double j=0;j<=recallOnePoint;j+=(0.1 * (recallOnePoint))) {
      int p = (int) Math.round(j);
      if(!flag) {
        double prec = (double) rr.get(p) * 1.0/(p+1);
        //System.out.println("p "+p+" prec "+prec);
        recallprecision.set(k, prec);
      }
      else {
        flag = false;
      }
      k += 1;
    }*/
    
    double pt = 0.1;
    Queue<Double> a = new LinkedList<Double>();
    for(pt = 0.1; pt <=1.0; pt+=0.1)
    {
        a.add(Math.ceil(pt*RR));
    }
    
    int fi = 0;
    ArrayList<Double> rpts = new ArrayList<Double>();
    while(!a.isEmpty())
    {
       if(fi >= rr.size())
         break;
       double z = rr.get(fi);
       if(a.peek() == z)
       {
         double x = a.remove();
         rpts.add(x/(fi+1));
       }
       else
       {
         fi++;
       }
    }
    
    for(int i = 0; i <= rpts.size(); i++)
    {
      sb.append(max(rpts,i-1));
      sb.append('\t');
    }
    
    sb.append(ap);
    sb.append('\t');
    
    //NDCG calculation
    List<Double> ideal_dcg_values = new LinkedList<Double>();
    List<Double> ndcg_values = new LinkedList<Double>();
    
    List<Integer> gainCounts = getCountWithGain(relevance_for_ndcg.get(q));
    double ideal_dcg = 0.0;
    int m = 0;
    int ct = gainCounts.get(m);
    for(int i=0;i<10;i++) {
      while(ct == 0 && m < 3) {
        m += 1;
        ct = gainCounts.get(m);
      }
      if(m==0) {
        ideal_dcg += (10/(Math.log(i+2)/Math.log(2)));
      }
      else if(m==1) {
        ideal_dcg += (7/(Math.log(i+2)/Math.log(2)));
      }
      else if(m==2) {
        ideal_dcg += (5/(Math.log(i+2)/Math.log(2)));
      }
      else if(m==3) {
        ideal_dcg += (1/(Math.log(i+2)/Math.log(2)));
      }
      else {
        //we don't do anything
        break;
      }
      //ideal_dcg +=  
      if(l.contains(i+1)) {
        ideal_dcg_values.add(ideal_dcg);
        //System.out.println("idcg "+ideal_dcg);
      }
      ct--;
    }
    
    //dcg calculation
    double dcg = 0;
    int counter = 0;
    HashMap<Integer,Double> rel_judge_for_query = relevance_for_ndcg.get(q);
    for(int i=0;i<10;i++) {
      int did = docIdList.get(i);
      double gain = 0;
      if(rel_judge_for_query.containsKey(did)) {
        gain = rel_judge_for_query.get(did);
      }
      else {
        gain = 0.0;
      }
      dcg += gain/(Math.log(i+2)/Math.log(2));
      if(l.contains(i+1)) {
        //System.out.println("dcg "+dcg);
        ndcg_values.add(dcg/ideal_dcg_values.get(counter));
        counter++;
      }
    }
    
    for(double ndcg_value:ndcg_values) {
      sb.append(ndcg_value);
      sb.append("\t");
    }
    
    sb.append(reciprocal_rank);
    sb.append("\n");
    
    System.out.println(sb.toString());
  }

  public static void evaluateStdin(
    HashMap < String , HashMap < Integer , Double > > relevance_judgments){
    // only consider one query per call    
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      
      ArrayList<Double> rr = new ArrayList<Double>();
      String line = null;
      double RR = 0.0;
      double N = 0.0;
      int i = 0;
      String q = null;
      List<Integer> docIdList = new LinkedList<Integer>();
      while ((line = reader.readLine()) != null){
        Scanner s = new Scanner(line).useDelimiter("\t");
        String query = s.next();
        if(!query.equals(q) && q != null) {
          printMetrics(RR,rr,q,docIdList);
          rr = new ArrayList<Double>();
          docIdList = new LinkedList<Integer>();
          RR = 0.0;
        }
        q = query;
        int did = Integer.parseInt(s.next());
        docIdList.add(did);
      	String title = s.next();
      	double rel = Double.parseDouble(s.next());
      	if (relevance_judgments.containsKey(query) == false){
      	  throw new IOException("query not found");
      	}
      	HashMap < Integer , Double > qr = relevance_judgments.get(query);
      	if (qr.containsKey(did) != false){
      	  RR += qr.get(did);
      	}
      	rr.add(RR);
      	++N;
      }
      printMetrics(RR, rr, q,docIdList);
    } catch (Exception e){
      System.err.println("Error:" + e.getMessage());
    }
  }
}