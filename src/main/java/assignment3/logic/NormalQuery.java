package assignment3.logic;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import assignment3.api.APIQueryBuilder;
import assignment3.api.Query;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class NormalQuery implements Query {
    private List<SchemaComparable> columnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;
    private Logic logic;
    private int limitRows;
    private SchemaBase orderByColumn;
    private APIQueryBuilder.OrderByRule orderByRule;

    protected List<SerializedJournalCitation> journals;

    public NormalQuery(List<SchemaComparable> columnsToShow, SchemaPredicate predicate, List<String> tablesToRead) {
        this.columnsToShow = Collections.unmodifiableList(columnsToShow);
        this.predicate = predicate;
        this.tablesToRead = tablesToRead;
        this.limitRows = -1;
        this.orderByRule = APIQueryBuilder.OrderByRule.ASC;
    }

    private List<SerializedJournalCitation> filterOutRows(List<SerializedJournalCitation> data) {
        return data.stream()
                .filter(serializedJournalCitation -> predicate.test(serializedJournalCitation))
                .collect(Collectors.toList());
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

    @Override
    public String execute() {
        try {

            if (!isNull(logic)) {
                journals = new ArrayList<>(150);

                for (String table : tablesToRead) {
                    journals.addAll(logic.getDataFromTableWithNoCitations(table));
                }
            }

        } catch (Exception e) {
            Logger.getLogger(this.getClass().toString())
                    .warning("Exception thrown while trying to get data from LogicLayer: " + e.getMessage());
            return EMPTY_JSON;
        }

        journals = filterOutRows(journals);

        JsonGenerator json = new JsonGenerator();

        for (SerializedJournalCitation journal : journals) {
            JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

            columnsToShow.forEach(schema -> rowJson.generateJson(schema.getNameOfAttribute(), schema.getValue(journal)));
            json.addObjectToArray(rowJson);
        }

        return json.getJsonString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof NormalQuery
                && this.columnsToShow.equals(((NormalQuery) other).columnsToShow)
                && this.tablesToRead.equals(((NormalQuery) other).tablesToRead)
                && this.predicate.equals(((NormalQuery) other).predicate)
                && ( (this.orderByColumn == null && ((NormalQuery) other).orderByColumn == null)
                    || this.orderByColumn.equals(((NormalQuery) other).orderByColumn) )
                && this.orderByRule.equals( ((NormalQuery) other).orderByRule )
                && this.limitRows == ((NormalQuery) other).limitRows);
    }
}
