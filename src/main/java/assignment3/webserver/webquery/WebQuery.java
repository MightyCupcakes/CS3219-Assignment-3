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
