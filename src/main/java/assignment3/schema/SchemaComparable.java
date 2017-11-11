package assignment3.schema;

import static java.util.Objects.isNull;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.JsonValue;

import assignment3.api.Query;

public abstract class SchemaComparable<T extends Comparable> extends SchemaBase<T> {

    public SchemaComparable(String name) {
        super(name);
    }

    public SchemaPredicate isNotNull() {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null);
        });
    }

    public SchemaPredicate like(String value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            if (isNull(v)) return false;

            return v.toString().contains(value);
        });
    }

    public SchemaPredicate in(Query subQuery) {
        JsonReader jsonReader = Json.createReader(new StringReader(subQuery.execute()));
        JsonArray array = jsonReader.readArray();
        jsonReader.close();

        Set<String> values = new HashSet<>();

        for (int i = 0; i < array.size(); i ++) {

            Set<Map.Entry<String, JsonValue>>keySet = array.getJsonObject(i).entrySet();
            keySet.stream().forEach(entry -> values.add(entry.getValue().toString().replaceAll("\"", "")));
        }

        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            if (isNull(v)) return false;

            return values.contains(v.toString());
        });
    }

    public SchemaPredicate in(Set<T> values) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            if (isNull(v)) return false;

            return values.contains(v);
        });
    }

    public SchemaPredicate greaterThan(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) > 0;
        });
    }

    public SchemaPredicate greaterThanOrEqualsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) >= 0;
        });
    }

    public SchemaPredicate equalsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.equals(value);
        });
    }

    public SchemaPredicate notEqualsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && !v.equals(value);
        });
    }

    public SchemaPredicate equalsToIgnoreCase(String value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            if (isNull(v)) return false;

            return v.toString().equalsIgnoreCase(value);
        });
    }

    public SchemaPredicate lessThanOrEqualsTo(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) <= 0;
        });
    }

    public SchemaPredicate lessThan(T value) {
        return new SchemaPredicate(this, o -> {
            T v = this.getValue(o);
            return (v != null) && v.compareTo(value) < 0;
        });
    }
}
