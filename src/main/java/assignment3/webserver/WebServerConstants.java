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

    public static final ImmutableList<PremadeQueriesInfo> PREMADE_QUERIES =
            ImmutableList.of(
                    new PremadeQueriesInfo("TransitionOverTime",
                            "Transition Over Time",
                            "Number of citations for a conference over a few years"),
                    new PremadeQueriesInfo("TransitionOverTimeMultipleConferences",
                            "Transition Over Time",
                            "Number of citations for different conferences"),
                    new PremadeQueriesInfo("ContemporaryComparison",
                            "Contemporary comparison",
                            "Number of citations for two different venue held in the same year"),
                    new PremadeQueriesInfo("TopNXOFY",
                            "Top N X of Y",
                            "Construct Top N X of Y Graph"),
                    new PremadeQueriesInfo("Advanced",
                    		"Advanced Query",
                    		"Advanced"),
                    new PremadeQueriesInfo("CitationWebForBasePaper",
                    		"Citation Web",
                    		"Citation Web for base paper"),
                    new PremadeQueriesInfo("CitationWebForest",
                    		"Citation Web",
                    		"Citation Web Forest in range of years")
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

    public static class PremadeQueriesInfo {
        public final String htmlFile;
        public final String category;
        public final String name;

        public PremadeQueriesInfo(String htmlFile, String category, String name) {
            this.htmlFile = htmlFile;
            this.category = category;
            this.name = name;
        }
    }
}
