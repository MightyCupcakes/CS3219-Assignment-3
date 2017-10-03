package assignment3.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import assignment3.logic.QueryBuilder;
import assignment3.schema.aggregate.SchemaCountUnique;

public class APITest {

    @Test
    public void test_Question6() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D12")
                .groupBy(ConferenceData.CITATION.year)
                .build();

        //assertEquals("", query.execute());
    }
}
