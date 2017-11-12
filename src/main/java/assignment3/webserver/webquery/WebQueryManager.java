package assignment3.webserver.webquery;

import java.util.Optional;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.WebServerRegistry;
import assignment3.webserver.webrequest.WebRequest;

public class WebQueryManager implements WebQuery {

    private static final WebServerRegistry<WebQueryProcessor> registry =
            new WebServerRegistry<>(WebQueryProcessor.class.getPackage().getName());

    private final WebServerManager manager;
    private boolean latestResult = false;
    private WebQueryProcessor queryProcessor;

    public WebQueryManager(WebServerManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean executeAndSaveResultIntoCsvFile(WebRequest query) {
        latestResult = false;

        String queryType = query.getValue("premadeType");
        Optional<WebQueryProcessor> processor = registry.getHandler(queryType);

        processor.ifPresent(p -> getProcessorAndExecute(p, query));

        return latestResult;
    }

    @Override
    public String getHtmlFileName() {
        if (latestResult) {
            return queryProcessor.getHtmlFileName();
        } else {
            return "";
        }
    }

    private void getProcessorAndExecute(WebQueryProcessor processor, WebRequest query) {
        queryProcessor = processor;
        latestResult = processor.processAndSaveIntoCSV(manager, query);
    }
}
