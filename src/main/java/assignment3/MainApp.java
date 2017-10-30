package assignment3;

import java.io.IOException;

import assignment3.api.API;
import assignment3.api.APIManager;
import assignment3.webserver.WebServer;

public class MainApp {

    public static void main(String[] args) throws IOException {
        WebServer server = new WebServer();
        server.start();
    }
}
