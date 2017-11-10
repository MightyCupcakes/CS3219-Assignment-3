package assignment3.schema;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import assignment3.api.Query;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.logic.JsonGenerator;
import assignment3.schema.citations.CitationAttribute;

public class SchemaComparableTest {

    private static SerializedJournal journal;

    @Before
    public void setUp() {
        SerializedJournal.Builder builder = new SerializedJournal.Builder();
        SerializedCitation.Builder cBuilder = new SerializedCitation.Builder();

        List<SerializedCitation> citationList = new ArrayList<>();

        builder.withTitle("Test title").withAuthor("A test author").withYear(2000);

        cBuilder.withJournalId("AA1");
        citationList.add(cBuilder.build());

        cBuilder = new SerializedCitation.Builder();
        cBuilder.withJournalId("AA2");
        citationList.add(cBuilder.build());

        builder.withCitations(citationList);

        journal = builder.build();
    }

    @Test
    public void test_SchemaComparable_in() {
        Set<String> stringSet = new HashSet<>();
        stringSet.add("AA1");
        stringSet.add("BB1");

        CitationAttribute<String> ids = new CitationAttribute<String>("journalId");
        SchemaPredicate predicate = ids.in(stringSet);

        SerializedJournalCitation journalCitation1 = new SerializedJournalCitation(journal, journal.citations.get(0));
        SerializedJournalCitation journalCitation2 = new SerializedJournalCitation(journal, journal.citations.get(1));

        assertTrue(predicate.test(journalCitation1));
        assertFalse(predicate.test(journalCitation2));
    }

    @Test
    public void test_SchemaComparable_inSubQuery() {
        CitationAttribute<String> ids = new CitationAttribute<String>("journalId");

        Query query = mock(Query.class);

        JsonGenerator json = new JsonGenerator();

        JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();
        rowJson.generateJson(ids.getNameOfAttribute(), "AA1");

        json.addObjectToArray(rowJson);

        when(query.execute()).thenReturn(json.getJsonString());

        SchemaPredicate predicate = ids.in(query);

        SerializedJournalCitation journalCitation1 = new SerializedJournalCitation(journal, journal.citations.get(0));
        SerializedJournalCitation journalCitation2 = new SerializedJournalCitation(journal, journal.citations.get(1));

        assertTrue(predicate.test(journalCitation1));
        assertFalse(predicate.test(journalCitation2));
    }
}
