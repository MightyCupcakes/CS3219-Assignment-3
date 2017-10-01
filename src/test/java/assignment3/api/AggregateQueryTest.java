package assignment3.api;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.logic.AggregrateQuery;
import assignment3.logic.JsonGenerator;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaInt;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.SchemaString;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.aggregate.SchemaCount;

public class AggregateQueryTest {

    public List<SerializedJournalCitation> journals;

    @Before
    public void setUp() {
        journals = new ArrayList<>();
        createDummyJournals(journals);
    }

    @Test
    public void testAggregateQuery_countByAuthor() {
        SchemaString author = new SchemaString("author");
        SchemaString title = new SchemaString("title");
        SchemaCount countPapers = new SchemaCount(title);

        // Get the number of papers written by each author
        TestAggregrateQuery query = new TestAggregrateQuery(
                ImmutableList.of(countPapers),
                ImmutableList.of(author),
                SchemaPredicate.ALWAYS_TRUE,
                ImmutableList.of(""),
                ImmutableList.of(author)
        );

        query.setData(journals);

        assertEquals(getExpectedJsonForTest1(author, countPapers).getJsonString(), query.execute());
    }

    private JsonGenerator getExpectedJsonForTest1(SchemaString author, SchemaAggregate count) {
        Map<String, Integer> expectedNumOfPapersForAuthor = ImmutableMap
                .of("a", 6, "b", 3, "c", 2);

        JsonGenerator json = new JsonGenerator();

        for (Map.Entry<String, Integer> keyValuePair : expectedNumOfPapersForAuthor.entrySet()) {
            JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

            rowJson.generateJson(author.getNameOfAttribute(), keyValuePair.getKey());
            rowJson.generateJson(count.getNameOfAttribute(), keyValuePair.getValue());

            json.addObjectToArray(rowJson);
        }

        return json;
    }

    @Test
    public void testAggregateQuery_countByYear() {
        SchemaInt year = new SchemaInt("yearOfPublication");
        SchemaString title = new SchemaString("title");
        SchemaCount countPapers = new SchemaCount(title);

        // Get the number of papers written by all authors in each year
        TestAggregrateQuery query = new TestAggregrateQuery(
                ImmutableList.of(countPapers),
                ImmutableList.of(year),
                SchemaPredicate.ALWAYS_TRUE,
                ImmutableList.of(""),
                ImmutableList.of(year)
        );

        query.setData(journals);

        assertEquals(getExpectedJsonForTest2(year, countPapers).getJsonString(), query.execute());
    }

    private JsonGenerator getExpectedJsonForTest2(SchemaInt year, SchemaAggregate count) {
        Map<Integer, Integer> expectedNumOfPapersForYear = ImmutableMap
                .of(2011, 1, 2012, 1, 2013, 2, 2014, 3, 2015, 4);

        JsonGenerator json = new JsonGenerator();

        for (Map.Entry<Integer, Integer> keyValuePair : expectedNumOfPapersForYear.entrySet()) {
            JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

            rowJson.generateJson(year.getNameOfAttribute(), keyValuePair.getKey());
            rowJson.generateJson(count.getNameOfAttribute(), keyValuePair.getValue());

            json.addObjectToArray(rowJson);
        }

        return json;
    }

    @Test
    public void testAggregateQuery_NoGroupBy() {
        SchemaString title = new SchemaString("title");
        SchemaCount countPapers = new SchemaCount(title);

        // Get the total number of rows (no group by)
        TestAggregrateQuery query = new TestAggregrateQuery(
                ImmutableList.of(countPapers),
                Collections.emptyList(),
                SchemaPredicate.ALWAYS_TRUE,
                ImmutableList.of(""),
                Collections.emptyList()
        );

        query.setData(journals);

        assertEquals(getExpectedJsonForTest3(countPapers).getJsonString(), query.execute());
    }

    private JsonGenerator getExpectedJsonForTest3(SchemaAggregate count) {

        JsonGenerator json = new JsonGenerator();

        JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();
        rowJson.generateJson(count.getNameOfAttribute(), journals.size());

        json.addObjectToArray(rowJson);

        return json;
    }

    public static void createDummyJournals(List<SerializedJournalCitation> journals) {

        SerializedJournal.Builder builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a123").withTitle("A title").withYear("2012");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12346").withTitle("A title 1").withYear("2013");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12345").withTitle("A title 2").withYear("2014");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12344").withTitle("A title 3").withYear("2015");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12434").withTitle("A title 4").withYear("2015");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12434").withTitle("A title 5").withYear("2015");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("b").withAbstract("b123").withTitle("B title").withYear("2013");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("b").withAbstract("b123e").withTitle("B title 1").withYear("2014");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("b").withAbstract("b123e").withTitle("B title 2").withYear("2014");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("c").withAbstract("c123").withTitle("C title").withYear("2011");
        journals.add(new SerializedJournalCitation(builder.build(), null));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("c").withAbstract("c1d23").withTitle("C title 1").withYear("2015");
        journals.add(new SerializedJournalCitation(builder.build(), null));
    }

    /**
     * A stub class that allows the overriding of the list of journals bypassing the Logic.
     */
    public static class TestAggregrateQuery extends AggregrateQuery {
        public TestAggregrateQuery (List<SchemaAggregate> aggregatecolumnsToShow,
                                   List<SchemaComparable> normalColumnsToShow,
                                   SchemaPredicate predicate,
                                   List<String> tablesToRead,
                                   List<SchemaComparable> groupByColumns) {
            super (aggregatecolumnsToShow, normalColumnsToShow, predicate, tablesToRead, groupByColumns);
        }

        public void setData(List<SerializedJournalCitation> data) {
            this.journals = data;
        }
    }
}
