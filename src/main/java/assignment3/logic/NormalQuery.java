package assignment3.logic;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class NormalQuery extends QueryBase {
    private List<SchemaComparable> columnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;

    protected List<SerializedJournalCitation> journals;

    public NormalQuery(List<SchemaComparable> columnsToShow, SchemaPredicate predicate, List<String> tablesToRead) {
        super();

        this.columnsToShow = Collections.unmodifiableList(columnsToShow);
        this.predicate = predicate;
        this.tablesToRead = tablesToRead;
    }

    @Override
    public String execute() {

        List<SchemaBase> splitters = new ArrayList<>(1);

        if (columnsToShow.stream().anyMatch(schema ->schema.requireSplitRow())) {

            columnsToShow.stream()
                    .filter(schema -> schema.requireSplitRow())
                    .forEach(splitters::add);
        }

        if (!isNull(logic)) {
            try {
                journals = getDataFromLogicLayer(splitters, tablesToRead);
            } catch (Exception e) {
                Logger.getLogger(this.getClass().toString())
                        .warning("Exception thrown while trying to get data from LogicLayer: " + e.getMessage());
                return EMPTY_JSON;
            }
        }

        journals = filterOutRows(journals, predicate);

        JsonGenerator json = getJsonGenerator();

        for (SerializedJournalCitation journal : journals) {
            JsonGenerator.JsonGeneratorBuilder rowJson = new JsonGenerator.JsonGeneratorBuilder();

            columnsToShow.forEach(schema -> rowJson.generateJson(schema, schema.getValue(journal)));
            json.addObjectToArray(rowJson);
        }

        return json.getJsonString();
    }

    @Override
    public List<SchemaBase> getColumnsSelected() {
        return Collections.unmodifiableList(columnsToShow);
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
