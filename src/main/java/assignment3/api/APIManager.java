package assignment3.api;

import java.util.logging.Logger;

import assignment3.logic.Logic;
import assignment3.logic.LogicManager;

public class APIManager implements API{

    private Logic logic;

    public APIManager() {
        logic = new LogicManager();
    }

    @Override
    public void parseConferenceData(String folder) {
        try {
            logic.parseAndSaveRawData(folder);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().toString())
                    .warning("Something went wrong with parsing raw data in folder: " + folder
                    + "\n Error: " + e.getMessage());
        }
    }

    @Override
    public String runQueries() {
        return "";
    }
}
