package assignment3.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.model.ModelManager;

public class LogicManagerTest {
	private static Logic logic;
	private final String STORAGE_LOCATION = "Dataset/";
	private final String XML_FORMAT = ".xml";
	private final String TEST_FOLDER = "src/test/data/Dtest";
	private final String TEST_FILE_NAME = "Dtest";
	
	@BeforeClass
	public static void setUp() {
		logic = new LogicManager();
	}
	
	@Test
	public void testParseAndSaveRawData() throws Exception {
		String newFile = STORAGE_LOCATION + TEST_FILE_NAME + XML_FORMAT;
		File file = new File(newFile);
		assertTrue(!file.exists());
		
		logic.parseAndSaveRawData(TEST_FOLDER);
		assertTrue(file.exists());
		
	}
	
	@Test
	public void testGetDataWithNoCitation() throws Exception {
		logic.parseAndSaveRawData(TEST_FOLDER);
		List<SerializedJournalCitation> journalList = logic.getDataFromTableWithNoCitations(TEST_FILE_NAME);
		assertEquals(2, journalList.size());
		SerializedJournalCitation journalCitation_one= journalList.get(0);
		SerializedJournalCitation journalCitation_two = journalList.get(1);
		assertEquals(journalCitation_one.journal.title, "Wiki-ly Supervised Part-of-Speech Tagging");
        assertEquals(journalCitation_one.journal.author, "Li V Ben Taskar");
	
    	assertEquals(journalCitation_two.journal.title, "Dtest_2 title");
        assertEquals(journalCitation_two.journal.author, "Dtest_2 author");
        
        
	}
	
	@Test
	public void testGetDataWithCitation() throws Exception {
		logic.parseAndSaveRawData(TEST_FOLDER);
		List<SerializedJournalCitation> journalList = logic.getDataFromTableWithCitations(TEST_FILE_NAME);
		assertEquals(4, journalList.size());
		
		SerializedJournalCitation journalCitation_one= journalList.get(0);
		SerializedJournalCitation journalCitation_two = journalList.get(1);
		SerializedJournalCitation journalCitation_three = journalList.get(2);
		SerializedJournalCitation journalCitation_four = journalList.get(3);
		
		assertEquals(journalCitation_one.journal, journalCitation_two.journal);
		assertEquals(journalCitation_three.journal, journalCitation_four.journal);

	}
	
	@After
	public void deleteFile() {
		String newFile = STORAGE_LOCATION + TEST_FILE_NAME + XML_FORMAT;
		File file = new File(newFile);
		file.delete();
	}
}
