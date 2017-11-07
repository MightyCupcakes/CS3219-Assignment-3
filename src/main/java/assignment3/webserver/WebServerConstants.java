package assignment3.webserver;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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
}
