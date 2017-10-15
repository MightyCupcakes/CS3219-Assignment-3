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
import assignment3.schema.SchemaString;
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
                .select(new SchemaCount(new SchemaString("id")))
                .from("xmlTestAPI")
                .build();

        JsonReader jsonReader = Json.createReader(new StringReader(query.execute()));
        JsonArray object = jsonReader.readArray();
        jsonReader.close();

        assertEquals("15", object.getJsonObject(0).getString("COUNT(id)"));
    }

    @Test
    public void test_API_limit() throws Exception {
        Query query = QueryBuilder.createNewBuilder()
                .select(new SchemaString("id"))
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
                .select(year, new SchemaCount(new SchemaString("id")))
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
