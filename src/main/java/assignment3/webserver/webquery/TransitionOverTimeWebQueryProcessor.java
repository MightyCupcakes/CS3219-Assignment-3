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
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Transition Over Time")
public class TransitionOverTimeWebQueryProcessor implements WebQueryProcessor {

    private static final String QUERY_FOR_YEARS = PREMADE_QUERIES.get(0).name;
    private static final String QUERY_FOR_CONFS = PREMADE_QUERIES.get(1).name;
    private static String requiredHtmlFile = "barchart";


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
        	query = getFilteredQueryForMultipleYears(builder, webRequest);
            requiredHtmlFile ="LineChart";

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


    private static Query getFilteredQueryForMultipleYears(APIQueryBuilder builder, WebRequest request) {
    	int endYear = getHighestYear(request);
        int startYear = getLowestYear(request);
        String conf = request.getValue("conferenceValue");
        String venue = request.getValue("venueValue");
        builder = builder.select(ConferenceData.CITATION.year.as("x"),
        		new SchemaCountUnique(ConferenceData.CITATION.title).as("y"))
                .from(conf)
                .where(ConferenceData.CITATION.year.greaterThanOrEqualsTo(startYear)
                        .and(ConferenceData.CITATION.year.lessThanOrEqualsTo(endYear)
                                .and(ConferenceData.CITATION.year.isNotNull()
                                		.and(ConferenceData.VENUE.equalsToIgnoreCase(venue)))))
                .groupBy(ConferenceData.CITATION.year)
                .orderBy(ConferenceData.CITATION.year, APIQueryBuilder.OrderByRule.ASC);
        return builder.build();
    }

    private static int getHighestYear(WebRequest request) {
        return Math.max(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
    }
    private static int getLowestYear(WebRequest request) {
        return Math.min(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
    }



}
