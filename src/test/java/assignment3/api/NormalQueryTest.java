package assignment3.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaInt;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.SchemaString;


public class NormalQueryTest {

    public List<SerializedJournal> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();
        createDummyJournals(journals);
    }

    @Test
    public void testQuery() {
        SchemaString author = new SchemaString("author");
        SchemaString title = new SchemaString("title");

        JsonGenerator json = new JsonGenerator();
        JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

        rowJson.generateJson(author.getNameOfAttribute(), "a");
        rowJson.generateJson(title.getNameOfAttribute(), "A title");
        json.addObjectToArray(rowJson);

        rowJson = new JsonGenerator.JsonGeneratorBuilder();

        rowJson.generateJson(author.getNameOfAttribute(), "a");
        rowJson.generateJson(title.getNameOfAttribute(), "A title 1");
        json.addObjectToArray(rowJson);

        ImmutableList<SchemaComparable> columnsToShow = ImmutableList.of(author, title);
        SchemaPredicate predicate = author.equalsTo("a");

        TestNormalQuery query = new TestNormalQuery(columnsToShow, predicate);
        query.setData(journals);

        assertEquals(json.getJsonString(), query.execute());
    }

    @Test
    public void testQuery2() {
        SchemaString author = new SchemaString("author");
        SchemaString title = new SchemaString("title");
        SchemaInt year = new SchemaInt("yearOfPublication");

        JsonGenerator json = new JsonGenerator();
        JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

        rowJson.generateJson(author.getNameOfAttribute(), "c");
        rowJson.generateJson(title.getNameOfAttribute(), "C title");
        json.addObjectToArray(rowJson);

        ImmutableList<SchemaComparable> columnsToShow = ImmutableList.of(author, title);
        SchemaPredicate predicate = author.equalsTo("c").and(year.equalsTo(2011));

        TestNormalQuery query = new TestNormalQuery(columnsToShow, predicate);
        query.setData(journals);

        assertEquals(json.getJsonString(), query.execute());
    }

    public static void createDummyJournals(List<SerializedJournal> journals) {
        SerializedJournal.Builder builder = new SerializedJournal.Builder();
        builder.withAuthor("a");
        builder.withAbstract("a123");
        builder.withTitle("A title");
        builder.withYear("2012");

        journals.add(builder.build());

        builder = new SerializedJournal.Builder();
        builder.withAuthor("b");
        builder.withAbstract("b123");
        builder.withTitle("B title");
        builder.withYear("2013");

        journals.add(builder.build());

        builder = new SerializedJournal.Builder();
        builder.withAuthor("c");
        builder.withAbstract("c123");
        builder.withTitle("C title");
        builder.withYear("2011");

        journals.add(builder.build());

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a");
        builder.withAbstract("a1234");
        builder.withTitle("A title 1");
        builder.withYear("2013");

        journals.add(builder.build());
    }

    public static class TestNormalQuery extends NormalQuery {
        public TestNormalQuery(List<SchemaComparable> columnsToShow, SchemaPredicate predicate) {
            super (columnsToShow, predicate);
        }

        public void setData(List<SerializedJournal> data) {
            this.journals = data;
        }
    }
}
