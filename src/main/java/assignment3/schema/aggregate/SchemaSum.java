package assignment3.schema.aggregate;

import static java.util.Objects.isNull;

import java.util.logging.Logger;

import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;

public class SchemaSum extends SchemaAggregate {

    private int sum;

    public SchemaSum(SchemaComparable column) {
        super(column);
        sum = 0;
    }

    @Override
    public String getNameOfAttribute() {
        return (!hasBeenRenamed) ? "SUM(" + this.nameOfAttribute + ")" : nameOfAttribute;
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
            sum += integerValue;
        }
    }

    @Override
    public int getResult() {
        int result = sum;
        sum = 0;

        return result;
    }

    @Override
    public SchemaBase copy() {
        return new SchemaSum(column);
    }
}
