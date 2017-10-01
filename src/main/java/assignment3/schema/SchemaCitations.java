package assignment3.schema;

import java.util.ArrayList;
import java.util.List;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;

public class SchemaCitations extends SchemaBase<List<SerializedJournal>> {

    // A citation is considered a seperate table with its own attributes
    // So getting SchemaCitaions is actually performing a table join

    public final CitationAttribute<SchemaString> title;
    public final SchemaInt year;
    public final SchemaString authors;
    public final SchemaString booktitle;

    public static final SchemaCitations citation = new SchemaCitations("");

    public SchemaCitations(String name) {
        super(name);

        this.title = new CitationAttribute("title");
        this.year = new SchemaInt("year");
        this.authors = new SchemaString("authors");
        this.booktitle = new SchemaString("booktitle");
}

    @Override
    public List<SerializedJournal> getValue(SerializedJournalCitation journalCitation) {
        List<SerializedCitation> citations = journalCitation.journal.citations;

        List<SerializedJournal> citationConvertedToJournal = new ArrayList<>();

        for(SerializedCitation citation : citations) {
            SerializedJournal.Builder builder = new SerializedJournal.Builder();

            builder.withAuthor(citation.authors)
                    .withTitle(citation.title)
                    .withYear(citation.year)
                    .withConference(citation.booktitle);

            citationConvertedToJournal.add(builder.build());
        }

        return citationConvertedToJournal;
    }

    public static class CitationAttribute<T extends SchemaComparable> {

        public CitationAttribute(String name) {

        }
    }
}
