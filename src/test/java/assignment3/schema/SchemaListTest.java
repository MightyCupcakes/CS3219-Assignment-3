package assignment3.schema;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public class SchemaListTest {

    private List<SerializedJournal> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();

        createDummyJournals(journals);
    }

    @Test
    public void testSchemaList() {
       // SchemaList citations = new SchemaList("citations");
    }

    public static void createDummyJournals(List<SerializedJournal> journals) {
        SerializedCitation.Builder citationBuilder;
        SerializedJournal.Builder journalBuilder;

        SerializedJournal journal;
        SerializedCitation citation;

        List<SerializedCitation> citations = new ArrayList<>();

        citation = new SerializedCitation.Builder()
                .withAuthor("b").withTitle("B 1").withYear("2011")
                .build();
        citations.add(citation);

        citation = new SerializedCitation.Builder()
                .withAuthor("c").withTitle("c 1").withYear("2010")
                .build();
        citations.add(citation);

        journal = new SerializedJournal.Builder()
                .withAuthor("a").withTitle("A title").withAbstract("a123").withYear("2012").withCitations(citations)
                .build();
    }
}
