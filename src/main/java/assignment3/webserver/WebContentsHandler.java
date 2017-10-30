package assignment3.webserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * The web server handler to handle requests for CSV files within the d3 visualisation code.
 */
public class WebContentsHandler implements HttpHandler {

    private final String root;

    public WebContentsHandler(String root) {
        this.root = root;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        // Get the file requested from the request headers
        URI fileRequested = t.getRequestURI();

        // Proof of concept
        // Try sending the contents of a file as a response instead
        File file = new File(root  + fileRequested.getPath());
        String response = Files.asCharSource(file, Charset.forName("UTF-8")).read();
        sendResponse(response, t);
    }

    private static void sendResponse(String response, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        Logger.getLogger(WebServerHandler.class.toString()).info("" +response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
