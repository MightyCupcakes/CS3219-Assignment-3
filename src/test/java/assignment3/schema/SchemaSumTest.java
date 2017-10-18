package assignment3.schema;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import assignment3.api.ConferenceData;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.aggregate.SchemaSum;
import assignment3.schema.citations.CitationAttribute;

public class SchemaSumTest {

    private List<SerializedJournalCitation> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();

        createDummyJournals(journals);
    }

    @Test
    public void test_schemaSum() {
        CitationAttribute author = ConferenceData.CITATION.numOfAuthors;
        SchemaAggregate sumCitationAuthors = new SchemaSum(author);

        for(SerializedJournalCitation serializedJournalCitation: journals) {
            sumCitationAuthors.accumulate(serializedJournalCitation);
        }

        assertEquals(4, sumCitationAuthors.getResult());
    }

    public static void createDummyJournals(List<SerializedJournalCitation> journals) {
        SerializedCitation.Builder citationBuilder;
        SerializedJournal.Builder journalBuilder;

        SerializedJournal journal;
        SerializedCitation citation;

        journal = new SerializedJournal.Builder()
                .withAuthor("a").withTitle("A title").withAbstract("a123").withYear("2012")
                .build();

        citation = new SerializedCitation.Builder()
                .withYear("2012").withTitle("Hi").withAuthor("cA").withAuthor("cB")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));

        citation = new SerializedCitation.Builder()
                .withYear("2012").withTitle("Hi").withAuthor("cA").withAuthor("cB")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));
    }
}
