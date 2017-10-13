package assignment3;

import assignment3.api.API;
import assignment3.api.APIManager;

public class MainApp {

    public static void main(String[] args) {
        API api = new APIManager();

        if (args.length > 0 && args[0].equalsIgnoreCase("parse")) {
            api.parseConferenceData(args[1]);
        } else if (args.length > 0 && args[0].equalsIgnoreCase("parseA4")) {
            api.parseConferenceJsonData(args[1]);
        } else {
            api.runQueries();
        }
    }
}
