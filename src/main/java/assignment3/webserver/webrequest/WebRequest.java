package assignment3.webserver.webrequest;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Represents a GET/POST request from the user's browser
 */
public class WebRequest {

    private Map<String, String> keyValueMap;

    public boolean doesKeyExists(String key) {
        return keyValueMap.containsKey(key);
    }

    public String getValue(String key) {
        return keyValueMap.get(key);
    }

    public void parseQueryString(RequestType type, String queryString) {
        if (RequestType.GET.equals(type)) {
            parseGetRequest(queryString);
        }
    }

    private void parseGetRequest(String queryString) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String[] keyValuePairs = queryString.split("&");

        for (String keyValuePair : keyValuePairs) {
            String[] keyAndValue = keyValuePair.split("=");
            builder.put(keyAndValue[0], keyAndValue[1]);
        }

        keyValueMap = builder.build();
    }

    public enum RequestType {
        GET, POST
    }
}
