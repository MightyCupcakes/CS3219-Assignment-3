package assignment3.model;

import java.util.List;
import java.util.Map;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournal;

public interface Model {

    /**
     * Saves the given list of SerializedJournal with the given conferenceName
     * @param journalList
     * @param conferenceName
     */
    void saveJournalData (List<SerializedJournal> journalList, String conferenceName) throws Exception;
    /**
     * Write result into a .csv file which D3 will directly load 
     * The first list contained the column names
     * subsequent lists will contain the actual data
     */
    void  writeResultIntoCsvFile(String filename, List<List<String>> resultLists) throws Exception;
    /**
     * Retrieves the HashMap<Integer, SerializedJournal>  from the given conferenceName with Integer representing the specific document in that conferenceName
     * @param conferenceName
     * @return
     * @throws Exception 
     */
    Map<Integer, SerializedJournal> getJournal(String conferenceName) throws Exception;
    /**
     * Retrieves the  HashMap<Integer, List<SerializedCitation>>from the given conferenceName with Integer representing the list of citations for that specific document in that confernceName
     * @param conferenceName
     * @return
     * @throws Exception 
     */
    Map<Integer, List<SerializedCitation>> getCitations(String conferenceName) throws Exception;
    
    
    /**
     * return journal/citations that are mapped to a given conference as key
     * @return
     */
    Map<String, Map<Integer, SerializedJournal>> getJournalMap();
    Map<String, Map<Integer, List<SerializedCitation>>> getCitationMap();

}
