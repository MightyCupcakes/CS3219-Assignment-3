package assignment3.webserver.requestprocessor;

import java.util.Map;

public interface RequestProcessor {
    /**
     * Handles the request represented by the key-value pairs and returns a JSON string representing
     * the response to be sent back to the user browser
     */
    String handleRequest(Map<String, String> keyValuePairs);
}
