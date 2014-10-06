package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LogHandler implements HttpHandler {

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
	    
	    String uriQuery = exchange.getRequestURI().getQuery();
	    System.out.println(uriQuery);
	    Map<String,String> query_map = getQueryMap(uriQuery);
        Set<String> keys = query_map.keySet();
        StringBuilder sb = new StringBuilder();
        sb.append(query_map.get("sessionid"));
        sb.append('\t');
        sb.append(query_map.get("query"));
        sb.append('\t');
        sb.append(query_map.get("docid"));
        sb.append('\t');
        sb.append("click");
        sb.append('\t');
        sb.append(System.currentTimeMillis());
        
        System.out.println(sb.toString());
	}

}
