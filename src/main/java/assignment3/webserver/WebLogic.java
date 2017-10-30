package assignment3.webserver;

public interface WebLogic {
    void parseAndSaveRawData(String folder) throws Exception;
    void parseAndSaveRawJSONData(String file) throws Exception;
    void saveResultIntoCsv(String jsonStringData) throws Exception;
    
    void parseUserInputIntoQuery(String input) throws Exception;
    
}
