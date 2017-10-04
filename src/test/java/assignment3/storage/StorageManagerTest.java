package assignment3.storage;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public class StorageManagerTest {
	private static final String TEST_DATA = "src/test/data/";
	Storage storage;
	RetrievedFileData storagePair;
	
	
	@Test
	public void testStorageManager() throws Exception {
		storage = new StorageManager();
		storagePair = storage.retrieveFile("xmlTestDataCompiled", TEST_DATA);
		
		Map<Integer, SerializedJournal> journalMap = storagePair.journalsMap;
		Map<Integer, List<SerializedCitation>> citationMap = storagePair.citationsMap;
		
		assertEquals(journalMap.size(), 2);
		assertEquals(citationMap.size(), 2);
		assertEquals(citationMap.get(2).size(), 2);
		
	} 
}
