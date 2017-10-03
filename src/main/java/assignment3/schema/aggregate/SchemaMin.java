package assignment3.schema.aggregate;

import java.util.logging.Logger;

import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaComparable;

public class SchemaMin extends SchemaAggregate {

    private int min;

    public SchemaMin(SchemaComparable column) {
        super(column);
        min = Integer.MAX_VALUE;
    }

    @Override
    public String getNameOfAttribute() {
        return "MIN(" + this.nameOfAttribute + ")";
    }

    @Override
    public void accumulate(SerializedJournalCitation row) {
        Object value = column.getValue(row);

        int integerValue = 0;

        try {
            integerValue = Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            Logger.getLogger(this.getClass().toString())
                    .warning(column.getNameOfAttribute() + " : value cannot parsed into integer!");
        }

        min = Integer.min(min, integerValue);
    }

    @Override
    public int getResult() {
        int result = min;
        min = Integer.MAX_VALUE;

        return result;
    }
}
