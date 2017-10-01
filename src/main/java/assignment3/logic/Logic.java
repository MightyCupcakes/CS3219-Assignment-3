package assignment3.logic;

import java.io.IOException;
import java.util.List;

import assignment3.datarepresentation.SerializedJournalCitation;

public interface Logic {
    void parseAndSaveRawData(String folder) throws Exception;

    /**
     * Gets the data in the form of {@code SerializedJournalCitation}
     * from the specified tableName with no citation data (that is all citations
     * in {@code SerializedJournalCitation} is null.
     *
     * @param tableName the table the data is requested from
     * @return
     */
    List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName);

    /**
     * Gets the data in the form of {@code SerializedJournalCitation}
     * from the specified tableName with citation data. If a journal has more than one
     * citation, multiple copies of {@code SerializedJournalCitation} for each citation
     * should be returned in the list.
     *
     * @param tableName the table the data is requested from
     * @return
     */
    List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName);
}
