package assignment3.schema;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

import assignment3.datarepresentation.SerializedJournal;
import assignment3.datarepresentation.SerializedJournalCitation;

/**
 * Represents an attribute of an PersistentObject
 * @param <T> The data type of the attribute
 */
public abstract class SchemaBase<T> {

    protected String nameOfAttribute = "";
    protected boolean splitAttributeIntoRows;
    protected boolean hasBeenRenamed;
    protected Function<SerializedJournal, Collection<String>> splittingFunction;

    public final String originalNameOfAttribute;

    protected BiFunction<SerializedJournal, String, SerializedJournal> splitter;

    public SchemaBase(String nameOfAttribute) {
        this.nameOfAttribute = nameOfAttribute;
        this.originalNameOfAttribute = nameOfAttribute;
        this.splitAttributeIntoRows = false;
        this.hasBeenRenamed = false;
        this.splittingFunction = j -> Collections.emptyList();
        this.splitter = (j, s) ->  j;
    }

    /**
     * Gets the value of this attribute from the specified SerializedJournal
     */
    public T getValue(SerializedJournalCitation journalCitation) {
        assert !originalNameOfAttribute.equals("");

        try {
            Field field = SerializedJournal.class.getDeclaredField(originalNameOfAttribute);
            return (T) field.get(journalCitation.journal);
        } catch (NoSuchFieldException | IllegalAccessException e) {

            Logger.getLogger(this.getClass().toString())
                    .warning("Attribute [" + originalNameOfAttribute + "] not found in serializedJournal!");
            return null;
        }
    }

    public SchemaBase as(String variableName) {
        assert !"".equals(variableName);

        SchemaBase schema = this.copy();
        schema.nameOfAttribute = variableName;
        schema.hasBeenRenamed = true;

        return schema;
    }

    public String getNameOfAttribute() {
        return nameOfAttribute;
    }

    public boolean isJoinTable() {
        return false;
    }

    public boolean requireSplitRow() {
        return splitAttributeIntoRows;
    }

    public Function<SerializedJournal, Collection<String>> getSplittingFunction() {
        return splittingFunction;
    }

    public BiFunction<SerializedJournal, String, SerializedJournal> getDuplicateGenerator() {
        return splitter;
    }

    public abstract SchemaBase copy();

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SchemaBase
                && originalNameOfAttribute.equals( ((SchemaBase) other).originalNameOfAttribute));
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalNameOfAttribute);
    }
}
