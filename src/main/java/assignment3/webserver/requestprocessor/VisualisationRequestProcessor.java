package assignment3.webserver.requestprocessor;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * A GET request that requests for a certain visualisation (such as piechart, bar chart) html file
 */
@RegisterProcessor( requestType = "getVisualisation")
public class VisualisationRequestProcessor implements RequestProcessor {

    @Override
    public String handleRequest(Map<String, String> keyValuePairs) {
        // Create JSON response
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // TODO: Not hardcode this
        builder.add("src", "q1.html");

        return builder.build().toString();
    }
}
