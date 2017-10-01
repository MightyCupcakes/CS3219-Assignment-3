package assignment3.schema.aggregate;

import java.util.function.BiConsumer;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;

public abstract class SchemaAggregate extends SchemaBase<Integer> {

    public SchemaAggregate(SchemaComparable column) {
        super(column.getNameOfAttribute());
    }

    public abstract void accumulate(SerializedJournal row);
    public abstract int getResult();
}
