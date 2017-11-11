package assignment3.webserver.webquery;

import assignment3.api.APIQueryBuilder;
import assignment3.api.Query;
import assignment3.schema.SchemaBase;
import assignment3.schema.SchemaComparable;
import assignment3.schema.SchemaPredicate;
import assignment3.schema.aggregate.SchemaCount;
import assignment3.webserver.WebServer;
import assignment3.webserver.WebServerConstants;
import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Top N X of Y")
public class TopNXofYWebQueryProcessor implements WebQueryProcessor{
	private static final String DEFAULT_FILE = "donut.html";
	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
        APIQueryBuilder builder = manager.getAPI().getQueryBuilder();
        
        int topn = Integer.parseInt(webRequest.getValue("nform"));
        String xAttr = webRequest.getValue("xform");
        String yAttr = webRequest.getValue("yform");
        Boolean isExact = Boolean.valueOf(webRequest.getValue("isExact"));
        SchemaBase selectAttrX = WebServerConstants.COLUMNS.get(xAttr);
        SchemaComparable searchAttrY = (SchemaComparable) WebServerConstants.COLUMNS.get(yAttr);
        SchemaCount count;
        SchemaComparable base;
        
        if (!xAttr.equals("Citations")) {
        	base = (SchemaComparable) WebServerConstants.COLUMNS.get("Journals");
        	System.out.println("counting by : " + base.getNameOfAttribute());
        	count = new SchemaCount(base);
        } else {
        	base = (SchemaComparable) WebServerConstants.COLUMNS.get(yAttr);
        	System.out.println("counting by : " + base.getNameOfAttribute());
        	count = new SchemaCount(base);      	
        }
        
        System.out.println("selecting : " + selectAttrX.getNameOfAttribute());
        builder = builder.select(selectAttrX, count).from(WebQueryProcessor.DEFAULT_CONFERENCE);
       
        SchemaPredicate predicate;
        if (yAttr.equals("Journal Published Year")) {
        	int yValueYear = Integer.parseInt(webRequest.getValue("yformValueYear"));
        	predicate = searchAttrY.equalsTo(yValueYear);
        } else {
        	String yValue = webRequest.getValue("yformValue");
        	if (isExact) {
        		System.out.println("exact search");
        		predicate = searchAttrY.equalsToIgnoreCase(yValue);
        	} else {
        		System.out.println("like search");
        		predicate = searchAttrY.like(yValue);
        	}
        }
        
        Query query = builder.where(predicate)
        		.groupBy((SchemaComparable)selectAttrX)
        		.orderBy(count, APIQueryBuilder.OrderByRule.DESC)
        		.limit(topn)
        		.build();
        		
		return false;
	}

    @Override
    public String getHtmlFileName() {
        return null;
    }
}
