package assignment3.schema.aggregate;

import static java.util.Objects.isNull;

import java.util.logging.Logger;

import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;

public class SchemaMax extends SchemaAggregate {

    private int max;

    public SchemaMax(SchemaComparable column) {
        super(column);
        max = Integer.MIN_VALUE;
    }

    @Override
    public String getNameOfAttribute() {
        return (!hasBeenRenamed) ? "MAX(" + this.nameOfAttribute + ")" : nameOfAttribute;
    }

    @Override
    public void accumulate(SerializedJournalCitation row) {
        Object value = column.getValue(row);

        if (isNull(value)) {
            return;
        }

        int integerValue = 0;

        try {
            integerValue = Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            Logger.getLogger(this.getClass().toString())
                    .warning(column.getNameOfAttribute() + " : value cannot parsed into integer!");
        }

        if (integerValue != Integer.MIN_VALUE) {
            max = Integer.max(integerValue, max);
        }
    }

    @Override
    public int getResult() {
        int result = max;
        max = Integer.MIN_VALUE;

        return result;
    }

    @Override
    public SchemaBase copy() {
        return new SchemaMax(column);
    }
}
