package assignment3.api;

import assignment3.api.exceptions.QueryException;
import assignment3.logic.Logic;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public interface APIQueryBuilder {

    APIQueryBuilder select(SchemaBase... columns);
    APIQueryBuilder from(String... table);
    APIQueryBuilder where(SchemaPredicate predicate);
    APIQueryBuilder groupBy(SchemaComparable... columns);
    Query build() throws QueryException;
}
