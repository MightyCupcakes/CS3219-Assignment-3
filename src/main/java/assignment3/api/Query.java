package assignment3.api;

public interface Query {

    String EMPTY_JSON = "[{}]";

    /**
     * Executes this query and returns a JSON string representing the
     * results of this query
     * @throws Exception 
     *
     */
    String execute() throws Exception;
}
