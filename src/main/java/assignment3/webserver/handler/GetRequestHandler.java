package assignment3.webserver.handler;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.google.common.collect.ImmutableMap;
import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.exceptions.WebServerException;
import assignment3.webserver.requestprocessor.RequestProcessor;
import assignment3.webserver.requestprocessor.RequestProcessorRegistry;

/**
 * Processes a GET request from the user and response with the appropriate HTML file to
 * display the visualisation
 */
public class GetRequestHandler extends FileRequestHandler {

    private static final RequestProcessorRegistry registry = RequestProcessorRegistry.getInstance();

    public GetRequestHandler(String root) {
        super(root);
    }

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        Map<String, String> keyValuePairs = parseQueryString(httpExchange.getRequestURI().getQuery());

        if (keyValuePairs.containsKey("requestType")) {
            if (registry.getRequestProcessor(keyValuePairs.get("requestType")).isPresent()) {
                Optional<RequestProcessor> processor = registry.getRequestProcessor(keyValuePairs.get("requestType"));
                return processor.get().handleRequest(keyValuePairs);
            } else {
                return super.handleRequest(httpExchange);
            }
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
