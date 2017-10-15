package assignment3.api;

import java.util.Arrays;

import assignment3.schema.SchemaInt;
import assignment3.schema.SchemaString;
import assignment3.schema.citations.SchemaCitation;

public final class ConferenceData {

    public static final SchemaString TITLE = new SchemaString("title");
    public static final SchemaString AFFILIATION = new SchemaString("affiliation");
    public static final SchemaString ABSTRACT_TEXT = new SchemaString("abstractText");
    public static final SchemaString VENUE = new SchemaString("venue");
    public static final SchemaInt YEAR_OF_PUBLICATION = new SchemaInt("yearOfPublication");
    public static final SchemaString ID = new SchemaString("id");

    /**
     * Represents a single author in a journal authored by one or more authors.
     * Note this is a few order of magnitudes more expensive than querying all the authors at once.
     * This will also duplicate a single journal written by n authors n times so counting
     * operations will be affected.
     */
    public static final SchemaString AUTHOR = new SchemaString("author",
            journal -> Arrays.asList(journal.author.split(",")), SchemaString::createDuplicateJournalCitationAuthor );
    /**
     * Combines all authors in a journal into one entity for faster querying.
     */
    public static final SchemaString AUTHORS = new SchemaString("author");

    public static final SchemaInt NUM_OF_IN_CITATIONS = new SchemaInt("numOfInCitations");

    public static final SchemaCitation CITATION = SchemaCitation.INSTANCE;
}
