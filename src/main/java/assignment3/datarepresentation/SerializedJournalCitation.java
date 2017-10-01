package assignment3.datarepresentation;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A container class to hold one journal to a particular citation
 */
public class SerializedJournalCitation {

    public final SerializedJournal journal;
    public final SerializedCitation citation;

    public SerializedJournalCitation(@Nonnull SerializedJournal journal, @Nullable SerializedCitation citation) {
        requireNonNull(journal);

        this.journal = journal;
        this.citation = citation;
    }
}
