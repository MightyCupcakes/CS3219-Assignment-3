package assignment3.schema;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import assignment3.datarepresentation.SerializedJournal;

public class SchemaString extends SchemaComparable<String> {

    public SchemaString(String name) {
        super(name);
    }

    public SchemaString(String name, Function<SerializedJournal, Collection<String>> splitter,
        BiFunction<SerializedJournal, String, SerializedJournal> duplicateGenerator) {

        this(name);

        this.splitAttributeIntoRows = true;
        this.splittingFunction = splitter;
        this.splitter = duplicateGenerator;
    }

    public static SerializedJournal createDuplicateJournalCitationAuthor(SerializedJournal journal, String author) {
        SerializedJournal.Builder builder = new SerializedJournal.Builder();

        builder.withTitle(journal.title)
                .withAuthor(author)
                .withAffiliation(journal.affiliation)
                .withAbstract(journal.abstractText)
                .withId(journal.id)
                .withVenue(journal.venue)
                .withYear(journal.yearOfPublication)
                .withInCitationTotal(journal.numOfInCitations);

        return builder.build();
    }

    @Override
    public SchemaBase copy() {
        if (!splitAttributeIntoRows) {
            return new SchemaString(originalNameOfAttribute);
        }

        return new SchemaString(originalNameOfAttribute, splittingFunction, splitter);
    }
}
