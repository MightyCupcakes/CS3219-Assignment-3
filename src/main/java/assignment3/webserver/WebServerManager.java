package assignment3.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

import assignment3.api.API;
import assignment3.api.APIManager;

public class WebServerManager implements WebServer {

    private static final String WEB_ROOT = "docs/";

    private final int port;
    private API api;

    public WebServerManager(int port) {
        this.port = port;
        this.api = new APIManager();
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new WebServerHandler(WEB_ROOT));
            server.createContext("/csv/", new WebContentsHandler(WEB_ROOT));

            server.setExecutor(null); // creates a default executor
            server.start();

            Logger.getLogger(this.getClass().toString()).info("Web server started on port: " + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}