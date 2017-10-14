package assignment3.api;

import javax.annotation.Nonnull;

import assignment3.api.exceptions.QueryException;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public interface APIQueryBuilder {

    APIQueryBuilder select(@Nonnull SchemaBase... columns);
    APIQueryBuilder from(String... table);
    APIQueryBuilder where(@Nonnull SchemaPredicate predicate);
    APIQueryBuilder groupBy(@Nonnull SchemaComparable... columns);
    APIQueryBuilder orderBy(@Nonnull SchemaBase column, @Nonnull OrderByRule rule);
    APIQueryBuilder limit(int numberOfRows);
    Query build() throws QueryException;

    enum OrderByRule {
        ASC, DESC
    }
}
