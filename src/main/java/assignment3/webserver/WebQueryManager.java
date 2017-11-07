package assignment3.webserver;

import java.util.Map;
import java.util.Set;



import assignment3.api.APIQueryBuilder;
import assignment3.api.ConferenceData;
import assignment3.api.Query;
import assignment3.logic.QueryBuilder;
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

public class WebQueryManager implements WebQuery{


	private static final String DEFAULT_CONFERENCE = "A4";

	@SuppressWarnings("rawtypes")
	@Override
	public void generateTopNXofYGraph(Map<String, String> data)  {
		APIQueryBuilder builder = QueryBuilder.createNewBuilder();
		int n = Integer.parseInt(data.get("n"));
		String xAttr = data.get("xAttr");
		String yAttr = data.get("yAttr");
		String yValue = data.get("yValue");
		
		SchemaComparable selectAttribute = (SchemaComparable) this.getSchemaAttr(xAttr);
		SchemaBase selectCount;
		if (selectAttribute instanceof CitationAttribute) {
			selectCount = this.getSchemaAggre(selectAttribute, "count");
		} else {
			selectCount = new SchemaCount(ConferenceData.ID);
		}
		builder = builder.select(selectAttribute.as("attribute"), selectCount.as("count"));
		
		if (yAttr.equals("conference")) {

			builder = builder.from(yValue);
		} else {
			SchemaComparable whereAttr = (SchemaComparable) this.getSchemaAttr(yAttr);
			SchemaPredicate predicate = this.getSchemaPredi(whereAttr, yValue, null, "eqt");
			builder = builder.from(DEFAULT_CONFERENCE).where(predicate);
		}

		Query query = builder.groupBy(selectAttribute)
				.orderBy(selectCount, APIQueryBuilder.OrderByRule.DESC)
				.limit(n)
				.build();
		System.out.println("savingFile");
		query.executeAndSaveInCSV("5");
		System.out.println("file saved");
	}

	@Override
	public void generateTrendGraph(Map<String, String> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateContemporaryGraph(Map<String, String> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateNewGraph(Map<String, String> data) {
		// TODO Auto-generated method stub
		
	}

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
			return new SchemaMin(schemaAttr);	
		} else if (type.equals("min")) {
			return new SchemaMax(schemaAttr);
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
	

	@SuppressWarnings("rawtypes")
	@Override
	public String retrieveDataForDropDown(String attribute) {
		APIQueryBuilder builder = QueryBuilder.createNewBuilder();
		SchemaComparable selectAttr = (SchemaComparable) this.getSchemaAttr(attribute);
		if (attribute.equals("")) {
			builder = builder.select(this.getSchemaAggre(selectAttr, "min"),
					this.getSchemaAggre(selectAttr, "max"));
		} else {
			builder = builder.select(selectAttr);
		}
		Query query = builder.from(DEFAULT_CONFERENCE)
				.groupBy((SchemaComparable) selectAttr)
				.build();
		return query.execute();
	}

}
