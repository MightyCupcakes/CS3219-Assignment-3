package assignment3.model;

import java.util.HashMap;
import java.util.List;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;
import javafx.util.Pair;

public interface Model {

    /**
     * Saves the given list of SerializedJournal with the given conferenceName
     * @param journalList
     * @param conferenceName
     */
    void saveJournalData (List<SerializedJournal> journalList, String conferenceName) throws Exception;

    /**
     * Retrieves the HashMap<Integer, SerializedJournal>  from the given conferenceName with Integer representing the specific document in that conferenceName
     * @param conferenceName
     * @return
     * @throws Exception 
     */
    HashMap<Integer, SerializedJournal> getJournal(String conferenceName) throws Exception;
    /**
     * Retrieves the  HashMap<Integer, List<SerializedCitation>>from the given conferenceName with Integer representing the list of citations for that specific document in that confernceName
     * @param conferenceName
     * @return
     * @throws Exception 
     */
    HashMap<Integer, List<SerializedCitation>> getCitations(String conferenceName) throws Exception;
}
