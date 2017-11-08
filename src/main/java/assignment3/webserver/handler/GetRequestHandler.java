package assignment3.webserver.handler;


import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.WebQuery;
import assignment3.webserver.WebQueryManager;
import assignment3.webserver.exceptions.WebServerException;
import assignment3.webserver.requestprocessor.RequestProcessor;
import assignment3.webserver.requestprocessor.RequestProcessorRegistry;
import assignment3.webserver.webrequest.WebRequest;

/**
 * Processes a GET request from the user and response with the appropriate HTML file to
 * display the visualisation
 */
public class GetRequestHandler extends FileRequestHandler {
	WebQuery webQuery = new WebQueryManager();

    private static final RequestProcessorRegistry registry = RequestProcessorRegistry.getInstance();

    public GetRequestHandler(String root) {
        super(root);
    }

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        WebRequest request = new WebRequest();

        request.parseQueryString(WebRequest.RequestType.GET, httpExchange.getRequestURI().getQuery());

        if (request.doesKeyExists("requestType")) {
            if (registry.getRequestProcessor(request.getValue("requestType")).isPresent()) {
                Optional<RequestProcessor> processor = registry.getRequestProcessor(request.getValue("requestType"));
                return processor.get().handleRequest(request);
            } else {
                return super.handleRequest(httpExchange);
            }
        } else {
            return super.handleRequest(httpExchange);
        }
    }
}
