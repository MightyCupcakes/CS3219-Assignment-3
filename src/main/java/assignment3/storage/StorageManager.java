package assignment3.storage;

import static java.util.Objects.isNull;

import java.io.File;
import java.io.PrintWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import assignment3.dataparser.xmlparser.XmlDataParser;

public class StorageManager implements Storage {
	private final static String DEFAULT_STORAGE = "Dataset/";
	private final static String D3_STORAGE = "docs/d3charts/d3SavedData/";
	private final String SAVED_LOCATION = "Dataset/";

	private final String XML_FORMAT = ".xml";
    private final String CSV_FORMAT = ".csv";

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
		parser.parseCompiledFile(fileLocation + conferenceName + XML_FORMAT, "XmlTestDataCompiled");
		RetrievedFileData data = new RetrievedFileData(parser.getJournalMap(), parser.getCitationMap());

        return data;
	}

	@Override
	public void saveParsedXmlData(Document doc, String conferenceName) throws Exception {
		String outputLocation = SAVED_LOCATION + conferenceName + XML_FORMAT;
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(outputLocation));
		transformer.transform(source,  result);
	}

	private String getStorageLocation() {
        return isNull(location) ? DEFAULT_STORAGE : location;
    }

	@Override
	public void saveResulToCsvData(String data, String fileName) throws Exception {
		File csvFile = new File(D3_STORAGE + fileName + CSV_FORMAT);
		PrintWriter writer = new PrintWriter(csvFile);	
		writer.write(data);
		writer.close();
		
	}
}
