package assignment3.webserver.webquery;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
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
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Citation Network")
public class CitationNetworkBasePaperWebQueryProcessor implements WebQueryProcessor {
	
	private static final String DEFAULT_FILE = "citationNetworkBasePaperData";
	
	@Override
    public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        String basePaperTitle = webRequest.getValue("basepapertitle");
                
        JsonReader jsonReader;
    	JsonArray jsonTuples;
    	JsonObjectBuilder objectBuilder;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
               
    	Query query = builder
                .select(ConferenceData.ID)
                .from("A4")
                .where(ConferenceData.TITLE.equalsTo(basePaperTitle))
                .build();
    	
    	String basePaperID = query.execute();
    	JsonReader reader = Json.createReader(new StringReader(basePaperID));
    	JsonArray object = reader.readArray();
    	reader.close();
    	basePaperID = ((JsonObject) object.get(0)).get("id").toString();
    	basePaperID = basePaperID.replaceAll("\"", "");
        
        Query query2 = builder
                .select(ConferenceData.ID.as("journalId"),
                        ConferenceData.TITLE.as("journalTitle"),
                		ConferenceData.AUTHORS.as("journalAuthors"),
                        ConferenceData.CITATION.journalId.as("citedJournalId"),
                        ConferenceData.CITATION.title.as("citedJournalTitle"),
                        ConferenceData.CITATION.authors.as("citedJournalAuthors")
                )
                .from("A4")
                .where(ConferenceData.CITATION.journalId.equalsTo(basePaperID))
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
        
        Query query3 = builder
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
        JsonArray array = arrayBuilder.build();
        JsonObject jsonObject = convertToCitationNetworkJSON(array, basePaperTitle, basePaperID);
        //System.out.println(jsonObject.toString());
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("docs/d3charts/d3SavedData/citationWebForBasePaper.json");	
			fileWriter.write(jsonObject.toString());
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return true;
    }
	
	private JsonObject convertToCitationNetworkJSON(JsonArray array, String basePaperTitle, String basePaperID){
        JsonObject jsonObject = Json.createObjectBuilder().build();
        JsonArray jsonArray = Json.createArrayBuilder().build();
        int count = 0;
        
        for(int i=0; i<array.size(); i++){
        	JsonObject current = array.getJsonObject(i);
        	String journalID = current.get("journalId").toString();
        	journalID = trim(journalID);
        	String journalTitle = current.get("journalTitle").toString();
        	journalTitle = trim(journalTitle);
        	String journalAuthor = current.get("journalAuthors").toString();
        	journalAuthor = trim(journalAuthor);
        	String citedJournalTitle = current.get("citedJournalTitle").toString();
        	citedJournalTitle = trim(citedJournalTitle);
        	String citedJournalID = current.get("citedJournalId").toString();
        	citedJournalID = trim(citedJournalID);
        	String citedJournalAuthor = current.get("citedJournalAuthors").toString();
        	citedJournalAuthor = trim(citedJournalAuthor);
        	if(basePaperID.equals(citedJournalID)){
        		if(count == 0){
        			jsonObject = jsonObjectToBuilder(jsonObject).add("name", basePaperTitle).build();
        			jsonObject = jsonObjectToBuilder(jsonObject).add("color", "#4682b4").build();
        			jsonObject = jsonObjectToBuilder(jsonObject).add("author", citedJournalAuthor).build();
        			count ++;
        		} 
                JsonObject jsonObject2 = Json.createObjectBuilder().build();
                jsonObject2 = jsonObjectToBuilder(jsonObject2).add("name", journalTitle).build();
                jsonObject2 = jsonObjectToBuilder(jsonObject2).add("color", "#b48a46").build();
                jsonObject2 = jsonObjectToBuilder(jsonObject2).add("author", journalAuthor).build();
        		boolean haveNest = false; 
                JsonArray jsonArray2 = Json.createArrayBuilder().build();
        		for(int j=0; j<array.size(); j++){
        			if(j == i) 
        				continue;
        			JsonObject currentJ = array.getJsonObject(j);
        			String journalIDJ = currentJ.get("journalId").toString();
                	journalIDJ = trim(journalIDJ);
                	String journalTitleJ = currentJ.get("journalTitle").toString();
                	journalTitleJ = trim(journalTitleJ);
                	String journalAuthorJ = currentJ.get("journalAuthors").toString();
                	journalAuthorJ = trim(journalAuthorJ);
        			String citedJournalTitleJ = currentJ.get("citedJournalTitle").toString();
                	citedJournalTitleJ = trim(citedJournalTitleJ);
                	String citedJournalIDJ = currentJ.get("citedJournalId").toString();
                	citedJournalIDJ = trim(citedJournalIDJ);
                	String citedJournalAuthorJ = currentJ.get("citedJournalAuthors").toString();
                	citedJournalAuthorJ = trim(citedJournalAuthorJ);
                	if(journalID.equals(citedJournalIDJ)){
        				JsonObject jsonObject3 = Json.createObjectBuilder().build();        				
        				jsonObject3 = jsonObjectToBuilder(jsonObject3).add("name", journalTitleJ).build();
        				jsonObject3 = jsonObjectToBuilder(jsonObject3).add("color", "b48a46").build();
        				jsonObject3 = jsonObjectToBuilder(jsonObject3).add("author", journalAuthorJ).build();
        				jsonArray2 = addJsonObjectToArray(jsonArray2, jsonObject3);
        				haveNest = true;
        			}
        		}
        		if(haveNest){
                    jsonObject2 = jsonObjectToBuilder(jsonObject2).add("children", jsonArray2).build();
        		}
        		jsonArray = addJsonObjectToArray(jsonArray, jsonObject2);
        	}
        } 
        jsonObject = jsonObjectToBuilder(jsonObject).add("children", jsonArray).build(); 
        
        return jsonObject;
    }
	
	private JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
        JsonObjectBuilder job = Json.createObjectBuilder();

        for (Entry<String, JsonValue> entry : jo.entrySet()) {
            job.add(entry.getKey(), entry.getValue());
        }

        return job;
    }
    
    private JsonArray addJsonObjectToArray(JsonArray jsonArray, JsonObject jsonObject){
    	JsonArrayBuilder builder = Json.createArrayBuilder();
    	for (JsonValue value : jsonArray) {
    	    builder.add(value);
    	}
    	builder.add(jsonObject);
    	JsonArray newArray = builder.build();
    	return newArray;
    }
    
    private JsonObjectBuilder addNewJsonObject(JsonArrayBuilder builder, JsonObject jsonTuple) {
    	JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    	for (Entry<String, JsonValue> entry : jsonTuple.entrySet()) {
    		String value = entry.getValue().toString();

    		objectBuilder.add(entry.getKey(), value);
    	}
    	return objectBuilder;
    }
    
    private String trim(String str){
    	return str.replace("\\\"", "").replaceAll("\"", "");
    }

	@Override
	public String getHtmlFileName() {
		return "CollapsibleTree";
	}
}
