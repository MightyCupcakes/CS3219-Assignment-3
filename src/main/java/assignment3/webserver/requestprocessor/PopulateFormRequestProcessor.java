package assignment3.webserver.requestprocessor;

import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import assignment3.webserver.WebServerConstants;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

/**
 * A get request from the user's browser to populate certain form fields like
 * type of graphs and premade graphs
 */
@RegisterProcessor( requestType = "populateForm")
public class PopulateFormRequestProcessor implements RequestProcessor {

    @Override
    public String handleRequest(WebRequest keyValuePairs) {
        JsonArrayBuilder builder = Json.createArrayBuilder();

        String formElement = keyValuePairs.getValue("formElement");

        if ("typeOfGraph".equalsIgnoreCase(formElement)) {
            WebServerConstants.TYPES_OF_GRAPH.forEach(builder::add);

        } else if ("columnNames".equalsIgnoreCase(formElement) && keyValuePairs.doesKeyExists("term")) {
            String searchTerm = keyValuePairs.getValue("term").toLowerCase();

            Set<String> matchedColumns = new HashSet<>();

            for(String columnName : WebServerConstants.COLUMNS.keySet()) {
                if (columnName.toLowerCase().contains(searchTerm)) {
                    matchedColumns.add(columnName);
                }
            }

            matchedColumns.forEach(builder::add);
        } else if ("premade_type".equalsIgnoreCase(formElement)) {
            Set<String> categorySet = new HashSet<>();
            WebServerConstants.PREMADE_QUERIES.forEach( info -> categorySet.add(info.category));
            categorySet.forEach(category ->  builder.add(category));      
        } else if ("premade_query".equals(formElement)) {
            String type = keyValuePairs.getValue("type");
            WebServerConstants.PREMADE_QUERIES.stream()
            .filter( info -> info.category.equalsIgnoreCase(type))
            .forEach( info -> builder.add(info.name));

        }

        return builder.build().toString();
    }
}
