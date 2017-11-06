package assignment3.webserver.handler;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.google.common.collect.ImmutableMap;
import com.sun.net.httpserver.HttpExchange;

import assignment3.api.APIQueryBuilder;
import assignment3.api.ConferenceData;
import assignment3.api.Query;
import assignment3.logic.QueryBuilder;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.webserver.WebQuery;
import assignment3.webserver.WebQueryManager;
import assignment3.webserver.exceptions.WebServerException;

/**
 * Processes a GET request from the user and response with the appropriate HTML file to
 * display the visualisation
 */
public class GetRequestHandler extends FileRequestHandler {
	WebQuery webQuery = new WebQueryManager();
    public GetRequestHandler(String root) {
        super(root);
    }

    @Override
    public String handleRequest(HttpExchange httpExchange) throws WebServerException {
        Map<String, String> keyValuePairs = parseQueryString(httpExchange.getRequestURI().getQuery());

        if (keyValuePairs.containsKey("getVisualisation")) {
            // Create JSON response
            JsonObjectBuilder builder = Json.createObjectBuilder();
            // TODO: Not hardcode this
            String html = "q1.html";
            if (keyValuePairs.containsKey("vizType")) {
            	html = generateNewCsvData(keyValuePairs);
            }
            builder.add("src", html);

            return builder.build().toString();
        } else {
            return super.handleRequest(httpExchange);
        }
    }

    private String generateNewCsvData(Map<String, String> data) {
		int type = Integer.parseInt(data.get("vizType"));
		if (type == 1) {
			
		} else if (type == 2) {
			
		} else if (type == 3) {
			generateTopNXofYData(data);
			return "q2.html";
		} 
		return "q1.html";
	}

	private void generateTopNXofYData(Map<String, String> data) {
		APIQueryBuilder builder = QueryBuilder.createNewBuilder();
		int n = Integer.parseInt(data.get("n"));
		String xAttr = data.get("xAttr");
		String yAttr = data.get("yAttr");
		String yValue = data.get("yValue");

		SchemaComparable selectAttr = (SchemaComparable) webQuery.getSchemaAttr(xAttr, "normal");
		SchemaBase selectCount = webQuery.getSchemaAttr(xAttr, "count");
		builder = builder.select(selectAttr.as("attr"), selectCount.as("count"));
		
		if (yAttr.equals("conference")) {

			builder = builder.from(yValue);
		} else {
			SchemaComparable whereAttr = (SchemaComparable) webQuery.getSchemaAttr(yAttr, "normal");
			SchemaPredicate predicate = webQuery.getSchemaPredi(whereAttr, yValue, null, "eqt");
			builder = builder.from("A4").where(predicate);
		}

		Query query = builder.groupBy(selectAttr)
				.orderBy(selectCount, APIQueryBuilder.OrderByRule.DESC)
				.limit(n)
				.build();
		System.out.println("savingFile");
		query.executeAndSaveInCSV("5");
		System.out.println("file saved");
		
	}

	private Map<String, String> parseQueryString(String queryString) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String[] keyValuePairs = queryString.split("&");

        for (String keyValuePair : keyValuePairs) {
            String[] keyAndValue = keyValuePair.split("=");
            builder.put(keyAndValue[0], keyAndValue[1]);
        }

        return builder.build();
    }
}
