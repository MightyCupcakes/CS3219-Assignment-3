package assignment3.webserver.handler;

import java.util.Arrays;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.google.common.collect.ImmutableMap;
import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.exceptions.WebServerException;

/**
 * Processes a GET request from the user and response with the appropriate HTML file to
 * display the visualisation
 */
public class GetRequestHandler extends FileRequestHandler {

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        Map<String, String> keyValuePairs = parseQueryString(httpExchange.getRequestURI().getQuery());

        if (!keyValuePairs.containsKey("clientonly")) {
            // Create JSON response
            JsonObjectBuilder builder = Json.createObjectBuilder();
            // TODO: Not hardcode this
            builder.add("src", "q1.html");

            return builder.build().toString();
        } else {
            return super.handleRequest(httpExchange);
        }
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
