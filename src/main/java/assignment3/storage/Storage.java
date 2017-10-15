package assignment3.storage;

import org.w3c.dom.Document;

public interface Storage {
	RetrievedFileData retrieveFile(String conferenceName) throws Exception;
	RetrievedFileData retrieveFile(String conferenceName, String fileLocation) throws Exception;
	void saveParsedXmlData(Document doc, String conferenceName) throws Exception;
	void saveResulToCsvData(String data, String fileName) throws Exception;
}
