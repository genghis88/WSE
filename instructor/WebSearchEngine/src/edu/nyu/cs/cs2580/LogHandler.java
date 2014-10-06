package edu.nyu.cs.cs2580;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LogHandler implements HttpHandler {
  
  private static final String logLocation = "logs/log.txt";
  
  private static BufferedWriter logger = null;
  
  public LogHandler() {
    try {
      File file = new File(logLocation);
      System.out.println("FILE "+file.getAbsolutePath());
      logger = new BufferedWriter(new FileWriter(file));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

	public static Map<String, String> getQueryMap(String query){  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params){  
	      String name = param.split("=")[0];  
	      String value = param.split("=")[1];  
	      map.put(name, value);  
	    }
	    return map;  
	}
	
	private static void log(String uriQuery) {
    System.out.println(uriQuery);
    Map<String,String> query_map = getQueryMap(uriQuery);
    String sessionid = query_map.get("sessionid");
    String query = query_map.get("query");
    String docid = query_map.get("docid");
    String event = "click";
    log(sessionid,query,docid,event);
	}
	
	private static void log(String sessionid,String query, String docid,String event) {
      StringBuilder sb = new StringBuilder();
      sb.append(sessionid);
      sb.append('\t');
      sb.append(query);
      sb.append('\t');
      sb.append(docid);
      sb.append('\t');
      sb.append(event);
      sb.append('\t');
      sb.append(System.currentTimeMillis());
      sb.append('\n');
      
      System.out.println(sb.toString());
      try {
        logger.write(sb.toString());
        logger.flush();
        System.out.println("written");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.println("Unable to log");
      }
  }
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub
		String requestMethod = exchange.getRequestMethod();
	    if (!requestMethod.equalsIgnoreCase("GET")){  // POST requests only.
	      return;
	    }
	    
	    Headers requestHeaders = exchange.getRequestHeaders();
	    System.out.print("Incoming request: ");
	    for (String key : requestHeaders.keySet()){
	      System.out.print(key + ":" + requestHeaders.get(key) + "; ");
	    }
	    System.out.println(); 
	    
	    if(requestHeaders.get("Cookie") != null) {
        String cookie = requestHeaders.get("Cookie").get(0);
        int sesId = Integer.parseInt(cookie);
      }
      else {
        //something wrong!!
      }
	    
	    String uriQuery = exchange.getRequestURI().getQuery();
	    log(uriQuery);
	}
}
