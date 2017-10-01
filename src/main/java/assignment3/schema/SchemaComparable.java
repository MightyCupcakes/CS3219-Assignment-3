package assignment3.schema;

public abstract class SchemaComparable<T extends Comparable> extends SchemaBase<T> {

    public SchemaComparable(String name) {
        super(name);
    }

    public SchemaPredicate isNotNull() {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null);
        });
    }

    public SchemaPredicate greaterThan(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) > 0;
        });
    }

    public SchemaPredicate greaterThanOrEqualsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) >= 0;
        });
    }

    public SchemaPredicate equalsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) == 0;
        });
    }

    public SchemaPredicate LessThanOrEqualsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) <= 0;
        });
    }

    public SchemaPredicate LessThan(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) < 0;
        });
    }
}
