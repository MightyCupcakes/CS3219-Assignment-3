package assignment3.webserver.webquery;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
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
