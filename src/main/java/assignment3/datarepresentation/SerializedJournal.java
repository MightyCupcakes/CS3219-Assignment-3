package assignment3.datarepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SerializedJournal {

    public static final String DEFAULT_JOURNAL_ID = String.valueOf(Integer.MIN_VALUE);

    public final String id;
    public final String journalId;
    public final String title;
    public final String author;
    public final String affiliation;
    public final String abstractText;
    public final String conference;
    public final String venue;
    public final int yearOfPublication;
    public final int numOfInCitations;

    public List<SerializedCitation> citations;

    public List<String> outCitationsId;

    protected SerializedJournal (String id,
    						   String journalId,
                               String title,
                               String author,
                               String affiliation,
                               String abstractText,
                               String conference,
                               String venue,
                               int numOfInCitations,
                               int yearOfPublication,
                               List<String> outCitationsId,
                               List<SerializedCitation> citedArticles) {
        this.id = id;
        this.journalId = journalId;
        this.title = title;
        this.author = author;
        this.affiliation = affiliation;
        this.abstractText = abstractText;
        this.yearOfPublication = yearOfPublication;
        this.conference = conference;
        this.venue = venue;
        this.numOfInCitations = numOfInCitations;
        this.outCitationsId = outCitationsId;
        this.citations = new ArrayList<>();

        citedArticles.forEach(citations::add);

    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, yearOfPublication);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SerializedJournal
                && this.title.equals(((SerializedJournal) other).title)
                && this.author.equals(((SerializedJournal) other).author)
                && this.conference.equals(((SerializedJournal) other).conference)
                && this.yearOfPublication == ((SerializedJournal) other).yearOfPublication);
    }

    public void citeJournal(SerializedJournal journal) {
        SerializedCitation.Builder builder = new SerializedCitation.Builder();

        builder
                .withAuthor(journal.author)
                .withTitle(journal.title)
                .withYear(journal.yearOfPublication)
                .withJournalId(journal.id);

        citations.add(builder.build());
    }

    public static class Builder {
        private String title = "";
        private String affiliation;
        private String abstractText;
        private String conference = "";
        private String venue = "";
        private String id = DEFAULT_JOURNAL_ID;
        private String journalId = "";
        public int numInCitations= 0;
        private int yearOfPublication;

        private StringBuilder author = new StringBuilder("");

        private List<SerializedCitation> citations = Collections.emptyList();
        private List<String> citationsId = new ArrayList<>();

        public Builder withTitle(String title) {
            this.title = title;

            return this;
        }

        public Builder withAuthor(String author) {
            if(this.author.length() > 0) {
                this.author.append(",");
            }

            this.author.append(author);

            return this;
        }

        public Builder withAffiliation(String affiliation) {
            this.affiliation = affiliation;

            return this;
        }

        public Builder withAbstract(String abstractText) {
            this.abstractText = abstractText;

            return this;
        }

        public Builder withYear(String yearOfPublication) {

            this.yearOfPublication = Integer.parseInt(yearOfPublication);

            return this;
        }

        public Builder withYear(int yearOfPublication) {
            this.yearOfPublication = yearOfPublication;

            return this;
        }

        public Builder withCitations(List<SerializedCitation> citations) {
            this.citations = citations;

            return this;
        }

        public Builder withCitationId(String id) {
            this.citationsId.add(id);

            return this;
        }

        public Builder withInCitation(String id) {
            numInCitations++;

            return this;
        }
        public Builder withInCitationTotal(String id) {
        	this.numInCitations = Integer.parseInt(id);
        	return this;
        }
        public Builder withInCitationTotal(int id) {
        	this.numInCitations = id;
        	return this;
        }

        public Builder withConference(String conference) {
            this.conference = conference;

            return this;
        }

        public Builder withId(String id) {
            this.id = id;

            return this;
        }
        
        public Builder withJournalId(String id) {
        	this.journalId = id;
        	return this;
        }

        public Builder withVenue(String venue) {
            this.venue = venue;

            return this;
        }

        public SerializedJournal build() {
            return new SerializedJournal(id,
            		journalId,
                    title,
                    author.toString(),
                    affiliation,
                    abstractText,
                    conference,
                    venue,
                    numInCitations,
                    yearOfPublication,
                    citationsId,
                    citations
            );
        }
    }
}
