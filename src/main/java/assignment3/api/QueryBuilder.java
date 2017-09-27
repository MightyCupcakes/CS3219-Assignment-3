package assignment3.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        whereClause = predicate;
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
