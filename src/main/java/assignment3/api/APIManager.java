package assignment3.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.function.Supplier;

import assignment3.logic.Logic;
import assignment3.logic.LogicManager;
import assignment3.logic.QueryBuilder;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
import assignment3.schema.aggregate.SchemaSum;

public class APIManager implements API{

    private Logic logic;

    public APIManager() {
        logic = new LogicManager();
    }

    @Override
    public void parseConferenceData(String folder) {
        try {
            logic.parseAndSaveRawData(folder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseConferenceJsonData(String file) {
        try {
            logic.parseAndSaveRawJSONData(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runQueries() {

    }

    @Deprecated
    private void assignment3Queries() {
        BufferedWriter writer;

        try {
             writer = new BufferedWriter(new FileWriter("results.txt"));

            executeQueryForQuestion(writer, this::queryForQuestion1, 1);
            executeQueryForQuestion(writer, this::queryForQuestion2, 2);
            executeQueryForQuestion(writer, this::queryForQuestion3, 3);
            executeQueryForQuestion(writer, this::queryForQuestion4, 4);
            executeQueryForQuestion(writer, this::queryForQuestion5, 5);
            executeQueryForQuestion(writer, this::queryForQuestion6, 6);
            executeQueryForQuestion(writer, this::queryForQuestion7, 7);
            executeQueryForQuestion(writer, this::queryForQuestion8, 8);
            executeQueryForQuestion(writer, this::queryForQuestion9, 9);
            executeQueryForQuestion(writer, this::queryForQuestion10, 10);

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void executeQueryForQuestion(BufferedWriter writer, Supplier<String> function, int questionNo) throws Exception {
        writer.write("\n");
        String questionNum = "Question " + questionNo + ": \n";

        writer.write(questionNum.concat(function.get()));

        writer.flush();
    }

    private String queryForQuestion1() {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCount(ConferenceData.TITLE))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();

        return query.execute();
    }

    public String queryForQuestion2() {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCount(ConferenceData.CITATION.title))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();

        return query.execute();
    }

    public String queryForQuestion3() {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();

        return query.execute();
    }

    public String queryForQuestion4() {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaSum(ConferenceData.CITATION.numOfAuthors))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();

        return query.execute();
    }

    public String queryForQuestion5() {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaMin(ConferenceData.CITATION.year), new SchemaMax(ConferenceData.CITATION.year))
                .from("D12", "D13", "D14", "D15", "J14", "W14", "Q14")
                .build();

        return query.execute();
    }
    
    public String queryForQuestion6() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCount(ConferenceData.CITATION.title))
                .from("D12")
                .where(ConferenceData.CITATION.year.greaterThanOrEqualsTo(2000)
                        .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(2015)))
                .groupBy(ConferenceData.CITATION.year)
                .build();

        return query.execute();
    }

    public String queryForQuestion7() {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCount(ConferenceData.CITATION.title))
                .from("D13")
                .where(ConferenceData.CITATION.booktitle.like("CoNLL")) // CoNLL EMNLP
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();

        Query query2 = QueryBuilder.createNewBuilder()
                .select(new SchemaCount(ConferenceData.CITATION.title))
                .from("D13")
                .where(ConferenceData.CITATION.booktitle.like("EMNLP")) // CoNLL EMNLP
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();

        return "CoNLL \n" + query.execute() + "\n EMNLP \n" + query2.execute();
    }

    public String queryForQuestion8() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D12")
                .where(ConferenceData.CITATION.authors.like("Yoshua Bengio")
                        .or(ConferenceData.CITATION.authors.like("Y. Bengio")))
                .groupBy(ConferenceData.CITATION.year)
                .build();

        return query.execute();
    }

    public String queryForQuestion9() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("J14")
                .where(ConferenceData.CITATION.year.greaterThanOrEqualsTo(2010)
                    .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(2015)))
                .groupBy(ConferenceData.CITATION.year)
                .build();

        Query query2 = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("W14")
                .where(ConferenceData.CITATION.year.greaterThanOrEqualsTo(2010)
                        .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(2015)))
                .groupBy(ConferenceData.CITATION.year)
                .build();

        return "J14 \n" + query.execute() + "\n W14 \n" + query2.execute();
    }

    public String queryForQuestion10() {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("Q14")
                .where(ConferenceData.CITATION.booktitle.like("NAACL")
                    .and(ConferenceData.CITATION.year.greaterThanOrEqualsTo(2010)
                            .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(2015))))
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();

        Query query2 = QueryBuilder.createNewBuilder()
                .select(new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D14")
                .where(ConferenceData.CITATION.booktitle.like("NAACL")
                        .and(ConferenceData.CITATION.year.greaterThanOrEqualsTo(2010)
                                .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(2015))))
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();

        return "Q14 \n" + query.execute() + "\n D14 \n" + query2.execute();
    }
}
