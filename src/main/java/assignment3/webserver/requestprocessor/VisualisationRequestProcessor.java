package assignment3.webserver.requestprocessor;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.exceptions.WebServerException;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

/**
 * A GET request that requests for a certain visualisation (such as piechart, bar chart) html file
 */
@RegisterProcessor( requestType = "getVisualisation")
public class VisualisationRequestProcessor implements RequestProcessor {

    private static final String GRAPH_HTML_FOLDER = "d3charts/";

    private WebServerManager manager;

    @Override
    public String handleRequest(WebRequest keyValuePairs) {
        // Create JSON response
        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (manager.getWebQuery().executeAndSaveResultIntoCsvFile(keyValuePairs)) {
            String htmlFile = manager.getWebQuery().getHtmlFileName() + ".html";

            builder.add("src", GRAPH_HTML_FOLDER + htmlFile);
        } else {
            builder.add("src", "");
        }

        return builder.build().toString();
    }

    @Override
    public void setWebManager(WebServerManager manager) {
        this.manager = manager;
    }
}
