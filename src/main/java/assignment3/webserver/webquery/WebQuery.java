package assignment3.webserver.webquery;

import assignment3.webserver.webrequest.WebRequest;

public interface WebQuery {

    /**
     * Executes the web query represented by the WebRequest object and saves the result
     * into a CSV file. Returns true if the operation is successful; false otherwise.
     *
     */
    boolean executeAndSaveResultIntoCsvFile(WebRequest query);

    /**
     * Get the name of the html file (without the .html postfix) to display to the user for the
     * web request if the execute operation is successful (that is executeAndSaveResultIntoCsvFile returns true)
     * @return
     */
    String getHtmlFileName();
}
