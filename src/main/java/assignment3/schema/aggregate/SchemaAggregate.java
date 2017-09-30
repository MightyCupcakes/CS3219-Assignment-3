package assignment3.schema.aggregate;

import java.util.function.BiConsumer;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.schema.SchemaBase;

public abstract class SchemaAggregate extends SchemaBase<Integer> {

    private BiConsumer<Integer, Integer> accumulationFunction;
    private int result;

    private SchemaAggregate(String name, BiConsumer<Integer, Integer> accumulationFunction) {
        super(name);
        this.accumulationFunction = accumulationFunction;
        result = 0;
    }

    public void accumulate(SerializedJournal row){

    }

    public int getResult() {
        return result;
    }
}
