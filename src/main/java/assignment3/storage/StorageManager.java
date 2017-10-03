package assignment3.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import javafx.util.Pair;

public class StorageManager implements Storage {
	private final static String DEFAULT_STORAGE = "dataset/";
	private final String XML_FORMAT = ".xml";
	private XmlDataParser parser = new XmlDataParser();
		    

	@Override
	public Pair<HashMap<Integer, SerializedJournal>, HashMap<Integer, List<SerializedCitation>>> retrieveFile(String conferenceName) throws Exception  {
		parser.parseCompiledFile(DEFAULT_STORAGE + conferenceName + XML_FORMAT, conferenceName);
		Pair journalCitationPair = new Pair(parser.getJournalMap(), parser.getCitationMap());
		return journalCitationPair;
		
	}


	@Override
	public Pair<HashMap<Integer, SerializedJournal>, HashMap<Integer, List<SerializedCitation>>> retrieveFile(
			String conferenceName, String fileLocation) throws Exception {
		parser.parseCompiledFile(fileLocation + conferenceName + XML_FORMAT, conferenceName);
		Pair journalCitationPair = new Pair(parser.getJournalMap(), parser.getCitationMap());
		return journalCitationPair;
	} 
}
