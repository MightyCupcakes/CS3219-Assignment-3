package assignment3.logic;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.schema.SchemaBase;

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
            nestedBuilder.add("title", citation.citationtitle);
            nestedBuilder.add("year",  citation.year);
            nestedBuilder.add("booktitle", citation.booktitle);

            return nestedBuilder;
        }

        public void generateJson(SchemaBase column, @Nullable Object value) {

            String stringValue = isNull(value) ? "NULL" : value.toString();

            keyValuePair.put(column.originalNameOfAttribute, stringValue);
            builder.add(column.getNameOfAttribute(), stringValue);
        }

        public void generateJson(String column, @Nullable Object value) {

            String stringValue = isNull(value) ? "NULL" : value.toString();

            keyValuePair.put(column, stringValue);
            builder.add(column, stringValue);
        }

        public void generateJson(String name, Iterable<SerializedCitation> value) {
            JsonArrayBuilder nestedBuilder = Json.createArrayBuilder();

            value.forEach(serializedCitation -> nestedBuilder.add(generateCitationJson(serializedCitation)));

            builder.add("citations", nestedBuilder);
        }
    }
}
