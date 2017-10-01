package assignment3.schema;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.aggregate.SchemaCountUnique;

public class SchemaCountUniqueTest {
    private List<SerializedJournalCitation> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();

        createDummyJournals(journals);
    }

    @Test
    public void countUnique_author() {
        SchemaString author = new SchemaString("author");
        SchemaAggregate countUniqueAuthor = new SchemaCountUnique(author);

        for(SerializedJournalCitation serializedJournalCitation: journals) {
            countUniqueAuthor.accumulate(serializedJournalCitation);
        }

        assertEquals(3, countUniqueAuthor.getResult());
    }

    @Test
    public void countUnique_titles() {
        SchemaString titles = new SchemaString("title");
        SchemaAggregate countUniqueTitles = new SchemaCountUnique(titles);

        for(SerializedJournalCitation serializedJournalCitation: journals) {
            countUniqueTitles.accumulate(serializedJournalCitation);
        }

        assertEquals(5, countUniqueTitles.getResult());
    }

    public static void createDummyJournals(List<SerializedJournalCitation> journals) {
        SerializedCitation.Builder citationBuilder;
        SerializedJournal.Builder journalBuilder;

        SerializedJournal journal;
        SerializedCitation citation;

        journal = new SerializedJournal.Builder()
                .withAuthor("a").withTitle("A title").withAbstract("a123").withYear("2012")
                .build();

        journals.add(new SerializedJournalCitation(journal, null));

        journal = new SerializedJournal.Builder()
                .withAuthor("a").withTitle("A title 2").withAbstract("a123").withYear("2013")
                .build();

        journals.add(new SerializedJournalCitation(journal, null));

        journal = new SerializedJournal.Builder()
                .withAuthor("b").withTitle("A title 3").withAbstract("a123").withYear("2012")
                .build();

        journals.add(new SerializedJournalCitation(journal, null));

        journal = new SerializedJournal.Builder()
                .withAuthor("c").withTitle("A title 4").withAbstract("a123").withYear("2014")
                .build();

        journals.add(new SerializedJournalCitation(journal, null));

        journal = new SerializedJournal.Builder()
                .withAuthor("a").withTitle("A title 5").withAbstract("a123").withYear("2015")
                .build();

        journals.add(new SerializedJournalCitation(journal, null));
    }
}
