package assignment3.webserver;

import java.util.Set;



import assignment3.api.ConferenceData;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaInt;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.SchemaString;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
import assignment3.schema.citations.CitationAttribute;
import assignment3.webserver.webquery.WebQuery;
import assignment3.webserver.webrequest.WebRequest;

public class WebQueryManager implements WebQuery{

	private static final String DEFAULT_CONFERENCE = "A4";


	@SuppressWarnings("rawtypes")
	private SchemaBase getSchemaAttr(String attribute) {
		SchemaComparable schemaAttr;
		if (attribute.equals("author")) {
			schemaAttr = ConferenceData.AUTHOR;
		} else if (attribute.equals("yearOfPublication")) {
			schemaAttr = new SchemaInt(attribute);
		} else if (isCitationAttr(attribute)){
			schemaAttr = attribute.equals("year") 
					? new CitationAttribute<Integer> (attribute) :
				new CitationAttribute<String> (attribute);
		} else {
			schemaAttr = new SchemaString(attribute);
		}
		
		return schemaAttr;
	}
	
	private SchemaBase getSchemaAggre(SchemaComparable schemaAttr, String type) {
		if (type.equals("count")) {
			return new SchemaCount(schemaAttr);
		} else if (type.equals("max")) {
			return new SchemaMax(schemaAttr);	
		} else if (type.equals("min")) {
			return new SchemaMin(schemaAttr);
		}
		return new SchemaCountUnique(schemaAttr);

	}

	private boolean isCitationAttr(String attribute) {
		return attribute.equals("citationtitle") || attribute.equals("year")
				|| attribute.equals("booktitle") || attribute.equals("authors")
				|| attribute.equals("citationVenue") || attribute.equals("journalId")
				|| attribute.equals("numOfAuthors");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private SchemaPredicate getSchemaPredi(SchemaComparable  attribute, String valueToCompare, 
			Set valueSet, String type){
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
	
	private SchemaPredicate getSchemaPrediIn(SchemaComparable attribute, Set valueSet) {
		return attribute.in(valueSet);
	}

	@Override
	public boolean executeAndSaveResultIntoCsvFile(WebRequest query) {
		// TODO Auto-generated method stub
		return false;
	}
	



}
