package assignment3.logic;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment3.model.ModelManager;

public class LogicManagerTest {
	private static Logic logic;
	private final String STORAGE_LOCATION = "Dataset/";
	private final String XML_FORMAT = ".xml";
	private final String TEST_FOLDER = "src/test/data/Dtest";
	
	@BeforeClass
	public static void setUp() {
		logic = new LogicManager();
	}
	
	@Test
	public void testParseAndSaveRawData() throws Exception {
		String newFile = STORAGE_LOCATION + "Dtest" + XML_FORMAT;
		File file = new File(newFile);
		assertTrue(!file.exists());
		
		logic.parseAndSaveRawData(TEST_FOLDER);
		assertTrue(file.exists());
		file.delete();
		
	}
	
	@Test
	public void testGetDataWithNoCitation() {
		
	}
	
	@Test
	public void testGetDataWithCitation() {
		
	}
}
