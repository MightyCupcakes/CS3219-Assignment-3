package assignment3.schema;

public class SchemaInt extends SchemaComparable<Integer> {

    public SchemaInt(String name) {
        super(name);
    }

    @Override
    public SchemaBase copy() {
        return new SchemaInt(originalNameOfAttribute);
    }
}
