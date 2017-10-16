package assignment3.api;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ImmutableList;

import assignment3.api.exceptions.QueryException;
import assignment3.logic.AggregrateQuery;
import assignment3.logic.NormalQuery;
import assignment3.logic.QueryBuilder;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.SchemaString;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.citations.CitationAttribute;
import assignment3.schema.citations.SchemaCitation;


public class QueryBuilderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testNormalQueryBuilder() throws Exception {
        SchemaString author = new SchemaString("author");
        SchemaString title = new SchemaString("title");

        Query query = QueryBuilder.createNewBuilder()
                .select(author, title)
                .from("")
                .where(author.equalsTo("a").and(title.equalsTo("A title")))
                .build();

        Query expectedQuery = new NormalQuery(
                ImmutableList.of(author, title),
                author.equalsTo("a").and(title.equalsTo("A title")),
                ImmutableList.of(""));

        assertTrue(expectedQuery.equals(query));
    }

    @Test
    public void testAggregateQueryBuilder() throws Exception {
        SchemaString author = new SchemaString("author");
        SchemaString title = new SchemaString("title");
        SchemaCount countPapers = new SchemaCount(title);

        Query query = QueryBuilder.createNewBuilder()
                .select(author, countPapers)
                .from("")
                .groupBy(author)
                .build();

        Query expectedQuery = new AggregrateQuery(
                ImmutableList.of(countPapers),
                ImmutableList.of(author),
                SchemaPredicate.ALWAYS_TRUE,
                ImmutableList.of(""),
                ImmutableList.of(author)
        );

        assertTrue(expectedQuery.equals(query));
    }

    @Test
    public void testAggregateQueryBuilder_InvalidAggregates() throws Exception {
        thrown.expect(QueryException.class);

        SchemaString author = new SchemaString("author");
        SchemaString title = new SchemaString("title");
        SchemaCount countPapers = new SchemaCount(title);

        Query query = QueryBuilder.createNewBuilder()
                .select(author, title, countPapers) // title not in group by
                .from("")
                .groupBy(author)
                .build();
    }

    @Test
    public void testCitationQueryBuilder() throws Exception {
        CitationAttribute citationTitle = SchemaCitation.INSTANCE.title;

        Query query = QueryBuilder.createNewBuilder()
                .select(citationTitle)
                .from("")
                .build();

        Query expectedQuery = new NormalQuery(
                ImmutableList.of(citationTitle),
                SchemaPredicate.ALWAYS_TRUE,
                ImmutableList.of("")
        );

        assertTrue(expectedQuery.equals(query));
        assertTrue(((NormalQuery) query).isQueryRetrivingCitations());
    }
}
