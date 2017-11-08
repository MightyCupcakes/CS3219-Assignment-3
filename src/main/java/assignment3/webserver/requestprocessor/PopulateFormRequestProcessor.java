package assignment3.webserver.requestprocessor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        } else if ("columnNames".equalsIgnoreCase(formElement) && keyValuePairs.containsKey("term")) {
            String searchTerm = keyValuePairs.get("term").toLowerCase();

            Set<String> matchedColumns = new HashSet<>();

            for(String columnName : WebServerConstants.COLUMNS.keySet()) {
                if (columnName.toLowerCase().contains(searchTerm)) {
                    matchedColumns.add(columnName);
                }
            }

            matchedColumns.forEach(builder::add);
        }

        return builder.build().toString();
    }
}
