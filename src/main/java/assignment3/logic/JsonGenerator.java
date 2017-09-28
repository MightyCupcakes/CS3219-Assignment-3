package assignment3.logic;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import assignment3.datarepresentation.SerializedCitation;

public class JsonGenerator {

    private JsonArrayBuilder listOfObjects;

    public JsonGenerator() {
        listOfObjects = Json.createArrayBuilder();
    }

    public void addObjectToArray(JsonGeneratorBuilder generator) {
        listOfObjects.add(generator.builder);
    }

    public String getJsonString() {
        return listOfObjects.build().toString();
    }


    public static class JsonGeneratorBuilder {

        protected JsonObjectBuilder builder;

        public JsonGeneratorBuilder() {
            builder = Json.createObjectBuilder();
        }

        private static JsonObjectBuilder generateCitationJson(SerializedCitation citation) {
            JsonObjectBuilder nestedBuilder = Json.createObjectBuilder();
            JsonArrayBuilder moreNested = Json.createArrayBuilder();

            citation.authors.forEach(moreNested::add);

            nestedBuilder.add("authors", moreNested);
            nestedBuilder.add("title", citation.title);
            nestedBuilder.add("year", citation.year);

            return nestedBuilder;
        }

        public void generateJson(String name, Object value) {
            builder.add(name, value.toString());
        }

        public void generateJson(String name, Iterable<SerializedCitation> value) {
            JsonArrayBuilder nestedBuilder = Json.createArrayBuilder();

            value.forEach(serializedCitation -> nestedBuilder.add(generateCitationJson(serializedCitation)));

            builder.add("citations", nestedBuilder);
        }
    }
}
