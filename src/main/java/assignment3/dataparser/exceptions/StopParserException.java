package assignment3.dataparser.exceptions;

/**
 * Signifies an custom exception based on custom rulesets
 * that causes the data parser to prematurely stop parsing the file.
 */
public class StopParserException extends Exception {
    public StopParserException(String message) {
        super(message);
    }
}
