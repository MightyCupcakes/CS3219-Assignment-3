package assignment3.webserver.webquery;

import static assignment3.webserver.WebServerConstants.PREMADE_QUERIES;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Transition Over Time")
public class TransitionOverTimeWebQueryProcessor implements WebQueryProcessor {

    private static final String QUERY_FOR_YEARS = PREMADE_QUERIES.get(0).name;
    private static final String QUERY_FOR_CONFS = PREMADE_QUERIES.get(1).name;

    private static final Logic logic = new LogicManager();

    @Override
    public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        String premadeType = webRequest.getValue("premadeQuery");

        Query query;

        if (premadeType.equals(QUERY_FOR_YEARS)) {
            query = getQueryForMultipleYears(builder, webRequest);
            query.executeAndSaveInCSV("1");
        } else if (premadeType.equals(QUERY_FOR_CONFS)) {
            getQueryForMultipleConfs(manager, webRequest);
            return true;
        }

        return true;
    }

    @Override
    public String getHtmlFileName() {
        return "BarChart";
    }

    private static Query getQueryForMultipleYears(APIQueryBuilder builder, WebRequest request) {
        int endYear = getHighestYear(request);
        int startYear = getLowestYear(request);
        String conf = request.getValue("conferenceValue");
        System.out.println(endYear + " " + startYear + " " + conf);
        builder = builder.select(ConferenceData.CITATION.year.as("x"),
                new SchemaCount(ConferenceData.CITATION.title).as("y"))
                .from(conf)
                .where(ConferenceData.CITATION.year.greaterThanOrEqualsTo(startYear)
                        .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(endYear)
                                .and(ConferenceData.CITATION.year.isNotNull())))
                .groupBy(ConferenceData.CITATION.year)
                .orderBy(ConferenceData.CITATION.year, APIQueryBuilder.OrderByRule.ASC);

        return builder.build();
    }
    private static void getQueryForMultipleConfs(WebServerManager manager, WebRequest request) {
        String conferenceValues = request.getValue("conferenceValue");
        int year = Integer.parseInt(request.getValue("startYearDate"));
        List<String> confList = Arrays.asList(conferenceValues.split(","));
        Map<String, String> confResultMap = new HashMap<>();
        for (String conference : confList) {
            System.out.println("querying for " + conference);
            APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
            Query query = builder.select(ConferenceData.CITATION.year.as("x"),
                new SchemaCount(ConferenceData.CITATION.title).as("y"))
                .from(conference)
                .where(ConferenceData.CITATION.year.equalsTo(year)
                        .and(ConferenceData.CITATION.year.isNotNull()))
                .groupBy(ConferenceData.CITATION.year)
                .build();
            String result = query.execute();
            confResultMap.put(conference, result);
        }
        String compiledResult = compileJsonResults(confResultMap);
        System.out.println(compiledResult);
        try {
            logic.saveResultIntoCsv(compiledResult, "1");
        } catch (Exception e) {
;
        }
    }



    private static int getHighestYear(WebRequest request) {
        return Math.max(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
    }
    private static int getLowestYear(WebRequest request) {
        return Math.min(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
    }

    private static String compileJsonResults(Map<String, String> confResultMap) {

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        confResultMap.forEach((conference, result) -> {
            JsonReader jsonReader;
            JsonArray jsonTuples;
            JsonObjectBuilder objectBuilder;
        	System.out.println("BREAKING " + result);
            jsonReader = Json.createReader(new StringReader(result));
            jsonTuples = jsonReader.readArray();
            for (int i=0; i<jsonTuples.size(); i++) {
                JsonObject jsonTuple = jsonTuples.getJsonObject(i);
                System.out.println(jsonTuple.toString() + "  LOL" + i);
                objectBuilder = addNewJsonObject(arrayBuilder, jsonTuple, conference);
                arrayBuilder.add(objectBuilder);
            }
        });

        return arrayBuilder.build().toString();
    }
    private static JsonObjectBuilder addNewJsonObject(JsonArrayBuilder builder, JsonObject jsonTuple, String conference) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("x", conference);
        objectBuilder.add("y", jsonTuple.getJsonString("y"));
       
        return objectBuilder;
    }

}
