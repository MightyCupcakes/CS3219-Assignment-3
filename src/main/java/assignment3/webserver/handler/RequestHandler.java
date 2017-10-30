package assignment3.webserver.handler;

import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.exceptions.WebServerException;

public interface RequestHandler {

    /**
     * Handles the http request and returns the response as a string
     * @param httpExchange the request object
     * @return the string response
     * @throws WebServerException if the request is illegal
     **/
    String handleRequest(HttpExchange httpExchange) throws WebServerException;

    default void logToConsole(String message) {
        Logger.getLogger(this.getClass().toString()).info(message);
    }
}
