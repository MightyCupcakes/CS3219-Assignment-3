package assignment3.logic;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import assignment3.api.Query;
import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class NormalQuery implements Query {
    private List<SchemaComparable> columnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;
    private Logic logic;

    protected List<SerializedJournalCitation> journals;

    public NormalQuery(List<SchemaComparable> columnsToShow, SchemaPredicate predicate, List<String> tablesToRead) {
        this.columnsToShow = Collections.unmodifiableList(columnsToShow);
        this.predicate = predicate;
        this.tablesToRead = tablesToRead;
    }

    private List<SerializedJournalCitation> filterOutRows(List<SerializedJournalCitation> data) {
        return data.stream()
                .filter(serializedJournalCitation -> predicate.test(serializedJournalCitation))
                .collect(Collectors.toList());
    }

    public void setDataSource(Logic logic) {
        this.logic = logic;
    }

    @Override
    public String execute() throws Exception {

        if(!isNull(logic)) {
            journals = logic.getDataFromTableWithNoCitations(tablesToRead.get(0));
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
                && this.predicate.equals(((NormalQuery) other).predicate));
    }
}
