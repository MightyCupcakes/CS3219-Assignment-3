package assignment3.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class WebServer {
    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/test", new WebServerHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}