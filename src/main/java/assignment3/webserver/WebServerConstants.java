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

    public static final ImmutableList<String> TYPES_OF_PREMADE_VISUALS
            = ImmutableList.of("Transition over time", "Contemporary comparison", "Top N X of Y");

    public static final ImmutableMap<String, List<String>> PREMADE_QUERIES = ImmutableMap.of(
            "Transition over time", ImmutableList.of(
                "Number of citations for a conference over a few years",
                "Number of citations for different conferences"),
            "Contemporary comparison", ImmutableList.of(
                "Number of citations for two different conferences held in the same year",
                "?????"),
            "Top N X of Y", ImmutableList.of(
                "Top authors for a conference")
            );

    public static final ImmutableList<String> TYPES_OF_GRAPH = ImmutableList.of(
            "Bar Chart",
            "Line Chart"
    );

    public static final ImmutableMap<String, SchemaBase> COLUMNS = populateColumnNames();

    private static ImmutableMap<String, SchemaBase> populateColumnNames() {
        ImmutableMap.Builder<String, SchemaBase> columns = ImmutableMap.builder();

        columns.put("Journal Authors", ConferenceData.AUTHORS);
        columns.put("Journal Title", ConferenceData.TITLE);
        columns.put("Journal Venue", ConferenceData.VENUE);
        columns.put("Journal Published Year", ConferenceData.YEAR_OF_PUBLICATION);

        columns.put("Citation Title", ConferenceData.CITATION.title);
        columns.put("Citation Venue", ConferenceData.CITATION.citationVenue);
        columns.put("Citation Year", ConferenceData.CITATION.year);

        return columns.build();
    }
}
