package assignment3.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import javax.json.JsonArray;

import com.google.common.collect.ImmutableMap;

import assignment3.api.APIQueryBuilder;
import assignment3.schema.SchemaBase;

public class JsonSorter extends JsonGenerator {

    private final String sortByField;
    private final APIQueryBuilder.OrderByRule orderByRule;
    private final List<JsonGeneratorBuilder> builderList;
    private final Comparator<JsonGeneratorBuilder> listComparator;

    private static final ImmutableMap<APIQueryBuilder.OrderByRule, Function<Integer, Integer>> comparingFunction =
            ImmutableMap.of(
                    APIQueryBuilder.OrderByRule.ASC, i -> i,
                    APIQueryBuilder.OrderByRule.DESC, i -> (i > 0) ? -1: (i < 0) ? 1 : 0
            );

    public JsonSorter(SchemaBase sortBy, APIQueryBuilder.OrderByRule rule) {
        this.sortByField = sortBy.getNameOfAttribute();
        this.orderByRule = rule;
        this.builderList = new ArrayList<>();

        this.listComparator = (row1, row2) ->
                comparingFunction.getOrDefault(orderByRule, i -> i).apply(
                        row1.keyValuePair.get(sortByField).compareTo(row2.keyValuePair.get(sortByField))
                );
    }

    public JsonSorter(SchemaBase sortBy, APIQueryBuilder.OrderByRule rule, int limit) {
        this(sortBy, rule);
        numOfRowsToParse = limit;
    }

    @Override
    public void addObjectToArray(JsonGeneratorBuilder generator) {
        builderList.add(generator);
    }

    @Override
    public JsonArray getJsonArray() {
        builderList.sort(listComparator);

        int numRows = 0;

        for (JsonGeneratorBuilder jsonGeneratorBuilder : builderList) {
            if (numOfRowsToParse > 0 && numRows == numOfRowsToParse) {
                break;
            }

            listOfObjects.add(jsonGeneratorBuilder.builder);
            numRows++;
        }
        return listOfObjects.build();
    }
}
