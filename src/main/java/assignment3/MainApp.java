package assignment3;

import java.io.IOException;

import assignment3.api.API;
import assignment3.api.APIManager;
import assignment3.webserver.WebServer;
import assignment3.webserver.WebServerManager;

public class MainApp {

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equalsIgnoreCase("runquery")) {
            API api = new APIManager();
            api.runQueries();
        } else {
            WebServer server = new WebServerManager(8003);
            server.start();
        }
    }
}
