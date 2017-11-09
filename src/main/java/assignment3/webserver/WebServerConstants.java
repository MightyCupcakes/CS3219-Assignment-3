package assignment3.webserver;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import assignment3.api.ConferenceData;
import assignment3.schema.SchemaBase;

/**
 * A data class purely to hold the constants of this web server
 */
public final class WebServerConstants {

    public static final ImmutableList<String> TYPES_OF_GRAPH = ImmutableList.of(
            "Bar Chart",
            "Line Chart",
            "Citation Web"
    );

    public static final ImmutableMap<String, SchemaBase> COLUMNS = populateColumnNames();

    private static ImmutableMap<String, SchemaBase> populateColumnNames() {
        ImmutableMap.Builder<String, SchemaBase> columns = ImmutableMap.builder();

        columns.put("Journals", ConferenceData.ID);
        columns.put("Journal Authors", ConferenceData.AUTHORS);
        columns.put("Journal Title", ConferenceData.TITLE);
        columns.put("Journal Venue", ConferenceData.VENUE);
        columns.put("Journal Published Year", ConferenceData.YEAR_OF_PUBLICATION);

        columns.put("Citations", ConferenceData.CITATION.title);
        columns.put("Citation Title", ConferenceData.CITATION.title);
        columns.put("Citation Venue", ConferenceData.CITATION.citationVenue);
        columns.put("Citation Year", ConferenceData.CITATION.year);

        return columns.build();
    }

}
