package assignment3.api;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.lang.reflect.Field;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment3.logic.LogicManager;
import assignment3.logic.QueryBuilder;
import assignment3.model.ModelManager;
import assignment3.schema.SchemaInt;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.storage.StorageManager;

public class APITest {

    private static final String FOLDER = "src/test/data/";

    private static API api;
    private static LogicManager logic;
    private static ModelManager model;

    @BeforeClass
    public static void setUp() throws Exception {
        api = new APIManager();

        Field modelField = LogicManager.class.getDeclaredField("model");
        modelField.setAccessible(true);

        Field logicField = APIManager.class.getDeclaredField("logic");
        logicField.setAccessible(true);

        Field storageField = ModelManager.class.getDeclaredField("storage");
        storageField.setAccessible(true);

        logic = (LogicManager) logicField.get(api);
        model = (ModelManager) modelField.get(logic);
        storageField.set(model, new StorageManager(FOLDER));

    }

    @Test
    public void test_API_vanilla() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaCount(ConferenceData.AUTHORS))
                .from("xmlTestAPI")
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        //assertEquals("15", object.getJsonObject(0).getString("COUNT(author)"));
    }

    @Test
    public void test_API_splitAuthors() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.AUTHOR, ConferenceData.ID)
                .from("xmlTestAPI")
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        assertEquals(75, object.size());
    }

    @Test
    public void test_API_getCitations() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.AUTHORS, ConferenceData.ID, ConferenceData.CITATION.title)
                .from("xmlTestAPI")
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        assertEquals(17, object.size());
        assertEquals("0030c7052fdba87fa2629976aad5c7380f97c82d",
                object.getJsonObject(1).getString(ConferenceData.ID.getNameOfAttribute()).trim());
        assertEquals("The Arithmetic of Elliptic Curvesâ€”an Update",
                object.getJsonObject(1).getString(ConferenceData.CITATION.title.getNameOfAttribute()).trim());

        assertEquals("00227b167f2bd46369e50fd2e75c9a08dc82d830",
                object.getJsonObject(13).getString(ConferenceData.ID.getNameOfAttribute()).trim());
        assertEquals("Low-density parity check codes over GF(q)",
                object.getJsonObject(13).getString(ConferenceData.CITATION.title.getNameOfAttribute()).trim());

        assertEquals("00227b167f2bd46369e50fd2e75c9a08dc82d830",
                object.getJsonObject(14).getString(ConferenceData.ID.getNameOfAttribute()).trim());
        assertEquals("Understanding of emotional experience in autism: insights from the personal accounts of high-functioning children with autism.",
                object.getJsonObject(14).getString(ConferenceData.CITATION.title.getNameOfAttribute()).trim());

        assertEquals("00227b167f2bd46369e50fd2e75c9a08dc82d830",
                object.getJsonObject(15).getString(ConferenceData.ID.getNameOfAttribute()).trim());
        assertEquals("How Unique and Traceable Are Usernames?",
                object.getJsonObject(15).getString(ConferenceData.CITATION.title.getNameOfAttribute()).trim());
    }

    @Test
    public void test_API_getCitations2() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.CITATION.title)
                .from("xmlTestAPI")
                .where(ConferenceData.ID.like("00227b167f2bd46369e50fd2e75c9a08dc82d830"))
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        assertEquals(3, object.size());
        assertEquals("Low-density parity check codes over GF(q)",
                object.getJsonObject(0).getString(ConferenceData.CITATION.title.getNameOfAttribute()));
    }

    @Test
    public void test_API_splitAuthorsCount() throws Exception {
        SchemaCount count = new SchemaCount(ConferenceData.ID);

        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.AUTHOR, count)
                .from("xmlTestAPI")
                .groupBy(ConferenceData.AUTHOR)
                .orderBy(count, APIQueryBuilder.OrderByRule.DESC)
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        assertEquals("T A Coleman", object.getJsonObject(0).getString("author"));
        assertEquals("3", object.getJsonObject(0).getString(count.getNameOfAttribute()));
    }

    @Test
    public void test_API_limit() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(ConferenceData.ID)
                .from("xmlTestAPI")
                .limit(5)
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        assertEquals(5, object.size());
    }

    @Test
    public void test_API_orderby() throws Exception {
        SchemaInt year = new SchemaInt("yearOfPublication");

        Query query = QueryBuilder.createNewBuilder()
                .select(year, new SchemaCount(ConferenceData.ID))
                .from("xmlTestAPI")
                .where(year.greaterThan(0))
                .groupBy(year)
                .orderBy(year, APIQueryBuilder.OrderByRule.DESC)
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        assertEquals("2015", object.getJsonObject(0).getString("yearOfPublication"));
        assertEquals("2014", object.getJsonObject(1).getString("yearOfPublication"));
    }
}
