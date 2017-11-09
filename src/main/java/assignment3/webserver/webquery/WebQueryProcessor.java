package assignment3.webserver.webquery;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.webrequest.WebRequest;

public interface WebQueryProcessor {

    String DEFAULT_CONFERENCE = "A4";

    /**
     * Parse and processes the given web query represented by the specified web request and
     * saves the result into a CSV file. Returns true if the operation is successful; false otherwise.
     */
    boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest);
}
