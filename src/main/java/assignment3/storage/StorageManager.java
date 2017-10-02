package assignment3.storage;

import java.io.File;
import java.io.FileNotFoundException;

import assignment3.dataparser.xmlparser.XmlDataParser;
import assignment3.datarepresentation.SerializedJournal;

public class StorageManager implements Storage {
	private final static String STORAGE_LOCATION = "dataset/";
	private final String XML_FORMAT = ".xml";
	private XmlDataParser parser = new XmlDataParser();
		    

	@Override
	public SerializedJournal retrieveFile(String conferenceName) throws Exception  {
		parser.parseFile(STORAGE_LOCATION + conferenceName + XML_FORMAT);
		return parser.getJournal();
		
	} 
}
