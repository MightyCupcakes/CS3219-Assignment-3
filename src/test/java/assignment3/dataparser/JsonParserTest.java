package assignment3.dataparser;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Test;

import assignment3.dataparser.jsonparser.JsonDataParser;
import assignment3.datarepresentation.SerializedJournal;

public class JsonParserTest {

    private static final String TEST_DATA = "src/test/data/jsonDataParserTest.json";

    @Test
    public void test_JsonDataParser() throws Exception {
        JsonDataParser parser = new JsonDataParser();

        File file = new File(TEST_DATA);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        parser.parseData(reader.readLine());

        SerializedJournal journal = parser.getJournal();

        assertEquals("0000c8f31b6058b1b42d9580c7afaa2e673a98c2", journal.id);
        assertEquals("A new minimally invasive technique for pudendal nerve stimulation.", journal.title);
        assertEquals(0, journal.numOfInCitations);
        assertEquals("Colorectal disease : the official journal of the Association of Coloproctology of Great Britain and Ireland", journal.venue);
        assertEquals("A T George,T C Dudding,R J Nicholls,C J Vaizey", journal.author);
    }
}
