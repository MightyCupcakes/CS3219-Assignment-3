package assignment3.webserver.handler;


import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.exceptions.WebServerException;
import assignment3.webserver.registry.WebServerRegistry;
import assignment3.webserver.requestprocessor.RequestProcessor;
import assignment3.webserver.webrequest.WebRequest;

/**
 * Processes a GET request from the user and response with the appropriate HTML file to
 * display the visualisation
 */
public class GetRequestHandler extends FileRequestHandler {

    private static final WebServerRegistry<RequestProcessor> registry =
            new WebServerRegistry<>(RequestProcessor.class.getPackage().getName());

    private final WebServerManager manager;

    public GetRequestHandler(String root, WebServerManager manager) {
        super(root);

        this.manager = manager;
    }

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        WebRequest request = new WebRequest();

        request.parseQueryString(WebRequest.RequestType.GET, httpExchange.getRequestURI().getQuery());
        if (request.doesKeyExists("requestType")) {
            if (registry.getHandler(request.getValue("requestType")).isPresent()) {
                Optional<RequestProcessor> processor = registry.getHandler(request.getValue("requestType"));

                if (processor.isPresent()) {
                    RequestProcessor requestProcessor = processor.get();

                    requestProcessor.setWebManager(manager);
                    return requestProcessor.handleRequest(request);
                }

                return super.handleRequest(httpExchange);
            } else {
                return super.handleRequest(httpExchange);
            }
        } else {
            return super.handleRequest(httpExchange);
        }
    }
}
