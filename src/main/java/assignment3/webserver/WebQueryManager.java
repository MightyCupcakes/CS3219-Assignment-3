package assignment3.webserver;

import java.util.Set;

import assignment3.api.APIQueryBuilder;
import assignment3.api.ConferenceData;
import assignment3.logic.QueryBuilder;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaInt;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.SchemaString;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;

public class WebQueryManager implements WebQuery{


	@SuppressWarnings("rawtypes")
	@Override
	public SchemaBase getSchemaAttr(String attribute, String type) {
		SchemaComparable schemaAttr;
		if (attribute.equals("author")) {
			schemaAttr = ConferenceData.AUTHOR;
		} else if (attribute.equals("yearOfPublication") ||
				attribute.equals("year")) {
			schemaAttr = new SchemaInt(attribute);
		} else {
			schemaAttr = new SchemaString(attribute);
		}
		
		if (type.equals("count")) {
			return new SchemaCount(schemaAttr);
		} else if(type.equals("countUnique")) {
			return new SchemaCountUnique(schemaAttr);
		} 
		return schemaAttr;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SchemaPredicate getSchemaPredi(SchemaComparable  attribute, String valueToCompare, 
			Set valueSet, String type) {
		// TODO Auto-generated method stub
		if (type.equals("gt")) {
			return attribute.greaterThan(Integer.parseInt(valueToCompare));
		} else if (type.equals("gte")) {
			return attribute.greaterThanOrEqualsTo(Integer.parseInt(valueToCompare));
		} else if (type.equals("lte")) {
			return attribute.lessThanOrEqualsTo(Integer.parseInt(valueToCompare));
		} else if (type.equals("lt")) {
			return attribute.lessThan(Integer.parseInt(valueToCompare));
		} else if (type.equals("like")) {
			return attribute.like(valueToCompare);
		} else if (type.equals("in") && valueSet != null) {
			return attribute.in(valueSet);
		}
		return attribute.equalsTo(valueToCompare);
	}

}
