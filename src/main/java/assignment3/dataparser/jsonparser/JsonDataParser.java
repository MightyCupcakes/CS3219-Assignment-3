package assignment3.dataparser.jsonparser;

import java.io.FileNotFoundException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import assignment3.dataparser.DataParser;
import assignment3.dataparser.exceptions.StopParserException;
import assignment3.datarepresentation.SerializedJournal;

public class JsonDataParser implements DataParser {

    private SerializedJournal journal;

    @Override
    public void parseData(String json) throws StopParserException {
        journal = parseJournal(json);
    }

    @Override
    public SerializedJournal getJournal(){
        return journal;
    }

    private SerializedJournal parseJournal(String json) {
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        SerializedJournal.Builder builder = new SerializedJournal.Builder();

        parseId(object, builder);
        parseAuthors(object, builder);
        parseInCitations(object, builder);
        parseCitations(object, builder);
        parseVenue(object, builder);
        parseTitle(object, builder);
        parseDate(object, builder);

        return builder.build();
    }

    private void parseAuthors(JsonObject object, SerializedJournal.Builder builder) {
        JsonArray authors = object.getJsonArray("authors");

        for(int i = 0; i < authors.size(); i++) {
            JsonObject authorDetails = authors.getJsonObject(i);
            builder.withAuthor(authorDetails.getString("name"));
        }
    }

    private void parseId(JsonObject object, SerializedJournal.Builder builder) {
        builder.withId(object.getString("id"));
    }

    private void parseInCitations(JsonObject object, SerializedJournal.Builder builder) {
        JsonArray papers = object.getJsonArray("inCitations");

        for(int i = 0; i < papers.size(); i++) {
            builder.withInCitation(papers.getString(i));
        }
    }

    private void parseCitations(JsonObject object, SerializedJournal.Builder builder) {
        JsonArray papers = object.getJsonArray("outCitations");

        for(int i = 0; i < papers.size(); i++) {
            builder.withCitationId(papers.getString(i));
        }
    }

    private void parseVenue(JsonObject object, SerializedJournal.Builder builder) {
        builder.withVenue(object.getString("venue"));
    }

    private void parseTitle(JsonObject object, SerializedJournal.Builder builder) {
        builder.withTitle(object.getString("title"));
    }

    private void parseDate(JsonObject object, SerializedJournal.Builder builder) {
        builder.withYear(object.getInt("year", 0));
    }
}
