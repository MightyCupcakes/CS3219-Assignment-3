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

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.logic.JoinTableQuery;
import assignment3.logic.JsonGenerator;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.schema.citations.CitationAttribute;
import assignment3.schema.citations.SchemaCitation;

public class JoinTableQueryTest {

    public List<SerializedJournalCitation> journals;
    public List<SerializedCitation> citationsList;

    @Before
    public void setUp() {
        journals = new ArrayList<>();
        citationsList = new ArrayList<>();
        createDummyJournals(journals, citationsList);
    }

    @Test
    public void test_JoinTableQuery() {
        CitationAttribute title = SchemaCitation.title;
        SchemaAggregate citationsCount = new SchemaCount(title);

        TestTableJoinQuery query = new TestTableJoinQuery(
                ImmutableList.of(citationsCount),
                Collections.emptyList(),
                title.isNotNull(),
                ImmutableList.of(""),
                Collections.emptyList()
        );

        query.setData(journals);
        assertEquals(getExpectedJsonForTestCount(citationsCount, 7).getJsonString(), query.execute());
    }

    private JsonGenerator getExpectedJsonForTestCount(SchemaAggregate count, int value) {

        JsonGenerator json = new JsonGenerator();

        JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();
        rowJson.generateJson(count.getNameOfAttribute(), value);

        json.addObjectToArray(rowJson);

        return json;
    }

    @Test
    public void test_JoinTableQuery_GroupBy() {
        CitationAttribute title = SchemaCitation.title;
        CitationAttribute year = SchemaCitation.year;
        SchemaAggregate citationsCount = new SchemaCount(title);

        TestTableJoinQuery query = new TestTableJoinQuery(
                ImmutableList.of(citationsCount),
                ImmutableList.of(year),
                title.isNotNull(),
                ImmutableList.of(""),
                ImmutableList.of(year)
        );

        query.setData(journals);
        assertEquals(getExpectedJsonForTest2(citationsCount, year).getJsonString(), query.execute());
    }

    private JsonGenerator getExpectedJsonForTest2(SchemaAggregate count, CitationAttribute year) {

        Map<Integer, Integer> expectedNumOfCitationsForYear = ImmutableMap
                .of(2011, 1, 2012, 5, 2013, 1);

        JsonGenerator json = new JsonGenerator();

        for (Map.Entry<Integer, Integer> keyValuePair : expectedNumOfCitationsForYear.entrySet()) {
            JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

            rowJson.generateJson(year.getNameOfAttribute(), keyValuePair.getKey());
            rowJson.generateJson(count.getNameOfAttribute(), keyValuePair.getValue());

            json.addObjectToArray(rowJson);
        }

        return json;
    }

    @Test
    public void test_JoinTableQuery_CountUnique() {
        CitationAttribute title = SchemaCitation.title;
        SchemaAggregate uniqueCitationsCount = new SchemaCountUnique(title);

        TestTableJoinQuery query = new TestTableJoinQuery(
                ImmutableList.of(uniqueCitationsCount),
                Collections.emptyList(),
                title.isNotNull(),
                ImmutableList.of(""),
                Collections.emptyList()
        );

        query.setData(journals);
        assertEquals(getExpectedJsonForTestCount(uniqueCitationsCount, 6).getJsonString(), query.execute());
    }

    @Test
    public void test_JoinTableQuery_CountAuthor() {
        CitationAttribute title = SchemaCitation.title;
        CitationAttribute author = SchemaCitation.authors;
        SchemaAggregate uniqueCitationsCount = new SchemaCountUnique(title);

        TestTableJoinQuery query = new TestTableJoinQuery(
                ImmutableList.of(uniqueCitationsCount),
                Collections.emptyList(),
                author.like("cA"),
                ImmutableList.of(""),
                Collections.emptyList()
        );

        query.setData(journals);
        assertEquals(getExpectedJsonForTestCount(uniqueCitationsCount, 2).getJsonString(), query.execute());
    }

    public static void createDummyJournals(List<SerializedJournalCitation> journals, List<SerializedCitation> citationsList) {

        createDummyCitations(citationsList);

        SerializedJournal.Builder builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a123").withTitle("A title").withYear("2012");
        journals.add(new SerializedJournalCitation(builder.build(), citationsList.get(1)));
        journals.add(new SerializedJournalCitation(builder.build(), citationsList.get(0)));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12346").withTitle("A title 1").withYear("2013");
        journals.add(new SerializedJournalCitation(builder.build(), citationsList.get(2)));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12345").withTitle("A title 2").withYear("2014");
        journals.add(new SerializedJournalCitation(builder.build(), citationsList.get(0)));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12344").withTitle("A title 3").withYear("2015");
        journals.add(new SerializedJournalCitation(builder.build(), citationsList.get(3)));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12434").withTitle("A title 4").withYear("2015");
        journals.add(new SerializedJournalCitation(builder.build(), citationsList.get(4)));

        builder = new SerializedJournal.Builder();
        builder.withAuthor("a").withAbstract("a12434").withTitle("A title 5").withYear("2015");
        journals.add(new SerializedJournalCitation(builder.build(), citationsList.get(5)));

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

    private static void createDummyCitations(List<SerializedCitation> citationsList) {
        SerializedCitation citation;

        citation = new SerializedCitation.Builder()
                .withAuthor("cA").withAuthor("cB").withTitle("Interesting title").withYear("2012")
                .build();
        citationsList.add(citation);

        citation = new SerializedCitation.Builder()
                .withAuthor("cC").withTitle("Interesting title 1").withYear("2012")
                .build();
        citationsList.add(citation);

        citation = new SerializedCitation.Builder()
                .withAuthor("cD").withAuthor("cB").withTitle("Interesting title 3").withYear("2012")
                .build();
        citationsList.add(citation);

        citation = new SerializedCitation.Builder()
                .withAuthor("cE").withAuthor("cF").withTitle("Interesting title 5").withYear("2012")
                .build();
        citationsList.add(citation);

        citation = new SerializedCitation.Builder()
                .withAuthor("cF").withAuthor("cA").withTitle("Interesting title 4").withYear("2013")
                .build();
        citationsList.add(citation);

        citation = new SerializedCitation.Builder()
                .withAuthor("cG").withAuthor("cK").withTitle("Interesting title 6").withYear("2011")
                .build();
        citationsList.add(citation);
    }

    /**
     * A stub class that allows the overriding of the list of journals bypassing the Logic.
     */
    public static class TestTableJoinQuery extends JoinTableQuery {
        public TestTableJoinQuery (List<SchemaAggregate> aggregatecolumnsToShow,
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
