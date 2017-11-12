package assignment3.webserver.webquery;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Citation Web")
public class CitationWebWebQueryProcessor implements WebQueryProcessor {
	
	@Override
	public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
		String premadeQuery = webRequest.getValue("premadeQuery");
		System.out.println(premadeQuery);
		if(premadeQuery == "Citation Web for base paper"){
			CitationNetworkBasePaperWebQueryProcessor citationNetworkBasePaper = new CitationNetworkBasePaperWebQueryProcessor();
			citationNetworkBasePaper.processAndSaveIntoCSV(manager, webRequest);
		}
		else if(premadeQuery == "Citation Web Forest in range of years"){
			ForestWebQueryProcessor forest = new ForestWebQueryProcessor();
			forest.processAndSaveIntoCSV(manager, webRequest);
		}
		return true;
	}

	@Override
	public String getHtmlFileName() {
		//need to return "Forest" or "CollapsibleTree"
		return "CollapsibleTree";
	}
}
