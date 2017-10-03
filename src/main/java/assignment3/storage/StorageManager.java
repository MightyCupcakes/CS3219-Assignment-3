package assignment3.storage;

import assignment3.dataparser.xmlparser.XmlDataParser;

public class StorageManager implements Storage {
	private final static String DEFAULT_STORAGE = "dataset/";
	private final String XML_FORMAT = ".xml";
	private XmlDataParser parser = new XmlDataParser();
		    

	@Override
	public RetrievedFileData retrieveFile(String conferenceName) throws Exception  {
		parser.parseCompiledFile(DEFAULT_STORAGE + conferenceName + XML_FORMAT, conferenceName);
        RetrievedFileData data = new RetrievedFileData(parser.getJournalMap(), parser.getCitationMap());

		return data;
		
	}


	@Override
	public RetrievedFileData retrieveFile(String conferenceName, String fileLocation) throws Exception {
		parser.parseCompiledFile(fileLocation + conferenceName + XML_FORMAT, conferenceName);
        RetrievedFileData data = new RetrievedFileData(parser.getJournalMap(), parser.getCitationMap());

        return data;
	} 
}
