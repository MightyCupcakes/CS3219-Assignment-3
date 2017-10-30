package assignment3.webserver;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.handler.WebServerHandler;

/**
 * The web server handler to handle requests for CSV files within the d3 visualisation code.
 */
public class WebContentsHandler extends WebServerHandler{

    public WebContentsHandler(String root) {
        super(root);
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        // Get the file requested from the request headers
        URI fileRequested = t.getRequestURI();

        // Proof of concept
        // Try sending the contents of a file as a response instead
        File file = new File(root  + fileRequested.getPath());
        String response = Files.asCharSource(file, Charset.forName("UTF-8")).read();
        sendSuccessResponse(response, t);
    }


}
