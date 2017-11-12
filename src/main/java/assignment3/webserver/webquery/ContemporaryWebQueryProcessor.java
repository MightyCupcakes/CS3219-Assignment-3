package assignment3.webserver.webquery;

import static assignment3.webserver.WebServerConstants.PREMADE_QUERIES;

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
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Contemporary comparison")
public class ContemporaryWebQueryProcessor implements WebQueryProcessor {
    private static final Logic logic = new LogicManager();
    private static final String MULTI_CONF_A_YEAR = PREMADE_QUERIES.get(2).name;
    private static final String CONF_TREND = PREMADE_QUERIES.get(7).name;
    private static String requiredHtml;
	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) throws Exception {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        String premadeType = webRequest.getValue("premadeQuery");
        if (premadeType.equals(MULTI_CONF_A_YEAR)) {
        	getQueryForMultipleConfs(manager, webRequest);
        	requiredHtml = "BarChart";
        } else if (premadeType.equals(CONF_TREND)) {
        	queryForAConf(manager, webRequest);
        	requiredHtml = "LineChart";
        }
		return true;
	}
	
	   private void queryForAConf(WebServerManager manager, WebRequest webRequest) throws Exception {
	        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
	        String conference = webRequest.getValue("conferenceValue");
	        Query query = builder.select(ConferenceData.CITATION.year.as("x"), new SchemaCountUnique(ConferenceData.CITATION.title).as("y"))
	        		.from(conference)
	        		.where(ConferenceData.CITATION.year.isNotNull().and(ConferenceData.CITATION.year.greaterThan(0)))
	        		.groupBy(ConferenceData.CITATION.year)
	        		.orderBy(ConferenceData.CITATION.year, APIQueryBuilder.OrderByRule.ASC)
	        		.build();
	        query.executeAndSaveInCSV("2");
	}

	private static void getQueryForMultipleConfs(WebServerManager manager, WebRequest request) throws Exception {
	        String conferenceValues = request.getValue("conferenceValue");
	        int year = Integer.parseInt(request.getValue("startYearDate"));
	        List<String> confList = Arrays.asList(conferenceValues.split(","));
	        Map<String, String> confResultMap = new HashMap<>();
	        for (String conference : confList) {
	            APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
	            Query query = builder.select(ConferenceData.CITATION.year.as("x"),
	                new SchemaCountUnique(ConferenceData.CITATION.title).as("y"))
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

	@Override
	public String getHtmlFileName() {
		return requiredHtml;
	}

}
