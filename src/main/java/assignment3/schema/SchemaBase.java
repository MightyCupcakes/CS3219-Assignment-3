package assignment3.schema;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;

/**
 * Represents an attribute of an PersistentObject
 * @param <T> The data type of the attribute
 */
public abstract class SchemaBase<T> {

    protected String nameOfAttribute = "";

    public SchemaBase(String nameOfAttribute) {
        this.nameOfAttribute = nameOfAttribute;
    }

    /**
     * Gets the value of this attribute from the specified SerializedJournal
     */
    public T getValue(SerializedJournalCitation journalCitation) {
        assert !nameOfAttribute.equals("");

        try {
            Field field = SerializedJournal.class.getDeclaredField(nameOfAttribute);
            return (T) field.get(journalCitation.journal);
        } catch (NoSuchFieldException | IllegalAccessException e) {

            Logger.getLogger(this.getClass().toString())
                    .warning("Attribute [" + nameOfAttribute + "] not found in serializedJournal!");
            return null;
        }
    }
    public String getNameOfAttribute() {
        return nameOfAttribute;
    }

    public boolean isJoinTable() {
        return false;
    }
}
