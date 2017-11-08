package assignment3.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

import assignment3.api.API;
import assignment3.api.APIManager;
import assignment3.webserver.handler.WebServerHandler;

public class WebServerManager implements WebServer {

    public static final String WEB_ROOT = "docs/";
    public static final Charset UTF8 = Charset.forName("UTF-8");

    private final int port;
    private API api;
    private WebQuery webQuery;

    public WebServerManager(int port) {
        this.port = port;
        this.api = new APIManager();
        this.webQuery = new WebQueryManager(this);
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/", new WebServerHandler(WEB_ROOT, this));
            server.setExecutor(null); // creates a default executor
            server.start();

            Logger.getLogger(this.getClass().toString()).info("Web server started on port: " + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public API getAPI() {
        return api;
    }
}