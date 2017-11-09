package assignment3.webserver.requestprocessor;

import assignment3.webserver.webrequest.WebRequest;

public interface RequestProcessor {
    /**
     * Handles the request represented by the key-value pairs and returns a JSON string representing
     * the response to be sent back to the user browser
     */
    String handleRequest(WebRequest keyValuePairs);
}
