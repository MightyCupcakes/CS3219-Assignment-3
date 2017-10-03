package assignment3.storage;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import javafx.util.Pair;

public class StorageManagerTest {
	private static final String TEST_DATA = "src/test/data/";
	Storage storage;
	Pair<HashMap<Integer, SerializedJournal>, HashMap<Integer, List<SerializedCitation>>> storagePair;
	
	/*
	@Test
	public void testStorageManager() throws Exception {
		storage = new StorageManager();
		storagePair = storage.retrieveFile("XmlTestDataCompiled", TEST_DATA);
		
		HashMap<Integer, SerializedJournal> journalMap = storagePair.getKey();
		HashMap<Integer, List<SerializedCitation>> citationMap = storagePair.getValue();
		
		assertEquals(journalMap.size(), 2);
		assertEquals(citationMap.size(), 2);
		assertEquals(citationMap.get(2).size(), 2);
		
	} */
}
