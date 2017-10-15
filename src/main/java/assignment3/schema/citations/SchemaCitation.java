package assignment3.schema.citations;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;

public class SchemaCitation extends SchemaBase<SerializedCitation> {

    public final CitationAttribute<String> title = new CitationAttribute<String>("title");
    public final CitationAttribute<Integer> year = new CitationAttribute<Integer>("year");
    public final CitationAttribute<String> authors = new CitationAttribute<String>("authors");
    public final CitationAttribute<String> booktitle = new CitationAttribute<String>("booktitle");
    public final CitationAttribute<String> journalId = new CitationAttribute<String>("journalId");
    public final CitationAttribute<String> citationId = new CitationAttribute<String>("citationId");
    public final CitationAttribute<Integer> numOfAuthors = new CitationAttribute<>("numOfAuthors");

    public static final SchemaCitation INSTANCE = new SchemaCitation();

    private SchemaCitation() {
        super("");
    }

    @Override
    public SerializedCitation getValue(SerializedJournalCitation journalCitation) {
        return journalCitation.citation;
    }
}
