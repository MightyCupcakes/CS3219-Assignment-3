package assignment3.schema;

import java.lang.reflect.Field;

import assignment3.datarepresentation.SerializedJournal;

/**
 * Represents an attribute of an PersistentObject
 * @param <T> The data type of the attribute
 */
public abstract class SchemaBase<T> {

    private String nameOfAttribute = "";

    public SchemaBase(String nameOfAttribute) {
        this.nameOfAttribute = nameOfAttribute;
    }

    /**
     * Gets the value of this attribute from the specified SerializedJournal
     */
    public T getValue(SerializedJournal journal) {
        assert !nameOfAttribute.equals("");

        try {
            Field field = SerializedJournal.class.getDeclaredField(nameOfAttribute);
            return (T) field.get(journal);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
    public String getNameOfAttribute() {
        return nameOfAttribute;
    }
}
