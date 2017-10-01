package assignment3.datarepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerializedCitation {
	private static final String EMPTY_BOOK_TITLE ="";

	public String title;
    public int year;
    public String booktitle;
    public String authors;

    public List<String> authorsList;

    private SerializedCitation(String title, int year, List<String> listOfAuthors,
    		String booktitle) {

        this.title = title;
        this.year = year;
        this.authorsList = new ArrayList<>();
        this.booktitle = isEmpty(booktitle) ? EMPTY_BOOK_TITLE : booktitle;

        listOfAuthors.forEach(authorsList::add);
        Collections.sort(authorsList);

        createAuthorsString();
    }
    private boolean isEmpty(String value) {
    	return value == null;
    }

    private void createAuthorsString() {
        StringBuilder builder = new StringBuilder();

        authorsList.forEach( author -> {
            builder.append(author);
            builder.append(",");
        });

        this.authors = builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SerializedCitation
                && this.title.equals(((SerializedCitation) other).title)
                && this.year == ((SerializedCitation) other).year
                && this.authorsList.equals(((SerializedCitation) other).authorsList)
                && this.booktitle.equals(((SerializedCitation) other).booktitle));
    }

    public static class Builder {
        private List<String> authors;
        private int year;
        private String title;
        private String booktitle;

        public Builder() {
            authors = new ArrayList<>();
        }

        public Builder withYear(String year) {
            this.year = Integer.parseInt(year);

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

        public SerializedCitation build() {
            return new SerializedCitation(this.title, this.year, this.authors, this.booktitle);
        }

    }

}
