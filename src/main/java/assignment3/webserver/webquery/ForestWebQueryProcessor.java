package assignment3.webserver.webquery;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
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
import assignment3.logic.QueryBuilder;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Citation Forest")
public class ForestWebQueryProcessor implements WebQueryProcessor {
    
    private static final String TITLE = "title";
    private static final String CITATION_TITLE = "citationtitle";
    private static final String SOURCE = "source";
    private static final String TARGET = "target";
    private static final String TYPE = "type";
    private static final String LINK = "links = '";
    private static final String END = "';";
    
    @Override
    public boolean processAndSaveIntoCSV(WebServerManager manager,WebRequest webRequest) {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        String result = webRequest.getValue("currentYearDate");
        
        int currentYear = Integer.parseInt(result);
        
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.TITLE, ConferenceData.CITATION.title)
                .from("A4")
                .where((ConferenceData.YEAR_OF_PUBLICATION.equalsTo(currentYear))
                          .and(ConferenceData.CITATION.title.isNotNull()))
                .build();
        
        String temp = query.execute();
        JsonReader reader = Json.createReader(new StringReader(temp));
        JsonArray array = reader.readArray();
        reader.close();
        
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int link = 0;
        JsonArray jsonArray = Json.createArrayBuilder().build();
        
        String JSON = LINK;
        int size = array.size();
        
        for (int i=0; i < size; i++) {
           JsonObject curr = array.getJsonObject(i);
           String title = curr.getString(TITLE).toString();
           String citation = curr.getString(CITATION_TITLE).toString();
           // title = title.replaceAll("[^a-zA-Z0-9]", " ");
           // citation = citation.replaceAll("[^a-zA-Z0-9]", " ");
           
           title = reduce(title);
           citation = reduce(title);
         
           if (map.containsKey(title)) {
               int value = map.get(title);
               JsonObject jsonObj = Json.createObjectBuilder().build();
               jsonObj = jsonObjectToBuilder(jsonObj).add(SOURCE, title.trim()).build();
               jsonObj = jsonObjectToBuilder(jsonObj).add(TARGET, citation.trim()).build();
               String valueStr = "" + value;
               jsonObj = jsonObjectToBuilder(jsonObj).add(TYPE, valueStr).build();

               jsonArray = addJsonObjectToArray(jsonArray, jsonObj);
           } else {
               map.put(title, link);
               link++;
           }
        }
       
        JSON += jsonArray.toString();
        JSON += END;
        // System.out.print(JSON);
        
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("docs/d3charts/d3SavedData/citationWebForForest.json");  
            fileWriter.write(JSON);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return true;
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
    
    private String reduce(String str){
        return str.replaceAll("[^a-zA-Z0-9]", " ");
    }

    @Override
    public String getHtmlFileName() {
        return "CollapsibleTree";
    }
}
