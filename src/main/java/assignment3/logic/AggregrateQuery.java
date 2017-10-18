package assignment3.logic;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import assignment3.api.APIQueryBuilder;
import assignment3.api.Query;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaAggregate;

public class AggregrateQuery extends QueryBase {
    private Set<SchemaAggregate> aggregateColumnsToShow;
    private Set<SchemaComparable> normalColumnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;
    private List<SchemaComparable> groupByColumns;

    protected List<SerializedJournalCitation> journals;

    private Multimap<Set<Object>, SerializedJournalCitation> groupedRows;

    public AggregrateQuery(List<SchemaAggregate> aggregatecolumnsToShow,
                           List<SchemaComparable> normalColumnsToShow,
                           SchemaPredicate predicate,
                           List<String> tablesToRead,
                           List<SchemaComparable> groupByColumns) {

        super();

        this.aggregateColumnsToShow = new HashSet<>(aggregatecolumnsToShow);
        this.normalColumnsToShow = new HashSet<>(normalColumnsToShow);
        this.predicate = predicate;
        this.tablesToRead = tablesToRead;
        this.groupByColumns = Collections.unmodifiableList(groupByColumns);

        this.groupedRows = MultimapBuilder.ListMultimapBuilder.hashKeys().arrayListValues().build();
    }

    private void groupRows(List<SerializedJournalCitation> data) {

        for (SerializedJournalCitation row : data) {
            Set<Object> group = new LinkedHashSet<>(groupByColumns.size());

            groupByColumns.forEach( column -> group.add(column.getValue(row)));
            groupedRows.put(group, row);
        }
    }

    public String executeAndGetResult(List<SerializedJournalCitation> journalCitations) {

        JsonGenerator json = getJsonGenerator();

        if (groupByColumns.isEmpty()) {
            // No group by means all one big group (we assume the query is valid at this stage
            // since invalid queries will throw an exception during QueryBuilder)
            JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

            journalCitations.forEach(
                    journal -> {
                        aggregateColumnsToShow
                                .forEach(schemaAggregate -> schemaAggregate.accumulate(journal));

                    }
            );

            aggregateColumnsToShow.forEach(schemaAggregate ->
                    rowJson.generateJson(schemaAggregate, schemaAggregate.getResult()));

            json.addObjectToArray(rowJson);

            return json.getJsonString();

        } else {
            groupRows(journalCitations);

            for (Set<Object> key : groupedRows.keySet()) {
                JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

                Iterable<SerializedJournalCitation> rows = groupedRows.get(key);
                List<Object> keyValues = new ArrayList<>(key);

                // Print out the groups to the json as columns
                // Like if it is group by names, print out the name first (if they are in normal columns to show)
                // if they are not then the user only wants to group journals by these columns but not show them
                // as columns in the results
                for (int i = 0; i < groupByColumns.size(); i ++) {
                    if (!normalColumnsToShow.contains(groupByColumns.get(i))) {
                        continue;
                    }

                    rowJson.generateJson(groupByColumns.get(i), keyValues.get(i));
                }

                // For each row in the group, pass the value to the aggregate function
                // For example if the aggregate function is a count, it simply counts the number
                // of SerializedJournal in the group and etc. Here we don't care what the aggregate function
                // is going to do - we just pass it to them
                for (SerializedJournalCitation row : rows) {
                    // Pass it to the aggregate function (so that they can count or do something to it)
                    aggregateColumnsToShow.forEach(schemaAggregate -> schemaAggregate.accumulate(row));
                }

                // Then print out the aggregate for this group by asking the aggregate for the result
                // Like the number of names for a count aggregate
                aggregateColumnsToShow.forEach(schemaAggregate ->
                        rowJson.generateJson(schemaAggregate, schemaAggregate.getResult()));

                json.addObjectToArray(rowJson);
            }

            return json.getJsonString();
        }

    }

    @Override
    public List<SchemaBase> getColumnsSelected() {
        List<SchemaBase> list = new ArrayList<>();
        list.addAll(aggregateColumnsToShow);
        list.addAll(normalColumnsToShow);

        return Collections.unmodifiableList(list);
    }

    @Override
    public String execute() {

        if (!isNull(logic)) {

            journals = new ArrayList<>(150);

            List<SchemaBase> splitters = new ArrayList<>(1);

            if (normalColumnsToShow.stream().anyMatch(schema ->schema.requireSplitRow()) ||
                    groupByColumns.stream().anyMatch(schema -> schema.requireSplitRow())) {

                if (!groupByColumns.isEmpty()) {
                    // Whatever is in group by clause should be in the select clause so no point
                    // going through the select clause if the group by clause is non-empty
                    groupByColumns.stream()
                            .filter(schema -> schema.requireSplitRow())
                            .forEach(splitters::add);
                } else {
                    normalColumnsToShow.stream()
                            .filter(schema -> schema.requireSplitRow())
                            .forEach(splitters::add);
                }
            }

            try {
                journals = getDataFromLogicLayer(splitters, tablesToRead);
            } catch (Exception e) {
                Logger.getLogger(this.getClass().toString())
                        .warning("Exception thrown while trying to get data from LogicLayer");
                e.printStackTrace();
                return EMPTY_JSON;
            }
        }

        journals = filterOutRows(journals, predicate);

        return executeAndGetResult(journals);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AggregrateQuery
                && this.aggregateColumnsToShow.equals(((AggregrateQuery) other).aggregateColumnsToShow)
                && this.normalColumnsToShow.equals(((AggregrateQuery) other).normalColumnsToShow)
                && this.tablesToRead.equals(((AggregrateQuery) other).tablesToRead)
                && this.groupByColumns.equals(((AggregrateQuery) other).groupByColumns)
                && this.predicate.equals(((AggregrateQuery) other).predicate)
                && ( (this.orderByColumn == null && ((AggregrateQuery) other).orderByColumn == null)
                || this.orderByColumn.equals(((AggregrateQuery) other).orderByColumn) )
                && this.orderByRule.equals( ((AggregrateQuery) other).orderByRule )
                && this.limitRows == ((AggregrateQuery) other).limitRows);
    }
}
