package assignment3.logic;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import assignment3.datarepresentation.SerializedCitation;

public class JsonGenerator {

    protected JsonArrayBuilder listOfObjects;
    protected int numOfRowsToParse = -1;
    protected int rowsAdded = 0;

    public JsonGenerator() {
        listOfObjects = Json.createArrayBuilder();
    }

    public JsonGenerator(int limit) {
        this();
        numOfRowsToParse = limit;
    }

    public void addObjectToArray(JsonGeneratorBuilder generator) {

        if (numOfRowsToParse > 0 && rowsAdded == numOfRowsToParse) {
            return;
        }

        rowsAdded++;
        listOfObjects.add(generator.builder);
    }

    public String getJsonString() {
        return getJsonArray().toString();
    }
    public JsonArray getJsonArray() {
    	return listOfObjects.build();
    }

    public static class JsonGeneratorBuilder {

        protected JsonObjectBuilder builder;
        protected Map<String, String> keyValuePair;

        public JsonGeneratorBuilder() {
            builder = Json.createObjectBuilder();
            keyValuePair = new HashMap<>();
        }

        private static JsonObjectBuilder generateCitationJson(SerializedCitation citation) {
            JsonObjectBuilder nestedBuilder = Json.createObjectBuilder();
            JsonArrayBuilder moreNested = Json.createArrayBuilder();

            citation.authorsList.forEach(moreNested::add);

            nestedBuilder.add("authors", moreNested);
            nestedBuilder.add("title", citation.title);
            nestedBuilder.add("year",  citation.year);
            nestedBuilder.add("booktitle", citation.booktitle);

            return nestedBuilder;
        }

        public void generateJson(String name, Object value) {
            keyValuePair.put(name, value.toString());
            builder.add(name, value.toString());
        }

        public void generateJson(String name, Iterable<SerializedCitation> value) {
            JsonArrayBuilder nestedBuilder = Json.createArrayBuilder();

            value.forEach(serializedCitation -> nestedBuilder.add(generateCitationJson(serializedCitation)));

            builder.add("citations", nestedBuilder);
        }
    }
}
