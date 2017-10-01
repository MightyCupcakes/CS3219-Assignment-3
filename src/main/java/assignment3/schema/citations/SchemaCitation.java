package assignment3.schema.citations;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;

public class SchemaCitation extends SchemaBase<SerializedCitation> {

    // A citation is considered a seperate table with its own attributes
    // So getting SchemaCitaions is actually performing a table join

    public final CitationAttribute<String> title;
    public final CitationAttribute<Integer> year;
    public final CitationAttribute<String> authors;
    public final CitationAttribute<String> booktitle;

    public static final SchemaCitation citation = new SchemaCitation("");

    public SchemaCitation(String name) {
        super(name);

        this.title = new CitationAttribute<String>("title");
        this.year = new CitationAttribute<Integer>("year");
        this.authors = new CitationAttribute<String>("authors");
        this.booktitle = new CitationAttribute<String>("booktitle");
}

    @Override
    public SerializedCitation getValue(SerializedJournalCitation journalCitation) {
        return journalCitation.citation;
    }
}
