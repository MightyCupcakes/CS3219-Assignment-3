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
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
import assignment3.schema.citations.CitationAttribute;

public class SchemaMaxMinTest {
    private List<SerializedJournalCitation> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();

        createDummyJournals(journals);
    }

    @Test
    public void test_SchemaMax() {
        CitationAttribute year = ConferenceData.CITATION.year;
        SchemaAggregate citationYear = new SchemaMax(year);

        for(SerializedJournalCitation serializedJournalCitation: journals) {
            citationYear.accumulate(serializedJournalCitation);
        }

        assertEquals(2016, citationYear.getResult());
    }

    @Test
    public void test_SchemaMin() {
        CitationAttribute year = ConferenceData.CITATION.year;
        SchemaAggregate citationYear = new SchemaMin(year);

        for(SerializedJournalCitation serializedJournalCitation: journals) {
            citationYear.accumulate(serializedJournalCitation);
        }

        assertEquals(2011, citationYear.getResult());
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
                .withYear("2013").withTitle("Hi").withAuthor("cA").withAuthor("cB")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));

        citation = new SerializedCitation.Builder()
                .withYear("2011").withTitle("Hi").withAuthor("cA").withAuthor("cB")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));

        citation = new SerializedCitation.Builder()
                .withYear("2011").withTitle("Hi").withAuthor("cA").withAuthor("cB")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));

        citation = new SerializedCitation.Builder()
                .withYear("2016").withTitle("Hi").withAuthor("cA").withAuthor("cB")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));
    }
}
