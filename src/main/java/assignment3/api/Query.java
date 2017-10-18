package assignment3.api;

public interface Query {

    String EMPTY_JSON = "[{}]";

    /**
     * Executes this query and returns a JSON string representing the
     * results of this query
     *
     */
    String execute();

    /**
     * Same as execute, but instead of returning the result directly as a JSON string,
     * this will instead save the results into the "dataset" folder as a CSV file.
     * The filename will be in the following format of a string query followed by the date and time
     * the query executed like the following: "query-yyyymmdd-HHmmss.csv"
     *
     */
    void executeAndSaveInCSV();
}
