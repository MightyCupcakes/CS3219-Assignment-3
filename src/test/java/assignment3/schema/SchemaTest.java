package assignment3.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import assignment3.api.ConferenceData;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;

public class SchemaTest {

    public List<SerializedJournalCitation> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();

        createDummyJournals(journals);
    }

    @Test
    public void testSchema() {
        SchemaString author = new SchemaString("author");
        SchemaBase authors = ConferenceData.AUTHOR.as("author");
        SchemaString title = new SchemaString("title");
        SchemaBase year = ConferenceData.YEAR_OF_PUBLICATION.as("year");
        year.as("year");

        Set<SchemaBase> set = new HashSet<>();
        set.add(ConferenceData.AUTHOR);

        assertTrue(year instanceof SchemaComparable);

        assertEquals("a", author.getValue(journals.get(0)));
        assertEquals("A title", title.getValue(journals.get(0)));
        assertEquals(new Integer(2012), year.getValue(journals.get(0)));
        assertEquals("C title", title.getValue(journals.get(2)));
        assertEquals(new Integer(2011), year.getValue(journals.get(2)));

        assertEquals("year", year.getNameOfAttribute());
        assertEquals("yearOfPublication", year.originalNameOfAttribute);
        assertTrue(year != ConferenceData.YEAR_OF_PUBLICATION);
        assertTrue(set.contains(author));
        assertEquals(ConferenceData.YEAR_OF_PUBLICATION.getNameOfAttribute(), ConferenceData.YEAR_OF_PUBLICATION.originalNameOfAttribute);
    }

    @Test
    public void testSchemaComparing() {
        SchemaString author = new SchemaString("author");
        SchemaInt year = new SchemaInt("yearOfPublication");

        SchemaPredicate predicate = author.equalsTo("a").or(author.equalsTo("c"));

        assertTrue(predicate.test(journals.get(0)));
        assertTrue(predicate.test(journals.get(2)));

        predicate = author.equalsTo("a").and(author.equalsTo("c"));

        assertFalse(predicate.test(journals.get(0)));
        assertFalse(predicate.test(journals.get(2)));

        SchemaString title = new SchemaString("title");

        predicate = author.equalsTo("a").and(year.equalsTo(2012)).and(title.equalsTo("A title"));

        assertTrue(predicate.test(journals.get(0)));
        assertFalse(predicate.test(journals.get(2)));
    }

    public static void createDummyJournals(List<SerializedJournalCitation> journals) {
        SerializedJournal.Builder builder = new SerializedJournal.Builder();
        builder.withAuthor("a");
        builder.withAbstract("a123");
        builder.withTitle("A title");
        builder.withYear("2012");

        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("b");
        builder.withAbstract("b123");
        builder.withTitle("B title");
        builder.withYear("2013");

        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("c");
        builder.withAbstract("c123");
        builder.withTitle("C title");
        builder.withYear("2011");

        journals.add(new SerializedJournalCitation(builder.build(), null));
    }
}
