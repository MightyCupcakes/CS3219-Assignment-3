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
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaAggregate;

public class JoinTableQuery implements Query {

    private List<SchemaAggregate> aggregateColumnsToShow;
    private List<SchemaComparable> normalColumnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;
    private List<SchemaComparable> groupByColumns;

    protected List<SerializedJournalCitation> journals;

    private Multimap<List<Object>, SerializedJournalCitation> groupedRows;

    public JoinTableQuery (List<SchemaAggregate> aggregatecolumnsToShow,
                           List<SchemaComparable> normalColumnsToShow,
                           SchemaPredicate predicate,
                           List<String> tablesToRead,
                           List<SchemaComparable> groupByColumns) {

        this.aggregateColumnsToShow = Collections.unmodifiableList(aggregatecolumnsToShow);
        this.normalColumnsToShow = Collections.unmodifiableList(normalColumnsToShow);
        this.predicate = predicate;
        this.tablesToRead = tablesToRead;
        this.groupByColumns = Collections.unmodifiableList(groupByColumns);

        this.groupedRows = HashMultimap.create();
    }

    private List<SerializedJournalCitation> filterOutRows(List<SerializedJournalCitation> data) {
        return data.stream()
                .filter(serializedJournalCitation -> predicate.test(serializedJournalCitation))
                .collect(Collectors.toList());
    }

    @Override
    public String execute() {
        // TODO: Get data from logic instead of an empty list like this

        AggregrateQuery query = new AggregrateQuery(aggregateColumnsToShow,
                normalColumnsToShow,
                predicate,
                tablesToRead,
                groupByColumns);

        journals = filterOutRows(journals);
        return query.executeAndGetResult(journals);
    }
}
