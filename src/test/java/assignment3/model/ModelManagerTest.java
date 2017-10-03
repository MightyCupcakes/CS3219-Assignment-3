package assignment3.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public class ModelManagerTest {
	private static String CONFERENCE_FILE = "D12";

	private static Model model;
	HashMap<String, HashMap<Integer, SerializedJournal>> journalMap;
	HashMap<String, HashMap<Integer, List<SerializedCitation>>> citationMap;
	
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
	/*
	@Test
	public void testAddConferenceToModel() throws Exception {
		journalMap = model.getJournalMap();
		citationMap = model.getCitationMap();
		HashMap<Integer, SerializedJournal> journals = model.getJournal(CONFERENCE_FILE);
		assertEquals(1, journalMap.size());
		assertEquals(1, citationMap.size());
		HashMap<Integer, List<SerializedCitation>> citations = model.getCitations(CONFERENCE_FILE);
		assertEquals(1, journalMap.size());
		assertEquals(1, citationMap.size());
		
		assertTrue(journalMap.containsKey(CONFERENCE_FILE));
		assertTrue(citationMap.containsKey(CONFERENCE_FILE));
	}*/
	
}
