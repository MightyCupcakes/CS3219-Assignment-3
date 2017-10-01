package assignment3.schema;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.citations.CitationAttribute;
import assignment3.schema.citations.SchemaCitation;

public class SchemaCitationTest {

    private List<SerializedJournalCitation> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();

        createDummyJournals(journals);
    }

    @Test
    public void testSchemaCitation() {
        CitationAttribute citationsTitle = new SchemaCitation("citations").title;
        CitationAttribute citationsYear = new SchemaCitation("citations").year;

        assertEquals("B 1", citationsTitle.getValue(journals.get(0)));
        assertEquals("c 1", citationsTitle.getValue(journals.get(1)));

        assertEquals(2011, citationsYear.getValue(journals.get(0)));
        assertEquals(2010, citationsYear.getValue(journals.get(1)));
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
                .withAuthor("b").withTitle("B 1").withYear("2011")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));

        citation = new SerializedCitation.Builder()
                .withAuthor("c").withTitle("c 1").withYear("2010")
                .build();

        journals.add(new SerializedJournalCitation(journal, citation));
    }
}
