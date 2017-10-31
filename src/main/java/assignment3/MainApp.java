package assignment3;

import java.io.IOException;

import assignment3.webserver.WebServer;
import assignment3.webserver.WebServerManager;

public class MainApp {

    public static void main(String[] args) throws IOException {
        WebServer server = new WebServerManager(8001);
        server.start();
    }
}
