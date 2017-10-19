package assignment3.schema.citations;

import static java.util.Objects.isNull;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import assignment3.datarepresentation.SerializedCitation;
import assignment3.datarepresentation.SerializedJournalCitation;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;

public class CitationAttribute<T extends Comparable> extends SchemaComparable<T> {

    public CitationAttribute(String name) {
        super(name);
    }

    @Override
    public boolean isJoinTable() {
        return true;
    }

    @Override
    public T getValue(SerializedJournalCitation serializedJournalCitation) {
        assert !originalNameOfAttribute.equals("");

        if (isNull(serializedJournalCitation.citation)) {
            return null;
        }

        try {
            Field field = SerializedCitation.class.getDeclaredField(originalNameOfAttribute);
            return (T) field.get(serializedJournalCitation.citation);
        } catch (NoSuchFieldException | IllegalAccessException e) {

            Logger.getLogger(this.getClass().toString())
                    .warning("Attribute [" + originalNameOfAttribute + "] not found in serializedCitation!");
            return null;
        }
    }

    @Override
    public SchemaBase copy() {
        return new CitationAttribute<T>(originalNameOfAttribute);
    }
}
