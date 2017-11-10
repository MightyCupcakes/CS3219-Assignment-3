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
	public boolean executeAndSaveResultIntoCsvFile(WebRequest query) {
		String queryType = query.getValue("premadeType");
        Optional<WebQueryProcessor> processor = registry.getHandler(queryType);

        if (processor.isPresent()) {
            return processor.get().processAndSaveIntoCSV(manager, query);
        }

		return false;
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


}
