package assignment3.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import assignment3.api.Query;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class AggregrateQuery implements Query {
    private Set<SchemaAggregate> aggregateColumnsToShow;
    private Set<SchemaComparable> normalColumnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;
    private List<SchemaComparable> groupByColumns;
    private Logic logic;

    protected List<SerializedJournal> journals;

    private Multimap<List<Object>, SerializedJournal> groupedRows;

    public AggregrateQuery(List<SchemaAggregate> columnsToShow,
                           List<SchemaComparable> normalColumnsToShow,
                           SchemaPredicate predicate,
                           List<String> tablesToRead,
                           List<SchemaComparable> groupByColumns) {

        this.aggregateColumnsToShow = new HashSet<>(columnsToShow);
        this.normalColumnsToShow = new HashSet<>(normalColumnsToShow);
        this.predicate = predicate;
        this.tablesToRead = tablesToRead;
        this.groupByColumns = Collections.unmodifiableList(groupByColumns);

        this.groupedRows = HashMultimap.create();
    }

    private List<SerializedJournal> filterOutRows(List<SerializedJournal> data) {
        return data.stream()
                .filter(serializedJournal -> predicate.test(serializedJournal))
                .collect(Collectors.toList());
    }

    private void groupRows(List<SerializedJournal> data) {

        for (SerializedJournal row : data) {
            List<Object> group = new ArrayList<>();

            groupByColumns.forEach( column -> group.add(column.getValue(row)));
            groupedRows.put(group, row);
        }
    }

    @Override
    public String execute() {

        // TODO: Get data from logic instead of an empty list like this
        journals = filterOutRows(journals);

        JsonGenerator json = new JsonGenerator();

        if (groupByColumns.isEmpty()) {
            // No group by means all one big group (we assume the query is valid at this stage
            // since invalid queries will throw an exception during QueryBuilder)
            JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

            journals.forEach(
                    journal -> aggregateColumnsToShow.forEach(schemaAggregate -> schemaAggregate.accumulate(journal))
            );

            aggregateColumnsToShow.forEach(schemaAggregate ->
                    rowJson.generateJson(schemaAggregate.getNameOfAttribute(), schemaAggregate.getResult()));

            json.addObjectToArray(rowJson);

            return json.getJsonString();

        } else {
            groupRows(journals);

            for (List<Object> key : groupedRows.keySet()) {
                JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

                Iterable<SerializedJournal> rows = groupedRows.get(key);

                // Print out the groups to the json as columns
                // Like if it is group by names, print out the name first (if they are in normal columns to show)
                // if they are not then the user only wants to group journals by these columns but not show them
                // as columns in the results
                for (int i = 0; i < groupByColumns.size(); i ++) {
                    if (!normalColumnsToShow.contains(groupByColumns)) {
                        continue;
                    }
                    rowJson.generateJson(groupByColumns.get(i).getNameOfAttribute(), key.get(i));
                }

                // For each row in the group, pass the value to the aggregate function
                // For example if the aggregate function is a count, it simply counts the number
                // of SerializedJournal in the group and etc. Here we don't care what the aggregate function
                // is going to do - we just pass it to them
                for (SerializedJournal row : rows) {
                    // Pass it to the aggregate function (so that they can count or do something to it)
                    aggregateColumnsToShow.forEach(schemaAggregate -> schemaAggregate.accumulate(row));
                }

                // Then print out the aggregate for this group by asking the aggregate for the result
                // Like the number of names for a count aggregate
                aggregateColumnsToShow.forEach(schemaAggregate ->
                        rowJson.generateJson(schemaAggregate.getNameOfAttribute(), schemaAggregate.getResult()));

                json.addObjectToArray(rowJson);
            }

            return json.getJsonString();
        }
    }
}
