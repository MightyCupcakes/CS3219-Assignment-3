package assignment3.api;

public interface API {

    /**
     * Parses raw conference data for a conference into the system
     * @param folder
     */
    void parseConferenceData(String folder);

    void parseConferenceJsonData(String file);

    /**
     * All queries within this method will be executed.
     */
    void runQueries();
}
