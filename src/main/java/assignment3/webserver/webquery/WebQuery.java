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

    enum DisplayOption {
        DISPLAY("Display", base -> base),
        COUNT ("count", SchemaCount::new),
        MIN("min", SchemaMin::new),
        MAX("max", SchemaMax::new);

        public final String stringValue;
        public final Function<SchemaComparable, SchemaBase> converterFunction;

        private static final Stream<DisplayOption> VALUES = Arrays.stream(DisplayOption.values());

        DisplayOption(String stringValue, Function<SchemaComparable, SchemaBase> converterFunction) {
            this.stringValue = stringValue;
            this.converterFunction = converterFunction;
        }

        public static Optional<DisplayOption> getOptionWithStringValue(String value) {
            List<DisplayOption> results = VALUES
                    .filter( displayOption -> displayOption.stringValue.equalsIgnoreCase(value))
                    .collect(Collectors.toList());

            assert results.size() <= 1;

            if (results.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(results.get(0));
            }
        }
    }
}
