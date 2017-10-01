package assignment3.schema;

import java.util.Objects;
import java.util.function.Predicate;

import assignment3.datarepresentation.SerializedJournal;

public class SchemaPredicate {

    public SchemaBase column;
    public Predicate<SerializedJournal> conditional;

    public static final SchemaPredicate ALWAYS_TRUE = new SchemaPredicate();

    public SchemaPredicate(SchemaBase column, Predicate<SerializedJournal> conditional) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(conditional);

        this.column = column;
        this.conditional = conditional;
    }

    private SchemaPredicate() {
        this.column = null;
        this.conditional = null;
    }

    public boolean test(SerializedJournal journal) {

        if (Objects.isNull(column)) {
            return true;
        }

        Object value = column.getValue(journal);

        if (value != null) {
            return conditional.test(journal);
        }

        return false;
    }

    public SchemaPredicate and(SchemaPredicate other) {
        this.conditional = this.conditional.and(other.conditional);

        return this;
    }

    public SchemaPredicate or(SchemaPredicate other) {
        this.conditional = this.conditional.or(other.conditional);

        return this;
    }

    public SchemaPredicate copy() {
        return new SchemaPredicate(column, conditional);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SchemaPredicate
                && this.column.equals(((SchemaPredicate) other).column));
    }
}
