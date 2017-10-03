package assignment3.schema.aggregate;

import java.util.HashSet;
import java.util.Set;

import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaComparable;

public class SchemaCountUnique extends SchemaAggregate{

    private int count;
    private Set<Object> countedValues;

    public SchemaCountUnique(SchemaComparable column) {
        super(column);

        count = 0;
        countedValues = new HashSet<>();
    }

    @Override
    public String getNameOfAttribute() {
        return "COUNT UNIQUE(" + this.nameOfAttribute + ")";
    }

    @Override
    public void accumulate(SerializedJournalCitation row) {
        Object value = column.getValue(row);

        if (!countedValues.contains(value)) {
            countedValues.add(value);
            count ++;
        }
    }
    @Override
    public int getResult() {
        int result = count;
        count = 0;

        return result;
    }
}
