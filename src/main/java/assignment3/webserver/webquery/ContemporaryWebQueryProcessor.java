package assignment3.webserver.webquery;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import assignment3.api.APIQueryBuilder;
import assignment3.api.ConferenceData;
import assignment3.api.Query;
import assignment3.logic.Logic;
import assignment3.logic.LogicManager;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Contemporary comparison")
public class ContemporaryWebQueryProcessor implements WebQueryProcessor {
    private static final Logic logic = new LogicManager();
    
	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) throws Exception {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        String premadeType = webRequest.getValue("premadeQuery");
        getQueryForMultipleConfs(manager, webRequest);
		return true;
	}
	
	   private static void getQueryForMultipleConfs(WebServerManager manager, WebRequest request) throws Exception {
	        String conferenceValues = request.getValue("conferenceValue");
	        int year = Integer.parseInt(request.getValue("startYearDate"));
	        List<String> confList = Arrays.asList(conferenceValues.split(","));
	        Map<String, String> confResultMap = new HashMap<>();
	        for (String conference : confList) {
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
	        logic.saveResultIntoCsv(compiledResult, "1");
	 
	    }
	   
	    private static String compileJsonResults(Map<String, String> confResultMap) {

	        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	        confResultMap.forEach((conference, result) -> {
	            JsonReader jsonReader;
	            JsonArray jsonTuples;
	            JsonObjectBuilder objectBuilder;
	            jsonReader = Json.createReader(new StringReader(result));
	            jsonTuples = jsonReader.readArray();
	            for (int i=0; i<jsonTuples.size(); i++) {
	                JsonObject jsonTuple = jsonTuples.getJsonObject(i);
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
	/*
	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) throws Exception {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        
        String firstVenue = webRequest.getValue("firstVenue");
        String secondVenue = webRequest.getValue("secondVenue");
        Boolean isExact = Boolean.valueOf(webRequest.getValue("Exact"));
        int year = Integer.parseInt(webRequest.getValue("Year"));
        
        System.out.println("comparing " + firstVenue + " " + secondVenue + " " + year);
        SchemaPredicate predicate;
        predicate = ConferenceData.YEAR_OF_PUBLICATION.equalsTo(year);
        if (isExact) {
        	predicate = predicate
        			.and(ConferenceData.CITATION.citationVenue.equalsToIgnoreCase(firstVenue))
        			.or(ConferenceData.CITATION.citationVenue.equalsToIgnoreCase(secondVenue));
        } else {
        	predicate = predicate
        			.and(ConferenceData.CITATION.citationVenue.like(firstVenue))
        			.or(ConferenceData.CITATION.citationVenue.like(secondVenue));    	
        }
        Query query = builder
        		.select(ConferenceData.CITATION.citationVenue.as("x"),
        				new SchemaCount(ConferenceData.CITATION.citationVenue).as("y"))
        		.from(WebQueryProcessor.DEFAULT_CONFERENCE)
        		.where(predicate.and(ConferenceData.CITATION.citationVenue.isNotNull()))
        		.groupBy(ConferenceData.CITATION.citationVenue)
        		.build();
        query.executeAndSaveInCSV("1");
        return true;
	}
*/
	@Override
	public String getHtmlFileName() {
		return "BarChart";
	}

}
