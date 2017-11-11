package assignment3.schema;

import static java.util.Objects.isNull;

import java.util.Objects;
import java.util.function.Predicate;

import assignment3.datarepresentation.SerializedJournalCitation;

public class SchemaPredicate {

    public SchemaBase column;
    public Predicate<SerializedJournalCitation> conditional;

    public static final SchemaPredicate ALWAYS_TRUE = new SchemaPredicate();

    public SchemaPredicate(SchemaBase column, Predicate<SerializedJournalCitation> conditional) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(conditional);

        this.column = column;
        this.conditional = conditional;
    }

    private SchemaPredicate() {
        this.column = null;
        this.conditional = null;
    }

    public boolean test(SerializedJournalCitation serializedJournalCitation) {

        if (isNull(column)) {
            return true;
        }

        Object value = column.getValue(serializedJournalCitation);

        if (value != null) {
            return conditional.test(serializedJournalCitation);
        }

        return false;
    }

    public SchemaPredicate and(SchemaPredicate other) {
        if (!isNull(conditional)) {
            this.conditional = this.conditional.and(other.conditional);
        } else {
            return other;
        }

        return this;
    }

    public SchemaPredicate or(SchemaPredicate other) {
        if (!isNull(conditional)) {
            this.conditional = this.conditional.or(other.conditional);
        }

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
