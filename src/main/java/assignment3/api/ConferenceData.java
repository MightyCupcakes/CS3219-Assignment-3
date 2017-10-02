package assignment3.api;

import assignment3.schema.SchemaString;
import assignment3.schema.citations.SchemaCitation;

public final class ConferenceData {

    public static final SchemaString TITLE = new SchemaString("title");
    public static final SchemaString AUTHOR = new SchemaString("author");
    public static final SchemaString AFFILIATION = new SchemaString("affiliation");
    public static final SchemaString ABSTRACT_TEXT = new SchemaString("abstractText");
    public static final SchemaCitation CITATION = SchemaCitation.INSTANCE;

}