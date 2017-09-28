package assignment3.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import assignment3.logic.NormalQuery;
import assignment3.api.exceptions.QueryException;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class QueryBuilder {

    private List<SchemaBase> selectColumns;
    private List<String> fromTables;
    private SchemaPredicate whereClause;
    private List<SchemaComparable> groupByClause;

    public static QueryBuilder createNewBuilder() {
        return new QueryBuilder();
    }

    private QueryBuilder() {
        selectColumns = new ArrayList<>();
        fromTables = new ArrayList<>();
        whereClause = null;
        groupByClause = new ArrayList<>();
    }

    public QueryBuilder select(SchemaBase... columns) {
        Arrays.stream(columns).forEach(selectColumns::add);
        return this;
    }

    public QueryBuilder from(String... tables) {
        Arrays.stream(tables).forEach(fromTables::add);
        return this;
    }

    public QueryBuilder where(SchemaPredicate predicate) {
        whereClause = predicate.copy();
        return this;
    }

    public QueryBuilder groupBy(SchemaComparable... columns) {
        Arrays.stream(columns).forEach(groupByClause::add);
        return this;
    }

    public Query build() throws QueryException {
        if (selectColumns.isEmpty()) {
            throw new QueryException("At least one column needs to be selected");
        }

        if (!groupByClause.isEmpty()) {
            // If group by is not empty, then check that all columns to be printed out are in the group by clause
            boolean allMatch = selectColumns.stream()
                    .filter(schemaBase -> schemaBase instanceof SchemaComparable)
                    .allMatch(groupByClause::contains);

            if (!allMatch) {
                throw new QueryException("Only attributes in the group by clause and aggregrate queries can be used in the select clause.");
            }
        }

        if (selectColumns.stream().allMatch(schemaBase -> schemaBase instanceof SchemaComparable)) {
            List<SchemaComparable> normalSelectColumns = new ArrayList<>();

            selectColumns.stream()
                    .map(schemaBase -> (SchemaComparable) schemaBase)
                    .forEach(normalSelectColumns::add);

            return new NormalQuery(normalSelectColumns, whereClause, fromTables);
        }

        return null;
    }

}
