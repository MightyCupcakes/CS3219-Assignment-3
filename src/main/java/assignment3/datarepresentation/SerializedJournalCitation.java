package assignment3.datarepresentation;

import java.util.List;

/**
 * A container class to hold one journal to a particular citation
 */
public class SerializedJournalCitation {

    public final SerializedJournal journal;
    public final SerializedCitation citation;

    public SerializedJournalCitation(SerializedJournal journal, SerializedCitation citation) {
        this.journal = journal;
        this.citation = citation;
    }
}
