package assignment3.storage;

public interface Storage {
	RetrievedFileData retrieveFile(String conferenceName) throws Exception;
	RetrievedFileData retrieveFile(String conferenceName, String fileLocation) throws Exception;

}
