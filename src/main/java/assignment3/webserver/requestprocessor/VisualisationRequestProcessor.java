package assignment3.webserver.requestprocessor;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import assignment3.webserver.webrequest.WebRequest;

/**
 * A GET request that requests for a certain visualisation (such as piechart, bar chart) html file
 */
@RegisterProcessor( requestType = "getVisualisation")
public class VisualisationRequestProcessor implements RequestProcessor {

    @Override
    public String handleRequest(WebRequest keyValuePairs) {
        // Create JSON response
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // TODO: Not hardcode this

        String html = "q1.html";
        if (keyValuePairs.doesKeyExists("vizType")) {
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
    private String generateNewCsvData(WebRequest data)  {
		int type = Integer.parseInt(data.getValue("vizType"));

		if (type == 1) {
			
		} else if (type == 2) {
			
		} else if (type == 3) {
			//webQuery.generateTopNXofYGraph(data);
			return "q2.html";
		} 
		return "q1.html";
	}

}
