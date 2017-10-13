package assignment3.dataparser;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.junit.Test;

import assignment3.dataparser.jsonparser.JsonDataParser;
import assignment3.dataparser.jsonparser.JsonIdDataParser;
import assignment3.datarepresentation.SerializedJournal;

public class JsonIdParserTest {

    private static final String TEST_DATA = "src/test/data/jsonDataParserTest.json";

    @Test
    public void test_JsonIdTest() throws Exception {

        JsonIdDataParser idDataParser = new JsonIdDataParser();

        File file = new File(TEST_DATA);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;

        while ( (line = reader.readLine()) != null) {
            JsonDataParser parser = new JsonDataParser();

            parser.parseData(line);
            idDataParser.addJournal(parser.getJournal());
        }

        idDataParser.linkCitationsIdToJournal();

        List<SerializedJournal> journalCollection = idDataParser.getJournals();

        SerializedJournal firstJournal = journalCollection.get(0);

        assertEquals("0000c8f31b6058b1b42d9580c7afaa2e673a98c2", firstJournal.id);
        assertEquals(2, firstJournal.citations.size());
        assertEquals("A", firstJournal.citations.get(0).title);
        assertEquals("B", firstJournal.citations.get(1).title);
    }
}
