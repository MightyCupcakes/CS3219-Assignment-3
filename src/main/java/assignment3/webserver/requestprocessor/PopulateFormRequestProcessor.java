package assignment3.webserver.requestprocessor;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import assignment3.webserver.WebServerConstants;

/**
 * A get request from the user's browser to populate certain form fields like
 * type of graphs and premade graphs
 */
@RegisterProcessor( requestType = "populateForm")
public class PopulateFormRequestProcessor implements RequestProcessor {

    @Override
    public String handleRequest(Map<String, String> keyValuePairs) {
        JsonArrayBuilder builder = Json.createArrayBuilder();

        String formElement = keyValuePairs.get("formElement");

        if ("typeOfGraph".equalsIgnoreCase(formElement)) {
            WebServerConstants.TYPES_OF_GRAPH.forEach(builder::add);

        } else if ("PremadeQueries".equalsIgnoreCase(formElement) && keyValuePairs.containsKey("premadeVisuals")) {
            WebServerConstants.PREMADE_QUERIES.get(keyValuePairs.get("premadeVisuals")).forEach(builder::add);

        } else if ("PremadeVisuals".equalsIgnoreCase(formElement)) {
            WebServerConstants.TYPES_OF_PREMADE_VISUALS.forEach(builder::add);
        }

        return builder.build().toString();
    }
}
