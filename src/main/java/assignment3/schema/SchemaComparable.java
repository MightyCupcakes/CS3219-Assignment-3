package assignment3.schema;

import static java.util.Objects.isNull;

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

    public SchemaPredicate like(String value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            if (isNull(v)) return false;

            return v.toString().contains(value);
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

    public SchemaPredicate lessThanOrEqualsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) <= 0;
        });
    }

    public SchemaPredicate lessThan(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) < 0;
        });
    }
}
