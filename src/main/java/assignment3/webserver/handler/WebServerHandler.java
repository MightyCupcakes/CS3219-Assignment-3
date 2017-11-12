package assignment3.webserver.handler;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.exceptions.WebServerException;

public class WebServerHandler implements HttpHandler {

    protected final String root;
    private final WebServerManager manager;

    public WebServerHandler(String root, WebServerManager manager) {
        this.manager = manager;
        this.root = root;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        RequestHandler handler;

        if (isNull(t.getRequestURI().getQuery())) {
            handler = new FileRequestHandler(root);
        } else {
            handler = new GetRequestHandler(root, manager);
        }

        try {
            String response = handler.handleRequest(t);
            sendSuccessResponse(response, t);
        } catch (WebServerException e) {
            sendErrorResponse(e.getMessage(), t);
        }
    }

    protected static void sendSuccessResponse(String response, HttpExchange exchange) throws IOException {
        byte[] responseBytes = response.getBytes();

        exchange.sendResponseHeaders(200, responseBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    protected static void sendErrorResponse(String response, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    protected void logToConsole(String message) {
        Logger.getLogger(this.getClass().toString()).info(message);
    }
}
