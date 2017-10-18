package assignment3.logic;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import assignment3.api.APIQueryBuilder;
import assignment3.api.Query;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaPredicate;

public abstract class QueryBase implements Query {

    protected Logic logic;
    protected boolean isJoinTable;
    protected int limitRows;
    protected SchemaBase orderByColumn;
    protected APIQueryBuilder.OrderByRule orderByRule;

    public QueryBase() {
        this.isJoinTable = false;
        this.limitRows = -1;
        this.orderByRule = APIQueryBuilder.OrderByRule.ASC;
    }

    void setDataSource(Logic logic) {
        this.logic = logic;
    }

    void setLimitRows(int limit) {
        this.limitRows = limit;
    }

    void setOrderByColumn(SchemaBase column, APIQueryBuilder.OrderByRule rule) {
        this.orderByColumn = column;
        this.orderByRule = rule;
    }

    public void setQueryToRetrieveCitations() {
        this.isJoinTable = true;
    }

    public boolean isQueryRetrivingCitations() {
        return this.isJoinTable;
    }

    protected List<SerializedJournalCitation> filterOutRows(List<SerializedJournalCitation> data, SchemaPredicate predicate) {
        return data.stream()
                .filter(serializedJournalCitation -> predicate.test(serializedJournalCitation))
                .collect(Collectors.toList());
    }

    protected JsonGenerator getJsonGenerator() {

        JsonGenerator json;

        if (isNull(orderByColumn)) {
            if (limitRows == -1) {
                json = new JsonGenerator();
            } else {
                json = new JsonGenerator(limitRows);
            }
        } else {
            if (limitRows == -1) {
                json = new JsonSorter(orderByColumn, orderByRule);
            } else {
                json = new JsonSorter(orderByColumn, orderByRule, limitRows);
            }
        }

        return json;
    }

    protected List<SerializedJournalCitation> getDataFromLogicLayer(
            List<SchemaBase> splitters, List<String> tablesToRead) throws Exception {

        requireNonNull(logic);

        List<SerializedJournalCitation> journals = new ArrayList<>(150);

        if (isQueryRetrivingCitations()) {

            for (String table : tablesToRead) {

                if (splitters.isEmpty()) {
                    journals.addAll(logic.getDataFromTableWithCitations(table));
                } else {
                    journals.addAll(logic.getDataFromTableWithCitations(table, splitters));
                }

            }
        } else if (!isQueryRetrivingCitations()) {

            for (String table : tablesToRead) {

                if (splitters.isEmpty()) {
                    journals.addAll(logic.getDataFromTableWithNoCitations(table));
                } else {
                    journals.addAll(logic.getDataFromTableWithNoCitations(table, splitters));
                }
            }
        }

        return journals;
    }

    @Override
    public void executeAndSaveInCSV() {
        requireNonNull(logic);

        String json = execute();

        try {
            logic.saveResultIntoCsv(json);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().toString()).warning(
                    "An error occured when saving the data into CSV!"
            );

            e.printStackTrace();
        }
    }

    public abstract List<SchemaBase> getColumnsSelected();
}
