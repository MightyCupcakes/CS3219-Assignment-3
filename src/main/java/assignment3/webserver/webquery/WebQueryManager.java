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

    @Override
    public boolean executeAndSaveResultIntoCsvFile(WebRequest query) {
        String queryType = query.getValue("premadeType");
        Optional<WebQueryProcessor> processor = registry.getHandler(queryType);

        return processor.isPresent() && processor.get().processAndSaveIntoCSV(manager, query);
    }
}
