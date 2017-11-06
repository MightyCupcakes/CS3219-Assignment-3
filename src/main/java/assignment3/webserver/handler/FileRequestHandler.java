package assignment3.webserver.handler;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.exceptions.WebServerException;

public class FileRequestHandler implements RequestHandler {

    private final String root;

    public FileRequestHandler(String root) {
        this.root = root;
    }

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        String fileRequested = httpExchange.getRequestURI().getPath();

        if ("/".equals(fileRequested)) {
            logToConsole("No file requested, sending main.html");
            fileRequested = "main.html";
        } else {
            logToConsole("File requested:" + fileRequested);
        }

        File file = new File(root  + fileRequested);

        if (file.isDirectory() || !file.exists()) {
            throw new WebServerException(fileRequested + " not found in server");
        }

        try {
            String response = Files.asCharSource(file, WebServerManager.UTF8).read();
            return response;
        } catch (IOException e) {
            throw new WebServerException("Something went wrong while reading the requested file. File requested: " + fileRequested);
        }
    }
}
