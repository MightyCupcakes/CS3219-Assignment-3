package assignment3.model;

import java.util.List;

import assignment3.datarepresentation.SerializedJournal;

public interface Model {

    /**
     * Saves the given list of SerializedJournal with the given conferenceName
     * @param journalList
     * @param conferenceName
     */
    void saveJournalData (List<SerializedJournal> journalList, String conferenceName) throws Exception;

    /**
     * Retrieves the list of SerializedJournal from the given conferenceName
     * @param conferenceName
     * @return
     */
    List<SerializedJournal> getJournalData(String conferenceName);
}
