package assignment3.api;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public class NormalQuery {
    private List<SchemaComparable> columnsToShow;
    private SchemaPredicate predicate;

    protected List<SerializedJournal> journals;

    public NormalQuery(List<SchemaComparable> columnsToShow, SchemaPredicate predicate) {
        this.columnsToShow = Collections.unmodifiableList(columnsToShow);
        this.predicate = predicate;
    }

    private List<SerializedJournal> filterOutRows(List<SerializedJournal> data) {
        return data.stream()
                .filter(serializedJournal -> predicate.test(serializedJournal))
                .collect(Collectors.toList());
    }

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
}
