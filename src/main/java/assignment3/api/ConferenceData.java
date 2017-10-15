package assignment3.api;

import assignment3.schema.SchemaInt;
import assignment3.schema.SchemaString;
import assignment3.schema.citations.SchemaCitation;

public final class ConferenceData {

    public static final SchemaString TITLE = new SchemaString("title");
    public static final SchemaString AUTHOR = new SchemaString("author");
    public static final SchemaString AFFILIATION = new SchemaString("affiliation");
    public static final SchemaString ABSTRACT_TEXT = new SchemaString("abstractText");
    public static final SchemaString VENUE = new SchemaString("venue");
    public static final SchemaInt YEAR_OF_PUBLICATION = new SchemaInt("yearOfPublication");
    public static final SchemaInt NUM_OF_IN_CITATIONS = new SchemaInt("numOfInCitations");
    public static final SchemaString ID = new SchemaString("id");
    public static final SchemaCitation CITATION = SchemaCitation.INSTANCE;

}
