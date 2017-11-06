package assignment3.logic;

import java.util.Collection;
import java.util.List;

import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;

public interface Logic {
    void parseAndSaveRawData(String folder) throws Exception;
    void parseAndSaveRawJSONData(String file) throws Exception;
    void saveResultIntoCsv(String jsonStringData, String filename) throws Exception;

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
     * into multiple rows for each splitting schemas provided.
     *
     * @param tableName the table the data is requested from
     * @return
     * @throws Exception
     */
    List<SerializedJournalCitation> getDataFromTableWithNoCitations(String tableName,
        Collection<SchemaBase> schemas) throws Exception;

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
     * into multiple rows for each splitting schemas provided.
     *
     * @param tableName the table the data is requested from
     * @return
     * @throws Exception
     */
    List<SerializedJournalCitation> getDataFromTableWithCitations(String tableName,
        Collection<SchemaBase> schemas) throws Exception;
}
