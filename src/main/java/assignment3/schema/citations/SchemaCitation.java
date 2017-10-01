package assignment3.schema.citations;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;

public class SchemaCitation extends SchemaBase<SerializedCitation> {

    public static final CitationAttribute<String> title = new CitationAttribute<String>("title");
    public static final CitationAttribute<Integer> year = new CitationAttribute<Integer>("year");
    public static final CitationAttribute<String> authors = new CitationAttribute<String>("authors");
    public static final CitationAttribute<String> booktitle = new CitationAttribute<String>("booktitle");

    private SchemaCitation() {
        super("");
    }

    @Override
    public SerializedCitation getValue(SerializedJournalCitation journalCitation) {
        return journalCitation.citation;
    }
}
