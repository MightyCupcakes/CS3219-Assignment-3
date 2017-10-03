package assignment3.storage;

import static java.util.Objects.isNull;

import assignment3.dataparser.xmlparser.XmlDataParser;

public class StorageManager implements Storage {
	private final static String DEFAULT_STORAGE = "dataset/";

	private final String XML_FORMAT = ".xml";
	private XmlDataParser parser = new XmlDataParser();
	private String location;

	public StorageManager() {
	    this.location = null;
    }
    
    public StorageManager(String storageLocation) {
        this.location = storageLocation;
    }

	@Override
	public RetrievedFileData retrieveFile(String conferenceName) throws Exception  {
		parser.parseCompiledFile(getStorageLocation() + conferenceName + XML_FORMAT, conferenceName);
        RetrievedFileData data = new RetrievedFileData(parser.getJournalMap(), parser.getCitationMap());

		return data;
		
	}

	@Override
	public RetrievedFileData retrieveFile(String conferenceName, String fileLocation) throws Exception {
		parser.parseCompiledFile(fileLocation + conferenceName + XML_FORMAT, conferenceName);
        RetrievedFileData data = new RetrievedFileData(parser.getJournalMap(), parser.getCitationMap());

        return data;
	}

	private String getStorageLocation() {
        return isNull(location) ? DEFAULT_STORAGE : location;
    }
}
