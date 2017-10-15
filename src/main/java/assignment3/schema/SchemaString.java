package assignment3.schema;

import java.util.Collection;
import java.util.function.Function;

public class SchemaString extends SchemaComparable<String> {

    public SchemaString(String name) {
        super(name);
    }

    public SchemaString(String name, Function<String, Collection<String>> splitter) {
        this(name);

        this.splitAttributeIntoRows = true;
        this.splittingFunction = splitter;
    }
}
