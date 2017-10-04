package assignment3.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.function.Supplier;
import java.util.logging.Logger;

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
            Logger.getLogger(this.getClass().toString())
                    .warning("Something went wrong with parsing raw data in folder: " + folder
                    + "\n Error: " + e.getMessage());
        }
    }

    @Override
    public void runQueries() {
        BufferedWriter writer;

        try {
             writer = new BufferedWriter(new FileWriter("results.txt"));


            writer.write(executeQueryForQuestion(this::queryForQuestion6, 6));
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private String executeQueryForQuestion(Supplier<String> function, int questionNo) {
        String questionNum = "Question " + questionNo + ": \n";

        return questionNum.concat(function.get());
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
                        .and(ConferenceData.CITATION.year.LessThanOrEqualsTo(2015)))
                .groupBy(ConferenceData.CITATION.year)
                .build();

        return query.execute();
    }

    public String queryForQuestion7() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.booktitle, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("D13")
                .where(ConferenceData.CITATION.booktitle.like("EMNLP")
                        .or(ConferenceData.CITATION.booktitle.like("CoNLL")))
                .groupBy(ConferenceData.CITATION.booktitle)
                .build();

        return query.execute();
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
                .groupBy(ConferenceData.CITATION.year)
                .build();

        Query query2 = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.year, new SchemaCountUnique(ConferenceData.CITATION.title))
                .from("W14")
                .groupBy(ConferenceData.CITATION.year)
                .build();

        return query.execute();
    }

    public String queryForQuestion10() {
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

        return query.execute();
    }
}
