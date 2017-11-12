package assignment3.webserver.webquery;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import assignment3.api.APIQueryBuilder;
import assignment3.api.exceptions.QueryException;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Advanced Query")
public class AdvancedWebQueryProcessor implements WebQueryProcessor {

    private static final Map<String, Function<SchemaComparable, SchemaAggregate>> stringSchemaAggregateMap;

    private String htmlFile;
    private SchemaBase column1;
    private SchemaBase column2;

    private String yAxis;
    private String xAxis;

    static {
        ImmutableMap.Builder<String, Function<SchemaComparable, SchemaAggregate>> builder = ImmutableMap.builder();

        builder.put("count", SchemaCount::new);
        builder.put("max", SchemaMax::new);
        builder.put("min", SchemaMin::new);

        stringSchemaAggregateMap = builder.build();
    }

    @Override
    public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest query) throws QueryException, Exception {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();

        List<WebServerConstants.GraphTypeInfo> graphInfoList = WebServerConstants.TYPES_OF_GRAPH.stream()
                .filter( graphTypeInfo -> graphTypeInfo.graphName.equalsIgnoreCase(query.getValue("typeOfGraph")))
                .collect(Collectors.toList());

        if (graphInfoList.isEmpty() || graphInfoList.size() > 1) return false;

        builder.from(DEFAULT_CONFERENCE);
        builder = getColumnsToBeDisplayed(builder, query, graphInfoList.get(0));

        if (isNull(builder)) {
            return false;
        }

        builder = getConditions(builder, query);
        builder = getOrderBy(builder, query);
        builder = getLimitBy(builder, query);

        builder.build().executeAndSaveInCSV(graphInfoList.get(0).dataSourceFile);
        htmlFile = graphInfoList.get(0).htmlFileName;

        return true;
    }

    @Override
    public String getHtmlFileName() {
        return htmlFile + ".html?" + "yAxis=" + yAxis + "&xAxis=" + xAxis;
    }

    private APIQueryBuilder getLimitBy(APIQueryBuilder builder, WebRequest query) {
        if (query.getValue("limit").isEmpty()) return builder;

        return builder.limit(Integer.parseInt(query.getValue("limit")));
    }

    private APIQueryBuilder getOrderBy(APIQueryBuilder builder, WebRequest query) {
        if (query.getValue("columnsort").isEmpty()) return builder;

        SchemaBase orderByColumn = null;

        if (query.getValue("column1Name").equalsIgnoreCase(query.getValue("columnsort"))) {
            orderByColumn = getSchemaAttribute(query.getValue("column1Name"), query.getValue("column1ShowType"));

        } else if (query.getValue("column2Name").equalsIgnoreCase(query.getValue("columnsort"))) {
            orderByColumn = getSchemaAttribute(query.getValue("column2Name"), query.getValue("column2ShowType"));
        }

        if (isNull(orderByColumn)) return builder;

        APIQueryBuilder.OrderByRule orderByRule =
                (query.getValue("columnsortorder").equalsIgnoreCase("desc"))
                        ? APIQueryBuilder.OrderByRule.DESC : APIQueryBuilder.OrderByRule.ASC;

        return builder.orderBy(orderByColumn, orderByRule);
    }

    private APIQueryBuilder getConditions(APIQueryBuilder builder, WebRequest query) {
        // Get the number of conditions
        int numConditions = Integer.parseInt(query.getValue("numOfConditions"));

        String conditionCombine = "";
        SchemaPredicate condition = null;

        for (int i = 1; i <= numConditions; i ++) {
            String columnName = query.getValue("conditionColumn" + i) ;
            String comparator = query.getValue("conditionComparator" + i);
            String value = query.getValue("conditionValue" + i);

            SchemaComparable column = (SchemaComparable) WebServerConstants.COLUMNS.get(columnName);

            if (isNull(condition)) {
                condition = getSchemaPredicate(column, value, Collections.emptySet(), comparator);
            } else {
                if ("OR".equalsIgnoreCase(conditionCombine)) {
                    condition = condition.or(getSchemaPredicate(column, value, Collections.emptySet(), comparator));
                } else if ("AND".equalsIgnoreCase(conditionCombine)) {
                    condition = condition.and(getSchemaPredicate(column, value, Collections.emptySet(), comparator));
                }
            }

            conditionCombine = query.getValue("conditionCombine" + i);
        }

        if (isNull(condition)) condition = SchemaPredicate.ALWAYS_TRUE;

        if (column1 instanceof SchemaComparable) {
            condition = condition.and( ((SchemaComparable) column1).isNotNull());
        }

        if (column2 instanceof SchemaComparable) {
            condition = condition.and( ((SchemaComparable) column2).isNotNull());
        }

        return builder.where(condition);
    }

    private APIQueryBuilder getColumnsToBeDisplayed(APIQueryBuilder builder, WebRequest query, WebServerConstants.GraphTypeInfo graphInfo) {
        // Get columns to be displayed
        column1 = getSchemaAttribute(query.getValue("column1Name"), query.getValue("column1ShowType"));
        column2 = getSchemaAttribute(query.getValue("column2Name"), query.getValue("column2ShowType"));

        builder.select(column1.as(graphInfo.columnNames.get(0)), column2.as(graphInfo.columnNames.get(1)));

        xAxis = query.getValue("column1Name");
        yAxis = query.getValue("column2ShowType").toUpperCase() + "(" + query.getValue("column2Name") + ")";

        // If any of the columns is an aggregate, the other column will require a group by
        if ( column1 instanceof SchemaAggregate && column2 instanceof SchemaAggregate) {
            return null;

        } else if (column1 instanceof SchemaAggregate && column2 instanceof SchemaComparable) {
            builder.groupBy( (SchemaComparable) column2);

        } else if (column2 instanceof SchemaAggregate && column1 instanceof SchemaComparable) {
            builder.groupBy( (SchemaComparable) column1);

        } else {
            return null;
        }

        return builder;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private SchemaPredicate getSchemaPredicate(SchemaComparable attribute, String valueToCompare,
                                               Set valueSet, String type) {

        if (type.equals("gt")) {
            return attribute.greaterThan(Integer.parseInt(valueToCompare));
        } else if (type.equals("gte")) {
            return attribute.greaterThanOrEqualsTo(Integer.parseInt(valueToCompare));
        } else if (type.equals("lte")) {
            return attribute.lessThanOrEqualsTo(Integer.parseInt(valueToCompare));
        } else if (type.equals("lt")) {
            return attribute.lessThan(Integer.parseInt(valueToCompare));
        } else if (type.equals("like")) {
            return attribute.like(valueToCompare);
        } else if (type.equals("in") && valueSet != null) {
            return attribute.in(valueSet);
        } else if (type.equals("neq")) {
            return attribute.notEqualsTo(valueToCompare);
        }

        return attribute.equalsToIgnoreCase(valueToCompare);

    }

    @SuppressWarnings("rawtypes")
    private SchemaBase getSchemaAttribute(String attribute, String attributeDisplayOption) {
        SchemaBase schema = WebServerConstants.COLUMNS.get(attribute);

        if (isNull(schema) || !(schema instanceof SchemaComparable)) {
            return null;
        }

        if (stringSchemaAggregateMap.containsKey(attributeDisplayOption)) {
            return stringSchemaAggregateMap.get(attributeDisplayOption).apply( (SchemaComparable) schema);
        } else {
            return schema;
        }
    }
}
