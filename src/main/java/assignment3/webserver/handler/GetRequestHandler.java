package assignment3.webserver.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.google.common.collect.ImmutableMap;
import com.sun.net.httpserver.HttpExchange;


import assignment3.webserver.WebQuery;
import assignment3.webserver.WebQueryManager;
import assignment3.webserver.exceptions.WebServerException;

/**
 * Processes a GET request from the user and response with the appropriate HTML file to
 * display the visualisation
 */
public class GetRequestHandler extends FileRequestHandler {
	WebQuery webQuery = new WebQueryManager();
    public GetRequestHandler(String root) {
        super(root);
    }

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        Map<String, String> keyValuePairs = parseQueryString(httpExchange.getRequestURI().getQuery());;
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (keyValuePairs.containsKey("getVisualisation")) {
            // Create JSON response
            // TODO: Not hardcode this
        	
            String html = "q1.html";
            if (keyValuePairs.containsKey("vizType")) {
            	html = generateNewCsvData(keyValuePairs);
            }
            builder.add("src", html);
            return builder.build().toString();
        }  else {
        	return super.handleRequest(httpExchange);
        }
    }
    
    /**
     * 
     * @param data that contain the request from client side
     * @return the html string that tell which html should the client load
     */
    private String generateNewCsvData(Map<String, String> data)  {
		int type = Integer.parseInt(data.get("vizType"));
		if (type == 1) {
			
		} else if (type == 2) {
			
		} else if (type == 3) {
			webQuery.generateTopNXofYGraph(data);
			return "q2.html";
		} 
		return "q1.html";
	}



	private Map<String, String> parseQueryString(String queryString) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String[] keyValuePairs = queryString.split("&");

        for (String keyValuePair : keyValuePairs) {
            String[] keyAndValue = keyValuePair.split("=");
            builder.put(keyAndValue[0], keyAndValue[1]);
        }

        return builder.build();
    }
}
