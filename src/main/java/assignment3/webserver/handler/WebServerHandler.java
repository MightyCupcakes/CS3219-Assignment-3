package assignment3.webserver.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class WebServerHandler implements HttpHandler {

    protected final String root;

    public WebServerHandler(String root) {
        this.root = root;
    }

    protected static void sendSuccessResponse(String response, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
