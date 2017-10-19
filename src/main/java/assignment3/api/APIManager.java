package assignment3.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

import assignment3.logic.JsonGenerator;
import assignment3.logic.Logic;
import assignment3.logic.LogicManager;
import assignment3.logic.QueryBuilder;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
import assignment3.schema.aggregate.SchemaSum;

public class APIManager implements API {

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
    	try {
			assignment4Queries();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void assignment4Queries() throws Exception { 
    	String taskOne = queryForTaskOne();
    	String taskTwo = queryForTaskTwo();
    	String taskThree = queryForTaskThree();
    	String taskFour = queryForTaskFour();
    	logic.saveResultIntoCsv(taskFour);
    	String taskFive = queryForTaskFive();
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
    private String queryForTaskOne() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.AUTHOR.as("author"), new SchemaCount(ConferenceData.ID).as("count"))
                .from("A4")
                .where(ConferenceData.VENUE.equalsTo("ArXiv"))
                .groupBy(ConferenceData.AUTHOR)
                .orderBy(new SchemaCount(ConferenceData.ID), APIQueryBuilder.OrderByRule.DESC)
                .limit(10)
                .build();
        query.executeAndSaveInCSV();
        return "";
    }
    
    private String queryForTaskTwo() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.TITLE,ConferenceData.NUM_OF_IN_CITATIONS.as("numOfInCitation"))
                .from("A4")
                .where(ConferenceData.VENUE.equalsTo("ArXiv"))
                .orderBy(ConferenceData.NUM_OF_IN_CITATIONS, APIQueryBuilder.OrderByRule.DESC)
                .limit(5)
                .build();
        query.executeAndSaveInCSV();
        return "";
    }
    
    private String queryForTaskThree() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.YEAR_OF_PUBLICATION, new SchemaCountUnique(ConferenceData.ID).as("count"))
                .from("A4")
                .where(ConferenceData.VENUE.like("ICSE"))
                .groupBy(ConferenceData.YEAR_OF_PUBLICATION)
                .orderBy(ConferenceData.YEAR_OF_PUBLICATION, APIQueryBuilder.OrderByRule.ASC)
                .build();
        query.executeAndSaveInCSV();
        return "";
    }
    
    private String queryForTaskFour() {
    	JsonReader jsonReader;
    	JsonArray jsonTuples;
    	JsonObjectBuilder objectBuilder;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        

    	Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.ID)
                .from("A4")
                .where(ConferenceData.TITLE.equalsTo("Low-density parity check codes over GF(q)"))
                .build();

        
        Query query2 = QueryBuilder.createNewBuilder()
                .select(ConferenceData.ID.as("journalId"),
                        ConferenceData.TITLE.as("journalTitle"),
                		ConferenceData.AUTHORS.as("journalAuthors"),
                        ConferenceData.CITATION.journalId.as("citedJournalId"),
                        ConferenceData.CITATION.title.as("citedJournalTitle"),
                        ConferenceData.CITATION.authors.as("citedJournalAuthors")
                )
                .from("A4")
                .where(ConferenceData.CITATION.journalId.equalsTo("36adf8c327b95bdffe2778bf022e0234d433454a"))
                .build();
        Set<String> citatingJournalIdSet = new HashSet<>();
        jsonReader = Json.createReader(new StringReader(query2.execute()));
        jsonTuples = jsonReader.readArray();
        
        for (int i=0; i<jsonTuples.size(); i++) {
        	JsonObject jsonTuple = jsonTuples.getJsonObject(i);
        	objectBuilder = addNewJsonObject(arrayBuilder, jsonTuple);
        	arrayBuilder.add(objectBuilder);
        	citatingJournalIdSet.add(jsonTuple.getString("journalId"));
        }
        
        Query query3 = QueryBuilder.createNewBuilder()
                .select(ConferenceData.ID.as("journalId"),
                        ConferenceData.TITLE.as("journalTitle"),
                        ConferenceData.AUTHORS.as("journalAuthors"),
                        ConferenceData.CITATION.journalId.as("citedJournalId"),
                        ConferenceData.CITATION.title.as("citedJournalTitle"),
                        ConferenceData.CITATION.authors.as("citedJournalAuthors")
                )
                .from("A4")
                .where(ConferenceData.CITATION.journalId.in(citatingJournalIdSet))
                .build();
        
        jsonReader = Json.createReader(new StringReader(query3.execute()));
        jsonTuples = jsonReader.readArray();
        for (int i=0; i<jsonTuples.size(); i++) {
        	JsonObject jsonTuple = jsonTuples.getJsonObject(i);
        	objectBuilder = addNewJsonObject(arrayBuilder, jsonTuple);
        	arrayBuilder.add(objectBuilder);
        }        
        
        return arrayBuilder.build().toString();
    }
    private JsonObjectBuilder addNewJsonObject(JsonArrayBuilder builder, JsonObject jsonTuple) {
    	JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    	for (Entry<String, JsonValue> entry : jsonTuple.entrySet()) {
    		String value = entry.getValue().toString();

    		objectBuilder.add(entry.getKey(), value);
    	}
    	return objectBuilder;
    }
    
    //get the number of publication in ArXiv for the past 6 year
    private String queryForTaskFive() {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.YEAR_OF_PUBLICATION, new SchemaCount(ConferenceData.ID).as("count"))
                .from("A4")
                .where(ConferenceData.VENUE.equalsTo("ArXiv"))
                .groupBy(ConferenceData.YEAR_OF_PUBLICATION)
                .orderBy(ConferenceData.YEAR_OF_PUBLICATION, APIQueryBuilder.OrderByRule.DESC)
                .limit(6)
                .build();
        return query.execute();
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
