package assignment3.webserver.webquery;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import assignment3.api.APIQueryBuilder;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Advanced Query")
public class AdvancedWebQueryProcessor implements WebQueryProcessor {

    @Override
    public boolean processAndSaveIntoCSV(WebServerManager manager,WebRequest query) {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();

        // Get columns to be displayed
        SchemaBase column1 = getSchemaAttribute(query.getValue("column1Name"), query.getValue("column1ShowType"));
        SchemaBase column2 = getSchemaAttribute(query.getValue("column2Name"), query.getValue("column2ShowType"));

        builder.select(column1, column2);
        builder.from(DEFAULT_CONFERENCE);

        // If any of the columns is an aggregate, the other column will require a group by
        if ( column1 instanceof SchemaAggregate && column2 instanceof SchemaAggregate) {
            return false;

        } else if (column1 instanceof SchemaAggregate && column2 instanceof SchemaComparable) {
            builder.groupBy( (SchemaComparable) column2);

        } else if (column2 instanceof SchemaAggregate && column1 instanceof SchemaComparable) {
            builder.groupBy( (SchemaComparable) column1);

        } else {
            return false;
        }

        // Get the number of conditions
        int numConditions = Integer.parseInt(query.getValue("numOfConditions"));

        String conditionCombine = "";
        SchemaPredicate condition = null;

        for (int i = 1; i <= numConditions; i ++) {
            String columnName = query.getValue("conditionColumn" + i) ;
            String comparator = query.getValue("conditionComparator" + i);
            String value = query.getValue("conditionValue" + i);

            SchemaComparable column = (SchemaComparable) WebServerConstants.COLUMNS.get(columnName);

            if (!isNull(condition)) {
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

        if (!isNull(condition)) {
            builder.where(condition);
        }

        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private SchemaPredicate getSchemaPredicate(SchemaComparable attribute, String valueToCompare,
                                               Set valueSet, String type){
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
        }

        return attribute.equalsTo(valueToCompare);

    }

    @SuppressWarnings("rawtypes")
    private SchemaBase getSchemaAttribute(String attribute, String attributeDisplayOption) {
        SchemaBase schema = WebServerConstants.COLUMNS.get(attribute);

        if (isNull(schema) || !(schema instanceof SchemaComparable)) {
            return null;
        }

        Optional<WebQuery.DisplayOption> option = WebQuery.DisplayOption.getOptionWithStringValue(attributeDisplayOption);

        if (option.isPresent()) {
            return option.get().converterFunction.apply( (SchemaComparable) schema);
        } else {
            return null;
        }
    }

    @Override
    public String getHtmlFileName() {
        return null;
    }
}
