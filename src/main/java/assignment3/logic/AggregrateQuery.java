package assignment3.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import assignment3.api.Query;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.schema.SchemaAggregate;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class AggregrateQuery implements Query {
    private List<SchemaAggregate> columnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;
    private List<SchemaComparable> groupByColumns;
    private Logic logic;

    protected List<SerializedJournal> journals;

    private Multimap<List<Object>, SerializedJournal> groupedRows;

    public AggregrateQuery(List<SchemaAggregate> columnsToShow,
                           SchemaPredicate predicate,
                           List<String> tablesToRead,
                           List<SchemaComparable> groupByColumns) {

        this.columnsToShow = Collections.unmodifiableList(columnsToShow);
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
        if (groupByColumns.isEmpty()) {
            // Do something...
            return "";
        } else {
            JsonGenerator json = new JsonGenerator();
            for (List<Object> key : groupedRows.keySet()) {
                JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

                Iterable<SerializedJournal> rows = groupedRows.get(key);

                // Print out the groups to the json as columns
                // Like if it is group by names, print out the name first
                for (int i = 0; i < groupByColumns.size(); i ++) {
                    rowJson.generateJson(groupByColumns.get(i).getNameOfAttribute(), key.get(i));
                }

                // For each row in the group, pass the value to the aggregate function
                // For example if the aggregate function is a count, it simply counts the number
                // of SerializedJournal in the group and etc. Here we don't care what the aggregate function
                // is going to do - we just pass it to them
                for (SerializedJournal row : rows) {
                    // Pass it to the aggregate function (so that they can count or do something to it)
                    columnsToShow.forEach( schemaAggregate -> schemaAggregate.accumulate(row));
                }

                // Then print out the aggregate for this group by asking the aggregate for the result
                // Like the number of names for a count aggregate
                columnsToShow.forEach( schemaAggregate ->
                        rowJson.generateJson(schemaAggregate.getNameOfAttribute(), schemaAggregate.getResult()));

                json.addObjectToArray(rowJson);
            }

            return json.getJsonString();
        }
    }
}
