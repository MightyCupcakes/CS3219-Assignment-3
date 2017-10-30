package assignment3.webserver.handler;

import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.exceptions.WebServerException;

/**
 * Processes a GET request from the user and response with the appropriate HTML file to
 * display the visualisation
 */
public class GetRequestHandler implements RequestHandler {

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        String queryString = httpExchange.getRequestURI().getQuery();

        // TODO: Not hardcode this
        String response = "q1.html";
        return response;
    }
}
