package assignment3.logic;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import assignment3.datarepresentation.SerializedJournalCitation;

public interface Logic {
    void parseAndSaveRawData(String folder) throws Exception;
    void parseAndSaveRawJSONData(String file) throws Exception;
    void saveResultIntoCsv(String jsonStringData, int taskType) throws Exception;

    /**
     * Gets the data in the form of {@code SerializedJournalCitation}
     * from the specified tableName with no citation data (that is all citations
     * in {@code SerializedJournalCitation} is null.
     *
     * @param tableName the table the data is requested from
     * @return
     * @throws Exception 
     */
    List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName) throws Exception;

    /**
     * Gets the data in the form of {@code SerializedJournalCitation}
     * from the specified tableName with no citation data (that is all citations
     * in {@code SerializedJournalCitation} is null. Additionally, the journals will be duplicated
     * into multiple rows for each splitting function provided.
     *
     * @param tableName the table the data is requested from
     * @return
     * @throws Exception
     */
    List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName,
        Collection<Function<Object, Collection<Object>>> splittingFunctions) throws Exception;

    /**
     * Gets the data in the form of {@code SerializedJournalCitation}
     * from the specified tableName with citation data. If a journal has more than one
     * citation, multiple copies of {@code SerializedJournalCitation} for each citation
     * should be returned in the list.
     *
     * @param tableName the table the data is requested from
     * @return
     * @throws Exception 
     */
    List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName) throws Exception;

    /**
     * Gets the data in the form of {@code SerializedJournalCitation}
     * from the specified tableName wwith citation data. If a journal has more than one
     * citation, multiple copies of {@code SerializedJournalCitation} for each citation
     * should be returned in the list. Additionally, the journals will be duplicated
     * into multiple rows for each splitting function provided.
     *
     * @param tableName the table the data is requested from
     * @return
     * @throws Exception
     */
    List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName,
        Collection<Function<Object, Collection<Object>>> splittingFunctions) throws Exception;
}
