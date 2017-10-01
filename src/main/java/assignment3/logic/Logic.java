package assignment3.logic;

import java.io.IOException;

public interface Logic {
    void parseAndSaveRawData(String folder) throws Exception;
    void getDataFromTable(String tableName);
}
