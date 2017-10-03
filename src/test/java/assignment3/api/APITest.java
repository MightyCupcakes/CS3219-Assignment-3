package assignment3.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import assignment3.logic.QueryBuilder;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
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
    public void test_Question5() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaMin(ConferenceData.CITATION.year), new SchemaMax(ConferenceData.CITATION.year))
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

    @Test
    public void test_Question7() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.booktitle, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D13")
                .where(ConferenceData.CITATION.booktitle.like("EMNLP")
                        .or(ConferenceData.CITATION.booktitle.like("CoNLL")))
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();
    }

    @Test
    public void test_Question8() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D12")
                .where(ConferenceData.CITATION.authors.like("Yoshua Bengio")
                        .or(ConferenceData.CITATION.authors.like("Y. Bengio")))
                .groupBy(ConferenceData.CITATION.year)
                .build();
    }

    @Test
    public void test_Question9() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("J14")
                .groupBy(ConferenceData.CITATION.year)
                .build();

        Query query2 = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("W14")
                .groupBy(ConferenceData.CITATION.year)
                .build();
    }

    @Test
    public void test_Question10() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("Q14")
                .where(ConferenceData.CITATION.booktitle.like("NAACL"))
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();

        Query query2 = QueryBuilder.createNewBuilder()
                .select(new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D14")
                .where(ConferenceData.CITATION.booktitle.like("NAACL"))
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();
    }
}
