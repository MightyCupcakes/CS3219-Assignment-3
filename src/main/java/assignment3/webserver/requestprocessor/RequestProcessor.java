package assignment3.webserver.requestprocessor;

import java.util.Map;

public interface RequestProcessor {

    String handleRequest(Map<String, String> keyValuePairs);
}
