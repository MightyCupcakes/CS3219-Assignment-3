package assignment3.schema;

import java.util.function.Predicate;

import assignment3.datarepresentation.SerializedJournal;

public class SchemaPredicate {

    private SchemaBase column;
    private Predicate<SerializedJournal> conditional;

    public SchemaPredicate(SchemaBase column, Predicate<SerializedJournal> conditional) {
        this.column = column;
        this.conditional = conditional;
    }

    public boolean test(SerializedJournal journal) {
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
}
