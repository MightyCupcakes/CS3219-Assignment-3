package assignment3.dataparser;

import java.util.ArrayList;
import java.util.List;

public class SerializedCitation {
    public String title;
    public int year;
    private List<String> authors;

    private SerializedCitation(String title, int year, List<String> authorsList) {
        this.title = title;
        this.year = year;
        this.authors = new ArrayList<>();

        authorsList.forEach(authors::add);
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
        }

        public SerializedCitation build() {
            return new SerializedCitation(this.title, this.year, this.authors);
        }
    }

}
