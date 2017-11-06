package assignment3.webserver;

import java.util.Set;

import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;

public interface WebQuery {

	SchemaBase getSchemaAttr(String attribute, String type);
	SchemaPredicate getSchemaPredi(SchemaComparable  attribute, String valueToCompare, 
			Set valueSet, String type);

}
