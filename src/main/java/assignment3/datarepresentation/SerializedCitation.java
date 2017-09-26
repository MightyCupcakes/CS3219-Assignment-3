package assignment3.datarepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerializedCitation {
    public String title;
    public int year;
    public List<String> authors;

    private SerializedCitation(String title, int year, List<String> authorsList) {
        this.title = title;
        this.year = year;
        this.authors = new ArrayList<>();

        authorsList.forEach(authors::add);
        Collections.sort(authorsList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SerializedCitation
                && this.title.equals(((SerializedCitation) other).title)
                && this.year == ((SerializedCitation) other).year
                && this.authors.equals(((SerializedCitation) other).authors));
    }

    public static class Builder {
        private List<String> authors;
        private int year;
        private String title;

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

        public SerializedCitation build() {
            return new SerializedCitation(this.title, this.year, this.authors);
        }
    }

}
