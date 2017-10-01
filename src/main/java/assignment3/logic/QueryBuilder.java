package assignment3.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import assignment3.api.APIQueryBuilder;
import assignment3.api.Query;
import assignment3.api.exceptions.QueryException;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.citations.CitationAttribute;

public class QueryBuilder implements APIQueryBuilder {

    private List<SchemaBase> selectColumns;
    private List<String> fromTables;
    private SchemaPredicate whereClause;
    private List<SchemaComparable> groupByClause;

    private static Logic logic;

    public static void setLogicTo(Logic controller) {
        QueryBuilder.logic = controller;
    }

    public static APIQueryBuilder createNewBuilder() {
        return new QueryBuilder();
    }

    private QueryBuilder() {
        selectColumns = new ArrayList<>();
        fromTables = new ArrayList<>();
        whereClause = SchemaPredicate.ALWAYS_TRUE;
        groupByClause = new ArrayList<>();
    }

    @Override
    public APIQueryBuilder select(SchemaBase... columns) {
        Arrays.stream(columns).forEach(selectColumns::add);
        return this;
    }

    @Override
    public APIQueryBuilder from(String... tables) {
        Arrays.stream(tables).forEach(fromTables::add);
        return this;
    }

    @Override
    public APIQueryBuilder where(SchemaPredicate predicate) {
        whereClause = predicate.copy();
        return this;
    }

    @Override
    public APIQueryBuilder groupBy(SchemaComparable... columns) {
        Arrays.stream(columns).forEach(groupByClause::add);
        return this;
    }

    @Override
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

        if (selectColumns.stream().anyMatch(schemaBase -> schemaBase instanceof CitationAttribute)) {

            List<SchemaComparable> normalColumns = new ArrayList<>();
            List<SchemaAggregate> aggregateColumns = new ArrayList<>();

            selectColumns.stream()
                    .filter(schemaBase -> schemaBase instanceof SchemaComparable)
                    .map(schemaBase -> (SchemaComparable) schemaBase)
                    .forEach(normalColumns::add);

            selectColumns.stream()
                    .filter(schemaBase -> schemaBase instanceof SchemaAggregate)
                    .map(schemaBase -> (SchemaAggregate) schemaBase)
                    .forEach(aggregateColumns::add);

            return new JoinTableQuery(aggregateColumns, normalColumns, whereClause, fromTables, groupByClause);

        } else if (selectColumns.stream().allMatch(schemaBase -> schemaBase instanceof SchemaComparable)) {
            // If all columns selected are normal comparable columns (like author, title) etc,
            // then it is a normal query.
            List<SchemaComparable> normalSelectColumns = new ArrayList<>();

            selectColumns.stream()
                    .map(schemaBase -> (SchemaComparable) schemaBase)
                    .forEach(normalSelectColumns::add);

            NormalQuery query = new NormalQuery(normalSelectColumns, whereClause, fromTables);
            query.setDataSource(logic);

            return query;

        } else {
            // Some columns are not normal columns as they are counting the number of rows or something.
            // First seperate the aggregate columns and the normal ones
            List<SchemaComparable> normalColumns = new ArrayList<>();
            List<SchemaAggregate> aggregateColumns = new ArrayList<>();

            selectColumns.stream()
                    .filter(schemaBase -> schemaBase instanceof SchemaComparable)
                    .map(schemaBase -> (SchemaComparable) schemaBase)
                    .forEach(normalColumns::add);

            selectColumns.stream()
                    .filter(schemaBase -> schemaBase instanceof SchemaAggregate)
                    .map(schemaBase -> (SchemaAggregate) schemaBase)
                    .forEach(aggregateColumns::add);

            AggregrateQuery query = new AggregrateQuery(aggregateColumns, normalColumns, whereClause, fromTables, groupByClause);
            query.setDataSource(logic);

            return query;
        }
    }

}
