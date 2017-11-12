package assignment3.webserver.webquery;

import static assignment3.webserver.WebServerConstants.PREMADE_QUERIES;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

import assignment3.api.APIQueryBuilder;
import assignment3.api.ConferenceData;
import assignment3.api.Query;
import assignment3.logic.Logic;
import assignment3.logic.LogicManager;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;
import javafx.util.Pair;

@RegisterProcessor( requestType = "Transition Over Time")
public class TransitionOverTimeWebQueryProcessor implements WebQueryProcessor {

    private static final String QUERY_FOR_YEARS = PREMADE_QUERIES.get(0).name;
    private static final String QUERY_FOR_CONFS = PREMADE_QUERIES.get(1).name;
    private static String requiredHtmlFile = "barchart";
    private static final Logic logic = new LogicManager();


    @Override
    public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) throws Exception {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        String premadeType = webRequest.getValue("premadeQuery");
        requiredHtmlFile = premadeType;
        Query query = null;

        if (premadeType.equals(QUERY_FOR_YEARS)) {
            query = getQueryForMultipleYears(builder, webRequest);
            requiredHtmlFile = "LineChart";
        } else if (premadeType.equals(QUERY_FOR_CONFS)) {
            requiredHtmlFile ="MultiLineChart";
        	return queryForTwoConf(manager, webRequest);
            

        }
        query.executeAndSaveInCSV("2");
        return true;
    }

    @Override
    public String getHtmlFileName() {
    	return requiredHtmlFile;
    }

    private static Query getQueryForMultipleYears(APIQueryBuilder builder, WebRequest request) {
        int endYear = getHighestYear(request);
        int startYear = getLowestYear(request);
        String conf = request.getValue("conferenceValue");
        builder = builder.select(ConferenceData.CITATION.year.as("x"),
                new SchemaCountUnique(ConferenceData.CITATION.title).as("y"))
                .from(conf)
                .where(ConferenceData.CITATION.year.greaterThanOrEqualsTo(startYear)
                        .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(endYear)
                                .and(ConferenceData.CITATION.year.isNotNull())))
                .groupBy(ConferenceData.CITATION.year)
                .orderBy(ConferenceData.CITATION.year, APIQueryBuilder.OrderByRule.ASC);

        return builder.build();
    }


    private static boolean queryForTwoConf(WebServerManager manager, WebRequest request) throws Exception {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        APIQueryBuilder builder2 = manager.getAPI().getQueryBuilder();

        String conf = request.getValue("conferenceValue");
        String conf2 = request.getValue("conferenceValue2");
        Query query1 = builder.select(ConferenceData.CITATION.year.as("x"), 
        		new SchemaCountUnique(ConferenceData.CITATION.title).as("y"))
        		.from(conf)
        		.where(ConferenceData.CITATION.year.isNotNull().and(ConferenceData.CITATION.year.greaterThan(0)))
        		.groupBy(ConferenceData.CITATION.year)
        		.orderBy(ConferenceData.CITATION.year, APIQueryBuilder.OrderByRule.ASC).build();
        
        Query query2 = builder2.select(ConferenceData.CITATION.year.as("x"), 
        		new SchemaCountUnique(ConferenceData.CITATION.title).as("z"))
        		.from(conf2)
        		.where(ConferenceData.CITATION.year.isNotNull().and(ConferenceData.CITATION.year.greaterThan(0)))
        		.groupBy(ConferenceData.CITATION.year)
        		.orderBy(ConferenceData.CITATION.year, APIQueryBuilder.OrderByRule.ASC).build();
        
        TreeMap<Integer, Pair<Integer, Integer>> sortedYearMap = new TreeMap<>();
        String result1 = query1.execute();
        String result2 = query2.execute();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonReader jsonReader;
        JsonArray jsonTuples;


        jsonReader = Json.createReader(new StringReader(result1));
        jsonTuples = jsonReader.readArray();

        for (int i =0; i < jsonTuples.size(); i++) {
        	JsonObject jsonTuple = jsonTuples.getJsonObject(i);
        	int year = Integer.parseInt(jsonTuple.getString("x"));
        	int yValue = Integer.parseInt(jsonTuple.getString("y"));
        	Pair<Integer, Integer> pair = new Pair<>(yValue, 0);
        	sortedYearMap.put(year, pair);
        }
        
        jsonReader = Json.createReader(new StringReader(result2));
        jsonTuples = jsonReader.readArray();

        for (int i =0; i < jsonTuples.size(); i++) {

        	JsonObject jsonTuple = jsonTuples.getJsonObject(i);
        	int year = Integer.parseInt(jsonTuple.getString("x"));
        	int zValue = Integer.parseInt(jsonTuple.getString("z"));
        	if (!sortedYearMap.containsKey(year)) {
        		continue;
        	}
        	Pair<Integer, Integer> newPair = new Pair<>(sortedYearMap.get(year).getKey(), zValue);
        	sortedYearMap.put(year, newPair);
        }
        
        Set<Integer> yearSet = sortedYearMap.keySet();
        for(Integer year : yearSet) {
        	JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        	Pair<Integer, Integer> pair = sortedYearMap.get(year);
        	String yValue = Integer.toString(pair.getKey());
        	String zValue =  Integer.toString(pair.getValue());
        	objectBuilder.add("x", Integer.toString(year));
        	objectBuilder.add("y", yValue);
        	objectBuilder.add("z", zValue);
        	arrayBuilder.add(objectBuilder);
        }
        
        String compiledResult = arrayBuilder.build().toString();
        logic.saveResultIntoCsv(compiledResult, "multiLineData");
        return true;
    }

    private static int getHighestYear(WebRequest request) {
        return Math.max(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
    }
    private static int getLowestYear(WebRequest request) {
        return Math.min(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
    }



}
