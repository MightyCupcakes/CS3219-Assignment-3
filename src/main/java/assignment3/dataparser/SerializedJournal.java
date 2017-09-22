package assignment3.dataparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerializedJournal {
    private final String title;
    private final String author;
    private final String affiliation;
    private final String abstractText;
    private final int yearOfPublication;

    private final List<SerializedCitation> citations;

    private SerializedJournal (String title,
                               String author,
                               String affiliation,
                               String abstractText,
                               int yearOfPublication,
                               List<SerializedCitation> citedArticles) {
        this.title = title;
        this.author = author;
        this.affiliation = affiliation;
        this.abstractText = abstractText;
        this.yearOfPublication = yearOfPublication;
        this.citations = new ArrayList<>();

        citedArticles.forEach(citations::add);

    }

    public static class Builder {
        private String title;
        private String author;
        private String affiliation;
        private String abstractText;
        private int yearOfPublication;

        private List<SerializedCitation> citations;

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

        public Builder withYear(int yearOfPublication) {
            this.yearOfPublication = yearOfPublication;

            return this;
        }

        public Builder withCitations(List<SerializedCitation> citations) {
            this.citations = citations;

            return this;
        }

        public SerializedJournal build() {
            return new SerializedJournal(title, author, affiliation, abstractText, yearOfPublication, citations);
        }
    }
}
