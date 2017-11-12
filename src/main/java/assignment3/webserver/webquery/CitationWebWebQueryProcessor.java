package assignment3.webserver.webquery;

import static assignment3.webserver.WebServerConstants.PREMADE_QUERIES;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.RegisterProcessor;
import assignment3.webserver.webrequest.WebRequest;

@RegisterProcessor( requestType = "Citation Web")
public class CitationWebWebQueryProcessor implements WebQueryProcessor {

    private static final String CITATION_WEB = PREMADE_QUERIES.get(5).name;
    private static final String CITATION_FOREST = PREMADE_QUERIES.get(6).name;

    private WebQueryProcessor processor;

    @Override
    public boolean processAndSaveIntoCSV(WebServerManager manager, WebRequest webRequest) {
        String premadeQuery = webRequest.getValue("premadeQuery");

        if(CITATION_WEB.equalsIgnoreCase(premadeQuery)) {
            processor = new CitationNetworkBasePaperWebQueryProcessor();

        } else if(CITATION_FOREST.equalsIgnoreCase(premadeQuery)) {
            processor = new ForestWebQueryProcessor();

        } else {
            return false;
        }

        return processor.processAndSaveIntoCSV(manager, webRequest);
    }

    @Override
    public String getHtmlFileName() {
        return processor.getHtmlFileName();
    }
}
