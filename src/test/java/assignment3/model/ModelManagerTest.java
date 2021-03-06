package assignment3.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public class ModelManagerTest {
	private static String CONFERENCE_FILE = "D12";

	private static Model model;
	Map<String, Map<Integer, SerializedJournal>> journalMap;
	Map<String, Map<Integer, List<SerializedCitation>>> citationMap;
	
	@BeforeClass
	public static void setUp() {
		model = new ModelManager();
	}
	
	@Test
	public void testEmptyModel() {
		journalMap = model.getJournalMap();
		citationMap = model.getCitationMap();
		assertEquals(0, journalMap.size());
		assertEquals(0, citationMap.size());
	}

	@Test
	public void testAddConferenceToModel() throws Exception {
		journalMap = model.getJournalMap();
		citationMap = model.getCitationMap();

		Map<Integer, SerializedJournal> journals = model.getJournal(CONFERENCE_FILE);
		assertEquals(1, journalMap.size());
		assertEquals(1, citationMap.size());

		Map<Integer, List<SerializedCitation>> citations = model.getCitations(CONFERENCE_FILE);
		assertEquals(1, journalMap.size());
		assertEquals(1, citationMap.size());
		
		assertTrue(journalMap.containsKey(CONFERENCE_FILE));
		assertTrue(citationMap.containsKey(CONFERENCE_FILE));
	}


	@Test
	public void testWriteToCsvFIle() throws Exception {
		List<List<String>> dataList = new ArrayList<>();
		dataList.add(Arrays.asList("label", "count"));
		dataList.add(Arrays.asList("Monday", "379130"));
		dataList.add(Arrays.asList("Tuesday", "424923"));
		dataList.add(Arrays.asList("Wednesday", "430728"));
		dataList.add(Arrays.asList("Thursday", "432138"));
		dataList.add(Arrays.asList("Friday", "428295"));
		dataList.add(Arrays.asList("Saturday", "368239"));
		dataList.add(Arrays.asList("Sunday", "282701"));
		model.writeResultIntoCsvFile("testResult", dataList);
		File file = new File("docs/d3charts/d3SavedData/testResult.csv");
		assertTrue(file.exists());
		file.delete();
	}
	
}
