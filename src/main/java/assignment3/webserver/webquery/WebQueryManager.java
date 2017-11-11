package assignment3.webserver.webquery;

import java.util.Optional;

import assignment3.webserver.WebServerManager;
import assignment3.webserver.registry.WebServerRegistry;
import assignment3.webserver.webrequest.WebRequest;

public class WebQueryManager implements WebQuery {

	private static final WebServerRegistry<WebQueryProcessor> registry =
            new WebServerRegistry<>(WebQueryProcessor.class.getPackage().getName());

	private final WebServerManager manager;

	public WebQueryManager(WebServerManager manager) {
	    this.manager = manager;
    }

    @Override
    public boolean executeAndSaveResultIntoCsvFile(WebRequest query) {
        String queryType = query.getValue("premadeType");
        Optional<WebQueryProcessor> processor = registry.getHandler(queryType);

        return processor.isPresent() && processor.get().processAndSaveIntoCSV(manager, query);
    }
}
