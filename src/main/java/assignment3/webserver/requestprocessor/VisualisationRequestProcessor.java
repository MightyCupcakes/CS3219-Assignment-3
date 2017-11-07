package assignment3.webserver.requestprocessor;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import assignment3.webserver.WebQuery;
import assignment3.webserver.WebQueryManager;

/**
 * A GET request that requests for a certain visualisation (such as piechart, bar chart) html file
 */
@RegisterProcessor( requestType = "getVisualisation")
public class VisualisationRequestProcessor implements RequestProcessor {
	WebQuery webQuery = new WebQueryManager();

    @Override
    public String handleRequest(Map<String, String> keyValuePairs) {
        // Create JSON response
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // TODO: Not hardcode this

        String html = "q1.html";
        if (keyValuePairs.containsKey("vizType")) {
        	html = generateNewCsvData(keyValuePairs);
        }
        builder.add("src", html);
        return builder.build().toString();
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

}
