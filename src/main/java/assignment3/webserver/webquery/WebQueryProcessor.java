package assignment3.webserver.webquery;

import assignment3.api.exceptions.QueryException;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.webrequest.WebRequest;

public interface WebQueryProcessor {

    String DEFAULT_CONFERENCE = "A4";

    /**
     * Parse and processes the given web query represented by the specified web request and
     * saves the result into a CSV file. Returns true if the operation is successful; false otherwise.
     * @throws Exception 
     * @throws QueryException 
     */
    boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) throws QueryException, Exception;

    /**
     * Get the name of the html file (without the .html postfix) to display to the user for the
     * web request if the execute operation is successful (that is processAndSaveIntoCSV returns true)
     * @return
     */
    String getHtmlFileName();
}
