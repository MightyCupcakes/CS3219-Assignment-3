package assignment3.webserver.webquery;

import assignment3.api.APIQueryBuilder;
import assignment3.api.ConferenceData;
import assignment3.api.Query;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Transition over Time")
public class TransitionOverTimeWebQueryProcessor implements WebQueryProcessor {
	private static final String QUERY_FOR_YEARS = "Number of citations for a conference over a few years";
	private static final String QUERY_FOR_CONFS = "Number of citations for different conferences";
	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();

		String premadeType = webRequest.getValue("premadeQuery");
		Query query = null;
		if (premadeType.equals(QUERY_FOR_YEARS)) {
			query = getQueryForMultipleYears(builder, webRequest);
		} else if (premadeType.equals(QUERY_FOR_CONFS)) {
			query = getQueryForMultipleConfs(builder, webRequest);
		}
		
		query.executeAndSaveInCSV("1.csv");
		return true;
	}

	@Override
	public String getHtmlFileName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Query getQueryForMultipleYears(APIQueryBuilder builder, WebRequest request) {
		int endYear = getHighestYear(request);
		int startYear = getLowestYear(request);
		String conf = request.getValue("conferenceValue");
		builder = builder.select(ConferenceData.CITATION.year, 
				new SchemaCount(ConferenceData.CITATION.title))
				.from(conf)
				.where(ConferenceData.CITATION.year.greaterThanOrEqualsTo(startYear)
						.and(ConferenceData.CITATION.year.lessThanOrEqualsTo(endYear)))
				.groupBy(ConferenceData.CITATION.year)
				.orderBy(ConferenceData.CITATION.year, APIQueryBuilder.OrderByRule.ASC);
				
		return builder.build();
	}
	private static Query getQueryForMultipleConfs(APIQueryBuilder builder, WebRequest request) {
		return null;
	}
	private static int getHighestYear(WebRequest request) {
		return Math.max(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
	}
	private static int getLowestYear(WebRequest request) {
		return Math.min(Integer.parseInt(request.getValue("startYearDate")), Integer.parseInt(request.getValue("endYearDate")));
	}

}
