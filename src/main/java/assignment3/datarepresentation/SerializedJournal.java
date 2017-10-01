package assignment3.datarepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SerializedJournal {
    public final String title;
    public final String author;
    public final String affiliation;
    public final String abstractText;
    public final String conference;
    public final int yearOfPublication;

    public final List<SerializedCitation> citations;

    protected SerializedJournal (String title,
                               String author,
                               String affiliation,
                               String abstractText,
                               String conference,
                               int yearOfPublication,
                               List<SerializedCitation> citedArticles) {
        this.title = title;
        this.author = author;
        this.affiliation = affiliation;
        this.abstractText = abstractText;
        this.yearOfPublication = yearOfPublication;
        this.conference = conference;
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

    public static class Builder {
        private String title;
        private String author;
        private String affiliation;
        private String abstractText;
        private String conference;
        private int yearOfPublication;

        private List<SerializedCitation> citations = Collections.emptyList();

        public Builder withTitle(String title) {
            this.title = title;

            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;

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

        public Builder withConference(String conference) {
            this.conference = conference;

            return this;
        }

        public SerializedJournal build() {
            return new SerializedJournal(title, author, affiliation, abstractText, conference, yearOfPublication, citations);
        }
    }
}
