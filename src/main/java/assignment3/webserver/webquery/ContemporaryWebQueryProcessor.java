package assignment3.webserver.webquery;

import assignment3.api.APIQueryBuilder;
import assignment3.api.ConferenceData;
import assignment3.api.Query;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Contemporary comparison")
public class ContemporaryWebQueryProcessor implements WebQueryProcessor {

	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        
        String firstVenue = webRequest.getValue("firstVenue");
        String secondVenue = webRequest.getValue("secondVenue");
        Boolean isExact = Boolean.valueOf(webRequest.getValue("Exact"));
        int year = Integer.parseInt(webRequest.getValue("Year"));
        
        System.out.println("comparing " + firstVenue + " " + secondVenue + " " + year);
        SchemaPredicate predicate;
        predicate = ConferenceData.YEAR_OF_PUBLICATION.equalsTo(year);
        if (isExact) {
        	predicate = predicate
        			.and(ConferenceData.CITATION.citationVenue.equalsToIgnoreCase(firstVenue))
        			.or(ConferenceData.CITATION.citationVenue.equalsToIgnoreCase(secondVenue));
        } else {
        	predicate = predicate
        			.and(ConferenceData.CITATION.citationVenue.like(firstVenue))
        			.or(ConferenceData.CITATION.citationVenue.like(secondVenue));    	
        }
        Query query = builder
        		.select(ConferenceData.CITATION.citationVenue.as("x"),
        				new SchemaCount(ConferenceData.CITATION.citationVenue).as("y"))
        		.from(WebQueryProcessor.DEFAULT_CONFERENCE)
        		.where(predicate)
        		.groupBy(ConferenceData.CITATION.citationVenue)
        		.build();
        query.executeAndSaveInCSV("1.csv");
        return false;
	}

	@Override
	public String getHtmlFileName() {
		// TODO Auto-generated method stub
		return null;
	}

}
