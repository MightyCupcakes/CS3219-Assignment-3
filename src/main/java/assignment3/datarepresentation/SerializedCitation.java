package assignment3.datarepresentation;

import static assignment3.datarepresentation.SerializedJournal.DEFAULT_JOURNAL_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SerializedCitation {

	private static final String EMPTY_BOOK_TITLE = "";

	public final String citationtitle;
    public final int year;
    public final String booktitle;
    public final String journalId;
    public final String citationVenue;
    public String authors;
    public final int numOfAuthors;

    public List<String> authorsList;

    private SerializedCitation(String title, int year, List<String> listOfAuthors,
    		String booktitle, String id, String citationVenue) {

        this.citationtitle = title;
        this.year = year;
        this.journalId = id;
        this.authorsList = new ArrayList<>();
        this.booktitle = isEmpty(booktitle) ? EMPTY_BOOK_TITLE : booktitle;
        this.citationVenue = citationVenue;
        authorsList.addAll(listOfAuthors);
        Collections.sort(authorsList);

        createAuthorsString();
        numOfAuthors = authorsList.size();
    }
    private boolean isEmpty(String value) {
    	return value == null;
    }

    private void createAuthorsString() {
        StringBuilder builder = new StringBuilder("");

        authorsList.forEach( author -> {
            if (builder.length() > 0) builder.append(",");
            builder.append(author);
        });

        this.authors = builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SerializedCitation
                && this.citationtitle.equals(((SerializedCitation) other).citationtitle)
                && this.year == ((SerializedCitation) other).year
                && this.authorsList.equals(((SerializedCitation) other).authorsList)
                && this.journalId.equals(((SerializedCitation) other).journalId)
                && this.booktitle.equals(((SerializedCitation) other).booktitle));
    }

    @Override
    public int hashCode() {
        return Objects.hash(authors, citationtitle, year);
    }

    public static class Builder {
        private List<String> authors;
        private int year = Integer.MIN_VALUE;
        private String title;
        private String id = DEFAULT_JOURNAL_ID;
        private String booktitle;
        private String citationVenue;

        public Builder() {
            authors = new ArrayList<>();
        }

        public Builder withYear(String year) {
            this.year = Integer.parseInt(year);

            return this;
        }

        public Builder withYear(int year) {
            this.year = year;

            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;

            return this;
        }

        public Builder withAuthor(String author) {
            authors.add(author);

            return this;
        }
        
        public Builder withBooktitle(String booktitle) {
        	this.booktitle = booktitle;
        	
        	return this;
        }

        public Builder withJournalId(String id) {
            this.id = id;

            return this;
        }
        public Builder withVenue(String citationVenue) {
        	this.citationVenue = citationVenue;
        	return this;
        }

        public SerializedCitation build() {
            return new SerializedCitation(this.title, this.year, this.authors, this.booktitle, this.id, this.citationVenue);
        }

    }

}
