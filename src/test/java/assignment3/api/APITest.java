package assignment3.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import assignment3.logic.QueryBuilder;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.schema.aggregate.SchemaSum;

public class APITest {

    @Test
    public void test_Question1() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCount(ConferenceData.TITLE))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();
    }

    @Test
    public void test_Question2() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCount(ConferenceData.CITATION.title))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();
    }

    @Test
    public void test_Question3() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();
    }

    @Test
    public void test_Question4() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaSum(ConferenceData.CITATION.numOfAuthors))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();
    }

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
