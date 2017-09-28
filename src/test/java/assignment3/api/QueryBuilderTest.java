package assignment3.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import assignment3.logic.NormalQuery;
import assignment3.schema.SchemaString;


public class QueryBuilderTest {

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
}
