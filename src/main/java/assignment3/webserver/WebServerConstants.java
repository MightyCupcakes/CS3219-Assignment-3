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

    public static final ImmutableList<GraphTypeInfo> TYPES_OF_GRAPH = ImmutableList.of(
            new GraphTypeInfo("Bar Chart", "1", ImmutableList.of("x", "y"),"BarChart"),
            new GraphTypeInfo("Line Chart", "2", ImmutableList.of("x", "y"), "LineChart"),
            new GraphTypeInfo("Citation Web", "", ImmutableList.of("x", "y"), "CollapsibleTree", false),
            new GraphTypeInfo("Donut Chat", "donutData", ImmutableList.of("x", "y"),"DonutChart")
    );

    public static final ImmutableList<PremadeQueriesInfo> PREMADE_QUERIES =
            ImmutableList.of(
                    new PremadeQueriesInfo("TransitionOverTime",
                            "Transition Over Time",
                            "Number of citations for a conference over a few years"),
                    new PremadeQueriesInfo("TransitionOverTime",
                            "Transition Over Time",
                            "Number of citations for a conference over a few years filtered by venue"),
                    new PremadeQueriesInfo("ContemporaryComparison",
                            "Contemporary comparison",
                            "Number of citations for different conferences in the same year"),
                    new PremadeQueriesInfo("ContemporaryComparison",
                    		"Contemporary comparison",
                    		"Number of citations for a conference"),
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
                    		"Citation Web Forest for specific year")
            );

    public static final ImmutableMap<String, SchemaBase> COLUMNS = populateColumnNames();

    private static ImmutableMap<String, SchemaBase> populateColumnNames() {
        ImmutableMap.Builder<String, SchemaBase> columns = ImmutableMap.builder();

        columns.put("Journals", ConferenceData.ID);
        columns.put("Journal Authors", ConferenceData.AUTHORS);
        columns.put("Journal Author", ConferenceData.AUTHOR);
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

    public static class GraphTypeInfo {
        /** The name of this type of graph **/
        public final String graphName;
        /** The name of the file that this graph is using as a datasource */
        public final String dataSourceFile;
        /** The column names the D3 expects for the data **/
        public final List<String> columnNames;
        /** The name of the d3 HTML file for this graph */
        public final String htmlFileName;
        /** Whether if this graph type is allowed in advanced query **/
        public final boolean showInAdvanced;

        public GraphTypeInfo (String graphName, String dataSourceFile, List<String> columns, String htmlFileName) {
            this (graphName, dataSourceFile, columns, htmlFileName, true);
        }

        public GraphTypeInfo (String graphName, String dataSourceFile, List<String> columns, String htmlFileName, boolean showInAdvanced) {
            this.graphName = graphName;
            this.dataSourceFile = dataSourceFile;
            this.htmlFileName = htmlFileName;
            this.columnNames = columns;
            this.showInAdvanced = showInAdvanced;
        }
    }
}
