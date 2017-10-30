package assignment3.webserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.handler.WebServerHandler;

/**
 * The web server handler to handle requests for d3 visualisation code.
 */
public class WebPageHandler extends WebServerHandler {

    public WebPageHandler(String root) {
        super(root);
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        // Proof of concept
        // Try sending the contents of a file as a response instead
        File file = new File(root + "q1.html");
        String response = Files.asCharSource(file, Charset.forName("UTF-8")).read();
        sendSuccessResponse(response, t);
    }
}
