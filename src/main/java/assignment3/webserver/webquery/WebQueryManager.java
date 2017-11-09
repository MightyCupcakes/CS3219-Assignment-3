package assignment3.webserver.webquery;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import assignment3.api.APIQueryBuilder;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaAggregate;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.schema.aggregate.SchemaCountUnique;
import assignment3.schema.aggregate.SchemaMax;
import assignment3.schema.aggregate.SchemaMin;
import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.WebServerRegistry;
import assignment3.webserver.webquery.WebQuery;
import assignment3.webserver.webrequest.WebRequest;

public class WebQueryManager implements WebQuery {

	private static final String DEFAULT_CONFERENCE = "A4";
	private static final WebServerRegistry<WebQueryProcessor> registry =
            new WebServerRegistry<>(WebQueryProcessor.class.getPackage().getName());

	private final WebServerManager manager;

	public WebQueryManager(WebServerManager manager) {
	    this.manager = manager;
    }

	@SuppressWarnings("rawtypes")
	public void generateTopNXofYGraph(Map<String, String> data)  {
//		APIQueryBuilder builder = QueryBuilder.createNewBuilder();
//		int n = Integer.parseInt(data.get("n"));
//		String xAttr = data.get("xAttr");
//		String yAttr = data.get("yAttr");
//		String yValue = data.get("yValue");
//
//		SchemaComparable selectAttribute = (SchemaComparable) this.getSchemaAttribute(xAttr);
//		SchemaBase selectCount;
//		if (selectAttribute instanceof CitationAttribute) {
//			selectCount = this.getSchemaAggregate(selectAttribute, "count");
//		} else {
//			selectCount = new SchemaCount(ConferenceData.ID);
//		}
//		builder = builder.select(selectAttribute.as("attribute"), selectCount.as("count"));
//
//		if (yAttr.equals("conference")) {
//
//			builder = builder.from(yValue);
//		} else {
//			SchemaComparable whereAttr = (SchemaComparable) this.getSchemaAttribute(yAttr);
//			SchemaPredicate predicate = this.getSchemaPredicate(whereAttr, yValue, null, "eqt");
//			builder = builder.from(DEFAULT_CONFERENCE).where(predicate);
//		}
//
//		Query query = builder.groupBy(selectAttribute)
//				.orderBy(selectCount, APIQueryBuilder.OrderByRule.DESC)
//				.limit(n)
//				.build();
//		System.out.println("savingFile");
//		query.executeAndSaveInCSV("5");
//		System.out.println("file saved");
	}

	@Override
	public boolean parseWebQuery(WebRequest query) {

	    APIQueryBuilder builder = manager.getAPI().getQueryBuilder();

	    // Get columns to be displayed
        SchemaBase column1 = getSchemaAttribute(query.getValue("column1Name"), query.getValue("column1ShowType"));
        SchemaBase column2 = getSchemaAttribute(query.getValue("column2Name"), query.getValue("column2ShowType"));

        builder.select(column1, column2);
        builder.from(DEFAULT_CONFERENCE);

        // If any of the columns is an aggregate, the other column will require a group by
        if ( column1 instanceof SchemaAggregate && column2 instanceof SchemaAggregate) {
            return false;

        } else if (column1 instanceof SchemaAggregate && column2 instanceof SchemaComparable) {
            builder.groupBy( (SchemaComparable) column2);

        } else if (column2 instanceof SchemaAggregate && column1 instanceof SchemaComparable) {
            builder.groupBy( (SchemaComparable) column1);

        } else {
            return false;
        }

        // Get the number of conditions
        int numConditions = Integer.parseInt(query.getValue("numOfConditions"));

        String conditionCombine = "";
        SchemaPredicate condition = null;

        for (int i = 1; i <= numConditions; i ++) {
            String columnName = query.getValue("conditionColumn" + i) ;
            String comparator = query.getValue("conditionComparator" + i);
            String value = query.getValue("conditionValue" + i);

            SchemaComparable column = (SchemaComparable) WebServerConstants.COLUMNS.get(columnName);

            if (!isNull(condition)) {
                condition = getSchemaPredicate(column, value, Collections.emptySet(), comparator);
            } else {
                if ("OR".equalsIgnoreCase(conditionCombine)) {
                    condition = condition.or(getSchemaPredicate(column, value, Collections.emptySet(), comparator));
                } else if ("AND".equalsIgnoreCase(conditionCombine)) {
                    condition = condition.and(getSchemaPredicate(column, value, Collections.emptySet(), comparator));
                }
            }

            conditionCombine = query.getValue("conditionCombine" + i);
        }

        if (!isNull(condition)) {
            builder.where(condition);
        }

		return false;
	}

	@Override
	public boolean executeAndSaveResultIntoCsvFile(String filename) {
		return false;
	}

	@SuppressWarnings("rawtypes")
	private SchemaBase getSchemaAttribute(String attribute, String attributeDisplayOption) {
		SchemaBase schema = WebServerConstants.COLUMNS.get(attribute);

		if (isNull(schema) || !(schema instanceof SchemaComparable)) {
		    return null;
        }

		Optional<DisplayOption> option = DisplayOption.getOptionWithStringValue(attributeDisplayOption);

		if (option.isPresent()) {
		    return option.get().converterFunction.apply( (SchemaComparable) schema);
        } else {
		    return null;
        }
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
	private SchemaPredicate getSchemaPredicate(SchemaComparable attribute, String valueToCompare,
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

//	@SuppressWarnings("rawtypes")
//	public String retrieveDataForDropDown(String attribute) {
//		APIQueryBuilder builder = QueryBuilder.createNewBuilder();
//		SchemaComparable selectAttr = (SchemaComparable) this.getSchemaAttribute(attribute);
//		if (attribute.equals("yearOfPublication")) {
//			builder = builder.select(this.getSchemaAggregate(selectAttr, "min"),
//					this.getSchemaAggregate(selectAttr, "max"))
//					.from(DEFAULT_CONFERENCE);
//		} else {
//			builder = builder.select(selectAttr)
//					.from(DEFAULT_CONFERENCE)
//					.groupBy((SchemaComparable) selectAttr)
//					.orderBy(selectAttr, APIQueryBuilder.OrderByRule.ASC);
//		}
//		Query query = builder.build();
//		String result = query.execute();
//		//query.executeAndSaveInCSV(attribute);
//		//System.out.println(result);
//		return result;
//	}
}
