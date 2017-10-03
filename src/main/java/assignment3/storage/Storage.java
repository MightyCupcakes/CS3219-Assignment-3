package assignment3.storage;

import java.util.HashMap;
import java.util.List;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import javafx.util.Pair;

public interface Storage {
	Pair<HashMap<Integer, SerializedJournal>, HashMap<Integer, List<SerializedCitation>>> retrieveFile(String conferenceName) throws Exception;
	Pair<HashMap<Integer, SerializedJournal>, HashMap<Integer, List<SerializedCitation>>> retrieveFile(String conferenceName, String fileLocation) throws Exception;

}
