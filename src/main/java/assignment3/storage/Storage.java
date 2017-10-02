package assignment3.storage;

import assignment3.datarepresentation.SerializedJournal;

public interface Storage {
	SerializedJournal retrieveFile(String conferenceName) throws Exception;
}
