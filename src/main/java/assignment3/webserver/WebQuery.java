package assignment3.webserver;

import java.util.Map;

import assignment3.webserver.webrequest.WebRequest;

public interface WebQuery {

    /**
     * Parses the given WebRequest into a Query object.
     * @param query the web query entered by the user
     * @return true if the query is well formed, false otherwise.
     */
    boolean parseWebQuery(WebRequest query);

    /**
     * Executes the last parsed query (if it is well formed) and saves the result into
     * a CSV file with the specified filename
     * @param filename the filename of the CSV file the results should be saved into.
     * @return true if everything went well, false otherwise
     */
    boolean executeAndSaveResultIntoCsvFile(String filename);

    void generateTopNXofYGraph(Map<String, String> data) ;
    void generateTrendGraph(Map<String, String> data) ;
    void generateContemporaryGraph(Map<String, String> data);
    void generateNewGraph(Map<String, String> data);
    String retrieveDataForDropDown(String attribute);
}
