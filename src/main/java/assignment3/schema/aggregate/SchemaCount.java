package assignment3.schema.aggregate;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.schema.SchemaComparable;

public class SchemaCount extends SchemaAggregate {

    private int count;

    public SchemaCount(SchemaComparable column) {
        super(column);
        count = 0;
    }

    @Override
    public String getNameOfAttribute() {
        return "COUNT(" + this.nameOfAttribute + ")";
    }

    @Override
    public void accumulate(SerializedJournal row) {
        count ++;
    }
    @Override
    public int getResult() {
        int result = count;
        count = 0;

        return result;
    }
}
