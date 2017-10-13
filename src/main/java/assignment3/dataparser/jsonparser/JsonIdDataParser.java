package assignment3.dataparser.jsonparser;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import assignment3.datarepresentation.SerializedJournal;

public class JsonIdDataParser {

    private Map<String, SerializedJournal> journalMap;

    public JsonIdDataParser() {
        journalMap = new HashMap<>();
    }

    public void addJournal(SerializedJournal journal) {
        journalMap.put(journal.id, journal);
    }

    public void linkCitationsIdToJournal() {
        for (SerializedJournal journalEntry : journalMap.values()) {
            List<String> citations = journalEntry.outCitationsId;

            for (String id : citations) {
                SerializedJournal journal = journalMap.get(id);

                if (!isNull(journal)) {
                    journalEntry.citeJournal(journal);
                }
            }
        }
    }

    public List<SerializedJournal> getJournals() {
        return new ArrayList<>(journalMap.values());
    }
}
