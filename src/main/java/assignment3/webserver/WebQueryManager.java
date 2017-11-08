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
import assignment3.webserver.webrequest.WebRequest;

public class WebQueryManager implements WebQuery {

	private static final String DEFAULT_CONFERENCE = "A4";

	private final WebServerManager manager;

	public WebQueryManager(WebServerManager manager) {
	    this.manager = manager;
    }

	@SuppressWarnings("rawtypes")
	public void generateTopNXofYGraph(Map<String, String> data)  {
		APIQueryBuilder builder = QueryBuilder.createNewBuilder();
		int n = Integer.parseInt(data.get("n"));
		String xAttr = data.get("xAttr");
		String yAttr = data.get("yAttr");
		String yValue = data.get("yValue");
		
		SchemaComparable selectAttribute = (SchemaComparable) this.getSchemaAttribute(xAttr);
		SchemaBase selectCount;
		if (selectAttribute instanceof CitationAttribute) {
			selectCount = this.getSchemaAggregate(selectAttribute, "count");
		} else {
			selectCount = new SchemaCount(ConferenceData.ID);
		}
		builder = builder.select(selectAttribute.as("attribute"), selectCount.as("count"));
		
		if (yAttr.equals("conference")) {

			builder = builder.from(yValue);
		} else {
			SchemaComparable whereAttr = (SchemaComparable) this.getSchemaAttribute(yAttr);
			SchemaPredicate predicate = this.getSchemaPredicate(whereAttr, yValue, null, "eqt");
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
	public boolean parseWebQuery(WebRequest query) {
		return false;
	}

	@Override
	public boolean executeAndSaveResultIntoCsvFile(String filename) {
		return false;
	}

	@SuppressWarnings("rawtypes")
	private SchemaBase getSchemaAttribute(String attribute) {
		SchemaComparable schemaAttr;
		if (attribute.equals("author")) {
			schemaAttr = ConferenceData.AUTHOR;
		} else if (attribute.equals("yearOfPublication")) {
			schemaAttr = new SchemaInt(attribute);
		} else if (isCitationAttribute(attribute)){
			schemaAttr = attribute.equals("year") 
					? new CitationAttribute<Integer> (attribute) :
				new CitationAttribute<String> (attribute);
		} else {
			schemaAttr = new SchemaString(attribute);
		}
		
		return schemaAttr;
	}
	
	private SchemaBase getSchemaAggregate(SchemaComparable schemaAttr, String type) {
		if (type.equals("count")) {
			return new SchemaCount(schemaAttr);
		} else if (type.equals("max")) {
			return new SchemaMax(schemaAttr);	
		} else if (type.equals("min")) {
			return new SchemaMin(schemaAttr);
		}
		return new SchemaCountUnique(schemaAttr);

	}

	private boolean isCitationAttribute(String attribute) {
		return attribute.equals("citationtitle") || attribute.equals("year")
				|| attribute.equals("booktitle") || attribute.equals("authors")
				|| attribute.equals("citationVenue") || attribute.equals("journalId")
				|| attribute.equals("numOfAuthors");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private SchemaPredicate getSchemaPredicate(SchemaComparable  attribute, String valueToCompare,
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

	@SuppressWarnings("rawtypes")
	public String retrieveDataForDropDown(String attribute) {
		APIQueryBuilder builder = QueryBuilder.createNewBuilder();
		SchemaComparable selectAttr = (SchemaComparable) this.getSchemaAttribute(attribute);
		if (attribute.equals("yearOfPublication")) {
			builder = builder.select(this.getSchemaAggregate(selectAttr, "min"),
					this.getSchemaAggregate(selectAttr, "max"))
					.from(DEFAULT_CONFERENCE);
		} else {
			builder = builder.select(selectAttr)
					.from(DEFAULT_CONFERENCE)
					.groupBy((SchemaComparable) selectAttr)
					.orderBy(selectAttr, APIQueryBuilder.OrderByRule.ASC);
		}
		Query query = builder.build();
		String result = query.execute();
		//query.executeAndSaveInCSV(attribute);
		//System.out.println(result);
		return result;
	}

}
