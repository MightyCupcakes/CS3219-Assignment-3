package assignment3.api;

public interface API {

    /**
     * Parses raw conference data for a conference into the system
     * @param folder
     */
    void parseConferenceData(String folder);

    void parseConferenceJsonData(String file);

    /**
     * Gets a new QueryBuilder instance
     */
    APIQueryBuilder getQueryBuilder();

    /**
     * All queries in this method will be executed.
     */
    void runQueries();
}
