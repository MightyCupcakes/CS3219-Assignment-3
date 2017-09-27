package assignment3.api;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.model.Model;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class NormalQuery implements Query {
    private List<SchemaComparable> columnsToShow;
    private SchemaPredicate predicate;
    private List<String> tablesToRead;
    private Model model;

    protected List<SerializedJournal> journals;

    public NormalQuery(List<SchemaComparable> columnsToShow, SchemaPredicate predicate, List<String> tablesToRead) {
        this.columnsToShow = Collections.unmodifiableList(columnsToShow);
        this.predicate = predicate;
        this.tablesToRead = tablesToRead;
    }

    private List<SerializedJournal> filterOutRows(List<SerializedJournal> data) {
        return data.stream()
                .filter(serializedJournal -> predicate.test(serializedJournal))
                .collect(Collectors.toList());
    }

    @Override
    public void setDataSource(Model model) {
        this.model = model;
    }

    @Override
    public String execute() {

        journals = filterOutRows(journals);

        JsonGenerator json = new JsonGenerator();

        for (SerializedJournal journal : journals) {
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
