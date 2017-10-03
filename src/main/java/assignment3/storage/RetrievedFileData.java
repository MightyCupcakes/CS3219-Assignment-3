package assignment3.storage;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

/**
 * A storage class to hold the journal and citations data retrieved
 * from a file
 */
public class RetrievedFileData {

    public final Map<Integer, SerializedJournal> journalsMap;
    public final Map<Integer, List<SerializedCitation>> citationsMap;

    public RetrievedFileData(Map<Integer, SerializedJournal> journalsMap, Map<Integer, List<SerializedCitation>> citationsMap) {
        requireNonNull(journalsMap);
        requireNonNull(citationsMap);

        this.journalsMap = journalsMap;
        this.citationsMap = citationsMap;
    }
}
